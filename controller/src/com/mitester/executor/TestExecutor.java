/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: TestExecutor.java
 * Copyright (C) 2008 - 2009  Mobax Networks Private Limited
 * miTester for SIP – License Information
 * --------------------------------------------------
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>
 * 
 * LICENSE INFORMATION REGARDING RELATED THIRD-PARTY SOFTWARE
 * -----------------------------------------------------------------------------------------
 * The miTester for SIP relies on the following third party software. Below is the location and license information :
 *---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Package 					License 											Details
 *---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Jain SIP stack 			NIST-CONDITIONS-OF-USE 								https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 					The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * 
 */

/* miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications. 
 */
package com.mitester.executor;

import static com.mitester.executor.ExecutorConstants.TEST_MODE;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.mitester.adapter.Adapter;
import com.mitester.sipserver.SIPHeaderValidator;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestMode;
import com.mitester.utility.TestResult;
import com.mitester.utility.TestUtility;

/**
 * 
 * This class starts the test execution according to the test mode (ADVANCED or
 * USER) and updates the test result at the end of every test execution.
 */

public class TestExecutor {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(TestExecutor.class.getName());

	private ExecutorService clientExecutors = Executors.newCachedThreadPool();

	private ExecutorService serverExecutors = Executors.newCachedThreadPool();

	private ClientScriptRunner runClientTests = null;

	private ServerScriptRunner runServerTests = null;

	private CountDownLatch clientLatch = null;

	private CountDownLatch serverLatch = null;

	private Adapter sipAdapter = null;

	/**
	 * initialize the variables
	 * 
	 */
	public TestExecutor(Adapter sipAdapter,
			SIPHeaderValidator sipHeaderValidator) {

		this.sipAdapter = sipAdapter;

		// initialize the ClientScriptRunner
		runClientTests = new ClientScriptRunner(sipAdapter, this);

		// initialize the ServerScriptRunner
		runServerTests = new ServerScriptRunner(sipHeaderValidator, this);

	}

	/**
	 * execute the test scripts
	 * 
	 * @param clientTests
	 *            is a com.mitester.jaxbparser.client.TEST includes set of
	 *            client Tests and its actions
	 * @param serverTests
	 *            is a com.mitester.jaxbparser.server.TEST includes set of
	 *            server Tests and its actions
	 */

	public void executeTest(
			List<com.mitester.jaxbparser.client.TEST> clientTests,
			List<com.mitester.jaxbparser.server.TEST> serverTests) {

		switch (TestMode.getTestModefromString(CONFIG_INSTANCE
				.getValue(TEST_MODE))) {
		case ADVANCED: {

			// Execute both client and server Tests */
			executeAdvancedTest(clientTests, serverTests);

			break;
		}
		case USER: {

			// execute server test
			executeUserTest(serverTests);
			break;
		}

		}

		// stop client
		if (sipAdapter.isRunning())
			sipAdapter.stop();

		// shut down the thread execution
		shutdownClientExecutor();
		shutdownServerExecutor();
		shutDownClientTimerExecutor();
		shutDownServerTimerExecutor();

	}

	/**
	 * execute the ADVANCED mode of test scripts
	 * 
	 * @param clientTests
	 *            is a com.mitester.jaxbparser.client.TEST includes set of
	 *            client Tests and its actions
	 * @param serverTests
	 *            is a com.mitester.jaxbparser.server.TEST includes set of
	 *            server Tests and its actions
	 */

