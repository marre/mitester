/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: AcceptLanguageHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.ACCEPTLANGUAGE;
import gov.nist.javax.sip.header.AcceptLanguage;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.AcceptLanguageHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * AcceptLanguageHeaderHandler is used to add and remove accept language header
 * 
 * RFC 3261 says,
 * 
 * 20.3 Accept-Language
 * 
 * The Accept-Language header field is used in requests to indicate the
 * preferred languages for reason phrases, session descriptions, or status
 * responses carried as message bodies in the response. If no Accept-Language
 * header field is present, the server SHOULD assume all languages are
 * acceptable to the client. The Accept-Language header field follows the syntax
 * defined in [H14.4]. The rules for ordering the languages based on the "q"
 * parameter apply to SIP as well.
 * 
 * 
 * 
 */
public class AcceptLanguageHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(AcceptLanguageHeaderHandler.class.getName());

	/**
	 * addAcceptLangaugeHeader is used to add accept-language headerto sip
	 * message
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return AcceptLanguageHeader
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static AcceptLanguageHeader createAcceptLangaugeHeader(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {
		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerfactory = factory.createHeaderFactory();
		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = ACCEPTLANGUAGE;
		Locale accept = new Locale(hvalue);
		AcceptLanguageHeader acceptLangauage = headerfactory
		        .createAcceptLanguageHeader(accept);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			acceptLangauage.setParameter(paramname, value);
		}
		return acceptLangauage;
	}

	/**
	 * removeAcceptLangaugeHeader is used to remove the accept language header
	 * from the sip message
	 * 
	 * @param name
	 * @param removeParams
	 * @param sipMessage
	 * @param type
	 * @return sipMessage
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */
	public static SIPMessage removeAcceptLangaugeHeader(Header header,
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

		AcceptLanguageHeader acceptlang = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			acceptlang = (AcceptLanguageHeader) response
			        .getHeader(AcceptLanguage.ACCEPT_LANGUAGE);
		else
			acceptlang = (AcceptLanguageHeader) request
			        .getHeader(AcceptLanguage.ACCEPT_LANGUAGE);

		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			acceptlang.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(AcceptLanguage.ACCEPT_LANGUAGE);
			else
				request.removeHeader(AcceptLanguage.ACCEPT_LANGUAGE);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}

}
