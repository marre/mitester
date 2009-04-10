/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: FirstLineHandler.java
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

import java.util.List;
import java.util.StringTokenizer;

import com.mitester.jaxbparser.server.ReqLine;
import com.mitester.jaxbparser.server.StatusLine;

/**
 * This class process the first line of SIP message
 * 
 */

public class FirstLineHandler {

	/**
	 * addStatusLine is used to add a first line to the sip mesage
	 * 
	 * @param sipMessage
	 * @param statusLine
	 * @return after added first line will be return
	 */
	public static String addStatusLine(String sipMessage,
	        List<StatusLine> statusLine) {

		String msg = null;
		msg = sipMessage.toString();
		if (statusLine != null && statusLine.size() != 0) {
			StringBuilder sb = new StringBuilder(msg);
			StringTokenizer token1 = new StringTokenizer(sb.toString(), "\r\n");
			StringTokenizer token = new StringTokenizer(msg, "\r\n");
			String NextToken = token.nextToken();
			String Array[] = NextToken.split(" ");
			for (StatusLine testStatus : statusLine) {

				if (testStatus.getName().equals("sip-version")) {
					Array[0] = testStatus.getValue();
				} else if (testStatus.getName().equals("status-code")) {
					Array[1] = testStatus.getValue();
				} else if (testStatus.getName().equals("reason-phrase")) {
					Array[2] = testStatus.getValue();
				}
			}
			String a = null;
			if (Array.length > 2)
				a = Array[0] + " " + Array[1] + " " + Array[2];
			else
				a = Array[0] + " " + Array[1];
			String first = token1.nextToken();
			int len = first.length();
			sb.delete(0, len);
			sb.insert(0, a);
			msg = sb.toString();
		}
		return msg;
	}

	/**
	 * addRequestLine is used to add a request line to the sip mesage
	 * 
	 * @param sipMessage
	 * @param sipReqLine
	 * @return string sip message with added first line
	 */
	public static String addRequestLine(String sipMessage,
	        List<ReqLine> sipReqLine) {
		String msg = null;

		msg = sipMessage.toString();

		if (sipReqLine != null && sipReqLine.size() != 0) {
			StringBuilder sb = new StringBuilder(msg);
			StringTokenizer token1 = new StringTokenizer(sb.toString(), "\r\n");
			StringTokenizer token = new StringTokenizer(msg, "\r\n");
			String NextToken = token.nextToken();
			String Array[] = NextToken.split(" ");
			for (ReqLine testStatus : sipReqLine) {

				if (testStatus.getName().equals("sip-version")) {
					Array[2] = testStatus.getValue();
				} else if (testStatus.getName().equals("req-uri")) {
					Array[1] = testStatus.getValue();
				} else if (testStatus.getName().equals("method")) {
					Array[0] = testStatus.getValue();
				} else if (testStatus.getName().equals("transport")) {
					String tran = Array[1] + ";transport="
					        + testStatus.getValue();
					Array[1] = tran;
				} else if (testStatus.getName().equals("maddr")) {
					String tran = Array[1] + ";maddr=" + testStatus.getValue();
					Array[1] = tran;
				} else if (testStatus.getName().equals("ttl")) {
					String tran = Array[1] + ";ttl=" + testStatus.getValue();
					Array[1] = tran;
				} else {
					String tran = Array[1] + ";" + testStatus.getName() + "="
					        + testStatus.getValue();
					Array[1] = tran;
				}
			}
			String a = Array[0] + " " + Array[1] + " " + Array[2];
			String first = token1.nextToken();
			int len = first.length();
			sb.delete(0, len);
			sb.insert(0, a);
			msg = sb.toString();
		}
		return msg;
	}

	/**
	 * removeFirstLine is used to remvoe the first line
	 * 
	 * @param sipMessage
	 * @return string sip message with out first line
	 */
	public static String removeFirstLine(String sipMessage) {

		String msg = null;
		msg = sipMessage;
		final String NEWLINE = "\r\n";
		StringBuilder resp = new StringBuilder(msg);
		StringTokenizer token = new StringTokenizer(resp.toString(), NEWLINE);
		String nexttoken = token.nextToken();
		int lent = nexttoken.length();
		resp.delete(0, lent);
		resp.delete(0, 2);
		msg = resp.toString();
		return msg;
	}
}
