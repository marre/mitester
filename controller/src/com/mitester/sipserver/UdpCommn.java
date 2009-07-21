/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: UdpCommn.java
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
 * Jain SIP stack 				NIST-CONDITIONS-OF-USE 						        https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 						The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * JNetStreamStandalone lib     GNU Library or LGPL			     					http://sourceforge.net/projects/jnetstream/
 * 
 */
/*
 * miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications.
 *  
 */
package com.mitester.sipserver;

import static com.mitester.executor.ExecutorConstants.EMPTY_SEPARATOR;
import static com.mitester.executor.ExecutorConstants.EQUAL_SEPARATOR;
import static com.mitester.executor.ExecutorConstants.INITIAL_DELAY_SEC;
import static com.mitester.executor.ExecutorConstants.REQUEST_MSG;
import static com.mitester.executor.ExecutorConstants.RESPONSE_MSG;
import static com.mitester.executor.ExecutorConstants.UNDERLINE_SEPARATOR;
import static com.mitester.sipserver.SipServerConstants.B2BUA_MODE;
import static com.mitester.sipserver.SipServerConstants.INCOMING_MSG;
import static com.mitester.sipserver.SipServerConstants.INCOMING_SIP_MESSAGE;
import static com.mitester.sipserver.SipServerConstants.LINE_SEPARATOR;
import static com.mitester.sipserver.SipServerConstants.NORMAL_MODE;
import static com.mitester.sipserver.SipServerConstants.OUTGOING_MSG;
import static com.mitester.sipserver.SipServerConstants.OUTGOING_SIP_MESSAGE;
import static com.mitester.sipserver.SipServerConstants.PROXY_MODE;
import static com.mitester.sipserver.SipServerConstants.SIPVERSION;
import static com.mitester.sipserver.SipServerConstants.SUT_IP_ADDRESS;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;
import static com.mitester.utility.UtilityConstants.CALL_FLOW;
import static com.mitester.utility.UtilityConstants.CALL_FLOW_MODE;
import static com.mitester.utility.UtilityConstants.NORMAL;
import gov.nist.javax.sip.message.SIPMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.ParseException;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.mitester.executor.ControllerException;
import com.mitester.executor.ServerScriptRunner;
import com.mitester.sipserver.headervalidation.InvalidValuesHeaderException;
import com.mitester.sipserver.headervalidation.MissingMandatoryHeaderException;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestResult;
import com.mitester.utility.TestUtility;

/**
 * This class sends and receives the SIP message through UDP channel
 * 
 */
public class UdpCommn {

	private static final Logger LOGGER = MiTesterLog.getLogger(UdpCommn.class
			.getName());

	private DatagramSocket udpSocket = null;

	private final byte[] buf = new byte[SipServerConstants.MAX_BUFFER_LENGTH];

	private DatagramPacket dataPacket = null;

	private int SERVER_PORT = 0;

	private int CLIENT_PORT = 0;

	private InetAddress CLIENT_IP_ADDR = null;

	private String mode = null;

	private ServerScriptRunner serverScriptRunner = null;

	public UdpCommn(String mode) {

		this.mode = mode;
		if (CONFIG_INSTANCE.isKeyExists(SipServerConstants.MITESTER_SIP_PORT)) {
			SERVER_PORT = Integer.parseInt(CONFIG_INSTANCE
					.getValue(SipServerConstants.MITESTER_SIP_PORT));
		} else {
			SERVER_PORT = SipServerConstants.DEFAULT_SERVER_PORT;
		}

		if (CONFIG_INSTANCE.isKeyExists(SipServerConstants.SUT_SIP_PORT)) {
			CLIENT_PORT = Integer.parseInt(CONFIG_INSTANCE
					.getValue(SipServerConstants.SUT_SIP_PORT));
		} else {
			CLIENT_PORT = SipServerConstants.DEFAULT_CLIENT_PORT;
		}

	}

	public UdpCommn(String mode, ServerScriptRunner serverScriptRunner) {

		this.mode = mode;

		this.serverScriptRunner = serverScriptRunner;

		CLIENT_IP_ADDR = TestUtility.getHostAddress(CONFIG_INSTANCE
				.getValue(SUT_IP_ADDRESS));

		if (CONFIG_INSTANCE.isKeyExists(SipServerConstants.MITESTER_SIP_PORT)) {
			SERVER_PORT = Integer.parseInt(CONFIG_INSTANCE
					.getValue(SipServerConstants.MITESTER_SIP_PORT));
		} else {
			SERVER_PORT = SipServerConstants.DEFAULT_SERVER_PORT;
		}
		if (CONFIG_INSTANCE.isKeyExists(SipServerConstants.SUT_SIP_PORT)) {
			CLIENT_PORT = Integer.parseInt(CONFIG_INSTANCE
					.getValue(SipServerConstants.SUT_SIP_PORT));
		} else {
			CLIENT_PORT = SipServerConstants.DEFAULT_CLIENT_PORT;
		}
	}

