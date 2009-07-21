/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: DateHeaderHandler.java
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
import gov.nist.javax.sip.header.SIPDateHeader;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.DateHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * DateHeaderHandler is used to add and remove the Date header
 * 
 * RFC 3261 says,
 * 
 * 20.17 Date
 * 
 * The Date header field contains the date and time. Unlike HTTP/1.1, SIP only
 * supports the most recent RFC 1123 [20] format for dates. As in [H3.3], SIP
 * restricts the time zone in SIP-date to "GMT", while RFC 1123 allows any time
 * zone. An RFC 1123 date is case-sensitive. The Date header field reflects the
 * time when the request or response is first sent. The Date header field can be
 * used by simple end systems without a battery-backed clock to acquire a notion
 * of current time. However, in its GMT form, it requires clients to know their
 * offset from GMT.
 * 
 * 
 * 
 */

public class DateHeaderHandler {
	private static final Logger LOGGER = MiTesterLog
	        .getLogger(DateHeaderHandler.class.getName());

	/**
	 * addDateHeader is used to create a DAte header
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

	public static DateHeader createDateHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");
		Date date = null;
		Calendar c = null;
		DateHeader newDate = null;
		DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null) {
			date = new Date();
			hvalue = df.format(date);
			hvalue = hvalue.replace("+0530", "GMT");
		}
		date = df.parse(hvalue);
		c = Calendar.getInstance();
		c.setTime(date);
		newDate = headerFactroy.createDateHeader(c);

		List<Param> param = headerNew.getParam();
		for (Param objParam : param) {
			LOGGER
			        .warn("As Per RFC 3261 Date  Header does not have any parameters. Hence Ignoring the parameters\t"
			                + "Parameter Name: "
			                + objParam.getName()
			                + "\t"
			                + "Parameter Value: " + objParam.getValue());
		}

		return newDate;
	}

	/**
	 * removeDateHeader is used to a remove the date header
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
	public static SIPMessage removeDateHeader(Header header,
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
			response.removeHeader(SIPDateHeader.DATE);
		else
			request.removeHeader(SIPDateHeader.DATE);
		} else {
			for (Param objParam : removeParams) {
				LOGGER
				        .warn("As Per RFC 3261 Date  Header does not have any parameters. Hence Ignoring the parameters\t"
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
