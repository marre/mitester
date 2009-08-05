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
package com.mitester.main;

import static com.mitester.executor.ExecutorConstants.MITESTER_MODE;
import static com.mitester.sipserver.SipServerConstants.CHECK_PRESENCE_OF_HEADER;
import static com.mitester.sipserver.SipServerConstants.SERVER_MODE;
import static com.mitester.sipserver.SipServerConstants.VALIDATION;
import static com.mitester.sipserver.SipServerConstants.VALIDATION_FILE_PATH;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;
import static com.mitester.utility.UtilityConstants.HELP_MODE;
import static com.mitester.utility.UtilityConstants.NORMAL;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.mitester.adapter.Adapter;
import com.mitester.adapter.SipAdapter;
import com.mitester.executor.TestExecutor;
import com.mitester.jaxbparser.client.ParseClientScript;
import com.mitester.jaxbparser.server.ParseServerScript;
import com.mitester.jaxbparser.validation.ParseValidationScript;
import com.mitester.jaxbparser.validation.VALIDATION;
import com.mitester.sipserver.ProcessSIPMessage;
import com.mitester.sipserver.features.StartServer;
import com.mitester.sipserver.headervalidation.PropertyKeyValidation;
import com.mitester.utility.ConfigurationPropertiesValidator;
import com.mitester.utility.ConsoleReader;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestMode;
import com.mitester.utility.TestResult;
import com.mitester.utility.TestUtility;
import com.mitester.utility.ThreadControl;

/**
 * This is the Main class it starts the miTester after successful parsing of the
 * test scripts.
 * 
 */

public class Main {
	

	private static final Logger LOGGER = MiTesterLog.getLogger(Main.class
			.getName());

	public static void main(String args[]) throws Exception {

		List<com.mitester.jaxbparser.client.TEST> clientScenarios = null;

		List<com.mitester.jaxbparser.server.TEST> serverScenarios = null;

		String startTime = null;

		String endTime = null;

		if ((args.length >= 1)
				&& (args[0].equalsIgnoreCase("--help")
						|| args[0].equalsIgnoreCase("-h") || args[0]
						.equalsIgnoreCase("h"))) {
			
			TestUtility.printMessage(NORMAL, "");
			TestUtility.setModeOfDisplay(HELP_MODE);
			ConsoleReader.printHelpInfo();
			stopMiTester();
		}

		LOGGER.info("miTester [STARTED]");

		if (CONFIG_INSTANCE.isPropertyFileLoaded()) {

			if (CONFIG_INSTANCE.isKeyExists(SERVER_MODE)) {

				if (CONFIG_INSTANCE.getValue(SERVER_MODE).equalsIgnoreCase("B2BUA")
						|| CONFIG_INSTANCE.getValue(SERVER_MODE)
								.equalsIgnoreCase("PROXY")) {

					if (CONFIG_INSTANCE.isKeyExists("AUTHENTICATION")) {
						if (ConfigurationPropertiesValidator
								.validateAuthentication(CONFIG_INSTANCE
										.getValue("AUTHENTICATION"))) {

							// start server
							new StartServer().startServer();
						}

					} else {

						// start server
						new StartServer().startServer();

					}

				} else {

					TestUtility
							.printMessage(NORMAL,
									"SERVER_MODE specified in the 'miTester.properties' file is not valid");
					LOGGER
							.error("SERVER_MODE specified in the 'miTester.properties' file is not valid");

				}

			}

			else if (ConfigurationPropertiesValidator.checkPropertyFile()) {

				if ((CONFIG_INSTANCE.isKeyExists(VALIDATION) && CONFIG_INSTANCE
						.getValue(VALIDATION).equals("YES"))
						|| (CONFIG_INSTANCE
								.isKeyExists(CHECK_PRESENCE_OF_HEADER) && CONFIG_INSTANCE
								.getValue(CHECK_PRESENCE_OF_HEADER).equals(
										"YES"))) {

					VALIDATION validation = ParseValidationScript
							.parseValidationScript(CONFIG_INSTANCE
									.getValue(VALIDATION_FILE_PATH));

					if ((CONFIG_INSTANCE.isKeyExists(VALIDATION))
							&& (CONFIG_INSTANCE.getValue(VALIDATION)
									.equals("YES"))) {
						if (!PropertyKeyValidation.keyValidation(validation))
							stopMiTester();
					}

					if (validation == null)
						stopMiTester();
					else
						ProcessSIPMessage.setValidation(validation);
				}
				
				ThreadControl threadControl = new ThreadControl();

				Adapter sipAdapter = new SipAdapter(threadControl);

				TestExecutor testExecutor = new TestExecutor(sipAdapter,threadControl);

				// Get the start time of test execution
				startTime = TestUtility.getTime();

				if (TestMode.getTestModefromString(
						CONFIG_INSTANCE.getValue(MITESTER_MODE)).equals(
						TestMode.ADVANCED)) {

					serverScenarios = parseServerTestScenarios();
					clientScenarios = parseClientTestScenarios();

					if ((serverScenarios != null) && (clientScenarios != null)) {
						if (TestUtility.checkTestsAvailable(clientScenarios,
								serverScenarios)) {

							// Execute the tests
							testExecutor.executeTest(clientScenarios,
									serverScenarios);

							// Get the end time of test execution
							endTime = TestUtility.getTime();

							// display test result
							TestResult.printResult(startTime, endTime);

						}
					}

				} else if (TestMode.getTestModefromString(
						CONFIG_INSTANCE.getValue(MITESTER_MODE)).equals(
						TestMode.USER)) {

					serverScenarios = parseServerTestScenarios();

					if ((serverScenarios != null)) {

						// Execute the tests
						testExecutor.executeTest(clientScenarios,
								serverScenarios);

						// Get the end time of test execution
						endTime = TestUtility.getTime();

						// display test result
						TestResult.printResult(startTime, endTime);
					}
				}
			}

			// stop miTester
			stopMiTester();

		} else {

			// stop miTester
			stopMiTester();

		}
	}

