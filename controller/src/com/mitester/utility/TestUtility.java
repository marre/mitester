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
package com.mitester.utility;

import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;
import static com.mitester.utility.UtilityConstants.CALL_FLOW;
import static com.mitester.utility.UtilityConstants.CALL_FLOW_MODE;
import static com.mitester.utility.UtilityConstants.FILE_SEPARATOR;
import static com.mitester.utility.UtilityConstants.LINE_SEPARATOR;
import static com.mitester.utility.UtilityConstants.LOG_MODE;
import static com.mitester.utility.UtilityConstants.NORMAL;
import static com.mitester.utility.UtilityConstants.HELP_MODE;
import static com.mitester.utility.UtilityConstants.HELP;
import static com.mitester.utility.UtilityConstants.NORMAL_MODE;
import static com.mitester.utility.UtilityConstants.OS_NAME;
import static com.mitester.utility.UtilityConstants.RESULT;
import static com.mitester.utility.UtilityConstants.RESULT_MODE;
import static com.mitester.utility.UtilityConstants.THREE_DIGIT_FORMAT;
import static com.mitester.utility.UtilityConstants.TWO_DIGIT_FORMAT;
import static com.mitester.utility.UtilityConstants.WINDOWS_OS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 * This class contains the methods which are used to check the existence of file
 * or directory, number of tests, order of tests and also used to write the
 * messages in the console window
 * 
 */

public class TestUtility {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(TestUtility.class.getName());

	private static final BufferedWriter SYSTEM_OUT = new BufferedWriter(
			new OutputStreamWriter(System.out));

	private static final BufferedWriter SYSTEM_ERR = new BufferedWriter(
			new OutputStreamWriter(System.err));

	private static String modeOfDisplay = NORMAL_MODE;

	private static boolean isCallFlowWait = false;

	private TestUtility() {

	}

	/**
	 * method used for checking availability of file
	 * 
	 * @param fileName
	 *            is a name of file required to check
	 * @return true if file exists
	 */
	public static boolean isFileExist(String fileName) throws IOException {
		return new File(fileName).exists();

	}

	/**
	 * It used for check number of tests available in both server and client
	 * scripts
	 * 
	 * @param clientTests
	 *            is a List holds client Tests and its actions
	 * @param serverTests
	 *            is a List holds server Tests and its actions
	 * @return true if number of tests in the both server and client scripts are
	 *         same else false
	 */

