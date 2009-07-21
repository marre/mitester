/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ConfigurationPropertiesValidator.java
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
import static com.mitester.utility.UtilityConstants.NORMAL;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

/**
 * This class validates the miTester properties
 * 
 */

public class ConfigurationPropertiesValidator {
	private static final Logger LOGGER = MiTesterLog
			.getLogger(ConfigurationPropertiesValidator.class.getName());

	private static final String SUT_TCP_PORT = "SUT_TCP_PORT";
	private static final String MITESTER_MODE = "MITESTER_MODE";
	private static final String SUT_IP_ADDRESS = "SUT_IP_ADDRESS";
	private static final String SUT_PATH = "SUT_PATH";
	private static final String SCRIPT_PATH_MITESTER = "SCRIPT_PATH_MITESTER";
	private static final String SCRIPT_PATH_SUT = "SCRIPT_PATH_SUT";
	private static final String SUT_WAIT_TIME = "SUT_WAIT_TIME";
	private static final String MITESTER_WAIT_TIME = "MITESTER_WAIT_TIME";
	private static final String MITESTER_DELAY = "MITESTER_DELAY";
	private static final String TEST_INTERVAL = "TEST_INTERVAL";
	private static final String MITESTER_SIP_PORT = "MITESTER_SIP_PORT";
	private static final String SUT_SIP_PORT = "SUT_SIP_PORT";
	private static final String SUT_RTP_PORT = "SUT_RTP_PORT";
	private static final String MITESTER_RTP_PORT = "MITESTER_RTP_PORT";
	private static final String VALIDATION = "VALIDATION";
	private static final String VALIDATION_FILE_PATH = "VALIDATION_FILE_PATH";
	private static final String CHECK_PRESENCE_OF_HEADER = "CHECK_PRESENCE_OF_HEADER";

	/**
	 * This method is used to check the existence of required properties and
	 * validate the their values
	 * 
	 * @return true when required properties are exist
	 * @throws IOException
	 */

	public static boolean checkPropertyFile() {

		LOGGER.info("entered into checkPropertyFiles");

		if (CONFIG_INSTANCE.isKeyExists(MITESTER_MODE)) {

			if (CONFIG_INSTANCE.getValue(MITESTER_MODE).equals("ADVANCED")) {

				if (checkSUTTCPPort() && checkSUTIPAddress() && checkSUTPath()
						&& checkSUTScriptPath() && checkmiTesterScriptPath()) {

					return checkOptionalProperties();
				} else
					return false;

			} else if (CONFIG_INSTANCE.getValue(MITESTER_MODE).equals("USER")) {

				if (checkSUTIPAddress() && checkmiTesterScriptPath()) {

					return checkOptionalProperties();
				} else
					return false;

			} else {

				TestUtility
						.printMessage(NORMAL,
								"Check the MITESTER_MODE specified in the 'miTester.properties' file");
				LOGGER
						.error("Invalid MITESTER_MODE specified in the 'miTester.properties' file");
				return false;
			}

		} else {

			TestUtility
					.printMessage(NORMAL,
							"No MITESTER_MODE exists in the 'miTester.properties' file");
			LOGGER
					.error("No MITESTER_MODE exists in the 'miTester.properties' file");
			return false;

		}
	}

