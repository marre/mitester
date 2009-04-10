/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ReferredByHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.REFERREDBY;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.extensions.ReferredByHeader;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * ReferredByHeaderHandler is used to create and remove the referrred by header
 * 
 * RFC 3892 says,
 * 
 * 3. The Referred-By Header Field
 * 
 * Referred-By is a request header field as defined by [5]. It can appear in any
 * request. It carries a SIP URI representing the identity of the referrer and,
 * optionally, the Content-ID of a body part (the Referred-By token) that
 * provides a more secure statement of that identity. Since the Content-ID
 * appears as a SIP header parameter value which must conform to the expansion
 * of the gen-value defined in [5], this grammar produces values in the
 * intersection of the expansions of gen-value and msg-id from [9]. The
 * double-quotes surrounding the sip-clean-msg-id MUST be replaced with left and
 * right angle brackets to derive the Content-ID used in the message's MIME
 * body. For example, Referred-By:
 * sip:r@ref.example;cid="2UWQFN309shb3@ref.example" indicates the token is in
 * the body part containing Content-ID: <2UWQFN309shb3@ref.example> If the
 * referrer-uri contains a comma, question mark, or semicolon, (for example, if
 * it contains URI parameters) the URI MUST be enclosed in angle brackets (< and
 * >). Any URI parameters are contained within these brackets. If the URI is not
 * enclosed in angle brackets, any semicolon-delimited parameters are
 * header-parameters, not URI parameters. The Referred-By header field MAY
 * appear in any SIP request, but is meaningless for ACK and CANCEL. Proxies do
 * not need to be able to read Referred-By header field values and MUST NOT
 * remove or modify them.
 * 
 * 
 * 
 */
public class ReferredByHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(ReferredByHeaderHandler.class.getName());

	/**
	 * addReferredByHeader is used to add ReferredBy header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return Accept-Encoding Header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static ReferredByHeader addReferredByHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		ReferredByHeader referby = null;
		Address referedby = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactoryImpl hfimpl = (HeaderFactoryImpl) factory
		        .createHeaderFactory();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		AddressFactory addressFactory = factory.createAddressFactory();
		String hvalue = null;

		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = REFERREDBY;

		referedby = addressFactory.createAddress(hvalue);
		referby = hfimpl.createReferredByHeader(referedby);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			if (paramname.equalsIgnoreCase("display-name")) {
				referedby.setDisplayName(value);
			} else {
				referby.setParameter(paramname, value);
			}
		}

		return referby;
	}

	/**
	 * removeReferredByHeader is used to remove the ReferredBy header
	 * 
	 * @param name
	 * @param removeParams
	 * @param sipMessage
	 * @param type
	 * @return sip message
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */
	public static SIPMessage removeReferredByHeader(Header header,
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
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			response.removeHeader(ReferredByHeader.NAME);
		else
			request.removeHeader(ReferredByHeader.NAME);
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}

}
