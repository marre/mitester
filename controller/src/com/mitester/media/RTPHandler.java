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
import static com.mitester.media.MediaConstants.INCOMING_RTP_MESSAGE;
import static com.mitester.media.MediaConstants.MAX_MEDIA_PKT_BUFFER_SIZE;
import static com.mitester.media.MediaConstants.MAX_MEDIA_PKT_LENGTH;
import static com.mitester.media.MediaConstants.OUTGOING_RTP_MESSAGE;
import static com.mitester.media.MediaConstants.SPACE_SEPARATOR;
import static com.mitester.media.MediaConstants.SUT_RTP_PORT;
import static com.mitester.sipserver.SipServerConstants.INCOMING_MSG;
import static com.mitester.sipserver.SipServerConstants.LINE_SEPARATOR;
import static com.mitester.sipserver.SipServerConstants.OUTGOING_MSG;
import static com.mitester.sipserver.SipServerConstants.SUT_IP_ADDRESS;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;
import static com.mitester.utility.UtilityConstants.CALL_FLOW;
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
 * This class supports sending and receiving RTP packets between client/SUT and
 * miTester
 * 
 */

public class RTPHandler {

	private static final Logger LOGGER = MiTesterLog.getLogger(RTPHandler.class
			.getName());

	private final byte[] rtpBuf = new byte[MAX_MEDIA_PKT_BUFFER_SIZE];

	private final int SUT_RTP_PORT_NO;

	private volatile DatagramSocket rtpSocket = null;

	private volatile boolean isRTPSocketOpened = false;

	private final InetAddress CLIENT_IP_ADDR = TestUtility
			.getHostAddress(CONFIG_INSTANCE.getValue(SUT_IP_ADDRESS));

	private ServerScriptRunner serverScriptRunner = null;

	/**
	 * RTPHandler constructor initializes the serverScriptRunner,
	 * SUT_RTP_PORT_NO instance variables
	 * 
	 * @param serverScriptRunner
	 *            is an object of ServerScriptRunner
	 */

	public RTPHandler(ServerScriptRunner serverScriptRunner) {

		this.serverScriptRunner = serverScriptRunner;

		// set RTP port number
		SUT_RTP_PORT_NO = Integer.parseInt(CONFIG_INSTANCE
				.getValue(SUT_RTP_PORT));

	}

	/**
	 * this method is used start/open the RTP port for media communication
	 * 
	 * @param rtpListenPort
	 *            represents rtp port at which miTester receives RTP packets
	 * @return Runnable interface
	 */

	public Runnable startListeningRTP(final int rtpListenPort) {

		LOGGER.info("called startListeningRTP");

		isRTPSocketOpened = false;

		return new Runnable() {

			public void run() {

				try {

					if (rtpSocket != null) {

						// stop listening RTP packets
						if (stopListeningRTP()) {
							LOGGER.info("RTP datagram socket is closed");
						}

						// wait for socket close
						TimeUnit.SECONDS.sleep(1);

						rtpSocket = new DatagramSocket(rtpListenPort);
						LOGGER.info("RTP datagram socket initialized at "
								+ rtpListenPort);
					} else {

						rtpSocket = new DatagramSocket(rtpListenPort);
						LOGGER.info("RTP datagram socket initialized at "
								+ rtpListenPort);

					}

					isRTPSocketOpened = true;

				} catch (SocketException ex) {
					isRTPSocketOpened = false;
					TestUtility.printError(
							"Error while initializing RTP datagram socket", ex);
				} catch (Exception ex) {
					isRTPSocketOpened = false;
					TestUtility.printError(
							"Error while initializing RTP datagram socket", ex);
				} finally {
					if (serverScriptRunner.getRTPCountDownLatch().getCount() > 0)
						serverScriptRunner.getRTPCountDownLatch().countDown();
				}
			}
		};

	}

	/**
	 * This method used to close RTP port return true if RTP data gram socket is
	 * closed
	 */

