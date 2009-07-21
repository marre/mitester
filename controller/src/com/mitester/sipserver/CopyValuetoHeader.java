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
 * This class copy the header values
 * 
 * 
 * 
 */

public class CopyValuetoHeader {
	private static final Logger LOGGER = MiTesterLog
			.getLogger(CopyValuetoHeader.class.getName());
	private static final String CONTENT_LENGTH = "Content-Length";
	private static final String FROM = "From";

	/**
	 * 
	 * @param copy
	 *            list of informations to be copied
	 * @param msg
	 *            header value
	 * @return the To header
	 */
	public static String addCopyValueToHeader(List<String> copy, String msg) {
		LOGGER.info("Adding copy value from the previous SIP Message");
		StringBuilder Message = null;
		if (copy != null) {
			Message = new StringBuilder(msg);
			for (int j = 0; j < copy.size(); j++) {
				String name = copy.get(j);

				if ((!name.startsWith(CONTENT_LENGTH))
						&& (!name.startsWith(FROM))) {
					int index = Message.indexOf(FROM);
					Message.insert(index, name);
				} else if (name.startsWith(FROM)) {
					int index = Message.indexOf("To");
					Message.insert(index, name);
				} else if (name.startsWith(CONTENT_LENGTH)) {
					int index = Message.indexOf(CONTENT_LENGTH);
					Message.delete(index, Message.length());
					Message.insert(index, name);
				}
			}
		}
		return Message.toString();
	}
}
