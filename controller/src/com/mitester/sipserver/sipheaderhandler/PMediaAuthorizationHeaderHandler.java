/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: PMediaAuthorizationHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.P_MEDIA_AUTHORIZATION;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.ims.PMediaAuthorizationHeader;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * PMediaAuthorizationHeaderHandler is used to add and remove the
 * P-MediaAuthorization header
 * 
 * RFC 3313 says,
 * 
 * 5.1 SIP Header Extension
 * 
 * A new P-Media-Authorization general header field is defined. The
 * P-Media-Authorization header field contains one or more media authorization
 * tokens which are to be included in subsequent resource reservations for the
 * media flows associated with the session, that is, passed to an independent
 * resource reservation mechanism, which is not specified here. The media
 * authorization tokens are used for authorizing QoS for the media stream(s).
 * The P-Media-Authorization header field can be used only in SIP requests or
 * responses that can carry a SIP offer or answer. This naturally keeps the
 * scope of this header field narrow.
 * 
 * 
 * 
 */

public class PMediaAuthorizationHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(PMediaAuthorizationHeaderHandler.class.getName());

	/**
	 * createPPreferredIdentityHeader is used to create P-MediaAuthorization
	 * header
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
	public static PMediaAuthorizationHeader createPPreferredIdentityHeader(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		SipFactory factory = SipFactory.getInstance();

		HeaderFactory hf = factory.createHeaderFactory();
		HeaderFactoryImpl hfimpl = (HeaderFactoryImpl) factory
		        .createHeaderFactory();
		((HeaderFactoryImpl) hf).setPrettyEncoding(true);
		hfimpl.setPrettyEncoding(true);
		PMediaAuthorizationHeader MediaAuth = null;
		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = P_MEDIA_AUTHORIZATION;

		MediaAuth = hfimpl.createPMediaAuthorizationHeader(hvalue);
		List<Param> param = headerNew.getParam();
		for (Param objParam : param) {
			LOGGER
			        .warn("As Per RFC 3313 P-Media-Authorization Header does not have any parameters. Hence Ignoring the parameters\t"
			                + "Parameter Name: "
			                + objParam.getName()
			                + "\t"
			                + "Parameter Value: " + objParam.getValue());
		}
		return MediaAuth;
	}
	/**
	 * To remove PMediaAuthorizationHeader from sip message
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
	public static SIPMessage removePMediaAuthorizationHeader(Header header,
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
		List<Param> removeParams = header.getParam();
		if (removeParams.size() == 0) {
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			response.removeHeader(PMediaAuthorizationHeader.NAME);
		else
			request.removeHeader(PMediaAuthorizationHeader.NAME);
		} else {
			for (Param objParam : removeParams) {
				LOGGER
				        .warn("As Per RFC 3313 P-Media-Authorization Header does not have any parameters. Hence Ignoring the parameters\t"
				                + "Parameter Name: "
				                + objParam.getName());
				}
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}
}