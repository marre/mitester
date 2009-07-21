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

import static com.mitester.executor.ExecutorConstants.ADVANCED_MODE;
import static com.mitester.executor.ExecutorConstants.DEFAULT_TEST_INTERVAL;
import static com.mitester.executor.ExecutorConstants.INITIAL_DELAY_SEC;
import static com.mitester.executor.ExecutorConstants.MITESTER_DELAY;
import static com.mitester.executor.ExecutorConstants.MITESTER_MODE;
import static com.mitester.executor.ExecutorConstants.MITESTER_WAIT_TIME;
import static com.mitester.executor.ExecutorConstants.MITESTER_WAIT_TIME_SEC;
import static com.mitester.executor.ExecutorConstants.SEC;
import static com.mitester.executor.ExecutorConstants.TEST_INTERVAL;
import static com.mitester.media.MediaConstants.MITESTER_RTP_PORT;
import static com.mitester.media.MediaConstants.RTCP_PACKET;
import static com.mitester.media.MediaConstants.RTP_PACKET;
import static com.mitester.media.MediaConstants.SUT_RTP_PORT;
import static com.mitester.sipserver.SipServerConstants.NORMAL_MODE;
import static com.mitester.sipserver.SipServerConstants.INCOMING_MSG;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;
import static com.mitester.utility.UtilityConstants.CALL_FLOW;
import static com.mitester.utility.UtilityConstants.NORMAL;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.mitester.media.MediaException;
import com.mitester.media.RTCPHandler;
import com.mitester.media.RTPHandler;
import com.mitester.sipserver.ProcessSIPMessage;
import com.mitester.sipserver.UdpCommn;
import com.mitester.sipserver.headervalidation.InvalidValuesHeaderException;
import com.mitester.sipserver.headervalidation.MissingMandatoryHeaderException;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestResult;
import com.mitester.utility.TestUtility;
import com.mitester.utility.ThreadControl;

/**
 * This class controls the server module of the miTester. Sending and receiving
 * of SIP messages are controlled by the actions specified in the server test
 * scripts. It implements Timer threads which are used to suspend the server
 * actions temporarily for specified time duration and sets the maximum time to
 * the controller will wait for receiving SIP messages from SUT.
 * 
 */
public class ServerScriptRunner {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(ServerScriptRunner.class.getName());

	private final int MITESTER_MAX_WAIT_TIME;

	private final int EXECUTION_INTERVAL;

	private int MITESTER_RTP_PORT_NO = 0;

	private int MITESTER_RTCP_PORT_NO = 0;

	private final float SERVER_DELAY;

	private volatile boolean isSIPPortCloseCalled = false;

	private volatile boolean isMediaPortCloseCalled = false;

	private volatile boolean isServerTestSucceed = false;

	private volatile boolean isServerTestStart = false;

	private volatile boolean isServerTimerExpired = false;

	private volatile String serverActName = null;

	private boolean isMediaPortSet = false;

	private ScheduledExecutorService serverTimerScheduler = Executors
			.newScheduledThreadPool(1);

	private ExecutorService rtpExecutors = Executors.newCachedThreadPool();

	private ExecutorService rtcpExecutors = Executors.newCachedThreadPool();

	private volatile ScheduledFuture<?> serverTestEndTimer = null;

	private TestExecutor testExecutor = null;

	private String testMode = CONFIG_INSTANCE.getValue(MITESTER_MODE);

	private UdpCommn udpCommn = new UdpCommn(NORMAL_MODE, this);

	private RTPHandler rtpHandler = null;

	private RTCPHandler rtcpHandler = null;

	private CountDownLatch rtpCountDownLatch = null;

	private CountDownLatch rtcpCountDownLatch = null;

	private ThreadControl threadControl = null;

