/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SIPHeaderHandler.java
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

/**
 * SIPHeaderHandler.java class is used t o add a extra unwanted header to the SIP
 * Messages.
 * 
 * 
 */
import static com.mitester.sipserver.SipServerConstants.SERVER_REQUEST;
import static com.mitester.sipserver.SipServerConstants.SERVER_RESPONSE;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.ACCEPT_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.ALERT_INFO_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.CALL_INFO_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.CONTACT_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.CONTENT_LENGTH_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.CONTENT_TYPE_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.COPY_VALUE;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.CSEQ_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.DATE_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.ERROR_INFO_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.EXPIRES_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.FROM_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.JOIN_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.MAX_FORWARD_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.MIME_VERSION_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.MIN_EXPIRES_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.MIN_SE_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.P_ASSOCIATED_IDENTITY_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.P_ASSOCIATED_URI_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.P_PREFERRED_IDENTITY_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.REASON_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.RECORD_ROUTE_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.REFFERED_BY_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.REFFER_TO_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.REPLY_TO_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.RETRY_AFTER_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.ROUTE_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.RSEQ_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.SERVICE_ROUTE_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.SESSION_EXPIRES_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.TIMESTAMP_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.TO_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.VIA_REGX;
import static com.mitester.sipserver.sipheaderhandler.SIPHeaderHandlerConstants.WARNING;
import gov.nist.javax.sip.header.SIPHeader;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
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
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.mitester.sipserver.ProcessSIPMessage;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.sipserver.SipParser;
import com.mitester.utility.MiTesterLog;

public class SIPHeaderHandler {
	private static final Logger LOGGER = MiTesterLog
	        .getLogger(SIPHeaderHandler.class.getName());

	/**
	 * method called during server test execution for adding headers in the SIP
	 * message construction
	 * 
	 * @param headers
	 *            is list contains the headers and its parameters
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 */
	private static List<String> empty = new ArrayList<String>();
	private static List<String> copy = new ArrayList<String>();

	private static List<com.mitester.jaxbparser.server.Header> Invalid = new ArrayList<com.mitester.jaxbparser.server.Header>();

