/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SipServerConstants.java
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
package com.mitester.sipserver;

/**
 * SIP Server Constants are used sipServer
 * 
 * 
 * 
 */
public class SipServerConstants {
	/** To represent the INVITE method */
	public static final String INVITE_METHOD = "INVITE";

	/** To represent the ACK method */
	public static final String ACK_METHOD = "ACK";

	/** To represent the OPTIONS method */
	public static final String OPTIONS_METHOD = "OPTIONS";

	/** To represent the CANCEL method */
	public static final String CANCEL_METHOD = "CANCEL";

	/** To represent the BYE method */
	public static final String BYE_METHOD = "BYE";

	/** To represent the PRACK method */
	public static final String PRACK_METHOD = "PRACK";

	/** To represent the PUBLISH method */
	public static final String PUBLISH_METHOD = "PUBLISH";

	/** To represent the INFO method */
	public static final String INFO_METHOD = "INFO";

	/** To represent the SUBSCRIBE method */
	public static final String SUBSCRIBE_METHOD = "SUBSCRIBE";

	/** To represent the NOTIFY method */
	public static final String NOTIFY_METHOD = "NOTIFY";

	/** To represent the REFER method */
	public static final String REFER_METHOD = "REFER";

	/** To represent the REGISTER method */
	public static final String REGISTER_METHOD = "REGISTER";

	/** To represent the MESSAGE method */
	public static final String MESSAGE_METHOD = "MESSAGE";

	/** To represent the UPDATE method */
	public static final String UPDATE_METHOD = "UPDATE";

	/** To represent the SIP Version method */
	public static final String SIPVERSION = "SIP/2.0";

	/** To represents the delimiter used in parsing the Server action */
	public static final String SERVER_ACTION_MSG_SEPARATOR = "-ACTION-";

	/** To represents the delimiter used in parsing the Server action parameters */
	public static final String SERVER_ACTION_PARAM_SEPARATOR = "-PARAM-";

	/**
	 * To represents the delimiter used in parsing the Server action name and
	 * value
	 */
	public static final String SERVER_ACTION_VALUE_SEPARATOR = "-AND-";

	/**
	 * To represents the delimiter used in parsing the Server action ADD Message
	 * TAG
	 */
	public static final String SERVER_ACTION_ADDMSG_SEPARATOR = "ADDMSG";

	/**
	 * To represents the delimiter used in parsing the Server action DEL Message
	 * TAg
	 */
	public static final String SERVER_ACTION_DELMSG_SEPARATOR = "DELMSG";

	/**
	 * To represents the delimiter used in parsing the Server action Empty
	 * Message Tag
	 */
	public static final String SERVER_ACTION_EMPTYMSG_SEPARATOR = "EMPTYMSG";

	/**
	 * To represents the delimiter used in parsing the Server action ADDCRCR TAG
	 */
	public static final String SERVER_ACTION_ADDCRCR_SEPARATOR = "ADDCRCR";

	/**
	 * To represents the delimiter used in parsing the Server action ADDLFLF TAG
	 */
	public static final String SERVER_ACTION_ADDLFLF_SEPARATOR = "ADDLFLF";

	/**
	 * To represents the delimiter used in parsing the Server action ADDCRLF TAG
	 */
	public static final String SERVER_ACTION_ADDCRLF_SEPARATOR = "ADDCRLF";

	/** To represents the delimiter used in parsing the Server action DELCR TAG */
	public static final String SERVER_ACTION_DELCR_SEPARATOR = "DELCR";

	/** To represents the delimiter used in parsing the Server action DEL LF TAG */
	public static final String SERVER_ACTION_DELLF_SEPARATOR = "DELLF";

	/**
	 * To represents the delimiter used in parsing the Server action DELCRLF TAG
	 */
	public static final String SERVER_ACTION_DELCRLF_SEPARATOR = "DELCRLF";

