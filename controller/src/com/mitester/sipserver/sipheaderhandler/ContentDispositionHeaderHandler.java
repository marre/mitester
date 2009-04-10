/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ContentDispositionHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.CONTENTDISPOSITION;
import gov.nist.javax.sip.header.ContentDisposition;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.ContentDispositionHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * ContentDispositionHeaderHandler is used to add and remove the
 * ContentDisposition header
 * 
 * RFC 3261 says,
 * 
 * 20.11 Content-Disposition
 * 
 * The Content-Disposition header field describes how the message body or, for
 * multipart messages, a message body part is to be interpreted by the UAC or
 * UAS. This SIP header field extends the MIME Content-Type (RFC 2183 [18]).
 * Several new "disposition-types" of the Content-Disposition header are defined
 * by SIP. The value "session" indicates that the body part describes a session,
 * for either calls or early (pre-call) media. The value "render" indicates that
 * the body part should be displayed or otherwise rendered to the user. Note
 * that the value "render" is used rather than "inline" to avoid the connotation
 * that the MIME body is displayed as a part of the rendering of the entire
 * message (since the MIME bodies of SIP messages oftentimes are not displayed
 * to users).For backward-compatibility, if the Content-Disposition header field
 * is missing, the server SHOULD assume bodies of Content-Type application/sdp
 * are the disposition "session", while other content types are "render". The
 * disposition type "icon" indicates that the body part contains an image
 * suitable as an iconic representation of the caller or callee that could be
 * rendered informationally by a user agent when a message has been received, or
 * persistently while a dialog takes place. The value "alert" indicates that the
 * body part contains information, such as an audio clip, that should be
 * rendered by the user agent in an attempt to alert the user to the receipt of
 * a request, generally a request that initiates a dialog; this alerting body
 * could for example be rendered as a ring tone for a phone call after a 180
 * Ringing provisional response has been sent. Any MIME body with a
 * "disposition-type" that renders content to the user should only be processed
 * when a message has been properly authenticated. The handling parameter,
 * handling-param, describes how the UAS should react if it receives a message
 * body whose content type or disposition type it does not understand. The
 * parameter has defined values of "optional" and "required". If the handling
 * parameter is missing, the value "required" SHOULD be assumed. The handling
 * parameter is described in RFC 3204 [19]. If this header field is missing, the
 * MIME type determines the default content disposition. If there is none,
 * "render" is assumed.
 * 
 * 
 * 
 */

public class ContentDispositionHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(ContentDispositionHeaderHandler.class.getName());

	/**
	 * addContentDispositionHeader is used to add a content-disposition to the
	 * sip message
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
	public static ContentDispositionHeader createContentDispositionHeader(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		SipFactory factory = SipFactory.getInstance();
		HeaderFactory hf = factory.createHeaderFactory();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);

		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = CONTENTDISPOSITION;
		ContentDispositionHeader contentdis = hf
		        .createContentDispositionHeader(hvalue);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			contentdis.setParameter(paramname, value);
		}

		return contentdis;
	}

	/**
	 * removeContentDispositionHeader is used to remove the content-disposiion
	 * header to the sip message
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
	public static SIPMessage removeContentDispositionHeader(Header header,
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

		ContentDispositionHeader contentdisposition;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			contentdisposition = (ContentDispositionHeader) response
			        .getHeader(ContentDisposition.CONTENT_DISPOSITION);
		else
			contentdisposition = (ContentDispositionHeader) request
			        .getHeader(ContentDisposition.CONTENT_DISPOSITION);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			contentdisposition.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(ContentDisposition.CONTENT_DISPOSITION);
			else
				request.removeHeader(ContentDisposition.CONTENT_DISPOSITION);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}

}
