/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ContactHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.CONTACT;
import gov.nist.javax.sip.header.Contact;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.ContactHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * ContactHeaderHandler is used to add and remove the Contact header
 * 
 * RFC 3261 says, 8.1.1.8 Contact
 * 
 * The Contact header field provides a SIP or SIPS URI that can be used to
 * contact that specific instance of the UA for subsequent requests. The Contact
 * header field MUST be present and contain exactly one SIP or SIPS URI in any
 * request that can result in the establishment of a dialog. For the methods
 * defined in this specification, that includes only the INVITE request. For
 * these requests, the scope of the Contact is global. That is, the Contact
 * header field value contains the URI at which the UA would like to receive
 * requests, and this URI MUST be valid even if used in subsequent requests
 * outside of any dialogs. If the Request-URI or top Route header field value
 * contains a SIPS URI, the Contact header field MUST contain a SIPS URI as
 * well.
 * 
 * 
 */

public class ContactHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(ContactHeaderHandler.class.getName());

	/**
	 * addContactHeader is used to add a contact header to th sip message
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
	public static ContactHeader createContactHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");
		ContactHeader fromHeader = null;

		Address fromNameAddress = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory hf = factory.createHeaderFactory();
		AddressFactory af = factory.createAddressFactory();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);

		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = CONTACT;

		fromNameAddress = af.createAddress(hvalue);
		fromHeader = hf.createContactHeader(fromNameAddress);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			if (paramname.equalsIgnoreCase("display-name")) {
				fromNameAddress.setDisplayName(value);
			} else {
				fromHeader.setParameter(paramname, value);
			}
		}

		return fromHeader;
	}

	/**
	 * removeContactHeader is used to remove the contact header to sip message
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
	public static SIPMessage removeContactHeader(Header header,
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

		ContactHeader contact;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			contact = (ContactHeader) response.getHeader(Contact.CONTACT);
		else
			contact = (ContactHeader) request.getHeader(Contact.CONTACT);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			contact.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(Contact.CONTACT);
			else
				request.removeHeader(Contact.CONTACT);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}

}
