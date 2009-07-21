/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ExecutorConstants.java
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
package com.mitester.executor;

/**
 * Executor constants are being used in the test script execution
 * 
 */

public class ExecutorConstants {

	/**
	 * MITESTER_MODE
	 */
	public static final String MITESTER_MODE = "MITESTER_MODE";

	/**
	 * ADVANCED MODE
	 */
	public static final String ADVANCED_MODE = "ADVANCED";

	/**
	 * USER MODE
	 */
	public static final String USER_MODE = "USER";

	/**
	 * WAIT
	 */
	public static final String WAIT_TIME = "WAIT";

	/**
	 * SUT_WAIT_TIME
	 */
	public static final String SUT_WAIT_TIME = "SUT_WAIT_TIME";

	/**
	 * MITESTER_WAIT_TIME
	 */
	public static final String MITESTER_WAIT_TIME = "MITESTER_WAIT_TIME";

	/**
	 * MITESTER_DELAY
	 */
	public static final String MITESTER_DELAY = "MITESTER_DELAY";

	/**
	 * TEST_INTERVAL
	 */
	public static final String TEST_INTERVAL = "TEST_INTERVAL";

	/**
	 * SEND
	 */
	public static final String SEND_MSG = "SEND";

	/**
	 * RECV
	 */
	public static final String RECV_MSG = "RECV";

	/**
	 * DISCARD
	 */
	public static final String DISCARD = "DISCARD";

	/**
	 * SIP request type
	 */
	public static final String REQUEST_MSG = "req";

	/**
	 * SIP response type
	 */
	public static final String RESPONSE_MSG = "res";

	/**
	 * ',' SEPARATOR
	 */
	public static final String COMMA_SEPARATOR = ",";

	/**
	 * '=' SEPARATOR
	 */
	public static final String EQUAL_SEPARATOR = "=";

	/**
	 * '_' SEPARATOR
	 */
	public static final String UNDERLINE_SEPARATOR = "_";

	/**
	 * ' ' SEPARATOR
	 */
	public static final String EMPTY_SEPARATOR = " ";

	/**
	 * seconds
	 */
	public static final String SEC = "sec";

	/**
	 * Maximum amount of time (in seconds) the ClientScriptRunner will wait for
	 * receiving notification or message from SUT
	 */
	public static final int SUT_WAIT_TIME_SEC = 35;

	/**
	 * Maximum amount of time (in seconds)the ServerScriptRunner will wait for
	 * receiving sip message from client
	 */
	public static final int MITESTER_WAIT_TIME_SEC = 40;

	/**
	 * amount of time represents default DISCARD_WAIT_TIME (in seconds)
	 */
	public static final int DISCARD_WAIT_TIME_SEC = 32;

	/**
	 * amount of time represents initial delay on sending SIP request/response 
	 */
	public static final int INITIAL_DELAY_SEC = 0;

	/**
	 * amount of time represents interval between the test execution (in
	 * milliseconds) 
	 */
	public static final int DEFAULT_TEST_INTERVAL = 2000;

}