	/**
	 * It initializes
	 * sipServer,testExecutor,MAXIMUM_MITESTER_WAIT_TIME,threadControl and
	 * SERVER_SEND_DELAY instance variables
	 * 
	 * @param CheckPresence
	 *            checkPresence object
	 * @param sipServer
	 *            SipServer object
	 * @param testExecutor
	 *            TestExecutor object
	 * @param threadControl
	 *            ThreadControl object
	 */
	public ServerScriptRunner(TestExecutor testExecutor,
			ThreadControl threadControl) {

		this.testExecutor = testExecutor;
		this.threadControl = threadControl;

		// set the RTP, RTCP port for media transport
		if (CONFIG_INSTANCE.isKeyExists(SUT_RTP_PORT)) {

			MITESTER_RTP_PORT_NO = Integer.parseInt(CONFIG_INSTANCE
					.getValue(MITESTER_RTP_PORT));
			MITESTER_RTCP_PORT_NO = MITESTER_RTP_PORT_NO + 1;

			rtpHandler = new RTPHandler(this);

			rtcpHandler = new RTCPHandler(this);

			isMediaPortSet = true;
		}

		// sets the maximum server wait time
		if (CONFIG_INSTANCE.isKeyExists(MITESTER_WAIT_TIME)) {
			MITESTER_MAX_WAIT_TIME = Integer.parseInt(CONFIG_INSTANCE
					.getValue(MITESTER_WAIT_TIME));
		} else {
			MITESTER_MAX_WAIT_TIME = MITESTER_WAIT_TIME_SEC;
		}

		// sets the server delay on sending SIP message
		if (CONFIG_INSTANCE.isKeyExists(MITESTER_DELAY)) {
			SERVER_DELAY = Float.parseFloat(CONFIG_INSTANCE
					.getValue(MITESTER_DELAY)) * 1000;

		} else {
			SERVER_DELAY = INITIAL_DELAY_SEC;
		}

		// set the client execution interval
		if (CONFIG_INSTANCE.isKeyExists(TEST_INTERVAL)) {
			EXECUTION_INTERVAL = Integer.parseInt(CONFIG_INSTANCE
					.getValue(TEST_INTERVAL)) * 1000;
		} else {
			EXECUTION_INTERVAL = DEFAULT_TEST_INTERVAL;
		}
	}

	/**
	 * Execute the Server Test
	 * 
	 * @param serverTest
	 *            consists of set of server actions
	 * 
	 * @return Runnable interface
	 */