	public static SIPMessage addHeader(
	        List<com.mitester.jaxbparser.server.Header> sipHeaders,
	        String type, String sipMessage) throws SipException,
	        ParseException, InvalidArgumentException, IndexOutOfBoundsException {
		Response response = null;
		Request request = null;

		if (type.equalsIgnoreCase(SERVER_RESPONSE)) {
			SIPMessage sipmsgconvert = SipParser.parseSipMessage(sipMessage);
			response = (Response) sipmsgconvert;
		} else {
			SIPMessage sipmsgconvert = SipParser.parseSipMessage(sipMessage);
			request = (Request) sipmsgconvert;
		}

		String headerName = null, headerValue = null;

		for (com.mitester.jaxbparser.server.Header headerNew : sipHeaders) {
			headerNew.getName();

			headerName = headerNew.getName();
			headerValue = headerNew.getValue();

			switch (SIPHeaders.getSipHeaderfromString(headerName.toUpperCase())) {
				case ACCEPT_ENCODING: {

					AcceptEncodingHeader acceptEncoding = null;
					if (headerValue == null) {
						acceptEncoding = AcceptEncodingHeaderHandler
						        .createAcceptEncodingHeader(headerNew);
					}
					if (acceptEncoding == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							acceptEncoding = AcceptEncodingHeaderHandler
							        .createAcceptEncodingHeader(headerNew);

						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (acceptEncoding != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(acceptEncoding);
						else
							request.addHeader(acceptEncoding);
					}
					break;
				}
				case ACCEPT: {
					LOGGER.info("Adding Accept Header");
					AcceptHeader acceptHeader = null;
					if (headerValue == null) {
						acceptHeader = AcceptHeaderHanldler
						        .createAcceptHeader(headerNew);
					}
					if (acceptHeader == null) {
						if (!headerValue.equals(COPY_VALUE)) {

							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        ACCEPT_REGX, headerValue);
								if (matchFound) {
									acceptHeader = AcceptHeaderHanldler
									        .createAcceptHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (acceptHeader != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(acceptHeader);
						else
							request.addHeader(acceptHeader);
					}
					break;
				}

				case ACCEPT_LANGUAGE: {
					AcceptLanguageHeader acceptLanguageHeader = null;
					if (headerValue == null) {
						acceptLanguageHeader = AcceptLanguageHeaderHandler
						        .createAcceptLangaugeHeader(headerNew);
					}
					if (acceptLanguageHeader == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							acceptLanguageHeader = AcceptLanguageHeaderHandler
							        .createAcceptLangaugeHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (acceptLanguageHeader != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(acceptLanguageHeader);
						else
							request.addHeader(acceptLanguageHeader);
					}
					break;
				}
				case ALERT_INFO: {
					AlertInfoHeader alertinfo = null;
					if (headerValue == null) {
						alertinfo = AlertInfoHeaderHandler
						        .createAlertInfoHeader(headerNew);

					}
					if (alertinfo == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        ALERT_INFO_REGX, headerValue);
								if (matchFound) {
									alertinfo = AlertInfoHeaderHandler
									        .createAlertInfoHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (alertinfo != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(alertinfo);
						else
							request.addHeader(alertinfo);
					}
					break;
				}
				case ALLOW_EVENTS: {
					AllowEventsHeader allowevent = null;
					if (headerValue == null) {
						allowevent = AllowEventsHeaderHandler
						        .createAllowEventsHeader(headerNew);
					}
					if (allowevent == null) {
						if (!headerValue.equals(COPY_VALUE)) {

							allowevent = AllowEventsHeaderHandler
							        .createAllowEventsHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (allowevent != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(allowevent);
						else
							request.addHeader(allowevent);
					}
					break;
				}
				case ALLOW: {
					AllowHeader allow = null;
					if (headerValue == null) {
						allow = AllowHeaderHandler.createAllowHeader(headerNew);
					}
					if (allow == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							allow = AllowHeaderHandler
							        .createAllowHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (allow != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(allow);
						else
							request.addHeader(allow);
					}
					break;
				}
				case AUTHENTICATION_INFO: {
					AuthenticationInfoHeader authentication = null;
					if (headerValue == null) {
						authentication = AuthenticationInfoHeaderHandler
						        .createAuthenticationInfo(headerNew);
					}
					if (authentication == null) {
						if (!headerValue.equals(COPY_VALUE)) {

							if (headerValue.length() != 0) {
								authentication = AuthenticationInfoHeaderHandler
								        .createAuthenticationInfo(headerNew);

							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (authentication != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(authentication);
						else
							request.addHeader(authentication);
					}
					break;
				}
				case AUTHORIZATION: {
					AuthorizationHeader authorization = null;

					if (headerValue == null) {
						authorization = AuthorizationHeaderHandler
						        .createAutherizationHeader(headerNew);
					}
					if (authorization == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								authorization = AuthorizationHeaderHandler
								        .createAutherizationHeader(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (authorization != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(authorization);
						else
							request.addHeader(authorization);
					}
					break;
				}
				case CALL_ID: {
					CallIdHeader callid = null;
					if (type.equalsIgnoreCase(SERVER_RESPONSE))
						response.removeHeader(CallIdHeader.NAME);
					else
						request.removeHeader(CallIdHeader.NAME);
					if (headerValue == null) {
						callid = CallIdHeaderHandler
						        .createCallIdHeader(headerNew);
					}
					if (callid == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							callid = CallIdHeaderHandler
							        .createCallIdHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (callid != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(callid);
						else
							request.addHeader(callid);
					}
					break;
				}
				case CALL_INFO: {
					CallInfoHeader callinfo = null;
					if (headerValue == null) {
						callinfo = CallInfoHeaderHandler
						        .createCallInfoHeader(headerNew);
					}
					if (callinfo == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        CALL_INFO_REGX, headerValue);
								if (matchFound) {
									callinfo = CallInfoHeaderHandler
									        .createCallInfoHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (callinfo != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(callinfo);
						else
							request.addHeader(callinfo);
					}
					break;
				}
				case CONTACT: {
					ContactHeader contactHeader = null;
					if (type.equalsIgnoreCase(SERVER_RESPONSE))
						response.removeHeader(ContactHeader.NAME);
					else
						request.removeHeader(ContactHeader.NAME);
					if (headerValue == null) {
						contactHeader = ContactHeaderHandler
						        .createContactHeader(headerNew);
					}
					if (contactHeader == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        CONTACT_REGX, headerValue);
								if (matchFound) {
									contactHeader = ContactHeaderHandler
									        .createContactHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (contactHeader != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(contactHeader);
						else
							request.addHeader(contactHeader);
					}
					break;
				}
				case CONTENT_DISPOSITION: {
					ContentDispositionHeader contentdisposition = null;
					if (headerValue == null) {
						contentdisposition = ContentDispositionHeaderHandler
						        .createContentDispositionHeader(headerNew);
					}
					if (contentdisposition == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							contentdisposition = ContentDispositionHeaderHandler
							        .createContentDispositionHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (contentdisposition != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(contentdisposition);
						else
							request.addHeader(contentdisposition);
					}
					break;
				}
				case CONTENT_ENCODING: {
					ContentEncodingHeader contentEncoding = null;
					if (headerValue == null) {
						contentEncoding = ContentEncodingHeaderHandler
						        .createContentEncodingHeader(headerNew);
					}
					if (contentEncoding == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							contentEncoding = ContentEncodingHeaderHandler
							        .createContentEncodingHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (contentEncoding != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(contentEncoding);
						else
							request.addHeader(contentEncoding);
					}
					break;
				}
				case CONTENT_LANGUAGE: {
					ContentLanguageHeader contentlanguage = null;
					if (headerValue == null) {
						contentlanguage = ContentLanguageHeaderHandler
						        .createContentLanguageHeader(headerNew);
					}
					if (contentlanguage == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							contentlanguage = ContentLanguageHeaderHandler
							        .createContentLanguageHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (contentlanguage != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(contentlanguage);
						else
							request.addHeader(contentlanguage);
					}
					break;
				}
				case CONTENT_LENGTH: {

					ContentLengthHeader contentlength = null;

					if (headerValue == null) {
						contentlength = ContentLengthHeaderHandler
						        .createContentLengthHeader(headerNew);
					}
					if (contentlength == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        CONTENT_LENGTH_REGX, headerValue);
								if (matchFound) {
									contentlength = ContentLengthHeaderHandler
									        .createContentLengthHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (contentlength != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE)) {
							response.removeHeader(ContentLengthHeader.NAME);
							response.addHeader(contentlength);
						} else {
							request.removeHeader(ContentLengthHeader.NAME);
							request.addHeader(contentlength);
						}

					}
					break;
				}
				case CONTENT_TYPE: {
					if (type.equalsIgnoreCase(SERVER_RESPONSE))
						response.removeHeader(ContentTypeHeader.NAME);
					else
						request.removeHeader(ContentTypeHeader.NAME);

					ContentTypeHeader contenttype = null;
					if (headerValue == null) {
						contenttype = ContentTypeHeaderHandler
						        .createContentTypeHeader(headerNew);

					}
					if (contenttype == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        CONTENT_TYPE_REGX, headerValue);
								if (matchFound) {
									contenttype = ContentTypeHeaderHandler
									        .createContentTypeHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (contenttype != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(contenttype);
						else

							request.addHeader(contenttype);
					}
					break;
				}
				case CSEQ: {
					CSeqHeader cSeqHeader = null;
					if (type.equalsIgnoreCase(SERVER_RESPONSE))
						response.removeHeader(CSeqHeader.NAME);
					else
						request.removeHeader(CSeqHeader.NAME);

					if (headerValue == null) {
						cSeqHeader = CSeqHeaderHandler
						        .createCSeqHeader(headerNew);
					}
					if (cSeqHeader == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        CSEQ_REGX, headerValue);
								if (matchFound) {
									cSeqHeader = CSeqHeaderHandler
									        .createCSeqHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (cSeqHeader != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))

							response.addHeader(cSeqHeader);
						else
							request.addHeader(cSeqHeader);
					}
					break;
				}
				case DATE: {
					DateHeader date = null;
					if (headerValue == null) {
						date = DateHeaderHandler.createDateHeader(headerNew);
					}
					if (date == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        DATE_REGX, headerValue);
								if (matchFound) {
									date = DateHeaderHandler
									        .createDateHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (date != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(date);
						else
							request.addHeader(date);
					}
					break;
				}
				case ERROR_INFO: {
					ErrorInfoHeader errorInfo = null;

					if (headerValue == null) {
						errorInfo = ErrorInfoHeaderHandler
						        .createErrorInfoHeader(headerNew);
					}
					if (errorInfo == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        ERROR_INFO_REGX, headerValue);
								if (matchFound) {
									errorInfo = ErrorInfoHeaderHandler
									        .createErrorInfoHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (errorInfo != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(errorInfo);
						else
							request.addHeader(errorInfo);
					}
					break;
				}
				case EVENT: {
					EventHeader event = null;
					if (headerValue == null) {
						event = EventHeaderHandler.createEventHeader(headerNew);
					}
					if (event == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							event = EventHeaderHandler
							        .createEventHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (event != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(event);
						else
							request.addHeader(event);
					}
					break;
				}
				case EXPIRES: {
					ExpiresHeader expires = null;
					if (type.equalsIgnoreCase(SERVER_RESPONSE)) {
						response.removeHeader(ExpiresHeader.NAME);
					} else
						request.removeHeader(ExpiresHeader.NAME);

					if (headerValue == null) {
						expires = ExpiresHeaderHandler
						        .createExpiresHeader(headerNew);
					}
					if (expires == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        EXPIRES_REGX, headerValue);
								if (matchFound) {
									expires = ExpiresHeaderHandler
									        .createExpiresHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());

						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (expires != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(expires);
						else
							request.addHeader(expires);
					}
					break;
				}
				case FROM: {
					FromHeader fromHeader = null;
					if (type.equalsIgnoreCase(SERVER_RESPONSE))
						response.removeHeader(FromHeader.NAME);
					else
						request.removeHeader(FromHeader.NAME);

					if (headerValue == null) {
						fromHeader = FromHeaderHandler
						        .createFromHeader(headerNew);
					}
					if (fromHeader == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        FROM_REGX, headerValue);
								if (matchFound) {
									fromHeader = FromHeaderHandler
									        .createFromHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());

						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (fromHeader != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(fromHeader);
						else
							request.addHeader(fromHeader);
					}
					break;
				}
				case IN_REPLY_TO: {
					InReplyToHeader inreplyto = null;
					if (headerValue == null) {
						inreplyto = InReplyToHeaderHandler
						        .createInReplyToHeader(headerNew);
					}
					if (inreplyto == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							inreplyto = InReplyToHeaderHandler
							        .createInReplyToHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (inreplyto != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(inreplyto);
						else
							request.addHeader(inreplyto);
					}
					break;
				}
				case MAX_FORWARDS: {
					MaxForwardsHeader maxforwards = null;
					if (type.equalsIgnoreCase(SERVER_REQUEST))
						request.removeHeader(MaxForwardsHeader.NAME);

					if (headerValue == null) {
						maxforwards = MaxForwardsHeaderHandler
						        .createMaxForwardsHeader(headerNew);

					}
					if (maxforwards == null) {
						if (!headerValue.equals(COPY_VALUE)) {

							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        MAX_FORWARD_REGX, headerValue);
								if (matchFound) {
									maxforwards = MaxForwardsHeaderHandler
									        .createMaxForwardsHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());

						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (maxforwards != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(maxforwards);
						else
							request.addHeader(maxforwards);
					}
					break;
				}
				case MIME_VERSION: {
					MimeVersionHeader mimeVersion = null;

					if (headerValue == null) {
						mimeVersion = MimeVersionHeaderHandler
						        .createMimeVersionHeader(headerNew);
					}
					if (mimeVersion == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        MIME_VERSION_REGX, headerValue);
								if (matchFound) {
									mimeVersion = MimeVersionHeaderHandler
									        .createMimeVersionHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (mimeVersion != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(mimeVersion);
						else
							request.addHeader(mimeVersion);
					}
					break;
				}
				case MIN_SE: {
					MinSEHeader minse = null;

					if (headerValue == null) {
						minse = MinSEHeaderHandler.createMinSEHeader(headerNew);
					}
					if (minse == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        MIN_SE_REGX, headerValue);
								if (matchFound) {
									minse = MinSEHeaderHandler
									        .createMinSEHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (minse != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(minse);
						else
							request.addHeader(minse);
					}
					break;
				}
				case MIN_EXPIRES: {
					MinExpiresHeader minexpires = null;
					if (headerValue == null) {
						minexpires = MinExpiresHeaderHandler
						        .createMinExpiresHeader(headerNew);
					}
					if (minexpires == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        MIN_EXPIRES_REGX, headerValue);
								if (matchFound) {
									minexpires = MinExpiresHeaderHandler
									        .createMinExpiresHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (minexpires != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(minexpires);
						else
							request.addHeader(minexpires);
					}
					break;
				}
				case ORGANIZATION: {
					OrganizationHeader org = null;
					if (headerValue == null) {
						org = OrganizationHeaderHandler
						        .createOrganizationHeader(headerNew);
					}
					if (org == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							org = OrganizationHeaderHandler
							        .createOrganizationHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (org != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(org);
						else
							request.addHeader(org);
					}
					break;
				}
				case PRIORITY: {
					PriorityHeader priority = null;
					if (headerValue == null) {
						priority = PriorityHeaderHandler
						        .createPriorityHeader(headerNew);
					}
					if (priority == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							priority = PriorityHeaderHandler
							        .createPriorityHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (priority != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(priority);
						else
							request.addHeader(priority);
					}
					break;
				}
				case PRIVACY: {
					PrivacyHeader privacy = null;
					if (headerValue == null) {
						privacy = PrivacyHeaderHandler
						        .createPrivacyHeader(headerNew);
					}
					if (privacy == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							privacy = PrivacyHeaderHandler
							        .createPrivacyHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (privacy != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(privacy);
						else
							request.addHeader(privacy);
					}
					break;
				}
				case PROXY_AUTHENTICATE: {
					ProxyAuthenticateHeader proxyauth = null;
					if (headerValue == null) {
						proxyauth = ProxyAuthenticateHeaderHandler
						        .createProxyAuthenticateHeader(headerNew);
					}
					if (proxyauth == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {

								proxyauth = ProxyAuthenticateHeaderHandler
								        .createProxyAuthenticateHeader(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (proxyauth != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(proxyauth);
						else
							request.addHeader(proxyauth);
					}
					break;
				}
				case PROXY_AUTHORIZATION: {
					ProxyAuthorizationHeader proxyauthorization = null;
					if (headerValue == null) {
						proxyauthorization = ProxyAuthorizationHeaderHandler
						        .createProxyAuthorizationHeader(headerNew);
					}
					if (proxyauthorization == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								proxyauthorization = ProxyAuthorizationHeaderHandler
								        .createProxyAuthorizationHeader(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (proxyauthorization != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(proxyauthorization);
						else
							request.addHeader(proxyauthorization);
					}
					break;
				}
				case PROXY_REQUIRE: {
					ProxyRequireHeader proxyrequire = null;
					if (headerValue == null) {
						proxyrequire = ProxyRequireHeaderHandler
						        .createProxyRequireHeader(headerNew);
					}
					if (proxyrequire == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							proxyrequire = ProxyRequireHeaderHandler
							        .createProxyRequireHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (proxyrequire != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(proxyrequire);
						else
							request.addHeader(proxyrequire);
					}
					break;
				}
				case RACK: {
					RAckHeader rack = null;
					if (headerValue == null) {
						rack = RAckHeaderHandler.createRAckHeader(headerNew);
					}
					if (rack == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        "RA", headerValue);
								if (matchFound) {
									rack = RAckHeaderHandler
									        .createRAckHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (rack != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(rack);
						else
							request.addHeader(rack);
					}
					break;
				}
				case REASON: {
					ReasonHeader reason = null;
					if (headerValue == null) {
						reason = ReasonHeaderHandler
						        .createReasonHeader(headerNew);
					}
					if (reason == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        REASON_REGX, headerValue);
								if (matchFound) {
									reason = ReasonHeaderHandler
									        .createReasonHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (reason != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(reason);
						else
							request.addHeader(reason);
					}
					break;
				}
				case RECORD_ROUTE: {
					RecordRouteHeader record = null;
					if (headerValue == null) {
						record = RecordRouteHeaderHandler
						        .addRecordRouteHeader(headerNew);
					}
					if (record == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        RECORD_ROUTE_REGX, headerValue);
								if (matchFound) {
									record = RecordRouteHeaderHandler
									        .addRecordRouteHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (record != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(record);
						else
							request.addHeader(record);
					}
					break;
				}
				case REFER_TO: {
					ReferToHeader referto = null;
					if (headerValue == null) {
						referto = ReferToHeaderHandler
						        .createReferToHeader(headerNew);
					}
					if (referto == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        REFFER_TO_REGX, headerValue);
								if (matchFound) {
									referto = ReferToHeaderHandler
									        .createReferToHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (referto != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(referto);
						else
							request.addHeader(referto);
					}
					break;
				}
				case REFERRED_BY: {
					ReferredByHeader referby = null;
					if (headerValue == null) {
						referby = ReferredByHeaderHandler
						        .addReferredByHeader(headerNew);
					}
					if (referby == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        REFFERED_BY_REGX, headerValue);
								if (matchFound) {
									referby = ReferredByHeaderHandler
									        .addReferredByHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (referby != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(referby);
						else
							request.addHeader(referby);
					}
					break;
				}
				case REPLACES: {
					ReplacesHeader replace = null;
					if (headerValue == null) {
						replace = ReplacesHeaderHandler
						        .createReplacesHeader(headerNew);
					}
					if (replace == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        JOIN_REGX, headerValue);
								if (matchFound) {
									replace = ReplacesHeaderHandler
									        .createReplacesHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (replace != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(replace);
						else
							request.addHeader(replace);
					}
					break;
				}
				case SECURITY_CLIENT: {
					SecurityClientHeader securityClientheader = null;
					if (headerValue == null) {
						securityClientheader = SecurityClientHeaderHandler
						        .createSecurityClientHeader(headerNew);
					}
					if (securityClientheader == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {

								securityClientheader = SecurityClientHeaderHandler
								        .createSecurityClientHeader(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (securityClientheader != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(securityClientheader);
						else
							request.addHeader(securityClientheader);
					}
					break;
				}
				case SECURITY_SERVER: {
					SecurityServerHeader securityServerheader = null;
					if (headerValue == null) {
						securityServerheader = SecurityServerHeaderHandler
						        .addSecurityServerHeader(headerNew);
					}
					if (securityServerheader == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								securityServerheader = SecurityServerHeaderHandler
								        .addSecurityServerHeader(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (securityServerheader != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(securityServerheader);
						else
							request.addHeader(securityServerheader);
					}
					break;
				}
				case SECURITY_VERIFY: {
					SecurityVerifyHeader securityVerifyheader = null;
					if (headerValue == null) {
						securityVerifyheader = SecurityVerifyHeaderHandler
						        .createSecurityVerifyHeader(headerNew);
					}
					if (securityVerifyheader == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								securityVerifyheader = SecurityVerifyHeaderHandler
								        .createSecurityVerifyHeader(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (securityVerifyheader != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(securityVerifyheader);
						else
							request.addHeader(securityVerifyheader);
					}
					break;
				}
				case SERVICE_ROUTE: {
					ServiceRouteHeader serviceroute = null;
					if (headerValue == null) {
						serviceroute = ServiceRouteHeaderHandler
						        .addServiceRouteHeader(headerNew);
					}
					if (serviceroute == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        SERVICE_ROUTE_REGX, headerValue);
								if (matchFound) {
									serviceroute = ServiceRouteHeaderHandler
									        .addServiceRouteHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (serviceroute != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(serviceroute);
						else
							request.addHeader(serviceroute);
					}
					break;
				}
				case SESSION_EXPIRES: {
					SessionExpiresHeader session = null;
					if (headerValue == null) {
						session = SessionExpiresHeaderHandler
						        .createSessionExpiresHeader(headerNew);
					}
					if (session == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        SESSION_EXPIRES_REGX, headerValue);
								if (matchFound) {
									session = SessionExpiresHeaderHandler
									        .createSessionExpiresHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (session != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(session);
						else
							request.addHeader(session);
					}
					break;
				}
				case REPLY_TO: {
					ReplyToHeader replyto = null;
					if (headerValue == null) {
						replyto = ReplyToHeaderHandler
						        .createReplyToHeader(headerNew);
					}
					if (replyto == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        REPLY_TO_REGX, headerValue);
								if (matchFound) {
									replyto = ReplyToHeaderHandler
									        .createReplyToHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (replyto != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(replyto);
						else
							request.addHeader(replyto);
					}
					break;
				}
				case REQUIRE: {
					RequireHeader require = null;
					if (headerValue == null) {
						require = RequireHeaderHandler
						        .createRequireHeader(headerNew);
					}
					if (require == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        "REQ", headerValue);
								if (matchFound) {
									require = RequireHeaderHandler
									        .createRequireHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (require != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(require);
						else
							request.addHeader(require);
					}
					break;
				}
				case RETRY_AFTER: {
					RetryAfterHeader retryafter = null;
					if (headerValue == null) {
						retryafter = RetryAfterHeaderHandler
						        .createRetryAfterHeader(headerNew);
					}
					if (retryafter == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        RETRY_AFTER_REGX, headerValue);
								if (matchFound) {
									retryafter = RetryAfterHeaderHandler
									        .createRetryAfterHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (retryafter != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(retryafter);
						else
							request.addHeader(retryafter);
					}
					break;
				}
				case ROUTE: {
					RouteHeader route = null;
					if (headerValue == null) {
						route = RouteHeaderHandler.createRouteHeader(headerNew);
					}
					if (route == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        ROUTE_REGX, headerValue);
								if (matchFound) {
									route = RouteHeaderHandler
									        .createRouteHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (route != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(route);
						else
							request.addHeader(route);
					}
					break;
				}
				case RSEQ: {
					RSeqHeader rs = null;
					if (headerValue == null) {
						rs = RSeqHeaderHandler.createRSeqHeader(headerNew);
					}
					if (rs == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        RSEQ_REGX, headerValue);
								if (matchFound) {
									rs = RSeqHeaderHandler
									        .createRSeqHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (rs != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(rs);
						else
							request.addHeader(rs);
					}
					break;
				}
				case SERVER: {
					ServerHeader server = null;
					if (headerValue == null) {
						server = ServerHeaderHandler
						        .createServerHeader(headerNew);
					}
					if (server == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							server = ServerHeaderHandler
							        .createServerHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (server != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(server);
						else
							request.addHeader(server);
					}
					break;
				}
				case SIP_ETAG: {
					SIPETagHeader sipetag = null;
					if (headerValue == null) {
						sipetag = SIPETagHeaderHandler
						        .createSIPETagHeader(headerNew);
					}
					if (sipetag == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							sipetag = SIPETagHeaderHandler
							        .createSIPETagHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (sipetag != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(sipetag);
						else
							request.addHeader(sipetag);
					}
					break;
				}
				case SIP_IF_MATCH: {
					SIPIfMatchHeader sipifmatch = null;
					if (headerValue == null) {
						sipifmatch = SIPIfMatchHeaderHandler
						        .createSIPIfMatchHeader(headerNew);
					}
					if (sipifmatch == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							sipifmatch = SIPIfMatchHeaderHandler
							        .createSIPIfMatchHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (sipifmatch != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(sipifmatch);
						else
							request.addHeader(sipifmatch);
					}
					break;
				}
				case SUBJECT: {
					SubjectHeader subject = null;
					if (headerValue == null) {
						subject = SubjectHeaderHandler
						        .createSubjectHeader(headerNew);
					}
					if (subject == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							subject = SubjectHeaderHandler
							        .createSubjectHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (subject != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(subject);
						else
							request.addHeader(subject);
					}
					break;
				}
				case SUBSCRIPTION_STATE: {
					SubscriptionStateHeader subscription = null;
					if (headerValue == null) {
						subscription = SubscriptionStateHeaderHandler
						        .createSubscriptionStateHeader(headerNew);
					}
					if (subscription == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							subscription = SubscriptionStateHeaderHandler
							        .createSubscriptionStateHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (subscription != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(subscription);
						else
							request.addHeader(subscription);
					}
					break;
				}
				case SUPPORTED: {
					SupportedHeader supported = null;
					if (headerValue == null) {
						supported = SupportedHeaderHandler
						        .createSupportedHeader(headerNew);
					}
					if (supported == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							supported = SupportedHeaderHandler
							        .createSupportedHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (supported != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(supported);
						else
							request.addHeader(supported);
					}
					break;
				}
				case TIMESTAMP: {
					TimeStampHeader time = null;
					if (headerValue == null) {
						time = TimeStampHeaderHandler
						        .createTimeStampHeader(headerNew);
					}
					if (time == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        TIMESTAMP_REGX, headerValue);
								if (matchFound) {
									time = TimeStampHeaderHandler
									        .createTimeStampHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (time != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(time);
						else
							request.addHeader(time);
					}
					break;
				}
				case TO: {
					ToHeader toHeader = null;
					if (type.equalsIgnoreCase(SERVER_RESPONSE))
						response.removeHeader(ToHeader.NAME);
					else
						request.removeHeader(ToHeader.NAME);

					if (headerValue == null) {
						toHeader = ToHeaderHandler.createtoHeader(headerNew);
					}
					if (toHeader == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        TO_REGX, headerValue);
								if (matchFound) {
									toHeader = ToHeaderHandler
									        .createtoHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (toHeader != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(toHeader);
						else
							request.addHeader(toHeader);
					}
					break;
				}
				case UNSUPPORTED: {
					UnsupportedHeader unsupported = null;
					if (headerValue == null) {
						unsupported = UnsupportedHeaderHandler
						        .createUnsupportedHeader(headerNew);
					}
					if (unsupported == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							unsupported = UnsupportedHeaderHandler
							        .createUnsupportedHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (unsupported != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(unsupported);
						else
							request.addHeader(unsupported);
					}
					break;
				}
				case USER_AGENT: {
					UserAgentHeader useragent = null;
					if (headerValue == null) {
						useragent = UserAgentHeaderHandler
						        .createUserAgentHeader(headerNew);
					}
					if (useragent == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							useragent = UserAgentHeaderHandler
							        .createUserAgentHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (useragent != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(useragent);
						else
							request.addHeader(useragent);
					}
					break;
				}
				case VIA: {
					List<ViaHeader> viaHeaders = null;
					if (type.equalsIgnoreCase(SERVER_RESPONSE))
						response.removeHeader(ViaHeader.NAME);
					else
						request.removeHeader(ViaHeader.NAME);

					if (headerValue == null) {
						viaHeaders = ViaHeaderHandler
						        .createViaHeader(headerNew);
					}
					if (viaHeaders == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        VIA_REGX, headerValue);
								if (matchFound) {
									viaHeaders = ViaHeaderHandler
									        .createViaHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (viaHeaders != null) {
						for (ViaHeader v : viaHeaders) {
							LOGGER.info("Adding "
							        + SIPHeaders.getSipHeaderfromString(
							                headerNew.getName().toUpperCase())
							                .toString() + " Header");
							if (type.equalsIgnoreCase(SERVER_RESPONSE))
								response.addHeader(v);
							else
								request.addHeader(v);
						}
					}
					break;
				}
				case WARNING: {
					WarningHeader warning = null;

					if (headerValue == null) {
						warning = WarningHeaderHandler
						        .createWarningHeader(headerNew);
					}
					if (warning == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        WARNING, headerValue);
								if (matchFound) {
									warning = WarningHeaderHandler
									        .createWarningHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (warning != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(warning);
						else
							request.addHeader(warning);
					}
					break;
				}
				case WWW_AUTHENTICATE: {
					WWWAuthenticateHeader wwwauth = null;

					if (headerValue == null) {
						wwwauth = WWWAuthenticateHeaderHandler
						        .createWWWAuthenticateHeader(headerNew);
					}
					if (wwwauth == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								wwwauth = WWWAuthenticateHeaderHandler
								        .createWWWAuthenticateHeader(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (wwwauth != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(wwwauth);
						else
							request.addHeader(wwwauth);
					}
					break;
				}
				case CUSTOM: {
					Header customHeader = null;
					if (headerValue == null) {
						customHeader = CustomHeaderHandler
						        .createCustomHeader(headerNew);
					}
					if (customHeader == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							customHeader = CustomHeaderHandler
							        .createCustomHeader(headerNew);

						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (customHeader != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(customHeader);
						else
							request.addHeader(customHeader);
					}
					break;
				}
				case P_ACCESS_NETWORK_INFO: {

					PAccessNetworkInfoHeader PAccessNetworkInfo = null;

					if (headerValue == null) {
						PAccessNetworkInfo = PAccessNetworkInfoHeaderHandler
						        .createPAccessNetworkInfoHeader(headerNew);
					}
					if (PAccessNetworkInfo == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        "P", headerValue);
								if (matchFound) {
									PAccessNetworkInfo = PAccessNetworkInfoHeaderHandler
									        .createPAccessNetworkInfoHeader(headerNew);

								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (PAccessNetworkInfo != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(PAccessNetworkInfo);
						else
							request.addHeader(PAccessNetworkInfo);
					}
					break;
				}

				case P_ASSERTED_IDENTITY: {
					PAssertedIdentityHeader pAssertedIdentity = null;

					if (headerValue == null) {
						pAssertedIdentity = PAssertedIdentityHeaderHandler
						        .createPAssertedIdentityHeader(headerNew);
					}
					if (pAssertedIdentity == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        P_ASSOCIATED_IDENTITY_REGX, headerValue);
								if (matchFound) {
									pAssertedIdentity = PAssertedIdentityHeaderHandler
									        .createPAssertedIdentityHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (pAssertedIdentity != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(pAssertedIdentity);
						else
							request.addHeader(pAssertedIdentity);
					}
					break;
				}
				case P_ASSOCIATED_URI: {
					PAssociatedURIHeader pAssociatedURI = null;

					if (headerValue == null) {
						pAssociatedURI = PAssociatedURIHeaderHandler
						        .createPAssociatedURIHeader(headerNew);
					}
					if (pAssociatedURI == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        P_ASSOCIATED_URI_REGX, headerValue);
								if (matchFound) {
									pAssociatedURI = PAssociatedURIHeaderHandler
									        .createPAssociatedURIHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (pAssociatedURI != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(pAssociatedURI);
						else
							request.addHeader(pAssociatedURI);
					}
					break;
				}
				case P_CALLED_PARTY_ID: {
					PCalledPartyIDHeader pCalledParty = null;
					if (headerValue == null) {
						pCalledParty = PCalledPartyIDHeaderHandler
						        .createPCalledPartyIDHeader(headerNew);
					}
					if (pCalledParty == null) {

						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        P_ASSOCIATED_URI_REGX, headerValue);
								if (matchFound) {
									pCalledParty = PCalledPartyIDHeaderHandler
									        .createPCalledPartyIDHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (pCalledParty != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(pCalledParty);
						else
							request.addHeader(pCalledParty);
					}
					break;
				}
				case P_CHARGING_FUNCTION_ADDRESSES: {
					PChargingFunctionAddressesHeader PChargingFunAdd = null;
					if (headerValue == null) {
						PChargingFunAdd = PChargingFunctionAddressHeaderHandler
						        .createPChargingFunctionAddressesHeader(headerNew);
					}
					if (PChargingFunAdd == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							PChargingFunAdd = PChargingFunctionAddressHeaderHandler
							        .createPChargingFunctionAddressesHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (PChargingFunAdd != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(PChargingFunAdd);
						else
							request.addHeader(PChargingFunAdd);
					}
					break;
				}
				case P_CHARGING_VECTOR: {
					PChargingVectorHeader pChargingVecotr = null;
					if (headerValue == null) {
						pChargingVecotr = PChargingVectorHeaderHandler
						        .createPChargingVectorHeader(headerNew);
					}
					if (pChargingVecotr == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							pChargingVecotr = PChargingVectorHeaderHandler
							        .createPChargingVectorHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (pChargingVecotr != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(pChargingVecotr);
						else
							request.addHeader(pChargingVecotr);
					}
					break;
				}
				case P_MEDIA_AUTHORIZATION: {
					PMediaAuthorizationHeader MediaAuth = null;
					if (headerValue == null) {
						MediaAuth = PMediaAuthorizationHeaderHandler
						        .createPPreferredIdentityHeader(headerNew);
					}
					if (MediaAuth == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							MediaAuth = PMediaAuthorizationHeaderHandler
							        .createPPreferredIdentityHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (MediaAuth != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(MediaAuth);
						else
							request.addHeader(MediaAuth);
					}
					break;
				}
				case P_PREFERRED_IDENTITY: {
					PPreferredIdentityHeader pprefrerredidentity = null;
					if (headerValue == null) {
						pprefrerredidentity = PPreferredIdentityHeaderHandler
						        .createPPreferredIdentityHeader(headerNew);
					}
					if (pprefrerredidentity == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        P_PREFERRED_IDENTITY_REGX, headerValue);
								if (matchFound) {
									pprefrerredidentity = PPreferredIdentityHeaderHandler
									        .createPPreferredIdentityHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (pprefrerredidentity != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(pprefrerredidentity);
						else
							request.addHeader(pprefrerredidentity);
					}
					break;
				}
				case P_VISITED_NETWORK_ID: {
					PVisitedNetworkIDHeader pvistednetworkid = null;
					if (headerValue == null) {
						pvistednetworkid = PVisitedNetworkIDHeaderHandler
						        .createPVisitedNetworkIDHeader(headerNew);
					}
					if (pvistednetworkid == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							pvistednetworkid = PVisitedNetworkIDHeaderHandler
							        .createPVisitedNetworkIDHeader(headerNew);
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (pvistednetworkid != null) {
						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(pvistednetworkid);
						else
							request.addHeader(pvistednetworkid);
					}
					break;
				}
				case JOIN: {
					JoinHeader joinHeader = null;
					if (headerValue == null) {
						joinHeader = JoinHeaderHandler
						        .createJoinHeader(headerNew);
					}
					if (joinHeader == null) {
						if (!headerValue.equals(COPY_VALUE)) {
							if (headerValue.length() != 0) {
								boolean matchFound = validatingSIPHeaderValue(
								        JOIN_REGX, headerValue);
								if (matchFound) {
									joinHeader = JoinHeaderHandler
									        .createJoinHeader(headerNew);
								} else
									Invalid.add(headerNew);
							} else
								empty.add(SIPHeaders.getSipHeaderfromString(
								        headerName).toString());
						} else
							copy = copyValues(SIPHeaders
							        .getSipHeaderfromString(headerName)
							        .toString());
					}
					if (joinHeader != null) {

						LOGGER.info("Adding "
						        + SIPHeaders.getSipHeaderfromString(
						                headerNew.getName().toUpperCase())
						                .toString() + " Header");
						if (type.equalsIgnoreCase(SERVER_RESPONSE))
							response.addHeader(joinHeader);
						else
							request.addHeader(joinHeader);
					}
					break;
				}
			}
		}
		SIPMessage SIPMesssage = null;
		if (type.equalsIgnoreCase(SERVER_RESPONSE))
			SIPMesssage = (SIPMessage) response;
		else
			SIPMesssage = (SIPMessage) request;
		return SIPMesssage;
	}

	public static List<String> getEmptyList() {
		return empty;
	}

	public static List<String> getCopyList() {
		return copy;
	}

	public static List<com.mitester.jaxbparser.server.Header> getInvalidList() {
		return Invalid;
	}

	public static void deiniEmptyList() {
		empty = null;
		empty = new ArrayList<String>();
	}

	public static void deiniCopyList() {
		copy = null;
		copy = new ArrayList<String>();
	}

	public static void deiniInvalidList() {
		Invalid = null;
		Invalid = new ArrayList<com.mitester.jaxbparser.server.Header>();
	}

	public static boolean validatingSIPHeaderValue(String regularExpression,
	        String HeaderValue) {
		boolean matchFound = false;

		if (!HeaderValue.equals(COPY_VALUE))
			matchFound = Pattern.matches(regularExpression, HeaderValue);
		else
			matchFound = true;

		return matchFound;
	}

	public static List<String> copyValues(String Headername)
	        throws SipException, ParseException, InvalidArgumentException,
	        IndexOutOfBoundsException {

		LOGGER
		        .info("Entered Copied value from the Previous Request or Response with the Header name: "
		                + Headername);
		List<String> copy = new ArrayList<String>();
		SIPMessage sipMsg = ProcessSIPMessage.getSipMessage();
		Iterator<SIPHeader> headers = sipMsg.getHeaders();
		while (headers.hasNext()) {
			String header = headers.next().toString();
			if (header.startsWith(Headername + ":")) {
				copy.add(header);
				LOGGER.info("Header Found In the previous Request or Response");
			}
		}
		return copy;
	}
}
