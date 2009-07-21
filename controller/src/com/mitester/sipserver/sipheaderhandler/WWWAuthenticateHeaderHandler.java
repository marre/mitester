/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: WWWAuthenticateHeaderHandler.java
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
package com.mitester.sipserver.sipheaderhandler;

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.WWWAUTHENTICATE;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.WWWAuthenticate;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * WWWAuthenticateHeaderHandler is used to create and remove teh
 * WWW-AuthenticateHeader header
 * 
 * RFC 3261 says,
 * 
 * 20.44 WWW-Authenticate
 * 
 * A WWW-Authenticate header field value contains an authentication challenge.
 * See Section 22.2 for further details on its usage.
 * 
 * 22.2 User-to-User Authentication
 * 
 * When a UAS receives a request from a UAC, the UAS MAY authenticate the
 * originator before the request is processed. If no credentials (in the
 * Authorization header field) are provided in the request, the UAS can
 * challenge the originator to provide credentials by rejecting the request with
 * a 401 (Unauthorized) status code. The WWW-Authenticate response-header field
 * MUST be included in 401 (Unauthorized) response messages. The field value
 * consists of at least one challenge that indicates the authentication
 * scheme(s) and parameters applicable to the realm. When the originating UAC
 * receives the 401 (Unauthorized), it SHOULD, if it is able, re-originate the
 * request with the proper credentials. The UAC may require input from the
 * originating user before proceeding. Once authentication credentials have been
 * supplied (either directly by the user, or discovered in an internal keyring),
 * UAs SHOULD cache the credentials for a given value of the To header field and
 * "realm" and attempt to re-use these values on the next request for that
 * destination. UAs MAY cache credentials in any way they would like. If no
 * credentials for a realm can be located, UACs MAY attempt to retry the request
 * with a username of "anonymous" and no password (a password of ""). Once
 * credentials have been located, any UA that wishes to authenticate itself with
 * a UAS or registrar -- usually, but not necessarily, after receiving a 401
 * (Unauthorized) response -- MAY do so by including an Authorization header
 * field with the request. The Authorization field value consists of credentials
 * containing the authentication information of the UA for the realm of the
 * resource being requested as well as parameters required in support of
 * authentication and replay protection.
 * 
 * 
 * 
 */
public class WWWAuthenticateHeaderHandler {
	private static final Logger LOGGER = MiTesterLog
	        .getLogger(WWWAuthenticateHeaderHandler.class.getName());

	/**
	 * createWWWAuthenticateHeader is used to add WWW-AuthenticateHeader header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return WWW-AuthenticateHeader header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static WWWAuthenticateHeader createWWWAuthenticateHeader(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		WWWAuthenticateHeader w = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		String hvalue = null;
		// String headerName = null;
		// headerName = headerNew.getName();
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = WWWAUTHENTICATE;
		if (hvalue.length() != 0)
			w = headerFactroy.createWWWAuthenticateHeader(hvalue);

		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			w.setParameter(paramname, value);
		}

		return w;
	}

	/**
	 * removeWWWAuthenticate is used to remove the WWW-AuthenticateHeader header
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
	public static SIPMessage removeWWWAuthenticate(Header header,
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

		WWWAuthenticateHeader wwwauth;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			wwwauth = (WWWAuthenticateHeader) response
			        .getHeader(WWWAuthenticateHeader.NAME);
		else
			wwwauth = (WWWAuthenticateHeader) request
			        .getHeader(WWWAuthenticateHeader.NAME);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			wwwauth.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(WWWAuthenticate.WWW_AUTHENTICATE);
			else
				request.removeHeader(WWWAuthenticate.WWW_AUTHENTICATE);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}
}
