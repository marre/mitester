/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: AcceptHeaderHanldler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.ACCEPT;
import gov.nist.javax.sip.header.Accept;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.AcceptHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * AcceptHeaderHanldler is used to add and remove the accept header
 * 
 * RFC 3261 says,
 * 
 * 20.1 Accept
 * 
 * The Accept header field follows the syntax defined in [H14.1]. The semantics
 * are also identical, with the exception that if no Accept header field is
 * present, the server SHOULD assume a default value of application/sdp. An
 * empty Accept header field means that no formats are acceptable.
 * 
 * 
 * 
 */
public class AcceptHeaderHanldler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(AcceptHeaderHanldler.class.getName());

	/**
	 * addAcceptHeader is used to add a accept to the sip message
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return accept header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */

	public static AcceptHeader createAcceptHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {
		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");
		AcceptHeader acceptHeader = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerfactory = factory.createHeaderFactory();
		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = ACCEPT;

		int index = hvalue.indexOf("/");
		String maintype = hvalue.substring(0, index);
		String subtype = hvalue.substring(index + 1, hvalue.length());
		acceptHeader = headerfactory.createAcceptHeader(maintype, subtype);

		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			acceptHeader.setParameter(paramname, value);
		}

		return acceptHeader;
	}

	/**
	 * removeAcceptHeader is used to remove a accept to the sip message
	 * 
	 * @param headers
	 * @param type
	 * @param sipMessage
	 * @return sip message
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */

	public static SIPMessage removeAcceptHeader(Header header,
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
		AcceptHeader accept = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			accept = (AcceptHeader) response.getHeader(Accept.ACCEPT);
		else
			accept = (AcceptHeader) request.getHeader(Accept.ACCEPT);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			accept.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(Accept.ACCEPT);
			else
				request.removeHeader(Accept.ACCEPT);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}

}
