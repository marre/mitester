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
 * Package 				License 										Details
 *---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Jain SIP stack 		NIST-CONDITIONS-OF-USE 						        https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 				The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * 
 */

/*
 * miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications.
 *  
 */
package com.mitester.sipserver;

import static com.mitester.sipserver.SipServerConstants.CLIENT_IP_ADDRESS;
import static com.mitester.sipserver.SipServerConstants.OUTGOING_MSG;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.ParseException;
import java.util.logging.Logger;

import javax.sip.SipException;

import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;

/**
 * UDP Socket communication is to send and receive packet from/to system under
 * test and miTester
 * 
 * @author Kulandaivelu
 * 
 */
public class UdpCommn {

	private static final Logger LOGGER = MiTesterLog.getLogger(UdpCommn.class
			.getName());

	private DatagramSocket udpSocket = null;

	private final byte[] buf = new byte[SipServerConstants.MAX_BUFFER_LENGTH];

	private DatagramPacket dataPacket = null;

	private final int SERVER_PORT;

	private final int CLIENT_PORT;

	private final InetAddress CLIENT_IP_ADDR = TestUtility
			.getHostAddress(CONFIG_INSTANCE.getValue(CLIENT_IP_ADDRESS));

	public UdpCommn() {

		if (CONFIG_INSTANCE.isKeyExists(SipServerConstants.SERVER_LISTEN_PORT)) {
			SERVER_PORT = Integer.parseInt(CONFIG_INSTANCE
					.getValue(SipServerConstants.SERVER_LISTEN_PORT));
		} else {
			SERVER_PORT = SipServerConstants.DEFAULT_SERVER_PORT;
		}
		if (CONFIG_INSTANCE.isKeyExists(SipServerConstants.CLIENT_LISTEN_PORT)) {
			CLIENT_PORT = Integer.parseInt(CONFIG_INSTANCE
					.getValue(SipServerConstants.CLIENT_LISTEN_PORT));
		} else {
			CLIENT_PORT = SipServerConstants.DEFAULT_CLIENT_PORT;
		}
	}

	/**
	 * initialize UDP datagram socket
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
	 * @return is a byte array
	 * @throws IOException
	 * @throws SocketException
	 */
	public String receiveUdpMessage() throws IOException, SocketException {
		dataPacket = new DatagramPacket(buf, SipServerConstants.MAX_PACKET_SIZE);
		udpSocket.receive(dataPacket);
		LOGGER.info("receiving SIP message from "
				+ dataPacket.getAddress().getHostAddress() + ", port "
				+ dataPacket.getPort());
		return new String(dataPacket.getData(), 0, dataPacket.getLength());
	}

	/**
	 * close udp socket
	 * 
	 * @throws IOException
	 * @throws SocketException
	 */
	public void closeUdpSocket() throws IOException, SocketException {

		if (udpSocket != null) {
			udpSocket.close();
			udpSocket = null;
			LOGGER.info("udp datagram socket closed");
		}
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
	 */
	public void sendUdpMessage(String sipMessage) throws IOException,
			SocketException, ParseException, SipException, NullPointerException {

		LOGGER.info("sending SIP message to " + CLIENT_IP_ADDR.getHostAddress()
				+ ", port " + CLIENT_PORT);
		ProcessSIPMessage.processSIPMessage(sipMessage, OUTGOING_MSG);
		DatagramPacket packet = new DatagramPacket(sipMessage.getBytes(),
				sipMessage.length(), CLIENT_IP_ADDR, CLIENT_PORT);
		LOGGER.info(packet.toString());
		udpSocket.send(packet);
	}
}