	/**
	 * validate miTester optional properties
	 * 
	 * @return true on successful validation
	 */
	public static boolean checkOptionalProperties() {

		LOGGER.info("entered into checkOptionalProperties");

		if (CONFIG_INSTANCE.isKeyExists(SUT_WAIT_TIME)) {
			if (!checkSUTWaitTime(CONFIG_INSTANCE.getValue(SUT_WAIT_TIME)))
				return false;
		}

		if (CONFIG_INSTANCE.isKeyExists(MITESTER_WAIT_TIME)) {
			if (!checkmiTesterWaitTime(CONFIG_INSTANCE
					.getValue(MITESTER_WAIT_TIME)))
				return false;
		}

		if (CONFIG_INSTANCE.isKeyExists(MITESTER_DELAY)) {
			if (!checkmiTesterDelay(CONFIG_INSTANCE.getValue(MITESTER_DELAY)))
				return false;
		}

		if (CONFIG_INSTANCE.isKeyExists(TEST_INTERVAL)) {
			if (!checkTestInterval(CONFIG_INSTANCE.getValue(TEST_INTERVAL)))
				return false;
		}

		if (CONFIG_INSTANCE.isKeyExists(SUT_RTP_PORT)) {

			if (!checkSUTRTPPort(CONFIG_INSTANCE.getValue(SUT_RTP_PORT)))
				return false;
			if (CONFIG_INSTANCE.isKeyExists(MITESTER_RTP_PORT)) {
				if (!checkmiTesterRTPPort(CONFIG_INSTANCE
						.getValue(MITESTER_RTP_PORT)))
					return false;
			} else {

				TestUtility
						.printMessage(NORMAL,
								"No MITESTER_RTP_PORT exists in the 'miTester.properties' file");
				LOGGER
						.error("No MITESTER_RTP_PORT exists in the 'miTester.properties' file");
				return false;

			}

		}

		if (CONFIG_INSTANCE.isKeyExists(MITESTER_SIP_PORT)) {
			if (!checkmiTesterListenPort(CONFIG_INSTANCE
					.getValue(MITESTER_SIP_PORT)))
				return false;
		}

		if (CONFIG_INSTANCE.isKeyExists(SUT_SIP_PORT)) {
			if (!checkSUTSIPPort(CONFIG_INSTANCE.getValue(SUT_SIP_PORT)))
				return false;
		}

		if (CONFIG_INSTANCE.isKeyExists(VALIDATION)) {
			if (!checkValidation(CONFIG_INSTANCE.getValue(VALIDATION)))
				return false;
		}

		if (CONFIG_INSTANCE.isKeyExists(CHECK_PRESENCE_OF_HEADER)) {
			if (!checkHeaderPresence(CONFIG_INSTANCE
					.getValue(CHECK_PRESENCE_OF_HEADER)))
				return false;
		}

		return checkPortValues();
	}

	/**
	 * validate SUT_TCP_PORT
	 * 
	 * @return true on successful validation
	 */
	public static boolean checkSUTTCPPort() {

		if (CONFIG_INSTANCE.isKeyExists(SUT_TCP_PORT)) {

			String tcpPort = CONFIG_INSTANCE.getValue(SUT_TCP_PORT);

			if (tcpPort.equals("")) {
				TestUtility
						.printMessage(NORMAL,
								"Value of SUT_TCP_PORT not specified in the 'miTester.properties' file");
				LOGGER
						.error("Value of SUT_TCP_PORT not specified in the 'miTester.properties' file");
				return false;

			}

			if (!TestUtility.isAllDigit(tcpPort)) {

				TestUtility
						.printMessage(
								NORMAL,
								"Value of SUT_TCP_PORT must be a digits.. Check the specified value of SUT_TCP_PORT in the 'miTester.properties' file");
				LOGGER
						.error("Specified SUT_TCP_PORT value in the  in the 'miTester.properties' file is not digit");
				return false;
			} else
				return checkPortNumberLimit(tcpPort, SUT_TCP_PORT);

		} else {

			TestUtility.printMessage(NORMAL,
					"No SUT_TCP_PORT exists in the 'miTester.properties' file");
			LOGGER
					.error("No SUT_TCP_PORT exists in the 'miTester.properties' file");
			return false;

		}

	}

	/**
	 * validate MITESTER_DELAY
	 * 
	 * @param miTesterDelay
	 *            is s string value of MITESTER_DELAY
	 * @return true on successful validation
	 */

	public static boolean checkmiTesterDelay(String miTesterDelay) {

		if (miTesterDelay.equals("")) {
			TestUtility
					.printMessage(NORMAL,
							"MITESTER_DELAY value is not specified in the 'miTester.properties' file");
			LOGGER
					.error("MITESTER_DELAY value is not specified in the 'miTester.properties' file");
			return false;

		}

		if (!validatemiTesterDelay(miTesterDelay)) {

			TestUtility
					.printMessage(NORMAL,
							"Check the value of MITESTER_DELAY in the 'miTester.properties' file");
			LOGGER
					.error(" Specified MITESTER_DELAY value in the 'miTester.properties' file is not valid");

			return false;
		} else
			return true;
	}

