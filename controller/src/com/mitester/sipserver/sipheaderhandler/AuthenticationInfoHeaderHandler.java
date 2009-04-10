/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: AuthenticationInfoHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.AUTHENTICATIONINFO;
import gov.nist.javax.sip.header.AuthenticationInfo;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.AuthenticationInfoHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * AuthenticationInfoHeaderHandler is used to add and remove the
 * AuthenticationInfo header
 * 
 * RFC 3261 says, 20.6 Authentication-Info The Authentication-Info header field
 * provides for mutual authentication with HTTP Digest.A UAS MAY include this
 * header field in a 2xx response to a request that was successfully
 * authenticated using digest based on the Authorization header field. Syntax
 * and semantics follow those specified in RFC 2617 [17].
 * 
 * RFC 2617 says, 3.2.3 The Authentication-Info Header The Authentication-Info
 * header is used by the server to communicate some information regarding the
 * successful authentication in the response. The value of the nextnonce
 * directive is the nonce the server wishes the client to use for a future
 * authentication response. The server may send the Authentication-Info header
 * with a nextnonce field as a means of implementing one-time or otherwise
 * changing nonces. If the nextnonce field is present the client SHOULD use it
 * when constructing the Authorization header for its next request. Failure of
 * the client to do so may result in a request to re-authenticate from the
 * server with the "stale=TRUE". Server implementations should carefully
 * consider the performance implications of the use of this mechanism; pipelined
 * requests will not be possible if every response includes a nextnonce
 * directive that must be used on the next request received by the
 * server.Consideration should be given to the performance vs. security
 * tradeoffs of allowing an old nonce value to be used for a limited time to
 * permit request pipelining. Use of the nonce-count can retain most of the
 * security advantages of a new server nonce without the deleterious affects on
 * pipelining.
 * 
 * message-qop Indicates the "quality of protection" options applied to the
 * response by the server. The value "auth" indicates authentication; the value
 * "auth-int" indicates authentication with integrity protection. The server
 * SHOULD use the same value for the message- qop directive in the response as
 * was sent by the client in the corresponding request.
 * 
 * The optional response digest in the "response-auth" directive supports mutual
 * authentication -- the server proves that it knows the user's secret, and with
 * qop=auth-int also provides limited integrity protection of the response. The
 * "response-digest" value is calculated as for the "request-digest" in the
 * Authorization header, except that if "qop=auth" or is not specified in the
 * Authorization header for the request, A2 is A2 = ":" digest-uri-value and if
 * "qop=auth-int", then A2 is A2 = ":" digest-uri-value ":" H(entity-body) where
 * "digest-uri-value" is the value of the "uri" directive on the Authorization
 * header in the request. The "cnonce-value" and "nc- value" MUST be the ones
 * for the client request to which this message is the response. The
 * "response-auth", "cnonce", and "nonce-count" directives MUST BE present if
 * "qop=auth" or "qop=auth-int" is specified. The Authentication-Info header is
 * allowed in the trailer of an HTTP message transferred via chunked
 * transfer-coding.
 * 
 * 
 * 
 */
public class AuthenticationInfoHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(AuthenticationInfoHeaderHandler.class.getName());

	/**
	 * addAuthenticationInfo is used to add a Authentication Info header to sip
	 * message
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
	public static AuthenticationInfoHeader createAuthenticationInfo(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {
		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");
		AuthenticationInfoHeader authenticationinfo = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);

		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = AUTHENTICATIONINFO;
		authenticationinfo = headerFactroy
		        .createAuthenticationInfoHeader(hvalue);

		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			authenticationinfo.setParameter(paramname, value);
		}

		return authenticationinfo;
	}

	/**
	 * removeAuthenticationInfo is used to remove the Authentication-Info header
	 * from the sip message
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
	public static SIPMessage removeAuthenticationInfo(Header header,
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

		AuthenticationInfoHeader authenticationinfo = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			authenticationinfo = (AuthenticationInfoHeader) response
			        .getHeader(AuthenticationInfo.AUTHENTICATION_INFO);
		else
			authenticationinfo = (AuthenticationInfoHeader) request
			        .getHeader(AuthenticationInfo.AUTHENTICATION_INFO);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			authenticationinfo.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(AuthenticationInfo.AUTHENTICATION_INFO);
			else
				request.removeHeader(AuthenticationInfo.AUTHENTICATION_INFO);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}

}