	public Runnable frameServerRunnable(
			final com.mitester.jaxbparser.server.TEST serverTest) {

		// clean-up server Tests
		cleanUpServerTest();

		// clean-up the SIPMessageList
		ProcessSIPMessage.cleanUpSipMessageList();

		return new Runnable() {
			public void run() {

				try {

					if (testMode.equalsIgnoreCase("ADVANCED"))
						threadControl.take();

					List<Object> serverActions = serverTest.getACTIONOrWAIT();

					if (isMediaPortSet) {

						// run server script with enabled SIP, RTP, RTCP ports
						runScript_With_MediaSupport(serverActions);

					} else {

						// run server script with enabled SIP port only
						runScript_Without_MediaSupport(serverActions);
					}

				} catch (com.mitester.executor.ControllerException ex) {
					if (!isServerTimerExpired)
						LOGGER.error(ex.getMessage());

					// set the fail reason
					if (testExecutor.getIsValidationFailed())
						TestResult.setFailReason(testExecutor
								.getBufferedFailString()
								+ ", " + ex.getMessage());
					else
						TestResult.setFailReason(ex.getMessage());

				} catch (com.mitester.media.MediaException ex) {
					if (!isServerTimerExpired)
						LOGGER.error(ex.getMessage());
					// set the fail reason
					if (testExecutor.getIsValidationFailed())
						TestResult.setFailReason(testExecutor
								.getBufferedFailString()
								+ ", " + ex.getMessage());
					else
						TestResult.setFailReason(ex.getMessage());
				} catch (com.mitester.sipserver.headervalidation.MissingMandatoryHeaderException ex) {
					LOGGER.error(ex.getMessage());
					TestResult.setFailReason(ex.getMessage());
				} catch (com.mitester.sipserver.headervalidation.InvalidValuesHeaderException ex) {
					LOGGER.error(ex.getMessage());
					TestResult.setFailReason(ex.getMessage());
				} catch (Exception ex) {

					if (!isServerTimerExpired)
						LOGGER.error(ex.getMessage());

					// set the fail reason
					if (testExecutor.getIsValidationFailed())
						TestResult.setFailReason(testExecutor
								.getBufferedFailString()
								+ ", Error while executing server test script");
					else
						TestResult
								.setFailReason("Error while executing server test script");
				} finally {

					try {

						// stop timer
						stopServerTimer();

						// close the sip data gram socket
						if (!isSIPPortCloseCalled) {

							// close UDP socket
							if (udpCommn.closeUdpSocket()) {
								LOGGER.info("udp datagram socket is closed");
							}
						}

						// close the media data gram sockets
						if ((isMediaPortSet) && (!isMediaPortCloseCalled)) {

							// close RTP socket
							if (rtpHandler.stopListeningRTP()) {
								LOGGER.info("RTP datagram socket is closed");
							}
							// close RTCP socket
							if (rtcpHandler.stopListeningRTCP()) {
								LOGGER.info("RTCP datagram socket is closed");
							}
						}

						// setting delay
						startServerWaitTimer(EXECUTION_INTERVAL);

						if (!udpCommn.isBounded())
							LOGGER.info("SIP UDP socket is closed");

						if (isServerTestSucceed) {
							if (testExecutor.getIsValidationFailed()) {
								isServerTestSucceed = false;
								LOGGER.error(testExecutor
										.getBufferedFailString());
								TestResult.setFailReason(testExecutor
										.getBufferedFailString());
							}
							TestUtility
									.printMessage("server actions completed...");
							LOGGER.info("server actions completed ...");
						} else
							LOGGER.info("incomplete server scenario...");

						// count down latch
						if (testExecutor.getServerTestCountDownLatch()
								.getCount() == 1) {
							testExecutor.getServerTestCountDownLatch()
									.countDown();
						}

					} catch (Exception ex) {

					}
				}
			}
		};
	}

	/**
	 * This method is called to suspend server action temporarily
	 * 
	 * @param amount
	 *            of time server will wait for receiving SIP message from SUT in
	 *            ADVANCED mode
	 */
	public void startServerWaitTimer(long time) {

		try {

			TimeUnit.MILLISECONDS.sleep(time);
		} catch (InterruptedException ex) {

		}
	}

	/**
	 * It stops serverTestEndTimer
	 * 
	 */
	public void stopServerTimer() {

		if (serverTestEndTimer != null) {
			serverTestEndTimer.cancel(true);
			serverTestEndTimer = null;
			LOGGER.info("server timer is stopped");
		}
	}

	/**
	 * This method is called when tool started to wait for expected SIP message
	 * from client. It starts timer, wait for specified time duration if
	 * expected SIP message not received in specified time, forcefully ends the
	 * server test.
	 */
	public void startServerTimer() {

		if ((testMode.equals(ADVANCED_MODE))) {

			if (serverTestEndTimer != null) {
				serverTestEndTimer.cancel(true);
				serverTestEndTimer = null;
			}
			LOGGER.info("server timer is started");
			serverTestEndTimer = serverTimerScheduler.schedule(new Runnable() {
				public void run() {

					isServerTimerExpired = true;

					String actionName = getServerActionName();

					if (testExecutor.getIsValidationFailed()
							&& (!actionName.startsWith(RTP_PACKET) && !actionName
									.startsWith(RTCP_PACKET))) {
						String errStr = testExecutor.getBufferedFailString()
								+ ", Expected '" + getServerActionName()
								+ "' SIP message is not received from SUT";
						TestResult.setFailReason(errStr);
						LOGGER.error(errStr);

					} else if (testExecutor.getIsValidationFailed()
							&& (actionName.startsWith(RTP_PACKET) || actionName
									.startsWith(RTCP_PACKET))) {
						String errStr = testExecutor.getBufferedFailString()
								+ ", Expected '" + getServerActionName()
								+ "' message is not received from SUT";
						TestResult.setFailReason(errStr);
						LOGGER.error(errStr);

					} else if (!testExecutor.getIsValidationFailed()
							&& (!actionName.startsWith(RTP_PACKET) && !actionName
									.startsWith(RTCP_PACKET))) {
						String errStr = "Expected '" + getServerActionName()
								+ "' SIP message is not received from SUT";
						TestResult.setFailReason(errStr);
						LOGGER.error(errStr);
					} else {
						String errStr = "Expected '" + getServerActionName()
								+ "' message is not received from SUT";
						TestResult.setFailReason(errStr);
						LOGGER.error(errStr);
					}

					LOGGER
							.info("stopping the server as the server timer is expired");

					// stop the server test execution
					stopServerTestExecution();

				}
			}, MITESTER_MAX_WAIT_TIME, TimeUnit.SECONDS);
		}
	}

