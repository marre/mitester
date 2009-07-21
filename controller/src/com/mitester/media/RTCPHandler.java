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
package com.mitester.media;

import static com.mitester.media.MediaConstants.FILE_EXTENSE_WITH_PACP;
import static com.mitester.media.MediaConstants.FILE_EXTENSE_WITH_TXT;
import static com.mitester.media.MediaConstants.INCOMING_RTCP_MESSAGE;
import static com.mitester.media.MediaConstants.MAX_MEDIA_PKT_BUFFER_SIZE;
import static com.mitester.media.MediaConstants.MAX_MEDIA_PKT_LENGTH;
import static com.mitester.media.MediaConstants.OUTGOING_RTCP_MESSAGE;
import static com.mitester.media.MediaConstants.SPACE_SEPARATOR;
import static com.mitester.media.MediaConstants.SUT_RTP_PORT;
import static com.mitester.sipserver.SipServerConstants.INCOMING_MSG;
import static com.mitester.sipserver.SipServerConstants.LINE_SEPARATOR;
import static com.mitester.sipserver.SipServerConstants.OUTGOING_MSG;
import static com.mitester.sipserver.SipServerConstants.SUT_IP_ADDRESS;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;
import static com.mitester.utility.UtilityConstants.CALL_FLOW;
import static com.mitester.utility.UtilityConstants.CALL_FLOW_MODE;
import static com.mitester.utility.UtilityConstants.NORMAL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.mitester.executor.ControllerException;
import com.mitester.executor.ServerScriptRunner;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestResult;
import com.mitester.utility.TestUtility;

/**
 * This class supports sending and receiving RTCP packet between client/SUT and
 * miTester
 * 
 */

public class RTCPHandler {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(RTCPHandler.class.getName());

	private volatile DatagramSocket rtcpSocket = null;

	private int SUT_RTCP_PORT_NO;

	private volatile boolean isRTCPSocketOpened = false;

	private final byte[] rtcpBuf = new byte[MAX_MEDIA_PKT_BUFFER_SIZE];

	private final InetAddress CLIENT_IP_ADDR = TestUtility
			.getHostAddress(CONFIG_INSTANCE.getValue(SUT_IP_ADDRESS));

	private ServerScriptRunner serverScriptRunner = null;

	/**
	 * RTCPHandler constructor initializes the serverScriptRunner,
	 * SUT_RTCP_PORT_NO instance variables
	 * 
	 * @param serverScriptRunner
	 *            ServerScriptRunner class object
	 */

	public RTCPHandler(ServerScriptRunner serverScriptRunner) {

		this.serverScriptRunner = serverScriptRunner;

		// set RTCP port number
		SUT_RTCP_PORT_NO = Integer.parseInt(CONFIG_INSTANCE
				.getValue(SUT_RTP_PORT)) + 1;

	}

	/**
	 * this method is used start/open the RTCP port for media communication
	 * 
	 * @param rtcpListenPort
	 *            represents rtcp port at which miTester receives RTCP packets
	 * @return Runnable object
	 */

	public Runnable startListeningRTCP(final int rtcpListenPort) {

		LOGGER.info("called startListeningRTCP");

		isRTCPSocketOpened = false;

		return new Runnable() {

			public void run() {

				try {

					if (rtcpSocket != null) {

						// stop listening RTCP packets
						
						if(stopListeningRTCP())
							LOGGER.info("RTCP datagram socket closed");

						// wait for socket close
						TimeUnit.SECONDS.sleep(1);

						rtcpSocket = new DatagramSocket(rtcpListenPort);
						LOGGER.info("RTCP datagram socket initialized at "
								+ rtcpListenPort);
					} else {

						rtcpSocket = new DatagramSocket(rtcpListenPort);
						LOGGER.info("RTCP datagram socket initialized at "
								+ rtcpListenPort);

					}

					isRTCPSocketOpened = true;

				} catch (SocketException ex) {
					isRTCPSocketOpened = false;
					TestUtility
							.printError(
									"Error while initializing RTCP datagram socket",
									ex);
				} catch (Exception ex) {
					isRTCPSocketOpened = false;
					TestUtility
							.printError(
									"Error while initializing RTCP datagram socket",
									ex);
				} finally {
					if (serverScriptRunner.getRTCPCountDownLatch().getCount() > 0)
						serverScriptRunner.getRTCPCountDownLatch().countDown();
				}
			}
		};
	}

