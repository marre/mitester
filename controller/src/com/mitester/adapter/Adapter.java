/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: Adapter.java
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

/*
 * miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications. 
 */

package com.mitester.adapter;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * It consists of set of abstract methods which are used to establish the TCP
 * communication channel through which miTester sends the client actions and
 * receives client notifications.
 */
public interface Adapter {

	/**
	 * This method used to start the SUT
	 * 
	 * @return true if client started successfully
	 * @throws IOException
	 * @throws SocketException
	 * @throws InterruptedException
	 */
	public boolean start();

	/**
	 * This method is used to stop the SUT
	 * 
	 * @return true if client closed successfully
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws SocketException
	 */
	public boolean stop();

	/**
	 * This method is used to send the message to SUT
	 * 
	 * @param message
	 * @throws IOException
	 * @throws SocketException
	 * @throws InterruptedException
	 */
	public void send(String message) throws IOException, SocketException,
			InterruptedException;

	/**
	 * This method used for receiving message from TCP channel
	 * 
	 * @return the String message received from SUT
	 * @throws IOException
	 * @throws SocketException
	 * @throws InterruptedException
	 */
	public String receive() throws IOException, SocketException,
			InterruptedException;

	/**
	 * This method used to check the existence of TCP socket
	 * 
	 * @return true if client is running
	 */
	public boolean isRunning();

	/**
	 * This method returns the TCP socket object
	 * 
	 * @return Socket
	 */
	public Socket getSocket();

	/**
	 * This method return the closing status of client
	 * 
	 * @return true if there is no TCP communication between miTester and SUT
	 */
	public boolean isClosed();

	/**
	 * This method used to check whether client closed by default
	 * 
	 * @return true when closed by itself not closed by miTester
	 */
	public boolean isClosedDefault();

	/**
	 * This method cleaning up all socket variables
	 * 
	 */

	public void cleanUpSocket();

}