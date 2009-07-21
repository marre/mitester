/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ToHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.TO;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.To;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * ToHeaderHandler is used to create and remove the To Header
 * 
 * RFC 3261 says,
 * 
 * 20.39 To
 * 
 * The To header field specifies the logical recipient of the request. The
 * optional "display-name" is meant to be rendered by a human-user interface.
 * The "tag" parameter serves as a general mechanism for dialog identification.
 * See Section 19.3 for details of the "tag" parameter. Comparison of To header
 * fields for equality is identical to comparison of From header fields. See
 * Section 20.10 for the rules for parsing a display name, URI and URI
 * parameters, and header field parameters. The compact form of the To header
 * field is t.
 * 
 * 
 * 
 */
public class ToHeaderHandler {
	private static final Logger LOGGER = MiTesterLog
	        .getLogger(ToHeaderHandler.class.getName());

	/**
	 * createtoHeader is used to add To header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return To Header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static ToHeader createtoHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		Random remotetag = new Random();
		ToHeader toHeader = null;
		String hvalue = null;
		String tag = null;
		Address fromNameAddress = null;

		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		AddressFactory addressFactory = factory.createAddressFactory();
		// String headerName = null;
		// headerName = headerNew.getName();
		hvalue = headerNew.getValue();

		if (hvalue == null) {
			hvalue = TO;
			tag = Integer.toHexString(remotetag.nextInt());
		}

		fromNameAddress = addressFactory.createAddress(hvalue);
		toHeader = headerFactroy.createToHeader(fromNameAddress, null);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			if (paramname.equals("tag"))
				value = Integer.toHexString(remotetag.nextInt());
			if (paramname.equalsIgnoreCase("display-name")) {
				fromNameAddress.setDisplayName(value);
			} else {
				toHeader.setParameter(paramname, value);
			}
		}

		if (tag != null)
			toHeader.setTag(tag);

		return toHeader;
	}

	/**
	 * removeToHeader is used to remove the To header
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
	public static SIPMessage removeToHeader(Header header,
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
		ToHeader to = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			to = (ToHeader) response.getHeader(ToHeader.NAME);
		else
			to = (ToHeader) request.getHeader(ToHeader.NAME);

		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			to.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(To.TO);
			else
				request.removeHeader(To.TO);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}

}