	public static boolean checkTestsAvailable(
			List<com.mitester.jaxbparser.client.TEST> clientTests,
			List<com.mitester.jaxbparser.server.TEST> serverTests) {

		boolean isTestsAvailable = false;
		boolean isTestsMisMatced = false;

		LOGGER.info("entered into checkTestsAvailablity");

		if ((clientTests.size()) != (serverTests.size())) {

			TestUtility
					.printMessage(NORMAL,
							"Number of tests in both client and server scripts are mis-match");

			LOGGER
					.error("Number of tests in both client and server scripts are mis-match");
			return isTestsAvailable;
		}

		// check duplication of client Test
		HashSet<String> set = new HashSet<String>();

		for (int i = 0; i < clientTests.size(); i++) {
			boolean val = set.add(clientTests.get(i).getTESTID());
			if (val == false) {
				TestUtility.printMessage(NORMAL, clientTests.get(i).getTESTID()
						+ " already exists in client script");

				LOGGER.error(clientTests.get(i).getTESTID()
						+ " already exists in client script");
				return isTestsAvailable;
			}
		}

		// check duplication of server Test
		HashSet<String> set1 = new HashSet<String>();

		for (int i = 0; i < serverTests.size(); i++) {
			boolean val = set1.add(serverTests.get(i).getTESTID());
			if (val == false) {
				TestUtility.printMessage(NORMAL, serverTests.get(i).getTESTID()
						+ " already exists in server script");

				LOGGER.error(serverTests.get(i).getTESTID()
						+ " already exists in server script");
				return isTestsAvailable;
			}
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
						.printMessage("Mis-Matched client and server test scripts");
				LOGGER.error("Mis-Matched client and server test scripts");
				break;
			} else {
				isTestsAvailable = false;
				TestUtility.printMessage(NORMAL, clientTestID
						+ " does not exist in the server script");
				LOGGER.error(clientTestID
						+ " does not exist in the server script");
				break;
			}
		}
		return isTestsAvailable;
	}

	/**
	 * This method prints the message in the console window according to mode of
	 * display
	 * 
	 * @param messages
	 *            consists of informations going to be printed on the console
	 *            window
	 */
	public static void printMessage(String... messages) {

		if (messages.length > 1) {
			if (messages[0].equals(NORMAL)) {
				if (modeOfDisplay.equalsIgnoreCase(NORMAL_MODE)) {
					print(messages[1]);
				} else if (modeOfDisplay.equalsIgnoreCase(LOG_MODE)) {
					print(messages[1]);
				} else if (modeOfDisplay.equalsIgnoreCase(CALL_FLOW_MODE)
						&& messages.length == 2) {
					print(messages[1]);
				}
			} else if (messages[0].equals(CALL_FLOW)
					&& modeOfDisplay.equalsIgnoreCase(CALL_FLOW_MODE)) {

				if (messages[1].indexOf(String.format("%s%28s", "miTester",
						"SUT")) > 0) {
					printCallFlow(messages[1]);
					printCallFlow(LINE_SEPARATOR);

				} else if (messages[1].equals("outgoing SIP message")) {
					if (messages[2].length() < 21) {
						printCallFlow(String.format("%20s%38s", messages[2],
								"|--------------------------------->|"));
					} else {
						printCallFlow(String
								.format("%20s%38s", messages[2]
										.substring(0, 16)
										+ "...",
										"|--------------------------------->|"));
					}
					printCallFlow(LINE_SEPARATOR);
				} else if (messages[1].equals("incoming SIP message")
						&& messages[2].equals("MSG RECEIVED")) {
					if (isCallFlowWait)
						printCallFlow("<---------------------------------|");
					else {

						if (messages[3].length() < 21) {
							printCallFlow(String.format("%20s%38s",
									messages[3],
									"|<---------------------------------|"));
						} else {
							printCallFlow(String.format("%20s%38s", messages[3]
									.substring(0, 16)
									+ "...",
									"|<---------------------------------|"));
						}

					}
					isCallFlowWait = false;
					printCallFlow(LINE_SEPARATOR);

				} else if (messages[1].equals("incoming SIP message")) {
					if (messages[2].length() < 21) {
						printCallFlow(String
								.format("%20s%3s", messages[2], "|"));
					} else {
						printCallFlow(String.format("%20s%3s", messages[2]
								.substring(0, 16)
								+ "...", "|"));
					}
					isCallFlowWait = true;
				}

			} else if (messages[0].equals(RESULT)
					&& modeOfDisplay.equalsIgnoreCase(RESULT_MODE)) {
				print(messages[1]);
			} else if (messages[0].equals(HELP)
					&& modeOfDisplay.equalsIgnoreCase(HELP_MODE)) {
				print(messages[1]);
			} else if (modeOfDisplay.equalsIgnoreCase(LOG_MODE)) {
//				if (messages.length == 2)
//					print(messages[1]);
			}
		} else {
			if (modeOfDisplay.equalsIgnoreCase(LOG_MODE)) {
				if (messages.length == 1)
					print(messages[0]);
			}
		}
	}

	/**
	 * This method prints the message in the console window
	 * 
	 * @param msg
	 *            specifies the message going to be printed in the console
	 *            window
	 */

	private static void print(String msg) {

		try {
			SYSTEM_OUT.write(msg);
			SYSTEM_OUT.write(LINE_SEPARATOR);
			SYSTEM_OUT.flush();

		} catch (IOException ex) {
			TestUtility.printError("Error at printing message", ex);
		}
	}

	/**
	 * This method is called to print the call flow message in the console
	 * window
	 * 
	 * @param msg
	 *            String object printed on the console window
	 */

	private static void printCallFlow(String msg) {

		try {
			SYSTEM_OUT.write(msg);
			SYSTEM_OUT.flush();

		} catch (IOException ex) {
			TestUtility.printError("Error at printing message", ex);
		}
	}

	/**
	 * It prints the error message in the console window
	 * 
	 * @param message
	 *            specifies the error message going to be printed in the log
	 *            file
	 * 
	 * @param exception
	 *            specifies the exception info going to be printed in the log
	 *            file
	 * 
	 */
	public static void printError(String errorMessage, Exception exception) {

		try {

			LOGGER.error(errorMessage, exception);
			if (modeOfDisplay.equalsIgnoreCase(LOG_MODE)) {
				SYSTEM_ERR.write(errorMessage);
				SYSTEM_ERR.write(LINE_SEPARATOR);
				SYSTEM_ERR.flush();
			}
		} catch (IOException ex) {
		}

	}

	/**
	 * it closes all streams
	 * 
	 */
	public static void close() {
		try {
			SYSTEM_ERR.close();
			SYSTEM_OUT.close();
		} catch (IOException ex) {
			TestUtility.printError("Error at closing I/O streams", ex);
		}

	}

	/**
	 * This method return the time as String format
	 * 
	 * @return current time
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
	 * it returns InetAddress of the given client IP address
	 * 
	 * @param ipAddress
	 *            client IP address
	 * 
	 * @return InetAddress
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
			TestUtility.printError("Error while getting IP address", ex);
			return null;
		}
	}

	/**
	 * it sets the mode of the display window
	 * 
	 * @param mode
	 *            represents the mode of the display window
	 */
	public static void setModeOfDisplay(String mode) {
		isCallFlowWait = false;
		modeOfDisplay = mode;
	}

	/**
	 * return the mode of the display window
	 * 
	 */
	public static String getModeOfDisplay() {
		return modeOfDisplay;
	}

	/**
	 * remove the shell
	 * 
	 */

	public static void removeShell() {

		try {

			if (OS_NAME.startsWith(WINDOWS_OS)) {

				String batchPath = CONFIG_INSTANCE.getValue("BATCH_FILE_PATH");
				if (batchPath == null)
					return;

				if (TestUtility.isFileExist(batchPath)) {
					new File(batchPath).delete();
				}

			} else {

				String shPath = new java.io.File(".").getCanonicalPath()
						+ FILE_SEPARATOR + "runApp.sh";

				if (TestUtility.isFileExist(shPath)) {
					new File(shPath).delete();
				}
			}
		} catch (IOException e) {

		}
	}

	/**
	 * title of the call flow
	 * 
	 */

	public static void callFlow() {

		if (modeOfDisplay.equalsIgnoreCase(CALL_FLOW_MODE)) {
			printMessage(CALL_FLOW, String
					.format("%30s%28s", "miTester", "SUT"));
		}
	}

	/**
	 * check the all characters in the string are digits
	 * 
	 * @param value
	 *            is a string going to be checked
	 * @return true if all characters in the string are digits
	 */

	public static boolean isAllDigit(String value) {

		char digits[] = value.toCharArray();

		if (digits.length == 0)
			return false;

		for (int i = 0; i < digits.length; i++) {

			if (!(Character.isDigit(digits[i])))
				return false;
		}

		return true;

	}
	/**
	 * set isCallFlowWait flag
	 * @param callFlowFlag is a boolean value used to set the isCallFlowWait flag
	 */
	public static void setIsCallFlowWait(boolean callFlowFlag) {
		isCallFlowWait = callFlowFlag;
	}

}