	/**
	 * This method used to close RTCP port return true if RTCP data gram socket
	 * is closed
	 */

	public boolean stopListeningRTCP() {

		// LOGGER.info("called stopListeningRTCP");
		boolean isRTCPClosed = false;
		
		try {

			if (rtcpSocket != null) {
				rtcpSocket.close();
				rtcpSocket = null;
				isRTCPClosed = true;
				// LOGGER.info("RTCP datagram socket closed");
			}

		} catch (Exception ex) {
			LOGGER.error("Error while stopping RTCP listener", ex);
		}
		
		return isRTCPClosed;

	}

	/**
	 * This method is used to receive the RTCP data gram
	 * 
	 * @return DatagramPacket
	 * @throws IOException
	 */

	public DatagramPacket receiveRTCPdatagram() throws IOException {

		LOGGER.info("called receiveRTCPdatagram");

		DatagramPacket dataPacket = new DatagramPacket(rtcpBuf,
				MAX_MEDIA_PKT_LENGTH);
		rtcpSocket.receive(dataPacket);
		LOGGER.info("receiving RTCP message from "
				+ dataPacket.getAddress().getHostAddress() + " and port "
				+ dataPacket.getPort());
		return dataPacket;
	}

	/**
	 * this method is used to check whether RTCP port bound or not
	 * 
	 * @return true if RTCP port bound
	 */

	public boolean isRTCPSocketBounded() {

		if (rtcpSocket != null && rtcpSocket.isBound()) {
			LOGGER.info("RTCP datagram socket bounded");
			return true;
		} else {
			LOGGER.info("RTCP datagram socket not bounded");
			return false;
		}
	}

	/**
	 * This method is used to send the RTCP data gram
	 * 
	 * @param rtcpPacket
	 *            is a byte array consists of RTCP data
	 * @param rtcpPort
	 *            represents rtcp port of client/SUT
	 * @return true if RTCP data is sent successfully
	 * @throws IOException
	 */
	public boolean sendRTCPdatagram(byte[] rtcpPacket, int rtcpPort)
			throws IOException {

		LOGGER.info("called sendRTCPdatagram");

		LOGGER.info("sending RTCP message to "
				+ CLIENT_IP_ADDR.getHostAddress() + " and port " + rtcpPort);

		DatagramPacket packet = new DatagramPacket(rtcpPacket,
				rtcpPacket.length, CLIENT_IP_ADDR, rtcpPort);
		LOGGER.info(packet.toString());
		rtcpSocket.send(packet);

		return true;
	}

	/**
	 * This method is used to check the whether RTCP port opened or not
	 * 
	 * @return true if RTCP port opened
	 */

	public boolean isRTCPSocketOpened() {

		return isRTCPSocketOpened;

	}

	/**
	 * send RTCP datagram
	 * 
	 * @param action
	 *            is com.mitester.jaxbparser.server.ACTION type object which
	 *            consists of current action details
	 * @throws ControllerException
	 * @throws MediaException
	 */