	/**
	 * validate SUT_WAIT_TIME
	 * 
	 * @param is
	 *            a String value of SUT_WAIT_TIME
	 * @return true on successful validation
	 */
	public static boolean checkSUTWaitTime(String clientWaitTime) {

		if (clientWaitTime.equals("")) {
			TestUtility
					.printMessage(NORMAL,
							"SUT_WAIT_TIME value is not specified in the 'miTester.properties' file");
			LOGGER
					.error("SUT_WAIT_TIME value is not specified in the 'miTester.properties' file");
			return false;

		}

		if (!TestUtility.isAllDigit(clientWaitTime)) {

			TestUtility
					.printMessage(
							NORMAL,
							"SUT_WAIT_TIME value specified in the 'miTester.properties' file is not numeric");
			LOGGER
					.error("SUT_WAIT_TIME value specified in the 'miTester.properties' file is not numeric");
			return false;
		} else
			return true;

	}

	/**
	 * validate MITESTER_WAIT_TIME
	 * 
	 * @param is
	 *            a String value of MITESTER_WAIT_TIME
	 * @return true on successful validation
	 */

	public static boolean checkmiTesterWaitTime(String miTesterWaitTime) {

		if (miTesterWaitTime.equals("")) {
			TestUtility
					.printMessage(NORMAL,
							"MITESTER_WAIT_TIME value not specified in the 'miTester.properties' file");
			LOGGER
					.error("MITESTER_WAIT_TIME value not specified in the 'miTester.properties' file");
			return false;

		}

		if (!TestUtility.isAllDigit(miTesterWaitTime)) {

			TestUtility
					.printMessage(
							NORMAL,
							"Value of MITESTER_WAIT_TIME must be a digits.. Check the specified value of MITESTER_WAIT_TIME in the 'miTester.properties' file");
			LOGGER
					.error("Specified MITESTER_WAIT_TIME value in the 'miTester.properties' file is not digit");
			return false;
		} else
			return true;
	}

	/**
	 * validate SUT_RTP_PORT
	 * 
	 * @param is
	 *            a String value of SUT_RTP_PORT
	 * @return true on successful validation
	 */

	public static boolean checkSUTRTPPort(String clientRTPPort) {

		if (clientRTPPort.equals("")) {
			TestUtility
					.printMessage(NORMAL,
							"SUT_RTP_PORT value is not specified in the 'miTester.properties' file");
			LOGGER
					.error("SUT_RTP_PORT value is not specified in the 'miTester.properties' file");
			return false;

		}

		if (!TestUtility.isAllDigit(clientRTPPort)) {

			TestUtility
					.printMessage(
							NORMAL,
							"SUT_RTP_PORT value specified in the 'miTester.properties' file must be numeric");
			LOGGER
					.error("SUT_RTP_PORT value specified in the 'miTester.properties' file must be numeric");
			return false;
		} else
			return checkPortNumberLimit(clientRTPPort, SUT_RTP_PORT);

	}

	/**
	 * validate MITESTER_RTP_PORT
	 * 
	 * @param is
	 *            a String value of MITESTER_RTP_PORT
	 * @return true on successful validation
	 */

	public static boolean checkmiTesterRTPPort(String miTesterRTPPort) {

		if (miTesterRTPPort.equals("")) {
			TestUtility
					.printMessage(NORMAL,
							"MITESTER_RTP_PORT value is not specified in the 'miTester.properties' file");
			LOGGER
					.error("MITESTER_RTP_PORT value is not specified in the 'miTester.properties' file");
			return false;

		}

		if (!TestUtility.isAllDigit(miTesterRTPPort)) {

			TestUtility
					.printMessage(
							NORMAL,
							"MITESTER_RTP_PORT value specified in the 'miTester.properties' file must be numeric");
			LOGGER
					.error("MITESTER_RTP_PORT value specified in the 'miTester.properties' file must be numeric");
			return false;
		} else
			return checkPortNumberLimit(miTesterRTPPort, MITESTER_RTP_PORT);

	}

