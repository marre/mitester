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
package com.mitester.sipserver;

/**
 * Validation Keywords used in the Validating the SIP Headers in the incoming
 * SIP Message
 * 
 */
public class ValidationKeywords {
	/**
	 * it holds the Accept-Encoding header keyword
	 */
	public static final String ACCEPTENCODING = "ACCEPTENCODING";

	/**
	 * it holds the Accept header keyword
	 */
	public static final String ACCEPT = "ACCEPT";

	/**
	 * it holds the Accept-Language header keyword
	 */
	public static final String ACCEPTLANGUAGE = "ACCEPTLANGUAGE";

	/**
	 * it holds the ALERT_INFO header keyword
	 */
	public static final String ALERTINFO = "ALERTINFO";

	/**
	 * it holds the ALLOW_EVENTS header keyword
	 */
	public static final String ALLOWEVENTS = "ALLOWEVENTS";

	/**
	 * it holds the ALLOW header keyword
	 */
	public static final String ALLOW = "ALLOW";
	/**
	 * it holds the AUTHENTICATION_INFO header keyword
	 */
	public static final String AUTHENTICATIONINFO = "AUTHENTICATIONINFO";
	/**
	 * it holds the AUTHORIZATION header keyword
	 */
	public static final String AUTHORIZATION = "AUTHORIZATION";
	/**
	 * it holds the CALL_ID header keyword
	 */
	public static final String CALLID = "CALLID";
	/**
	 * it holds the CALL_INFO header keyword
	 */
	public static final String CALLINFO = "CALLINFO";
	/**
	 * it holds the CONTACT header keyword
	 */
	public static final String CONTACT = "CONTACT";
	/**
	 * it holds the CONTENT_DISPOSITION header keyword
	 */
	public static final String CONTENTDISPOSITION = "CONTENTDISPOSITION";
	/**
	 * it holds the CONTENT_ENCODING header keyword
	 */
	public static final String CONTENTENCODING = "CONTENTENCODING";
	/**
	 * it holds the CONTENT_LANGUAGE header keyword
	 */
	public static final String CONTENTLANGUAGE = "CONTENTLANGUAGE";
	/**
	 * it holds the CONTENT_LENGTH header keyword
	 */
	public static final String CONTENTLENGTH = "CONTENTLENGTH";
	/**
	 * it holds the CONTENT_TYPE header keyword
	 */
	public static final String CONTENTTYPE = "CONTENTTYPE";
	/**
	 * it holds the CSEQ header keyword
	 */
	public static final String CSEQ = "CSEQ";
	/**
	 * it holds the DATE header keyword
	 */
	public static final String DATE = "DATE";
	/**
	 * it holds the ERROR_INFO header keyword
	 */
	public static final String ERRORINFO = "ERRORINFO";
	/**
	 * it holds the EVENT header keyword
	 */
	public static final String EVENT = "EVENT";
	/**
	 * it holds the EXPIRES header keyword
	 */
	public static final String EXPIRES = "EXPIRES";
	/**
	 * it holds the FROM header keyword
	 */
	public static final String FROM = "FROM";
	/**
	 * it holds the IN_REPLY_TO header keyword
	 */
	public static final String INREPLYTO = "INREPLYTO";
	/**
	 * it holds the MAX_FORWARDS header keyword
	 */
	public static final String MAXFORWARDS = "MAXFORWARDS";
	/**
	 * it holds the MIME_VERSION header keyword
	 */
	public static final String MIMEVERSION = "MIMEVERSION";
	/**
	 * it holds the MIN_SE header keyword
	 */
	public static final String MINSE = "MINSE";
	/**
	 * it holds the MIN_EXPIRES header keyword
	 */
	public static final String MINEXPIRES = "MINEXPIRES";
	/**
	 * it holds the ORGANIZATION header keyword
	 */
	public static final String ORGANIZATION = "ORGANIZATION";
	/**
	 * it holds the PRIORITY header keyword
	 */
	public static final String PRIORITY = "PRIORITY";
	/**
	 * it holds the PRIVACY header keyword
	 */
	public static final String PRIVACY = "PRIVACY";
	/**
	 * it holds the PROXY_AUTHENTICATE header keyword
	 */
	public static final String PROXYAUTHENTICATE = "PROXYAUTHENTICATE";
	/**
	 * it holds the PROXY_AUTHORIZATION header keyword
	 */
	public static final String PROXYAUTHORIZATION = "PROXYAUTHORIZATION";
	/**
	 * it holds the PROXY_REQUIRE header keyword
	 */
	public static final String PROXYREQUIRE = "PROXYREQUIRE";
	/**
	 * it holds the RACK header keyword
	 */
	public static final String RACK = "RACK";
	/**
	 * it holds the REASON header keyword
	 */
	public static final String REASON = "REASON";
	/**
	 * it holds the RECORD_ROUTE header keyword
	 */
	public static final String RECORDROUTE = "RECORDROUTE";
	/**
	 * it holds the REFER_TO header keyword
	 */
	public static final String REFERTO = "REFERTO";
	/**
	 * it holds the REFERRED_BY header keyword
	 */
	public static final String REFERREDBY = "REFERREDBY";
	/**
	 * it holds the REPLACES header keyword
	 */
	public static final String REPLACES = "REPLACES";
	/**
	 * it holds the SECURITY_CLIENT header keyword
	 */
	public static final String SECURITYCLIENT = "SECURITYCLIENT";
	/**
	 * it holds the SECURITY_SERVER header keyword
	 */
	public static final String SECURITYSERVER = "SECURITYSERVER";
	/**
	 * it holds the SECURITY_VERIFY header keyword
	 */
	public static final String SECURITYVERIFY = "SECURITYVERIFY";
	/**
	 * it holds the SERVICE_ROUTE header keyword
	 */
	public static final String SERVICEROUTE = "SERVICEROUTE";
	/**
	 * it holds the SESSION_EXPIRES header keyword
	 */
	public static final String SESSIONEXPIRES = "SESSIONEXPIRES";
	/**
	 * it holds the REPLY_TO header keyword
	 */
	public static final String REPLYTO = "REPLYTO";
	/**
	 * it holds the REQUIRE header keyword
	 */
	public static final String REQUIRE = "REQUIRE";
	/**
	 * it holds the RETRY_AFTER header keyword
	 */
	public static final String RETRYAFTER = "RETRYAFTER";