	private void executeAdvancedTest(
			List<com.mitester.jaxbparser.client.TEST> clientTests,
			List<com.mitester.jaxbparser.server.TEST> serverTests) {

		try {

			LOGGER.info("miTester running in ADVANCED mode");

			for (com.mitester.jaxbparser.client.TEST clientTest : clientTests) {
				clientLatch = new CountDownLatch(1);
				serverLatch = new CountDownLatch(1);
				String clientTestID = clientTest.getTESTID();
				TestUtility.printMessage(clientTestID + " Started");
				LOGGER.info(clientTestID + " Started");

				for (com.mitester.jaxbparser.server.TEST serverTest : serverTests) {

					String serverTestID = serverTest.getTESTID();
					if (clientTestID.equals(serverTestID)) {

						serverExecutors.execute(runServerTests
								.frameServerRunnable(serverTest));
						break;
					}

				}

				clientExecutors.execute(runClientTests
						.frameClientRunnable(clientTest));

				// waiting for client test to get finish
				clientLatch.await();

				// waiting for server test to get finish
				serverLatch.await();

				if ((!runClientTests.getClientTestStart())
						|| (!runServerTests.getServerTestStart())) {
					break;
				}

				if (sipAdapter.isClosed()) {

					// clean up the socket
					sipAdapter.cleanUpSocket();
				}

				// update test result
				if ((runClientTests.getClientTestResult())
						&& (runServerTests.getServerTestResult())) {
					TestResult.updateResult(clientTestID, true);
				} else {
					TestResult.updateResult(clientTestID, false);
				}

				TestUtility.printMessage(clientTestID + " Ended");
				LOGGER.info(clientTestID + " Stopped");

				if ((!runClientTests.getClientTestResult())
						|| (!runServerTests.getServerTestResult())) {
					LOGGER.info("check whether client stopped or not");
					if ((!sipAdapter.isClosedDefault())
							&& (!sipAdapter.isClosed())) {
						TestUtility
								.printMessage("Error at Stopping the Client");
						break;
					}

				}
			}

		} catch (NullPointerException ex) {
			TestUtility.printError("Error at executing client test ", ex);
		} catch (InterruptedException ex) {
			TestUtility.printError("Error at executing client test ", ex);
		} catch (RuntimeException ex) {
			TestUtility.printError("Error at executing client test ", ex);
		} catch (Exception ex) {
			TestUtility.printError("Error at executing client test ", ex);
		}
	}

	/**
	 * execute the USER mode of Test scripts
	 * 
	 * @param serverTests
	 *            is a com.mitester.jaxbparser.server.TEST includes set of
	 *            server Tests and its actions
	 */
	private void executeUserTest(
			List<com.mitester.jaxbparser.server.TEST> serverTests) {

		try {

			LOGGER.info("miTester running in USER mode");

			for (com.mitester.jaxbparser.server.TEST serverTest : serverTests) {

				String serverTestID = serverTest.getTESTID();
				TestUtility.printMessage(serverTestID + " Started");
				LOGGER.info(serverTestID + " Started");
				serverLatch = new CountDownLatch(1);
				serverExecutors.execute(runServerTests
						.frameServerRunnable(serverTest));

				// waiting for server thread to get finish
				serverLatch.await();

				if ((!runServerTests.getServerTestStart())) {
					break;
				}

				// update test result
				if ((runServerTests.getServerTestResult())) {
					TestResult.updateResult(serverTestID, true);
				} else {
					TestResult.updateResult(serverTestID, false);
				}

				TestUtility.printMessage(serverTestID + " Ended");
				LOGGER.info(serverTestID + " Stopped");

			}

		} catch (NullPointerException ex) {
			TestUtility.printError("Error at executing server test ", ex);
		} catch (InterruptedException ex) {
			TestUtility.printError("Error at executing server test ", ex);
		} catch (RuntimeException ex) {
			TestUtility.printError("Error at executing server test ", ex);
		} catch (Exception ex) {
			TestUtility.printError("Error at executing server test ", ex);
		}
	}

	/**
	 * 
	 * @return client Test count down latch object
	 */

	public CountDownLatch getClientCountDownLatch() {
		return clientLatch;
	}

	/**
	 * 
	 * @return server Test count down latch object
	 */
	public CountDownLatch getServerCountDownLatch() {
		return serverLatch;
	}

	/**
	 * shut down the client thread execution
	 */
	private void shutdownClientExecutor() {
		try {
			clientExecutors.shutdownNow();
			clientExecutors.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * shut down the server thread execution
	 */
	private void shutdownServerExecutor() {
		try {
			serverExecutors.shutdownNow();
			serverExecutors.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {

		}

	}

	/**
	 * shut down the client timer thread execution
	 */
	private void shutDownClientTimerExecutor() {

		try {
			ExecutorService clientTimerExecutor = runClientTests
					.getClientScheduledExecutorService();
			clientTimerExecutor.shutdownNow();
			clientTimerExecutor.awaitTermination(3, TimeUnit.SECONDS);

		} catch (InterruptedException e) {

		}
	}

	/**
	 * shut down the server timer thread execution
	 */
	private void shutDownServerTimerExecutor() {
		try {
			serverExecutors.shutdownNow();
			serverExecutors.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {

		}

	}

	// /**
	// * stop the client test execution
	// */
	//
	// public void stopClientExecution() {
	//
	// runClientTests.stopClientTestExecution();
	//
	// }
	//
	// /**
	// * stop the server test execution
	// */
	//
	// public void stopServerExecution() {
	//
	// runServerTests.stopServerTestExecution();
	// }

}