	/**
	 * it initializes UDP data gram socket
	 * 
	 * @throws SocketException
	 * @throws IOException
	 */
	public void initializeUdpSocket() throws SocketException, IOException {
		udpSocket = new DatagramSocket(SERVER_PORT);
	}

	/**
	 * receiving UDP datagram
	 * 
	 * @return byte array
	 * @throws IOException
	 * @throws SocketException
	 */
	public String receiveUdpMessage() throws IOException, SocketException {

		dataPacket = new DatagramPacket(buf, SipServerConstants.MAX_PACKET_SIZE);
		udpSocket.receive(dataPacket);
		LOGGER.info("receiving SIP message from "
				+ dataPacket.getAddress().getHostAddress() + " and port "
				+ dataPacket.getPort());
		return new String(dataPacket.getData(), 0, dataPacket.getLength());
	}

	/**
	 * close udp socket
	 * 
	 * @throws IOException
	 * @throws SocketException
	 *             return true if SIP data gram socket is closed
	 */
	public boolean closeUdpSocket() throws IOException, SocketException {

		boolean isClosed = false;
		if (udpSocket != null) {
			udpSocket.close();
			udpSocket = null;
//			LOGGER.info("udp datagram socket is closed");
			isClosed = true;
		}

		return isClosed;
	}

	/**
	 * check whether UDP socket bounded to the specified port
	 * 
	 * @return is a boolean value represents true if udp socket bound to the
	 *         specified port
	 */
	public boolean isBounded() {

		if (udpSocket != null && udpSocket.isBound()) {
			LOGGER.info("UDP datagram socket bounded");
			return true;
		} else {
			LOGGER.info("UDP datagram socket not bounded");
			return false;
		}
	}

	/**
	 * sending UDP datagram to the specified port and IP address
	 * 
	 * @param sipMessage
	 * @throws IOException
	 * @throws SocketException
	 * @throws JAXBException
	 */
	public void sendUdpMessage(String... sendPacket) throws IOException,
			SocketException, ParseException, SipException,
			NullPointerException, JAXBException,
			MissingMandatoryHeaderException, InvalidValuesHeaderException {

		if (mode.equals(B2BUA_MODE) || mode.equals(PROXY_MODE)) {

			LOGGER.info("sending SIP message to " + sendPacket[1]
					+ " and port " + sendPacket[2]);
			DatagramPacket packet = new DatagramPacket(
					sendPacket[0].getBytes(), sendPacket[0].length(),
					InetAddress.getByName(sendPacket[1]), new Integer(
							sendPacket[2]).intValue());
			LOGGER.info(packet.toString());
			udpSocket.send(packet);
			LOGGER.info(LINE_SEPARATOR + OUTGOING_SIP_MESSAGE + LINE_SEPARATOR
					+ sendPacket[0] + OUTGOING_SIP_MESSAGE);
		} else if (mode.equals(NORMAL_MODE)) {

			LOGGER.info("sending SIP message to "
					+ CLIENT_IP_ADDR.getHostAddress() + "and port "
					+ CLIENT_PORT);
			ProcessSIPMessage.processSIPMessage(sendPacket[0], OUTGOING_MSG);
			DatagramPacket packet = new DatagramPacket(
					sendPacket[0].getBytes(), sendPacket[0].length(),
					CLIENT_IP_ADDR, CLIENT_PORT);
			// LOGGER.info(packet.toString());
			udpSocket.send(packet);
		}
	}