	public boolean stopListeningRTP() {

		// LOGGER.info("called stopListeningRTP");

		boolean isRTPClosed = false;

		try {

			isRTPSocketOpened = false;

			if (rtpSocket != null) {
				rtpSocket.close();
				rtpSocket = null;
				// LOGGER.info("RTP datagram socket closed");
				isRTPClosed = true;
			}
		} catch (Exception ex) {
			LOGGER.error("Error while stopping RTP listener", ex);

		}

		return isRTPClosed;

	}

	/**
	 * This method is used to receive the RTP data gram
	 * 
	 * @return DatagramPacket
	 * @throws IOException
	 */

	public DatagramPacket receiveRTPdatagram() throws IOException {

		LOGGER.info("called receiveRTPdatagram");

		DatagramPacket dataPacket = new DatagramPacket(rtpBuf,
				MAX_MEDIA_PKT_LENGTH);
		rtpSocket.receive(dataPacket);
		LOGGER.info("receiving RTP message from "
				+ dataPacket.getAddress().getHostAddress() + " and port "
				+ dataPacket.getPort());
		return dataPacket;

	}

	/**
	 * this method is used to check whether RTP port bound or not
	 * 
	 * @return true if RTP port bound
	 */

	public boolean isRTPSocketBounded() {

		if (rtpSocket != null && rtpSocket.isBound()) {
			LOGGER.info("RTP datagram socket bounded");
			return true;
		} else {
			LOGGER.info("RTP datagram socket not bounded");
			return false;
		}
	}

	/**
	 * This method is used to send the RTP data gram
	 * 
	 * @param rtpPacket
	 *            is a byte array consists of RTP data
	 * @param rtpPort
	 *            represents rtp port of client/SUT
	 * @return true if RTP data is sent successfully
	 * @throws IOException
	 */

	public boolean sendRTPdatagram(byte[] rtpPacket, int rtpPort)
			throws IOException {

		LOGGER.info("called sendRTPdatagram");

		LOGGER.info("sending RTP message to " + CLIENT_IP_ADDR.getHostAddress()
				+ " and port " + rtpPort);

		DatagramPacket packet = new DatagramPacket(rtpPacket, rtpPacket.length,
				CLIENT_IP_ADDR, rtpPort);
		LOGGER.info(packet.toString());
		rtpSocket.send(packet);

		return true;
	}

	/**
	 * This method is used to check whether the RTP port opened or not
	 * 
	 * @return true if RTP port opened
	 */

	public boolean isRTPSocketOpened() {

		return isRTPSocketOpened;

	}

	/**
	 * send RTP datagram
	 * 
	 * @param action
	 *            is com.mitester.jaxbparser.server.ACTION type object which
	 *            consists of current action details
	 * @throws ControllerException
	 * @throws MediaException
	 */

