/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ClientScriptRunner.java
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

import static com.mitester.executor.ExecutorConstants.CLIENT_WAIT_TIME;
import static com.mitester.executor.ExecutorConstants.CLIENT_WAIT_TIME_SEC;
import static com.mitester.executor.ExecutorConstants.COMMA_SEPARATOR;
import static com.mitester.executor.ExecutorConstants.DISCARD_WAIT_TIME_SEC;
import static com.mitester.executor.ExecutorConstants.EXECUTION_INTERVAL;
import static com.mitester.executor.ExecutorConstants.CLIENT_EXECUTION_INTERVAL;
import static com.mitester.executor.ExecutorConstants.SEC;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.mitester.adapter.Adapter;
import com.mitester.jaxbparser.client.PARAM;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;

/**
 * This class automates SIP client functionalities by sending client actions to
 * SUT through the TCP channel established by an Adapter and controls the client
 * actions according to the flow described in the test script. It implements
 * Timer threads which are used to suspend the client actions temporarily for
 * the specified time duration and sets the maximum time to the controller will
 * wait for receiving notifications from SUT. It also set the test result
 * status.
 * 
 */

public class ClientScriptRunner {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(ClientScriptRunner.class.getName());

	private final int MAXIMUM_CLIENT_WAIT_TIME;

	private final int CLIENT_TEST_EXECUTION_INTERVAL;

	private ScheduledExecutorService clientTimerScheduler = Executors
			.newScheduledThreadPool(1);

	private boolean isClientTestStart = false;

	private boolean isClientTestSucceed = false;

	private ScheduledFuture<?> clientTestEndTimer = null;

	private TestExecutor testExecutor = null;

	private Adapter adapter = null;

	/**
	 * constructor initialize the adapter,testExecutor and
	 * MAXIMUM_CLIENT_WAIT_TIME variables
	 * 
	 * @param adapter
	 *            is an Adapter interface object
	 * @param testExecutor
	 *            is a TestExecutor class object
	 */

	public ClientScriptRunner(Adapter adapter, TestExecutor testExecutor) {

		this.adapter = adapter;
		this.testExecutor = testExecutor;

		// set the client wait time
		if (CONFIG_INSTANCE.isKeyExists(CLIENT_WAIT_TIME)) {
			MAXIMUM_CLIENT_WAIT_TIME = Integer.parseInt(CONFIG_INSTANCE
					.getValue(CLIENT_WAIT_TIME));
		} else {
			MAXIMUM_CLIENT_WAIT_TIME = CLIENT_WAIT_TIME_SEC;
		}

		// set the client execution interval
		if (CONFIG_INSTANCE.isKeyExists(EXECUTION_INTERVAL)) {
			CLIENT_TEST_EXECUTION_INTERVAL = Integer.parseInt(CONFIG_INSTANCE
					.getValue(EXECUTION_INTERVAL)) * 1000;
		} else {
			CLIENT_TEST_EXECUTION_INTERVAL = CLIENT_EXECUTION_INTERVAL;
		}

	}

	/**
	 * Execute the client Test
	 * 
	 * @param clientTest
	 *            is a com.mitester.jaxbparser.client.TEST object represents the
	 *            set of client actions
	 * @return Runnable
	 */

