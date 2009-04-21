/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ServerScriptRunner.java
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

import static com.mitester.executor.ExecutorConstants.*;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;
import gov.nist.javax.sip.message.SIPMessage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;

import com.mitester.sipserver.ProcessSIPMessage;
import com.mitester.sipserver.SIPHeaderValidator;
import com.mitester.sipserver.SendRequestHandler;
import com.mitester.sipserver.SendResponseHandler;

import static com.mitester.sipserver.SipServerConstants.*;
import com.mitester.sipserver.UdpCommn;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;

/**
 * This class controls the server module of the miTester. Sending and receiving
 * of SIP messages are controlled by the actions specified in the server test
 * scripts. It implements Timer threads which are used to suspend the server
 * actions temporarily for specified time duration and sets the maximum time to
 * the controller will wait for receiving SIP messages from SUT. It also sets
 * the test result based on number of actions completed in the test.
 * 
 */
public class ServerScriptRunner {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(ServerScriptRunner.class.getName());

	private final int MAXIMUM_SERVER_WAIT_TIME;

	private final float SERVER_SEND_DELAY;

	private volatile boolean isSocketCloseCalled = false;

	private boolean isServerTestSucceed = false;

	private ScheduledExecutorService serverTimerScheduler = Executors
			.newScheduledThreadPool(1);

	private ScheduledFuture<?> serverTestEndTimer = null;

	private TestExecutor testExecutor = null;

	private SIPHeaderValidator sipHeaderValidator = null;

	private String testMode = CONFIG_INSTANCE.getValue(TEST_MODE);

	private UdpCommn udpCommn = new UdpCommn();

	private boolean isServerTestStart = false;

	/**
	 * constructor initialize sipServer,testExecutor,MAXIMUM_SERVER_WAIT_TIME
	 * and SERVER_SEND_DELAY variables
	 * 
	 * @param CheckPresence
	 *            is a checkPresence class object
	 * @param sipServer
	 *            is a SipServer class object
	 * @param testExecutor
	 *            is a testExecutorclass object
	 */
	public ServerScriptRunner(SIPHeaderValidator sipHeaderValidator,
			TestExecutor testExecutor) {
		this.testExecutor = testExecutor;
		this.sipHeaderValidator = sipHeaderValidator;

		// sets the maximum server wait time
		if (CONFIG_INSTANCE.isKeyExists(SERVER_WAIT_TIME)) {
			MAXIMUM_SERVER_WAIT_TIME = Integer.parseInt(CONFIG_INSTANCE
					.getValue(SERVER_WAIT_TIME));
		} else {
			MAXIMUM_SERVER_WAIT_TIME = SERVER_WAIT_TIME_SEC;
		}

		// sets the server delay on sending SIP message
		if (CONFIG_INSTANCE.isKeyExists(SERVER_DELAY)) {
			SERVER_SEND_DELAY = Float.parseFloat(CONFIG_INSTANCE
					.getValue(SERVER_DELAY)) * 1000;

		} else {
			SERVER_SEND_DELAY = INITIAL_DELAY_SEC;
		}
	}

	/**
	 * Execute the Server Test
	 * 
	 * @param serverTest
	 *            is a com.mitester.jaxbparser.server.TEST object represents the
	 *            set of server actions
	 * @return Runnable
	 */