	public boolean sendRTCPPacket(com.mitester.jaxbparser.server.ACTION action)
			throws ControllerException, MediaException {

		boolean isRTCPSent = false;

		List<Object> rtcpPacketList = null;

		com.mitester.jaxbparser.server.MEDIA media = null;

		TestUtility.printMessage(NORMAL, "sending......." + action.getValue(),
				LINE_SEPARATOR);
		LOGGER.info("sending......." + action.getValue());

		try {

			media = (com.mitester.jaxbparser.server.MEDIA) action.getMEDIA();

			String fileName = media.getFile().getSource();

			if (fileName.endsWith(FILE_EXTENSE_WITH_PACP)) {

				// read RTCP packet
				rtcpPacketList = MediaPacketHandler.readPcapFile(media
						.getFile().getSource());

				if (rtcpPacketList != null) {

					for (Object packet : rtcpPacketList) {

						byte bytes[] = (byte[]) packet;

						// decoding RTCP packet
						String rtcpMessage = MediaPacketHandler.decodeMessage(
								bytes, bytes.length);

						LOGGER.info(LINE_SEPARATOR + OUTGOING_RTCP_MESSAGE
								+ LINE_SEPARATOR + rtcpMessage
								+ OUTGOING_RTCP_MESSAGE);
						TestUtility.printMessage(rtcpMessage);

						// send RTCP data gram
						sendRTCPdatagram(bytes, SUT_RTCP_PORT_NO);

						TestUtility.printMessage(CALL_FLOW, OUTGOING_MSG,
								action.getValue());

						isRTCPSent = true;
					}
				} else {

					LOGGER.error("Error while reading RTCP packets from '"
							+ fileName + "' file");
					TestResult
							.setFailReason("Error while reading RTCP packets from '"
									+ fileName + "' file");
				}

			} else if (fileName.endsWith(FILE_EXTENSE_WITH_TXT)) {

				// read RTCP packet
				String rtcpMessage = MediaPacketHandler.readTxtFile(media
						.getFile().getSource());

				if (rtcpMessage != null) {

					LOGGER.info(LINE_SEPARATOR + OUTGOING_RTCP_MESSAGE
							+ LINE_SEPARATOR + rtcpMessage
							+ OUTGOING_RTCP_MESSAGE);
					TestUtility.printMessage(rtcpMessage);

					// encode RTCP message
					byte[] rtcpPacket = MediaPacketHandler
							.encodeMessage(rtcpMessage);

					// send RTCP data gram
					sendRTCPdatagram(rtcpPacket, SUT_RTCP_PORT_NO);

					TestUtility.printMessage(CALL_FLOW, OUTGOING_MSG, action
							.getValue());

					isRTCPSent = true;
				} else {

					LOGGER.error("Error while reading RTCP packets from '"
							+ fileName + "' file");
					TestResult
							.setFailReason("Error while reading RTCP packets from '"
									+ fileName + "' file");
				}

			}
		} catch (com.mitester.media.MediaException ex) {
			throw ex;
		} catch (IOException ex) {

			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while sending RTCP packet to SUT");

		} catch (Exception ex) {

			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while sending RTCP packet to SUT");
		}

		return isRTCPSent;
	}

	/**
	 * receive and process the RTCP datagram
	 * 
	 * @param action
	 *            is com.mitester.jaxbparser.server.ACTION type object which
	 *            consists of current action details
	 * @throws ControllerException
	 */

	public void receiveRTCPPacket(com.mitester.jaxbparser.server.ACTION action)
			throws ControllerException {

		String rtcpMessage = null;

		String packetType = null;

		String rtcpPktType = action.getValue();

		TestUtility.printMessage(NORMAL, "waiting for......."
				+ action.getValue(), LINE_SEPARATOR);

		LOGGER.info("waiting for......." + action.getValue());

		try {

			// start server wait timer
			serverScriptRunner.startServerTimer();

			do {

				// call flow display
				TestUtility.printMessage(CALL_FLOW, INCOMING_MSG, action
						.getValue());

				// receiving RTCP packet
				DatagramPacket rtcpPacket = receiveRTCPdatagram();

				LOGGER.info("decoding RTCP packet...");

				// decoding RTCP packet
				rtcpMessage = MediaPacketHandler.decodeMessage(rtcpPacket
						.getData(), rtcpPacket.getLength());

				LOGGER.info("decoding RTCP packet completed");

				int separatorIndex = rtcpMessage.indexOf(SPACE_SEPARATOR);

				packetType = rtcpMessage.substring(separatorIndex + 1,
						rtcpMessage
								.indexOf(SPACE_SEPARATOR, separatorIndex + 1));

				TestUtility.printMessage(rtcpMessage);

				LOGGER.info(LINE_SEPARATOR + INCOMING_RTCP_MESSAGE
						+ LINE_SEPARATOR + rtcpMessage + INCOMING_RTCP_MESSAGE);

				String rtcpName = MediaPacketHandler
						.getRTCPPacketName(packetType);

				if (!rtcpName.equalsIgnoreCase(rtcpPktType)) {

					// call flow display
					if (TestUtility.getModeOfDisplay().equalsIgnoreCase(
							CALL_FLOW_MODE)) {
						TestUtility.printMessage(NORMAL, "");
						TestUtility.setIsCallFlowWait(false);
						TestUtility.printMessage(CALL_FLOW, INCOMING_MSG,
								"MSG RECEIVED", rtcpName);
					}

				} else {

					// call flow display
					TestUtility.printMessage(CALL_FLOW, INCOMING_MSG,
							"MSG RECEIVED", rtcpName);
				}

			} while (!MediaPacketHandler.getRTCPtype(rtcpPktType)
					.equalsIgnoreCase(packetType));

			// stop server wait timer
			serverScriptRunner.stopServerTimer();

		} catch (IOException ex) {

			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while receiving and processing the RTCP message");
		} catch (Exception ex) {

			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while receiving and processing the RTCP message");
		}

	}

}
