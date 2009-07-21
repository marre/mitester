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
 * Package 						License 											Details
 *---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Jain SIP stack 				NIST-CONDITIONS-OF-USE 								https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 						The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * JNetStreamStandalone lib     GNU Library or LGPL			     					http://sourceforge.net/projects/jnetstream/
 * 
 */

/*
 * miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications. 
 */

package com.mitester.adapter;

import java.io.IOException;
import java.net.Socket;

/**
 * This class consists of set of abstract methods which are used to establish
 * the TCP communication channel through which miTester sends the SUT actions
 * and receives SUT notifications(status change of SUT).
 */
public interface Adapter {

	/**
	 * This method used to start the SUT
	 * 
	 * @return true on successful start of SUT
	 */
	public boolean start();

	/**
	 * This method is used to stop the SUT
	 * 
	 * @return true if client is closed by miTester
	 */
	public boolean stop();

	/**
	 * It sends the message(client action) to SUT
	 * 
	 * @param actionMessage
	 *            consists of action description going to be simulated by SUT
	 * @throws IOException
	 */
	public void send(String actionMessage) throws IOException;

	/**
	 * It receives the message(client status change or notification) from SUT
	 * 
	 * @return String message received from SUT
	 * @throws IOException
	 */
	public String receive() throws IOException;

	/**
	 * It used to check the existence of the TCP connection
	 * 
	 * @return true when SUT is connected with miTester
	 */
	public boolean isConnected();

	/**
	 * It returns the TCP socket
	 * 
	 * @return Socket
	 */
	public Socket getSocket();

	/**
	 * This method return the closing status of client
	 * 
	 * @return true when SUT is closed
	 */
	public boolean isClosed();

	/**
	 * This method used to check whether the client is closed by default
	 * 
	 * @return true when stop() is called
	 */
	public boolean isStopCalled();

	/**
	 * This method cleaning up all socket related variables
	 * 
	 */

	public void cleanUpSocket();

	/**
	 * This method is used to check the existence of client
	 * 
	 * @return true if the client exists
	 */
	public boolean checkClientAvailable();

}
