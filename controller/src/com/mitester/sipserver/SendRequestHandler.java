/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SendRequestHandler.java
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

import static com.mitester.sipserver.SipServerConstants.ACK_METHOD;
import static com.mitester.sipserver.SipServerConstants.BYE_METHOD;
import static com.mitester.sipserver.SipServerConstants.CANCEL_METHOD;
import static com.mitester.sipserver.SipServerConstants.INFO_METHOD;
import static com.mitester.sipserver.SipServerConstants.INVITE_METHOD;
import static com.mitester.sipserver.SipServerConstants.LINE_SEPARATOR;
import static com.mitester.sipserver.SipServerConstants.MESSAGE_METHOD;
import static com.mitester.sipserver.SipServerConstants.NOTIFY_METHOD;
import static com.mitester.sipserver.SipServerConstants.OPTIONS_METHOD;
import static com.mitester.sipserver.SipServerConstants.OUTGOING_SIP_MESSAGE;
import static com.mitester.sipserver.SipServerConstants.PRACK_METHOD;
import static com.mitester.sipserver.SipServerConstants.PUBLISH_METHOD;
import static com.mitester.sipserver.SipServerConstants.REFER_METHOD;
import static com.mitester.sipserver.SipServerConstants.REGISTER_METHOD;
import static com.mitester.sipserver.SipServerConstants.SERVER_REQUEST;
import static com.mitester.sipserver.SipServerConstants.SUBSCRIBE_METHOD;
import static com.mitester.sipserver.SipServerConstants.UPDATE_METHOD;
import gov.nist.javax.sip.message.SIPMessage;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.message.Request;

import com.mitester.sipserver.sipmessagehandler.ACKRequestHandler;
import com.mitester.sipserver.sipmessagehandler.BYERequestHandler;
import com.mitester.sipserver.sipmessagehandler.CANCELRequestHandler;
import com.mitester.sipserver.sipmessagehandler.INFORequestHandler;
import com.mitester.sipserver.sipmessagehandler.INVITERequestHandler;
import com.mitester.sipserver.sipmessagehandler.MESSAGERequestHandler;
import com.mitester.sipserver.sipmessagehandler.NOTIFYRequestHandler;
import com.mitester.sipserver.sipmessagehandler.OPTIONSRequestHandler;
import com.mitester.sipserver.sipmessagehandler.PRACKRequestHandler;
import com.mitester.sipserver.sipmessagehandler.PUBLISHRequestHandler;
import com.mitester.sipserver.sipmessagehandler.REFERRequestHandler;
import com.mitester.sipserver.sipmessagehandler.REGISTERRequestHandler;
import com.mitester.sipserver.sipmessagehandler.SUBSCRIBERequestHandler;
import com.mitester.sipserver.sipmessagehandler.UPDATERequestHandler;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;

/**
 * 
 * sends the SIP Request message
 * 
 * 
 * 
 */
public class SendRequestHandler {
	private static final Logger LOGGER = MiTesterLog
	        .getLogger(SendRequestHandler.class.getName());
	private static boolean isOriginator = false;

