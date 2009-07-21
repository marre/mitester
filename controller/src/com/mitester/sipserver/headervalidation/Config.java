/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: UdpCommn.java
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
 * Jain SIP stack 				NIST-CONDITIONS-OF-USE 						        https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 						The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * JNetStreamStandalone lib     GNU Library or LGPL			     					http://sourceforge.net/projects/jnetstream/
 * 
 */

/*
 * miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications.
 *  
 */
package com.mitester.sipserver.headervalidation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.mitester.utility.TestUtility;

public enum Config {

	CONFIG;

	private Properties configFile = new Properties();

	private boolean isLoaded = false;

	private Config() {
		try {
			loadConfigFile();
			isLoaded = true;
		} catch (IOException ex) {
			TestUtility.printMessage("NORMAL",ex.getMessage());
			TestUtility.printError("error in reading properties file", ex);
		} catch (Exception ex) {
			TestUtility.printMessage("NORMAL",ex.getMessage());
			TestUtility.printError("error in reading properties file", ex);

		}
	}

	/**
	 * return the miTester property value
	 * 
	 * @param key
	 *            specifies the name of the property
	 * @return the property value
	 */

	public String getValue(String key) {
		return configFile.getProperty(key);
	}

	/**
	 * This method is used to set the property
	 * 
	 * @param key
	 *            specifies the name of the property
	 * @param value
	 *            specifies the value of the property
	 */
	public void setProperty(String key, String value) {
		configFile.setProperty(key, value);
	}

	/**
	 * This method is used to load the miTester properties
	 * 
	 * @throws IOException
	 */

	private void loadConfigFile() throws IOException {
		configFile.load(new FileInputStream("lib/validateHeader.properties"));
	}

	/**
	 * this method is used to check existence of key
	 * 
	 * @param key
	 *            specifies the name of the property
	 * @return true if key exists
	 */

	public boolean isKeyExists(String key) {
		try {
			return configFile.containsKey(key);
		} catch (Exception ex) {
			TestUtility.printError("error in reading properties file", ex);
			return false;
		}
	}

	public boolean isPropertyFileLoaded() {
        return isLoaded;
	}
}
