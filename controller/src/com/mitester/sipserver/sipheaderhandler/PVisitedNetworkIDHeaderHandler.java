/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: PVisitedNetworkIDHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.PVISITEDNETWORKID;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.ims.PVisitedNetworkIDHeader;
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
 * PVisitedNetworkIDHeaderHandler is used to add and remove the
 * PVisitedNetworkIDHeader to the sip message
 * 
 * RFC 3455 says,
 * 
 * 4.3 The P-Visited-Network-ID header
 * 
 * 3GPP networks are composed of a collection of so called home networks,
 * visited networks and subscribers. A particular home network may have roaming
 * agreements with one or more visited networks. This has the effect that when a
 * mobile terminal is roaming, it can use resources provided by the visited
 * network in a transparent fashion. One of the conditions for a home network to
 * accept the registration of a UA roaming to a particular visited network, is
 * the existence of a roaming agreement between the home and the visited
 * network. There is a need to indicate to the home network which one is the
 * visited network that is providing services to the roaming UA. 3GPP user
 * agents always register to the home network. The REGISTER request is proxied
 * by one or more proxies located in the visited network towards the home
 * network. For the sake of a simple approach, it seems sensible that the
 * visited network includes an identification that is known at the home network.
 * This identification should be globally unique, and takes the form of a quoted
 * text string or a token. The home network may use this identification to
 * verify the existence of a roaming agreement with the visited network, and to
 * authorize the registration through that visited network.
 * 
 * 
 * 
 */
public class PVisitedNetworkIDHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(PVisitedNetworkIDHeaderHandler.class.getName());

	/**
	 * createPVisitedNetworkIDHeader is used to add PVisitedNetworkIDHeader to
	 * the sip message
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return PVisitedNetworkIDHeader
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static PVisitedNetworkIDHeader createPVisitedNetworkIDHeader(
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

		PVisitedNetworkIDHeader pvistednetworkid = null;

		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = PVISITEDNETWORKID;
		pvistednetworkid = hfimpl.createPVisitedNetworkIDHeader();
		if (hvalue.length() != 0)
			pvistednetworkid.setVisitedNetworkID(hvalue);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			pvistednetworkid.setParameter(paramname, value);
		}

		return pvistednetworkid;
	}

	/**
	 * removePVisitedNetworkIDHeader is used to remove the PVisitedNetworkIDHeader
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
	public static SIPMessage removePVisitedNetworkIDHeader(Header header,
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

		PVisitedNetworkIDHeader wwwauth;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			wwwauth = (PVisitedNetworkIDHeader) response
			        .getHeader(PVisitedNetworkIDHeader.NAME);
		else
			wwwauth = (PVisitedNetworkIDHeader) request
			        .getHeader(PVisitedNetworkIDHeader.NAME);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			wwwauth.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(PVisitedNetworkIDHeader.NAME);
			else
				request.removeHeader(PVisitedNetworkIDHeader.NAME);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}
}
