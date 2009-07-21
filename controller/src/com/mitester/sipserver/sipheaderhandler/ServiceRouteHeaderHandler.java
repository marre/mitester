/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ServiceRouteHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.SERVICEROUTE;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.ims.ServiceRoute;
import gov.nist.javax.sip.header.ims.ServiceRouteHeader;
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
 * ServiceRouteHeaderHandler is used to craete and remove the Service-Route
 * Header
 * 
 * RFC 3608 says, 3. Discussion of Mechanism
 * 
 * UAs may include a Route header field in an initial request to force that
 * request to visit and potentially be serviced by one or more proxies. Using
 * such a route (called a "service route" or "preloaded route") allows a UA to
 * request services from a specific home proxy or network of proxies. The open
 * question is, "How may a UA discover what service route to use?" This document
 * defines a header field called "Service-Route" which can contain a route
 * vector that, if used as discussed above, will direct requests through a
 * specific sequence of proxies. A registrar may use a Service-Route header
 * field to inform a UA of a service route that, if used by the UA, will provide
 * services from a proxy or set of proxies associated with that registrar. The
 * Service-Route header field may be included by a registrar in the response to
 * a REGISTER request. Consequently, a registering UA learns of a service route
 * that may be used to request services from the system it just registered with.
 * The routing established by the Service-Route mechanism applies only to
 * requests originating in the user agent. That is, it applies only to UA
 * originated requests, and not to requests terminated by that UA. Simply put,
 * the registrar generates a service route for the registering UA and returns it
 * in the response to each successful REGISTER request. This service route has
 * the form of a Route header field that the registering UA may use to send
 * requests through the service proxy selected by the registrar. The UA would
 * use this route by inserting it as a preloaded Route header field in requests
 * originated by the UA intended for routing through the service proxy. The
 * mechanism by which the registrar constructs the header field value is
 * specific to the local implementation and outside the scope of this document.
 * 
 * 
 * 
 */
public class ServiceRouteHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(ServiceRouteHeaderHandler.class.getName());

	/**
	 * addServiceRouteHeader is used to add Service-Route header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return Service-Route Header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static ServiceRouteHeader addServiceRouteHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		SipFactory factory = SipFactory.getInstance();
		ServiceRouteHeader serviceroute = null;
		AddressFactory af = factory.createAddressFactory();
		HeaderFactoryImpl hfimpl = (HeaderFactoryImpl) factory
		        .createHeaderFactory();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		String hvalue = null;

		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = SERVICEROUTE;

		Address service = af.createAddress(hvalue);
		serviceroute = hfimpl.createServiceRouteHeader(service);

		List<Param> parameter = headerNew.getParam();

		for (Param param : parameter) {
			if (param.getName().equalsIgnoreCase("display-name"))
				service.setDisplayName(param.getValue());
			else
				serviceroute.setParameter(param.getName(), param.getValue());
		}

		return serviceroute;
	}

	/**
	 * removeServiceRouteHeader is used to remove the Service-Route header
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
	public static SIPMessage removeServiceRouteHeader(Header header,
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
		ServiceRouteHeader serviceroute = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			serviceroute = (ServiceRouteHeader) response.getHeader(ServiceRouteHeader.NAME);
		else
			serviceroute = (ServiceRouteHeader) request.getHeader(ServiceRouteHeader.NAME);
		List<Param> removeParams = header.getParam();
		if (removeParams.size() == 0) {
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			response.removeHeader(ServiceRoute.SERVICE_ROUTE);
		else
			request.removeHeader(ServiceRoute.SERVICE_ROUTE);
		} else {
			for (Param parameterName : removeParams) {
				serviceroute.removeParameter(parameterName.getName());
			}
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;

		return returnsipMessage;
	}

}
