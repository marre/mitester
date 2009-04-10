/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ConfigurationProperties.java
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

import java.io.*;
import java.util.Properties;


public enum ConfigurationProperties {

	CONFIG_INSTANCE;

	private Properties configFile = new Properties();

	private ConfigurationProperties() {
		try {
			loadConfigFile();
		} catch (IOException ex) {
			TestUtility.printError("error in reading properties file", ex);
		}
	}

	public String getValue(String key) {
		return configFile.getProperty(key);
	}

	private void loadConfigFile() throws IOException {
		configFile.load(new FileInputStream("miTester.properties"));
	}

	public boolean isKeyExists(String key) {
		try {
			return configFile.containsKey(key);
		} catch (Exception ex) {
			TestUtility.printError("error in reading properties file", ex);
			return false;
		}
	}
}
