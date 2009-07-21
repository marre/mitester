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

import static com.mitester.sipserver.ValidationKeywords.ACCEPT;
import static com.mitester.sipserver.ValidationKeywords.ACCEPTENCODING;
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
import static com.mitester.sipserver.ValidationKeywords.TIMESTAMP;
import static com.mitester.sipserver.ValidationKeywords.TO;
import static com.mitester.sipserver.ValidationKeywords.UNSUPPORTED;
import static com.mitester.sipserver.ValidationKeywords.USERAGENT;
import static com.mitester.sipserver.ValidationKeywords.VIA;
import static com.mitester.sipserver.ValidationKeywords.WARNING;
import static com.mitester.sipserver.ValidationKeywords.WWWAUTHENTICATE;
import static com.mitester.sipserver.headervalidation.Config.CONFIG;
import static com.mitester.utility.UtilityConstants.NORMAL;
import java.util.List;

import org.apache.log4j.Logger;

import com.mitester.jaxbparser.validation.MANDATORY;
import com.mitester.jaxbparser.validation.Method;
import com.mitester.jaxbparser.validation.OPTIONAL;
import com.mitester.jaxbparser.validation.VALIDATION;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;

public class PropertyKeyValidation {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(PropertyKeyValidation.class.getName());

	private static StringBuilder errorMsgBuf = null;

