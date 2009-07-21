/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: PPreferredIdentityHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.SIPURI;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.ims.PPreferredIdentityHeader;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * PPreferredIdentityHeaderHandler is used to add and remove the
 * P-PreferredIdentity header
 * 
 * RFC 3325 says,
 * 
 * 9.2 The P-Preferred-Identity Header
 * 
 * The P-Preferred-Identity header field is used from a user agent to a trusted
 * proxy to carry the identity the user sending the SIP message wishes to be
 * used for the P-Asserted-Header field value that the trusted element will
 * insert. A P-Preferred-Identity header field value MUST consist of exactly one
 * name-addr or addr-spec. There may be one or two P-Preferred-Identity values.
 * If there is one value, it MUST be a sip, sips, or tel URI. If there are two
 * values, one value MUST be a sip or sips URI and the other MUST be a tel URI.
 * It is worth noting that proxies can (and will) remove this header field.
 * 
 * 
 * 
 */

public class PPreferredIdentityHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(PPreferredIdentityHeaderHandler.class.getName());

	/**
	 * createPPreferredIdentityHeader is used to create P-PreferredIdentity
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
	public static PPreferredIdentityHeader createPPreferredIdentityHeader(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {
		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		SipFactory factory = SipFactory.getInstance();
		AddressFactory af = factory.createAddressFactory();
		HeaderFactory hf = factory.createHeaderFactory();
		HeaderFactoryImpl hfimpl = (HeaderFactoryImpl) factory
		        .createHeaderFactory();
		((HeaderFactoryImpl) hf).setPrettyEncoding(true);
		hfimpl.setPrettyEncoding(true);

		Address identity = null;

		PPreferredIdentityHeader pprefrerredidentity = null;
		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = SIPURI;

		identity = af.createAddress(hvalue);
		pprefrerredidentity = hfimpl.createPPreferredIdentityHeader(identity);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			if (paramname.equalsIgnoreCase("display-name"))
				identity.setDisplayName(value);
		}
		return pprefrerredidentity;
	}


	/**
	 * To remove PPreferredIdentityHeader from sip message
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
	public static SIPMessage removePPreferredIdentityHeader(Header header,
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
			response.removeHeader(PPreferredIdentityHeader.NAME);
		else
			request.removeHeader(PPreferredIdentityHeader.NAME);
		} else {
			for (Param objParam : removeParams) {
				LOGGER
				        .warn("As Per RFC 3313 P-Preferred-Identity Header does not have any parameters. Hence Ignoring the parameters\t"
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