	public Runnable frameClientRunnable(
			final com.mitester.jaxbparser.client.TEST clientTest) {

		// cleanUp client Tests
		cleanUpClientTest();

		return new Runnable() {

			public void run() {

				try {

					int actionCount = 0;

					com.mitester.jaxbparser.client.ACTION action = null;

					com.mitester.jaxbparser.client.WAIT wait = null;

					List<Object> clientActions = clientTest.getACTIONOrWAIT();

					boolean isWAIT = false;

					int noOfClientActions = clientActions.size();

					if (startSUT()) {

						isClientTestStart = true;

						for (Object object : clientActions) {

							if (object instanceof com.mitester.jaxbparser.client.ACTION) {

								action = (com.mitester.jaxbparser.client.ACTION) object;
								isWAIT = false;

							} else {

								wait = (com.mitester.jaxbparser.client.WAIT) object;
								isWAIT = true;
							}

							if (isWAIT) {

								TestUtility
										.printMessage("client action in sleeping ...");
								LOGGER.info("client action in sleeping ...");

								// wait for specified time
								waitClientAction(wait);

								TestUtility
										.printMessage("resumed client action ...");
								LOGGER.info("resumed the client action ...");

							} else if (action.getRECV() != null) {

								// receive and process the message
								receiveMsgFromSUT(action.getValue());

							} else if (action.getSEND() != null) {

								// send the message to SUT
								sendMsgtoSUT(action);

							} else if (action.getDISCARD() != null) {

								// wait for discarded message
								if (!waitForDiscardMsg(action.getValue()))
									break;

							} else {
								break;
							}

							actionCount++;

						}
					} else {
						TestUtility
								.printMessage("Error while starting client ...");

					}

					if ((actionCount >= noOfClientActions)
							&& (noOfClientActions > 0)) {
						isClientTestSucceed = true;
					}

				} catch (SocketException ex) {
					TestUtility.printMessage("TCP socket closed");
				} catch (IOException ex) {
					TestUtility.printError("Error while running Client Test",
							ex);
				} catch (NullPointerException ex) {
					TestUtility.printError("Error while running Client Test",
							ex);
				} catch (SecurityException ex) {
					TestUtility.printError("Error while running Client Test",
							ex);
				} catch (IllegalArgumentException ex) {
					TestUtility.printError("Error while running Client Test",
							ex);
				} catch (InterruptedException ex) {
					TestUtility.printError("Error while running Client Test",
							ex);
				} catch (RejectedExecutionException ex) {
					TestUtility.printError("Error while scheduling task", ex);
				} finally {

					// stop the timer if run
					stopClientTimer();

					if (!isClientTestSucceed) {

						if (!adapter.isStopCalled()) {

							LOGGER
									.info("stopping client as the call flow of test execution is incomplete");

							/* stop the application */
							adapter.stop();

						}
					}

					// wait for specified time interval
					startClientWaitTimer((long) CLIENT_TEST_EXECUTION_INTERVAL);

					if (isClientTestSucceed) {
						TestUtility
								.printMessage("client actions completed ...");
						LOGGER.info("client actions completed ...");
					}

					// stop the execution
					if (testExecutor.getClientCountDownLatch().getCount() > 0) {
						testExecutor.getClientCountDownLatch().countDown();
					}
				}
			}
		};
	}

	/**
	 * This method called during the client test execution for suspending server
	 * action temporarily
	 * 
	 * @throws InterruptedException
	 * 
	 */
	private void startClientWaitTimer(long time) {
		try {

			TimeUnit.MILLISECONDS.sleep(time);

		} catch (InterruptedException ex) {

		}

	}

	/**
	 * method called during the client test execution to stop the client timer
	 * 
	 */
	private void stopClientTimer() {

		if (clientTestEndTimer != null) {
			clientTestEndTimer.cancel(true);
			clientTestEndTimer = null;
			LOGGER.info("wait timer stopped");
		}
	}

	/**
	 * This method called when tool started to wait for expected notification
	 * from client. starts timer, wait for specified time duration. if the
	 * expected notification not received in specified time duration, forcefully
	 * ends the client test.
	 * 
	 */
	private void startClientTimer(final Adapter adapter) {

		if (clientTestEndTimer != null) {
			clientTestEndTimer.cancel(true);
			clientTestEndTimer = null;
		}
		LOGGER.info("wait timer started");
		clientTestEndTimer = clientTimerScheduler.schedule(new Runnable() {
			public void run() {

				clientTestEndTimer.cancel(true);
				clientTestEndTimer = null;

				// stop the client
				stopClientTestExecution();

			}
		}, MAXIMUM_CLIENT_WAIT_TIME, TimeUnit.SECONDS);
	}

	/**
	 * This method returns the client start status
	 * 
	 * @return boolean
	 */
	public boolean getClientTestStart() {
		return isClientTestStart;
	}

	/**
	 * This method called to clean-up the client test variables
	 * 
	 */
	private void cleanUpClientTest() {

		isClientTestStart = false;
		isClientTestSucceed = false;
		clientTestEndTimer = null;

	}

	/**
	 * This method returns the client test result
	 * 
	 * @return boolean
	 */
	public boolean getClientTestResult() {
		return isClientTestSucceed;
	}

	/**
	 * This method returns the ScheduledExecutorService
	 * 
	 * @returns ScheduledExecutorService
	 */
	public ScheduledExecutorService getClientScheduledExecutorService() {
		return clientTimerScheduler;
	}

	/**
	 * This method sends the action to SUT
	 * 
	 * @param action
	 *            is a com.mitester.jaxbparser.client.ACTION object includes
	 *            action related information
	 * @throws SocketException
	 * @throws IOException
	 * @throws InterruptedException
	 */