	/**
	 * To represents the delimiter used in parsing the Server action DUPMessage
	 * TAG
	 */
	public static final String SERVER_ACTION_DUPMSG_SEPARATOR = "DUPMSG";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * in Header TAG
	 */
	public static final String SERVER_ACTION_HEADERNAME_SEPARATOR = "HeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * in ADDCRCR TAG
	 */
	public static final String SERVER_ACTION_CRCRHEADERNAME_SEPARATOR = "CRCRHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * in ADDLFLF TAG
	 */
	public static final String SERVER_ACTION_LFLFHEADERNAME_SEPARATOR = "LFLFHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * in ADDCRLF TAG
	 */
	public static final String SERVER_ACTION_CRLFHEADERNAME_SEPARATOR = "CRLFHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * in DELCR TAG
	 */
	public static final String SERVER_ACTION_DELCRHEADERNAME_SEPARATOR = "DELCRHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * in DELLF TAG
	 */
	public static final String SERVER_ACTION_DELLFHEADERNAME_SEPARATOR = "DELLFHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * in DELCRLF TAG
	 */
	public static final String SERVER_ACTION_DELCRLFHEADERNAME_SEPARATOR = "DELCRLFHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * in CONTENT TAG
	 */
	public static final String SERVER_ACTION_SDPBODYHEADERNAME_SEPARATOR = "SDPBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDCRCR TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_CRCRSDPBODYHEADERNAME_SEPARATOR = "CRCRSDPBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDLFLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_LFLFSDPBODYHEADERNAME_SEPARATOR = "LFLFSDPBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDCRLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_CRLFSDPBODYHEADERNAME_SEPARATOR = "CRLFSDPBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELCR TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELCRSDPBODYHEADERNAME_SEPARATOR = "DELCRSDPBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELLFSDPBODYHEADERNAME_SEPARATOR = "DELLFSDPBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELCRLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELCRLFSDPBODYHEADERNAME_SEPARATOR = "DELCRLFSDPBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action source file
	 * name in the xml COntent TAG
	 */
	public static final String SERVER_ACTION_XMLBODYHEADERNAME_SEPARATOR = "XMLBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDCRCR TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_CRCRXMLBODYHEADERNAME_SEPARATOR = "CRCRXMLBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDLFLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_LFLFXMLBODYHEADERNAME_SEPARATOR = "LFLFXMLBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDCRLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_CRLFXMLBODYHEADERNAME_SEPARATOR = "CRLFXMLBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELCR TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELCRXMLBODYHEADERNAME_SEPARATOR = "DELCRXMLBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELLFXMLBODYHEADERNAME_SEPARATOR = "DELLFXMLBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELCRLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELCRLFXMLBODYHEADERNAME_SEPARATOR = "DELCRLFXMLBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action source file
	 * name in the text COntent TAG
	 */
	public static final String SERVER_ACTION_TXTBODYHEADERNAME_SEPARATOR = "TXTBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDCRCR TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_CRCRTXTBODYHEADERNAME_SEPARATOR = "CRCRTXTBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDLFLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_LFLFTXTBODYHEADERNAME_SEPARATOR = "LFLFTXTBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDCRLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_CRLFTXTBODYHEADERNAME_SEPARATOR = "CRLFTXTBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELCR TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELCRTXTBODYHEADERNAME_SEPARATOR = "DELCRTXTBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELLFTXTBODYHEADERNAME_SEPARATOR = "DELLFTXTBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELCRLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELCRLFTXTBODYHEADERNAME_SEPARATOR = "DELCRLFTXTBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action source file
	 * name in the other COntent TAG
	 */
	public static final String SERVER_ACTION_OTHERSBODYHEADERNAME_SEPARATOR = "OTHERSBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDCRCR TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_CRCROTHERSBODYHEADERNAME_SEPARATOR = "CRCROTHERSBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDLFLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_LFLFOTHERSBODYHEADERNAME_SEPARATOR = "LFLFOTHERSBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDCRLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_CRLFOTHERSBODYHEADERNAME_SEPARATOR = "CRLFOTHERSBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELCR TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELCROTHERSBODYHEADERNAME_SEPARATOR = "DELCROTHERSBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELLFOTHERSBODYHEADERNAME_SEPARATOR = "DELLFOTHERSBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELCRLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELCRLFOTHERSBODYHEADERNAME_SEPARATOR = "DELCRLFOTHERSBodyHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header
	 * value inside header tag
	 */
	public static final String SERVER_ACTION_VALUENAME_SEPARATOR = "Value";

	/**
	 * To represents the delimiter used in parsing the Server action SIPMessage
	 * count inside action tag
	 */
	public static final String SERVER_ACTION_COUNT_SEPARATOR = "Count";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * inside header tag
	 */
	public static final String SERVER_ACTION_HEADER_SEPARATOR = "Header";

