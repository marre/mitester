/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: InvalidValueToHeaders.java
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
package com.mitester.sipserver;

import java.util.List;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;

/**
 * This class process the SIP header with Invalid value
 * 
 * 
 * 
 */

public class InvalidValueToHeaders {
	/**
	 * addInvalidValuesToHeader is used to add a invalid values to the sip
	 * message
	 * 
	 * @param InvalidHeader
	 * @param msg
	 * @return sip message with invalid values
	 */
	public static String addInvalidValuesToHeader(
	        List<com.mitester.jaxbparser.server.Header> InvalidHeader,
	        String msg) {
		StringBuilder Message = new StringBuilder();
		StringBuffer sb = new StringBuffer();
		for (Header header : InvalidHeader) {
			List<Param> parameter = header.getParam();
			String name = null, value = null;
			name = header.getName();
			value = header.getValue().toString();
			name = name.toUpperCase();
			sb.append(SIPHeaders.getSipHeaderfromString(name).toString() + ": "
			        + value);
			for (Param param : parameter) {
				String pn = param.getName();
				String pv = param.getValue();
				sb.append(";" + pn + "=" + pv);
			}

			Message = new StringBuilder(msg);
			if ((!name.equalsIgnoreCase("CONTENT-LENGTH"))
			        && (!name.equalsIgnoreCase("FROM"))) {
				int index = Message.indexOf("From: ");
				name = name.toUpperCase();

				if (sb.length() != 0)
					Message.insert(index, sb.toString() + "\r\n");

				else
					Message.insert(index, SIPHeaders.getSipHeaderfromString(
					        name).toString()
					        + ": " + value + "\r\n");
			} else {
				if (name.equalsIgnoreCase("CONTENT-LENGTH")) {
					int index = Message.indexOf("Content-Length: ");
					Message.delete(index, Message.length());
					Message.insert(index, sb.toString() + "\r\n");
				}
				if (name.equalsIgnoreCase("FROM")) {
					int index = Message.indexOf("To: ");
					name = name.toUpperCase();

					if (sb.length() != 0)
						Message.insert(index, sb.toString() + "\r\n");
					else
						Message.insert(index, SIPHeaders
						        .getSipHeaderfromString(name).toString()
						        + ": " + value + "\r\n");
				}
			}
		}
		return Message.toString();
	}

}