	/**
	 * This method used to parse the client test scripts
	 * 
	 * @return com.mitester.jaxbparser.client.TEST object which consists of list
	 *         of client tests
	 */

	private static List<com.mitester.jaxbparser.client.TEST> parseClientTestScenarios() {

		try {
			List<com.mitester.jaxbparser.client.TEST> clientScenarios = null;

			ParseClientScript clientScript = new ParseClientScript();
			String clientScriptPath = CONFIG_INSTANCE
					.getValue("SCRIPT_PATH_SUT");

			clientScenarios = clientScript.FileParsingClient(clientScriptPath);

			if (clientScenarios.size() == 0) {
				TestUtility
						.printMessage(NORMAL,
								"No client scripts exist in the specified client script path");
				LOGGER
						.error("No client scripts exist in the specified client script path");
				return null;
			} else {
				return clientScenarios;
			}

		} catch (FileNotFoundException ex) {
			TestUtility.printError(
					"specified client Script path doesn't exist", ex);
			return null;
		} catch (IOException ex) {
			TestUtility.printError("Error while parsing Client Test scripts",
					ex);
			return null;
		} catch (NullPointerException ex) {
			TestUtility.printError("Error while parsing Client Test scripts",
					ex);
			return null;
		} catch (JAXBException ex) {
			TestUtility.printError("Error while parsing Client Test scripts",
					ex);
			return null;
		} catch (Exception ex) {
			TestUtility.printError("Error while parsing Client Test scripts",
					ex);
			return null;
		}

	}

	/**
	 * This method used to parse the server test scripts
	 * 
	 * @return com.mitester.jaxbparser.server.TEST object which consists of list
	 *         of server tests
	 */

	private static List<com.mitester.jaxbparser.server.TEST> parseServerTestScenarios() {

		try {
			List<com.mitester.jaxbparser.server.TEST> serverScenarios;
			ParseServerScript serverScript = new ParseServerScript();
			String serverScriptPath = CONFIG_INSTANCE
					.getValue("SCRIPT_PATH_MITESTER");

			serverScenarios = serverScript.FileParsingServer(serverScriptPath);

			if (serverScenarios.size() == 0) {
				TestUtility
						.printMessage(NORMAL,
								"No client scripts exist in the specified server script path");
				LOGGER
						.error("No client scripts exist in the specified server script path");
				return null;
			} else {
				return serverScenarios;
			}

		} catch (FileNotFoundException ex) {
			TestUtility.printError(
					"specified server Script path doesn't exist", ex);
			return null;
		} catch (IOException ex) {
			TestUtility.printError("Error while parsing Server Test scripts",
					ex);
			return null;
		} catch (NullPointerException ex) {
			TestUtility.printError("Error while parsing Server Test scripts",
					ex);
			return null;
		} catch (JAXBException ex) {
			TestUtility.printError("Error while parsing Server Test scripts",
					ex);
			return null;
		} catch (com.mitester.jaxbparser.server.ParserException ex) {
			LOGGER.error(ex.getMessage());
			TestUtility.printMessage(NORMAL, ex.getMessage());
			return null;
		} catch (Exception ex) {
			TestUtility.printError("Error while parsing Server Test scripts",
					ex);
			return null;
		}
	}

	private static void stopMiTester() {

		// close the input, output and error streams
		TestUtility.close();

		LOGGER.info("miTester [STOPPED]");

		System.exit(0);

	}

}
