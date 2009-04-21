/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SendResponseHandler.java
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

import static com.mitester.sipserver.SipServerConstants.LINE_SEPARATOR;
import static com.mitester.sipserver.SipServerConstants.OUTGOING_SIP_MESSAGE;
import static com.mitester.sipserver.SipServerConstants.SERVER_RESPONSE;
import gov.nist.javax.sip.message.SIPMessage;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.message.Response;

import com.mitester.sipserver.sipmessagehandler.ResponseHandler;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;

/**
 * 
 * sends the SIP Response message
 * 
 * 
 * 
 */
public class SendResponseHandler {
	private static final Logger LOGGER = MiTesterLog
	        .getLogger(SendResponseHandler.class.getName());

	public static boolean sendResponse(
	        com.mitester.jaxbparser.server.ACTION action, UdpCommn udpCommn)
	        throws NullPointerException, java.text.ParseException,
	        SipException, InvalidArgumentException, SocketException,
	        IOException {
		boolean isSent = false;
		int statusCode = 0;
		int Count = 1;
		String method = null;
		SIPMessage sipMsg = null;
		Response res = null;
		String msg = null;
		String dialog = null;
		if (action.getCount() != null) {
			Count = action.getCount().intValue();
		}
		if(action.getDialog() != null){
			dialog = action.getDialog();
		}
		String send = action.getValue();
		int methodindex = send.indexOf("_");
		String sCode = send.substring(0, methodindex);
		method = send.substring(methodindex + 1, send.length());
		statusCode = Integer.parseInt(sCode);
		
		sipMsg = ProcessSIPMessage.getSIPMessage(method, method,
		        SERVER_RESPONSE);

		if (sipMsg != null) {
			
			res = ResponseHandler.sendResponseWithCode(statusCode, method,
			        sipMsg,dialog);
			msg = res.toString();
		}
		msg = SIPHeaderProcessor
		        .processSipHeaders(action, SERVER_RESPONSE, msg);

		for (int i = 1; i <= Count; i++) {
			TestUtility.printMessage(msg);
			if (udpCommn.isBounded()) {
				udpCommn.sendUdpMessage(msg.toString());
				LOGGER.info(LINE_SEPARATOR + OUTGOING_SIP_MESSAGE
				        + LINE_SEPARATOR + msg.toString()
				        + OUTGOING_SIP_MESSAGE);
			} else {
				isSent = false;
			}
		}
		isSent = true;

		return isSent;
	}
}
