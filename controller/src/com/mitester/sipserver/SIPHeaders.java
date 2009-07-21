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
package com.mitester.sipserver;

/**
 * Defining the enumaration to all the Headers supported by the JAIN SIP Stack
 * Version JAIN-SIP-1-2-90
 * 
 * 
 * 
 */

public enum SIPHeaders {
	/**
	 * It holdes Accept-Encoding Header
	 */
	ACCEPT_ENCODING {
		@Override
		public int getId() {
			return 1;
		}

	},
	/**
	 * It holdes Accept Header
	 */
	ACCEPT {
		@Override
		public int getId() {
			return 2;
		}
	},
	/**
	 * It holdes Accept-Language Header
	 */
	ACCEPT_LANGUAGE {
		@Override
		public int getId() {
			return 3;
		}
	},
	/**
	 * It holdes Alert-Info Header
	 */
	ALERT_INFO {
		@Override
		public int getId() {
			return 4;
		}
	},
	/**
	 * It holdes Allow-Events Header
	 */
	ALLOW_EVENTS {
		@Override
		public int getId() {
			return 5;
		}
	},
	/**
	 * It holdes compact form of allow-events Header
	 */
	u {
		@Override
		public int getId() {
			return 5;
		}
	},
	/**
	 * It holdes Allow Header
	 */
	ALLOW {
		@Override
		public int getId() {
			return 6;
		}
	},
	/**
	 * It holdes Authentication-Info Header
	 */
	AUTHENTICATION_INFO {
		@Override
		public int getId() {
			return 7;
		}
	},
	/**
	 * It holdes Authorization Header
	 */
	AUTHORIZATION {
		@Override
		public int getId() {
			return 8;
		}
	},
	/**
	 * It holdes Call-ID Header
	 */
	CALL_ID {
		@Override
		public int getId() {
			return 9;
		}
	},
	/**
	 * It holdes Compact form Call-ID Header
	 */
	i {
		@Override
		public int getId() {
			return 9;
		}
	},
	/**
	 * It holdes Call-Info Header
	 */
	CALL_INFO {
		@Override
		public int getId() {
			return 10;
		}
	},
	/**
	 * It holdes Contact Header
	 */
	CONTACT {
		@Override
		public int getId() {
			return 11;
		}
	},
	/**
	 * It holdes Compact form Contact Header
	 */
	m {
		@Override
		public int getId() {
			return 11;
		}
	},
	/**
	 * It holdes Content-Disposition Header
	 */
	CONTENT_DISPOSITION {
		@Override
		public int getId() {
			return 12;
		}
	},
	/**
	 * It holdes Content-Encoding Header
	 */
	CONTENT_ENCODING {
		@Override
		public int getId() {
			return 13;
		}
	},
	/**
	 * It holdes Compact form of content-Encoding Header
	 */
	e {
		@Override
		public int getId() {
			return 13;
		}
	},
	/**
	 * It holdes Content-Language Header
	 */
	CONTENT_LANGUAGE {
		@Override
		public int getId() {
			return 14;
		}
	},
	/**
	 * It holdes Content-Length Header
	 */
	CONTENT_LENGTH {
		@Override
		public int getId() {
			return 15;
		}
	},
	/**
	 * It holdes Compact form of content-Length Header
	 */
	l {
		@Override
		public int getId() {
			return 15;
		}
	},
	/**
	 * It holdes Cotent-Type Header
	 */
	CONTENT_TYPE {
		@Override
		public int getId() {
			return 16;
		}
	},
	/**
	 * It holdes Compact form of Content-Type Header
	 */
	c {
		@Override
		public int getId() {
			return 16;
		}
	},
	/**
	 * It holdes CSeq Header
	 */
	CSEQ {
		@Override
		public int getId() {
			return 17;
		}
	},
	/**
	 * It holdes Date Header
	 */
	DATE {
		@Override
		public int getId() {
			return 18;
		}
	},
	/**
	 * It holdes Error-Info Header
	 */
	ERROR_INFO {
		@Override
		public int getId() {
			return 19;
		}
	},
	/**
	 * It holdes Event Header
	 */
	EVENT {
		@Override
		public int getId() {
			return 20;
		}
	},
	/**
	 * It holdes Compact form of Event Header
	 */
	o {
		@Override
		public int getId() {
			return 20;
		}
	},
	/**
	 * It holdes Expires Header
	 */
	EXPIRES {
		@Override
		public int getId() {
			return 21;
		}
	},
	/**
	 * It holdes From Header
	 */
	FROM {
		@Override
		public int getId() {
			return 22;
		}
	},
	/**
	 * It holdes compact form of From Header
	 */
	f {
		@Override
		public int getId() {
			return 22;
		}
	},
	/**
	 * It holdes In-Reply-To Header
	 */
	IN_REPLY_TO {
		@Override
		public int getId() {
			return 23;
		}
	},
	/**
	 * It holdes Max-Forwards Header
	 */
	MAX_FORWARDS {
		@Override
		public int getId() {
			return 24;
		}
	},
	/**
	 * It holdes MIME-Version Header
	 */
	MIME_VERSION {
		@Override
		public int getId() {
			return 25;
		}
	},
	/**
	 * It holdes Min-SE Header
	 */
	MIN_SE {
		@Override
		public int getId() {
			return 26;
		}
	},
	/**
	 * It holdes Min_Expires Header
	 */
	MIN_EXPIRES {
		@Override
		public int getId() {
			return 27;
		}
	},
	/**
	 * It holdes Organization Header
	 */
	ORGANIZATION {
		@Override
		public int getId() {
			return 28;
		}
	},
	/**
	 * It holdes Priority Header
	 */
	PRIORITY {
		@Override
		public int getId() {
			return 29;
		}
	},
	/**
	 * It holdes Privacy Header
	 */
	PRIVACY {
		@Override
		public int getId() {
			return 30;
		}
	},
	/**
	 * It holdes Proxy-Authenticate Header
	 */
	PROXY_AUTHENTICATE {
		@Override
		public int getId() {
			return 31;
		}
	},
	/**
	 * It holdes Proxy-Authorization Header
	 */
	PROXY_AUTHORIZATION {
		@Override
		public int getId() {
			return 32;
		}
	},
	/**
	 * It holdes Proxy-Require Header
	 */
	PROXY_REQUIRE {
		@Override
		public int getId() {
			return 33;
		}
	},
	/**
	 * It holdes RAck Header
	 */
	RACK {
		@Override
		public int getId() {
			return 34;
		}
	},
	/**
	 * It holdes Reason Header
	 */
	REASON {
		@Override
		public int getId() {
			return 35;
		}
	},
	/**
	 * It holdes Record-Route Header
	 */
	RECORD_ROUTE {
		@Override
		public int getId() {
			return 36;
		}
	},
	/**
	 * It holdes Refer-To Header
	 */
	REFER_TO {
		@Override
		public int getId() {
			return 37;
		}
	},
	/**
	 * It holdes Compact form of Refer-To Header
	 */
	r {
		@Override
		public int getId() {
			return 37;
		}
	},
	/**
	 * It holdes Referred-By Header
	 */
	REFERRED_BY {
		@Override
		public int getId() {
			return 38;
		}
	},
	/**
	 * It holdes compact form of Referred-By Header
	 */
	b {
		@Override
		public int getId() {
			return 38;
		}
	},
	/**
	 * It holdes Replaces Header
	 */
	REPLACES {
		@Override
		public int getId() {
			return 39;
		}
	},
	/**
	 * It holdes Security-Client Header
	 */
	SECURITY_CLIENT {
		@Override
		public int getId() {
			return 40;
		}
	},
	/**
	 * It holdes Security-Server Header
	 */
	SECURITY_SERVER {
		@Override
		public int getId() {
			return 41;
		}
	},
	/**
	 * It holdes Security-Verify Header
	 */
	SECURITY_VERIFY {
		@Override
		public int getId() {
			return 42;
		}
	},
	/**
	 * It holdes Service-Route Header
	 */
	SERVICE_ROUTE {
		@Override
		public int getId() {
			return 43;
		}
	},
	/**
	 * It holdes Session-Expires Header
	 */
	SESSION_EXPIRES {
		@Override
		public int getId() {
			return 44;
		}
	},
	/**
	 * It holdes Reply-To Header
	 */
	REPLY_TO {
		@Override
		public int getId() {
			return 45;
		}
	},
	/**
	 * It holdes Require Header
	 */
	REQUIRE {
		@Override
		public int getId() {
			return 46;
		}
	},
	/**
	 * It holdes Retry-After Header
	 */
	RETRY_AFTER {
		@Override
		public int getId() {
			return 47;
		}
	},
	/**
	 * It holdes Route Header
	 */
	ROUTE {
		@Override
		public int getId() {
			return 48;
		}
	},
	/**
	 * It holdes RSeq Header
	 */
	RSEQ {
		@Override
		public int getId() {
			return 49;
		}
	},
	/**
	 * It holdes Server Header
	 */
	SERVER {
		@Override
		public int getId() {
			return 50;
		}
	},
	/**
	 * It holdes SIP-ETag Header
	 */
	SIP_ETAG {
		@Override
		public int getId() {
			return 51;
		}
	},
	/**
	 * It holdes SIP-If-Match Header
	 */
	SIP_IF_MATCH {
		@Override
		public int getId() {
			return 52;
		}
	},
	/**
	 * It holdes Subject Header
	 */
	SUBJECT {
		@Override
		public int getId() {
			return 53;
		}
	},
	/**
	 * It holdes compact form Subject Header
	 */
	s {
		@Override
		public int getId() {
			return 53;
		}
	},
	/**
	 * It holdes Subscription-State Header
	 */
	SUBSCRIPTION_STATE {
		@Override
		public int getId() {
			return 54;
		}
	},
	/**
	 * It holdes Supported Header
	 */
	SUPPORTED {
		@Override
		public int getId() {
			return 55;
		}
	},
	/**
	 * It holdes Compact form of Supported Header
	 */
	k {
		@Override
		public int getId() {
			return 55;
		}
	},
	/**
	 * It holdes Timestamp Header
	 */
	TIMESTAMP {
		@Override
		public int getId() {
			return 56;
		}
	},
	/**
	 * It holdes To Header
	 */
	TO {
		@Override
		public int getId() {
			return 57;
		}
	},
	/**
	 * It holdes Compact form of To Header
	 */
	t {
		@Override
		public int getId() {
			return 57;
		}
	},
	/**
	 * It holdes unsupported Header
	 */
	UNSUPPORTED {
		@Override
		public int getId() {
			return 58;
		}
	},
	/**
	 * It holdes User-Agent Header
	 */
	USER_AGENT {
		@Override
		public int getId() {
			return 59;
		}
	},
	/**
	 * It holdes Via Header
	 */
	VIA {
		@Override
		public int getId() {
			return 60;
		}
	},
	/**
	 * It holdes compact form Via Header
	 */
	v {
		@Override
		public int getId() {
			return 60;
		}
	},
	/**
	 * It holdes Warning Header
	 */
	WARNING {
		@Override
		public int getId() {
			return 61;
		}
	},
	/**
	 * It holdes WWW-Authenticate Header
	 */
	WWW_AUTHENTICATE {
		@Override
		public int getId() {
			return 62;
		}
	},
	/**
	 * It holdes other than RFC Headers (Not supported by JAIN SIP Stack) Header
	 */
	CUSTOM {
		@Override
		public int getId() {
			return 63;
		}
	},
	/**
	 * It holdes request-line Header
	 */
	REQUEST_LINE {
		@Override
		public int getId() {
			return 64;
		}
	},
	/**
	 * It holdes status-line Header
	 */
	STATUS_LINE {
		@Override
		public int getId() {
			return 65;
		}
	},
	/**
	 * It holdes P-Visited-Network-ID Header
	 */
	P_VISITED_NETWORK_ID {
		@Override
		public int getId() {
			return 66;
		}
	},
	/**
	 * It holdes AP-Preferred-Identity Header
	 */
	P_PREFERRED_IDENTITY {
		@Override
		public int getId() {
			return 67;
		}
	},
	/**
	 * It holdes P-Media-Authorization Header
	 */
	P_MEDIA_AUTHORIZATION {
		@Override
		public int getId() {
			return 68;
		}
	},
	/**
	 * It holdes P-Charging-Vector Header
	 */
	P_CHARGING_VECTOR {
		@Override
		public int getId() {
			return 69;
		}
	},
	/**
	 * It holdes P-Charging-Functino-Addresses Header
	 */
	P_CHARGING_FUNCTION_ADDRESSES {
		@Override
		public int getId() {
			return 70;
		}
	},
	/**
	 * It holdes P-Called-Party-ID Header
	 */
	P_CALLED_PARTY_ID {
		@Override
		public int getId() {
			return 71;
		}
	},
	/**
	 * It holdes P-Associated-URI Header
	 */
	P_ASSOCIATED_URI {
		@Override
		public int getId() {
			return 72;
		}
	},
	/**
	 * It holdes P-Associated-Identity Header
	 */
	P_ASSERTED_IDENTITY {
		@Override
		public int getId() {
			return 73;
		}
	},
	/**
	 * It holdes P-Access-Network-Info Header
	 */
	P_ACCESS_NETWORK_INFO {
		@Override
		public int getId() {
			return 74;
		}
	},
	/**
	 * It holdes Join Header
	 */
	JOIN {
		@Override
		public int getId() {
			return 75;
		}
	};
	/**
	 * getSIPHeaderfromId is the method used to get the header from the enum
	 * value
	 * 
	 * @param Id
	 * @return SIPHeaders
	 */
	public static SIPHeaders getSIPHeaderfromId(int Id) {
		switch (Id) {
			case 1:
				return ACCEPT_ENCODING;

			case 2:
				return ACCEPT;

			case 3:
				return ACCEPT_LANGUAGE;

			case 4:
				return ALERT_INFO;

			case 5:
				return ALLOW_EVENTS;

			case 6:
				return ALLOW;

			case 7:
				return AUTHENTICATION_INFO;

			case 8:
				return AUTHORIZATION;

			case 9:
				return CALL_ID;

			case 10:
				return CALL_INFO;

			case 11:
				return CONTACT;

			case 12:
				return CONTENT_DISPOSITION;

			case 13:
				return CONTENT_ENCODING;

			case 14:
				return CONTENT_LANGUAGE;

			case 15:
				return CONTENT_LENGTH;

			case 16:
				return CONTENT_TYPE;

			case 17:
				return CSEQ;

			case 18:
				return DATE;

			case 19:
				return ERROR_INFO;

			case 20:
				return EVENT;

			case 21:
				return EXPIRES;

			case 22:
				return FROM;

			case 23:
				return IN_REPLY_TO;

			case 24:
				return MAX_FORWARDS;

			case 25:
				return MIME_VERSION;

			case 26:
				return MIN_SE;

			case 27:
				return MIN_EXPIRES;

			case 28:
				return ORGANIZATION;

			case 29:
				return PRIORITY;

			case 30:
				return PRIVACY;

			case 31:
				return PROXY_AUTHENTICATE;

			case 32:
				return PROXY_AUTHORIZATION;

			case 33:
				return PROXY_REQUIRE;

			case 34:
				return RACK;

			case 35:
				return REASON;

			case 36:
				return RECORD_ROUTE;

			case 37:
				return REFER_TO;

			case 38:
				return REFERRED_BY;

			case 39:
				return REPLACES;

			case 40:
				return SECURITY_CLIENT;

			case 41:
				return SECURITY_SERVER;

			case 42:
				return SECURITY_VERIFY;

			case 43:
				return SERVICE_ROUTE;

			case 44:
				return SESSION_EXPIRES;

			case 45:
				return REPLY_TO;

			case 46:
				return REQUIRE;

			case 47:
				return RETRY_AFTER;

			case 48:
				return ROUTE;

			case 49:
				return RSEQ;

			case 50:
				return SERVER;

			case 51:
				return SIP_ETAG;

			case 52:
				return SIP_IF_MATCH;

			case 53:
				return SUBJECT;

			case 54:
				return SUBSCRIPTION_STATE;

			case 55:
				return SUPPORTED;

			case 56:
				return TIMESTAMP;

			case 57:
				return TO;

			case 58:
				return UNSUPPORTED;

			case 59:
				return USER_AGENT;

			case 60:
				return VIA;

			case 61:
				return WARNING;

			case 62:
				return WWW_AUTHENTICATE;

			case 63:
				return CUSTOM;

			case 64:
				return REQUEST_LINE;

			case 65:
				return STATUS_LINE;
			case 66:
				return P_VISITED_NETWORK_ID;
			case 67:
				return P_PREFERRED_IDENTITY;
			case 68:
				return P_MEDIA_AUTHORIZATION;
			case 69:
				return P_CHARGING_VECTOR;
			case 70:
				return P_CHARGING_FUNCTION_ADDRESSES;
			case 71:
				return P_CALLED_PARTY_ID;
			case 72:
				return P_ASSOCIATED_URI;
			case 73:
				return P_ASSERTED_IDENTITY;
			case 74:
				return P_ACCESS_NETWORK_INFO;
			case 75:
				return JOIN;
		}
		return null;
	}