	/**
	 * It used to clean the ServerScriptRunner instance variables
	 */
	private void cleanUpServerTest() {

		isServerTestSucceed = false;
		serverTestEndTimer = null;
		isSIPPortCloseCalled = false;
		isMediaPortCloseCalled = false;
		isServerTestStart = false;
		isServerTimerExpired = false;
		serverActName = null;
	}

	/**
	 * This method return the server test result status
	 * 
	 * @return server test result status
	 */
	public boolean getServerTestResult() {
		return isServerTestSucceed;
	}

	/**
	 * It returns the ScheduledExecutorService
	 * 
	 * @returns ScheduledExecutorService
	 */
	public ScheduledExecutorService getServerScheduledExecutorService() {
		return serverTimerScheduler;
	}

	/**
	 * It initializes the UDP data gram socket for SIP transaction
	 * 
	 * @throws ControllerException
	 */

	private boolean prepareUDP() throws ControllerException {

		try {
			if (!udpCommn.isBounded()) {
				udpCommn.initializeUdpSocket();
				LOGGER.info("UDP Listener is initialized");

			} else {

				// close UDP data gram socket if already exist
				if (udpCommn.closeUdpSocket()) {
					LOGGER.info("udp datagram socket is closed");
				}

				// making delay for creating data gram socket
				startServerWaitTimer(1800);

				// initialize the udp socket
				udpCommn.initializeUdpSocket();

				LOGGER.info("UDP Listener is initialized");

			}
		} catch (SocketException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while initializing UDP datagram for SIP transaction");

		} catch (IOException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while initializing UDP datagram for SIP transaction");

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while initializing UDP datagram for SIP transaction");
		}

		return true;
	}

	/**
	 * It used to suspend the server test execution
	 * 
	 * @param wait
	 *            is a com.mitester.jaxbparser.server.WAIT object consists of
	 *            the time information
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
	 * It returns the server start status
	 * 
	 * @return true if server started successfully
	 */

	public boolean getServerTestStart() {

		return isServerTestStart;
	}

	/**
	 * execute the server test script with enabled media ports
	 * 
	 * @param serverActions
	 *            consists of list of server actions
	 * @return true if all server actions executed
	 * @throws MissingMandatoryHeaderException
	 * @throws InvalidValuesHeaderException
	 * @throws ControllerException
	 * @throws MediaException
	 */

