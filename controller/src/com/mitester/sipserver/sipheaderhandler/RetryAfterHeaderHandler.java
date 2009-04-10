/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: RetryAfterHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.RETRYAFTER;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.RetryAfter;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.header.RetryAfterHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * RetryAfterHeaderHandler is used to craete and remove Retry-After header
 * 
 * RFC 3261 says,
 * 
 * 20.33 Retry-After
 * 
 * The Retry-After header field can be used with a 500 (Server Internal Error)
 * or 503 (Service Unavailable) response to indicate how long the service is
 * expected to be unavailable to the requesting client and with a 404 (Not
 * Found), 413 (Request Entity Too Large), 480 (Temporarily Unavailable), 486
 * (Busy Here), 600 (Busy), or 603 (Decline) response to indicate when the
 * called party anticipates being available again. The value of this field is a
 * positive integer number of seconds (in decimal) after the time of the
 * response. An optional comment can be used to indicate additional information
 * about the time of callback. An optional "duration" parameter indicates how
 * long the called party will be reachable starting at the initial time of
 * availability. If no duration parameter is given, the service is assumed to be
 * available indefinitely.
 * 
 * 
 * 
 */
public class RetryAfterHeaderHandler {
	private static final Logger LOGGER = MiTesterLog
	        .getLogger(RetryAfterHeaderHandler.class.getName());

	/**
	 * addRetryAfterHeader is used to add accept-encoding header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return Retry-After header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static RetryAfterHeader createRetryAfterHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		RetryAfterHeader retryafter = null;
		String Comment = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		String hvalue = null;

		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = RETRYAFTER;

		retryafter = headerFactroy.createRetryAfterHeader(Integer
		        .parseInt(hvalue));

		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			retryafter.setParameter(paramname, value);
		}

		if (Comment != null) {
			retryafter.setComment(Comment);
		}
		return retryafter;
	}

	/**
	 * removeRetryAfterHeader is used to remove the Retry-After header
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
	public static SIPMessage removeRetryAfterHeader(Header header,
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
			response.removeHeader(RetryAfter.RETRY_AFTER);
		else
			request.removeHeader(RetryAfter.RETRY_AFTER);
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;

	}

}