	/**
	 * getSipHeaderfromString is the methos used to get the header name from the
	 * string
	 * 
	 * @param header
	 * @return SIPHeaders
	 */
	public static SIPHeaders getSipHeaderfromString(String header) {

		if (header.equalsIgnoreCase("ACCEPT-ENCODING"))
			return ACCEPT_ENCODING;

		else if (header.equalsIgnoreCase("ACCEPT"))
			return ACCEPT;

		else if (header.equalsIgnoreCase("ACCEPT-LANGUAGE"))
			return ACCEPT_LANGUAGE;

		else if (header.equalsIgnoreCase("ALERT-INFO"))
			return ALERT_INFO;

		else if (header.equalsIgnoreCase("ALLOW-EVENTS"))
			return ALLOW_EVENTS;

		else if (header.equalsIgnoreCase("u"))
			return ALLOW_EVENTS;

		else if (header.equalsIgnoreCase("ALLOW"))
			return ALLOW;

		else if (header.equalsIgnoreCase("AUTHENTICATION-INFO"))
			return AUTHENTICATION_INFO;

		else if (header.equalsIgnoreCase("AUTHORIZATION"))
			return AUTHORIZATION;

		else if (header.equalsIgnoreCase("CALL-ID"))
			return CALL_ID;

		else if (header.equalsIgnoreCase("i"))
			return CALL_ID;

		else if (header.equalsIgnoreCase("CALL-INFO"))
			return CALL_INFO;

		else if (header.equalsIgnoreCase("CONTACT"))
			return CONTACT;

		else if (header.equalsIgnoreCase("m"))
			return CONTACT;

		else if (header.equalsIgnoreCase("CONTENT-DISPOSITION"))
			return CONTENT_DISPOSITION;

		else if (header.equalsIgnoreCase("CONTENT-ENCODING"))
			return CONTENT_ENCODING;

		else if (header.equalsIgnoreCase("e"))
			return CONTENT_ENCODING;

		else if (header.equalsIgnoreCase("CONTENT-LANGUAGE"))
			return CONTENT_LANGUAGE;

		else if (header.equalsIgnoreCase("CONTENT-LENGTH"))
			return CONTENT_LENGTH;

		else if (header.equalsIgnoreCase("l"))
			return CONTENT_LENGTH;

		else if (header.equalsIgnoreCase("CONTENT-TYPE"))
			return CONTENT_TYPE;

		else if (header.equalsIgnoreCase("c"))
			return CONTENT_TYPE;

		else if (header.equalsIgnoreCase("CSEQ"))
			return CSEQ;

		else if (header.equalsIgnoreCase("DATE"))
			return DATE;

		else if (header.equalsIgnoreCase("ERROR-INFO"))
			return ERROR_INFO;

		else if (header.equalsIgnoreCase("EVENT"))
			return EVENT;

		else if (header.equalsIgnoreCase("o"))
			return EVENT;

		else if (header.equalsIgnoreCase("EXPIRES"))
			return EXPIRES;

		else if (header.equalsIgnoreCase("FROM"))
			return FROM;
		else if (header.equalsIgnoreCase("f"))
			return FROM;
		else if (header.equalsIgnoreCase("IN-REPLY-TO"))
			return IN_REPLY_TO;

		else if (header.equalsIgnoreCase("MAX-FORWARDS"))
			return MAX_FORWARDS;

		else if (header.equalsIgnoreCase("MIME-VERSION"))
			return MIME_VERSION;

		else if (header.equalsIgnoreCase("MIN-SE"))
			return MIN_SE;

		else if (header.equalsIgnoreCase("MIN-EXPIRES"))
			return MIN_EXPIRES;

		else if (header.equalsIgnoreCase("ORGANIZATION"))
			return ORGANIZATION;

		else if (header.equalsIgnoreCase("PRIORITY"))
			return PRIORITY;

		else if (header.equalsIgnoreCase("PRIVACY"))
			return PRIVACY;

		else if (header.equalsIgnoreCase("PROXY-AUTHENTICATE"))
			return PROXY_AUTHENTICATE;

		else if (header.equalsIgnoreCase("PROXY-AUTHORIZATION"))
			return PROXY_AUTHORIZATION;

		else if (header.equalsIgnoreCase("PROXY-REQUIRE"))
			return PROXY_REQUIRE;

		else if (header.equalsIgnoreCase("RACK"))
			return RACK;

		else if (header.equalsIgnoreCase("REASON"))
			return REASON;

		else if (header.equalsIgnoreCase("RECORD-ROUTE"))
			return RECORD_ROUTE;

		else if (header.equalsIgnoreCase("REFER-TO"))
			return REFER_TO;

		else if (header.equalsIgnoreCase("r"))
			return REFER_TO;

		else if (header.equalsIgnoreCase("REFERRED-BY"))
			return REFERRED_BY;

		else if (header.equalsIgnoreCase("b"))
			return REFERRED_BY;

		else if (header.equalsIgnoreCase("REPLACES"))
			return REPLACES;

		else if (header.equalsIgnoreCase("SECURITY-CLIENT"))
			return SECURITY_CLIENT;

		else if (header.equalsIgnoreCase("SECURITY-SERVER"))
			return SECURITY_SERVER;

		else if (header.equalsIgnoreCase("SECURITY-VERIFY"))
			return SECURITY_VERIFY;

		else if (header.equalsIgnoreCase("SERVICE-ROUTE"))
			return SERVICE_ROUTE;

		else if (header.equalsIgnoreCase("SESSION-EXPIRES"))
			return SESSION_EXPIRES;

		else if (header.equalsIgnoreCase("REPLY-TO"))
			return REPLY_TO;

		else if (header.equalsIgnoreCase("REQUIRE"))
			return REQUIRE;

		else if (header.equalsIgnoreCase("RETRY-AFTER"))
			return RETRY_AFTER;

		else if (header.equalsIgnoreCase("ROUTE"))
			return ROUTE;

		else if (header.equalsIgnoreCase("RSEQ"))
			return RSEQ;

		else if (header.equalsIgnoreCase("SERVER"))
			return SERVER;

		else if (header.equalsIgnoreCase("SIP-ETAG"))
			return SIP_ETAG;

		else if (header.equalsIgnoreCase("SIP-IF-MATCH"))
			return SIP_IF_MATCH;

		else if (header.equalsIgnoreCase("SUBJECT"))
			return SUBJECT;

		else if (header.equalsIgnoreCase("s"))
			return SUBJECT;

		else if (header.equalsIgnoreCase("SUBSCRIPTION-STATE"))
			return SUBSCRIPTION_STATE;

		else if (header.equalsIgnoreCase("SUPPORTED"))
			return SUPPORTED;

		else if (header.equalsIgnoreCase("k"))
			return SUPPORTED;

		else if (header.equalsIgnoreCase("TIMESTAMP"))
			return TIMESTAMP;

		else if (header.equalsIgnoreCase("TO"))
			return TO;

		else if (header.equalsIgnoreCase("t"))
			return TO;

		else if (header.equalsIgnoreCase("UNSUPPORTED"))
			return UNSUPPORTED;

		else if (header.equalsIgnoreCase("USER-AGENT"))
			return USER_AGENT;

		else if (header.equalsIgnoreCase("VIA"))
			return VIA;

		else if (header.equalsIgnoreCase("v"))
			return VIA;

		else if (header.equalsIgnoreCase("WARNING"))
			return WARNING;

		else if (header.equalsIgnoreCase("WWW-AUTHENTICATE"))
			return WWW_AUTHENTICATE;

		// else if (header.equalsIgnoreCase("REQUEST-LINE"))
		// return REQUEST_LINE;
		//
		// else if (header.equalsIgnoreCase("STATUS-LINE"))
		// return STATUS_LINE;

		else if (header.equalsIgnoreCase("P-VISITED-NETWORK-ID"))
			return P_VISITED_NETWORK_ID;

		else if (header.equalsIgnoreCase("P-PREFERRED-IDENTITY"))
			return P_PREFERRED_IDENTITY;

		else if (header.equalsIgnoreCase("P-MEDIA-AUTHORIZATION"))
			return P_MEDIA_AUTHORIZATION;

		else if (header.equalsIgnoreCase("P-CHARGING-VECTOR"))
			return P_CHARGING_VECTOR;

		else if (header.equalsIgnoreCase("P-CHARGING-FUNCTION-ADDRESSES"))
			return P_CHARGING_FUNCTION_ADDRESSES;

		else if (header.equalsIgnoreCase("P-CALLED-PARTY-ID"))
			return P_CALLED_PARTY_ID;

		else if (header.equalsIgnoreCase("P-ASSOCIATED-URI"))
			return P_ASSOCIATED_URI;

		else if (header.equalsIgnoreCase("P-ASSERTED-IDENTITY"))
			return P_ASSERTED_IDENTITY;

		else if (header.equalsIgnoreCase("P-ACCESS-NETWORK-INFO"))
			return P_ACCESS_NETWORK_INFO;
		else if (header.equalsIgnoreCase("JOIN"))
			return JOIN;
		return CUSTOM;
	}