	public boolean runScript_With_MediaSupport(List<Object> serverActions)
			throws MissingMandatoryHeaderException,
			InvalidValuesHeaderException, ControllerException, MediaException {

		int actionCount = 0;

		com.mitester.jaxbparser.server.ACTION action = null;

		com.mitester.jaxbparser.server.WAIT wait = null;

		boolean isWAIT = false;

		try {

			int noOfServerActions = serverActions.size();

			rtpCountDownLatch = new CountDownLatch(1);

			rtcpCountDownLatch = new CountDownLatch(1);

			// initialize the RTP port
			rtpExecutors.execute(rtpHandler
					.startListeningRTP(MITESTER_RTP_PORT_NO));

			// initialize the RTCP port
			rtcpExecutors.execute(rtcpHandler
					.startListeningRTCP(MITESTER_RTCP_PORT_NO));

			// wait for opening RTP port
			rtpCountDownLatch.await();

			// wait for opening RTCP port
			rtcpCountDownLatch.await();

			// initialize the SIP port and check whether SIP, RTP and RTCP ports
			// are opened
			if (prepareUDP() && rtpHandler.isRTPSocketOpened()
					&& rtcpHandler.isRTCPSocketOpened()) {

				isServerTestStart = true;

				for (Object object : serverActions) {

					if (testMode.equalsIgnoreCase("USER")) {

						if (threadControl.getStopExecution())
							threadControl.stopExecution();

						if (threadControl.isThreadStop()) {
							TestUtility
									.printMessage(NORMAL,
											"Press 'p' + ENTER key to resume the execution");
							threadControl.take();
						} else
							threadControl.take();
					}

					if (object instanceof com.mitester.jaxbparser.server.ACTION) {

						action = (com.mitester.jaxbparser.server.ACTION) object;
						isWAIT = false;

					} else {

						wait = (com.mitester.jaxbparser.server.WAIT) object;
						isWAIT = true;
					}

					if (isWAIT) {

						TestUtility
								.printMessage("server action in sleeping...");
						LOGGER.info("server action in sleeping...");

						// wait for specified time
						waitServerAction(wait);

						TestUtility.printMessage("resumed server action...");
						LOGGER.info("resumed server action...");

					} else if (action.getRECV() != null) {

						// set ACTION name
						setServerActionName(action.getValue());

						if (action.getValue().startsWith(RTP_PACKET)) {

							// receive RTP message
							rtpHandler.receiveRTPPacket(action);

						} else if (action.getValue().startsWith(RTCP_PACKET)) {

							// receive RTCP message
							rtcpHandler.receiveRTCPPacket(action);

						} else {

							// receive and process the SIP message
							recAndProcSIPMessage(action);
						}

					} else if (action.getSEND() != null) {

						// set ACTION name
						setServerActionName(action.getValue());

						if (action.getValue().startsWith(RTP_PACKET)) {

							// send RTP message
							if (!rtpHandler.sendRTPPacket(action)) {
								TestUtility
										.printMessage("sending RTP message is failed, hence server test forcefully ended");
								LOGGER
										.error("sending RTP message is failed, hence server test forcefully ended");
								break;
							}

						} else if (action.getValue().startsWith(RTCP_PACKET)) {

							// send RTCP message
							if (!rtcpHandler.sendRTCPPacket(action)) {

								TestUtility
										.printMessage("sending RTCP message is failed, hence server test forcefully ended");
								LOGGER
										.error("sending RTCP message is failed, hence server test forcefully ended");
								break;
							}

						} else {

							// send SIP message
							if (!udpCommn.sendSIPMessage(action, SERVER_DELAY)) {
								TestUtility
										.printMessage("sending SIP message is failed, hence server test forcefully ended");
								LOGGER
										.error("sending SIP message is failed, hence server test forcefully ended");
								break;
							}
						}

					} else {
						break;
					}

					actionCount++;

				}

			} else {

				TestUtility.printMessage("Error while starting sip server");
				LOGGER.error("Error while starting sip server");
			}

			if ((actionCount >= noOfServerActions) && (noOfServerActions > 0)) {
				isServerTestSucceed = true;
			}

		} catch (com.mitester.executor.ControllerException ex) {
			throw ex;

		} catch (com.mitester.media.MediaException ex) {
			throw ex;

		} catch (com.mitester.sipserver.headervalidation.InvalidValuesHeaderException ex) {

			throw ex;

		} catch (com.mitester.sipserver.headervalidation.MissingMandatoryHeaderException ex) {
			throw ex;

		} catch (InterruptedException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error at executing server test script");

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error at executing server test script");
		}
		return isServerTestSucceed;

	}

