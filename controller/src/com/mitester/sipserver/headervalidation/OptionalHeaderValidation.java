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

import static com.mitester.sipserver.ValidationKeywords.ACCEPTENCODING;
import static com.mitester.sipserver.ValidationKeywords.ACCEPT;
import static com.mitester.sipserver.ValidationKeywords.ACCEPTLANGUAGE;
import static com.mitester.sipserver.ValidationKeywords.ALERTINFO;
import static com.mitester.sipserver.ValidationKeywords.ALLOW;
import static com.mitester.sipserver.ValidationKeywords.ALLOWEVENTS;
import static com.mitester.sipserver.ValidationKeywords.AUTHENTICATIONINFO;
import static com.mitester.sipserver.ValidationKeywords.AUTHORIZATION;
import static com.mitester.sipserver.ValidationKeywords.CALLID;
import static com.mitester.sipserver.ValidationKeywords.CALLINFO;
import static com.mitester.sipserver.ValidationKeywords.CONTACT;
import static com.mitester.sipserver.ValidationKeywords.CONTENTDISPOSITION;
import static com.mitester.sipserver.ValidationKeywords.CONTENTENCODING;
import static com.mitester.sipserver.ValidationKeywords.CONTENTLANGUAGE;
import static com.mitester.sipserver.ValidationKeywords.CONTENTLENGTH;
import static com.mitester.sipserver.ValidationKeywords.CONTENTTYPE;
import static com.mitester.sipserver.ValidationKeywords.CSEQ;
import static com.mitester.sipserver.ValidationKeywords.DATE;
import static com.mitester.sipserver.ValidationKeywords.ERRORINFO;
import static com.mitester.sipserver.ValidationKeywords.EVENT;
import static com.mitester.sipserver.ValidationKeywords.EXPIRES;
import static com.mitester.sipserver.ValidationKeywords.FROM;
import static com.mitester.sipserver.ValidationKeywords.INREPLYTO;
import static com.mitester.sipserver.ValidationKeywords.JOIN;
import static com.mitester.sipserver.ValidationKeywords.MAXFORWARDS;
import static com.mitester.sipserver.ValidationKeywords.MIMEVERSION;
import static com.mitester.sipserver.ValidationKeywords.MINEXPIRES;
import static com.mitester.sipserver.ValidationKeywords.MINSE;
import static com.mitester.sipserver.ValidationKeywords.ORGANIZATION;
import static com.mitester.sipserver.ValidationKeywords.PACCESSNETWORKINFO;
import static com.mitester.sipserver.ValidationKeywords.PASSERTEDIDENTITY;
import static com.mitester.sipserver.ValidationKeywords.PASSOCIATEDURI;
import static com.mitester.sipserver.ValidationKeywords.PCALLEDPARTYID;
import static com.mitester.sipserver.ValidationKeywords.PCHARGINGFUNCTIONADDRESSES;
import static com.mitester.sipserver.ValidationKeywords.PCHARGINGVECTOR;
import static com.mitester.sipserver.ValidationKeywords.PMEDIAAUTHORIZATION;
import static com.mitester.sipserver.ValidationKeywords.PPREFERREDIDENTITY;
import static com.mitester.sipserver.ValidationKeywords.PRIORITY;
import static com.mitester.sipserver.ValidationKeywords.PRIVACY;
import static com.mitester.sipserver.ValidationKeywords.PROXYAUTHENTICATE;
import static com.mitester.sipserver.ValidationKeywords.PROXYAUTHORIZATION;
import static com.mitester.sipserver.ValidationKeywords.PROXYREQUIRE;
import static com.mitester.sipserver.ValidationKeywords.PVISITEDNETWORKID;
import static com.mitester.sipserver.ValidationKeywords.RACK;
import static com.mitester.sipserver.ValidationKeywords.REASON;
import static com.mitester.sipserver.ValidationKeywords.RECORDROUTE;
import static com.mitester.sipserver.ValidationKeywords.REFERREDBY;
import static com.mitester.sipserver.ValidationKeywords.REFERTO;
import static com.mitester.sipserver.ValidationKeywords.REPLACES;
import static com.mitester.sipserver.ValidationKeywords.REPLYTO;
import static com.mitester.sipserver.ValidationKeywords.REQUIRE;
import static com.mitester.sipserver.ValidationKeywords.RETRYAFTER;
import static com.mitester.sipserver.ValidationKeywords.ROUTE;
import static com.mitester.sipserver.ValidationKeywords.RSEQ;
import static com.mitester.sipserver.ValidationKeywords.SECURITYCLIENT;
import static com.mitester.sipserver.ValidationKeywords.SECURITYSERVER;
import static com.mitester.sipserver.ValidationKeywords.SECURITYVERIFY;
import static com.mitester.sipserver.ValidationKeywords.SERVER;
import static com.mitester.sipserver.ValidationKeywords.SERVICEROUTE;
import static com.mitester.sipserver.ValidationKeywords.SESSIONEXPIRES;
import static com.mitester.sipserver.ValidationKeywords.SIPETAG;
import static com.mitester.sipserver.ValidationKeywords.SIPIFMATCH;
import static com.mitester.sipserver.ValidationKeywords.SUBJECT;
import static com.mitester.sipserver.ValidationKeywords.SUBSCRIPTIONSTATE;
import static com.mitester.sipserver.ValidationKeywords.SUPPORTED;
import static com.mitester.sipserver.ValidationKeywords.TO;
import static com.mitester.sipserver.ValidationKeywords.TIMESTAMP;
import static com.mitester.sipserver.ValidationKeywords.UNSUPPORTED;
import static com.mitester.sipserver.ValidationKeywords.USERAGENT;
import static com.mitester.sipserver.ValidationKeywords.VIA;
import static com.mitester.sipserver.ValidationKeywords.WARNING;
import static com.mitester.sipserver.ValidationKeywords.WWWAUTHENTICATE;
import static com.mitester.sipserver.headervalidation.Config.CONFIG;
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
import java.util.regex.Pattern;

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
 * To validate optional headers defined in the validate.xml file
 * 
 */
