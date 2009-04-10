/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SIPHeaderRemover.java
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

import gov.nist.javax.sip.message.SIPMessage;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;

import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipParser;
import com.mitester.sipserver.SipServerConstants;
import com.mitester.utility.MiTesterLog;

/**
 * SIPHeaderRemover.java file is used to remove the headers present in the SIP
 * Request and SIP Responses.
 * 
 * 
 */

public class SIPHeaderRemover {
	private static final Logger LOGGER = MiTesterLog
	        .getLogger(SIPHeaderRemover.class.getName());

	/**
	 * method called during server test execution for removing headers in the
	 * SIP message construction
	 * 
	 * @param name
	 *            to be removed in the SIP message construction
	 * @param removeParams
	 */

	public static SIPMessage removeHeader(
	        List<com.mitester.jaxbparser.server.Header> headers,
	        String SIPmessage, String type) throws SipException,
	        ParseException, InvalidArgumentException, NullPointerException,
	        java.lang.IllegalArgumentException {

		SIPMessage returnSIPMessage = null;
		SIPMessage sipmsgconvert = null;
		if (type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE))
			sipmsgconvert = SipParser.parseSipMessage(SIPmessage);
		else
			sipmsgconvert = SipParser.parseSipMessage(SIPmessage);

		for (com.mitester.jaxbparser.server.Header header : headers) {
			String name = header.getName();
			LOGGER.info("Removing "
			        + SIPHeaders.getSipHeaderfromString(name.toUpperCase())
			                .toString()
			        + " Header From the Request or Response");
			switch (SIPHeaders.getSipHeaderfromString(name.toUpperCase())) {

				case ACCEPT_ENCODING:
					returnSIPMessage = AcceptEncodingHeaderHandler
					        .removeAcceptEncodingHeader(header, sipmsgconvert,
					                type);
					break;

				case ACCEPT:
					returnSIPMessage = AcceptHeaderHanldler.removeAcceptHeader(
					        header, sipmsgconvert, type);
					break;

				case ACCEPT_LANGUAGE:
					returnSIPMessage = AcceptLanguageHeaderHandler
					        .removeAcceptLangaugeHeader(header, sipmsgconvert,
					                type);
					break;

				case ALERT_INFO:
					returnSIPMessage = AlertInfoHeaderHandler
					        .removeAlertInfoHeader(header, sipmsgconvert, type);
					break;

				case ALLOW_EVENTS:
					returnSIPMessage = AllowEventsHeaderHandler
					        .removeAllowEventsHeader(header, sipmsgconvert,
					                type);
					break;

				case ALLOW:
					returnSIPMessage = AllowHeaderHandler.removeAllowHeader(
					        header, sipmsgconvert, type);
					break;

				case AUTHENTICATION_INFO:
					returnSIPMessage = AuthenticationInfoHeaderHandler
					        .removeAuthenticationInfo(header, sipmsgconvert,
					                type);
					break;

				case AUTHORIZATION:
					returnSIPMessage = AuthorizationHeaderHandler
					        .removeAutherizationHeader(header, sipmsgconvert,
					                type);
					break;

				case CALL_ID:
					returnSIPMessage = CallIdHeaderHandler.removeCallIdHeader(
					        header, sipmsgconvert, type);
					break;

				case CALL_INFO:
					returnSIPMessage = CallInfoHeaderHandler
					        .removeCallInfoHeader(header, sipmsgconvert, type);
					break;

				case CONTACT:
					returnSIPMessage = ContactHeaderHandler
					        .removeContactHeader(header, sipmsgconvert, type);
					break;

				case CONTENT_DISPOSITION:
					returnSIPMessage = ContentDispositionHeaderHandler
					        .removeContentDispositionHeader(header,
					                sipmsgconvert, type);
					break;

				case CONTENT_ENCODING:
					returnSIPMessage = ContentEncodingHeaderHandler
					        .removeContentEncodingHeader(header, sipmsgconvert,
					                type);
					break;

				case CONTENT_LANGUAGE:
					returnSIPMessage = ContentLanguageHeaderHandler
					        .removeContentLanguageHeader(header, sipmsgconvert,
					                type);
					break;

				case CONTENT_LENGTH:
					returnSIPMessage = ContentLengthHeaderHandler
					        .removeContentLengthHeader(header, sipmsgconvert,
					                type);
					break;

				case CONTENT_TYPE:
					returnSIPMessage = ContentTypeHeaderHandler
					        .removeContentTypeHeader(header, sipmsgconvert,
					                type);
					break;

				case CSEQ:
					returnSIPMessage = CSeqHeaderHandler.removeCSeqHeader(
					        header, sipmsgconvert, type);
					break;

				case DATE:
					returnSIPMessage = DateHeaderHandler.removeDateHeader(
					        header, sipmsgconvert, type);
					break;

				case ERROR_INFO:
					returnSIPMessage = ErrorInfoHeaderHandler
					        .removeErrorInfoHeader(header, sipmsgconvert, type);
					break;

				case EVENT:
					returnSIPMessage = EventHeaderHandler.removeEventHeader(
					        header, sipmsgconvert, type);
					break;

				case EXPIRES:

					returnSIPMessage = ExpiresHeaderHandler
					        .removeExpiresHeader(header, sipmsgconvert, type);
					break;

				case FROM:
					returnSIPMessage = FromHeaderHandler.removeFromHeader(
					        header, sipmsgconvert, type);
					break;

				case IN_REPLY_TO:
					returnSIPMessage = InReplyToHeaderHandler
					        .removeInReplyToHeader(header, sipmsgconvert, type);
					break;

				case MAX_FORWARDS:
					returnSIPMessage = MaxForwardsHeaderHandler
					        .removeMaxForwardsHeader(header, sipmsgconvert,
					                type);
					break;

				case MIME_VERSION:
					returnSIPMessage = MimeVersionHeaderHandler
					        .removeMimeVersionHeader(header, sipmsgconvert,
					                type);
					break;

				case MIN_SE:
					returnSIPMessage = MinSEHeaderHandler.removeMinSEHeader(
					        header, sipmsgconvert, type);
					break;

				case MIN_EXPIRES:
					returnSIPMessage = MinExpiresHeaderHandler
					        .removeMinExpiresHeader(header, sipmsgconvert, type);
					break;

				case ORGANIZATION:
					returnSIPMessage = OrganizationHeaderHandler
					        .removeOrganizationHeader(header, sipmsgconvert,
					                type);
					break;

				case PRIORITY:
					returnSIPMessage = PriorityHeaderHandler
					        .removePriorityHeader(header, sipmsgconvert, type);
					break;

				case PRIVACY:
					returnSIPMessage = PrivacyHeaderHandler
					        .removePrivacyHeader(header, sipmsgconvert, type);
					break;

				case PROXY_AUTHENTICATE:
					returnSIPMessage = ProxyAuthenticateHeaderHandler
					        .removeProxyAuthenticateHeader(header,
					                sipmsgconvert, type);
					break;

				case PROXY_AUTHORIZATION:
					returnSIPMessage = ProxyAuthorizationHeaderHandler
					        .removeProxyAuthorizationHeader(header,
					                sipmsgconvert, type);
					break;

				case PROXY_REQUIRE:
					returnSIPMessage = ProxyRequireHeaderHandler
					        .removeProxyRequireHeader(header, sipmsgconvert,
					                type);
					break;

				case RACK:
					returnSIPMessage = RAckHeaderHandler.removeRAckHeader(
					        header, sipmsgconvert, type);
					break;

				case REASON:
					returnSIPMessage = ReasonHeaderHandler.removeReasonHeader(
					        header, sipmsgconvert, type);
					break;

				case RECORD_ROUTE:
					returnSIPMessage = RecordRouteHeaderHandler
					        .removeRecordRouteHeader(header, sipmsgconvert,
					                type);
					break;

				case REFER_TO:
					returnSIPMessage = ReferToHeaderHandler
					        .removeReferToHeader(header, sipmsgconvert, type);
					break;

				case REFERRED_BY:
					returnSIPMessage = ReferredByHeaderHandler
					        .removeReferredByHeader(header, sipmsgconvert, type);
					break;

				case REPLACES:
					returnSIPMessage = ReplacesHeaderHandler
					        .removeReplacesHeader(header, sipmsgconvert, type);
					break;

				case SECURITY_CLIENT:
					returnSIPMessage = SecurityClientHeaderHandler
					        .removeSecurityClientHeader(header, sipmsgconvert,
					                type);
					break;

				case SECURITY_SERVER:
					returnSIPMessage = SecurityServerHeaderHandler
					        .removeSecurityServerHeader(header, sipmsgconvert,
					                type);
					break;

				case SECURITY_VERIFY:
					returnSIPMessage = SecurityVerifyHeaderHandler
					        .removeSecurityVerifyHeader(header, sipmsgconvert,
					                type);
					break;

				case SERVICE_ROUTE:
					returnSIPMessage = ServiceRouteHeaderHandler
					        .removeServiceRouteHeader(header, sipmsgconvert,
					                type);
					break;

				case SESSION_EXPIRES:
					returnSIPMessage = SessionExpiresHeaderHandler
					        .removeSessionExpiresHeader(header, sipmsgconvert,
					                type);
					break;

				case REPLY_TO:
					returnSIPMessage = ReplyToHeaderHandler
					        .removeReplyToHeader(header, sipmsgconvert, type);
					break;

				case REQUIRE:
					returnSIPMessage = RequireHeaderHandler
					        .removeRequireHeader(header, sipmsgconvert, type);
					break;

				case RETRY_AFTER:
					returnSIPMessage = RetryAfterHeaderHandler
					        .removeRetryAfterHeader(header, sipmsgconvert, type);
					break;

				case ROUTE:
					returnSIPMessage = RouteHeaderHandler.removeRouteHeader(
					        header, sipmsgconvert, type);
					break;

				case RSEQ:
					returnSIPMessage = RSeqHeaderHandler.removeRSeqHeader(
					        header, sipmsgconvert, type);
					break;

				case SERVER:
					returnSIPMessage = ServerHeaderHandler.removeServerHeader(
					        header, sipmsgconvert, type);
					break;

				case SIP_ETAG:
					returnSIPMessage = SIPETagHeaderHandler
					        .removeSIPETagHeader(header, sipmsgconvert, type);
					break;

				case SIP_IF_MATCH:
					returnSIPMessage = SIPIfMatchHeaderHandler
					        .removeSIPIfMatchHeader(header, sipmsgconvert, type);
					break;

				case SUBJECT:
					returnSIPMessage = SubjectHeaderHandler
					        .removeSubjectHeader(header, sipmsgconvert, type);
					break;

				case SUBSCRIPTION_STATE:
					returnSIPMessage = SubscriptionStateHeaderHandler
					        .removeSubscriptionStateHeader(header,
					                sipmsgconvert, type);
					break;

				case SUPPORTED:
					returnSIPMessage = SupportedHeaderHandler
					        .removeSupportedHeader(header, sipmsgconvert, type);
					break;

				case TIMESTAMP:
					returnSIPMessage = TimeStampHeaderHandler
					        .removeTimeStampHeader(header, sipmsgconvert, type);
					break;

				case TO:
					returnSIPMessage = ToHeaderHandler.removeToHeader(header,
					        sipmsgconvert, type);
					break;

				case UNSUPPORTED:
					returnSIPMessage = UnsupportedHeaderHandler
					        .removeUnsupportedHeader(header, sipmsgconvert,
					                type);
					break;

				case USER_AGENT:
					returnSIPMessage = UserAgentHeaderHandler
					        .removeUserAgentHeader(header, sipmsgconvert, type);
					break;

				case VIA:
					returnSIPMessage = ViaHeaderHandler.removeViaHeader(header,
					        sipmsgconvert, type);
					break;

				case WARNING:
					returnSIPMessage = WarningHeaderHandler
					        .removeWarningHeader(header, sipmsgconvert, type);
					break;

				case WWW_AUTHENTICATE:
					returnSIPMessage = WWWAuthenticateHeaderHandler
					        .removeWWWAuthenticate(header, sipmsgconvert, type);
					break;

				case CUSTOM:

			}
		}
		return returnSIPMessage;

	}
}