	/**
	 * To represents the delimiter used in parsing the Server action Header
	 * parameter name inside param tag
	 */
	public static final String SERVER_ACTION_PARAMANAME_SEPARATOR = "Param";

	/**
	 * To represents the delimiter used in parsing the Server action SDP content
	 * inside a SDP body TAG
	 */
	public static final String SERVER_ACTION_SDPBODYPARAMANAME_SEPARATOR = "SDPBodyParam";

	/**
	 * To represents the delimiter used in parsing the Server action XML content
	 * inside a SDP body TAG
	 */
	public static final String SERVER_ACTION_XMLBODYPARAMANAME_SEPARATOR = "XMLBodyParam";

	/**
	 * To represents the delimiter used in parsing the Server action TEXT
	 * content inside a SDP body TAG
	 */
	public static final String SERVER_ACTION_TXTBODYPARAMANAME_SEPARATOR = "TXTBodyParam";

	/**
	 * To represents the delimiter used in parsing the Server action OTHERS
	 * content inside a SDP body TAG
	 */
	public static final String SERVER_ACTION_OTHERSBODYPARAMANAME_SEPARATOR = "OTHERSBodyParam";

	/**
	 * To represents the delimiter used in parsing the Server action for the
	 * status line
	 */
	public static final String SERVER_ACTION_STATUSLINE_SEPARATOR = "StatusLine";

	/**
	 * To represents the delimiter used in parsing the Server action for the
	 * status line
	 */
	public static final String SERVER_ACTION_STATUS_LINE_SEPARATOR = "status-line";

	/**
	 * To represents the delimiter used in parsing the Server action for the
	 * request line
	 */
	public static final String SERVER_ACTION_REQUESTLINE_SEPARATOR = "ReqLine";

	/**
	 * To represents the delimiter used in parsing the Server action for the
	 * request line
	 */
	public static final String SERVER_ACTION_REQUEST_LINE_SEPARATOR = "req-line";

	/**
	 * To represents the delimiter used in parsing the Server action for the
	 * first line in the LINE TAG
	 */
	public static final String SERVER_ACTION_FIRSTLINE_SEPARATOR = "fLine";

	/**
	 * To represents the delimiter used in parsing the Server action ADDCRCR to
	 * the first line
	 */
	public static final String SERVER_ACTION_CRCRFIRSTLINE_SEPARATOR = "CRCRfLine";

	/**
	 * To represents the delimiter used in parsing the Server action ADDLFLF to
	 * the first line
	 */
	public static final String SERVER_ACTION_LFLFFIRSTLINE_SEPARATOR = "LFLFfLine";

	/**
	 * To represents the delimiter used in parsing the Server action ADDCRLF to
	 * the first line
	 */
	public static final String SERVER_ACTION_CRLFFIRSTLINE_SEPARATOR = "CRLFfLine";

	/**
	 * To represents the delimiter used in parsing the Server action DELCR to
	 * the first line
	 */
	public static final String SERVER_ACTION_DELCRFIRSTLINE_SEPARATOR = "DELCRfLine";

	/**
	 * To represents the delimiter used in parsing the Server action DELLF to
	 * the first line
	 */
	public static final String SERVER_ACTION_DELLFFIRSTLINE_SEPARATOR = "DELLFfLine";

	/**
	 * To represents the delimiter used in parsing the Server action DELCRLF to
	 * the first line
	 */
	public static final String SERVER_ACTION_DELCRLFFIRSTLINE_SEPARATOR = "DELCRLFfLine";

	/**
	 * To represents the delimiter used in parsing the Server action ADDHEADER
	 * to the TREEMAP
	 */
	public static final String SERVER_ACTION_ADDHEADER_SEPARATOR = "ADDHEADER";

	/**
	 * To represents the delimiter used in parsing the Server action DUPHEADER
	 * to the TREEMAP
	 */
	public static final String SERVER_ACTION_DUPHEADER_SEPARATOR = "DUPHEADER";

	/**
	 * To represent the delimitor for the text file source inside a TXT_CONTENT
	 * TAG
	 */
	public static final String SERVER_ACTION_TEXTFILE_SEPARATOR = "TXTFile";

