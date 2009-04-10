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
 * Package 					License 											Details
 *---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Jain SIP stack 			NIST-CONDITIONS-OF-USE 								https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 					The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * 
 */

/* miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications. 
 */
package com.mitester.adapter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;
import com.mitester.utility.ClientStarter;

import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;

/**
 * This class implements all the methods declared in the Adapter interface.
 * 
 */

public class SipAdapter implements Adapter {

	private static final Logger LOGGER = MiTesterLog.getLogger(SipAdapter.class
			.getName());

	private static final String TCP_SERVER_PORT = "TCP_SERVER_PORT";

	private static final String TEST_APPLICATION_PATH = "TEST_APPLICATION_PATH";

	private static final String TEST_MODE_ADVANCED = "ADVANCED";

	private static final String TEST_MODE = CONFIG_INSTANCE
			.getValue("TEST_MODE");

	private ServerSocket serverSocket = null;

	private Socket connection = null;

	private DataInputStream dataIn = null;

	private DataOutputStream dataOut = null;

	private boolean isStarted = false;

	private boolean isStopped = false;

	private boolean isClosedDefault = true;

	private String processesBefStart = null;

	private String processesAfStart = null;

	private String newProcesses = null;

	private ClientStarter executeClient = new ClientStarter();

	private int tcpServerPort = 0;

	private String testApplicationPath = null;

	public SipAdapter() {

		if (TEST_MODE.equals(TEST_MODE_ADVANCED)) {

			tcpServerPort = Integer.parseInt(CONFIG_INSTANCE
					.getValue(TCP_SERVER_PORT));

			testApplicationPath = CONFIG_INSTANCE
					.getValue(TEST_APPLICATION_PATH);

		}

	}

	public boolean isRunning() {

		if ((connection != null) && (connection.isConnected())) {
			LOGGER.info("client is running...");
			return true;
		} else {
			LOGGER.info("client is not running...");
			return false;
		}
	}

	public String receive() throws IOException, SocketException,
			InterruptedException {
		String message = dataIn.readUTF();
		LOGGER.info("received message from SUT: " + message);
		return message;
	}

	public void send(String message) throws IOException, SocketException,
			InterruptedException {

		dataOut.writeUTF(message);
		dataOut.flush();
		TestUtility.printMessage("Msg to SUT ==>" + message);
		LOGGER.info("message sending to SUT: " + message);
	}

	public boolean start() {

		try {
			isStarted = false;

			serverSocket = new ServerSocket(tcpServerPort);

			/* get the running process details before start the application */
			processesBefStart = executeClient.getProcessInfo();

			/* start client */
			if (!isRunning()) {

				if (TestUtility.isFileExist(testApplicationPath)) {

					isStarted = executeClient.startClient(testApplicationPath);

				} else {

					LOGGER.info("Test Application Path does not exists");
					TestUtility
							.printMessage("Test Application Path does not exists");
					return isStarted;
				}
			}

			if (!isStarted) {
				LOGGER.severe("client not started");
				return isStarted;
			} else {
				LOGGER.info("client started");
			}

			connection = serverSocket.accept();
			TestUtility.printMessage("TCP/IP established with client");
			LOGGER.info("TCP/IP established with client");

			dataOut = new DataOutputStream(connection.getOutputStream());
			dataIn = new DataInputStream(connection.getInputStream());

			/* get the running process details after start the application */
			processesAfStart = executeClient.getProcessInfo();

			String processes[] = processesAfStart.split(";");
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < processes.length; i++) {

				if (processesBefStart.indexOf(processes[i]) < 0) {

					sb.append(processes[i]);
					sb.append(";");
				}
			}

			newProcesses = new String(sb.toString());
			LOGGER.info("new processes after run the application "
					+ newProcesses);

		} catch (InterruptedException ex) {
			TestUtility.printError("Error at starting client ", ex);
			isStarted = false;
		} catch (SecurityException ex) {
			TestUtility.printError("Error at starting client ", ex);
			isStarted = false;
		} catch (SocketException ex) {
			TestUtility.printError("Error at starting client ", ex);
			isStarted = false;
		} catch (IOException ex) {
			TestUtility.printError("Error at starting client ", ex);
			isStarted = false;
		} catch (NullPointerException ex) {
			TestUtility.printError("Error at starting client ", ex);
			isStarted = false;
		} catch (IllegalArgumentException ex) {
			TestUtility.printError("Error at starting client ", ex);
			isStarted = false;
		}
		return isStarted;

	}

	public boolean stop() {

		try {

			isStopped = false;

			/* stop client */
			if (isRunning()) {
				isStopped = executeClient.stopClient(testApplicationPath,
						processesBefStart, newProcesses);
				isClosedDefault = false;
			}

			if (isStopped) {

				LOGGER.info("client closed");
			} else {

				LOGGER.info("client not closed");
			}

		} catch (InterruptedException ex) {
			TestUtility.printError("Error at closing client", ex);
			isStopped = false;
		} catch (SecurityException ex) {
			TestUtility.printError("Error at stopping client ", ex);
			isStopped = false;
		} catch (SocketException ex) {
			TestUtility.printError("Error at stopping client ", ex);
			isStopped = false;
		} catch (IOException ex) {
			TestUtility.printError("Error at stopping client ", ex);
			isStopped = false;
		} catch (NullPointerException ex) {
			TestUtility.printError("Error at stopping client ", ex);
			isStopped = false;
		} catch (IllegalArgumentException ex) {
			TestUtility.printError("Error at stopping client ", ex);
			isStopped = false;
		}
		return isStopped;
	}

	public Socket getSocket() {
		return connection;
	}

	public boolean isClosed() {
		return isStopped;
	}

	public boolean isClosedDefault() {
		return isClosedDefault;
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
			TestUtility.printError("Error at cleaning socket ", ex);

		} finally {

			dataIn = null;
			dataOut = null;
			connection = null;
			serverSocket = null;
			isStarted = false;
			isStopped = false;
			isClosedDefault = true;
			LOGGER.info("cleaned-up the socket");
		}
	}
}
