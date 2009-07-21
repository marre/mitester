/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: UdpCommn.java
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
 * Package 						License 											Details
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
package com.mitester.sipserver.headervalidation;

import static com.mitester.sipserver.SipServerConstants.SIPVERSION;
import static com.mitester.sipserver.SipServerConstants.SINGLE_QUOTE;
import static com.mitester.sipserver.SipServerConstants.SPACE_SEP;
import gov.nist.javax.sip.header.extensions.JoinHeader;
import gov.nist.javax.sip.header.extensions.MinSEHeader;
import gov.nist.javax.sip.header.extensions.ReferredByHeader;
import gov.nist.javax.sip.header.extensions.ReplacesHeader;
import gov.nist.javax.sip.header.extensions.SessionExpiresHeader;
import gov.nist.javax.sip.header.ims.PAccessNetworkInfoHeader;
import gov.nist.javax.sip.header.ims.PAssertedIdentityHeader;
import gov.nist.javax.sip.header.ims.PAssociatedURIHeader;
import gov.nist.javax.sip.header.ims.PCalledPartyIDHeader;
import gov.nist.javax.sip.header.ims.PChargingFunctionAddressesHeader;
import gov.nist.javax.sip.header.ims.PChargingVectorHeader;
import gov.nist.javax.sip.header.ims.PMediaAuthorizationHeader;
import gov.nist.javax.sip.header.ims.PPreferredIdentityHeader;
import gov.nist.javax.sip.header.ims.PVisitedNetworkIDHeader;
import gov.nist.javax.sip.header.ims.PrivacyHeader;
import gov.nist.javax.sip.header.ims.SecurityClientHeader;
import gov.nist.javax.sip.header.ims.SecurityServerHeader;
import gov.nist.javax.sip.header.ims.SecurityVerifyHeader;
import gov.nist.javax.sip.header.ims.ServiceRouteHeader;
import gov.nist.javax.sip.message.SIPMessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.sip.header.AcceptEncodingHeader;
import javax.sip.header.AcceptHeader;
import javax.sip.header.AcceptLanguageHeader;
import javax.sip.header.AlertInfoHeader;
import javax.sip.header.AllowEventsHeader;
import javax.sip.header.AllowHeader;
import javax.sip.header.AuthenticationInfoHeader;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.CallInfoHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentDispositionHeader;
import javax.sip.header.ContentEncodingHeader;
import javax.sip.header.ContentLanguageHeader;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.DateHeader;
import javax.sip.header.ErrorInfoHeader;
import javax.sip.header.EventHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.InReplyToHeader;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.MimeVersionHeader;
import javax.sip.header.MinExpiresHeader;
import javax.sip.header.OrganizationHeader;
import javax.sip.header.PriorityHeader;
import javax.sip.header.ProxyAuthenticateHeader;
import javax.sip.header.ProxyAuthorizationHeader;
import javax.sip.header.ProxyRequireHeader;
import javax.sip.header.RAckHeader;
import javax.sip.header.RSeqHeader;
import javax.sip.header.ReasonHeader;
import javax.sip.header.RecordRouteHeader;
import javax.sip.header.ReferToHeader;
import javax.sip.header.ReplyToHeader;
import javax.sip.header.RequireHeader;
import javax.sip.header.RetryAfterHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.SIPETagHeader;
import javax.sip.header.SIPIfMatchHeader;
import javax.sip.header.ServerHeader;
import javax.sip.header.SubjectHeader;
import javax.sip.header.SubscriptionStateHeader;
import javax.sip.header.SupportedHeader;
import javax.sip.header.TimeStampHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.UnsupportedHeader;
import javax.sip.header.UserAgentHeader;
import javax.sip.header.ViaHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.header.WarningHeader;

import org.apache.log4j.Logger;

import com.mitester.sipserver.SIPHeaders;
import com.mitester.utility.MiTesterLog;

/**
 * To check the presence of Headers from incoming SIP Message
 * 
 */
