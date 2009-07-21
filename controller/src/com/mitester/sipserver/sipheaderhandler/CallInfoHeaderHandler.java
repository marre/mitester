/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: CallInfoHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.CALLINFO;
import gov.nist.javax.sip.header.CallInfo;
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
import javax.sip.header.CallInfoHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * CallInfoHeaderHandler is used to add and remove the CallInfo header
 * 
 * RFC 3261 says,
 * 
 * 20.9 Call-Info
 * 
 * The Call-Info header field provides additional information about the caller
 * or callee, depending on whether it is found in a request or response. The
 * purpose of the URI is described by the "purpose" parameter. The "icon"
 * parameter designates an image suitable as an iconic representation of the
 * caller or callee. The "info" parameter describes the caller or callee in
 * general, for example, through a web page. The "card" parameter provides a
 * business card, for example, in vCard [36] or LDIF [37] formats. Additional
 * tokens can be registered using IANA and the procedures in Section 27. Use of
 * the Call-Info header field can pose a security risk. If a callee fetches the
 * URIs provided by a malicious caller, the callee may be at risk for displaying
 * inappropriate or offensive content, dangerous or illegal content, and so on.
 * Therefore, it is RECOMMENDED that a UA only render the information in the
 * Call-Info header field if it can verify the authenticity of the element that
 * originated the header field and trusts that element. This need not be the
 * peer UA; a proxy can insert this header field into requests.
 * 
 * 
 * 
 */

public class CallInfoHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(CallInfoHeaderHandler.class.getName());

	/**
	 * addCallInfoHeader is used to add a call-info header to the sip message
	 * 
	 * @param headerNew
	 *            is contains the header value and parameter of header
	 * @param type
	 *            is to identify REQUEST or RESPONSE
	 * @param sipMessage
	 *            is contatructed SIP Message
	 * @return call-nfo header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static CallInfoHeader createCallInfoHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {
		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");
		CallInfoHeader callInfo = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		AddressFactory addressFactory = factory.createAddressFactory();
		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = CALLINFO;

		String CallInfo[] = hvalue.split(",");
		for (String AlertinfoValue : CallInfo) {
			AlertinfoValue = AlertinfoValue.replace(">", "");
			AlertinfoValue = AlertinfoValue.replace("<", "");
			URI Callinfo = addressFactory.createURI(AlertinfoValue);
			callInfo = headerFactroy.createCallInfoHeader(Callinfo);
		}
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			callInfo.setParameter(paramname, value);
		}

		return callInfo;
	}

	/**
	 * removeCallInfoHeader is used to remove the call-info header from the sip
	 * message
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
	public static SIPMessage removeCallInfoHeader(Header header,
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

		CallInfoHeader callinfo = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			callinfo = (CallInfoHeader) response.getHeader(CallInfo.CALL_INFO);
		else
			callinfo = (CallInfoHeader) request.getHeader(CallInfo.CALL_INFO);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			callinfo.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(CallInfo.CALL_INFO);
			else
				request.removeHeader(CallInfo.CALL_INFO);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}

}