	public Runnable frameServerRunnable(
			final com.mitester.jaxbparser.server.TEST serverTest) {

		// clean-up server Tests
		cleanUpServerTest();

		// clean-up the SIPMessageList
		ProcessSIPMessage.cleanUpSipMessageList();

		// clean-up the CheckPresenceList
		sipHeaderValidator.cleanUpheaderList();

		return new Runnable() {
			public void run() {

				try {

					int actionCount = 0;

					com.mitester.jaxbparser.server.ACTION action = null;

					com.mitester.jaxbparser.server.WAIT wait = null;

					List<Object> serverActions = serverTest.getACTIONOrWAIT();

					boolean isWAIT = false;

					int noOfServerActions = serverActions.size();

					// initialize the UDP socket
					if (prepareUDP()) {

						isServerTestStart = true;

						for (Object object : serverActions) {

							if (object instanceof com.mitester.jaxbparser.server.ACTION) {

								action = (com.mitester.jaxbparser.server.ACTION) object;
								isWAIT = false;

							} else {

								wait = (com.mitester.jaxbparser.server.WAIT) object;
								isWAIT = true;
							}

							if (isWAIT) {

								TestUtility
										.printMessage("server action in sleeping ...");
								LOGGER.info("server action in sleeping ...");

								// wait for specified time
								waitServerAction(wait);

								TestUtility
										.printMessage("resumed server action ...");
								LOGGER.info("resumed server action ...");

							} else if (action.getRECV() != null) {

								// receive and process the SIP message
								receiveSIPMessage(action);

							} else if (action.getSEND() != null) {

								// send the message to SUT
								if (!sendSIPMessage(action))
									break;

							} else {
								break;
							}

							actionCount++;

						}

					} else {
						TestUtility
								.printMessage("Error at starting server ...");

					}

					if ((actionCount >= noOfServerActions)
							&& (noOfServerActions > 0)) {
						isServerTestSucceed = true;
					}

				} catch (SocketException ex) {
					TestUtility
							.printMessage("UDP datagram socket closed forcefully");
				} catch (NullPointerException ex) {
					TestUtility.printError("Error while executing server test",
							ex);
				} catch (ParseException ex) {
					TestUtility.printError(
							"Error while processing SIP message", ex);
				} catch (SipException ex) {
					TestUtility.printError(
							"Error while processing SIP message", ex);
				} catch (InvalidArgumentException ex) {
					TestUtility.printError("Error while running server test",
							ex);
				} catch (IOException ex) {
					TestUtility.printError("Error while running server test",
							ex);
				} catch (IndexOutOfBoundsException ex) {
					TestUtility.printError("Error while running server test",
							ex);
				} finally {

					try {
						
						// stop timer
						stopServerTimer();

						if (!isSocketCloseCalled) {

							// close UDP socket
							udpCommn.closeUdpSocket();
						}

						// delay at end of execution
						startServerWaitTimer(SERVER_EXECUTION_INTERVAL);

						if (!udpCommn.isBounded())
							LOGGER.info("UDP socket closed ...");

						if (isServerTestSucceed) {
							
							isServerTestSucceed = sipHeaderValidator
									.validateHeaders();
							TestUtility
									.printMessage("server actions completed");
							LOGGER.info("server actions completed ...");

						}
						if ((testExecutor.getServerCountDownLatch().getCount()) > 0) {
							testExecutor.getServerCountDownLatch().countDown();
						}
					} catch (Exception ex) {

					}

				}
			}
		};
	}

	/**
	 * method called during the server execution for suspending server action
	 * temporarily
	 * 
	 */
	private void startServerWaitTimer(long time) {

		try {

			TimeUnit.MILLISECONDS.sleep(time);
		} catch (InterruptedException ex) {

		}
	}

	/**
	 * method called during the server execution to stop the timer
	 * 
	 */
	private void stopServerTimer() {
		if (serverTestEndTimer != null) {
			serverTestEndTimer.cancel(true);
			serverTestEndTimer = null;
			LOGGER.info("server timer stopped");
		}
	}

	/**
	 * This method is called when tool started to wait for expected SIP message
	 * from client. starts timer, wait for specified time duration if expected
	 * SIP message not received in specified time duration, forcefully ends the
	 * server test.
	 */
	private void startServerTimer() {

		if ((testMode.equals(TEST_MODE_ADVANCED))) {
			if (serverTestEndTimer != null) {
				serverTestEndTimer.cancel(true);
				serverTestEndTimer = null;
			}
			LOGGER.info("server timer started");
			serverTestEndTimer = serverTimerScheduler.schedule(new Runnable() {
				public void run() {
					try {

						serverTestEndTimer.cancel(true);
						serverTestEndTimer = null;
						isSocketCloseCalled = true;
						
						// close UDP socket
						udpCommn.closeUdpSocket();
						
						

					} catch (Exception ex) {

					}

				}
			}, MAXIMUM_SERVER_WAIT_TIME, TimeUnit.SECONDS);
		}
	}

	/**
	 * This method called during the server execution to clean up the server
	 * test
	 */
	private void cleanUpServerTest() {
		
		isServerTestSucceed = false;
		serverTestEndTimer = null;
		isSocketCloseCalled = false;
		isServerTestStart = false;

	}

	/**
	 * This method return the server test result
	 * 
	 * @return is a boolean value represents the test result
	 */
	public boolean getServerTestResult() {
		return isServerTestSucceed;
	}

	/**
	 * This method returns the ScheduledExecutorService
	 * 
	 * @returns ScheduledExecutorService
	 */
	public ScheduledExecutorService getServerScheduledExecutorService() {
		return serverTimerScheduler;
	}

	/**
	 * initialize the UDP data gram
	 * 
	 * @throws SocketException
	 * @throws IOException
	 */

	private boolean prepareUDP() throws SocketException, IOException {

		if (!udpCommn.isBounded()) {
			udpCommn.initializeUdpSocket();
			LOGGER.info("UDP Listener initialized ...");

		} else {

			// close UDP data gram socket
			udpCommn.closeUdpSocket();

			// delay made for data gram socket creation
			startServerWaitTimer(SERVER_EXECUTION_INTERVAL);

			udpCommn.initializeUdpSocket();
			LOGGER.info("UDP Listener initialized ...");

		}

		return true;
	}

