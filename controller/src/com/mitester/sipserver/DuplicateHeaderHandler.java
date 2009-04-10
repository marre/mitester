/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: DuplicateHeaderHandler.java
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
 * Package 				License 										    Details
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

import static com.mitester.sipserver.SipServerConstants.ACK_METHOD;
import static com.mitester.sipserver.SipServerConstants.BYE_METHOD;
import static com.mitester.sipserver.SipServerConstants.CANCEL_METHOD;
import static com.mitester.sipserver.SipServerConstants.INFO_METHOD;
import static com.mitester.sipserver.SipServerConstants.INVITE_METHOD;
import static com.mitester.sipserver.SipServerConstants.MESSAGE_METHOD;
import static com.mitester.sipserver.SipServerConstants.NOTIFY_METHOD;
import static com.mitester.sipserver.SipServerConstants.OPTIONS_METHOD;
import static com.mitester.sipserver.SipServerConstants.PRACK_METHOD;
import static com.mitester.sipserver.SipServerConstants.REFER_METHOD;
import static com.mitester.sipserver.SipServerConstants.REGISTER_METHOD;
import static com.mitester.sipserver.SipServerConstants.SIPVERSION;
import static com.mitester.sipserver.SipServerConstants.SUBSCRIBE_METHOD;
import static com.mitester.sipserver.SipServerConstants.UPDATE_METHOD;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;

import com.mitester.jaxbparser.server.Fline;
import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.utility.MiTesterLog;

/**
 * Duplication of SIPHeaders
 * 
 * 
 * 
 */

public class DuplicateHeaderHandler {

	final static String NEWLINE = "\r\n";

	private static final Logger logger = MiTesterLog
	        .getLogger(DuplicateHeaderHandler.class.getName());

	/**
	 * addDuplicateSIPHeader is used to duplicate the SIPHeader by count
	 * 
	 * @param sipDUPHeaders
	 * @param sipmesg
	 * @return Duplicated SIP Message
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */

	public static String addDuplicateSIPHeader(Header objHeader, String sipmesg)
	        throws SipException, ParseException, InvalidArgumentException,
	        IOException, NullPointerException,
	        java.lang.IllegalArgumentException {
		logger
		        .info("Duplicating the headers and parameters in the SIP Message");

		String duplicatedmsg = null;
		List<Param> parameter = objHeader.getParam();
		if (parameter.size() != 0) {
			duplicatedmsg = duplicateParameter(objHeader.getName(), sipmesg,
			        parameter);
		}
		if (parameter.size() == 0) {
			duplicatedmsg = duplicateheader(objHeader.getName(), Integer
			        .parseInt(objHeader.getCount().toString()), sipmesg);
		}

		return duplicatedmsg;
	}

	public static String addDuplicateContent(String name, String sipmesg,
	        int count) {
		StringBuilder resp = new StringBuilder(sipmesg);
		StringTokenizer token = new StringTokenizer(sipmesg.toString(), NEWLINE);
		while (token.hasMoreElements()) {
			String nexttoken = token.nextToken();
			int index = nexttoken.indexOf(name.toLowerCase() + "=");
			if (index >= 0) {
				int i = resp.indexOf(name.toLowerCase() + "=");
				for (int n = 0; n < count - 1; n++) {
					resp.insert(i, nexttoken + NEWLINE);
				}
			}
		}
		return null;
	}

	public static String addDuplicateLine(Fline fline, String msg) {

		int count = Integer.parseInt(fline.getCount().toString());
		StringBuilder resp = new StringBuilder(msg);
		StringTokenizer token = new StringTokenizer(resp.toString(), NEWLINE);
		String nexttoken = token.nextToken();
		for (int n = 0; n < count - 1; n++) {
			resp.insert(0, nexttoken + NEWLINE);
		}
		msg = resp.toString();
		return msg;
	}

	public static String duplicateParameter(String name, String msg,
	        List<Param> param) {
		StringTokenizer token = new StringTokenizer(msg, NEWLINE);
		StringBuilder s = new StringBuilder();
		SIPHeaders namehde = SIPHeaders.getSipHeaderfromString(name);
		while (token.hasMoreElements()) {
			String nexttoken = token.nextToken();
			int a = nexttoken.indexOf(namehde.toString());
			if (a >= 0) {
				String line = nexttoken;
				for (Param parameter : param) {
					String DUPHname = parameter.getName();
					String DUPHValue = parameter.getCount().toString();
					int DUPPCount = Integer.parseInt(DUPHValue);
					int indexparam = line.indexOf(DUPHname);
					String parame = line.substring(indexparam, line.length());
					parame = ";" + parame;
					parame = parame.trim();
					line = line.trim();
					s.append(line);
					for (int n1 = 0; n1 < DUPPCount - 1; n1++) {
						s.append(parame);
					}
					s.append(NEWLINE);
				}
			} else {
				s.append(nexttoken);
				s.append(NEWLINE);
			}
		}

		return s.toString();
	}

	public static String duplicateheader(String name, int count, String msg) {
		StringBuilder resp = new StringBuilder(msg);
		StringTokenizer token = new StringTokenizer(msg, NEWLINE);
		if (!name.equals("ALL")) {
			SIPHeaders namehde = SIPHeaders.getSipHeaderfromString(name
			        .toUpperCase());
			int index = resp.indexOf(namehde.toString());
			while (token.hasMoreElements()) {
				String nexttoken = token.nextToken();
				int a = nexttoken.indexOf(namehde.toString());
				if (a >= 0) {
					String fromhdr = nexttoken;
					for (int n = 0; n < count - 1; n++) {
						resp.insert(index, fromhdr + NEWLINE);
					}
				}
			}
		} else {
			while (token.hasMoreElements()) {
				String nexttoken = token.nextToken();
				if (nexttoken.startsWith(SIPVERSION)
				        || ((nexttoken.startsWith(REGISTER_METHOD))
				                || (nexttoken.startsWith(INVITE_METHOD))
				                || (nexttoken.startsWith(ACK_METHOD))
				                || (nexttoken.startsWith(CANCEL_METHOD))
				                || (nexttoken.startsWith(BYE_METHOD))
				                || (nexttoken.startsWith(PRACK_METHOD))
				                || (nexttoken.startsWith(SUBSCRIBE_METHOD))
				                || (nexttoken.startsWith(UPDATE_METHOD))
				                || (nexttoken.startsWith(REFER_METHOD))
				                || (nexttoken.startsWith(INFO_METHOD))
				                || (nexttoken.startsWith(MESSAGE_METHOD))
				                || (nexttoken.startsWith(NOTIFY_METHOD)) || (nexttoken
				                .startsWith(OPTIONS_METHOD)))) {
					resp.append(nexttoken);
					resp.append(NEWLINE);
				} else {
					for (int n = 0; n < count; n++) {
						resp.append(nexttoken);
						resp.append(NEWLINE);
					}
				}
			}
		}
		return resp.toString();
	}
}
