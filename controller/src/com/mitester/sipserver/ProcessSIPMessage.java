/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ProcessSIPMessage.java
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

import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sip.SipException;

import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;

/**
 * This class process the all incoming and outgoing SIP messages
 * 
 * 
 * 
 */
public class ProcessSIPMessage {
	private static final Logger LOGGER = MiTesterLog
	        .getLogger(ProcessSIPMessage.class.getName());
	private static List<SIPMessage> sipMessages = new ArrayList<SIPMessage>();
	private static SIPMessage sipMessage = null;

	public static SIPMessage getSIPMessage(String MethodName, String Method,
	        String Type) {
		if (Type.equals(SipServerConstants.SERVER_REQUEST)) {
			if (Method != SipServerConstants.ACK_METHOD) {
				for (int i = 0; i < sipMessages.size(); i++) {
					SIPMessage s = (SIPMessage) (sipMessages.get(i)).clone();
					if (s.getCSeq().getMethod().equals(MethodName)) {
						sipMessage = s;
						break;
					}
				}
			} else {
				for (int i = sipMessages.size() - 1; i > 0; i--) {
					SIPMessage s = (SIPMessage) sipMessages.get(i).clone();
					if (s.getCSeq().getMethod().equals(MethodName)) {
						sipMessage = s;
						break;
					}
				}
			}
		} else {
			for (int i = sipMessages.size() - 1; i >= 0; i--) {
				SIPMessage s = (SIPMessage) sipMessages.get(i).clone();
				if (s.getCSeq().getMethod().equals(MethodName)) {
					sipMessage = s;
					break;
				}
			}
		}
		return sipMessage;
	}

	public static SIPMessage processSIPMessage(String sipMsgBuf, String type)
	        throws ParseException, SipException, NullPointerException {

		SIPMessage parsedSipMsg = null;
		if ((sipMessages.size() > 0)
		        && (type.equals(SipServerConstants.INCOMING_MSG))) {
			for (int i = 0; i < sipMessages.size(); i++) {
				SIPMessage getSipMsg = sipMessages.get(i);
				parsedSipMsg = SipParser.parseSipMessage(sipMsgBuf);
				if ((getSipMsg.getFirstLine().equals(parsedSipMsg
				        .getFirstLine()))
				        && (getSipMsg.getCSeq().equals(parsedSipMsg.getCSeq()))
				        && (getSipMsg.getCallId().equals(parsedSipMsg
				                .getCallId()))
				        && (getSipMsg.getFrom().equals(parsedSipMsg.getFrom()))
				        && (getSipMsg.getTo().equals(parsedSipMsg.getTo()))
				        && (getSipMsg.getTopmostVia().getBranch()
				                .equals(parsedSipMsg.getTopmostVia()
				                        .getBranch()))) {

					TestUtility
					        .printMessage("SIP message already received hence dropped");
					LOGGER.info("SIP message already received hence dropped");
					return null;
				} else {

				}
			}
			
			/* add received sip message */
			sipMessages.add(parsedSipMsg);

		} else {
			parsedSipMsg = SipParser.parseSipMessage(sipMsgBuf);
			sipMessages.add(parsedSipMsg);
			parsedSipMsg = SipParser.parseSipMessage(sipMsgBuf);
		}
		return parsedSipMsg;
	}

	public static SIPMessage getSipMessage() {
		return sipMessage;
	}

	/**
	 * remove the unwanted int the SIPMessage List
	 */
	public static void removeSipMessageFromList() {

		int removeIndex = sipMessages.size() - 1;
		sipMessages.remove(removeIndex);
	}

	/**
	 * clean-up the SIPMessage List for every Test execution
	 */
	public static void cleanUpSipMessageList() {
		sipMessages = null;
		SendRequestHandler.setIsOriginator();
		sipMessages = new ArrayList<SIPMessage>();
	}
}
