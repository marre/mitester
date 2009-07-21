/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: JoinHeaderHandler.java
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
import gov.nist.javax.sip.header.extensions.JoinHeader;
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
 * JoinHeaderHandler is used to add and remove the Join header
 * 
 * RFC 3911 says, The Join header field indicates that a new dialog (created by
 * the INVITE in which the Join header field in contained) should be joined with
 * a dialog identified by the header field, and any associated dialogs or
 * conferences. It is a request header only, and defined only for INVITE
 * requests. The Join header field MAY be encrypted as part of end-to-end
 * encryption. Only a single Join header field value may be present in a SIP
 * request
 * 
 * This document adds the following entry to Table 3 of [1]. Additions to this
 * table are also provided for extension methods defined at the time of
 * publication of this document. This is provided as a courtesy to the reader
 * and is not normative in any way. MESSAGE, SUBSCRIBE and NOTIFY, REFER, INFO,
 * UPDATE, PRACK, and PUBLISH are defined respectively in [19], [20], [7], [21],
 * [22], [23], and [24].
 * 
 * 
 * 
 */

public class JoinHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(JoinHeaderHandler.class.getName());

	/**
	 * createJoinHeader is used to create a join header
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
	public static JoinHeader createJoinHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {
		JoinHeader joinHeader = null;

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		String CallID = null;
		String FromTag = null;
		String ToTag = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory hf = factory.createHeaderFactory();
		HeaderFactoryImpl hfimpl = (HeaderFactoryImpl) factory
		        .createHeaderFactory();
		((HeaderFactoryImpl) hf).setPrettyEncoding(true);
		hfimpl.setPrettyEncoding(true);

		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null) {
			CallID = "f81d4fae-7dec-11d0-a765-00a0c91e6bf6@biloxi.com";
			FromTag = "34892348923";
			ToTag = "4568943mlfh";
		}
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			if (paramname.equals("call-id"))
				CallID = value;
			else if (paramname.equals("from-tag"))
				FromTag = value;
			else if (paramname.equals("to-tag"))
				ToTag = value;
		}

		joinHeader = hfimpl.createJoinHeader(CallID, ToTag, FromTag);
		return joinHeader;
	}
	/**
	 * To remove join Header from the sip message
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
	public static SIPMessage removeJoinHeader(Header header,
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

		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			response.removeHeader(JoinHeader.NAME);
		else
			request.removeHeader(JoinHeader.NAME);
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	
	}
}
