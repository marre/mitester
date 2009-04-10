/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: PAssertedIdentityHeaderHandler.java
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
 * Package 				License 										Details
 *---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Jain SIP stack 		NIST-CONDITIONS-OF-USE 						        https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 				The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
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
import gov.nist.javax.sip.header.ims.PAssertedIdentityHeader;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.utility.MiTesterLog;

/**
 * PAssertedIdentityHeaderHandler is used to add and remove the
 * P-Asserted-Identity header
 * 
 * RFC 3325 says,
 * 
 * 9.1 The P-Asserted-Identity Header
 * 
 * The P-Asserted-Identity header field is used among trusted SIP entities
 * (typically intermediaries) to carry the identity of the user sending a SIP
 * message as it was verified by authentication. A P-Asserted-Identity header
 * field value MUST consist of exactly one name-addr or addr-spec. There may be
 * one or two P-Asserted-Identity values. If there is one value, it MUST be a
 * sip, sips, or tel URI. If there are two values, one value MUST be a sip or
 * sips URI and the other MUST be a tel URI. It is worth noting that proxies can
 * (and will) add and remove this header field.
 * 
 * 
 * 
 */

public class PAssertedIdentityHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(PAssertedIdentityHeaderHandler.class.getName());

	/**
	 * createPAssertedIdentityHeader is used to create P-Asserted-Identity
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
	public static PAssertedIdentityHeader createPAssertedIdentityHeader(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		PAssertedIdentityHeader pAssertedIdentity = null;
		Address associatedidentity = null;

		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		AddressFactory addressFactory = factory.createAddressFactory();
		String hvalue = null;
		hvalue = headerNew.getValue();
		HeaderFactoryImpl hfimpl = (HeaderFactoryImpl) factory
		        .createHeaderFactory();
		if (hvalue == null)
			hvalue = SIPURI;

		associatedidentity = addressFactory.createAddress(hvalue);
		pAssertedIdentity = hfimpl
		        .createPAssertedIdentityHeader(associatedidentity);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			if (paramname.equalsIgnoreCase("display-name"))
				associatedidentity.setDisplayName(value);
		}

		return pAssertedIdentity;
	}

}
