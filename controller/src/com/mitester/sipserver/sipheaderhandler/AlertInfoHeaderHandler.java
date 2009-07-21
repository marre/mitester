/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: AlertInfoHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.ALERTINFO;
import gov.nist.javax.sip.header.AlertInfo;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.AlertInfoHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * AlertInfoHeaderHandler is used add and remove the alert-info header from the
 * sip message
 * 
 * RFC 3261 says,
 * 
 * 20.4 Alert-Info
 * 
 * When present in an INVITE request, the Alert-Info header field specifies an
 * alternative ring tone to the UAS. When present in a 180 (Ringing) response,
 * the Alert-Info header field specifies an alternative ringback tone to the
 * UAC. A typical usage is for a proxy to insert this header field to provide a
 * distinctive ring feature. The Alert-Info header field can introduce security
 * risks.These risks and the ways to handle them are discussed in Section 20.9,
 * which discusses the Call-Info header field since the risks are identical. In
 * addition, a user SHOULD be able to disable this feature selectively. This
 * helps prevent disruptions that could result from the use of this header field
 * by untrusted elements.
 * 
 * 
 * 
 */
public class AlertInfoHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(AlertInfoHeaderHandler.class.getName());

	/**
	 * addAlertInfoHeader is used to add alert info header to teh request and
	 * respose
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return AlertInfoHeader
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static AlertInfoHeader createAlertInfoHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {
		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");
		AlertInfoHeader alertinfo = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		AddressFactory addressFactory = factory.createAddressFactory();
		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = ALERTINFO;

		String AlertInfo[] = hvalue.split(",");
		for (String AlertinfoValue : AlertInfo) {
			AlertinfoValue = AlertinfoValue.replace(">", "");
			AlertinfoValue = AlertinfoValue.replace("<", "");
			URI uri = addressFactory.createURI(AlertinfoValue);

			alertinfo = headerFactroy.createAlertInfoHeader(uri);
		}
		List<Param> param = headerNew.getParam();
		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			alertinfo.setParameter(paramname, value);
		}

		return alertinfo;
	}

	/**
	 * removeAlertInfoHeader is used to remove the alert info header
	 * 
	 * @param name
	 * @param removeParams
	 * @param sipMessage
	 * @param type
	 * @return
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */
	public static SIPMessage removeAlertInfoHeader(Header header,
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

		AlertInfoHeader alertinfo = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			alertinfo = (AlertInfoHeader) response
			        .getHeader(AlertInfo.ALERT_INFO);
		else
			alertinfo = (AlertInfoHeader) request
			        .getHeader(AlertInfo.ALERT_INFO);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			alertinfo.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(AlertInfo.ALERT_INFO);
			else
				request.removeHeader(AlertInfo.ALERT_INFO);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}

}
