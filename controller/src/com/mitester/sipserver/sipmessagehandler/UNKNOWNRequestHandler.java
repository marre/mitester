/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: UdpCommn.java
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
 * Package 						License 											Details
 *---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Jain SIP stack 				NIST-CONDITIONS-OF-USE 						        https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 						The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * JNetStreamStandalone lib     GNU Library or LGPL			     					http://sourceforge.net/projects/jnetstream/
 * 
 */
package com.mitester.sipserver.sipmessagehandler;

import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.FROM_DISPLAY_NAME;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.FROM_PORT;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.FROM_USER_NAME;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.LOOP_BACK_ADDRESS;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.MAGIC_COOKIES;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.MAXFORWARDS;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.PROTOCOL;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.TO_DISPLAY_NAME;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.TO_PORT;
import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.TO_USER_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gov.nist.javax.sip.message.MessageFactoryImpl;


import javax.sip.InvalidArgumentException;
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

import com.mitester.utility.MiTesterLog;

public class UNKNOWNRequestHandler {
	private static final Logger LOGGER = MiTesterLog
			.getLogger(UNKNOWNRequestHandler.class.getName());

	public static Request createUNKNOWNRequest(String methodname)
			throws NullPointerException, java.text.ParseException,
			InvalidArgumentException, SipException {

		LOGGER.info("Generation of UNKNOWN Request started");
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
				methodname, headerFactory);

		// Create a new MaxForwardsHeader
		MaxForwardsHeader maxForwards = MessageHandlerHelper
				.createMaxForwardsHeader(MAXFORWARDS, headerFactory);

		// create request
		request = MessageHandlerHelper.createRequest(requestURI, methodname,
				callid, cSeqHeader, fromHeader, toHeader, viaHeaders,
				maxForwards, messageFactoryImpl);
		LOGGER.info("Generation of UNKNOWN Request ended");
		return request;

	}
}
