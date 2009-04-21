/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: TestUtility.java
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
package com.mitester.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * This class contains the methods which are used to check the existence of file
 * or directory, number of tests, order of tests and also used to read and write
 * the messages in the console
 * 
 */

public class TestUtility {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(TestUtility.class.getName());

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	private static final DecimalFormat TWO_DIGIT_FORMAT = new DecimalFormat(
			"00");

	private static final DecimalFormat THREE_DIGIT_FORMAT = new DecimalFormat(
			"000");

	private static final BufferedWriter SYSTEM_OUT = new BufferedWriter(
			new OutputStreamWriter(System.out));

	private static final BufferedWriter SYSTEM_ERR = new BufferedWriter(
			new OutputStreamWriter(System.err));

	private static final BufferedReader SYSTEM_IN = new BufferedReader(
			new InputStreamReader(System.in));

	private TestUtility() {

	}

	/**
	 * method used for checking availability of file
	 * 
	 * @param fileName
	 *            is a name of file required to check
	 * @return is a boolean value
	 */
	public static boolean isFileExist(String fileName) throws IOException {
		return new File(fileName).exists();

	}

	/**
	 * method used for check number of tests available in both server and client
	 * scripts
	 * 
	 * @param clientTests
	 *            is a com.mitester.jaxbparser.client.TEST includes set of
	 *            client Tests and its actions
	 * @param serverTests
	 *            is a com.mitester.jaxbparser.server.TEST includes set of
	 *            server Tests and its actions
	 * @return is a boolean value represents true if number of tests in the both
	 *         server and client scripts are same else false
	 */

	public static boolean checkTestsAvailablity(
			List<com.mitester.jaxbparser.client.TEST> clientTests,
			List<com.mitester.jaxbparser.server.TEST> serverTests) {
		boolean isTestsAvailable = false;
		boolean isTestsMisMatced = false;

		if ((clientTests.size()) != (serverTests.size())) {
			System.out
					.println("please check the number of Tests available in both Client and Server Scripts");
			return isTestsAvailable;
		}

		for (com.mitester.jaxbparser.client.TEST clientTest : clientTests) {
			boolean isTestFind = false;
			int clientTestPos = 0;
			int serverTestPos = 0;
			String clientTestID = clientTest.getTESTID();
			clientTestPos++;
			for (com.mitester.jaxbparser.server.TEST serverTest : serverTests) {
				String serverTestID = serverTest.getTESTID();
				if (clientTestID.equals(serverTestID)) {
					serverTestPos++;
					if (serverTestPos == clientTestPos)
						isTestFind = true;
					else
						isTestsMisMatced = true;
					break;
				}

			}
			if (isTestFind) {
				isTestsAvailable = true;
			} else if (isTestsMisMatced) {
				isTestsAvailable = false;
				TestUtility
						.printMessage(" both Client Tests and Server Tests are Mis-Matched");
				break;
			} else {
				isTestsAvailable = false;
				TestUtility.printMessage(clientTestID
						+ " is not available in the server script");
				break;
			}
		}
		return isTestsAvailable;
	}

	/**
	 * This method is called to print the message in the console window
	 * 
	 * @param message
	 *            String object printed on the console window
	 */
	public static void printMessage(String message) {
		try {
			SYSTEM_OUT.write(message);
			SYSTEM_OUT.write("\n");
			SYSTEM_OUT.flush();
		} catch (IOException ex) {
			TestUtility.printError("Error at printing message", ex);
		}
	}

	/**
	 * This method is used to print the error message in the console window
	 * 
	 * @param message
	 *            String object printed on the console window
	 */
	public static void printError(String errorMessage, Exception exception) {
		try {
			LOGGER.severe(errorMessage + " " + exception);
			SYSTEM_ERR.write(errorMessage);
			SYSTEM_ERR.write(LINE_SEPARATOR);
			SYSTEM_ERR.flush();
		} catch (IOException ex) {
		}
	}

	/**
	 * This method is used to read the message from the console window
	 * 
	 * @return String object
	 */
	public static String readMessage() {
		try {
			return SYSTEM_IN.readLine();
		} catch (IOException ex) {
			TestUtility.printError("Error at reading message", ex);
			return null;
		}
	}

	/**
	 * close all streams
	 * 
	 */
	public static void close() {
		try {
			SYSTEM_IN.close();
			SYSTEM_ERR.close();
			SYSTEM_OUT.close();
		} catch (IOException ex) {
			TestUtility.printError("Error at closing I/O streams", ex);
		}

	}

	/**
	 * This method return the time as String format
	 * 
	 * @return String object represents current time
	 */
	public static String getTime() {

		StringBuilder buf = new StringBuilder();
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		int seconds = cal.get(Calendar.SECOND);
		int millis = cal.get(Calendar.MILLISECOND);
		buf.append(TWO_DIGIT_FORMAT.format(hour)).append(':');
		buf.append(TWO_DIGIT_FORMAT.format(minutes)).append(':');
		buf.append(TWO_DIGIT_FORMAT.format(seconds)).append('.');
		buf.append(THREE_DIGIT_FORMAT.format(millis)).append(' ');
		buf.append(' ');
		return buf.toString();
	}

	/**
	 * check the characters in string are digits
	 * 
	 * @param processId
	 *            name of the process id
	 * @return boolean value represents true if all the characters of the
	 *         processId are digits
	 */

	public static boolean isAllDigit(String processId) {

		char digits[] = processId.toCharArray();

		for (int i = 0; i < digits.length; i++) {

			if (!(Character.isDigit(digits[i])))
				return false;
		}

		return true;

	}

	/**
	 * Returns InetAddress of the Client IP address
	 * 
	 * @param ipAddress
	 *            string object points the Client IP address
	 * @return InetAddress of the given IP address
	 */
	public static InetAddress getHostAddress(String ipAddress) {
		try {
			StringTokenizer st = new StringTokenizer(ipAddress, ".");
			byte[] ip = new byte[4];
			int i = 0;
			while (st.hasMoreElements()) {
				int element = Integer.parseInt((String) st.nextElement());
				ip[i++] = new Integer(element).byteValue();
			}
			return InetAddress.getByAddress(ip);
		} catch (Exception ex) {
			TestUtility.printError("error in getting IP address", ex);
			return null;
		}
	}
}
