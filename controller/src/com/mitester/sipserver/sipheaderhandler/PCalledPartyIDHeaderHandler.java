/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: PCalledPartyIDHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.SIPURI;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.ims.PCalledPartyIDHeader;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.utility.MiTesterLog;

/**
 * PCalledPartyIDHeaderHandler is used to add and remove the P-CalledParty-ID
 * header
 * 
 * RFC 3455 says,
 * 
 * 4.2 The P-Called-Party-ID header
 * 
 * A proxy server inserts a P-Called-Party-ID header, typically in an INVITE
 * request, en-route to its destination. The header is populated with the
 * Request-URI received by the proxy in the request. The UAS identifies which
 * address-of-record, out of several registered address-of-records, the
 * invitation was sent to (for example, the user may be simultaneously using a
 * personal and a business SIP URIs to receive invitation to sessions). The UAS
 * may use the information to render different distinctive audiovisual alerting
 * tones, depending on the URI used to receive the invitation to the session.
 * Users in the 3GPP IP Multimedia Subsystem (IMS) may get one or several SIP
 * URIs (address-of-record) to identify the user. For instance, a user may get a
 * business SIP URI and a personal one. As an example of utilization, the user
 * may make available the business SIP URI to co-workers and may make available
 * the personal SIP URI to members of the family. At a certain point in time,
 * both the business SIP URI and the personal SIP URI are registered in the SIP
 * registrar, so both URIs can receive invitations to new sessions. When the
 * user receives an invitation to join a session, he/she should be aware of
 * which of the several registered SIP URIs this session was sent to. This
 * requirement is stated in the 3GPP Release 5 requirements on SIP [4]. The
 * problem arises during the terminating side of a session establishment, when
 * the SIP proxy that is serving a UA gets an INVITE, and the SIP server
 * retargets the SIP URI which is present in the Request-URI field, and replaces
 * it by the SIP URI published by the user in the Contact header field of the
 * REGISTER request at registration time. When the UAS receives the SIP INVITE,
 * it cannot determine which address-of-record the request was sent to. One can
 * argue that the To header field conveys the semantics of the called user, and
 * therefore, this extension to SIP is not needed. Although the To header field
 * in SIP may convey the called party ID in most situations, there are two
 * particular cases when the above assumption is not correct: 1. The session has
 * been forwarded, redirected, etc., by previous SIP proxies, before arriving to
 * the proxy which is serving the called user. 2. The UAC builds an INVITE
 * request and the To header field is not the same as the Request-URI. The
 * problem of using the To header field is that this field is populated by the
 * UAC and not modified by proxies in the path. If the UAC, for any reason, did
 * not populate the To header field with the address-of-record of the
 * destination user, then the destination user is not able to distinguish which
 * address-of-record the session was destined. Another possible solution to the
 * problem is built upon the differentiation of the Contact header value between
 * different address-of-record at registration time. The UA can differentiate
 * each address-of-record it registers by assigning a different Contact header
 * value. For instance, when the UA registers the address-of- record sip:id1,
 * the Contact header value can be sip:id1@ua; the registration of sip:id2 can
 * be bound to the Contact value sip:id2@ua.
 * 
 * 
 * 
 */

public class PCalledPartyIDHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(PCalledPartyIDHeaderHandler.class.getName());

	/**
	 * createPCalledPartyIDHeader is used to create P-CalledParty-ID header
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
	public static PCalledPartyIDHeader createPCalledPartyIDHeader(
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

		Address party = null;
		AddressFactory af = factory.createAddressFactory();

		PCalledPartyIDHeader pCalledParty = null;
		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = SIPURI;

		party = af.createAddress(hvalue);
		pCalledParty = hfimpl.createPCalledPartyIDHeader(party);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			if (paramname.equalsIgnoreCase("display-name")) {
				party.setDisplayName(value);
			} else {
				pCalledParty.setParameter(paramname, value);
			}
		}

		return pCalledParty;

	}
}