	/**
	 * validate MITESTER_SIP_PORT
	 * 
	 * @param is
	 *            a String value of MITESTER_SIP_PORT
	 * @return true on successful validation
	 */

	public static boolean checkmiTesterListenPort(String miTesterListenPort) {

		if (miTesterListenPort.equals("")) {
			TestUtility
					.printMessage(NORMAL,
							"MITESTER_SIP_PORT value is not specified in the 'miTester.properties' file");
			LOGGER
					.error("MITESTER_SIP_PORT value is not specified in the 'miTester.properties' file");
			return false;

		}

		if (!TestUtility.isAllDigit(miTesterListenPort)) {

			TestUtility
					.printMessage(
							NORMAL,
							"MITESTER_SIP_PORT value specified in the 'miTester.properties' file is not numeric");
			LOGGER
					.error("MITESTER_SIP_PORT value specified in the 'miTester.properties' file is not numeric");
			return false;
		} else
			return true;

	}

	/**
	 * validate SUT_SIP_PORT
	 * 
	 * @param is
	 *            a String value of SUT_SIP_PORT
	 * @return true on successful validation
	 */

	public static boolean checkSUTSIPPort(String clientSIPPort) {

		if (clientSIPPort.equals("")) {
			TestUtility
					.printMessage(NORMAL,
							"SUT_SIP_PORT value not specified in the 'miTester.properties' file");
			LOGGER
					.error("SUT_SIP_PORT value not specified in the 'miTester.properties' file");
			return false;

		}

		if (!TestUtility.isAllDigit(clientSIPPort)) {

			TestUtility
					.printMessage(
							NORMAL,
							"Value of SUT_SIP_PORT must be a digits.. Check the specified value of SUT_SIP_PORT in the 'miTester.properties' file");
			LOGGER
					.error("specified SUT_SIP_PORT value in the  in the 'miTester.properties' file is not digit");
			return false;
		} else
			return true;

	}

	/**
	 * validate TEST_INTERVAL
	 * 
	 * @param is
	 *            a String value of TEST_INTERVAL
	 * @return true on successful validation
	 */

	public static boolean checkTestInterval(String testInterval) {

		if (testInterval.equals("")) {
			TestUtility
					.printMessage(NORMAL,
							"TEST_INTERVAL value is not specified in the 'miTester.properties' file");
			LOGGER
					.error("TEST_INTERVAL value is not specified in the 'miTester.properties' file");
			return false;

		}

		if (!TestUtility.isAllDigit(testInterval)) {
			TestUtility
					.printMessage(NORMAL,
							"TEST_INTERVAL value is not specified in the 'miTester.properties' file");
			LOGGER
					.error("TEST_INTERVAL value is not specified in the 'miTester.properties' file");
			return false;
		} else
			return true;

	}

	/**
	 * validate SUT_IP_ADDRESS
	 * 
	 * @return true on successful validation
	 */

	public static boolean checkSUTIPAddress() {

		if (CONFIG_INSTANCE.isKeyExists(SUT_IP_ADDRESS)) {

			String clientIPAddress = CONFIG_INSTANCE.getValue(SUT_IP_ADDRESS);

			if (clientIPAddress.equals("")) {
				TestUtility
						.printMessage(NORMAL,
								"SUT_IP_ADDRESS value is not specified in the 'miTester.properties' file");
				LOGGER
						.error("SUT_IP_ADDRESS value is not specified in the 'miTester.properties' file");
				return false;

			}

			if (TestUtility.getHostAddress(clientIPAddress) == null) {

				TestUtility
						.printMessage(
								NORMAL,
								"Specified SUT_IP_ADDRESS value in the  in the 'miTester.properties' file is not valid");
				LOGGER
						.error("Specified SUT_IP_ADDRESS value in the  in the 'miTester.properties' file is not valid");
				return false;
			} else
				return true;

		} else {

			TestUtility
					.printMessage(NORMAL,
							"No SUT_IP_ADDRESS exists in the 'miTester.properties' file");
			LOGGER
					.error("No SUT_IP_ADDRESS exists in the 'miTester.properties' file");
			return false;

		}
	}

