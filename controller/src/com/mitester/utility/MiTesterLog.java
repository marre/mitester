/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: MiTesterLog.java
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

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * This class used to create logger and load the logging properties for the specified class
 * 
 */

public class MiTesterLog {
	
	static {

		DOMConfigurator.configure("lib/log4j.xml");	
	}
	
	/**
	 * it creates and returns the logger
	 * 
	 * @param name  of the class
	 * @return Logger
	 */
	public static Logger getLogger(String name) {
		
		Logger logger = Logger.getLogger(name);
		return logger;
	}

}
