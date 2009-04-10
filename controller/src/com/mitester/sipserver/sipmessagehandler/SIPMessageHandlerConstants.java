/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SIPMessageHandlerConstants.java
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
 * Package 				License 										Details
 *---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Jain SIP stack 		NIST-CONDITIONS-OF-USE 						        https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 				The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * 
 */

/*
 * miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications.
 *  
 */
package com.mitester.sipserver.sipmessagehandler;

public class SIPMessageHandlerConstants {

	/**
	 * To represent the From user name
	 */
	public static final String FROM_USER_NAME = "UserA";

	/**
	 * To represent the From display name
	 */
	public static final String FROM_DISPLAY_NAME = "UserA";

	/**
	 * To represent the to user name
	 */
	public static final String TO_USER_NAME = "UserB";

	/**
	 * To represent the to Dispaly name
	 */
	public static final String TO_DISPLAY_NAME = "UserB";

	/**
	 * To represent the protocol
	 */
	public static final String PROTOCOL = "udp";

	/**
	 * to represent the magic cookies
	 */
	public static final String MAGIC_COOKIES = "z9hG4bK";

	/**
	 * to Represent loop back address
	 */
	public static final String LOOP_BACK_ADDRESS = "127.0.0.1";

	/**
	 * To represent from Port
	 */
	public static final int FROM_PORT = 5070;

	/**
	 * To represent TO port
	 */
	public static final int TO_PORT = 5060;

	/**
	 * To represent the sucessful response code
	 */

	public static final int RESPONSE_CODE = 200;

	/**
	 * To represent transaport
	 */
	public static final String TRANSPORT = "transport";

	/**
	 * To represent max-forwards
	 */
	public static final int MAXFORWARDS = 70;

	/**
	 * To represent At symbol
	 */
	public static final String AT = "@";

	/**
	 * To represent expires symbol
	 */
	public static final int EXPIRES = 3600;
}
