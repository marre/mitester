/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ProxyAuthorizationHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.PROXYAUTHORIZATION;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.ProxyAuthorization;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ProxyAuthorizationHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * ProxyAuthorizationHeaderHandler is used to add and remove the
 * Proxy-Authorization header
 * 
 * RFC 3261 says,
 * 
 * 20.28 Proxy-Authorization
 * 
 * The Proxy-Authorization header field allows the client to identify itself (or
 * its user) to a proxy that requires authentication. A Proxy-Authorization
 * field value consists of credentials containing the authentication information
 * of the user agent for the proxy and/or realm of the resource being requested.
 * See Section 22.3 for a definition of the usage of this header field. This
 * header field, along with Authorization, breaks the general rules about
 * multiple header field names. Although not a comma-separated list, this header
 * field name may be present multiple times, and MUST NOT be combined into a
 * single header line using the usual rules described in Section 7.3.1.
 * 
 * 
 * 
 */

public class ProxyAuthorizationHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(ProxyAuthorizationHeaderHandler.class.getName());

	/**
	 * createProxyAuthorizationHeader is used to create Proxy-Authenticate
	 * header
	 * 
	 * @param headerNew
	 * @return
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static ProxyAuthorizationHeader createProxyAuthorizationHeader(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = PROXYAUTHORIZATION;
		ProxyAuthorizationHeader proxyauthorization = null;

		proxyauthorization = headerFactroy
		        .createProxyAuthorizationHeader(hvalue);

		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			if (paramname.equals("qop"))
				proxyauthorization.setQop(value);
			else
				proxyauthorization.setParameter(paramname, value);
		}

		return proxyauthorization;
	}

	/**
	 * removeProxyAuthorizationHeader is used to remove Proxy-Authorization
	 * header
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
	public static SIPMessage removeProxyAuthorizationHeader(Header header,
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
		ProxyAuthorizationHeader proxyauth;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			proxyauth = (ProxyAuthorizationHeader) response
			        .getHeader(ProxyAuthorizationHeader.NAME);
		else
			proxyauth = (ProxyAuthorizationHeader) response
			        .getHeader(ProxyAuthorizationHeader.NAME);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			proxyauth.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(ProxyAuthorization.PROXY_AUTHORIZATION);
			else
				request.removeHeader(ProxyAuthorization.PROXY_AUTHORIZATION);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;

	}
}
