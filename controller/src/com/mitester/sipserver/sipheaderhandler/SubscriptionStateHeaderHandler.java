/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SubscriptionStateHeaderHandler.java
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

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.SUBSCRIPTIONSTATE;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.SubscriptionState;
import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.header.SubscriptionStateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * SubscriptionStateHeaderHandler is used to create and remove the
 * Subscription-State Header
 * 
 * RFC 3265 says, 3.2.4. Subscriber NOTIFY Behavior ...NOTIFY requests MUST
 * contain "Subscription-State" headers which indicate the status of the
 * subscription. If the "Subscription-State" header value is "active", it means
 * that the subscription has been accepted and (in general) has been authorized.
 * If the header also contains an "expires" parameter, the subscriber SHOULD
 * take it as the authoritative subscription duration and adjust accordingly.
 * The "retry-after" and "reason" parameters have no semantics for "active". If
 * the "Subscription-State" value is "pending", the subscription has been
 * received by the notifier, but there is insufficient policy information to
 * grant or deny the subscription yet. If the header also contains an "expires"
 * parameter, the subscriber SHOULD take it as the authoritative subscription
 * duration and adjust accordingly. No further action is necessary on the part
 * of the subscriber. The "retry-after" and "reason" parameters have no
 * semantics for "pending". If the "Subscription-State" value is "terminated",
 * the subscriber should consider the subscription terminated. The "expires"
 * parameter has no semantics for "terminated". If a reason code is present, the
 * client should behave as described below. If no reason code or an unknown
 * reason code is present, the client MAY attempt to re- subscribe at any time
 * (unless a "retry-after" parameter is present, in which case the client SHOULD
 * NOT attempt re-subscription until after the number of seconds specified by
 * the "retry-after" parameter). The defined reason codes are: deactivated: The
 * subscription has been terminated, but the subscriber SHOULD retry immediately
 * with a new subscription. One primary use of such a status code is to allow
 * migration of subscriptions between nodes. The "retry-after" parameter has no
 * semantics for "deactivated". probation: The subscription has been terminated,
 * but the client SHOULD retry at some later time. If a "retry-after" parameter
 * is also present, the client SHOULD wait at least the number of seconds
 * specified by that parameter before attempting to re-subscribe. rejected: The
 * subscription has been terminated due to change in authorization policy.
 * Clients SHOULD NOT attempt to re-subscribe. The "retry-after" parameter has
 * no semantics for "rejected".timeout: The subscription has been terminated
 * because it was not refreshed before it expired. Clients MAY re-subscribe
 * immediately. The "retry-after" parameter has no semantics for "timeout".
 * giveup: The subscription has been terminated because the notifier could not
 * obtain authorization in a timely fashion. If a "retry- after" parameter is
 * also present, the client SHOULD wait at least the number of seconds specified
 * by that parameter before attempting to re-subscribe; otherwise, the client
 * MAY retry immediately, but will likely get put back into pending state.
 * noresource: The subscription has been terminated because the resource state
 * which was being monitored no longer exists. Clients SHOULD NOT attempt to
 * re-subscribe. The "retry-after" parameter has no semantics for "noresource".
 * 7.2.3. "Subscription-State" Header Subscription-State is added to the
 * definition of the element "request-header" in the SIP message grammar.
 * 
 * 
 * 
 */
public class SubscriptionStateHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(SubscriptionStateHeaderHandler.class.getName());

	/**
	 * addSubscriptionStateHeader is used to add Subscription-State Header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return Subscription-State Header
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static SubscriptionStateHeader createSubscriptionStateHeader(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {
		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactroy = factory.createHeaderFactory();
		((HeaderFactoryImpl) headerFactroy).setPrettyEncoding(true);
		String hvalue = null;
		// ,headerName = null;
		// headerName = headerNew.getName();
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = SUBSCRIPTIONSTATE;
		SubscriptionStateHeader subscription = headerFactroy
		        .createSubscriptionStateHeader(hvalue);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			subscription.setParameter(paramname, value);
		}

		return subscription;
	}

	/**
	 * removeSubscriptionStateHeader is used to remove the Subscription-State
	 * Header header
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
	public static SIPMessage removeSubscriptionStateHeader(Header header,
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
		SubscriptionStateHeader subsState;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			subsState = (SubscriptionStateHeader) response
			        .getHeader(SubscriptionStateHeader.NAME);
		else
			subsState = (SubscriptionStateHeader) request
			        .getHeader(SubscriptionStateHeader.NAME);
		List<Param> removeParams = header.getParam();

		for (Param parameterName : removeParams) {
			subsState.removeParameter(parameterName.getName());
		}

		if (removeParams.size() == 0) {
			if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
				response.removeHeader(SubscriptionState.SUBSCRIPTION_STATE);
			else
				request.removeHeader(SubscriptionState.SUBSCRIPTION_STATE);
		}
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			returnsipMessage = (SIPMessage) response;
		else
			returnsipMessage = (SIPMessage) request;
		return returnsipMessage;
	}
}
