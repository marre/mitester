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

import java.util.List;

import org.apache.log4j.Logger;


import com.mitester.utility.MiTesterLog;

/**
 * This class process the SIP header with Empty value
 */
public class EmptyValueToHeader {
	private static final Logger LOGGER = MiTesterLog
	.getLogger(EmptyValueToHeader.class.getName());
	public static String addEmptyValueToHeader(List<String> emptyHeader,
	        String msg) {
		LOGGER.info("Adding empty value to the Headers");
		StringBuilder Message = null;
		Message = new StringBuilder(msg);
		if (emptyHeader != null) {
			for (int j = 0; j < emptyHeader.size(); j++) {
				String name = emptyHeader.get(j);

				if ((!name.equalsIgnoreCase("Content-Length"))
				        && (!name.equalsIgnoreCase("FROM"))) {
					int index = Message.indexOf("From: ");
					Message.insert(index, name + ": " + "\r\n");
				} else {
					if (name.equalsIgnoreCase("CONTENT-LENGTH")) {
						int index = Message.indexOf("Content-Length: ");
						Message.delete(index, index + 17);
						Message.insert(index, "Content-Length: ");
					}
					if (name.equalsIgnoreCase("From")) {
						int index = Message.indexOf("To: ");
						// int i = index + 4;
						Message.insert(index, "From: \r\n");
					}
				}
			}
		}
		return Message.toString();
	}

}