	/**
	 * it holds the ROUTE header keyword
	 */
	public static final String ROUTE = "ROUTE";
	/**
	 * it holds the RSEQ header keyword
	 */
	public static final String RSEQ = "RSEQ";

	/**
	 * it holds the SERVER header keyword
	 */
	public static final String SERVER = "SERVER";
	/**
	 * it holds the SIP_ETAG header keyword
	 */
	public static final String SIPETAG = "SIPETAG";

	/**
	 * it holds the SIP_IFMATCH header keyword
	 */
	public static final String SIPIFMATCH = "SIPIFMATCH";
	/**
	 * it holds the SUBJECT header keyword
	 */
	public static final String SUBJECT = "SUBJECT";
	/**
	 * it holds the SUBSCRIPTION_STATE header keyword
	 */
	public static final String SUBSCRIPTIONSTATE = "SUBSCRIPTIONSTATE";
	/**
	 * it holds the SUPPORTED header keyword
	 */
	public static final String SUPPORTED = "SUPPORTED";
	/**
	 * it holds the TIMESTAMP header keyword
	 */
	public static final String TIMESTAMP = "TIMESTAMP";

	/**
	 * it holds the To header keyword
	 */
	public static final String TO = "TO";
	/**
	 * it holds the UNSUPPORTED header keyword
	 */
	public static final String UNSUPPORTED = "UNSUPPORTED";
	/**
	 * it holds the USER_AGENT header keyword
	 */
	public static final String USERAGENT = "USERAGENT";
	/**
	 * it holds the VIA header keyword
	 */
	public static final String VIA = "VIA";

	/**
	 * it holds the WARNING header keyword
	 */
	public static final String WARNING = "WARNING";
	/**
	 * it holds the WWW_AUTHENTICATE header keyword
	 */
	public static final String WWWAUTHENTICATE = "WWWAUTHENTICATE";
	/**
	 * it holds the P_VISITED_NETWORK_ID header keyword
	 */
	public static final String PVISITEDNETWORKID = "PVISITEDNETWORKID";
	/**
	 * it holds the P_PREFERRED_IDENTITY header keyword
	 */
	public static final String PPREFERREDIDENTITY = "PPREFERREDIDENTITY";
	/**
	 * it holds the P_MEDIA_AUTHORIZATION header keyword
	 */
	public static final String PMEDIAAUTHORIZATION = "PMEDIAAUTHORIZATION";
	/**
	 * it holds the P_CHARGING_VECTOR header keyword
	 */
	public static final String PCHARGINGVECTOR = "PCHARGINGVECTOR";
	/**
	 * it holds the P_CHARGING_FUNCTION_ADDRESSES header keyword
	 */
	public static final String PCHARGINGFUNCTIONADDRESSES = "PCHARGINGFUNCTIONADDRESSES";

	/**
	 * it holds the P_CALLED_PARTY_ID header keyword
	 */
	public static final String PCALLEDPARTYID = "PCALLEDPARTYID";
	/**
	 * it holds the P_ASSOCIATED_URI header keyword
	 */
	public static final String PASSOCIATEDURI = "PASSOCIATEDURI";
	/**
	 * it holds the P_ASSERTED_IDENTITY header keyword
	 */
	public static final String PASSERTEDIDENTITY = "PASSERTEDIDENTITY";
	/**
	 * it holds the P_ACCESS_NETWORK_INFO header keyword
	 */
	public static final String PACCESSNETWORKINFO = "PACCESSNETWORKINFO";
	/**
	 * it holds the JOIN header keyword
	 */
	public static final String JOIN = "JOIN";

}