	/**
	 * validate SUT_PATH
	 * 
	 * @return true on successful validation
	 */

	public static boolean checkSUTPath() {

		try {

			if (CONFIG_INSTANCE.isKeyExists(SUT_PATH)) {

				String clientPath = CONFIG_INSTANCE.getValue(SUT_PATH);

				if (clientPath.equals("")) {
					TestUtility
							.printMessage(NORMAL,
									"SUT_PATH value is not specified in the 'miTester.properties' file");
					LOGGER
							.error("SUT_PATH value is not specified in the 'miTester.properties' file");
					return false;

				}

				if (!TestUtility.isFileExist(clientPath)) {

					TestUtility
							.printMessage(NORMAL,
									"SUT_PATH specified in the 'miTester.properties' file does not exist");
					LOGGER
							.error("SUT_PATH specified in the 'miTester.properties' file does not exist");
					return false;
				} else
					return true;

			} else {

				TestUtility.printMessage(NORMAL,
						"No SUT_PATH exists in the 'miTester.properties' file");
				LOGGER
						.error("No SUT_PATH exists in the 'miTester.properties' file");
				return false;

			}

		} catch (IOException ex) {

			TestUtility.printMessage(NORMAL, "error while checking SUT_PATH");
			LOGGER.error("error while checking SUT_PATH");
			return false;
		}
	}

	/**
	 * validate SCRIPT_PATH_SUT
	 * 
	 * @return true on successful validation
	 */

	public static boolean checkSUTScriptPath() {

		try {

			if (CONFIG_INSTANCE.isKeyExists(SCRIPT_PATH_SUT)) {

				String clientScriptPath = CONFIG_INSTANCE
						.getValue(SCRIPT_PATH_SUT);

				if (clientScriptPath.equals("")) {
					TestUtility
							.printMessage(NORMAL,
									"SCRIPT_PATH_SUT value is not specified in the 'miTester.properties' file");
					LOGGER
							.error("SCRIPT_PATH_SUT value is not specified in the 'miTester.properties' file");
					return false;

				}

				if (!TestUtility.isFileExist(clientScriptPath)) {

					TestUtility
							.printMessage(NORMAL,
									"SCRIPT_PATH_SUT specified in the 'miTester.properties' file does not exist");
					LOGGER
							.error("SCRIPT_PATH_SUT specified in the 'miTester.properties' file does not exist");
					return false;

				} else
					return true;

			} else {

				TestUtility
						.printMessage(NORMAL,
								"No SCRIPT_PATH_SUT exists in the 'miTester.properties' file");
				LOGGER
						.error("No SCRIPT_PATH_SUT exists in the 'miTester.properties' file");
				return false;

			}

		} catch (IOException ex) {

			TestUtility.printMessage(NORMAL,
					"error while checking SCRIPT_PATH_SUT");
			LOGGER.error("error while checking SCRIPT_PATH_SUT");
			return false;
		}
	}

	/**
	 * validate SCRIPT_PATH_MITESTER
	 * 
	 * @return true on successful validation
	 */

