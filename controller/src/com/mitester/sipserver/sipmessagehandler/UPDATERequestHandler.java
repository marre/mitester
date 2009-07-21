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
 * Package 						License 										Details
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
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.AT;

import gov.nist.javax.sip.header.ViaList;
import gov.nist.javax.sip.message.MessageFactoryImpl;
import gov.nist.javax.sip.message.SIPMessage;

import java.util.ArrayList;
import java.util.List;
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

import org.apache.log4j.Logger;

import com.mitester.sipserver.ProcessSIPMessage;
import com.mitester.utility.MiTesterLog;

/**
 * A method to update the on-going/early session with the updated SDP
 * parameters.
 */
public class UPDATERequestHandler {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(UPDATERequestHandler.class.getName());

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
	public static Request createUPDATERequest(SIPMessage sipmsg, String dialog)
			throws NullPointerException, SipException,
			java.text.ParseException, InvalidArgumentException,
			PeerUnavailableException {
		LOGGER.info("Creation of UPDATE Request is started");
		MessageFactoryImpl messageFactoryImpl = new MessageFactoryImpl();
		Request request;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactory = factory.createHeaderFactory();
		AddressFactory addressFactory = factory.createAddressFactory();
		List<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
		Random remotetag = new Random();
		if (sipmsg != null) {
			ViaList via = sipmsg.getViaHeaders();
			String viaValue = via.getValue();
			int index = viaValue.indexOf("=");
			String branch = viaValue.substring(index + 1, viaValue.length());
			ViaHeader viaHeaderBye = MessageHandlerHelper.createViaHeader(
					LOOP_BACK_ADDRESS, FROM_PORT, PROTOCOL, branch, headerFactory);

			viaHeaders.add(viaHeaderBye);
			SipURI requestURI = MessageHandlerHelper.createSIPURI(FROM_USER_NAME,
					LOOP_BACK_ADDRESS, addressFactory);
			MaxForwardsHeader maxfwd = MessageHandlerHelper
					.createMaxForwardsHeader(70, headerFactory);
			long invitecseq = sipmsg.getCSeq().getSeqNumber();
			CSeqHeader cseq = MessageHandlerHelper.createCSeqHeader(
					invitecseq + 1, Request.UPDATE, headerFactory);
			ToHeader toHeader;
			FromHeader fromHeader;
			CallIdHeader callid;
			if (dialog == null) {
				callid = sipmsg.getCallId();
			
				Address from = sipmsg.getFrom().getAddress();
				String fromTag = sipmsg.getFromTag();
				Address to = sipmsg.getTo().getAddress();
				String toTag = sipmsg.getToTag();
				fromHeader = headerFactory.createFromHeader(to, toTag);
				toHeader = headerFactory.createToHeader(from, fromTag);
				} else {
				SIPMessage sipMsg = ProcessSIPMessage.getSIPMessage(dialog,
						Request.UPDATE, SERVER_REQUEST);
				callid = sipMsg.getCallId();
				fromHeader = sipMsg.getFrom();
				toHeader = sipMsg.getTo();
				if (toHeader.getTag() == null) {
					toHeader.setTag(Integer.toHexString(remotetag.nextInt()));
				}				
			}
			request = MessageHandlerHelper.createRequest(requestURI,
					Request.UPDATE, callid, cseq, fromHeader, toHeader,
					viaHeaders, maxfwd, messageFactoryImpl);
			request.setMethod(Request.UPDATE);
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

			CSeqHeader cSeqHeader = MessageHandlerHelper.createCSeqHeader(
					(long) 1, Request.UPDATE, headerFactory);

			// Create a new MaxForwardsHeader
			MaxForwardsHeader maxForwards = MessageHandlerHelper
					.createMaxForwardsHeader(70, headerFactory);
			CallIdHeader callid = headerFactory.createCallIdHeader(Integer
					.toHexString(remotetag.nextInt())
					+ AT + LOOP_BACK_ADDRESS);
			LOGGER.info("Created Call-ID Header successflly");
			request = MessageHandlerHelper.createRequest(requestURI,
					Request.UPDATE, callid, cSeqHeader, fromHeader, toHeader,
					viaHeaders, maxForwards, messageFactoryImpl);
		}
		LOGGER.info("Creation of UPDATE Request is ended");
		return request;
	}

}
