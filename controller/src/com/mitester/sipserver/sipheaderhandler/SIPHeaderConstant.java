/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SIPHeaderConstant.java
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

public class SIPHeaderConstant {
	/**
	 * SIP Header Constants are used in the add header with the value mensinoned
	 * in the default value
	 * 
	 */

	/** Default value to the Accept header */
	public static final String ACCEPT = "application/sdp";

	/** Default value to the Accept-Encoding header */
	public static final String ACCEPTENCODING = "identity";

	/** Default value to the Accept-Language header */
	public static final String ACCEPTLANGUAGE = "en";

	/** Default value to the Alert-Info header */
	public static final String ALERTINFO = "http://www.example.com/sounds/moo.wav";

	/** Default value to the Allow header */
	public static final String ALLOW = "REGISTER, INVITE, ACK, BYE, CANCEL, REFER, SUBSCRIBE, NOTIFY, INFO, MESSAGE, UPDATE, PRACK, PUBLISH";

	/** Default value to the Allow-Events header */
	public static final String ALLOWEVENTS = "presence.winfo";

	/** Default value to the Authentication-Info header */
	public static final String AUTHENTICATIONINFO = "47364c23432d2e131a5fb210812c";

	/** Default value to the Authorization header */
	public static final String AUTHORIZATION = "Digest";

	/** Default value to the Call-ID header */
	public static final String CALLID = "f81d4fae-7dec-11d0-a765-00a0c91e6bf6@sip.example.com";

	/** Default value to the Call-Info header */
	public static final String CALLINFO = "http://www.example.com/alice/photo.jpg";

	/** Default value to the Contact header */
	public static final String CONTACT = "sip:User@sip.example.com";

	/** Default value to the Content-Disposition header */
	public static final String CONTENTDISPOSITION = "Session";

	/** Default value to the Content-Encoding header */
	public static final String CONTENTENCODING = "gzip";

	/** Default value to the Content-Language header */
	public static final String CONTENTLANGUAGE = "en";

	/** Default value to the Content-Length header */
	public static final String CONTENTLENGTH = "0";

	/** Default value to the Content-Type header */
	public static final String CONTENTTYPE = "application/sdp";

	/** Default value to the CSeq header */
	public static final String CSEQ = "1 INVITE";

	/** Default value to the Date header */
	public static final String DATE = "GMT";

	/** Default value to the Error-Info header */
	public static final String ERRORINFO = "sip:not-in-service-recording@sip.example.com";

	/** Default value to the Event header */
	public static final String EVENT = "tag";

	/** Default value to the Expires header */
	public static final String EXPIRES = "3600";

	/** Default value to the From header */
	public static final String FROM = "sip:Tester@sip.example.com";

	/** Default value to the In-Reply-To header */
	public static final String INREPLYTO = "sip:Tester@sip.example.com";

	/** Default value to the Max-Forwards header */
	public static final String MAXFORWARDS = "70";

	/** Default value to the MIME-Version header */
	public static final String MIMEVERSION = "1.0";

	/** Default value to the Min-Expires header */
	public static final String MINEXPIRES = "60";

	/** Default value to the Min-SE header */
	public static final String MINSE = "60";

	/** Default value to the Organization header */
	public static final String ORGANIZATION = "Mobax.com";

	/** Default value to the Priority header */
	public static final String PRIORITY = "emergency";

	/** Default value to the Privacy header */
	public static final String PRIVACY = "None";

	/** Default value to the ProxyAuthenticate header */
	public static final String PROXYAUTHENTICATE = "Digest";

	/** Default value to the ProxyAuthorization header */
	public static final String PROXYAUTHORIZATION = "Digest";

	/** Default value to the ProxyRequire header */
	public static final String PROXYREQUIRE = "foo";

	/** Default value to the Rack header */
	public static final String RACK = "776656 1 INVITE";

	/** Default value to the Reason header */
	public static final String REASON = "200 OK Call completed elsewhere";

	/** Default value to the RecordRoute header */
	public static final String RECORDROUTE = "sip:server10.sip.example.com;lr";

	/** Default value to the ReferTo header */
	public static final String REFERTO = "sip:Tester@sip.example.com";

	/** Default value to the ReferredBy header */
	public static final String REFERREDBY = "sip:Tester@sip.example.com";

	/** Default value to the Replaces header */
	public static final String REPLACES = "98732@sip.example.com";

	/** Default value to the ReplyTo header */
	public static final String REPLYTO = "sip:Tester@sip.example.com";

	/** Default value to the Require header */
	public static final String REQUIRE = "100rel";

	/** Default value to the RetryAfter header */
	public static final String RETRYAFTER = "15";

	/** Default value to the Route header */
	public static final String ROUTE = "sip:Tester@sip.example.com";

	/** Default value to the Rseq header */
	public static final String RSEQ = "65582";

	/** Default value to the Server header */
	public static final String SERVER = "SipServer";

	/** Default value to the SIP-Etag header */
	public static final String SIPETAG = "dx200xyz";

	/** Default value to the SIP-If-Match header */
	public static final String SIPIFMATCH = "kwj449x";

	/** Default value to the Subject header */
	public static final String SUBJECT = "Testing";

	/** Default value to the SubscriptionState header */
	public static final String SUBSCRIPTIONSTATE = "active";

	/** Default value to the Supported header */
	public static final String SUPPORTED = "100rel";

	/** Default value to the SessionExpires header */
	public static final String SESSIONEXPIRES = "3600";

	/** Default value to the Timestamp header */
	public static final String TIMESTAMP = "36";

	/** Default value to the TO header */
	public static final String TO = "sip:Tester@sip.example.com";

	/** Default value to the UnSupported header */
	public static final String UNSUPPORTED = "100rel";

	/**
	 * Default value to the UserAgent header
	 */
	public static final String USERAGENT = "SipServer";

	/** Default value to the WWW-Authenticate header */
	public static final String WWWAUTHENTICATE = "Digest";

	/**
	 * Default value to the Via header
	 */
	public static final String VIA = "SIP/2.0/UDP 127.0.0.1:5060;branch=z9hG4bK77asjd";

	/**
	 * Default value to the Security-Server,Security-Client and Security-Verify
	 * header
	 */
	public static final String SECURITY = "Digest";

	/**
	 * Default value to the sip URI
	 */
	public static final String SIPURI = "sip:Tester@sip.example.com";

	/**
	 * Default value to the PVISITEDNETWORKID
	 */
	public static final String PVISITEDNETWORKID = "other.net, Visited network number 1";

	/**
	 * Default value to the p-media authorization
	 */
	public static final String P_MEDIA_AUTHORIZATION = "00200000100100101706466312e686f6d65312e6e6574000c02013942563330373200";

	/**
	 * Default value to the p-chargingvector
	 */
	public static final String P_CHARGING_VECTOR = "1234bc9876e";

	/** Default value to the service route header */
	public static final String SERVICEROUTE = "sip:server10.sip.example.com";

	/**
	 * Default value to the warning header
	 */
	public static final String WARNING = "200 OK Response";
}
