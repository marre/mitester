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

import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.parser.StringMsgParser;

import java.text.ParseException;

import javax.sip.SipException;

import org.apache.log4j.Logger;

import com.mitester.utility.MiTesterLog;

/**
 * SipParser is used to parse the received SIP Message from System Under Test
 * 
 * 
 */

public class SipParser {

	private static StringMsgParser sipMsgParser = new StringMsgParser();

	private static final Logger LOGGER = MiTesterLog.getLogger(SipParser.class
			.getName());

	public SipParser() {
		sipMsgParser = new StringMsgParser();
	}

	/**
	 * parseSipMessage is used to parse the sip messages received from the
	 * System under test
	 * 
	 * @param msgBuf
	 *            is a String object contains the SIP message
	 * @return is a SIPMessage object
	 * @throws ParseException
	 * @throws SipException
	 * @throws NullPointerException
	 */
	public static SIPMessage parseSipMessage(String msgBuf)
			throws ParseException, SipException, NullPointerException {

		LOGGER.info("parsing the SIP message");		

		return sipMsgParser.parseSIPMessage(msgBuf);
	}


}
