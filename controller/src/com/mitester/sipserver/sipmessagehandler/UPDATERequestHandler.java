/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: UPDATERequestHandler.java
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


import static com.mitester.sipserver.SipServerConstants.SERVER_REQUEST;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.FROM_DISPLAY_NAME;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.FROM_PORT;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.FROM_USER_NAME;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.LOOP_BACK_ADDRESS;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.MAGIC_COOKIES;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.PROTOCOL;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.TO_DISPLAY_NAME;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.TO_PORT;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.TO_USER_NAME;
import gov.nist.javax.sip.header.Via;
import gov.nist.javax.sip.header.ViaList;
import gov.nist.javax.sip.message.MessageFactoryImpl;
import gov.nist.javax.sip.message.SIPMessage;

import java.util.ArrayList;
import java.util.Random;

import javax.sip.InvalidArgumentException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

import com.mitester.sipserver.ProcessSIPMessage;

/**
 * A method to update the on-going/early session with the updated SDP
 * parameters.
 * 
 * 
 * 
 */
public class UPDATERequestHandler {
	/**
	 * Generating UPDATE Request
	 * 
	 * @param sipmsg
	 * @return UPDATE Request
	 * @throws NullPointerException
	 * @throws SipException
	 * @throws java.text.ParseException
	 * @throws InvalidArgumentException
	 * @throws PeerUnavailableException
	 */
	public static Request createUPDATERequest(SIPMessage sipmsg,String dialog)
	        throws NullPointerException, SipException,
	        java.text.ParseException, InvalidArgumentException,
	        PeerUnavailableException {
		MessageFactoryImpl messageFactoryImpl = new MessageFactoryImpl();
		Request request;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactory = factory.createHeaderFactory();
		AddressFactory addressFactory = factory.createAddressFactory();
		ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
		Random remotetag = new Random();
		if (sipmsg != null) {
			ViaList via = sipmsg.getViaHeaders();
			String viaValue = via.getValue();
			int index = viaValue.indexOf("=");
			String branch = viaValue.substring(index + 1, viaValue.length());
			ViaHeader viaHeaderBye = headerFactory.createViaHeader("127.0.0.1",
			        5070, "udp", branch);
			ViaList viaHeader = new ViaList();
			viaHeader.add((Via) viaHeaderBye);
			SipURI requestURI = addressFactory
			        .createSipURI("user", "127.0.0.1");
			MaxForwardsHeader maxfwd = headerFactory
			        .createMaxForwardsHeader(70);
			long invitecseq = sipmsg.getCSeq().getSeqNumber();
			CSeqHeader cseq = headerFactory.createCSeqHeader(invitecseq + 1,
			        Request.UPDATE);
			ToHeader toHeader;
			FromHeader fromHeader;
			CallIdHeader callid;
			if(dialog == null) {
				callid = sipmsg.getCallId();
				Address from = sipmsg.getFrom().getAddress();
				String fromTag = sipmsg.getFromTag();
				Address to = sipmsg.getTo().getAddress();
				String toTag = sipmsg.getToTag();
				fromHeader = headerFactory.createFromHeader(to, toTag);
				toHeader = headerFactory.createToHeader(from, fromTag);
			} else {
				SIPMessage sipMsg = ProcessSIPMessage.getSIPMessage(dialog, Request.UPDATE,
				        SERVER_REQUEST);
				callid = sipMsg.getCallId();
				fromHeader = sipMsg.getFrom();
				toHeader = sipMsg.getTo();
				if(toHeader.getTag() == null) {
					toHeader.setTag(Integer.toHexString(remotetag.nextInt()));
				}
			}			
			request = messageFactoryImpl.createRequest(requestURI,
			        Request.UPDATE, callid, cseq, fromHeader,
			        toHeader, viaHeader, maxfwd);
			request.setMethod(Request.UPDATE);
		} else {
			SipURI fromAddress = addressFactory.createSipURI(FROM_USER_NAME,
			        LOOP_BACK_ADDRESS);
			fromAddress.setPort(FROM_PORT);
			Address fromNameAddress = addressFactory.createAddress(fromAddress);
			fromNameAddress.setDisplayName(FROM_DISPLAY_NAME);
			FromHeader fromHeader = headerFactory.createFromHeader(
			        fromNameAddress, Integer.toHexString(remotetag.nextInt()));
			SipURI toAddress = addressFactory.createSipURI(TO_USER_NAME,
			        LOOP_BACK_ADDRESS);
			toAddress.setPort(TO_PORT);
			Address toNameAddress = addressFactory.createAddress(toAddress);
			toNameAddress.setDisplayName(TO_DISPLAY_NAME);
			ToHeader toHeader = headerFactory.createToHeader(toNameAddress,
			        null);

			SipURI requestURI = addressFactory.createSipURI(TO_USER_NAME,
			        LOOP_BACK_ADDRESS);
			requestURI.setPort(TO_PORT);

			ViaHeader viaHeader = headerFactory.createViaHeader(
			        LOOP_BACK_ADDRESS, FROM_PORT, PROTOCOL, MAGIC_COOKIES
			                + Integer.toHexString(remotetag.nextInt()));

			// add via headers
			viaHeaders.add(viaHeader);

			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader((long) 1,
			        Request.UPDATE);

			// Create a new MaxForwardsHeader
			MaxForwardsHeader maxForwards = headerFactory
			        .createMaxForwardsHeader(70);
			CallIdHeader callid = headerFactory.createCallIdHeader(Integer
			        .toHexString(remotetag.nextInt())
			        + "@" + LOOP_BACK_ADDRESS);
			request = messageFactoryImpl.createRequest(requestURI,
			        Request.UPDATE, callid, cSeqHeader, fromHeader, toHeader,
			        viaHeaders, maxForwards);
		}
		return request;
	}

}
