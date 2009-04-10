/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: FromHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.FROM;
import gov.nist.javax.sip.header.From;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * FromHeaderHandler is used to add and remove the From header
 * 
 * RFC 3261 says,
 * 
 * 20.20 From
 * 
 * The From header field indicates the initiator of the request. This may be
 * different from the initiator of the dialog. Requests sent by the callee to
 * the caller use the callee's address in the From header field. The optional
 * "display-name" is meant to be rendered by a human user interface. A system
 * SHOULD use the display name "Anonymous" if the identity of the client is to
 * remain hidden. Even if the "display- name" is empty, the "name-addr" form
 * MUST be used if the "addr-spec" contains a comma, question mark, or
 * semicolon. Syntax issues are discussed in Section 7.3.1. Two From header
 * fields are equivalent if their URIs match, and their parameters match.
 * Extension parameters in one header field, not present in the other are
 * ignored for the purposes of comparison. This means that the display name and
 * presence or absence of angle brackets do not affect matching. See Section
 * 20.10 for the rules for parsing a display name, URI and URI parameters, and
 * header field parameters. The compact form of the From header field is f.
 * 
 * 
 * 
 */

public class FromHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(FromHeaderHandler.class.getName());

	/**
	 * createFromHeader is used to create from header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static FromHeader createFromHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		Random remotetag = new Random();
		String tag = null;

		FromHeader fromHeader = null;
		Address fromNameAddress = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		AddressFactory addressFactory = factory.createAddressFactory();
		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null) {
			hvalue = FROM;
			tag = Integer.toHexString(remotetag.nextInt());
		}

		fromNameAddress = addressFactory.createAddress(hvalue);
		fromHeader = headerFactroy.createFromHeader(fromNameAddress, null);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			if (paramname.equalsIgnoreCase("display-name"))
				fromNameAddress.setDisplayName(value);
			else
				fromHeader.setParameter(paramname, value);
		}

		if (tag != null)
			fromHeader.setTag(tag);

		return fromHeader;
	}

	/**
	 * removeFromHeader is used to remove from header from the sip message
	 * 
	 * @param header
	 * @param sipMessage
	 * @param type
	 * @return
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */
	public static SIPMessage removeFromHeader(Header header,
	        SIPMessage sipMessage, String type) throws SipException,
	        ParseException, InvalidArgumentException, NullPointerException,
	        java.lang.IllegalArgumentException {
		Request request = null;
		Response response = null;
		SIPMessage returnsipMessage = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			response = (Response) sipMessage;
		else
			request = (Request) sipMessage;

		FromHeader from = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			from = (FromHeader) response.getHeader(FromHeader.NAME);
		else
			from = (FromHeader) request.getHeader(FromHeader.NAME);

		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			from.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(From.FROM);
			else
				request.removeHeader(From.FROM);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}

}