	/**
	 * execute the server test script without media support
	 * 
	 * @param serverActions
	 *            consists of server actions
	 * 
	 * @return true if all server actions completed
	 * @throws MissingMandatoryHeaderException
	 * @throws InvalidValuesHeaderException
	 * @throws ControllerException
	 */

	public boolean runScript_Without_MediaSupport(List<Object> serverActions)
			throws MissingMandatoryHeaderException,
			InvalidValuesHeaderException, ControllerException {

		int actionCount = 0;

		com.mitester.jaxbparser.server.ACTION action = null;

		com.mitester.jaxbparser.server.WAIT wait = null;

		boolean isWAIT = false;

		try {

			int noOfServerActions = serverActions.size();

			// initialize the UDP socket
			if (prepareUDP()) {

				isServerTestStart = true;

				for (Object object : serverActions) {

					if (testMode.equalsIgnoreCase("USER")) {

						if (threadControl.getStopExecution())
							threadControl.stopExecution();

						if (threadControl.isThreadStop()) {
							TestUtility
									.printMessage(NORMAL,
											"Press 'p' + ENTER key to resume the execution");
							threadControl.take();
						} else
							threadControl.take();
					}

					if (object instanceof com.mitester.jaxbparser.server.ACTION) {

						action = (com.mitester.jaxbparser.server.ACTION) object;
						isWAIT = false;

					} else {

						wait = (com.mitester.jaxbparser.server.WAIT) object;
						isWAIT = true;
					}

					if (isWAIT) {

						TestUtility
								.printMessage("server action in sleeping...");
						LOGGER.info("server action in sleeping...");

						// wait for specified time
						waitServerAction(wait);

						TestUtility.printMessage("resumed server action...");
						LOGGER.info("resumed server action...");

					} else if (action.getRECV() != null) {

						// set ACTION name
						setServerActionName(action.getValue());

						// receive and process the SIP message
						recAndProcSIPMessage(action);

					} else if (action.getSEND() != null) {

						// set ACTION name
						setServerActionName(action.getValue());

						// send SIP message
						if (!udpCommn.sendSIPMessage(action, SERVER_DELAY)) {
							TestUtility
									.printMessage("sending SIP message is failed, hence server test forcefully ended");
							LOGGER
									.error("sending SIP message is failed, hence server test forcefully ended");
							break;
						}

					} else {
						break;
					}

					actionCount++;

				}

			} else {

				TestUtility.printMessage("Error while starting sip server");
				LOGGER.error("Error while starting sip server");
			}

			if ((actionCount >= noOfServerActions) && (noOfServerActions > 0)) {
				isServerTestSucceed = true;
			}

		} catch (com.mitester.executor.ControllerException ex) {
			throw ex;

		} catch (com.mitester.sipserver.headervalidation.InvalidValuesHeaderException ex) {
			throw ex;

		} catch (com.mitester.sipserver.headervalidation.MissingMandatoryHeaderException ex) {
			throw ex;

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error at executing server test script");

		}

		return isServerTestSucceed;

	}

	/**
	 * 
	 * @return RTP count down latch object
	 */

	public CountDownLatch getRTPCountDownLatch() {
		return rtpCountDownLatch;
	}

	/**
	 * 
	 * @return RTCP count down latch object
	 */
	public CountDownLatch getRTCPCountDownLatch() {
		return rtcpCountDownLatch;
	}

