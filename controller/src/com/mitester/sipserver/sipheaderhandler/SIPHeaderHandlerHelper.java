/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SIPHeaderHandlerHelper.java
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
package com.mitester.sipserver.sipheaderhandler;

import gov.nist.javax.sip.header.SIPHeader;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.Iterator;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.header.Header;

public class SIPHeaderHandlerHelper {

	/**
	 * addHeaderHelper is used to add accept-encoding header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return Accept-Encoding Header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static String addHeaderHelper(String headerName,
	        SIPMessage sipmessage) {
		Iterator<SIPHeader> headers = sipmessage.getHeaders();
		StringBuffer stringbuffer = new StringBuffer();
		while (headers.hasNext()) {
			Header h = headers.next();
			String vl = h.toString();
			if (vl.startsWith(headerName)) {
				int index = h.toString().indexOf(":");
				String valu = vl.substring(index + 1, vl.length());
				stringbuffer.append(valu.trim());
			}
		}
		String hvalue = stringbuffer.toString().trim();
		return hvalue;
	}

}