	public static boolean checkmiTesterScriptPath() {

		try {

			if (CONFIG_INSTANCE.isKeyExists(SCRIPT_PATH_MITESTER)) {

				String miTesterScriptPath = CONFIG_INSTANCE
						.getValue(SCRIPT_PATH_MITESTER);

				if (miTesterScriptPath.equals("")) {
					TestUtility
							.printMessage(NORMAL,
									"SCRIPT_PATH_MITESTER value is not specified in the 'miTester.properties' file");
					LOGGER
							.error("SCRIPT_PATH_MITESTER value is not specified in the 'miTester.properties' file");
					return false;

				}

				if (!TestUtility.isFileExist(miTesterScriptPath)) {

					TestUtility
							.printMessage(
									NORMAL,
									"SCRIPT_PATH_MITESTER specified in the 'miTester.properties' file does not exist");
					LOGGER
							.error("SCRIPT_PATH_MITESTER specified in the 'miTester.properties' file does not exist");
					return false;
				} else
					return true;

			} else {

				TestUtility
						.printMessage(NORMAL,
								"No SCRIPT_PATH_MITESTER exists in the 'miTester.properties' file");
				LOGGER
						.error("No SCRIPT_PATH_MITESTER exists in the 'miTester.properties' file");
				return false;

			}

		} catch (IOException ex) {

			TestUtility.printMessage(NORMAL,
					"error while checking SCRIPT_PATH_MITESTER");
			LOGGER.error("error while checking SCRIPT_PATH_MITESTER");
			return false;
		}
	}

	/**
	 * validate AUTHENTICATION
	 * 
	 * @param authentication
	 *            is a string value of AUTHENTICATION
	 * @return true on successful validation
	 */

	public static boolean validateAuthentication(String authentication) {

		if (authentication.equals("")) {
			TestUtility
					.printMessage(NORMAL,
							"AUTHENTICATION value is not specified in the 'miTester.properties' file");
			LOGGER
					.error("AUTHENTICATION value is not specified in the 'miTester.properties' file");
			return false;

		}

		if (!authentication.equalsIgnoreCase("YES") && !authentication.equalsIgnoreCase("NO")) {

			TestUtility
					.printMessage(NORMAL,
							"AUTHENTICATION value specified in the 'miTester.properties' file is not valid");
			LOGGER
					.error("AUTHENTICATION value specified in the 'miTester.properties' file is not valid");
			return false;
		} else
			return true;

	}

	/**
	 * it validates the mitester delay
	 * 
	 * @param miTesterDelay
	 *            is String input going to be validated
	 * @return true when validating the mitester delay successfully
	 */

	public static boolean validatemiTesterDelay(String miTesterDelay) {

		LOGGER.info("entered into validateMiTesterDelay");

		try {
			// if parses the string successfully it return true
			Float.parseFloat(CONFIG_INSTANCE.getValue(MITESTER_DELAY));

			return true;

		} catch (Exception ex) {

			return false;
		}

	}

	/**
	 * validate the port limit
	 * 
	 * @return true on successful validation
	 */

	public static boolean checkPortNumberLimit(String portNumber,
			String portName) {

		try {

			BigDecimal decimal = new BigDecimal(portNumber);

			int value = decimal.intValue();

			if ((value < 1000) || (value > 60000)) {

				TestUtility
						.printMessage(
								NORMAL,
								" Value of "
										+ portName
										+ " must be set between 1000 - 60000.. Check the value specified in the 'miTester.properties' file");

				LOGGER.error(portName
						+ " value is not set between 1000 - 60000");

				return false;

			} else
				return true;

		} catch (Exception ex) {

			LOGGER.error("error while validating port number", ex);
			return false;
		}

	}

	/**
	 * validate VALIDATION
	 * 
	 * @param value
	 *            is a string value of VALIDATION
	 * @return true on successful validation
	 */

	public static boolean checkHeaderPresence(String value) {

		if (value.equals("")) {
			TestUtility
					.printMessage(
							NORMAL,
							"CHECK_PRESENCE_OF_HEADER value is not specified in the 'miTester.properties' file");
			LOGGER
					.error("CHECK_PRESENCE_OF_HEADER value is not specified in the 'miTester.properties' file");
			return false;

		}

		if (!value.equalsIgnoreCase("YES") && !value.equalsIgnoreCase("NO")) {

			TestUtility
					.printMessage(
							NORMAL,
							"CHECK_PRESENCE_OF_HEADER value specified in the 'miTester.properties' file is not valid");
			LOGGER
					.error("CHECK_PRESENCE_OF_HEADER value specified in the 'miTester.properties' file is not valid");
			return false;
		} else {

		}
		return checkValidationFilePath();

	}