	/**
	 * To represent the delimitor for the xml file source inside a XML_CONTENT
	 * TAG
	 */
	public static final String SERVER_ACTION_XMLFILE_SEPARATOR = "XMLFile";

	/**
	 * To represent the delimitor for the others file source inside a
	 * OTHERS_CONTENT TAG
	 */
	public static final String SERVER_ACTION_OTHERSFILE_SEPARATOR = "OTHERSFile";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * inside Content tag
	 */
	public static final String SERVER_ACTION_SDPHEADERNAME_SEPARATOR = "SDPHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDCRCR TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_CRCRSDPHEADERNAME_SEPARATOR = "CRCRSDPHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDLFLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_LFLFSDPHEADERNAME_SEPARATOR = "LFLFSDPHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * ADDCRLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_CRLFSDPHEADERNAME_SEPARATOR = "CRLFSDPHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELCR TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELCRSDPHEADERNAME_SEPARATOR = "DELCRSDPHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELLFSDPHEADERNAME_SEPARATOR = "DELLFSDPHeaderName";

	/**
	 * To represents the delimiter used in parsing the Server action Header name
	 * DELCRLF TAG and inside Content tag
	 */
	public static final String SERVER_ACTION_DELCRLFSDPHEADERNAME_SEPARATOR = "DELCRLFSDPHeaderName";

	/** To represents the contants value to the EQUAL symbol */
	public static final String SERVER_ACTION_EQUAL_SEPARATOR = "=";

	/** To represents the contants value to the new Line symbol */
	public static final String SERVER_ACTION_NEWLINE_SEPARATOR = "\r\n";

	/** To represents the contants value to the /> symbol */
	public static final String SERVER_ACTION_ANGLE_SEPARATOR = "/>";

	/** To represents the contants value to the UNDERSCRORE symbol */
	public static final String SERVER_ACTION_UNDERSCORE_SEPARATOR = "_";

	/** To represents the contants value to the REQUEST */
	public static final String SERVER_REQUEST = "REQUEST";

	/** To represents the contants value to the RESPONSE symbol */
	public static final String SERVER_RESPONSE = "RESPONSE";

	/** To represents the contants value to the COLON symbol */
	public static final String SERVER_ACTION_COLON_SEPARATOR = ":";

	/**
	 * To represents the delimiter used in parsing the Server action name and
	 * value
	 */
	public static final String SERVER_ACTION_OR_SEPARATOR = "-OR-";

	/** To represent constant value to the incoming sip message */
	public static final String INCOMING_MSG = "incoming SIP message";

	/** To represent constant value to the outgoing sip message */
	public static final String OUTGOING_MSG = "outgoing SIP message";

	/** To represent constant value to the incoming sip message */
	public static final String INCOMING_SIP_MESSAGE = "-----------------------------------------< incoming SIP message >------------------------------------------------------"
	        + System.getProperty("line.separator");

	/** To represent constant value to the outgoing sip message */
	public static final String OUTGOING_SIP_MESSAGE = "-----------------------------------------< outgoing SIP message >------------------------------------------------------"
	        + System.getProperty("line.separator");

	/** To represent constant value to the line separator */
	public static final String LINE_SEPARATOR = System
	        .getProperty("line.separator");

	/** To represent constant value to the maximum packet length */
	public static final int MAX_PACKET_LENGTH = 8192;

	/** To represent constant value to the SERVER LISTENING PORT */
	public static final String SERVER_LISTEN_PORT = "SERVER_LISTEN_PORT";

	/** To represent constant value to the CLIENT LISTENING PORT */
	public static final String CLIENT_LISTEN_PORT = "CLIENT_LISTEN_PORT";

	/** To represent constant value to the CLIENT IP ADDRESS */
	public static final String CLIENT_IP_ADDRESS = "CLIENT_IP_ADDRESS";

	/** To represent constant value to the client port */
	public static final int DEFAULT_CLIENT_PORT = 5060;

	/** To represent constant value to the server port */
	public static final int DEFAULT_SERVER_PORT = 5070;

	/** To represent constant value to the maximum buffer length */
	public static final int MAX_BUFFER_LENGTH = 10000;

	/** To represent constant value to the maximum packet size */
	public static final int MAX_PACKET_SIZE = 9000;

	/** To represent constant value to the local host */
	public static final String LOCAL_HOST = "localhost";
}
