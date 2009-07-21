/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ErrorInfoHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.ERRORINFO;
import gov.nist.javax.sip.header.ErrorInfo;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.ErrorInfoHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * ErrorInfoHeaderHandler is used to create and remove the Error-Info Header
 * 
 * RFC 3261 says,
 * 
 * 20.18 Error-Info
 * 
 * The Error-Info header field provides a pointer to additional information
 * about the error status response. SIP UACs have user interface capabilities
 * ranging from pop-up windows and audio on PC softclients to audio-only on
 * "black" phones or endpoints connected via gateways. Rather than forcing a
 * server generating an error to choose between sending an error status code
 * with a detailed reason phrase and playing an audio recording, the Error-Info
 * header field allows both to be sent. The UAC then has the choice of which
 * error indicator to render to the caller. A UAC MAY treat a SIP or SIPS URI in
 * an Error-Info header field as if it were a Contact in a redirect and generate
 * a new INVITE, resulting in a recorded announcement session being established.
 * A non-SIP URI MAY be rendered to the user.
 * 
 * 
 * 
 */

public class ErrorInfoHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(ErrorInfoHeaderHandler.class.getName());

	/**
	 * createErrorInfoHeader is used to create a error info hrader
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
	public static ErrorInfoHeader createErrorInfoHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		ErrorInfoHeader error = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		AddressFactory addressFactory = factory.createAddressFactory();
		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = ERRORINFO;

		URI einfo = addressFactory.createURI(hvalue);
		error = headerFactroy.createErrorInfoHeader(einfo);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			error.setParameter(paramname, value);
		}

		return error;
	}

	/**
	 * removeErrorInfoHeader is used to remove the error-info header from the
	 * sip message
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

	public static SIPMessage removeErrorInfoHeader(Header header,
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

		ErrorInfoHeader error;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			error = (ErrorInfoHeader) response.getHeader(ErrorInfoHeader.NAME);
		else
			error = (ErrorInfoHeader) request.getHeader(ErrorInfoHeader.NAME);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			error.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(ErrorInfo.ERROR_INFO);
			else
				request.removeHeader(ErrorInfo.ERROR_INFO);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}

}
