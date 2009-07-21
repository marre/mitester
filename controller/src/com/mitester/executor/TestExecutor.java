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
 * Package 						License 											Details
 *---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Jain SIP stack 				NIST-CONDITIONS-OF-USE 								https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 						The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * JNetStreamStandalone lib     GNU Library or LGPL			     					http://sourceforge.net/projects/jnetstream/
 * 
 */

/* miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications. 
 */
package com.mitester.executor;

import static com.mitester.executor.ExecutorConstants.COMMA_SEPARATOR;
import static com.mitester.executor.ExecutorConstants.MITESTER_MODE;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;
import static com.mitester.utility.UtilityConstants.NORMAL;
import static com.mitester.utility.UtilityConstants.CALL_FLOW_MODE;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.mitester.adapter.Adapter;
import com.mitester.utility.ConsoleReader;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestMode;
import com.mitester.utility.TestResult;
import com.mitester.utility.TestUtility;
import com.mitester.utility.ThreadControl;

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

	private ExecutorService consoleExecutors = Executors.newCachedThreadPool();

	private ClientScriptRunner runClientTests = null;

	private ServerScriptRunner runServerTests = null;

	private ConsoleReader consoleReader = null;

	private CountDownLatch doneClientTest = null;

	private CountDownLatch doneServerTest = null;

	private Adapter sipAdapter = null;

	private boolean isClientExist = false;

	private boolean isCompleted = false;

	private int totalTestCount = 0;

	private int executedTestCount = 0;

	private int passCount = 0;

	private volatile StringBuilder failString = null;

	private volatile boolean isValidationFailed = false;

	private ThreadControl threadControl = null;

	/**
	 * initialize the variables
	 * 
	 */
	public TestExecutor(Adapter sipAdapter, ThreadControl threadControl) {

		this.sipAdapter = sipAdapter;

		this.threadControl = threadControl;

		// initialize the ClientScriptRunner
		runClientTests = new ClientScriptRunner(sipAdapter, this);

		// initialize the ServerScriptRunner
		runServerTests = new ServerScriptRunner(this, threadControl);

		// initialize the ConsoleReader
		consoleReader = new ConsoleReader(this, threadControl);

	}

	/**
	 * execute the test scripts
	 * 
	 * @param clientTests
	 *            is a List of com.mitester.jaxbparser.client.TEST's includes
	 *            set of client Tests and its actions
	 * @param serverTests
	 *            is a List of com.mitester.jaxbparser.server.TEST's includes
	 *            set of server Tests and its actions
	 * @throws InterruptedException
	 */

	public void executeTest(
			List<com.mitester.jaxbparser.client.TEST> clientTests,
			List<com.mitester.jaxbparser.server.TEST> serverTests)
			throws InterruptedException {

		LOGGER.info("entered into executeTest");

		// start reading message from the console
		serverExecutors.execute(consoleReader.readConsoleMessages());

		switch (TestMode.getTestModefromString(CONFIG_INSTANCE
				.getValue(MITESTER_MODE))) {
		case ADVANCED: {

			// count number of client tests
			totalTestCount = clientTests.size();

			for (com.mitester.jaxbparser.client.TEST clientTest : clientTests) {

				BigInteger count = clientTest.getCOUNT();

				if (count != null) {
					totalTestCount = totalTestCount + (count.intValue() - 1);
				}
			}

			// Execute both client and server Tests
			executeAdvancedTest(clientTests, serverTests);

			break;
		}
		case USER: {

			// count number of server tests
			totalTestCount = serverTests.size();

			for (com.mitester.jaxbparser.server.TEST serverTest : serverTests) {

				BigInteger count = serverTest.getCOUNT();

				if (count != null) {
					totalTestCount = totalTestCount + (count.intValue() - 1);
				}
			}
			// execute server test
			executeUserTest(serverTests);
			break;
		}

		}
		// stop client
		if (isClientExist && sipAdapter.isConnected()) {
			LOGGER
					.info("stopping client as all the test executions are completed");

			if (sipAdapter.stop())
				LOGGER.info("SUT is closed");
			else
				LOGGER.info("closing or stopping SUT is failed");

		} else {
			// remove shell if exist
			TestUtility.removeShell();
		}

		isCompleted = true;

		// stop reading message from the console
		shutdownConsoleExecutor();

		// shut down the thread execution
		shutdownClientExecutor();

		shutdownServerExecutor();

		shutDownClientTimerExecutor();

		shutDownServerTimerExecutor();

		runServerTests.shutDownRtpExecutors();

		runServerTests.shutDownRtcpExecutors();

	}

	/**
	 * execute the test scripts in ADVANCED mode
	 * 
	 * @param clientTests
	 *            List of client Tests which consists of set of client Tests and
	 *            its actions
	 * @param serverTests
	 *            List of server Tests which consists of set of server Tests and
	 *            its actions
	 */

	private void executeAdvancedTest(
			List<com.mitester.jaxbparser.client.TEST> clientTests,
			List<com.mitester.jaxbparser.server.TEST> serverTests) {

		try {

			LOGGER.info("miTesterforSIP running in ADVANCED mode");
			TestUtility.printMessage(NORMAL,
					"miTesterforSIP running in ADVANCED mode");

			boolean isExecutionFailed = false;

			for (com.mitester.jaxbparser.client.TEST clientTest : clientTests) {

				boolean isSuccess = false;

				boolean setFail = false;

				if (threadControl.getStopExecution())
					threadControl.stopExecution();

				// takes the thread control
				if (threadControl.isThreadStop()) {
					TestUtility.printMessage(NORMAL,
							"Press 'p' + ENTER key to resume the execution");
					threadControl.take();
				} else
					threadControl.take();

				String clientTestID = clientTest.getTESTID();
				TestUtility.printMessage(NORMAL, clientTestID + " Started");
				LOGGER.info(clientTestID + " Started");

				// set Test ID
				TestResult.setTestID(clientTestID);

				// set call flow
				TestUtility.callFlow();

				BigInteger count = clientTest.getCOUNT();

				int loopCount = 1;

				if (count != null) {
					loopCount = count.intValue();
				}

				for (int i = 0; i < loopCount; i++) {

					// initialize the test executors
					InitTestExecutors();

					isSuccess = false;

					if (!isClientExist) {

						// suspend the server execution until SUT gets started
						threadControl.suspend();

						// start the check timer
						threadControl.startCheckTimer();
					}

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

					// wait for client test complete
					doneClientTest.await();

					// wait for server test complete
					doneServerTest.await();

					if ((!runClientTests.getClientTestStart())
							|| (!runServerTests.getServerTestStart())) {
						isExecutionFailed = true;
						break;
					}

					// update test result
					if ((runClientTests.getClientTestResult())
							&& (runServerTests.getServerTestResult())) {

						TestResult.updateResult(clientTestID, true);

						if (!sipAdapter.checkClientAvailable()) {

							// clean up the socket
							sipAdapter.cleanUpSocket();
							isClientExist = false;

						} else
							isClientExist = true;

						if (!setFail)
							isSuccess = true;

						passCount++;

					} else {

						TestResult.updateResult(clientTestID, false);

						if (!sipAdapter.isClosed() && sipAdapter.isStopCalled()) {

							TestUtility.printMessage("Failed to kill the SUT");
							LOGGER.error("Failed to kill the SUT");
							isExecutionFailed = true;
							break;
						} else if (!sipAdapter.isClosed()
								&& !sipAdapter.isStopCalled()) {

							if (!sipAdapter.checkClientAvailable()) {

								// clean up the socket
								sipAdapter.cleanUpSocket();
								isClientExist = false;

							} else
								isClientExist = true;

						} else if (sipAdapter.isClosed()) {

							// clean up the socket
							sipAdapter.cleanUpSocket();
							isClientExist = false;
							LOGGER.info("SUT does not exist");
						}

						if (TestUtility.getModeOfDisplay().equalsIgnoreCase(
								CALL_FLOW_MODE))
							TestUtility.printMessage(NORMAL, "");
					}

					executedTestCount++;

					if ((loopCount > 1) && (!isSuccess))
						setFail = true;

					failString = null;
				}

				if (isSuccess)
					TestUtility.printMessage(NORMAL, clientTestID
							+ " Ended PASS");
				else
					TestUtility.printMessage(NORMAL, clientTestID
							+ " Ended FAIL");

				LOGGER.info(clientTestID + " Ended");

				if (isExecutionFailed) {

					// clean up the socket
					sipAdapter.cleanUpSocket();

					break;
				}

			}

		} catch (NullPointerException ex) {
			TestUtility.printError(
					"Error while executing tests in Advanced mode", ex);
		} catch (InterruptedException ex) {
			TestUtility.printError(
					"Error while executing tests in Advanced mode ", ex);
		} catch (RuntimeException ex) {
			TestUtility.printError(
					"Error while executing tests in Advanced mode ", ex);
		} catch (Exception ex) {
			TestUtility.printError(
					"Error while executing tests in Advanced mode ", ex);
		}
	}

	/**
	 * execute the test scripts in USER mode
	 * 
	 * @param serverTests
	 *            List of server Tests which consists of set of server Tests and
	 *            its actions
	 */
	private void executeUserTest(
			List<com.mitester.jaxbparser.server.TEST> serverTests) {

		try {

			LOGGER.info("miTesterforSIP running in USER mode");
			TestUtility.printMessage(NORMAL,
					"miTesterforSIP running in USER mode");

			boolean isExecutionFailed = false;

			for (com.mitester.jaxbparser.server.TEST serverTest : serverTests) {

				boolean isSuccess = false;

				boolean setFail = false;

				String serverTestID = serverTest.getTESTID();
				TestUtility.printMessage(NORMAL, serverTestID + " Started");
				LOGGER.info(serverTestID + " Started");

				TestUtility.callFlow();

				BigInteger count = serverTest.getCOUNT();

				int loopCount = 1;

				if (count != null) {
					loopCount = count.intValue();
				}

				for (int i = 0; i < loopCount; i++) {

					// initialize the test executors
					InitTestExecutors();

					isSuccess = false;

					doneServerTest = new CountDownLatch(1);

					serverExecutors.execute(runServerTests
							.frameServerRunnable(serverTest));

					// waiting for server thread to get finish
					doneServerTest.await();

					if (!runServerTests.getServerTestStart()) {
						isExecutionFailed = true;
						break;
					}

					// update test result
					if ((runServerTests.getServerTestResult())) {

						TestResult.updateResult(serverTestID, true);

						if (!setFail)
							isSuccess = true;

						passCount++;

					} else {
						TestResult.updateResult(serverTestID, false);

						if (TestUtility.getModeOfDisplay().equalsIgnoreCase(
								CALL_FLOW_MODE))
							TestUtility.printMessage(NORMAL, "");
					}

					executedTestCount++;

					if ((loopCount > 1) && (!isSuccess))
						setFail = true;

					failString = null;
				}

				if (isExecutionFailed)
					break;

				if (isSuccess)
					TestUtility.printMessage(NORMAL, serverTestID
							+ " Ended PASS");
				else
					TestUtility.printMessage(NORMAL, serverTestID
							+ " Ended FAIL");

				LOGGER.info(serverTestID + " Ended");

			}

		} catch (NullPointerException ex) {
			TestUtility.printError("Error while executing tests in User mode",
					ex);
		} catch (InterruptedException ex) {
			TestUtility.printError("Error while executing tests in User mode",
					ex);
		} catch (RuntimeException ex) {
			TestUtility.printError("Error while executing tests in User mode",
					ex);
		} catch (Exception ex) {
			TestUtility.printError("Error while executing tests in User mode",
					ex);
		}
	}

	/**
	 * 
	 * @return client count down latch
	 */

	public CountDownLatch getClientTestCountDownLatch() {
		return doneClientTest;
	}

	/**
	 * 
	 * @return server count down latch
	 */

	public CountDownLatch getServerTestCountDownLatch() {
		return doneServerTest;
	}

	/**
	 * shut down the client thread execution
	 */
	private void shutdownClientExecutor() {

		LOGGER.info("called shutdownClientExecutor");

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

		LOGGER.info("called shutdownServerExecutor");

		try {
			serverExecutors.shutdownNow();
			serverExecutors.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {

		}

	}

	/**
	 * shutdown console executor
	 */
	private void shutdownConsoleExecutor() {

		LOGGER.info("called shutdownConsoleExecutor");

		try {
			consoleExecutors.shutdownNow();
			consoleExecutors.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {

		}
	}

	/**
	 * shut down the client timer thread execution
	 */
	private void shutDownClientTimerExecutor() {

		LOGGER.info("called shutDownClientTimerExecutor");

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

		LOGGER.info("called shutDownServerTimerExecutor");

		try {
			serverExecutors.shutdownNow();
			serverExecutors.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {

		}

	}

	/**
	 * Initialize test executors
	 */
	private void InitTestExecutors() {

		// set fail reason
		TestResult.setFailReason(null);

		failString = new StringBuilder();

		isValidationFailed = false;

		switch (TestMode.getTestModefromString(CONFIG_INSTANCE
				.getValue(MITESTER_MODE))) {
		case ADVANCED: {

			doneClientTest = new CountDownLatch(1);

			doneServerTest = new CountDownLatch(1);

			break;
		}
		case USER: {

			doneServerTest = new CountDownLatch(1);

			break;
		}
		}

	}

	/**
	 * It returns execution completion status
	 * 
	 * @return true when execution of test completed
	 */
	public boolean isCompleted() {
		return isCompleted;
	}

	/**
	 * It returns total number of test count
	 * 
	 * @return total test count
	 */
	public int getTotalTestCount() {
		return totalTestCount;

	}

	/**
	 * It returns executed test count
	 * 
	 * @return executed test count
	 */

	public int getExecutedTestCount() {
		return executedTestCount;
	}

	/**
	 * It returns pass test count
	 * 
	 * @return pass test count
	 */
	public int getPassCount() {
		return passCount;
	}

	/**
	 * buffer the fail string
	 * 
	 * @param fail
	 *            represents fail string
	 * 
	 */
	public void bufferFailString(String failStr) {

		if (!failString.toString().equals(""))
			failString.append(COMMA_SEPARATOR + " ");
		failString.append(failStr);
	}

	/**
	 * return the buffered fail string
	 * 
	 * @return buffered fail string
	 */
	public String getBufferedFailString() {
		return failString.toString();
	}

	/**
	 * set the validation failed status
	 * 
	 * @param isValidationFailed
	 *            is a boolean value represent validation status
	 */
	public void setIsValidationFailed(boolean isValidationFailed) {
		this.isValidationFailed = isValidationFailed;
	}

	/**
	 * return the validation failed status
	 * 
	 * @return validation failed status
	 */
	public boolean getIsValidationFailed() {
		return isValidationFailed;
	}

}
