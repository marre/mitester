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

import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;

/**
 * This class validates the miTester properties 
 * 
 */

public class ConfigurationPropertiesValidator 
{

	private static final String TCP_SERVER_PORT = "TCP_SERVER_PORT";
	private static final String TEST_MODE = "TEST_MODE";
	private static final String CLIENT_IP_ADDRESS = "CLIENT_IP_ADDRESS";
	private static final String TEST_APPLICATION_PATH = "TEST_APPLICATION_PATH";
	private static final String SERVER_SCRIPT_PATH = "SERVER_SCRIPT_PATH";
	private static final String CLIENT_SCRIPT_PATH = "CLIENT_SCRIPT_PATH";

	public static boolean checkPropertyFiles() {
		if (CONFIG_INSTANCE.isKeyExists(TEST_MODE)) {
			if (CONFIG_INSTANCE.getValue(TEST_MODE).equalsIgnoreCase(
					"ADVANCED")) {
				if (CONFIG_INSTANCE.isKeyExists(TCP_SERVER_PORT)) {
					if (CONFIG_INSTANCE.isKeyExists(CLIENT_IP_ADDRESS)) {
						if (CONFIG_INSTANCE
								.isKeyExists(TEST_APPLICATION_PATH)) {
							if (CONFIG_INSTANCE
									.isKeyExists(SERVER_SCRIPT_PATH)) {
								if (CONFIG_INSTANCE
										.isKeyExists(CLIENT_SCRIPT_PATH)) {
									return true;
								} else {
									TestUtility
											.printMessage("No CLIENT_SCRIPT_PATH exists in the config file");
									return false;
								}
							} else {
								TestUtility
										.printMessage("No SERVER_SCRIPT_PATH exists in the config file");
								return false;
							}
						} else {
							TestUtility
									.printMessage("No TEST_APPLICATION_PATH exists in the config file");
							return false;
						}
					} else {
						TestUtility
								.printMessage("No CLIENT_IP_ADDRESS exists in the config file");
						return false;
					}
				} else {
					TestUtility
							.printMessage("No TCP_SERVER_PORT exists in the config file");
					return false;
				}
			} else if (CONFIG_INSTANCE.getValue(TEST_MODE)
					.equalsIgnoreCase("USER")) {
				if (CONFIG_INSTANCE.isKeyExists(CLIENT_IP_ADDRESS)) {
					if (CONFIG_INSTANCE.isKeyExists(SERVER_SCRIPT_PATH)) {
						return true;
					} else {
						TestUtility
								.printMessage("No SERVER_SCRIPT_PATH exists in the config file");
						return false;
					}
				} else {
					TestUtility
							.printMessage("No CLIENT_IP_ADDRESS exists in the config file");
					return false;
				}
			} else {
				TestUtility
						.printMessage("Error at choosing TEST_MODE");
				return false;
			}
		} else {
			TestUtility.printMessage("No TEST_MODE exists in the config file");
			return false;
		}
	}
}
