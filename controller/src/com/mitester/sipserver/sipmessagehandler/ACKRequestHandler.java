/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ACKRequestHandler.java
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
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.MAXFORWARDS;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.PROTOCOL;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.RESPONSE_CODE;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.TO_DISPLAY_NAME;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.TO_PORT;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.TO_USER_NAME;
import gov.nist.javax.sip.header.Via;
import gov.nist.javax.sip.message.MessageFactoryImpl;
import gov.nist.javax.sip.message.SIPMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.sipserver.ProcessSIPMessage;

/**
 * CreateACKRequestHandler is used to generate the ACK Request for the the 2xx
 * and non-2xx INVITE response
 * 
 * 
 * 
 */
public class ACKRequestHandler {
	/**
	 * CreateACKRequest() method is used to generate a ACK Request for the 2xx
	 * or non-2xx INVITE response
	 * 
	 * @param invite
	 * @return ACK Request
	 * @throws NullPointerException
	 * @throws java.text.ParseException
	 * @throws InvalidArgumentException
	 * @throws SipException
	 */
	public static Request createACKRequest(SIPMessage invite,String dialog)
	        throws NullPointerException, java.text.ParseException,
	        InvalidArgumentException, SipException {
		MessageFactoryImpl messageFactoryImpl = new MessageFactoryImpl();
		Request request;
		SipFactory sipFactory = SipFactory.getInstance();
		HeaderFactory headerFactory = sipFactory.createHeaderFactory();
		AddressFactory addressFactory = sipFactory.createAddressFactory();
		List<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
		Random remotetag = new Random();
		if (invite != null) {
			Response response = (Response) invite;
			ViaHeader viaheader = (Via) response.getHeader(ViaHeader.NAME);
			// String branch = null;
			ViaHeader viaHeader = null;
			ContactHeader contact = null;

			if (response.getStatusCode() == RESPONSE_CODE) {
				contact = (ContactHeader) response
				        .getHeader(ContactHeader.NAME);
				contact.getAddress();

				viaHeader = MessageHandlerHelper.createViaHeader(
				        LOOP_BACK_ADDRESS, FROM_PORT, PROTOCOL, MAGIC_COOKIES
				                + Integer.toHexString(remotetag.nextInt()),
				        headerFactory);

			} else {

				viaHeader = MessageHandlerHelper.createViaHeader(
				        LOOP_BACK_ADDRESS, FROM_PORT, PROTOCOL, viaheader
				                .getBranch(), headerFactory);
			}

			viaHeaders.add(viaHeader);
			SipURI requestURI;
			if (contact == null) {
				requestURI = MessageHandlerHelper.createSIPURI(FROM_USER_NAME,
				        LOOP_BACK_ADDRESS, addressFactory);

			} else {
				Address a = contact.getAddress();
				String array[] = a.toString().split(" ");
				if (array.length > 1) {
					String uri = array[1];
					uri = uri.replace("<", "");
					uri = uri.replace(">", "");
					URI a1 = addressFactory.createURI(uri);
					requestURI = (SipURI) a1;
				} else {
					String uri = array[0];
					uri = uri.replace("<", "");
					uri = uri.replace(">", "");
					URI a1 = addressFactory.createURI(uri);
					requestURI = (SipURI) a1;
				}
			}
			URI requesturi = addressFactory.createURI(requestURI.toString());
			MaxForwardsHeader maxfwd = MessageHandlerHelper
			        .createMaxForwardsHeader(MAXFORWARDS, headerFactory);

			long invitecseq = invite.getCSeq().getSeqNumber();
			CSeqHeader cseq = MessageHandlerHelper.createCSeqHeader(invitecseq,
			        Request.ACK, headerFactory);
			CallIdHeader callid;
			FromHeader fromHeader;
			ToHeader toHeader;
			if(dialog != null) {
				SIPMessage sipMsg = ProcessSIPMessage.getSIPMessage(dialog, Request.ACK,
				        SERVER_REQUEST);
				callid = sipMsg.getCallId();
				fromHeader= sipMsg.getFrom();
				toHeader = sipMsg.getTo();
			} else {
				callid = invite.getCallId();
				fromHeader= invite.getFrom();
				toHeader = invite.getTo();
			}
			request = MessageHandlerHelper.createRequest(requesturi,
			        Request.ACK,callid , cseq,fromHeader ,
			        toHeader , viaHeaders, maxfwd, messageFactoryImpl);

			request.setMethod(Request.ACK);
		} else {

			SipURI fromAddress = MessageHandlerHelper.createSIPURI(
			        FROM_USER_NAME, LOOP_BACK_ADDRESS, addressFactory);

			fromAddress.setPort(FROM_PORT);
			Address fromNameAddress = MessageHandlerHelper.createAddress(
			        fromAddress, addressFactory);

			fromNameAddress.setDisplayName(FROM_DISPLAY_NAME);
			FromHeader fromHeader = MessageHandlerHelper.createFromHeader(
			        fromNameAddress, Integer.toHexString(remotetag.nextInt()),
			        headerFactory);

			SipURI toAddress = MessageHandlerHelper.createSIPURI(TO_USER_NAME,
			        LOOP_BACK_ADDRESS, addressFactory);

			toAddress.setPort(TO_PORT);
			Address toNameAddress = MessageHandlerHelper.createAddress(
			        toAddress, addressFactory);

			toNameAddress.setDisplayName(TO_DISPLAY_NAME);
			ToHeader toHeader = MessageHandlerHelper.createToHeader(
			        toNameAddress, null, headerFactory);

			SipURI requestURI = MessageHandlerHelper.createSIPURI(TO_USER_NAME,
			        LOOP_BACK_ADDRESS, addressFactory);
			requestURI.setPort(TO_PORT);

			ViaHeader viaHeader = MessageHandlerHelper.createViaHeader(
			        LOOP_BACK_ADDRESS, FROM_PORT, PROTOCOL, MAGIC_COOKIES
			                + Integer.toHexString(remotetag.nextInt()),
			        headerFactory);

			// add via headers
			viaHeaders.add(viaHeader);
			CSeqHeader cSeqHeader = MessageHandlerHelper.createCSeqHeader(1,
			        Request.ACK, headerFactory);

			// Create a new MaxForwardsHeader
			MaxForwardsHeader maxForwards = MessageHandlerHelper
			        .createMaxForwardsHeader(MAXFORWARDS, headerFactory);
			CallIdHeader callid = MessageHandlerHelper.createCallIdHeader(
			        Integer.toHexString(remotetag.nextInt()),
			        LOOP_BACK_ADDRESS, headerFactory);

			request = MessageHandlerHelper.createRequest(requestURI,
			        Request.ACK, callid, cSeqHeader, fromHeader, toHeader,
			        viaHeaders, maxForwards, messageFactoryImpl);

		}

		return request;
	}
}
