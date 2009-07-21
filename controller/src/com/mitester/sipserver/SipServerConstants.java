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
package com.mitester.sipserver;

/**
 * SIP Server Constants are used sipServer
 */
public class SipServerConstants {
	/** To represent the INVITE method */
	public static final String INVITE_METHOD = "INVITE";

	/** To represent the ACK method */
	public static final String ACK_METHOD = "ACK";

	/** To represent the OPTIONS method */
	public static final String OPTIONS_METHOD = "OPTIONS";

	/** To represent the CANCEL method */
	public static final String CANCEL_METHOD = "CANCEL";

	/** To represent the BYE method */
	public static final String BYE_METHOD = "BYE";

	/** To represent the PRACK method */
	public static final String PRACK_METHOD = "PRACK";

	/** To represent the PUBLISH method */
	public static final String PUBLISH_METHOD = "PUBLISH";

	/** To represent the INFO method */
	public static final String INFO_METHOD = "INFO";

	/** To represent the SUBSCRIBE method */
	public static final String SUBSCRIBE_METHOD = "SUBSCRIBE";

	/** To represent the NOTIFY method */
	public static final String NOTIFY_METHOD = "NOTIFY";

	/** To represent the REFER method */
	public static final String REFER_METHOD = "REFER";

	/** To represent the REGISTER method */
	public static final String REGISTER_METHOD = "REGISTER";

	/** To represent the MESSAGE method */
	public static final String MESSAGE_METHOD = "MESSAGE";

	/** To represent the UPDATE method */
	public static final String UPDATE_METHOD = "UPDATE";

	/** To represent the SIP Version method */
	public static final String SIPVERSION = "SIP/2.0";

	/** To represents the contants value to the REQUEST */
	public static final String SERVER_REQUEST = "REQUEST";

	/** To represents the contants value to the RESPONSE symbol */
	public static final String SERVER_RESPONSE = "RESPONSE";

	/** To represent constant value to the incoming sip message */
	public static final String INCOMING_MSG = "incoming SIP message";

	/** To represent constant value to the outgoing sip message */
	public static final String OUTGOING_MSG = "outgoing SIP message";

	/** To represent constant value to the incoming sip message */
	public static final String INCOMING_SIP_MESSAGE = "-----------------------------------------< incoming SIP message >------------------------------------------------------"
			+ System.getProperty("line.separator");

	/** To represent constant value to the outgoing sip message */
	public static final String OUTGOING_SIP_MESSAGE = "-----------------------------------------< outgoing SIP message >------------------------------------------------------"
			+ System.getProperty("line.separator");

	/** To represent constant value to the line separator */
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/** To represent constant value to the maximum packet length */
	public static final int MAX_PACKET_LENGTH = 8192;

	/** To represent constant value to the MITESTER LISTENING PORT */
	public static final String MITESTER_SIP_PORT = "MITESTER_SIP_PORT";

	/** To represent constant value to the CLIENT LISTENING PORT */
	public static final String SUT_SIP_PORT = "SUT_SIP_PORT";

	/** To represent constant value to the SUT IP ADDRESS */
	public static final String SUT_IP_ADDRESS = "SUT_IP_ADDRESS";

	/** To represent constant value to the client port */
	public static final int DEFAULT_CLIENT_PORT = 5060;

	/** To represent constant value to the server port */
	public static final int DEFAULT_SERVER_PORT = 5070;

	/** To represent constant value to the maximum buffer length */
	public static final int MAX_BUFFER_LENGTH = 10000;

	/** To represent constant value to the maximum packet size */
	public static final int MAX_PACKET_SIZE = 9000;

	/** To represent constant value to the local host */
	public static final String LOCAL_HOST = "localhost";

	/** To represent server mode */
	public static final String SERVER_MODE = "SERVER_MODE";

	/** To represent B2BUA mode */
	public static final String B2BUA_MODE = "B2BUA";

	/** To represent proxy mode */
	public static final String PROXY_MODE = "PROXY";

	/** To represent normal mode */
	public static final String NORMAL_MODE = "NORMAL";

	/** To represent authentication */
	public static final String AUTHENTICATION = "AUTHENTICATION";

	/** To represent validation */
	public static final String VALIDATION = "VALIDATION";
	
	/** To represent presence of header */
	public static final String CHECK_PRESENCE_OF_HEADER = "CHECK_PRESENCE_OF_HEADER";

	/** To represent validation path */
	public static final String VALIDATION_FILE_PATH = "VALIDATION_FILE_PATH";

	/** To represent YES state */
	public static final String YES = "YES";

	/** To represent NO */
	public static final String NO = "NO";
	
	/** To represent the single quote */
	public static final String SINGLE_QUOTE = "'";
	
	/** To represent the space separator */
	public static final String SPACE_SEP = " ";
	
	/** To represent constant error message to the SIP header Validation */
	//public static final String SIP_VALIDATE_ERROR_MESSAGE = "in the incoming SIP Message is mismatched with the syntax/value specified in 'validateHeader.properties'. Header presence/validation test failed... ";

//	/** To represent constant error message to the missed SIP header */
//	public static final String MISSED_SIP_HEADER_ERROR_MESSAGE_MUL = "headers are missed in received SIP Message.";
//
//	/** To represent constant error message to the missed SIP header */
//	public static final String MISSED_SIP_HEADER_ERROR_MESSAGE_SIN = "header is missed in received SIP Message.";

}