	public static boolean isKeyPresent(
			List<com.mitester.jaxbparser.validation.Header> header) {
		boolean isKeyword = true;
		for (com.mitester.jaxbparser.validation.Header headernew : header) {

			switch (SIPHeaders.getSipHeaderfromString(headernew.getName()
					.toUpperCase())) {

			case ACCEPT_ENCODING: {
				if (!CONFIG.isKeyExists(ACCEPTENCODING)) {
					PropertyKeyValidation.bufferErrorMessage(ACCEPTENCODING);
					isKeyword = false;
				}
				break;
			}
			case ACCEPT: {

				if (!CONFIG.isKeyExists(ACCEPT)) {
					PropertyKeyValidation.bufferErrorMessage(ACCEPT);
					isKeyword = false;
				}
				break;
			}

			case ACCEPT_LANGUAGE: {

				if (!CONFIG.isKeyExists(ACCEPTLANGUAGE)) {
					PropertyKeyValidation.bufferErrorMessage(ACCEPTLANGUAGE);
					isKeyword = false;
				}
				break;
			}
			case ALERT_INFO: {

				if (!CONFIG.isKeyExists(ALERTINFO)) {
					PropertyKeyValidation.bufferErrorMessage(ALERTINFO);
					isKeyword = false;
				}
				break;
			}
			case ALLOW_EVENTS: {

				if (!CONFIG.isKeyExists(ALLOWEVENTS)) {
					PropertyKeyValidation.bufferErrorMessage(ALLOWEVENTS);
					isKeyword = false;
				}
				break;
			}
			case ALLOW: {

				if (!CONFIG.isKeyExists(ALLOW)) {
					PropertyKeyValidation.bufferErrorMessage(ALLOW);
					isKeyword = false;
				}
				break;
			}
			case AUTHENTICATION_INFO: {

				if (!CONFIG.isKeyExists(AUTHENTICATIONINFO)) {
					PropertyKeyValidation
							.bufferErrorMessage(AUTHENTICATIONINFO);
					isKeyword = false;
				}
				break;
			}
			case AUTHORIZATION: {

				if (!CONFIG.isKeyExists(AUTHORIZATION)) {
					PropertyKeyValidation.bufferErrorMessage(AUTHORIZATION);
					isKeyword = false;
				}
				break;
			}
			case CALL_ID: {

				if (!CONFIG.isKeyExists(CALLID)) {
					PropertyKeyValidation.bufferErrorMessage(CALLID);
					isKeyword = false;
				}
				break;
			}
			case CALL_INFO: {

				if (!CONFIG.isKeyExists(CALLINFO)) {
					PropertyKeyValidation.bufferErrorMessage(CALLINFO);
					isKeyword = false;
				}
				break;
			}
			case CONTACT: {
				if (!CONFIG.isKeyExists(CONTACT)) {
					PropertyKeyValidation.bufferErrorMessage(CONTACT);
					isKeyword = false;
				}
				break;
			}
			case CONTENT_DISPOSITION: {

				if (!CONFIG.isKeyExists(CONTENTDISPOSITION)) {
					PropertyKeyValidation
							.bufferErrorMessage(CONTENTDISPOSITION);
					isKeyword = false;
				}
				break;
			}
			case CONTENT_ENCODING: {

				if (!CONFIG.isKeyExists(CONTENTENCODING)) {
					PropertyKeyValidation.bufferErrorMessage(CONTENTENCODING);
					isKeyword = false;
				}
				break;
			}
			case CONTENT_LANGUAGE: {

				if (!CONFIG.isKeyExists(CONTENTLANGUAGE)) {
					PropertyKeyValidation.bufferErrorMessage(CONTENTLANGUAGE);
					isKeyword = false;
				}
				break;
			}
			case CONTENT_LENGTH: {

				if (!CONFIG.isKeyExists(CONTENTLENGTH)) {
					PropertyKeyValidation.bufferErrorMessage(CONTENTLENGTH);
					isKeyword = false;
				}
				break;
			}
			case CONTENT_TYPE: {

				if (!CONFIG.isKeyExists(CONTENTTYPE)) {
					PropertyKeyValidation.bufferErrorMessage(CONTENTTYPE);
					isKeyword = false;
				}
				break;
			}
			case CSEQ: {

				if (!CONFIG.isKeyExists(CSEQ)) {
					PropertyKeyValidation.bufferErrorMessage(CSEQ);
					isKeyword = false;
				}
				break;
			}
			case DATE: {

				if (!CONFIG.isKeyExists(DATE)) {
					PropertyKeyValidation.bufferErrorMessage(DATE);
					isKeyword = false;
				}
				break;
			}
			case ERROR_INFO: {

				if (!CONFIG.isKeyExists(ERRORINFO)) {
					PropertyKeyValidation.bufferErrorMessage(ERRORINFO);
					isKeyword = false;
				}
				break;
			}
			case EVENT: {

				if (!CONFIG.isKeyExists(EVENT)) {
					PropertyKeyValidation.bufferErrorMessage(EVENT);
					isKeyword = false;
				}
				break;
			}
			case EXPIRES: {

				if (!CONFIG.isKeyExists(EXPIRES)) {
					PropertyKeyValidation.bufferErrorMessage(EXPIRES);
					isKeyword = false;
				}
				break;
			}
			case FROM: {

				if (!CONFIG.isKeyExists(FROM)) {
					PropertyKeyValidation.bufferErrorMessage(FROM);
					isKeyword = false;
				}
				break;
			}
			case IN_REPLY_TO: {

				if (!CONFIG.isKeyExists(INREPLYTO)) {
					PropertyKeyValidation.bufferErrorMessage(INREPLYTO);
					isKeyword = false;
				}
				break;
			}
			case MAX_FORWARDS: {

				if (!CONFIG.isKeyExists(MAXFORWARDS)) {
					PropertyKeyValidation.bufferErrorMessage(MAXFORWARDS);
					isKeyword = false;
				}
				break;
			}
			case MIME_VERSION: {

				if (!CONFIG.isKeyExists(MIMEVERSION)) {
					PropertyKeyValidation.bufferErrorMessage(MIMEVERSION);
					isKeyword = false;
				}
				break;
			}
			case MIN_SE: {

				if (!CONFIG.isKeyExists(MINSE)) {
					PropertyKeyValidation.bufferErrorMessage(MINSE);
					isKeyword = false;
				}
				break;
			}
			case MIN_EXPIRES: {

				if (!CONFIG.isKeyExists(MINEXPIRES)) {
					PropertyKeyValidation.bufferErrorMessage(MINEXPIRES);
					isKeyword = false;
				}
				break;
			}
			case ORGANIZATION: {

				if (!CONFIG.isKeyExists(ORGANIZATION)) {
					PropertyKeyValidation.bufferErrorMessage(ORGANIZATION);
					isKeyword = false;
				}
				break;
			}
			case PRIORITY: {

				if (!CONFIG.isKeyExists(PRIORITY)) {
					PropertyKeyValidation.bufferErrorMessage(PRIORITY);
					isKeyword = false;
				}
				break;
			}
			case PRIVACY: {

				if (!CONFIG.isKeyExists(PRIVACY)) {
					PropertyKeyValidation.bufferErrorMessage(PRIVACY);
					isKeyword = false;
				}
				break;
			}
			case PROXY_AUTHENTICATE: {

				if (!CONFIG.isKeyExists(PROXYAUTHENTICATE)) {
					PropertyKeyValidation.bufferErrorMessage(PROXYAUTHENTICATE);
					isKeyword = false;
				}
				break;
			}
			case PROXY_AUTHORIZATION: {

				if (!CONFIG.isKeyExists(PROXYAUTHORIZATION)) {
					PropertyKeyValidation
							.bufferErrorMessage(PROXYAUTHORIZATION);
					isKeyword = false;
				}
				break;
			}
			case PROXY_REQUIRE: {

				if (!CONFIG.isKeyExists(PROXYREQUIRE)) {
					PropertyKeyValidation.bufferErrorMessage(PROXYREQUIRE);
					isKeyword = false;
				}
				break;
			}
			case RACK: {

				if (!CONFIG.isKeyExists(RACK)) {
					PropertyKeyValidation.bufferErrorMessage(RACK);
					isKeyword = false;
				}
				break;
			}
			case REASON: {

				if (!CONFIG.isKeyExists(REASON)) {
					PropertyKeyValidation.bufferErrorMessage(REASON);
					isKeyword = false;
				}
				break;
			}
			case RECORD_ROUTE: {

				if (!CONFIG.isKeyExists(RECORDROUTE)) {
					PropertyKeyValidation.bufferErrorMessage(RECORDROUTE);
					isKeyword = false;
				}
				break;
			}
			case REFER_TO: {

				if (!CONFIG.isKeyExists(REFERTO)) {
					PropertyKeyValidation.bufferErrorMessage(REFERTO);
					isKeyword = false;
				}
				break;
			}
			case REFERRED_BY: {

				if (!CONFIG.isKeyExists(REFERREDBY)) {
					PropertyKeyValidation.bufferErrorMessage(REFERREDBY);
					isKeyword = false;
				}
				break;
			}
			case REPLACES: {

				if (!CONFIG.isKeyExists(REPLACES)) {
					PropertyKeyValidation.bufferErrorMessage(REPLACES);
					isKeyword = false;
				}
				break;
			}
			case SECURITY_CLIENT: {

				if (!CONFIG.isKeyExists(SECURITYCLIENT)) {
					PropertyKeyValidation.bufferErrorMessage(SECURITYCLIENT);
					isKeyword = false;
				}
				break;
			}
			case SECURITY_SERVER: {

				if (!CONFIG.isKeyExists(SECURITYSERVER)) {
					PropertyKeyValidation.bufferErrorMessage(SECURITYSERVER);
					isKeyword = false;
				}
				break;
			}
			case SECURITY_VERIFY: {

				if (!CONFIG.isKeyExists(SECURITYVERIFY)) {
					PropertyKeyValidation.bufferErrorMessage(SECURITYVERIFY);
					isKeyword = false;
				}
				break;
			}
			case SERVICE_ROUTE: {

				if (!CONFIG.isKeyExists(SERVICEROUTE)) {
					PropertyKeyValidation.bufferErrorMessage(SERVICEROUTE);
					isKeyword = false;
				}
				break;
			}
			case SESSION_EXPIRES: {

				if (!CONFIG.isKeyExists(SESSIONEXPIRES)) {
					PropertyKeyValidation.bufferErrorMessage(SESSIONEXPIRES);
					isKeyword = false;
				}
				break;
			}
			case REPLY_TO: {

				if (!CONFIG.isKeyExists(REPLYTO)) {
					PropertyKeyValidation.bufferErrorMessage(REPLYTO);
					isKeyword = false;
				}
				break;
			}
			case REQUIRE: {

				if (!CONFIG.isKeyExists(REQUIRE)) {
					PropertyKeyValidation.bufferErrorMessage(REQUIRE);
					isKeyword = false;
				}
				break;
			}
			case RETRY_AFTER: {

				if (!CONFIG.isKeyExists(RETRYAFTER)) {
					PropertyKeyValidation.bufferErrorMessage(RETRYAFTER);
					isKeyword = false;
				}
				break;
			}
			case ROUTE: {

				if (!CONFIG.isKeyExists(ROUTE)) {
					PropertyKeyValidation.bufferErrorMessage(ROUTE);
					isKeyword = false;
				}
				break;
			}
			case RSEQ: {

				if (!CONFIG.isKeyExists(RSEQ)) {
					PropertyKeyValidation.bufferErrorMessage(RSEQ);
					isKeyword = false;
				}
				break;
			}
			case SERVER: {

				if (!CONFIG.isKeyExists(SERVER)) {
					PropertyKeyValidation.bufferErrorMessage(SERVER);
					isKeyword = false;
				}
				break;
			}
			case SIP_ETAG: {

				if (!CONFIG.isKeyExists(SIPETAG)) {
					PropertyKeyValidation.bufferErrorMessage(SIPETAG);
					isKeyword = false;
				}
				break;
			}
			case SIP_IF_MATCH: {

				if (!CONFIG.isKeyExists(SIPIFMATCH)) {
					PropertyKeyValidation.bufferErrorMessage(SIPIFMATCH);
					isKeyword = false;
				}
				break;
			}
			case SUBJECT: {
				if (!CONFIG.isKeyExists(SUBJECT)) {
					PropertyKeyValidation.bufferErrorMessage(SUBJECT);
					isKeyword = false;
				}
				break;
			}
			case SUBSCRIPTION_STATE: {
				if (!CONFIG.isKeyExists(SUBSCRIPTIONSTATE)) {
					PropertyKeyValidation.bufferErrorMessage(SUBSCRIPTIONSTATE);
					isKeyword = false;
				}
				break;
			}
			case SUPPORTED: {

				if (!CONFIG.isKeyExists(SUPPORTED)) {
					PropertyKeyValidation.bufferErrorMessage(SUPPORTED);
					isKeyword = false;
				}
				break;
			}
			case TIMESTAMP: {

				if (!CONFIG.isKeyExists(TIMESTAMP)) {
					PropertyKeyValidation.bufferErrorMessage(TIMESTAMP);
					isKeyword = false;
				}
				break;
			}
			case TO: {

				if (!CONFIG.isKeyExists(TO)) {
					PropertyKeyValidation.bufferErrorMessage(TO);
					isKeyword = false;
				}
				break;
			}
			case UNSUPPORTED: {
				if (!CONFIG.isKeyExists(UNSUPPORTED)) {
					PropertyKeyValidation.bufferErrorMessage(UNSUPPORTED);
					isKeyword = false;
				}
				break;
			}
			case USER_AGENT: {
				if (!CONFIG.isKeyExists(USERAGENT)) {
					PropertyKeyValidation.bufferErrorMessage(USERAGENT);
					isKeyword = false;
				}
				break;
			}
			case VIA: {

				if (!CONFIG.isKeyExists(VIA)) {
					PropertyKeyValidation.bufferErrorMessage(VIA);
					isKeyword = false;
				}
				break;
			}
			case WARNING: {

				if (!CONFIG.isKeyExists(WARNING)) {
					PropertyKeyValidation.bufferErrorMessage(WARNING);
					isKeyword = false;
				}
				break;
			}
			case WWW_AUTHENTICATE: {

				if (!CONFIG.isKeyExists(WWWAUTHENTICATE)) {
					PropertyKeyValidation.bufferErrorMessage(WWWAUTHENTICATE);
					isKeyword = false;
				}
				break;
			}
			case CUSTOM: {

				break;
			}
			case P_ACCESS_NETWORK_INFO: {

				if (!CONFIG.isKeyExists(PACCESSNETWORKINFO)) {
					PropertyKeyValidation
							.bufferErrorMessage(PACCESSNETWORKINFO);
					isKeyword = false;
				}
				break;
			}

			case P_ASSERTED_IDENTITY: {

				if (!CONFIG.isKeyExists(PASSERTEDIDENTITY)) {
					PropertyKeyValidation.bufferErrorMessage(PASSERTEDIDENTITY);
					isKeyword = false;
				}
				break;
			}
			case P_ASSOCIATED_URI: {

				if (!CONFIG.isKeyExists(PASSOCIATEDURI)) {
					PropertyKeyValidation.bufferErrorMessage(PASSOCIATEDURI);
					isKeyword = false;
				}
				break;
			}
			case P_CALLED_PARTY_ID: {

				if (!CONFIG.isKeyExists(PCALLEDPARTYID)) {
					PropertyKeyValidation.bufferErrorMessage(PCALLEDPARTYID);
					isKeyword = false;
				}
				break;
			}
			case P_CHARGING_FUNCTION_ADDRESSES: {

				if (!CONFIG.isKeyExists(PCHARGINGFUNCTIONADDRESSES)) {

					PropertyKeyValidation
							.bufferErrorMessage(PCHARGINGFUNCTIONADDRESSES);
					isKeyword = false;
				}
				break;
			}
			case P_CHARGING_VECTOR: {
				if (!CONFIG.isKeyExists(PCHARGINGVECTOR)) {
					PropertyKeyValidation.bufferErrorMessage(PCHARGINGVECTOR);
					isKeyword = false;
				}
				break;
			}
			case P_MEDIA_AUTHORIZATION: {
				if (!CONFIG.isKeyExists(PMEDIAAUTHORIZATION)) {
					PropertyKeyValidation
							.bufferErrorMessage(PMEDIAAUTHORIZATION);
					isKeyword = false;
				}
				break;
			}
			case P_PREFERRED_IDENTITY: {
				if (!CONFIG.isKeyExists(PPREFERREDIDENTITY)) {
					PropertyKeyValidation
							.bufferErrorMessage(PPREFERREDIDENTITY);
					isKeyword = false;
				}
				break;
			}
			case P_VISITED_NETWORK_ID: {

				if (!CONFIG.isKeyExists(PVISITEDNETWORKID)) {
					PropertyKeyValidation.bufferErrorMessage(PVISITEDNETWORKID);
					isKeyword = false;
				}
				break;
			}
			case JOIN: {
				if (!CONFIG.isKeyExists(JOIN)) {
					PropertyKeyValidation.bufferErrorMessage(JOIN);
					isKeyword = false;
				}
				break;
			}

			}
		}
		return isKeyword;
	}

