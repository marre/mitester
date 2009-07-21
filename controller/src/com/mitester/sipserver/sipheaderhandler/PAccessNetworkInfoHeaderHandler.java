/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: PAccessNetworkInfoHeaderHandler.java
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
import gov.nist.javax.sip.header.ims.PAccessNetworkInfoHeader;
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
 * PAccessNetworkInfoHeaderHandler is used to add and remove the
 * P-Access-NetworkInfo header
 * 
 * RFC 3455 says,
 * 
 * 4.4 The P-Access-Network-Info header
 * 
 * This section describes the P-Access-Network-Info header. This header is
 * useful in SIP-based networks that also provide layer 2/layer 3 connectivity
 * through different access technologies. SIP User Agents may use this header to
 * relay information about the access technology to proxies that are providing
 * services. The serving proxy may then use this information to optimize
 * services for the UA. For example, a 3GPP UA may use this header to pass
 * information about the access network such as radio access technology and
 * radio cell identity to its home service provider. For the purpose of this
 * extension, we define an access network as the network providing the layer
 * 2/layer 3 IP connectivity which in turn provides a user with access to the
 * SIP capabilities and services provided. In some cases, the SIP server that
 * provides the user with services may wish to know information about the type
 * of access network that the UA is currently using. Some services are more
 * suitable or less suitable depending on the access type, and some services are
 * of more value to subscribers if the access network details are known by the
 * SIP proxy which provides the user with services. In other cases, the SIP
 * server that provides the user with services may simply wish to know crude
 * location information in order to provide certain services to the user. For
 * example, many of the location based services available in wireless networks
 * today require the home network to know the identity of the cell the user is
 * being served by. Some regulatory requirements exist mandating that for
 * cellular radio systems, the identity of the cell where an emergency call is
 * established is made available to the emergency authorities. The SIP server
 * that provides services to the user may desire knowledge about the access
 * network. This is achieved by defining a new private SIP extension header,
 * P-Access-Network-Info. This header carries information relating to the access
 * network between the UAC and its serving proxy in the home network.
 * 
 * 
 * 
 */

public class PAccessNetworkInfoHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(PAccessNetworkInfoHeaderHandler.class.getName());

	/**
	 * createPAccessNetworkInfoHeader is used to create P-Access-NetworkInfo
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
	public static PAccessNetworkInfoHeader createPAccessNetworkInfoHeader(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		SipFactory factory = SipFactory.getInstance();

		HeaderFactoryImpl hfimpl = (HeaderFactoryImpl) factory
		        .createHeaderFactory();

		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		// AddressFactory addressFactory = factory.createAddressFactory();
		String hvalue = null;
		hvalue = headerNew.getValue();
		PAccessNetworkInfoHeader PAccessNetworkInfo = null;
		if (hvalue == null)
			hvalue = "3GPP-GERAN";
		PAccessNetworkInfo = hfimpl.createPAccessNetworkInfoHeader();
		if (hvalue.length() != 0)
			PAccessNetworkInfo.setAccessType(hvalue);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			PAccessNetworkInfo.setParameter(paramname, value);
		}

		return PAccessNetworkInfo;
	}
	/**
	 * to remove P Access Netork Info Header from the sip message
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
	public static SIPMessage removePAccessNetworkInfoHeader(Header header,
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

		PAccessNetworkInfoHeader paccessNetwork = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			paccessNetwork = (PAccessNetworkInfoHeader) response
			        .getHeader(PAccessNetworkInfoHeader.NAME);
		else
			paccessNetwork = (PAccessNetworkInfoHeader) request
			        .getHeader(PAccessNetworkInfoHeader.NAME);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			paccessNetwork.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(PAccessNetworkInfoHeader.NAME);
			else
				request.removeHeader(PAccessNetworkInfoHeader.NAME);
		}

		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}
}
