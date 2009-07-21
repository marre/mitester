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
 * Package 				License 										    Details
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
package com.mitester.sipserver.sipmessagehandler;

import static com.mitester.sipserver.SipServerConstants.SERVER_RESPONSE;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.FROM_DISPLAY_NAME;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.FROM_PORT;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.LOOP_BACK_ADDRESS;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.FROM_USER_NAME;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.MAGIC_COOKIES;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.MAXFORWARDS;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.PROTOCOL;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.TO_DISPLAY_NAME;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.TO_PORT;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.TO_USER_NAME;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.TRANSPORT;
import gov.nist.javax.sip.message.MessageFactoryImpl;
import gov.nist.javax.sip.message.SIPMessage;

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
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.apache.log4j.Logger;

import com.mitester.sipserver.ProcessSIPMessage;
import static com.mitester.sipserver.SipServerConstants.SERVER_REQUEST;
import com.mitester.utility.MiTesterLog;

/**
 * Used to generate the SIP Response with the status code
 * 
 */
public class ResponseHandler {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(ResponseHandler.class.getName());
	private static boolean isResponse = false;

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
			SIPMessage sipMessage, String dialog) throws NullPointerException,
			java.text.ParseException, PeerUnavailableException,
			InvalidArgumentException {
		LOGGER.info("Creation of SIP Response is started");
		MessageFactoryImpl messageFactoryImpl = new MessageFactoryImpl();
		String tag = null;
		Response response;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactory = factory.createHeaderFactory();
		AddressFactory addressFactory = factory.createAddressFactory();
		Random remotetag = new Random();
		Request request = null;
		if (dialog != null) {
			SIPMessage sipMsg = ProcessSIPMessage.getSIPMessage(dialog, method,
					SERVER_RESPONSE);
			request = (Request) sipMsg;
			LOGGER.info("Creating SIP Response with the " + dialog + " dialog");
		} else {
			if (sipMessage.getFirstLine().startsWith("SIP/2.0")) {
				SIPMessage sipMsg = ProcessSIPMessage.getSIPMessage(method,
						method, SERVER_REQUEST);
				String toTag = sipMessage.getToTag();
				sipMessage = sipMsg;
				sipMessage.setToTag(toTag);
			}
			request = (Request) sipMessage;
		}
		try {
			response = messageFactoryImpl.createResponse(statusCode, request);
		} catch (IllegalArgumentException e) {
			response = messageFactoryImpl.createResponse(200, request);
			isResponse = true;
		}

		response.removeHeader(MaxForwardsHeader.NAME);

		SipURI contactURI = MessageHandlerHelper.createSIPURI(FROM_USER_NAME,
				LOOP_BACK_ADDRESS, addressFactory);

		contactURI.setPort(5070);

		contactURI.setParameter(TRANSPORT, PROTOCOL);

		Address contactAddress = MessageHandlerHelper.createAddress(contactURI,
				addressFactory);
		LOGGER.info("Adding display name to the contact header");
		contactAddress.setDisplayName(FROM_USER_NAME);

		ContactHeader contactHeader = MessageHandlerHelper.createContactHeader(
				contactAddress, headerFactory);

		response.addHeader(contactHeader);
		ToHeader to = (ToHeader) response.getHeader(ToHeader.NAME);
		if (method.equals(Request.REGISTER)) {
			if (statusCode == 200) {
				tag = Integer.toHexString(remotetag.nextInt());
				if (to.getTag() == null)
					to.setTag(tag);
				// ExpiresHeader e = request.getExpires();
				ExpiresHeader e = sipMessage.getExpires();
				response.setExpires(e);
				LOGGER
						.info("Adding Expires to the SIP Response");
			} else {
				tag = Integer.toHexString(remotetag.nextInt());
				if (to.getTag() == null)
					to.setTag(tag);
			}
		}

		if (method.equals(Request.SUBSCRIBE)) {
			if (statusCode == 200) {
				tag = Integer.toHexString(remotetag.nextInt());
				if (to.getTag() == null)
					to.setTag(tag);
				// ExpiresHeader e = request.getExpires();
				ExpiresHeader e = sipMessage.getExpires();
				response.setExpires(e);
			} else {
				tag = Integer.toHexString(remotetag.nextInt());
				if (to.getTag() == null)
					to.setTag(tag);
			}
		}

		if (method.equals(Request.INVITE)) {
			if (statusCode == 200) {
				tag = Integer.toHexString(remotetag.nextInt());
				if (to.getTag() == null)
					to.setTag(tag);

			} else {
				tag = Integer.toHexString(remotetag.nextInt());
				if (to.getTag() == null)
					to.setTag(tag);
			}
		}
		
		if (response != null)
			LOGGER.info("SIP response is created successfully");
		
		return response;
	}

	public static SIPMessage createRequest(String method)
			throws NullPointerException, java.text.ParseException,
			PeerUnavailableException, InvalidArgumentException {

		LOGGER.info("Creating " + method + " started");
		Request request;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactory = factory.createHeaderFactory();
		AddressFactory addressFactory = factory.createAddressFactory();
		MessageFactoryImpl messageFactoryImpl = new MessageFactoryImpl();
		List<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
		Random remotetag = new Random();
		CallIdHeader callid;
		SipURI fromAddress;
		Address fromNameAddress;
		FromHeader fromHeader;
		SipURI toAddress;
		Address toNameAddress;
		ToHeader toHeader;

		fromAddress = MessageHandlerHelper.createSIPURI(FROM_USER_NAME,
				LOOP_BACK_ADDRESS, addressFactory);
		fromAddress.setPort(FROM_PORT);
		fromNameAddress = MessageHandlerHelper.createAddress(fromAddress,
				addressFactory);
		fromNameAddress.setDisplayName(FROM_DISPLAY_NAME);
		// create from header
		fromHeader = MessageHandlerHelper.createFromHeader(fromNameAddress,
				Integer.toHexString(remotetag.nextInt()), headerFactory);

		toAddress = MessageHandlerHelper.createSIPURI(TO_USER_NAME,
				LOOP_BACK_ADDRESS, addressFactory);
		toAddress.setPort(TO_PORT);
		toNameAddress = MessageHandlerHelper.createAddress(toAddress,
				addressFactory);
		toNameAddress.setDisplayName(TO_DISPLAY_NAME);
		// create to Header
		toHeader = MessageHandlerHelper.createToHeader(toNameAddress, null,
				headerFactory);

		// create call-ID
		callid = MessageHandlerHelper.createCallIdHeader(Integer
				.toHexString(remotetag.nextInt()), LOOP_BACK_ADDRESS,
				headerFactory);

		SipURI requestURI = MessageHandlerHelper.createSIPURI(TO_USER_NAME,
				LOOP_BACK_ADDRESS, addressFactory);
		requestURI.setPort(TO_PORT);

		// create Vi aheader
		ViaHeader viaHeader = MessageHandlerHelper.createViaHeader(
				LOOP_BACK_ADDRESS, FROM_PORT, PROTOCOL, MAGIC_COOKIES
						+ Integer.toHexString(remotetag.nextInt()),
				headerFactory);

		// add via headers
		viaHeaders.add(viaHeader);

		CSeqHeader cSeqHeader = MessageHandlerHelper.createCSeqHeader(1,
				method, headerFactory);

		// Create a new MaxForwardsHeader
		MaxForwardsHeader maxForwards = MessageHandlerHelper
				.createMaxForwardsHeader(MAXFORWARDS, headerFactory);

		// create request
		request = MessageHandlerHelper.createRequest(requestURI, method,
				callid, cSeqHeader, fromHeader, toHeader, viaHeaders,
				maxForwards, messageFactoryImpl);
		LOGGER.info("Creating " + method + " ended");
		SIPMessage msg = (SIPMessage) request;
		return msg;
	}

	public static boolean getResponse() {
		return isResponse;
	}

	public static boolean setResponse() {
		return isResponse = false;
	}

}