	public static boolean keyValidation(VALIDATION validation) {

		boolean isTrue = true;

		errorMsgBuf = new StringBuilder();

		for (MANDATORY mandatory : validation.getMANDATORY()) {
			for (Method method : mandatory.getMethod()) {

				if (!isKeyPresent(method.getHeader()))
					isTrue = false;

			}
		}
		for (OPTIONAL optional : validation.getOPTIONAL()) {
			for (Method method : optional.getMethod()) {
				if (!isKeyPresent(method.getHeader()))
					isTrue = false;
			}
		}

		if (!isTrue) {
			TestUtility.printMessage(NORMAL, errorMsgBuf.toString());
			LOGGER.error(errorMsgBuf.toString());
		}
		return isTrue;
	}

	public static void bufferErrorMessage(String missingKeyword) {
		if (errorMsgBuf.toString().equals("")) {
			errorMsgBuf
					.append(missingKeyword
							+ " keyword is not found in the 'validateHeader.properties' file");
		} else {
			if (errorMsgBuf.indexOf(missingKeyword) < 0) {
				if (errorMsgBuf.indexOf(", ") < 0) {
					errorMsgBuf.insert(errorMsgBuf.indexOf(" "), ", "
							+ missingKeyword);
					errorMsgBuf.delete(errorMsgBuf.indexOf("is not"),
							errorMsgBuf.indexOf("is not") + 6);
					errorMsgBuf.insert(errorMsgBuf.indexOf(" found in"),
							"are not");
					errorMsgBuf.insert(errorMsgBuf.indexOf("keyword") + 7, "s");
				} else {
					errorMsgBuf.insert(errorMsgBuf.indexOf(" keyword"),
							", " + missingKeyword);
				}
			}
		}
	}
}