	/**
	 * toString method is used to get the Header name supported by the Jain SIP
	 * Stack return Header Name as per RFC.
	 */
	@Override
	public String toString() {
		switch (this) {
			case ACCEPT_ENCODING:
				return "Accept-Encoding";
			case ACCEPT:
				return "Accept";
			case ACCEPT_LANGUAGE:
				return "Accept-Language";
			case ALERT_INFO:
				return "Alert-Info";
			case ALLOW_EVENTS:
				return "Allow-Events";
			case ALLOW:
				return "Allow";
			case AUTHENTICATION_INFO:
				return "Authentication-Info";
			case AUTHORIZATION:
				return "Authorization";
			case CALL_ID:
				return "Call-ID";
			case CALL_INFO:
				return "Call-Info";
			case CONTACT:
				return "Contact";
			case CONTENT_DISPOSITION:
				return "Content-Disposition";
			case CONTENT_ENCODING:
				return "Content-Encoding";
			case CONTENT_LANGUAGE:
				return "Content-Language";
			case CONTENT_LENGTH:
				return "Content-Length";
			case CONTENT_TYPE:
				return "Content-Type";
			case CSEQ:
				return "CSeq";
			case DATE:
				return "Date";
			case ERROR_INFO:
				return "Error-Info";
			case EVENT:
				return "Event";
			case EXPIRES:
				return "Expires";
			case FROM:
				return "From";
			case IN_REPLY_TO:
				return "In-Reply-To";
			case MAX_FORWARDS:
				return "Max-Forwards";
			case MIME_VERSION:
				return "MIME-Version";
			case MIN_SE:
				return "Min-SE";
			case MIN_EXPIRES:
				return "Min-Expires";
			case ORGANIZATION:
				return "Organization";
			case PRIORITY:
				return "Priority";
			case PRIVACY:
				return "Privacy";
			case PROXY_AUTHENTICATE:
				return "Proxy-Authenticate";
			case PROXY_AUTHORIZATION:
				return "Proxy-Authorization";
			case PROXY_REQUIRE:
				return "Proxy-Require";
			case RACK:
				return "RAck";
			case REASON:
				return "Reason";
			case RECORD_ROUTE:
				return "Record-Route";
			case REFER_TO:
				return "Refer-To";
			case REFERRED_BY:
				return "Referred-By";
			case REPLACES:
				return "Replaces";
			case SECURITY_CLIENT:
				return "Security-Client";
			case SECURITY_SERVER:
				return "Security-Server";
			case SECURITY_VERIFY:
				return "Security-Verify";
			case SERVICE_ROUTE:
				return "Service-Route";
			case SESSION_EXPIRES:
				return "Session-Expires";
			case REPLY_TO:
				return "Reply-To";
			case REQUIRE:
				return "Require";
			case RETRY_AFTER:
				return "Retry-After";
			case ROUTE:
				return "Route";
			case RSEQ:
				return "RSeq";
			case SERVER:
				return "Server";
			case SIP_ETAG:
				return "SIP-ETag"; // header name SIP-Etag, t is in small
			case SIP_IF_MATCH:
				return "SIP-If-Match";
			case SUBJECT:
				return "Subject";
			case SUBSCRIPTION_STATE:
				return "Subscription-State";
			case SUPPORTED:
				return "Supported";
			case TIMESTAMP:
				return "Timestamp";
			case TO:
				return "To";
			case UNSUPPORTED:
				return "Unsupported";
			case USER_AGENT:
				return "User-Agent";
			case VIA:
				return "Via";
			case WARNING:
				return "Warning";
			case WWW_AUTHENTICATE:
				return "WWW-Authenticate";
			case P_VISITED_NETWORK_ID:
				return "P-Visited-Network-ID";
			case P_PREFERRED_IDENTITY:
				return "P-Preferred-Identity";
			case P_MEDIA_AUTHORIZATION:
				return "P-Media-Authorization";
			case P_CHARGING_VECTOR:
				return "P-Charging-Vector ";
			case P_CHARGING_FUNCTION_ADDRESSES:
				return "P-Charging-Function-Addresses";
			case P_CALLED_PARTY_ID:
				return "P-Called-Party-ID";
			case P_ASSOCIATED_URI:
				return "P-Associated-URI";
			case P_ASSERTED_IDENTITY:
				return "P-Asserted-Identity";
			case P_ACCESS_NETWORK_INFO:
				return "P-Access-Network-Info";
			case JOIN:
				return "Join";
		}
		return null;
	}

	/**
	 * Abscract method to get the enum number
	 * 
	 * @return enum number
	 */
	public abstract int getId();

}
