/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: PChargingFunctionAddressHeaderHandler.java
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

import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.ims.PChargingFunctionAddressesHeader;
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
 * PChargingFunctionAddressHeaderHandler is used to add and remove the
 * PChargingFunctionAddress header
 * 
 * RFC 3455 says,
 * 
 * 4.5 The P-Charging-Function-Addresses header
 * 
 * 3GPP has defined a distributed architecture that results in multiple network
 * entities becoming involved in providing access and services. There is a need
 * to inform each SIP proxy involved in a transaction about the common charging
 * functional entities to receive the generated charging records or charging
 * events. The solution provided by 3GPP is to define two types of charging
 * functional entities: Charging Collection Function (CCF) and Event Charging
 * Function (ECF). CCF is used for off-line charging (e.g., for postpaid account
 * charging). ECF is used for on-line charging (e.g., for pre-paid account
 * charging). There may be more than a single instance of CCF and ECF in a
 * network, in order to provide redundancy in the network. In case there are
 * more than a single instance of either the CCF or the ECF addresses,
 * implementations SHOULD attempt sending the charging data to the ECF or CCF
 * address, starting with the first address of the sequence (if any) in the
 * P-Charging-Function-Addresses header. The CCF and ECF addresses may be passed
 * during the establishment of a dialog or in a standalone transaction. More
 * detailed information about charging can be found in 3GPP TS 32.200 [16] and
 * 3GPP TS 32.225 [17]. We define the SIP private header
 * P-Charging-Function-Addresses. A proxy MAY include this header, if not
 * already present, in either the initial request or response for a dialog, or
 * in the request and response of a standalone transaction outside a dialog.
 * Only one instance of the header MUST be present in a particular request or
 * response. The mechanisms by which a SIP proxy collects the values to populate
 * the P-Charging-Function-Addresses header values are outside the scope of this
 * document. However, as an example, a SIP proxy may have preconfigured these
 * addresses, or may obtain them from a subscriber database.
 * 
 * 
 * 
 */

public class PChargingFunctionAddressHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(PChargingFunctionAddressHeaderHandler.class.getName());

	/**
	 * createPChargingFunctionAddressesHeader is used to create
	 * PChargingFunctionAddress header
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
	public static PChargingFunctionAddressesHeader createPChargingFunctionAddressesHeader(
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

		PChargingFunctionAddressesHeader PChargingFunAdd = hfimpl
		        .createPChargingFunctionAddressesHeader();
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			PChargingFunAdd.setParameter(paramname, value);
		}

		return PChargingFunAdd;
	}
	/**
	 * To remove PChargingFunctionAddressesHeaderr from sip message
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
	public static SIPMessage removePChargingFunctionAddressesHeader(Header header,
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

		PChargingFunctionAddressesHeader acceptEncoding = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			acceptEncoding = (PChargingFunctionAddressesHeader) response
			        .getHeader(PChargingFunctionAddressesHeader.NAME);
		else
			acceptEncoding = (PChargingFunctionAddressesHeader) request
			        .getHeader(PChargingFunctionAddressesHeader.NAME);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			acceptEncoding.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(PChargingFunctionAddressesHeader.NAME);
			else
				request.removeHeader(PChargingFunctionAddressesHeader.NAME);
		}

		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}
}