	public static boolean sendRequest(
	        com.mitester.jaxbparser.server.ACTION action, UdpCommn udpCommn)
	        throws NullPointerException, java.text.ParseException,
	        SipException, InvalidArgumentException, SocketException,
	        IOException {

		boolean isSent = true;
		SIPMessage sipMsg = null;
		int count = 1;
		Request request = null;
		String send = null;
		String msg = null;

		send = action.getValue();

		if (action.getCount() != null) {
			count = action.getCount().intValue();
		}
		if (send.equalsIgnoreCase(INVITE_METHOD)) {
			request = INVITERequestHandler.createINVITERequest();
		} else if (send.equalsIgnoreCase(UPDATE_METHOD)) {
			String method = UPDATE_METHOD;
			sipMsg = ProcessSIPMessage.getSIPMessage(INVITE_METHOD, method,
			        SERVER_REQUEST);
			request = UPDATERequestHandler.createUPDATERequest(sipMsg);
		} else if (send.equalsIgnoreCase(ACK_METHOD)) {
			isOriginator = true;
			String method = ACK_METHOD;
			sipMsg = ProcessSIPMessage.getSIPMessage(INVITE_METHOD, method,
			        SERVER_REQUEST);
			request = ACKRequestHandler.createACKRequest(sipMsg);
		} else if (send.equalsIgnoreCase(BYE_METHOD)) {
			String method = BYE_METHOD;
			sipMsg = ProcessSIPMessage.getSIPMessage(ACK_METHOD, method,
			        SERVER_REQUEST);
			request = BYERequestHandler.createBYERequest(sipMsg, isOriginator);
		} else if (send.equalsIgnoreCase(CANCEL_METHOD)) {
			String method = CANCEL_METHOD;
			sipMsg = ProcessSIPMessage.getSIPMessage(INVITE_METHOD, method,
			        SERVER_REQUEST);
			request = CANCELRequestHandler.createCANCELRequest(sipMsg);
		} else if (send.equalsIgnoreCase(NOTIFY_METHOD)) {
			String method = NOTIFY_METHOD;
			sipMsg = ProcessSIPMessage.getSIPMessage(INVITE_METHOD, method,
			        SERVER_REQUEST);
			request = NOTIFYRequestHandler.createNOTIFYRequest(sipMsg);
		} else if (send.equalsIgnoreCase(PUBLISH_METHOD)) {
			request = PUBLISHRequestHandler.createPUBLISHRequest();
		} else if (send.equalsIgnoreCase(SUBSCRIBE_METHOD)) {
			request = SUBSCRIBERequestHandler.createSUBSCRIBERequest();
		} else if (send.equalsIgnoreCase(MESSAGE_METHOD)) {
			request = MESSAGERequestHandler.createMESSAGERequest();
		} else if (send.equalsIgnoreCase(OPTIONS_METHOD)) {
			request = OPTIONSRequestHandler.createOPTIONSRequest();
		} else if (send.equalsIgnoreCase(REGISTER_METHOD)) {
			request = REGISTERRequestHandler.createREGISTERRequest();
		} else if (send.equalsIgnoreCase(PRACK_METHOD)) {
			String method = PRACK_METHOD;
			sipMsg = ProcessSIPMessage.getSIPMessage(INVITE_METHOD, method,
			        SERVER_REQUEST);
			request = PRACKRequestHandler.createPRACKRequest(sipMsg);
		} else if (send.equalsIgnoreCase(INFO_METHOD)) {
			String method = INFO_METHOD;
			sipMsg = ProcessSIPMessage.getSIPMessage(INVITE_METHOD, method,
			        SERVER_REQUEST);
			request = INFORequestHandler.createINFORequest(sipMsg);
		} else if (send.equalsIgnoreCase(REFER_METHOD)) {
			String method = REFER_METHOD;
			sipMsg = ProcessSIPMessage.getSIPMessage(INVITE_METHOD, method,
			        SERVER_REQUEST);
			request = REFERRequestHandler.createREFERRequest(sipMsg);
		}
		msg = request.toString();

		if (msg == null)
			msg = request.toString();
		msg = SIPHeaderProcessor.processSipHeaders(action, SERVER_REQUEST, msg);

		for (int i = 1; i <= count; i++) {
			TestUtility.printMessage(msg.toString());

			if (udpCommn.isBounded()) {
				udpCommn.sendUdpMessage(msg.toString());
				LOGGER.info(LINE_SEPARATOR + OUTGOING_SIP_MESSAGE
				        + LINE_SEPARATOR + msg.toString()
				        + OUTGOING_SIP_MESSAGE);
			} else {
				isSent = false;
			}

			isSent = true;

		}
		return isSent;
	}

	public static void setIsOriginator() {
		isOriginator = false;
	}
}