	/**
	 * shutdown RTP executors
	 */
	public void shutDownRtpExecutors() {

		LOGGER.info("called shutDownRtpExecutors");

		try {
			rtpExecutors.shutdownNow();
			rtpExecutors.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {

		}

	}

	/**
	 * shut down RTCP executors
	 */
	public void shutDownRtcpExecutors() {

		LOGGER.info("called shutDownRtcpExecutors");

		try {
			rtcpExecutors.shutdownNow();
			rtcpExecutors.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {

		}

	}

	/**
	 * stops the server test execution
	 */

	public void stopServerTestExecution() {

		if (!System.getProperty("os.name").startsWith("SunOS")
				&& !System.getProperty("os.name").startsWith("Solaris"))
			LOGGER.info("called stopServerTestExecution");

		try {

			if (serverTestEndTimer != null)
				serverTestEndTimer.cancel(true);
			serverTestEndTimer = null;

			isSIPPortCloseCalled = true;

			// close UDP socket
			if (udpCommn.closeUdpSocket()
					&& (!System.getProperty("os.name").startsWith("SunOS") && !System
							.getProperty("os.name").startsWith("Solaris"))) {
				LOGGER.info("udp datagram socket is closed");
			}

			if (isMediaPortSet) {

				isMediaPortCloseCalled = true;

				// close RTP socket
				if (rtpHandler.stopListeningRTP()
						&& (!System.getProperty("os.name").startsWith("SunOS") && !System
								.getProperty("os.name").startsWith("Solaris"))) {
					LOGGER.info("RTP datagram socket is closed");
				}

				// close RTCP socket
				if (rtcpHandler.stopListeningRTCP()
						&& (!System.getProperty("os.name").startsWith("SunOS") && !System
								.getProperty("os.name").startsWith("Solaris"))) {
					LOGGER.info("RTCP datagram socket closed");
				}

			}

		} catch (Exception ex) {

			LOGGER.error("error while stopping server test execution", ex);
		}
	}

	/**
	 * return the server ACTION name
	 * 
	 * @return the current ACTION name
	 */

	private String getServerActionName() {
		return serverActName;
	}

	/**
	 * set the server ACTION name
	 * 
	 * @param actName
	 *            name of the current ACTION
	 */

	private void setServerActionName(String actName) {
		this.serverActName = actName;
	}

	/**
	 * receive and process the incoming SIP message
	 * 
	 * @param action
	 *            consists of server action details
	 * @throws ControllerException
	 * @throws InvalidValuesHeaderException
	 * @throws MissingMandatoryHeaderException
	 */

	private void recAndProcSIPMessage(
			com.mitester.jaxbparser.server.ACTION action)
			throws ControllerException, InvalidValuesHeaderException,
			MissingMandatoryHeaderException {

		try {

			udpCommn.receiveSIPMessage(action);
		} catch (MissingMandatoryHeaderException e) {
			if (!CONFIG_INSTANCE.isKeyExists("COMPLETE_CALL_FLOW")) {
				throw e;
			} else if (!CONFIG_INSTANCE.getValue("COMPLETE_CALL_FLOW")
					.equalsIgnoreCase("YES")) {
				throw e;
			} else {

				TestUtility.printMessage(CALL_FLOW, INCOMING_MSG,
						"MSG RECEIVED", action.getValue());

				// set validation failed status
				testExecutor.setIsValidationFailed(true);

				if ((testExecutor.getBufferedFailString().indexOf(e
						.getMessage())) < 0)
					testExecutor.bufferFailString(e.getMessage());
			}
		} catch (InvalidValuesHeaderException e) {
			if (!CONFIG_INSTANCE.isKeyExists("COMPLETE_CALL_FLOW")) {
				throw e;
			} else if (!CONFIG_INSTANCE.getValue("COMPLETE_CALL_FLOW")
					.equalsIgnoreCase("YES")) {
				throw e;
			} else {

				TestUtility.printMessage(CALL_FLOW, INCOMING_MSG,
						"MSG RECEIVED", action.getValue());

				// set validation failed status
				testExecutor.setIsValidationFailed(true);

				if ((testExecutor.getBufferedFailString().indexOf(e
						.getMessage())) < 0)
					testExecutor.bufferFailString(e.getMessage());
			}
		} catch (ControllerException e) {
			throw e;
		}
	}

	/**
	 * return isServerTimerExpired status
	 */

	public boolean getIsServerTimerExpired() {
		return isServerTimerExpired;
	}
}