	/**
	 * validate CHECK_PRESENCE_OF_HEADER
	 * 
	 * @param value
	 *            is a string value of CHECK_PRESENCE_OF_HEADER
	 * @return true on successful validation
	 */

	public static boolean checkValidation(String value) {

		if (value.equals("")) {
			TestUtility
					.printMessage(NORMAL,
							"VALIDATION value is not specified in the 'miTester.properties' file");
			LOGGER
					.error("VALIDATION value is not specified in the 'miTester.properties' file");
			return false;

		}

		if (!value.equalsIgnoreCase("YES") && !value.equalsIgnoreCase("NO")) {

			TestUtility
					.printMessage(
							NORMAL,
							"VALIDATION value specified in the 'miTester.properties' file is not valid");
			LOGGER
					.error("VALIDATION value specified in the 'miTester.properties' file is not valid");
			return false;
		} else {

		}
		return checkValidationFilePath();

	}

	/**
	 * check VALIDATION_PATH
	 * 
	 * @return true on successful validation
	 */
	public static boolean checkValidationFilePath() {

		try {

			if (CONFIG_INSTANCE.isKeyExists(VALIDATION_FILE_PATH)) {

				String validationFilePath = CONFIG_INSTANCE
						.getValue(VALIDATION_FILE_PATH);

				if (validationFilePath.equals("")) {
					TestUtility
							.printMessage(NORMAL,
									"VALIDATION_FILE_PATH value is not specified in the 'miTester.properties' file");
					LOGGER
							.error("VALIDATION_FILE_PATH value is not specified in the 'miTester.properties' file");
					return false;

				}

				if (!TestUtility.isFileExist(validationFilePath)) {

					TestUtility
							.printMessage(
									NORMAL,
									"VALIDATION_FILE_PATH Specified in the 'miTester.properties' file does not exist");
					LOGGER
							.error("VALIDATION_FILE_PATH Specified in the 'miTester.properties' file does not exist");
					return false;

				} else
					return checkValidationProperties();

			} else {

				TestUtility
						.printMessage(NORMAL,
								"No VALIDATION_FILE_PATH exists in the 'miTester.properties' file");
				LOGGER
						.error("No VALIDATION_FILE_PATH exists in the 'miTester.properties' file");
				return false;

			}

		} catch (IOException ex) {

			TestUtility.printMessage(NORMAL,
					"error while checking VALIDATION_FILE_PATH");
			LOGGER.error("error while checking VALIDATION_FILE_PATH");
			return false;
		}

	}

	/**
	 * check VALIDATION properties
	 * 
	 * @return true on successful validation
	 */
	public static boolean checkValidationProperties() {

		try {
			if ((CONFIG_INSTANCE.isKeyExists(VALIDATION))
					&& (CONFIG_INSTANCE.getValue(VALIDATION).equals("YES"))) {
				if (TestUtility.isFileExist("lib/validateHeader.properties"))
					return true;
				else {
					TestUtility
							.printMessage(NORMAL,
									"'validateHeader.properties' file doesn't exist in the 'lib' folder");
					LOGGER
							.error("'validateHeader.properties' file doesn't exist in the 'lib' folder");
					return false;

				}
			} else {
				return true;
			}

		} catch (IOException ex) {

			TestUtility.printMessage(NORMAL,
					"error while checking VALIDATION properties");
			LOGGER.error("error while checking VALIDATION properties");
			return false;
		}

	}

	/**
	 * validate the port values
	 * 
	 * @return true on successful validation
	 */

