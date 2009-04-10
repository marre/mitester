/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: WarningHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.WARNING;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.Warning;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.header.WarningHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * WarningHeaderHandler is used to create and remove Warning header
 * 
 * RFC 3261 says,
 * 
 * 20.43 Warning
 * 
 * The Warning header field is used to carry additional information about the
 * status of a response. Warning header field values are sent with responses and
 * contain a three-digit warning code, host name, and warning text. The
 * "warn-text" should be in a natural language that is most likely to be
 * intelligible to the human user receiving the response. This decision can be
 * based on any available knowledge, such as the location of the user, the
 * Accept-Language field in a request, or the Content-Language field in a
 * response. The default language is i-default [21]. The currently-defined
 * "warn-code"s are listed below, with a recommended warn-text in English and a
 * description of their meaning. These warnings describe failures induced by the
 * session description. The first digit of warning codes beginning with "3"
 * indicates warnings specific to SIP. Warnings 300 through 329 are reserved for
 * indicating problems with keywords in the session description, 330 through 339
 * are warnings related to basic network services requested in the session
 * description, 370 through 379 are warnings related to quantitative QoS
 * parameters requested in the session description, and 390 through 399 are
 * miscellaneous warnings that do not fall into one of the above categories.
 * 
 * 300 Incompatible network protocol: One or more network protocols contained in
 * the session description are not available. 301 Incompatible network address
 * formats: One or more network address formats contained in the session
 * description are not available. 302 Incompatible transport protocol: One or
 * more transport protocols described in the session description are not
 * available. 303 Incompatible bandwidth units: One or more bandwidth
 * measurement units contained in the session description were not understood.
 * 304 Media type not available: One or more media types contained in the
 * session description are not available. 305 Incompatible media format: One or
 * more media formats contained in the session description are not available.
 * 306 Attribute not understood: One or more of the media attributes in the
 * session description are not supported. 307 Session description parameter not
 * understood: A parameter other than those listed above was not understood. 330
 * Multicast not available: The site where the user is located does not support
 * multicast. 331 Unicast not available: The site where the user is located does
 * not support unicast communication (usually due to the presence of a
 * firewall). 370 Insufficient bandwidth: The bandwidth specified in the session
 * description or defined by the media exceeds that known to be available. 399
 * Miscellaneous warning: The warning text can include arbitrary information to
 * be presented to a human user or logged. A system receiving this warning MUST
 * NOT take any automated action.
 * 
 * 1xx and 2xx have been taken by HTTP/1.1. Additional "warn-code"s can be
 * defined through IANA, as defined in Section 27.2.
 * 
 * 
 * 
 * 
 */
public class WarningHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(WarningHeaderHandler.class.getName());

	/**
	 * addWarningHeader is used to add Warning header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return Warning header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static WarningHeader createWarningHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		String hvalue = null;

		hvalue = headerNew.getValue();
		WarningHeader warning = null;
		if (hvalue == null)
			hvalue = WARNING;

		String array[] = hvalue.split(" ");
		if (array.length > 2) {
			StringBuffer b = new StringBuffer();
			for (int i = 2; i < array.length; i++) {
				b.append(array[i]);
				b.append(" ");
			}

			warning = headerFactroy.createWarningHeader(array[1], Integer
			        .parseInt(array[0]), b.toString().trim());
		}

		List<Param> param = headerNew.getParam();
		for (Param objParam : param) {
			LOGGER
			        .warning("As Per RFC 3261 Warning Header does not have any parameters. Hence Ignoring the parameters\t"
			                + "Parameter Name: "
			                + objParam.getName()
			                + "\t"
			                + "Parameter Value: " + objParam.getValue());
		}

		return warning;
	}

	/**
	 * removeWarningHeader is used to remove the Warning header
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
	public static SIPMessage removeWarningHeader(Header header,
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
			response.removeHeader(Warning.WARNING);
		else
			request.removeHeader(Warning.WARNING);
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}
}
