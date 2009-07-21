/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: AcceptEncodingHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.ACCEPTENCODING;
import gov.nist.javax.sip.header.AcceptEncoding;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.AcceptEncodingHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * AcceptEncodingHeaderHandler is used to add and remove the SIP Accept-Encoding
 * Header to the SIP Message
 * 
 * RFC 3261 says,
 * 
 * 20.2 Accept-Encoding
 * 
 * The Accept-Encoding header field is similar to Accept, but restricts the
 * content-codings [H3.5] that are acceptable in the response. See [H14.3]. The
 * semantics in SIP are identical to those defined in [H14.3]. An empty
 * Accept-Encoding header field is permissible. It is equivalent to
 * Accept-Encoding: identity, that is, only the identity encoding, meaning no
 * encoding, is permissible. If no Accept-Encoding header field is present, the
 * server SHOULD assume a default value of identity. This differs slightly from
 * the HTTP definition, which indicates that when not present, any encoding can
 * be used, but the identity encoding is preferred.
 * 
 * 
 * 
 */

public class AcceptEncodingHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(AcceptEncodingHeaderHandler.class.getName());

	/**
	 * addAcceptEncodingHeader is used to add accept-encoding header to the sip
	 * message
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
	public static AcceptEncodingHeader createAcceptEncodingHeader(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {
		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory hf = factory.createHeaderFactory();
		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = ACCEPTENCODING;
		AcceptEncodingHeader acceptEncodingHeader = hf
		        .createAcceptEncodingHeader(hvalue);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			acceptEncodingHeader.setParameter(paramname, value);
		}

		return acceptEncodingHeader;
	}

	/**
	 * removeAcceptEncodingHeader is used to remove the accept-encoding header
	 * from the sip message
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
	public static SIPMessage removeAcceptEncodingHeader(Header header,
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

		AcceptEncodingHeader acceptEncoding = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			acceptEncoding = (AcceptEncodingHeader) response
			        .getHeader(AcceptEncoding.ACCEPT_ENCODING);
		else
			acceptEncoding = (AcceptEncodingHeader) request
			        .getHeader(AcceptEncoding.ACCEPT_ENCODING);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			acceptEncoding.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(AcceptEncoding.ACCEPT_ENCODING);
			else
				request.removeHeader(AcceptEncoding.ACCEPT_ENCODING);
		}

		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}
}