	public static boolean checkPortValues() {

		if (CONFIG_INSTANCE.getValue(MITESTER_MODE).equals("ADVANCED")) {

			String tcpPort = CONFIG_INSTANCE.getValue(SUT_TCP_PORT);
			String miTesterSIPPort = null;

			if (CONFIG_INSTANCE.isKeyExists(MITESTER_SIP_PORT)) {
				miTesterSIPPort = CONFIG_INSTANCE.getValue(MITESTER_SIP_PORT);
				if (tcpPort.equals(miTesterSIPPort)) {
					TestUtility.printMessage(NORMAL,
							"SUT_TCP_PORT and MITESTER_SIP_PORT are same..");
					LOGGER.error("SUT_TCP_PORT and MITESTER_SIP_PORT are same");
					return false;
				}

			} else if (CONFIG_INSTANCE.isKeyExists(MITESTER_RTP_PORT)) {

				String miTesterRTPPort = CONFIG_INSTANCE
						.getValue(MITESTER_RTP_PORT);

				String clientRTPPort = CONFIG_INSTANCE.getValue(SUT_RTP_PORT);

				if (tcpPort.equals(miTesterRTPPort)) {
					TestUtility.printMessage(NORMAL,
							"SUT_TCP_PORT and MITESTER_RTP_PORT are same..");
					LOGGER.error("SUT_TCP_PORT and MITESTER_RTP_PORT are same");
					return false;

				} else if (tcpPort.equals(clientRTPPort)) {
					TestUtility.printMessage(NORMAL,
							"SUT_TCP_PORT and SUT_RTP_PORT are same..");
					LOGGER.error("SUT_TCP_PORT and SUT_RTP_PORT are same");
					return false;
				} else if (miTesterRTPPort.equals(clientRTPPort)) {
					TestUtility.printMessage(NORMAL,
							"MITESTER_RTP_PORT and SUT_RTP_PORT are same..");
					LOGGER.error("MITESTER_RTP_PORT and SUT_RTP_PORT are same");
					return false;
				} else if (CONFIG_INSTANCE.isKeyExists(MITESTER_SIP_PORT)) {

					if (miTesterRTPPort.equals(miTesterSIPPort)) {
						TestUtility
								.printMessage(NORMAL,
										"MITESTER_RTP_PORT and MITESTER_SIP_PORT are same..");
						LOGGER
								.error("MITESTER_RTP_PORT and MITESTER_SIP_PORT are same");
						return false;

					} else if (clientRTPPort.equals(CONFIG_INSTANCE
							.getValue(miTesterSIPPort))) {
						TestUtility
								.printMessage(NORMAL,
										"SUT_RTP_PORT and MITESTER_SIP_PORT are same..");
						LOGGER
								.error("SUT_RTP_PORT and MITESTER_SIP_PORT are same");
						return false;
					}

				}
			}

		} else {

			if (CONFIG_INSTANCE.isKeyExists(MITESTER_RTP_PORT)) {

				String miTesterRTPPort = CONFIG_INSTANCE
						.getValue(MITESTER_RTP_PORT);

				String clientRTPPort = CONFIG_INSTANCE.getValue(SUT_RTP_PORT);

				if (miTesterRTPPort.equals(clientRTPPort)) {
					TestUtility
							.printMessage(
									NORMAL,
									"Warning: MITESTER_RTP_PORT and SUT_RTP_PORT are same.. It should be applicable only when miTester and SUT are run in the different machine otherwise STOP the execution of miTester");
					LOGGER.warn("MITESTER_RTP_PORT and SUT_RTP_PORT are same");
				} else if (CONFIG_INSTANCE.isKeyExists(MITESTER_SIP_PORT)) {

					String miTesterSIPPort = CONFIG_INSTANCE
							.getValue(MITESTER_SIP_PORT);

					if (miTesterRTPPort.equals(miTesterSIPPort)) {
						TestUtility
								.printMessage(NORMAL,
										"MITESTER_RTP_PORT and MITESTER_SIP_PORT are same..");
						LOGGER
								.error("MITESTER_RTP_PORT and MITESTER_SIP_PORT are same");
						return false;

					} else if (clientRTPPort.equals(CONFIG_INSTANCE
							.getValue(miTesterSIPPort))) {
						TestUtility
								.printMessage(
										NORMAL,
										"SUT_RTP_PORT and MITESTER_SIP_PORT are same.. It is applicable only if miTester and SUT are run in the different machine in USER mode else STOP the execution of miTester");
						LOGGER
								.warn("SUT_RTP_PORT and MITESTER_SIP_PORT are same");
					}

				}

			}

		}

		return true;
	}
}