public class CheckHeaderPresence {
	private static final Logger LOGGER = MiTesterLog
			.getLogger(CheckHeaderPresence.class.getName());

	/**
	 * checks the presence of header in the incoming sip message
	 * @param sipMessage
	 *            is need to be validated
	 * @param header
	 *            header are to be checked in the received SIP Message
	 * @throws FileNotFoundException
	 *             if the validateHeader.properties not found
	 * @throws IOException
	 *             throws an IOException while reading file
	 * @throws MissingMandatoryHeaderException
	 *             throws an MissingMandatoryHeaderException if any mandatory
	 *             header is missing in the received SIP MEssage
	 * @throws InvalidValuesHeaderException
	 *             throws an InvalidValuesHeaderException if header syntax is
	 *             mis-match
	 */
	public static void checkHeaderPresence(SIPMessage sipMessage,
			List<com.mitester.jaxbparser.validation.Header> header)
			throws FileNotFoundException, IOException,
			MissingMandatoryHeaderException, InvalidValuesHeaderException {
		LOGGER
				.info("started checking the presence of header in the incoming SIP Message");
		String found = " header is found";
		String notfound = " header is not found";
		boolean isFound = false;
		StringBuilder MMH = new StringBuilder();

		String firstLine = sipMessage.getFirstLine();

		String res = null;

		if (firstLine.startsWith(SIPVERSION))
			res = firstLine.substring(firstLine.indexOf(SPACE_SEP) + 1,
					firstLine.length())
					+ " " + sipMessage.getCSeq().getMethod();
		else
			res = sipMessage.getCSeq().getMethod();

		for (com.mitester.jaxbparser.validation.Header sipHeader : header) {

			String headerName = SIPHeaders.getSipHeaderfromString(
					sipHeader.getName()).toString();

			switch (SIPHeaders.getSipHeaderfromString(sipHeader.getName().toUpperCase())) {

			case ACCEPT_ENCODING: {
				AcceptEncodingHeader acceptEncoding = null;
				acceptEncoding = (AcceptEncodingHeader) sipMessage
						.getHeader(AcceptEncodingHeader.NAME);
				if (acceptEncoding == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case ACCEPT: {
				AcceptHeader acceptHeader = (AcceptHeader) sipMessage
						.getHeader(AcceptHeader.NAME);
				if (acceptHeader == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case ACCEPT_LANGUAGE: {
				AcceptLanguageHeader acceptLanguageHeader = (AcceptLanguageHeader) sipMessage
						.getHeader(AcceptLanguageHeader.NAME);
				if (acceptLanguageHeader == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}

				break;
			}
			case ALERT_INFO: {
				AlertInfoHeader alertinfo = (AlertInfoHeader) sipMessage
						.getHeader(AlertInfoHeader.NAME);
				if (alertinfo == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case ALLOW_EVENTS: {
				AllowEventsHeader allowevent = (AllowEventsHeader) sipMessage
						.getHeader(AllowEventsHeader.NAME);
				if (allowevent == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case ALLOW: {
				AllowHeader allow = (AllowHeader) sipMessage
						.getHeader(AllowHeader.NAME);
				if (allow == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case AUTHENTICATION_INFO: {
				AuthenticationInfoHeader authentication = (AuthenticationInfoHeader) sipMessage
						.getHeader(AuthenticationInfoHeader.NAME);
				if (authentication == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case AUTHORIZATION: {
				AuthorizationHeader authorization = (AuthorizationHeader) sipMessage
						.getHeader(AuthorizationHeader.NAME);
				if (authorization == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case CALL_ID: {
				CallIdHeader callid = sipMessage.getCallId();
				if (callid == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case CALL_INFO: {
				CallInfoHeader callinfo = (CallInfoHeader) sipMessage
						.getHeader(CallInfoHeader.NAME);
				if (callinfo == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case CONTACT: {
				ContactHeader contactHeader = sipMessage.getContactHeader();
				if (contactHeader == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case CONTENT_DISPOSITION: {
				ContentDispositionHeader contentdisposition = sipMessage
						.getContentDisposition();
				if (contentdisposition == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case CONTENT_ENCODING: {
				ContentEncodingHeader contentEncoding = sipMessage
						.getContentEncoding();
				if (contentEncoding == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case CONTENT_LANGUAGE: {
				ContentLanguageHeader contentlanguage = sipMessage
						.getContentLanguage();
				if (contentlanguage == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case CONTENT_LENGTH: {

				ContentLengthHeader contentlength = sipMessage
						.getContentLength();
				if (contentlength == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case CONTENT_TYPE: {

				ContentTypeHeader contenttype = sipMessage
						.getContentTypeHeader();
				if (contenttype == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case CSEQ: {
				CSeqHeader cSeqHeader = sipMessage.getCSeq();
				if (cSeqHeader == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case DATE: {
				DateHeader date = (DateHeader) sipMessage
						.getHeader(DateHeader.NAME);
				if (date == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case ERROR_INFO: {
				ErrorInfoHeader errorInfo = (ErrorInfoHeader) sipMessage
						.getHeader(ErrorInfoHeader.NAME);
				if (errorInfo == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case EVENT: {
				EventHeader event = (EventHeader) sipMessage
						.getHeader(EventHeader.NAME);
				if (event == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case EXPIRES: {
				ExpiresHeader expires = sipMessage.getExpires();
				if (expires == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case FROM: {
				FromHeader fromHeader = sipMessage.getFrom();
				if (fromHeader == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case IN_REPLY_TO: {
				InReplyToHeader inreplyto = (InReplyToHeader) sipMessage
						.getHeader(InReplyToHeader.NAME);
				if (inreplyto == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case MAX_FORWARDS: {
				MaxForwardsHeader maxforwards = sipMessage.getMaxForwards();
				if (maxforwards == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case MIME_VERSION: {
				MimeVersionHeader mimeVersion = (MimeVersionHeader) sipMessage
						.getHeader(MimeVersionHeader.NAME);
				if (mimeVersion == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case MIN_SE: {
				MinSEHeader minse = (MinSEHeader) sipMessage
						.getHeader(MinSEHeader.NAME);
				if (minse == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case MIN_EXPIRES: {
				MinExpiresHeader minexpires = (MinExpiresHeader) sipMessage
						.getHeader(MinExpiresHeader.NAME);
				if (minexpires == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case ORGANIZATION: {
				OrganizationHeader org = (OrganizationHeader) sipMessage
						.getHeader(OrganizationHeader.NAME);
				if (org == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case PRIORITY: {
				PriorityHeader priority = (PriorityHeader) sipMessage
						.getHeader(PriorityHeader.NAME);
				if (priority == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case PRIVACY: {
				PrivacyHeader privacy = (PrivacyHeader) sipMessage
						.getHeader(PrivacyHeader.NAME);
				if (privacy == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case PROXY_AUTHENTICATE: {
				ProxyAuthenticateHeader proxyauth = (ProxyAuthenticateHeader) sipMessage
						.getHeader(ProxyAuthenticateHeader.NAME);
				if (proxyauth == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case PROXY_AUTHORIZATION: {
				ProxyAuthorizationHeader proxyauthorization = (ProxyAuthorizationHeader) sipMessage
						.getHeader(ProxyAuthorizationHeader.NAME);
				if (proxyauthorization == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case PROXY_REQUIRE: {
				ProxyRequireHeader proxyrequire = (ProxyRequireHeader) sipMessage
						.getHeader(ProxyRequireHeader.NAME);
				if (proxyrequire == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case RACK: {
				RAckHeader rack = (RAckHeader) sipMessage
						.getHeader(RAckHeader.NAME);
				if (rack == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case REASON: {
				ReasonHeader reason = (ReasonHeader) sipMessage
						.getHeader(ReasonHeader.NAME);
				if (reason == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case RECORD_ROUTE: {
				RecordRouteHeader record = (RecordRouteHeader) sipMessage
						.getHeader(RecordRouteHeader.NAME);
				if (record == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case REFER_TO: {
				ReferToHeader referto = (ReferToHeader) sipMessage
						.getHeader(ReferToHeader.NAME);
				if (referto == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case REFERRED_BY: {
				ReferredByHeader referby = (ReferredByHeader) sipMessage
						.getHeader(ReferredByHeader.NAME);
				if (referby == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case REPLACES: {
				ReplacesHeader replace = (ReplacesHeader) sipMessage
						.getHeader(ReplacesHeader.NAME);
				if (replace == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case SECURITY_CLIENT: {
				SecurityClientHeader securityClientheader = (SecurityClientHeader) sipMessage
						.getHeader(SecurityClientHeader.NAME);
				if (securityClientheader == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case SECURITY_SERVER: {
				SecurityServerHeader securityServerheader = (SecurityServerHeader) sipMessage
						.getHeader(SecurityServerHeader.NAME);
				if (securityServerheader == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case SECURITY_VERIFY: {
				SecurityVerifyHeader securityVerifyheader = (SecurityVerifyHeader) sipMessage
						.getHeader(SecurityVerifyHeader.NAME);
				if (securityVerifyheader == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case SERVICE_ROUTE: {
				ServiceRouteHeader serviceroute = (ServiceRouteHeader) sipMessage
						.getHeader(ServiceRouteHeader.NAME);
				if (serviceroute == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case SESSION_EXPIRES: {
				SessionExpiresHeader session = (SessionExpiresHeader) sipMessage
						.getHeader(SessionExpiresHeader.NAME);
				if (session == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case REPLY_TO: {
				ReplyToHeader replyto = (ReplyToHeader) sipMessage
						.getHeader(ReplyToHeader.NAME);
				if (replyto == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case REQUIRE: {
				RequireHeader require = (RequireHeader) sipMessage
						.getHeader(RequireHeader.NAME);
				if (require == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case RETRY_AFTER: {
				RetryAfterHeader retryafter = (RetryAfterHeader) sipMessage
						.getHeader(RetryAfterHeader.NAME);
				if (retryafter == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case ROUTE: {
				RouteHeader route = (RouteHeader) sipMessage
						.getHeader(RouteHeader.NAME);
				if (route == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case RSEQ: {
				RSeqHeader rs = (RSeqHeader) sipMessage
						.getHeader(RSeqHeader.NAME);
				if (rs == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case SERVER: {
				ServerHeader server = (ServerHeader) sipMessage
						.getHeader(ServerHeader.NAME);
				if (server == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case SIP_ETAG: {
				SIPETagHeader sipetag = (SIPETagHeader) sipMessage
						.getHeader(SIPETagHeader.NAME);
				if (sipetag == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case SIP_IF_MATCH: {
				SIPIfMatchHeader sipifmatch = (SIPIfMatchHeader) sipMessage
						.getHeader(SIPIfMatchHeader.NAME);
				if (sipifmatch == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case SUBJECT: {
				SubjectHeader subject = (SubjectHeader) sipMessage
						.getHeader(SubjectHeader.NAME);
				if (subject == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case SUBSCRIPTION_STATE: {
				SubscriptionStateHeader subscription = (SubscriptionStateHeader) sipMessage
						.getHeader(SubscriptionStateHeader.NAME);
				if (subscription == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case SUPPORTED: {
				SupportedHeader supported = (SupportedHeader) sipMessage
						.getHeader(SupportedHeader.NAME);
				if (supported == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case TIMESTAMP: {
				TimeStampHeader time = (TimeStampHeader) sipMessage
						.getHeader(TimeStampHeader.NAME);
				if (time == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case TO: {
				ToHeader toHeader = (ToHeader) sipMessage
						.getHeader(ToHeader.NAME);
				if (toHeader == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case UNSUPPORTED: {
				UnsupportedHeader unsupported = (UnsupportedHeader) sipMessage
						.getHeader(UnsupportedHeader.NAME);
				if (unsupported == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case USER_AGENT: {
				UserAgentHeader useragent = (UserAgentHeader) sipMessage
						.getHeader(UserAgentHeader.NAME);
				if (useragent == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case VIA: {
				ViaHeader viaheader = (ViaHeader) sipMessage
						.getHeader(ViaHeader.NAME);
				if (viaheader == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case WARNING: {
				WarningHeader warning = (WarningHeader) sipMessage
						.getHeader(WarningHeader.NAME);
				if (warning == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case WWW_AUTHENTICATE: {
				WWWAuthenticateHeader wwwauth = (WWWAuthenticateHeader) sipMessage
						.getHeader(WWWAuthenticateHeader.NAME);
				if (wwwauth == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}

			case P_ACCESS_NETWORK_INFO: {

				PAccessNetworkInfoHeader PAccessNetworkInfo = (PAccessNetworkInfoHeader) sipMessage
						.getHeader(PAccessNetworkInfoHeader.NAME);
				if (PAccessNetworkInfo == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}

			case P_ASSERTED_IDENTITY: {
				PAssertedIdentityHeader pAssertedIdentity = (PAssertedIdentityHeader) sipMessage
						.getHeader(PAssertedIdentityHeader.NAME);
				if (pAssertedIdentity == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case P_ASSOCIATED_URI: {
				PAssociatedURIHeader pAssociatedURI = (PAssociatedURIHeader) sipMessage
						.getHeader(PAssociatedURIHeader.NAME);
				if (pAssociatedURI == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case P_CALLED_PARTY_ID: {
				PCalledPartyIDHeader pCalledParty = (PCalledPartyIDHeader) sipMessage
						.getHeader(PCalledPartyIDHeader.NAME);
				if (pCalledParty == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case P_CHARGING_FUNCTION_ADDRESSES: {
				PChargingFunctionAddressesHeader PChargingFunAdd = (PChargingFunctionAddressesHeader) sipMessage
						.getHeader(PChargingFunctionAddressesHeader.NAME);
				if (PChargingFunAdd == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case P_CHARGING_VECTOR: {
				PChargingVectorHeader pChargingVecotr = (PChargingVectorHeader) sipMessage
						.getHeader(PChargingVectorHeader.NAME);
				if (pChargingVecotr == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case P_MEDIA_AUTHORIZATION: {
				PMediaAuthorizationHeader MediaAuth = (PMediaAuthorizationHeader) sipMessage
						.getHeader(PMediaAuthorizationHeader.NAME);
				if (MediaAuth == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case P_PREFERRED_IDENTITY: {
				PPreferredIdentityHeader pprefrerredidentity = (PPreferredIdentityHeader) sipMessage
						.getHeader(PPreferredIdentityHeader.NAME);
				if (pprefrerredidentity == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case P_VISITED_NETWORK_ID: {
				PVisitedNetworkIDHeader pvistednetworkid = (PVisitedNetworkIDHeader) sipMessage
						.getHeader(PVisitedNetworkIDHeader.NAME);
				if (pvistednetworkid == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case JOIN: {
				JoinHeader joinHeader = (JoinHeader) sipMessage
						.getHeader(JoinHeader.NAME);
				if (joinHeader == null) {
					LOGGER.info(headerName + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
							+ SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			case CUSTOM: {
				Header customHeader = sipMessage.getHeader(sipHeader.getName());
				if (customHeader == null) {
					LOGGER.info(sipHeader.getName() + notfound);
					isFound = true;
					MMH.append(SINGLE_QUOTE + sipHeader.getName()
							+ SINGLE_QUOTE + SPACE_SEP);
				} else {
					LOGGER.info(headerName + found);
				}
				break;
			}
			}
		}
		if (isFound) {
			throw new MissingMandatoryHeaderException(MMH.toString()
					+ "header(s) missing in the " + res
					+ " SIP message");

		}
	}
}
