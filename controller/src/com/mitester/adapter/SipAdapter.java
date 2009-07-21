/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SipAdapter.java
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
package com.mitester.adapter;

import static com.mitester.executor.ExecutorConstants.MITESTER_MODE;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

import com.mitester.utility.ClientStarter;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;
import com.mitester.utility.ThreadControl;

/**
 * This class implements all the methods declared in the Adapter interface.
 * 
 */

public class SipAdapter implements Adapter {

	private static final Logger LOGGER = MiTesterLog.getLogger(SipAdapter.class
			.getName());

	private static final String TEST_MODE = CONFIG_INSTANCE
			.getValue(MITESTER_MODE);

	private ServerSocket serverSocket = null;

	private Socket connection = null;

	private DataInputStream dataIn = null;

	private DataOutputStream dataOut = null;

	private boolean isStarted = false;

	private boolean isStopped = false;

	private boolean isStopCalled = false;

	private String processesBefStart = null;

	private String processesAfStart = null;

	private String newProcesses = null;

	private ClientStarter executeClient = new ClientStarter();

	private int tcpServerPort = 0;

	private String testApplicationPath = null;
	
	private ThreadControl threadControl = null;

	public SipAdapter(ThreadControl threadControl) {
		
		this.threadControl = threadControl;

		if (TEST_MODE.equals("ADVANCED")) {

			tcpServerPort = Integer.parseInt(CONFIG_INSTANCE
					.getValue("SUT_TCP_PORT"));
			testApplicationPath = CONFIG_INSTANCE.getValue("SUT_PATH");
		}

	}

	public boolean isConnected() {

		if ((connection != null) && (connection.isConnected()))
			return true;
		else
			return false;
	}

	public String receive() throws IOException {
		String message = dataIn.readUTF();
		LOGGER.info("received the message from SUT: " + message);
		return message;
	}

	public void send(String actionMessage) throws IOException {
		TestUtility.printMessage("message sending to SUT ==> " + actionMessage);
		LOGGER.info("message sending to SUT: " + actionMessage);

		dataOut.writeUTF(actionMessage);
		dataOut.flush();
	}

	public boolean start() {

		try {

			isStarted = false;

			serverSocket = new ServerSocket(tcpServerPort);

			// get the running process details before start
			processesBefStart = executeClient.getProcessInfo();

			// start client
			if (!isConnected())
				LOGGER.info("SUT is not connected");

			isStarted = executeClient.startClient(testApplicationPath);

			if (!isStarted) {
				LOGGER.error("SUT is not started");
				return isStarted;
			} else {
				LOGGER.info("SUT is started");
				isStopCalled = false;
			}

			connection = serverSocket.accept();

			TestUtility
					.printMessage("TCP/IP connection is established with client");
			LOGGER.info("TCP/IP connection is established with client");

			dataOut = new DataOutputStream(connection.getOutputStream());

			dataIn = new DataInputStream(connection.getInputStream());

			// get the running process details after start
			processesAfStart = executeClient.getProcessInfo();

			// stop the check Timer
			threadControl.stopCheckTimer();

			// release the thread once TCP channel is established with SUT and
			// miTester
			threadControl.resume();

			String processes[] = processesAfStart.split(";");

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < processes.length; i++) {

				if (processesBefStart.indexOf(processes[i]) < 0) {

					sb.append(processes[i]);
					sb.append(";");
				}
			}
			newProcesses = new String(sb.toString());
			LOGGER.info("process IDs after starting SUT " + newProcesses);

		} catch (IOException ex) {
			TestUtility.printError("Error while starting the SUT ", ex);
			isStarted = false;
		} catch (Exception ex) {
			TestUtility.printError("Error while starting the SUT ", ex);
			isStarted = false;
		}
		return isStarted;

	}

	public boolean stop() {

		try {

			isStopped = false;

			// stop client
			if (isConnected()) {

				isStopCalled = true;

				isStopped = executeClient.stopClient(testApplicationPath,
						processesBefStart, newProcesses);
			}

		} catch (IOException ex) {
			TestUtility.printError("Error while stopping the SUT ", ex);
			isStarted = false;
		} catch (Exception ex) {
			TestUtility.printError("Error while stopping the SUT ", ex);
			isStarted = false;
		}
		return isStopped;
	}

	public Socket getSocket() {
		return connection;
	}

	public boolean isClosed() {
		return isStopped;
	}

	public boolean isStopCalled() {
		return isStopCalled;
	}

	public boolean checkClientAvailable() {

		boolean isAvailable = false;

		try {

			// set socket timeout
			connection.setSoTimeout(500);
			receive();

		}

		catch (SocketTimeoutException ex) {

			try {

				// set socket timeout
				connection.setSoTimeout(0);

				isAvailable = true;

				LOGGER.info("SUT exists");

			} catch (SocketException e) {
			}

		} catch (IOException e) {
			LOGGER.error("SUT doesn't exist ");

		}

		return isAvailable;
	}

	public void cleanUpSocket() {

		try {

			if (dataIn != null)
				dataIn.close();
			if (dataOut != null)
				dataOut.close();
			if (connection != null)
				connection.close();
			if (serverSocket != null)
				serverSocket.close();

		} catch (Exception ex) {
			TestUtility
					.printError("Error while cleaning socket variables ", ex);

		} finally {

			dataIn = null;
			dataOut = null;
			connection = null;
			serverSocket = null;
			isStarted = false;
			isStopped = false;
			isStopCalled = false;
			LOGGER.info("cleaned-up the socket");
		}
	}
}
