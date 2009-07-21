/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ViaHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.VIA;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.Via;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * ViaHeaderHandler is used to create and remove the Via Header
 * 
 * RFC 3261 says,
 * 
 * 20.42 Via
 * 
 * The Via header field indicates the path taken by the request so far and
 * indicates the path that should be followed in routing responses. The branch
 * ID parameter in the Via header field values serves as a transaction
 * identifier, and is used by proxies to detect loops. A Via header field value
 * contains the transport protocol used to send the message, the client's host
 * name or network address, and possibly the port number at which it wishes to
 * receive responses. A Via header field value can also contain parameters such
 * as "maddr", "ttl", "received", and "branch", whose meaning and use are
 * described in other sections. For implementations compliant to this
 * specification, the value of the branch parameter MUST start with the magic
 * cookie "z9hG4bK", as discussed in Section 8.1.1.7. Transport protocols
 * defined here are "UDP", "TCP", "TLS", and "SCTP"."TLS" means TLS over TCP.
 * When a request is sent to a SIPS URI, the protocol still indicates "SIP", and
 * the transport protocol is TLS. The compact form of the Via header field is v.
 * 
 * Via: SIP/2.0/UDP erlang.bell-telephone.com:5060;branch=z9hG4bK87asdks7 Via:
 * SIP/2.0/UDP 192.0.2.1:5060 ;received=192.0.2.207;branch=z9hG4bK77asjd
 * 
 * In this example, the message originated from a multi-homed host with two
 * addresses, 192.0.2.1 and 192.0.2.207. The sender guessed wrong as to which
 * network interface would be used. Erlang.bell- telephone.com noticed the
 * mismatch and added a parameter to the previous hop's Via header field value,
 * containing the address that the packet actually came from. The host or
 * network address and port number are not required to follow the SIP URI
 * syntax. Specifically, LWS on either side of the ":" or "/" is allowed, as
 * shown here: Even though this specification mandates that the branch parameter
 * be present in all requests, the BNF for the header field indicates that it is
 * optional. This allows interoperation with RFC 2543 elements, which did not
 * have to insert the branch parameter. Two Via header fields are equal if their
 * sent-protocol and sent-by fields are equal, both have the same set of
 * parameters, and the values of all parameters are equal.
 * 
 * 
 * 
 */
public class ViaHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(ViaHeaderHandler.class.getName());

	/**
	 * addViaHeader is used to add the Via Header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return the Via Header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static List<ViaHeader> createViaHeader(Header headerNew)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {
		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");
		List<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
		Random remotetag = new Random();
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);

		String hvalue = null;
		hvalue = headerNew.getValue();
		ViaHeader viaHeader = null;
		if (hvalue == null)
			hvalue = VIA;

		String hvalueh[] = hvalue.split(",");
		for (int i = 0; i < hvalueh.length; i++) {
			hvalue = hvalueh[i];
			String array[] = hvalue.split(" ");
			String array1[] = null;
			String array3[] = null;
			if (array.length > 1) {
				array1 = array[1].split(";");
				array3 = array1[0].split(":");
			}
			String array2[] = array[0].split("/");

			viaHeader = headerFactroy.createViaHeader(array3[0], Integer
			        .parseInt(array3[1]), array2[2], null);

			if (viaHeader != null) {
				if (array1.length > 1) {
					for (int m = 1; m < array1.length; m++) {
						String branch = array1[m];
						int index = branch.indexOf("=");
						String name = branch.substring(0, index);
						String value = branch.substring(index + 1, branch
						        .length());
						viaHeader.setParameter(name, value);
					}
				}
				viaHeaders.add(viaHeader);

				List<Param> param = headerNew.getParam();

				for (Param objParam : param) {
					String paramname = objParam.getName();
					String value = objParam.getValue();
					if (paramname.equals("branch") && (value == null)) {
						value = Integer.toHexString(remotetag.nextInt());
						value = "z9hG4bK" + value;
					}
					viaHeader.setParameter(paramname, value);
				}
				if(param.size() == 0) {
					viaHeader.setBranch("z9hG4bK"+Integer.toHexString(remotetag.nextInt()));
				}
			}
		}

		return viaHeaders;
	}

	/**
	 * removeViaHeader is used to remove the Via Header
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
	public static SIPMessage removeViaHeader(Header header,
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

		ViaHeader via;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			via = (ViaHeader) response.getHeader(ViaHeader.NAME);
		else
			via = (ViaHeader) request.getHeader(ViaHeader.NAME);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			via.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(Via.VIA);
			else
				request.removeHeader(Via.VIA);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}

}
