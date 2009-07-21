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
package com.mitester.media;

/**
 * This class consists of constants used for RTP/RTCP automation 
 *
 */

public class MediaConstants {

	/**
	 * To represent SUT RTP PORT
	 */

	public static final String SUT_RTP_PORT = "SUT_RTP_PORT";

	/**
	 * To represent SUT RTCP PORT
	 */

	public static final String SUT_RTCP_PORT = "SUT_RTCP_PORT";

	/**
	 * To represent MITESTER RTP PORT
	 */

	public static final String MITESTER_RTP_PORT = "MITESTER_RTP_PORT";

	/**
	 * To represent MITESTER RTCP PORT
	 */

	public static final String MITESTER_RTCP_PORT = "MITESTER_RTCP_PORT";

	/**
	 * To represent RTP packet
	 */
	public static final String RTP_PACKET = "RTP";

	/**
	 * To represent RTCP packet
	 */
	public static final String RTCP_PACKET = "RTCP";

	/**
	 * To represent RTCP SR packet
	 */
	public static final String RTCP_SR_PACKET = "RTCP_SR";

	/**
	 * To represent RTCP RR packet
	 */
	public static final String RTCP_RR_PACKET = "RTCP_RR";

	/**
	 * To represent RTCP SDES packet
	 */
	public static final String RTCP_SDES_PACKET = "RTCP_SDES";

	/**
	 * To represent RTCP BYE packet
	 */
	public static final String RTCP_BYE_PACKET = "RTCP_BYE";

	/**
	 * To represent RTCP APP packet
	 */
	public static final String RTCP_APP_PACKET = "RTCP_APP";

	/**
	 * To represent constant value to the incoming rtp message
	 */
	public static final String INCOMING_RTP_MESSAGE = "-----------------------------------------< incoming RTP message >------------------------------------------------------"
			+ System.getProperty("line.separator");

	/**
	 * To represent constant value to the outgoing rtp message
	 */
	public static final String OUTGOING_RTP_MESSAGE = "-----------------------------------------< outgoing RTP message >------------------------------------------------------"
			+ System.getProperty("line.separator");

	/**
	 * To represent constant value to the incoming rtcp message
	 */
	public static final String INCOMING_RTCP_MESSAGE = "-----------------------------------------< incoming RTCP message >------------------------------------------------------"
			+ System.getProperty("line.separator");

	/** To represent constant value to the outgoing rtcp message */
	
	public static final String OUTGOING_RTCP_MESSAGE = "-----------------------------------------< outgoing RTCP message >------------------------------------------------------"
			+ System.getProperty("line.separator");

	/**
	 * To represent constant value to the maximum packet length
	 */
	public static final int MAX_MEDIA_PKT_LENGTH = 8192;

	/**
	 * To represent constant value to the maximum buffer size
	 */
	public static final int MAX_MEDIA_PKT_BUFFER_SIZE = 9000;

	/**
	 * To represents the space separator
	 */
	public static final String SPACE_SEPARATOR = " ";

	/**
	 * To represent the RTCP SR constant
	 */
	public static final String RTCP_SR = "c8";

	/**
	 * To represent the RTCP RR constant
	 */
	public static final String RTCP_RR = "c9";

	/**
	 * To represent the RTCP SDES constant
	 */

	public static final String RTCP_SDES = "ca";

	/**
	 * To represent the RTCP BYE constant
	 */
	public static final String RTCP_BYE = "cb";

	/**
	 * To represent the RTCP APP constant
	 */
	public static final String RTCP_APP = "cc";
	
	/**
	 * To represent the txt file extension
	 */
	public static final String FILE_EXTENSE_WITH_TXT = "txt";
	
	/**
	 * To represent the pacp file extension
	 */
	public static final String FILE_EXTENSE_WITH_PACP = "pcap";

}
