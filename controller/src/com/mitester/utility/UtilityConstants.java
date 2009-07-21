/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: TestExecutor.java
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
package com.mitester.utility;

import java.text.DecimalFormat;

/**
 * 
 * Utility constants
 * 
 */
public class UtilityConstants {

	/**
	 * To represents the call flow mode of display
	 */
	public static final String CALL_FLOW_MODE = "C";

	/**
	 * To represents the log mode of display
	 */
	public static final String LOG_MODE = "L";

	/**
	 * To represents the normal mode of display
	 */
	public static final String NORMAL_MODE = "N";

	/**
	 * To represents the previous mode of display
	 */

	public static final String PREVIOUS_MODE = "Q";
	
	/**
	 * To represents the help mode of display
	 */

	public static final String HELP_MODE = "H";
	
	
	/**
	 * To represents the pause mode of display
	 */

	public static final String PAUSE_MODE = "P";
	
	/**
	 * To represents the stop mode
	 */
	public static final String STOP_MODE = "T";
	

	/**
	 * To represents the Result mode of display
	 */
	public static final String RESULT_MODE = "R";

	/**
	 * To represents the Normal type
	 */
	public static final String NORMAL = "NORMAL";

	/**
	 * To represents the call flow type
	 */
	public static final String CALL_FLOW = "CALL_FLOW";

	/**
	 * To represents the result type
	 */
	public static final String RESULT = "RESULT";
	
	/**
	 * To represents the help type
	 */
	public static final String HELP = "HELP";

	/**
	 * To represents the operating system
	 */
	public static final String OS_NAME = System.getProperty("os.name");

	/**
	 * To represents the line separator
	 */
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * To represents the file separator
	 */
	public static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	/**
	 * To represents the semi colon separator
	 */
	public static final String SEMI_COLON_SEPARATOR = ";";

	/**
	 * To represents the Windows
	 */
	public static final String WINDOWS_OS = "Windows";

	/**
	 * To represents the Linux
	 */

	public static final String LINUX_OS = "Linux";

	/**
	 * To represents the Solaris
	 */

	public static final String SOLARIS_OS = "Solaris";

	/**
	 * To represents the SunOS
	 */
	public static final String SUN_OS = "SunOS";

	/**
	 * To represents the Mac
	 */
	public static final String MAC_OS = "Mac";
	
	/**
	 * To represents the two digit decimal format
	 */

	public static final DecimalFormat TWO_DIGIT_FORMAT = new DecimalFormat("00");

	/**
	 * To represents the three digit decimal format
	 */
	public static final DecimalFormat THREE_DIGIT_FORMAT = new DecimalFormat(
			"000");


}