	public boolean sendRTPPacket(com.mitester.jaxbparser.server.ACTION action)
			throws ControllerException, MediaException {

		boolean isRTPSent = false;

		List<Object> rtpPacketList = null;

		com.mitester.jaxbparser.server.MEDIA media = null;

		try {

			TestUtility.printMessage(NORMAL, "sending......."
					+ action.getValue(), LINE_SEPARATOR);
			LOGGER.info("sending......." + action.getValue());

			media = (com.mitester.jaxbparser.server.MEDIA) action.getMEDIA();

			String fileName = media.getFile().getSource();

			if (fileName.endsWith(FILE_EXTENSE_WITH_PACP)) {

				// read RTP packet
				rtpPacketList = MediaPacketHandler.readPcapFile(media.getFile()
						.getSource());

				if (rtpPacketList != null) {

					for (Object packet : rtpPacketList) {

						byte bytes[] = (byte[]) packet;

						// decoding RTCP packet
						String rtpMessage = MediaPacketHandler.decodeMessage(
								bytes, bytes.length);

						LOGGER.info(LINE_SEPARATOR + OUTGOING_RTP_MESSAGE
								+ LINE_SEPARATOR + rtpMessage
								+ OUTGOING_RTP_MESSAGE);

						TestUtility.printMessage(rtpMessage);

						// send RTP data gram
						sendRTPdatagram(bytes, SUT_RTP_PORT_NO);

						TestUtility.printMessage(CALL_FLOW, OUTGOING_MSG,
								action.getValue());
					}

					isRTPSent = true;

				} else {
					LOGGER.error("Error in reading RTP packets from '"
							+ fileName + "' file");
					TestResult
							.setFailReason("Error in reading RTP packets from '"
									+ fileName + "' file");
				}

			} else if (fileName.endsWith(FILE_EXTENSE_WITH_TXT)) {

				// read RTP packet
				String rtpMessage = MediaPacketHandler.readTxtFile(media
						.getFile().getSource());

				if (rtpMessage != null) {

					LOGGER.info(LINE_SEPARATOR + OUTGOING_RTP_MESSAGE
							+ LINE_SEPARATOR + rtpMessage
							+ OUTGOING_RTP_MESSAGE);
					TestUtility.printMessage(rtpMessage);

					// encode RTP message
					byte[] rtpPacket = MediaPacketHandler
							.encodeMessage(rtpMessage);

					// send RTP data gram
					sendRTPdatagram(rtpPacket, SUT_RTP_PORT_NO);

					TestUtility.printMessage(CALL_FLOW, OUTGOING_MSG, action
							.getValue());
					isRTPSent = true;

				} else {
					LOGGER.error("Error in reading RTP packets from '"
							+ fileName + "' file");
					TestResult
							.setFailReason("Error in reading RTP packets from '"
									+ fileName + "' file");
				}
			}
		} catch (com.mitester.media.MediaException ex) {
			throw ex;

		} catch (IOException ex) {

			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error in sending RTP packet to SUT");

		} catch (Exception ex) {

			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error in sending RTP packet to SUT");
		}
		return isRTPSent;

	}

	/**
	 * receive and process RTP datagram
	 * 
	 * @param action
	 *            is com.mitester.jaxbparser.server.ACTION type object which
	 *            consists of details for current action
	 * @throws ControllerException
	 */

	public void receiveRTPPacket(com.mitester.jaxbparser.server.ACTION action)
			throws ControllerException {

		try {

			String strBin = null;

			// start server wait timer
			serverScriptRunner.startServerTimer();

			TestUtility.printMessage(NORMAL, "waiting for......."
					+ action.getValue(), LINE_SEPARATOR);
			LOGGER.info("waiting for......." + action.getValue());

			do {

				// call flow display
				TestUtility.printMessage(CALL_FLOW, INCOMING_MSG, action
						.getValue());

				// receiving RTP packet
				DatagramPacket rtpPacket = receiveRTPdatagram();

				// decoding RTP packet
				String rtpMessage = MediaPacketHandler.decodeMessage(rtpPacket
						.getData(), rtpPacket.getLength());

				TestUtility.printMessage(rtpMessage);

				LOGGER.info(LINE_SEPARATOR + INCOMING_RTP_MESSAGE
						+ LINE_SEPARATOR + rtpMessage + INCOMING_RTP_MESSAGE);

				// Validating Version
				int separatorIndex = rtpMessage.indexOf(SPACE_SEPARATOR);
				String packetType = rtpMessage.substring(0, separatorIndex);
				strBin = Integer.toBinaryString(Integer.parseInt(Integer
						.toHexString(Integer.parseInt(packetType, 16))));

			} while (!strBin.startsWith("10"));

			// call flow display
			TestUtility.printMessage(CALL_FLOW, INCOMING_MSG, "MSG RECEIVED",
					action.getValue());

			// stop server wait timer
			serverScriptRunner.stopServerTimer();

		} catch (IOException ex) {

			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while receiving and processing the RTP message");
		} catch (Exception ex) {

			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.executor.ControllerException(
					"Error while receiving and processing the RTP message");
		}

	}

}
