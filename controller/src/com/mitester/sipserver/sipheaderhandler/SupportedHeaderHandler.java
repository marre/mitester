/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SupportedHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.SUPPORTED;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.Supported;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.header.SupportedHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * SupportedHeaderHandler is used to create and remove the Supported Header
 * 
 * RFC 3261 says,
 * 
 * 20.37 Supported
 * 
 * The Supported header field enumerates all the extensions supported by the UAC
 * or UAS. The Supported header field contains a list of option tags, described
 * in Section 19.2, that are understood by the UAC or UAS. A UA compliant to
 * this specification MUST only include option tags corresponding to
 * standards-track RFCs. If empty, it means that no extensions are supported.
 * The compact form of the Supported header field is k.
 * 
 */
public class SupportedHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(SupportedHeaderHandler.class.getName());

	/**
	 * addSupportedHeader is used to add Supported Header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return Supported Header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static SupportedHeader createSupportedHeader(Header headerNew)
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
		// ,headerName = null;
		// headerName = headerNew.getName();
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = SUPPORTED;
		SupportedHeader supported = headerFactroy.createSupportedHeader(hvalue);

		List<Param> param = headerNew.getParam();
		for (Param objParam : param) {
			LOGGER
			        .warn("As Per RFC 3261 Supported Header does not have any parameters. Hence Ignoring the parameters\t"
			                + "Parameter Name: "
			                + objParam.getName()
			                + "\t"
			                + "Parameter Value: " + objParam.getValue());
		}
		// 
		// LOGGER
		// .warn("As Per RFC 3261 Supported Header does not have any parameters. Hence Ignoring the parameters\t");
		return supported;
	}

	/**
	 * removeAcceptEncodingHeader is used to remove the Supported Header
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
	public static SIPMessage removeSupportedHeader(Header header,
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
			response.removeHeader(Supported.SUPPORTED);
		else
			request.removeHeader(Supported.SUPPORTED);
		} else {
			for (Param objParam : removeParams) {
				LOGGER
				        .warn("As Per RFC 3261 Supported Header does not have any parameters. Hence Ignoring the parameters\t"
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
