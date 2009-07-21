/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ReasonHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.REASON;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.Reason;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ReasonHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * ReasonHeaderHandler is used for creating and removing the reason header
 * 
 * RFC 3326 says,
 * 
 * 2. The Reason Header Field
 * 
 * The Reason header field MAY appear in any request within a dialog, in any
 * CANCEL request and in any response whose status code explicitly allows the
 * presence of this header field. The following values for the protocol field
 * have been defined:
 * 
 * SIP: The cause parameter contains a SIP status code.
 * 
 * Q.850: The cause parameter contains an ITU-T Q.850 cause value in decimal
 * representation.
 * 
 * Proxies generating a CANCEL request upon reception of a CANCEL from the
 * previous hop that contains a Reason header field SHOULD copy it into the new
 * CANCEL request. In normal SIP operation, a SIP status code in a response
 * provides the client with information about the request that triggered the
 * response, the session parameters, or the user. For example, a 405 (Method not
 * allowed) response indicates that the request contained an unsupported method.
 * A 488 (Not Acceptable Here) indicates that the session parameters are
 * unacceptable and a 486 (Busy Here) provides information about the status of
 * the user. Any SIP status code MAY appear in the Reason header field of a
 * request. However, status codes that provide information about the user and
 * about session parameters are typically useful for implementing services
 * whereas status codes intended to report errors about a request are typically
 * useful for debugging purposes. A SIP message MAY contain more than one Reason
 * value (i.e., multiple Reason lines), but all of them MUST have different
 * protocol values (e.g., one SIP and another Q.850). An implementation is free
 * to ignore Reason values that it does not understand.
 * 
 * ALSO SEE TO RFC 4411 (detailed explanation on "preemption")
 * 
 * 
 * 
 */
public class ReasonHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(ReasonHeaderHandler.class.getName());

	/**
	 * createReasonHeader is used to add reason header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return Accept-Encoding Header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static ReasonHeader createReasonHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		ReasonHeader reason = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		String hvalue = null;
		// ,headerName = null;
		// headerName = headerNew.getName();
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = REASON;

		String array[] = hvalue.split(";");

		int index = array[1].indexOf("=");
		String no = null;
		if (index >= 0) {
			no = array[1].substring(index + 1, array[1].length());
		} else
			no = array[1];
		int index1 = array[2].indexOf("=");
		String test = null;
		if (index1 >= 0) {
			test = array[2].substring(index + 1, array[2].length() - 1);
		} else
			test = array[2];
		reason = headerFactroy.createReasonHeader(array[0], Integer
		        .parseInt(no), test);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			reason.setParameter(paramname, value);
		}

		return reason;
	}

	/**
	 * removeReasonHeader is used to remove the reason header
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
	public static SIPMessage removeReasonHeader(Header header,
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
		ReasonHeader reason = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			reason = (ReasonHeader) response
			        .getHeader(ReasonHeader.NAME);
		else
			reason = (ReasonHeader) request
			        .getHeader(ReasonHeader.NAME);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			reason.removeParameter(parameterName.getName());
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			response.removeHeader(Reason.REASON);
		else
			request.removeHeader(Reason.REASON);
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}

}
