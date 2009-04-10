/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: Main.java
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
package com.mitester.main;

import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import com.mitester.adapter.Adapter;
import com.mitester.adapter.SipAdapter;
import com.mitester.executor.TestExecutor;
import com.mitester.jaxbparser.client.ParseClientScript;
import com.mitester.jaxbparser.server.ParseServerScript;
import com.mitester.sipserver.SIPHeaderValidator;
import com.mitester.utility.ConfigurationPropertiesValidator;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestMode;
import com.mitester.utility.TestResult;
import com.mitester.utility.TestUtility;

/**
 * This is the Main class it starts the miTester after successful parsing of the
 * test scripts.
 * 
 */
public class Main {

	private static final Logger LOGGER = MiTesterLog.getLogger(Main.class
			.getName());

	private static final String TEST_MODE = "TEST_MODE";

	private static final String SERVER_SCRIPT_PATH = "SERVER_SCRIPT_PATH";

	private static final String CLIENT_SCRIPT_PATH = "CLIENT_SCRIPT_PATH";

	public static void main(String args[]) throws Exception {

		List<com.mitester.jaxbparser.client.TEST> clientScenarios = null;

		List<com.mitester.jaxbparser.server.TEST> serverScenarios = null;

		String startTime = null;

		String endTime = null;

		String setLineSeparator = "\r\n";

		LOGGER.info("miTester [STARTED]");

		if (ConfigurationPropertiesValidator.checkPropertyFiles()) {

			SIPHeaderValidator sipHeaderValidator = new SIPHeaderValidator();

			/* load server configuration properties */
			sipHeaderValidator.loadServerConfigProperties();

			Adapter sipAdapter = new SipAdapter();

			TestExecutor testExecutor = new TestExecutor(sipAdapter,
					sipHeaderValidator);

			String getLineSeperator = System.getProperty("line.separator");

			if (System.getProperty("os.name").startsWith("Linux")) {

				/* for checking CR, LF cases */
				System.setProperty("line.separator", setLineSeparator);
			}

			/* Get the start time of test execution */
			startTime = TestUtility.getTime();

			if (TestMode.getTestModefromString(
					CONFIG_INSTANCE.getValue(TEST_MODE)).equals(
					TestMode.ADVANCED)) {

				serverScenarios = parseServerTestScenarios();
				clientScenarios = parseClientTestScenarios();

				if ((serverScenarios != null) && (clientScenarios != null)) {
					if (TestUtility.checkTestsAvailablity(clientScenarios,
							serverScenarios)) {

						/* Execute the tests */
						testExecutor.executeTest(clientScenarios,
								serverScenarios);

						/* Get the end time of test execution */
						endTime = TestUtility.getTime();

						/* display test result */
						TestResult.printResult(startTime, endTime);

					}
				}

			} else if (TestMode.getTestModefromString(
					CONFIG_INSTANCE.getValue(TEST_MODE)).equals(TestMode.USER)) {

				serverScenarios = parseServerTestScenarios();

				if ((serverScenarios != null)) {

					/* Execute the tests */
					testExecutor.executeTest(clientScenarios, serverScenarios);

					/* Get the end time of test execution */
					endTime = TestUtility.getTime();

					/* display test result */
					TestResult.printResult(startTime, endTime);

				}

			}

			/* reset the same as it was set earlier */
			if (System.getProperty("os.name").startsWith("Linux")) {

				System.setProperty("line.separator", getLineSeperator);
			}

		}

		/* close the input, output and error streams */
		TestUtility.close();

		LOGGER.info("miTester [STOPPED]");

		System.exit(0);
	}

	/**
	 * This method used to parse the client test scripts
	 * 
	 * @return com.mitester.jaxbparser.client.TEST object which consists of
	 *         number of client tests and its actions
	 */

	private static List<com.mitester.jaxbparser.client.TEST> parseClientTestScenarios() {
		try {
			List<com.mitester.jaxbparser.client.TEST> clientScenarios = null;

			ParseClientScript clientScript = new ParseClientScript();
			String clientScriptPath = CONFIG_INSTANCE
					.getValue(CLIENT_SCRIPT_PATH);

			clientScenarios = clientScript.FileParsingClient(clientScriptPath);

			if (clientScenarios.size() == 0) {
				TestUtility
						.printMessage("There are no client scripts exist in the specified client script path");
				return null;
			} else {
				return clientScenarios;
			}

		} catch (FileNotFoundException ex) {
			TestUtility.printError(
					"Specified client Script path doesn't exist", ex);
			return null;
		} catch (IOException ex) {
			TestUtility.printError("Error at parsing Client Test scripts", ex);
			return null;
		} catch (NullPointerException ex) {
			TestUtility.printError("Error at parsing Client Test scripts", ex);
			return null;
		} catch (JAXBException ex) {
			TestUtility.printError("Error at parsing Client Test scripts", ex);
			return null;
		} catch (Exception ex) {
			TestUtility.printError("Error at parsing Client Test scripts", ex);
			return null;
		}

	}

	/**
	 * This method used to parse the server test scripts
	 * 
	 * @return com.mitester.jaxbparser.server.TEST object which consists of
	 *         number of server tests and its actions
	 */

	private static List<com.mitester.jaxbparser.server.TEST> parseServerTestScenarios() {
		try {
			List<com.mitester.jaxbparser.server.TEST> serverScenarios;
			ParseServerScript serverScript = new ParseServerScript();
			String serverScriptPath = CONFIG_INSTANCE
					.getValue(SERVER_SCRIPT_PATH);

			serverScenarios = serverScript.FileParsingServer(serverScriptPath);

			if (serverScenarios.size() == 0) {
				TestUtility
						.printMessage("There are no server scripts exist in the specified server script path");
				return null;
			} else {
				return serverScenarios;
			}

		} catch (FileNotFoundException ex) {
			TestUtility.printError(
					"Specified server Script path doesn't exist", ex);
			return null;
		} catch (IOException ex) {
			TestUtility.printError("Error at parsing Server Test scripts", ex);
			return null;
		} catch (NullPointerException ex) {
			TestUtility.printError("Error at parsing Server Test scripts", ex);
			return null;
		} catch (JAXBException ex) {
			TestUtility.printError("Error at parsing Server Test scripts", ex);
			return null;
		} catch (Exception ex) {
			TestUtility.printError("Error at parsing Server Test scripts", ex);
			return null;
		}
	}
}