public class OptionalHeaderValidation {
	private static final Logger LOGGER = MiTesterLog
			.getLogger(OptionalHeaderValidation.class.getName());

	/**
	 * To validate the optional header in the received sip message
	 * 
	 * @param sipMessage
	 *            it holds the sip message needs to be validated
	 * @param header
	 *            it holds an header name to be validated
	 * @throws FileNotFoundException
	 *             throws an File not found Exception
	 * @throws IOException
	 *             it throws an IOException while reading an file
	 * @throws InvalidValuesHeaderException
	 *             throws an InvalidValuesHeaderException when header syntax is
	 *             mis-match
	 */
	public static void validateOptionalHeader(SIPMessage sipMessage,
			List<com.mitester.jaxbparser.validation.Header> header)
			throws IOException,
			InvalidValuesHeaderException {
		LOGGER.info("Started optional header validation");
		boolean isMatch = false;
		StringBuilder Invalid = new StringBuilder();

		String firstLine = sipMessage.getFirstLine();

		String res = null;

		if (firstLine.startsWith(SIPVERSION))
			res = firstLine.substring(firstLine.indexOf(SPACE_SEP) + 1,
					firstLine.length())
					+ " " + sipMessage.getCSeq().getMethod();
		else
			res = sipMessage.getCSeq().getMethod();

		String match = " header syntax is matched";
		String notfound = " header is not found";
		for (com.mitester.jaxbparser.validation.Header sipHeader : header) {

			String headerName = SIPHeaders.getSipHeaderfromString(
					sipHeader.getName().toUpperCase()).toString();
			switch (SIPHeaders.getSipHeaderfromString(sipHeader.getName()
					.toUpperCase())) {

			case ACCEPT_ENCODING: {
				AcceptEncodingHeader acceptEncoding = (AcceptEncodingHeader) sipMessage
						.getHeader(AcceptEncodingHeader.NAME);
				if (acceptEncoding != null) {

					// if (CONFIG.isKeyExists(ACCEPTENCODING)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(ACCEPTENCODING);
					boolean matchfound = Pattern.matches(regx, acceptEncoding
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case ACCEPT: {
				AcceptHeader acceptHeader = (AcceptHeader) sipMessage
						.getHeader(AcceptHeader.NAME);
				if (acceptHeader != null) {
					// if (CONFIG.isKeyExists(ACCEPT)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(ACCEPT);
					boolean matchfound = Pattern.matches(regx, acceptHeader
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}

			case ACCEPT_LANGUAGE: {
				AcceptLanguageHeader acceptLanguageHeader = (AcceptLanguageHeader) sipMessage
						.getHeader(AcceptLanguageHeader.NAME);
				if (acceptLanguageHeader != null) {
					// if (CONFIG.isKeyExists(ACCEPTLANGUAGE)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(ACCEPTLANGUAGE);
					boolean matchfound = Pattern.matches(regx,
							acceptLanguageHeader.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case ALERT_INFO: {
				AlertInfoHeader alertinfo = (AlertInfoHeader) sipMessage
						.getHeader(AlertInfoHeader.NAME);
				if (alertinfo != null) {

					// if (CONFIG.isKeyExists(ALERTINFO)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(ALERTINFO);
					boolean matchfound = Pattern.matches(regx, alertinfo
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case ALLOW_EVENTS: {
				AllowEventsHeader allowevent = (AllowEventsHeader) sipMessage
						.getHeader(AllowEventsHeader.NAME);
				if (allowevent != null) {

					// if (CONFIG.isKeyExists(ALLOWEVENTS)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(ALLOWEVENTS);
					boolean matchfound = Pattern.matches(regx, allowevent
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case ALLOW: {
				AllowHeader allow = (AllowHeader) sipMessage
						.getHeader(AllowHeader.NAME);
				if (allow != null) {

					// if (CONFIG.isKeyExists(ALLOW)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(ALLOW);
					boolean matchfound = Pattern.matches(regx, allow.toString()
							.trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case AUTHENTICATION_INFO: {
				AuthenticationInfoHeader authentication = (AuthenticationInfoHeader) sipMessage
						.getHeader(AuthenticationInfoHeader.NAME);
				if (authentication != null) {

					// if (CONFIG.isKeyExists(AUTHENTICATIONINFO)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(AUTHENTICATIONINFO);
					boolean matchfound = Pattern.matches(regx, authentication
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case AUTHORIZATION: {
				AuthorizationHeader authorization = (AuthorizationHeader) sipMessage
						.getHeader(AuthorizationHeader.NAME);
				if (authorization != null) {

					// if (CONFIG.isKeyExists(AUTHORIZATION)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(AUTHORIZATION);
					boolean matchfound = Pattern.matches(regx, authorization
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case CALL_ID: {
				CallIdHeader callid = sipMessage.getCallId();
				if (callid != null) {

					// if (CONFIG.isKeyExists(CALLID)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(CALLID);
					boolean matchfound = Pattern.matches(regx, callid
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case CALL_INFO: {
				CallInfoHeader callinfo = (CallInfoHeader) sipMessage
						.getHeader(CallInfoHeader.NAME);
				if (callinfo != null) {

					// if (CONFIG.isKeyExists(CALLINFO)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(CALLINFO);
					boolean matchfound = Pattern.matches(regx, callinfo
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case CONTACT: {
				ContactHeader contactHeader = sipMessage.getContactHeader();
				if (contactHeader != null) {

					// if (CONFIG.isKeyExists(CONTACT)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(CONTACT);
					boolean matchfound = Pattern.matches(regx, contactHeader
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case CONTENT_DISPOSITION: {
				ContentDispositionHeader contentdisposition = sipMessage
						.getContentDisposition();
				if (contentdisposition != null) {

					// if (CONFIG.isKeyExists(CONTENTDISPOSITION)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(CONTENTDISPOSITION);
					boolean matchfound = Pattern.matches(regx,
							contentdisposition.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case CONTENT_ENCODING: {
				ContentEncodingHeader contentEncoding = sipMessage
						.getContentEncoding();
				if (contentEncoding != null) {

					// if (CONFIG.isKeyExists(CONTENTENCODING)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(CONTENTENCODING);
					boolean matchfound = Pattern.matches(regx, contentEncoding
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case CONTENT_LANGUAGE: {
				ContentLanguageHeader contentlanguage = sipMessage
						.getContentLanguage();
				if (contentlanguage != null) {

					// if (CONFIG.isKeyExists(CONTENTLANGUAGE)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(CONTENTLANGUAGE);
					boolean matchfound = Pattern.matches(regx, contentlanguage
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case CONTENT_LENGTH: {

				ContentLengthHeader contentlength = sipMessage
						.getContentLength();
				if (contentlength != null) {

					// if (CONFIG.isKeyExists(CONTENTLENGTH)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(CONTENTLENGTH);
					boolean matchfound = Pattern.matches(regx, contentlength
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case CONTENT_TYPE: {
				ContentTypeHeader contenttype = sipMessage
						.getContentTypeHeader();
				if (contenttype != null) {

					// if (CONFIG.isKeyExists(CONTENTTYPE)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(CONTENTTYPE);
					boolean matchfound = Pattern.matches(regx, contenttype
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case CSEQ: {
				CSeqHeader cSeqHeader = sipMessage.getCSeq();
				if (cSeqHeader != null) {

					// if (CONFIG.isKeyExists(CSEQ)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(CSEQ);
					boolean matchfound = Pattern.matches(regx, cSeqHeader
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case DATE: {
				DateHeader date = (DateHeader) sipMessage
						.getHeader(DateHeader.NAME);
				if (date != null) {

					// if (CONFIG.isKeyExists(DATE)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(DATE);
					boolean matchfound = Pattern.matches(regx, date.toString()
							.trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case ERROR_INFO: {
				ErrorInfoHeader errorInfo = (ErrorInfoHeader) sipMessage
						.getHeader(ErrorInfoHeader.NAME);
				if (errorInfo != null) {

					// if (CONFIG.isKeyExists(ERRORINFO)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(ERRORINFO);
					boolean matchfound = Pattern.matches(regx, errorInfo
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case EVENT: {
				EventHeader event = (EventHeader) sipMessage
						.getHeader(EventHeader.NAME);
				if (event != null) {
					// if (CONFIG.isKeyExists(EVENT)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(EVENT);
					boolean matchfound = Pattern.matches(regx, event.toString()
							.trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case EXPIRES: {
				ExpiresHeader expires = sipMessage.getExpires();
				if (expires != null) {

					// if (CONFIG.isKeyExists(EXPIRES)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(EXPIRES);
					boolean matchfound = Pattern.matches(regx, expires
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case FROM: {
				FromHeader fromHeader = sipMessage.getFrom();
				if (fromHeader != null) {

					// if (CONFIG.isKeyExists(FROM)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(FROM);
					boolean matchfound = Pattern.matches(regx, fromHeader
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case IN_REPLY_TO: {
				InReplyToHeader inreplyto = (InReplyToHeader) sipMessage
						.getHeader(InReplyToHeader.NAME);
				if (inreplyto != null) {

					// if (CONFIG.isKeyExists(INREPLYTO)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(INREPLYTO);
					boolean matchfound = Pattern.matches(regx, inreplyto
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case MAX_FORWARDS: {
				MaxForwardsHeader maxforwards = sipMessage.getMaxForwards();
				if (maxforwards != null) {

					// if (CONFIG.isKeyExists(MAXFORWARDS)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(MAXFORWARDS);
					boolean matchfound = Pattern.matches(regx, maxforwards
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case MIME_VERSION: {
				MimeVersionHeader mimeVersion = (MimeVersionHeader) sipMessage
						.getHeader(MimeVersionHeader.NAME);
				if (mimeVersion != null) {

					// if (CONFIG.isKeyExists(MIMEVERSION)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(MIMEVERSION);
					boolean matchfound = Pattern.matches(regx, mimeVersion
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case MIN_SE: {
				MinSEHeader minse = (MinSEHeader) sipMessage
						.getHeader(MinSEHeader.NAME);
				if (minse != null) {

					// if (CONFIG.isKeyExists(MINSE)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(MINSE);
					boolean matchfound = Pattern.matches(regx, minse.toString()
							.trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case MIN_EXPIRES: {
				MinExpiresHeader minexpires = (MinExpiresHeader) sipMessage
						.getHeader(MinExpiresHeader.NAME);
				if (minexpires != null) {

					// if (CONFIG.isKeyExists(MINEXPIRES)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(MINEXPIRES);
					boolean matchfound = Pattern.matches(regx, minexpires
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case ORGANIZATION: {
				OrganizationHeader org = (OrganizationHeader) sipMessage
						.getHeader(OrganizationHeader.NAME);
				if (org != null) {

					// if (CONFIG.isKeyExists(ORGANIZATION)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(ORGANIZATION);
					boolean matchfound = Pattern.matches(regx, org.toString()
							.trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case PRIORITY: {
				PriorityHeader priority = (PriorityHeader) sipMessage
						.getHeader(PriorityHeader.NAME);
				if (priority != null) {

					// if (CONFIG.isKeyExists(PRIORITY)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PRIORITY);
					boolean matchfound = Pattern.matches(regx, priority
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case PRIVACY: {
				PrivacyHeader privacy = (PrivacyHeader) sipMessage
						.getHeader(PrivacyHeader.NAME);
				if (privacy != null) {

					// if (CONFIG.isKeyExists(PRIVACY)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PRIVACY);
					boolean matchfound = Pattern.matches(regx, privacy
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case PROXY_AUTHENTICATE: {
				ProxyAuthenticateHeader proxyauth = (ProxyAuthenticateHeader) sipMessage
						.getHeader(ProxyAuthenticateHeader.NAME);
				if (proxyauth != null) {

					// if (CONFIG.isKeyExists(PROXYAUTHENTICATE)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PROXYAUTHENTICATE);
					boolean matchfound = Pattern.matches(regx, proxyauth
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case PROXY_AUTHORIZATION: {
				ProxyAuthorizationHeader proxyauthorization = (ProxyAuthorizationHeader) sipMessage
						.getHeader(ProxyAuthorizationHeader.NAME);
				if (proxyauthorization != null) {

					// if (CONFIG.isKeyExists(PROXYAUTHORIZATION)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PROXYAUTHORIZATION);
					boolean matchfound = Pattern.matches(regx,
							proxyauthorization.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case PROXY_REQUIRE: {
				ProxyRequireHeader proxyrequire = (ProxyRequireHeader) sipMessage
						.getHeader(ProxyRequireHeader.NAME);
				if (proxyrequire != null) {

					// if (CONFIG.isKeyExists(PROXYREQUIRE)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PROXYREQUIRE);
					boolean matchfound = Pattern.matches(regx, proxyrequire
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case RACK: {
				RAckHeader rack = (RAckHeader) sipMessage
						.getHeader(RAckHeader.NAME);
				if (rack != null) {

					// if (CONFIG.isKeyExists(RACK)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(RACK);
					boolean matchfound = Pattern.matches(regx, rack.toString()
							.trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case REASON: {
				ReasonHeader reason = (ReasonHeader) sipMessage
						.getHeader(ReasonHeader.NAME);
				if (reason != null) {

					// if (CONFIG.isKeyExists(REASON)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(REASON);
					boolean matchfound = Pattern.matches(regx, reason
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case RECORD_ROUTE: {
				RecordRouteHeader record = (RecordRouteHeader) sipMessage
						.getHeader(RecordRouteHeader.NAME);
				if (record != null) {

					// if (CONFIG.isKeyExists(RECORDROUTE)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(RECORDROUTE);
					boolean matchfound = Pattern.matches(regx, record
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case REFER_TO: {
				ReferToHeader referto = (ReferToHeader) sipMessage
						.getHeader(ReferToHeader.NAME);
				if (referto != null) {

					// if (CONFIG.isKeyExists(REFERTO)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(REFERTO);
					boolean matchfound = Pattern.matches(regx, referto
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case REFERRED_BY: {
				ReferredByHeader referby = (ReferredByHeader) sipMessage
						.getHeader(ReferredByHeader.NAME);
				if (referby != null) {

					// if (CONFIG.isKeyExists(REFERREDBY)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(REFERREDBY);
					boolean matchfound = Pattern.matches(regx, referby
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case REPLACES: {
				ReplacesHeader replace = (ReplacesHeader) sipMessage
						.getHeader(ReplacesHeader.NAME);
				if (replace != null) {

					// if (CONFIG.isKeyExists(REPLACES)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(REPLACES);
					boolean matchfound = Pattern.matches(regx, replace
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case SECURITY_CLIENT: {
				SecurityClientHeader securityClientheader = (SecurityClientHeader) sipMessage
						.getHeader(SecurityClientHeader.NAME);
				if (securityClientheader != null) {

					// if (CONFIG.isKeyExists(SECURITYCLIENT)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(SECURITYCLIENT);
					boolean matchfound = Pattern.matches(regx,
							securityClientheader.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case SECURITY_SERVER: {
				SecurityServerHeader securityServerheader = (SecurityServerHeader) sipMessage
						.getHeader(SecurityServerHeader.NAME);
				if (securityServerheader != null) {

					// if (CONFIG.isKeyExists(SECURITYSERVER)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(SECURITYSERVER);
					boolean matchfound = Pattern.matches(regx,
							securityServerheader.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case SECURITY_VERIFY: {
				SecurityVerifyHeader securityVerifyheader = (SecurityVerifyHeader) sipMessage
						.getHeader(SecurityVerifyHeader.NAME);
				if (securityVerifyheader != null) {

					// if (CONFIG.isKeyExists(SECURITYVERIFY)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(SECURITYVERIFY);
					boolean matchfound = Pattern.matches(regx,
							securityVerifyheader.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case SERVICE_ROUTE: {
				ServiceRouteHeader serviceroute = (ServiceRouteHeader) sipMessage
						.getHeader(ServiceRouteHeader.NAME);
				if (serviceroute != null) {

					// if (CONFIG.isKeyExists(SERVICEROUTE)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(SERVICEROUTE);
					boolean matchfound = Pattern.matches(regx, serviceroute
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case SESSION_EXPIRES: {
				SessionExpiresHeader session = (SessionExpiresHeader) sipMessage
						.getHeader(SessionExpiresHeader.NAME);
				if (session != null) {

					// if (CONFIG.isKeyExists(SESSIONEXPIRES)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(SESSIONEXPIRES);
					boolean matchfound = Pattern.matches(regx, session
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case REPLY_TO: {
				ReplyToHeader replyto = (ReplyToHeader) sipMessage
						.getHeader(ReplyToHeader.NAME);
				if (replyto != null) {

					// if (CONFIG.isKeyExists(REPLYTO)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(REPLYTO);
					boolean matchfound = Pattern.matches(regx, replyto
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case REQUIRE: {
				RequireHeader require = (RequireHeader) sipMessage
						.getHeader(RequireHeader.NAME);
				if (require != null) {

					// if (CONFIG.isKeyExists(REQUIRE)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(REQUIRE);
					boolean matchfound = Pattern.matches(regx, require
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case RETRY_AFTER: {
				RetryAfterHeader retryafter = (RetryAfterHeader) sipMessage
						.getHeader(RetryAfterHeader.NAME);
				if (retryafter != null) {
					// if (CONFIG.isKeyExists(RETRYAFTER)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(RETRYAFTER);
					boolean matchfound = Pattern.matches(regx, retryafter
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case ROUTE: {
				RouteHeader route = (RouteHeader) sipMessage
						.getHeader(RouteHeader.NAME);
				if (route != null) {
					// if (CONFIG.isKeyExists(ROUTE)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(ROUTE);
					boolean matchfound = Pattern.matches(regx, route.toString()
							.trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case RSEQ: {
				RSeqHeader rs = (RSeqHeader) sipMessage
						.getHeader(RSeqHeader.NAME);
				if (rs != null) {
					// if (CONFIG.isKeyExists(RSEQ)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(RSEQ);
					boolean matchfound = Pattern.matches(regx, rs.toString()
							.trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case SERVER: {
				ServerHeader server = (ServerHeader) sipMessage
						.getHeader(ServerHeader.NAME);
				if (server != null) {

					// if (CONFIG.isKeyExists(SERVER)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(SERVER);
					boolean matchfound = Pattern.matches(regx, server
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case SIP_ETAG: {
				SIPETagHeader sipetag = (SIPETagHeader) sipMessage
						.getHeader(SIPETagHeader.NAME);
				if (sipetag != null) {

					// if (CONFIG.isKeyExists(SIPETAG)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(SIPETAG);
					boolean matchfound = Pattern.matches(regx, sipetag
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case SIP_IF_MATCH: {
				SIPIfMatchHeader sipifmatch = (SIPIfMatchHeader) sipMessage
						.getHeader(SIPIfMatchHeader.NAME);
				if (sipifmatch != null) {

					// if (CONFIG.isKeyExists(SIPIFMATCH)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(SIPIFMATCH);
					boolean matchfound = Pattern.matches(regx, sipifmatch
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case SUBJECT: {
				SubjectHeader subject = (SubjectHeader) sipMessage
						.getHeader(SubjectHeader.NAME);
				if (subject != null) {

					// if (CONFIG.isKeyExists(SUBJECT)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(SUBJECT);
					boolean matchfound = Pattern.matches(regx, subject
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case SUBSCRIPTION_STATE: {
				SubscriptionStateHeader subscription = (SubscriptionStateHeader) sipMessage
						.getHeader(SubscriptionStateHeader.NAME);
				if (subscription != null) {

					// if (CONFIG.isKeyExists(SUBSCRIPTIONSTATE)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(SUBSCRIPTIONSTATE);
					boolean matchfound = Pattern.matches(regx, subscription
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case SUPPORTED: {
				SupportedHeader supported = (SupportedHeader) sipMessage
						.getHeader(SupportedHeader.NAME);
				if (supported != null) {

					// if (CONFIG.isKeyExists(SUPPORTED)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(SUPPORTED);
					boolean matchfound = Pattern.matches(regx, supported
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case TIMESTAMP: {
				TimeStampHeader time = (TimeStampHeader) sipMessage
						.getHeader(TimeStampHeader.NAME);
				if (time != null) {

					// if (CONFIG.isKeyExists(TIMESTAMP)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(TIMESTAMP);
					boolean matchfound = Pattern.matches(regx, time.toString()
							.trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case TO: {
				ToHeader toHeader = (ToHeader) sipMessage
						.getHeader(ToHeader.NAME);
				if (toHeader != null) {

					// if (CONFIG.isKeyExists(TO)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(TO);
					boolean matchfound = Pattern.matches(regx, toHeader
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case UNSUPPORTED: {
				UnsupportedHeader unsupported = (UnsupportedHeader) sipMessage
						.getHeader(UnsupportedHeader.NAME);
				if (unsupported != null) {

					// if (CONFIG.isKeyExists(UNSUPPORTED)) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(UNSUPPORTED);
					boolean matchfound = Pattern.matches(regx, unsupported
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case USER_AGENT: {
				UserAgentHeader useragent = (UserAgentHeader) sipMessage
						.getHeader(UserAgentHeader.NAME);
				if (useragent != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(USERAGENT);
					boolean matchfound = Pattern.matches(regx, useragent
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case VIA: {
				ViaHeader viaheader = (ViaHeader) sipMessage
						.getHeader(ViaHeader.NAME);
				if (viaheader != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(VIA);
					boolean matchfound = Pattern.matches(regx, viaheader
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case WARNING: {
				WarningHeader warning = (WarningHeader) sipMessage
						.getHeader(WarningHeader.NAME);
				if (warning != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(WARNING);
					boolean matchfound = Pattern.matches(regx, warning
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case WWW_AUTHENTICATE: {
				WWWAuthenticateHeader wwwauth = (WWWAuthenticateHeader) sipMessage
						.getHeader(WWWAuthenticateHeader.NAME);
				if (wwwauth != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(WWWAUTHENTICATE);
					boolean matchfound = Pattern.matches(regx, wwwauth
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}

			case P_ACCESS_NETWORK_INFO: {

				PAccessNetworkInfoHeader PAccessNetworkInfo = (PAccessNetworkInfoHeader) sipMessage
						.getHeader(PAccessNetworkInfoHeader.NAME);
				if (PAccessNetworkInfo != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PACCESSNETWORKINFO);
					boolean matchfound = Pattern.matches(regx,
							PAccessNetworkInfo.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}

			case P_ASSERTED_IDENTITY: {
				PAssertedIdentityHeader pAssertedIdentity = (PAssertedIdentityHeader) sipMessage
						.getHeader(PAssertedIdentityHeader.NAME);
				if (pAssertedIdentity != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PASSERTEDIDENTITY);
					boolean matchfound = Pattern.matches(regx,
							pAssertedIdentity.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case P_ASSOCIATED_URI: {
				PAssociatedURIHeader pAssociatedURI = (PAssociatedURIHeader) sipMessage
						.getHeader(PAssociatedURIHeader.NAME);
				if (pAssociatedURI != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PASSOCIATEDURI);
					boolean matchfound = Pattern.matches(regx, pAssociatedURI
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case P_CALLED_PARTY_ID: {
				PCalledPartyIDHeader pCalledParty = (PCalledPartyIDHeader) sipMessage
						.getHeader(PCalledPartyIDHeader.NAME);
				if (pCalledParty != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PCALLEDPARTYID);
					boolean matchfound = Pattern.matches(regx, pCalledParty
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case P_CHARGING_FUNCTION_ADDRESSES: {
				PChargingFunctionAddressesHeader PChargingFunAdd = (PChargingFunctionAddressesHeader) sipMessage
						.getHeader(PChargingFunctionAddressesHeader.NAME);
				if (PChargingFunAdd != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PCHARGINGFUNCTIONADDRESSES);
					boolean matchfound = Pattern.matches(regx, PChargingFunAdd
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case P_CHARGING_VECTOR: {
				PChargingVectorHeader pChargingVecotr = (PChargingVectorHeader) sipMessage
						.getHeader(PChargingVectorHeader.NAME);
				if (pChargingVecotr != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PCHARGINGVECTOR);
					boolean matchfound = Pattern.matches(regx, pChargingVecotr
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case P_MEDIA_AUTHORIZATION: {
				PMediaAuthorizationHeader MediaAuth = (PMediaAuthorizationHeader) sipMessage
						.getHeader(PMediaAuthorizationHeader.NAME);
				if (MediaAuth != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PMEDIAAUTHORIZATION);
					boolean matchfound = Pattern.matches(regx, MediaAuth
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case P_PREFERRED_IDENTITY: {
				PPreferredIdentityHeader pprefrerredidentity = (PPreferredIdentityHeader) sipMessage
						.getHeader(PPreferredIdentityHeader.NAME);
				if (pprefrerredidentity != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PPREFERREDIDENTITY);
					boolean matchfound = Pattern.matches(regx,
							pprefrerredidentity.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case P_VISITED_NETWORK_ID: {
				PVisitedNetworkIDHeader pvistednetworkid = (PVisitedNetworkIDHeader) sipMessage
						.getHeader(PVisitedNetworkIDHeader.NAME);
				if (pvistednetworkid != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(PVISITEDNETWORKID);
					boolean matchfound = Pattern.matches(regx, pvistednetworkid
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case JOIN: {
				JoinHeader joinHeader = (JoinHeader) sipMessage
						.getHeader(JoinHeader.NAME);
				if (joinHeader != null) {
					String regx = "(" + headerName + ": )"
							+ CONFIG.getValue(JOIN);
					boolean matchfound = Pattern.matches(regx, joinHeader
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(headerName + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + headerName + SINGLE_QUOTE
								+ SPACE_SEP);
					} else {
						LOGGER.info(headerName + match);
					}
				}
				break;
			}
			case CUSTOM: {
				Header customHeader = sipMessage.getHeader(sipHeader.getName());
				if (customHeader != null) {
					boolean matchfound = Pattern.matches(sipHeader.getName()
							+ ": [a-zA-Z0-9[-_.+!%*`'~]]", customHeader
							.toString().trim());
					if (!matchfound) {
						LOGGER.info(sipHeader.getName() + notfound);
						isMatch = true;
						Invalid.append(SINGLE_QUOTE + sipHeader.getName()
								+ SINGLE_QUOTE + SPACE_SEP);
					} else {
						LOGGER.info(sipHeader.getName() + match);
					}
				}
				break;
			}
			}
		}
		if (isMatch) {
			throw new InvalidValuesHeaderException("Mismatch "
					+ Invalid.toString() + "header(s) in the " + res
					+ " SIP message");

		}
	}

}
