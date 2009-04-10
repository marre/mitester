/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SecurityClientHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.SECURITY;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.ims.SecurityClient;
import gov.nist.javax.sip.header.ims.SecurityClientHeader;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * SecurityClientHeaderHandler is used to create and remvoe Security-Client
 * header
 * 
 * RFC 3329 says,
 * 
 * Mechanism-name This token identifies the security mechanism supported by the
 * client, when it appears in a Security-Client header field; or by the server,
 * when it appears in a Security-Server or in a Security-Verify header field.
 * The mechanism-name tokens are registered with the IANA. This specification
 * defines four values: "tls" for TLS [3]. "digest" for HTTP Digest [4].
 * "ipsec-ike" for IPsec with IKE [2]. "ipsec-man" for manually keyed IPsec
 * without IKE. Preference The "q" value indicates a relative preference for the
 * particular mechanism. The higher the value the more preferred the mechanism
 * is. All the security mechanisms MUST have different "q" values. It is an
 * error to provide two mechanisms with the same "q" value.
 * 
 * Digest-algorithm This optional parameter is defined here only for HTTP Digest
 * [4] in order to prevent the bidding-down attack for the HTTP Digest algorithm
 * parameter. The content of the field may have same values as defined in [4]
 * for the "algorithm" field.
 * 
 * Digest-qop This optional parameter is defined here only for HTTP Digest [4]
 * in order to prevent the bidding-down attack for the HTTP Digest qop
 * parameter. The content of the field may have same values as defined in [4]
 * for the "qop" field.
 * 
 * Digest-verify This optional parameter is defined here only for HTTP Digest
 * [4] in order to prevent the bidding-down attack for the SIP security
 * mechanism agreement (this document). The content of the field is counted
 * exactly the same way as "request-digest" in [4] except that the
 * Security-Server header field is included in the A2 parameter. If the "qop"
 * directive's value is "auth" or is unspecified, then A2 is: A2 = Method ":"
 * digest-uri-value ":" security-server If the "qop" value is "auth-int", then
 * A2 is: A2 = Method ":" digest-uri-value ":" H(entity-body) ":"
 * security-server
 * 
 * All linear white spaces in the Security-Server header field MUST be replaced
 * by a single SP before calculating or interpreting the digest-verify
 * parameter. Method, digest-uri- value, entity-body, and any other HTTP Digest
 * parameter are as specified in [4]. Note that this specification does not
 * introduce any extension or change to HTTP Digest [4]. This specification only
 * re-uses the existing HTTP Digest mechanisms to protect the negotiation of
 * security mechanisms between SIP entities.
 * 
 * 
 * 
 * 
 */
public class SecurityClientHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(SecurityClientHeaderHandler.class.getName());

	/**
	 * addSecurityClientHeader is used to add Security-Client header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return Security-Client header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static SecurityClientHeader createSecurityClientHeader(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		SipFactory factory = SipFactory.getInstance();
		HeaderFactoryImpl hfimpl = (HeaderFactoryImpl) factory
		        .createHeaderFactory();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		String hvalue = null;
		// ,headerName = null;
		// headerName = headerNew.getName();
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = SECURITY;

		SecurityClientHeader header = hfimpl.createSecurityClientHeader();
		if (hvalue.length() != 0)
			header.setSecurityMechanism(hvalue);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			header.setParameter(paramname, value);
		}

		return header;
	}

	/**
	 * removeSecurityClientHeader is used to remove Security-Client header
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
	public static SIPMessage removeSecurityClientHeader(Header header,
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

		SecurityClientHeader security = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			security = (SecurityClientHeader) response
			        .getHeader(SecurityClientHeader.NAME);
		else
			security = (SecurityClientHeader) request
			        .getHeader(SecurityClientHeader.NAME);

		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			security.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(SecurityClient.NAME);
			else
				request.removeHeader(SecurityClient.NAME);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}
}
