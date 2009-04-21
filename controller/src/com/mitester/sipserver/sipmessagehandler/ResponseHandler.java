/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ResponseHandler.java
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
package com.mitester.sipserver.sipmessagehandler;

import static com.mitester.sipserver.SipServerConstants.SERVER_RESPONSE;
import gov.nist.javax.sip.header.Via;
import gov.nist.javax.sip.message.MessageFactoryImpl;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sip.InvalidArgumentException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Response;

import com.mitester.sipserver.ProcessSIPMessage;

/**
 * Used to generate the SIP Response with the status code
 * 
 * 
 * 
 */
public class ResponseHandler {
	/**
	 * Generating SIP Response with the status code
	 * 
	 * @param statusCode
	 * @param method
	 * @param sipMessage
	 * @return Response with the status code and method
	 * @throws NullPointerException
	 * @throws java.text.ParseException
	 * @throws PeerUnavailableException
	 * @throws InvalidArgumentException 
	 */
	public static Response sendResponseWithCode(int statusCode, String method,
	        SIPMessage sipMessage,String dialog) throws NullPointerException,
	        java.text.ParseException, PeerUnavailableException, InvalidArgumentException {
		MessageFactoryImpl messageFactoryImpl = new MessageFactoryImpl();
		String tag = null;
		Response response;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactory = factory.createHeaderFactory();
		AddressFactory addressFactory = factory.createAddressFactory();
		List<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
		Random remotetag = new Random();

		MaxForwardsHeader m;
		CallIdHeader call;
		FromHeader from;
		ToHeader to;
		CSeqHeader c;
		if(dialog != null) {
			SIPMessage sipMsg = ProcessSIPMessage.getSIPMessage(dialog, method,
			        SERVER_RESPONSE);
			
			call = sipMsg.getCallId();

			from = sipMsg.getFrom();
			to = sipMsg.getTo();
		} else {
			call = sipMessage.getCallId();

			from = sipMessage.getFrom();
			to = sipMessage.getTo();
		}
		
//		m = sipMessage.getMaxForwards();
		m = headerFactory.createMaxForwardsHeader(70);
		
		c = sipMessage.getCSeq();
		c.setMethod(method);
		Via v = sipMessage.getTopmostVia();
		
		viaHeaders.add(v);
		response = messageFactoryImpl.createResponse(statusCode, call, c, from,
		        to, viaHeaders, m);
		response.removeHeader(MaxForwardsHeader.NAME);
		
		String reason = SIPResponse.getReasonPhrase(statusCode);
		response.setReasonPhrase(reason);
		SipURI contactUrl = addressFactory.createSipURI(null, "127.0.0.1");
		contactUrl.setPort(5070);

		SipURI contactURI = addressFactory.createSipURI("UserA", "127.0.0.1");

		contactURI.setPort(5070);

		contactURI.setParameter("transport", "udp");

		Address contactAddress = addressFactory.createAddress(contactURI);

		contactAddress.setDisplayName("UserA");

		ContactHeader contactHeader = headerFactory
		        .createContactHeader(contactAddress);

		response.addHeader(contactHeader);

		if (method.equals("REGISTER")) {
			if (statusCode == 200) {
				tag = Integer.toHexString(remotetag.nextInt());
				if(to.getTag() == null)
				to.setTag(tag);
				// ExpiresHeader e = request.getExpires();
				ExpiresHeader e = sipMessage.getExpires();
				response.setExpires(e);
			} else {
				tag = Integer.toHexString(remotetag.nextInt());
				if(to.getTag() == null)
				to.setTag(tag);
			}
		}

		if (method.equals("SUBSCRIBE")) {
			if (statusCode == 200) {
				tag = Integer.toHexString(remotetag.nextInt());
				if(to.getTag() == null)
				to.setTag(tag);
				// ExpiresHeader e = request.getExpires();
				ExpiresHeader e = sipMessage.getExpires();
				response.setExpires(e);
			} else {
				tag = Integer.toHexString(remotetag.nextInt());
				if(to.getTag() == null)
				to.setTag(tag);
			}
		}

		if (method.equals("INVITE")) {
			if (statusCode == 200) {
				tag = Integer.toHexString(remotetag.nextInt());
				if(to.getTag() == null)
					to.setTag(tag);

			} else {
				tag = Integer.toHexString(remotetag.nextInt());
				if(to.getTag() == null)
				to.setTag(tag);
			}
		}
		return response;
	}

}
