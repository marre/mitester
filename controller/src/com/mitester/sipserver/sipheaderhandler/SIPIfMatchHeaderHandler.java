/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SIPIfMatchHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.SIPIFMATCH;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.SIPIfMatch;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.header.SIPIfMatchHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * SIPIfMatchHeaderHandler is used to create and remove teh SIP-If-Match Header
 * 
 * This interface represents the SIP-If-Match header, as defined by <a href =
 * "http://www.ietf.org/rfc/rfc3903.txt">RFC3903</a>.
 * <p>
 * The SIP-If-Match header is used by a client (event state publisher) in a
 * PUBLISH request, to update previously published event state. The value is
 * obtained from the server in a {@link javax.sip.header.SIPETagHeader} in a 2xx
 * response to a previous PUBLISH.
 * <p>
 * Sample syntax:<br>
 * <code>SIP-If-Match: dx200xyz</code>
 * 
 * <p>
 * A server must ignore Headers that it does not understand. A proxy must not
 * remove or modify Headers that it does not understand
 * 
 * 
 * 
 */
public class SIPIfMatchHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(SIPIfMatchHeaderHandler.class.getName());

	/**
	 * addSIPIfMatchHeader is used to add SIP-If-Match Header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return SIP-If-Match Header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static SIPIfMatchHeader createSIPIfMatchHeader(Header headerNew)
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

		if (hvalue == null)
			hvalue = SIPIFMATCH;

		SIPIfMatchHeader sipifmatch = headerFactroy
		        .createSIPIfMatchHeader(hvalue);

		List<Param> param = headerNew.getParam();
		for (Param objParam : param) {
			LOGGER
			        .warn("As Per RFC 3903 SIP-If-Match Header does not have any parameters. Hence Ignoring the parameters\t"
			                + "Parameter Name: "
			                + objParam.getName()
			                + "\t"
			                + "Parameter Value: " + objParam.getValue());
		}
		// 
		// LOGGER
		// .warn("As Per RFC 3903 SIP-If-Match Header does not have any parameters. Hence Ignoring the parameters\t");
		return sipifmatch;
	}

	/**
	 * removeSIPIfMatchHeader is used to remove the SIP-If-Match Header
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
	public static SIPMessage removeSIPIfMatchHeader(Header header,
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
			response.removeHeader(SIPIfMatch.SIP_IF_MATCH);
		else
			request.removeHeader(SIPIfMatch.SIP_IF_MATCH);
		} else {
			for (Param objParam : removeParams) {
				LOGGER
				        .warn("As Per RFC 3903 SIP-If-Match Header does not have any parameters. Hence Ignoring the parameters\t"
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