	/**
	 * Send the SIP message
	 * 
	 * @param action
	 *            is a com.mitester.jaxbparser.server.ACTION object includes
	 *            action related information
	 * @return true after sending the SIP message successfully
	 * @throws NullPointerException
	 * @throws SocketException
	 * @throws ParseException
	 * @throws SipException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 */
	private boolean sendSIPMessage(com.mitester.jaxbparser.server.ACTION action)
			throws NullPointerException, SocketException, ParseException,
			SipException, InvalidArgumentException, IOException {

		boolean isSent = false;

		String serverActionValue = action.getValue();

		if (SERVER_SEND_DELAY != INITIAL_DELAY_SEC) {

			LOGGER.info("setting server delay : " + SERVER_SEND_DELAY / 1000
					+ " secs");
			startServerWaitTimer((long) SERVER_SEND_DELAY);
		}

		TestUtility.printMessage("sending......." + serverActionValue + "\n");
		LOGGER.info("sending......." + serverActionValue + "\n");

		if (action.getSEND().startsWith(RESPONSE_MSG)) {

			if (!(SendResponseHandler.sendResponse(action, udpCommn))) {
				LOGGER.info("sending message failed");
				isSent = false;
			}
			isSent = true;

		} else if (action.getSEND().startsWith(REQUEST_MSG)) {
			if (!(SendRequestHandler.sendRequest(action, udpCommn))) {
				LOGGER.info("sending message failed");
				isSent = false;
			}
			isSent = true;
		}

		return isSent;

	}

	/**
	 * this method used to suspend the server test execution
	 * 
	 * @param wait
	 *            is a com.mitester.jaxbparser.server.WAIT object includes the
	 *            Time related information
	 */

	private void waitServerAction(com.mitester.jaxbparser.server.WAIT wait) {

		BigDecimal time;

		if (wait.getUnit().equals(SEC)) {
			time = new BigDecimal(wait.getValue().toString());
			time = time.multiply(new BigDecimal("1000"));

		} else {
			time = new BigDecimal(wait.getValue().toString());

		}

		startServerWaitTimer(time.longValueExact());

	}

	/**
	 * receive and process the SIP message
	 * 
	 * @param action
	 *            is a com.mitester.jaxbparser.server.ACTION object includes
	 *            action related information
	 * @return true if expected SIP message received
	 * @throws SocketException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws ParseException
	 * @throws SipException
	 */

	private void receiveSIPMessage(com.mitester.jaxbparser.server.ACTION action)
			throws SocketException, IOException, NullPointerException,
			ParseException, SipException {

		/* start server timer */
		startServerTimer();

		TestUtility.printMessage("waiting for......." + action.getValue());
		LOGGER.info("waiting for......." + action.getValue());

		do {

			String packet;

			// receive UDP data gram
			packet = udpCommn.receiveUdpMessage();

			LOGGER.info(LINE_SEPARATOR + INCOMING_SIP_MESSAGE + LINE_SEPARATOR
					+ packet + INCOMING_SIP_MESSAGE);

			// process SipMessage
			SIPMessage sipMsg = ProcessSIPMessage.processSIPMessage(packet,
					INCOMING_MSG);

			if (sipMsg != null) {

				String method = sipMsg.getCSeq().getMethod();
				if (action.getRECV().startsWith(REQUEST_MSG)
						&& action.getValue().startsWith(method)) {

					TestUtility.printMessage(packet);

					/* add the message for validation */
					sipHeaderValidator.setheaderList(sipMsg);

					break;

				} else if (action.getRECV().startsWith(RESPONSE_MSG)) {

					if (sipMsg.getFirstLine().startsWith("SIP/2.0")) {

						String serverActionValue = action.getValue();

						String methodName = serverActionValue.substring(
								serverActionValue
										.lastIndexOf(UNDERLINE_SEPARATOR) + 1,
								serverActionValue.length());
						String FirstLine = sipMsg.getFirstLine();
						String Fline[] = FirstLine.split(EMPTY_SEPARATOR);
						int resCode = Integer.parseInt(Fline[1]);
						int underindex = serverActionValue
								.indexOf(UNDERLINE_SEPARATOR);
						int equalindex = serverActionValue
								.indexOf(EQUAL_SEPARATOR);
						String code = serverActionValue.substring(
								equalindex + 1, underindex);
						int responseCode = Integer.parseInt(code);
						if (methodName.equals(method)
								&& responseCode == resCode) {
							TestUtility.printMessage(packet);

							/* add the message for validation */
							sipHeaderValidator.setheaderList(sipMsg);
							break;

						} else {
							TestUtility.printMessage(packet);
							TestUtility
									.printMessage("Recevied response code does not match with the expected one");
							LOGGER
									.info("Recevied response code does not match with the expected one");

							// remove the added sip message
							ProcessSIPMessage.removeSipMessageFromList();

						}
					} else {

						LOGGER.info("ignored the incoming SIP message");

						// remove the added sip message
						ProcessSIPMessage.removeSipMessageFromList();
					}
				} else {

					LOGGER.info("ignored the incoming SIP message");

					// remove the added sip message
					ProcessSIPMessage.removeSipMessageFromList();

				}
			}

		} while (true);

		// stop the timer if run
		stopServerTimer();

	}

	/**
	 * return the server test start status
	 * 
	 * @return boolean
	 */

	public boolean getServerTestStart() {

		return isServerTestStart;
	}

}