	private void sendMsgtoSUT(com.mitester.jaxbparser.client.ACTION action)
			throws SocketException, IOException, InterruptedException {

		List<PARAM> paramList = null;
		String clientActionValue = null;
		paramList = action.getPARAM();

		StringBuilder tempBuf = new StringBuilder();

		if (paramList.size() > 0) {

			for (PARAM param : paramList) {

				tempBuf.append(param.getValue());
				tempBuf.append(COMMA_SEPARATOR);

			}

			clientActionValue = action.getValue() + COMMA_SEPARATOR
					+ tempBuf.toString();

		} else {

			clientActionValue = action.getValue();
		}

		// send message to SUT
		adapter.send(clientActionValue);

	}

	/**
	 * this method used to suspend the client test execution
	 * 
	 * @param wait
	 *            is a com.mitester.jaxbparser.client.WAIT object includes the
	 *            Time related information
	 */
	private void waitClientAction(com.mitester.jaxbparser.client.WAIT wait) {

		BigDecimal time;

		if (wait.getUnit().equals(SEC)) {
			time = new BigDecimal(wait.getValue().toString());
			time = time.multiply(new BigDecimal("1000"));

		} else {
			time = new BigDecimal(wait.getValue().toString());

		}

		startClientWaitTimer(time.longValueExact());

	}

	/**
	 * miTester whether the client accepts the discarded message if client
	 * accepts discarded message, immediately it stops the execution
	 * 
	 * @throws SocketTimeoutException
	 * @throws SocketException
	 * @throws IOException
	 * @throws InterruptedException
	 */

	private boolean waitForDiscardMsg(String clientActionValue)
			throws SocketTimeoutException, SocketException, IOException,
			InterruptedException {

		// set socket timeout
		adapter.getSocket().setSoTimeout((DISCARD_WAIT_TIME_SEC * 1000));

		String receivedMsg = null;

		try {
			
			long timetoWait = DISCARD_WAIT_TIME_SEC * 1000;

			do {

				long startTime = System.currentTimeMillis();

				receivedMsg = adapter.receive();

				TestUtility.printMessage("MSG from SUT <==" + receivedMsg);

				long elapsedTime = System.currentTimeMillis() - startTime;

				if (elapsedTime < timetoWait) {
					timetoWait = timetoWait
							- elapsedTime;
				} else {
					timetoWait = DISCARD_WAIT_TIME_SEC * 1000;
				}
				
				// set socket timeout
				adapter.getSocket().setSoTimeout((int) timetoWait);

			} while (!(receivedMsg.equals(clientActionValue)));

			TestUtility
					.printMessage("client accepted the discarded SIP message");
			LOGGER.severe("client accepted the discarded SIP message");

			LOGGER
					.info("stopping client as the client accepting the discarded sip message");

			// stop the client
			if (!adapter.stop()) {
				TestUtility
						.printMessage("Not able to kill the application... ");
				LOGGER.severe("Not able to kill the application...");
			}

			return false;

		} catch (SocketTimeoutException ex) {

			// set socket timeout
			adapter.getSocket().setSoTimeout(0);

			return true;

		}

	}

	/**
	 * receive and process the message from SUT and returns true if miTester
	 * receives expected message from SUT
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws SocketException
	 */

	private void receiveMsgFromSUT(String clientActionValue)
			throws SocketException, IOException, InterruptedException {

		String receivedMsg = null;

		// start the timer
		startClientTimer(adapter);

		// LOGGER.info("Expected Message from SUT is " + clientActionValue);

		do {

			// receive message from SUT
			receivedMsg = adapter.receive();

			TestUtility.printMessage("MSG from SUT <==" + receivedMsg);

		} while (!(receivedMsg.equals(clientActionValue)));

		LOGGER.info("Expected Message received " + clientActionValue);

		// stop the timer if run
		stopClientTimer();

	}

	/**
	 * starts the SUT
	 * 
	 * @return true if client started successfully
	 */

	private boolean startSUT() {

		if (!adapter.isConnected()) {

			// start SUT and establish the TCP channel
			if (!adapter.start()) {
//				TestUtility.printMessage("client not started");
				return false;
			}
		} else {
			TestUtility.printMessage("client started");
		}
		return true;
		
	}

	/**
	 * stops the client test execution
	 */

	public void stopClientTestExecution() {

		LOGGER.info("stopping client as the timer got expired");

		// stop the client
		if (!adapter.stop()) {

			TestUtility.printMessage("Not able to kill the application... ");
			LOGGER.severe("Not able to kill the application...");
		}
	}

}