	/**
	 * Send the SIP message
	 * 
	 * @param action
	 *            is a com.mitester.jaxbparser.server.ACTION object consists of
	 *            action details
	 * @param serverDelay
	 *            it used to set the delay on outgoing SIP message
	 * @return true if SIP message sent successfully
	 * @throws ControllerException
	 */
	public boolean sendSIPMessage(com.mitester.jaxbparser.server.ACTION action,
			float serverDelay) throws ControllerException {

		boolean isSent = false;
		String serverActionValue = action.getValue();

		try {

			// set delay before sending SIP message
			if (serverDelay != INITIAL_DELAY_SEC) {

				LOGGER.info("setting server delay : " + serverDelay / 1000
						+ " secs");
				serverScriptRunner.startServerWaitTimer((long) serverDelay);
			}

			TestUtility.printMessage(NORMAL, "sending......."
					+ serverActionValue, LINE_SEPARATOR);

			LOGGER.info("sending......." + serverActionValue + LINE_SEPARATOR);

			if (action.getSEND().value().startsWith(RESPONSE_MSG)) {

				if (!(SendResponseHandler.sendResponse(action, this))) {
					LOGGER.error("sending " + action.getValue()
							+ " SIP message failed");
					TestResult.setFailReason("sending " + action.getValue()
							+ " SIP message failed");
					return isSent;
				}
				isSent = true;

				TestUtility.printMessage(CALL_FLOW, OUTGOING_MSG, action
						.getValue());

			} else if (action.getSEND().value().startsWith(REQUEST_MSG)) {
				if (!(SendRequestHandler.sendRequest(action, this))) {
					LOGGER.error("sending " + action.getValue()
							+ " SIP message failed");
					TestResult.setFailReason("sending " + action.getValue()
							+ " SIP message failed");
					return isSent;
				}
				isSent = true;
				TestUtility.printMessage(CALL_FLOW, OUTGOING_MSG, action
						.getValue());
			}
		} catch (NullPointerException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while sending " + action.getValue()
							+ " SIP message to SUT");
		} catch (SocketException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while sending " + action.getValue()
							+ " SIP message to SUT");
		} catch (ParseException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while sending " + action.getValue()
							+ " SIP message to SUT");
		} catch (SipException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while sending " + action.getValue()
							+ " SIP message to SUT");
		} catch (InvalidArgumentException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while sending " + action.getValue()
							+ " SIP message to SUT");
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while sending " + action.getValue()
							+ " SIP message to SUT");
		} catch (JAXBException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while parsing the " + action.getValue());
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while sending " + action.getValue()
							+ " SIP message to SUT");
		}
		return isSent;
	}

	/**
	 * receive and process the SIP message
	 * 
	 * @param action
	 *            is a com.mitester.jaxbparser.server.ACTION object consists of
	 *            current action details
	 * @return true if expected SIP message received
	 * @throws MissingMandatoryHeaderException
	 * @throws InvalidValuesHeaderException
	 * @throws ControllerException
	 */

	public void receiveSIPMessage(com.mitester.jaxbparser.server.ACTION action)
			throws MissingMandatoryHeaderException,
			InvalidValuesHeaderException, ControllerException {

		boolean isMatch = false;

		/* start server timer */
		serverScriptRunner.startServerTimer();

		try {

			TestUtility.printMessage(NORMAL, "waiting for......."
					+ action.getValue(), LINE_SEPARATOR);
			LOGGER.info("waiting for......." + action.getValue());

			boolean isSIPMsgNull = false;

			do {

				String packet;

				// call flow display
				if (!isSIPMsgNull)
					TestUtility.printMessage(CALL_FLOW, INCOMING_MSG, action
							.getValue());

				isSIPMsgNull = false;

				// receive UDP data gram
				packet = receiveUdpMessage();

				if (!packet.startsWith(SIPVERSION)
						&& !(packet.substring(packet.indexOf(" ", packet
								.indexOf(" ") + 1) + 1, packet.length())
								.startsWith(SIPVERSION))) {
					LOGGER.info("received NON SIP message hence dropped");
					continue;

				}

				LOGGER.info(LINE_SEPARATOR + INCOMING_SIP_MESSAGE
						+ LINE_SEPARATOR + packet + INCOMING_SIP_MESSAGE);

				TestUtility.printMessage(packet);

				// process SipMessage
				SIPMessage sipMsg = ProcessSIPMessage.processSIPMessage(packet,
						INCOMING_MSG);

				if (sipMsg == null) {
					isSIPMsgNull = true;
					continue;
				}

				String method = sipMsg.getCSeq().getMethod();

				if (action.getRECV().value().startsWith(REQUEST_MSG)
						&& action.getValue().startsWith(method)) {

					// call flow display
					TestUtility.printMessage(CALL_FLOW, INCOMING_MSG,
							"MSG RECEIVED", action.getValue());

					// validate the incoming SIP message
					ProcessSIPMessage.validateSIPMessage(sipMsg, action.getValue());

					isMatch = true;

				} else if (action.getRECV().value().startsWith(RESPONSE_MSG)) {

					if (sipMsg.getFirstLine().startsWith(SIPVERSION)) {

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

							// call flow display
							TestUtility.printMessage(CALL_FLOW, INCOMING_MSG,
									"MSG RECEIVED", action.getValue());

							// validate the incoming SIP message
							ProcessSIPMessage.validateSIPMessage(sipMsg,action.getValue());

							isMatch = true;

						} else {

							// call flow display
							if (TestUtility.getModeOfDisplay()
									.equalsIgnoreCase(CALL_FLOW_MODE)) {
								TestUtility.printMessage(NORMAL, "");
								TestUtility.setIsCallFlowWait(false);
								TestUtility
										.printMessage(CALL_FLOW, INCOMING_MSG,
												"MSG RECEIVED",
												ProcessSIPMessage
														.getMethodName(sipMsg));
							}

							TestUtility
									.printMessage("received response code does not match with the expected one, hence dropped"
											+ LINE_SEPARATOR);
							LOGGER
									.error("received response code does not match with the expected one, hence dropped");

							// remove the added sip message
							ProcessSIPMessage.removeSipMessageFromList();

						}
					} else {

						// call flow display
						if (TestUtility.getModeOfDisplay().equalsIgnoreCase(
								CALL_FLOW_MODE)) {
							TestUtility.printMessage(NORMAL, "");
							TestUtility.setIsCallFlowWait(false);
							TestUtility.printMessage(CALL_FLOW, INCOMING_MSG,
									"MSG RECEIVED", ProcessSIPMessage
											.getMethodName(sipMsg));
						}

						LOGGER.error("Expected " + action.getValue()
								+ " response SIP message but received "
								+ method + " SIP request hence ignored");

						TestUtility.printMessage("Expected "
								+ action.getValue()
								+ " response SIP message but received "
								+ method + " SIP request hence ignored"
								+ LINE_SEPARATOR);

						// remove the added sip message
						ProcessSIPMessage.removeSipMessageFromList();
					}
				} else {

					// call flow display
					if (TestUtility.getModeOfDisplay().equalsIgnoreCase(
							CALL_FLOW_MODE)) {
						TestUtility.printMessage(NORMAL, "");
						TestUtility.setIsCallFlowWait(false);
						TestUtility.printMessage(CALL_FLOW, INCOMING_MSG,
								"MSG RECEIVED", ProcessSIPMessage
										.getMethodName(sipMsg));
					}

					LOGGER.error("Expected " + action.getValue()
							+ " SIP request but received " + method
							+ " SIP request hence ignored");

					TestUtility.printMessage("Expected " + action.getValue()
							+ " SIP request but received " + method
							+ " SIP request hence ignored" + LINE_SEPARATOR);

					// remove the added sip message
					ProcessSIPMessage.removeSipMessageFromList();

				}

			} while (!isMatch);

		} catch (com.mitester.sipserver.headervalidation.MissingMandatoryHeaderException ex) {

			throw ex;

		} catch (com.mitester.sipserver.headervalidation.InvalidValuesHeaderException ex) {

			throw ex;

		} catch (com.mitester.sipserver.SipServerException ex) {

			throw new com.mitester.executor.ControllerException(ex.getMessage());

		} catch (SocketException ex) {
			if (!serverScriptRunner.getIsServerTimerExpired())
				LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while receiving and processing the incoming "
							+ action.getValue() + " SIP message");
		} catch (IOException ex) {
			if (!serverScriptRunner.getIsServerTimerExpired())
				LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while receiving and processing the incoming "
							+ action.getValue() + " SIP message");
		} catch (NullPointerException ex) {
			if (!serverScriptRunner.getIsServerTimerExpired())
				LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while receiving and processing the incoming "
							+ action.getValue() + " SIP message");
		} catch (SipException ex) {
			if (!serverScriptRunner.getIsServerTimerExpired())
				LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while receiving and processing the incoming "
							+ action.getValue() + " SIP message");
		} catch (ParseException ex) {
			if (!serverScriptRunner.getIsServerTimerExpired())
				LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while receiving and processing the incoming "
							+ action.getValue() + " SIP message");
		} catch (JAXBException ex) {
			if (!serverScriptRunner.getIsServerTimerExpired())
				LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while parsing " + action.getValue()
							+ " ACTION message");
		} catch (Exception ex) {
			if (!serverScriptRunner.getIsServerTimerExpired())
				LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while receiving and processing the incoming "
							+ action.getValue() + " SIP message");
		}

		// stop the timer if run
		serverScriptRunner.stopServerTimer();
	}

}
