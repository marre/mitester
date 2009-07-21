/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: PAssociatedURIHeaderHandler.java
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
import gov.nist.javax.sip.header.ims.PAssociatedURIHeader;
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
 * PAssociatedURIHeaderHandler is used to add and remove the P-Associated-URI
 * header
 * 
 * RFC 3455 says,
 * 
 * 4.1 The P-Associated-URI header
 * 
 * This extension allows a registrar to return a set of associated URIs for a
 * registered address-of-record. We define the P-Associated-URI header field,
 * used in the 200 OK response to a REGISTER request. The P-Associated-URI
 * header field transports the set of Associated URIs to the registered
 * address-of-record. An associated URI is a URI that the service provider has
 * allocated to a user for his own usage. A registrar contains information that
 * allows an address-of-record URI to be associated with zero or more URIs.
 * Usually, all these URIs (the address-of-record URI and the associated URIs)
 * are allocated for the usage of a particular user. This extension to SIP
 * allows the UAC to know, upon a successful authenticated registration, which
 * other URIs, if any, the service provider has associated to an
 * address-of-record URI. Note that, generally speaking, the registrar does not
 * register the associated URIs on behalf of the user. Only the
 * address-of-record which is present in the To header field of the REGISTER is
 * registered and bound to the contact address. The only information conveyed is
 * that the registrar is aware of other URIs to be used by the same user. It may
 * be possible, however, that an application server (or even the registrar
 * itself) registers any of the associated URIs on behalf of the user by means
 * of a third party registration. However, this third party registration is out
 * of the scope of this document. A UAC MUST NOT assume that the associated URIs
 * are registered. If a UAC wants to check whether any of the associated URIs is
 * registered, it can do so by mechanisms specified outside this document, e.g.,
 * the UA may send a REGISTER request with the To header field value set to any
 * of the associated URIs and without a Contact header. The 200 OK response will
 * include a Contact header with the list of registered contact addresses. If
 * the associated URI is not registered, the UA MAY register it prior to its
 * utilization.
 * 
 * 
 * 
 */

public class PAssociatedURIHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(PAssociatedURIHeaderHandler.class.getName());

	/**
	 * createPAssociatedURIHeader is used to create P-Associated-URI header
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
	public static PAssociatedURIHeader createPAssociatedURIHeader(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		AddressFactory addressFactory = factory.createAddressFactory();
		String hvalue = null;
		hvalue = headerNew.getValue();
		HeaderFactoryImpl hfimpl = (HeaderFactoryImpl) factory
		        .createHeaderFactory();
		PAssociatedURIHeader pAssociatedURI = null;
		Address associatedUri = null;
		if (hvalue == null)
			hvalue = SIPURI;

		associatedUri = addressFactory.createAddress(hvalue);
		pAssociatedURI = hfimpl.createPAssociatedURIHeader(associatedUri);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			if (!paramname.equalsIgnoreCase("display-name"))
				pAssociatedURI.setParameter(paramname, value);
			else
				associatedUri.setDisplayName(value);
		}

		return pAssociatedURI;
	}
	/**
	 * To remove PAssociatedURI header from sip message
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
	public static SIPMessage removePAssociatedURIHeader(Header header,
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

		PAssociatedURIHeader acceptEncoding = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			acceptEncoding = (PAssociatedURIHeader) response
			        .getHeader(PAssociatedURIHeader.NAME);
		else
			acceptEncoding = (PAssociatedURIHeader) request
			        .getHeader(PAssociatedURIHeader.NAME);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			acceptEncoding.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(PAssociatedURIHeader.NAME);
			else
				request.removeHeader(PAssociatedURIHeader.NAME);
		}

		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}
}
