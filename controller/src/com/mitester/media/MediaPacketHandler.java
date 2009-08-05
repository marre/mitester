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

import static com.mitester.media.MediaConstants.MAX_MEDIA_PKT_LENGTH;
import static com.mitester.media.MediaConstants.RTCP_APP;
import static com.mitester.media.MediaConstants.RTCP_APP_PACKET;
import static com.mitester.media.MediaConstants.RTCP_BYE;
import static com.mitester.media.MediaConstants.RTCP_BYE_PACKET;
import static com.mitester.media.MediaConstants.RTCP_RR;
import static com.mitester.media.MediaConstants.RTCP_RR_PACKET;
import static com.mitester.media.MediaConstants.RTCP_SDES;
import static com.mitester.media.MediaConstants.RTCP_SDES_PACKET;
import static com.mitester.media.MediaConstants.RTCP_SR;
import static com.mitester.media.MediaConstants.RTCP_SR_PACKET;
import static com.mitester.media.MediaConstants.SPACE_SEPARATOR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

import org.apache.log4j.Logger;

import com.mitester.utility.MiTesterLog;
import com.voytechs.jnetstream.codec.Decoder;
import com.voytechs.jnetstream.io.StreamFormatException;
import com.voytechs.jnetstream.npl.SyntaxError;

/**
 * This class used to parse and form the media packets
 * 
 */
public class MediaPacketHandler {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(MediaPacketHandler.class.getName());

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * This method is used to encode the messages read from text file
	 * 
	 * @param mediaMessage
	 *            is string message read from the txt file
	 * @return byte array of encoded message
	 */

	public static byte[] encodeMessage(String mediaMessage) {

		LOGGER.info("encoding media started...");

		char tempBuf[] = new char[MAX_MEDIA_PKT_LENGTH];

		char temp[] = new char[2];

		char msg[] = mediaMessage.toCharArray();

		int j = 0;

		// fill with null chars
		Arrays.fill(tempBuf, '\0');

		for (int i = 0; i < msg.length; i++) {

//			if ((msg[i] == '\r') || (msg[i] == '\n') || (msg[i] == ' ')) {
			if(Character.isDigit(msg[i])) {
				tempBuf[j] = msg[i];
				j++;
			} else {
				continue;
			}
		}

		int len = j;

		byte encodedMsg[] = new byte[(len / 2)];

		j = 0;

		for (int i = 0; i < len; i++) {

			// fill with null chars
			Arrays.fill(temp, '\0');

			temp[0] = tempBuf[i];
			i++;

			if (i >= len)
				break;

			temp[1] = tempBuf[i];

			String chunck = new String(temp);

			int dec = (int) Integer.parseInt(chunck, 16);

			encodedMsg[j] = new Integer(dec & 0xFF).byteValue();

			j++;
		}

		LOGGER.info("encoding media completed");

		return encodedMsg;

	}

	/**
	 * this method is used to decode the data gram received from the network
	 * 
	 * @param bytes
	 *            received from network
	 * @param packetLength
	 *            length of the bytes received from the network
	 * @return string message after decoding the media packet
	 */

	public static String decodeMessage(byte[] bytes, int packetLength) {

		LOGGER.info("decoding media packets started...");

		String packetMessage = null;

		if (packetLength > 0) {
			StringBuilder temp = new StringBuilder();

			int count = 0;

			for (int i = 0; i < packetLength; i++) {

				Formatter fmt = new Formatter();
				fmt.format("%02x", (new Byte(bytes[i]).intValue() & 0xFF));
				temp.append(fmt.toString());
				temp.append(SPACE_SEPARATOR);
				count++;

				if (count == 32) {
					temp.append(LINE_SEPARATOR);
					count = 0;
				}

			}
			temp.append(LINE_SEPARATOR);

			packetMessage = temp.toString();

		}

		LOGGER.info("decoding media packets completed");

		return packetMessage;

	}

	/**
	 * return the RTCP packet name from packet type
	 * 
	 * @param packetType
	 *            indicates sub type of RTCP packet
	 * @return RTCP packet name
	 */

	public static String getRTCPPacketName(String packetType) {

		if (packetType.equals(RTCP_SR)) {
			return RTCP_SR_PACKET;

		} else if (packetType.equals(RTCP_RR)) {
			return RTCP_RR_PACKET;

		} else if (packetType.equals(RTCP_SDES)) {
			return RTCP_SDES_PACKET;

		} else if (packetType.equals(RTCP_BYE)) {
			return RTCP_BYE_PACKET;
		} else if (packetType.equals(RTCP_APP)) {
			return RTCP_APP_PACKET;
		} else {
			return null;
		}

	}

	/**
	 * This method is used to check the RTCP packet type
	 * 
	 * @param packetName
	 *            specifies the sub type of the RTCP packet
	 * @return RTCP packet sub type
	 */

	public static String getRTCPtype(String subType) {

		LOGGER.info("called getRTCPtype ");

		if (subType.equals(RTCP_SR_PACKET)) {
			return RTCP_SR;

		} else if (subType.equals(RTCP_RR_PACKET)) {
			return RTCP_RR;

		} else if (subType.equals(RTCP_SDES_PACKET)) {
			return RTCP_SDES;

		} else if (subType.equals(RTCP_BYE_PACKET)) {
			return RTCP_BYE;
		} else if (subType.equals(RTCP_APP_PACKET)) {
			return RTCP_APP;
		} else {
			return null;
		}
	}

	/**
	 * This method is used to read the data of pacp file
	 * 
	 * @param filePath
	 *            name of the file path
	 * @return list of object which consists of data read from the pcap file
	 * @throws MediaException
	 */

	public static List<Object> readPcapFile(String filePath)
			throws MediaException {

		LOGGER.info("called readPcapFile ");

		List<Object> dataObject = null;

		try {

			dataObject = new ArrayList<Object>();

			Decoder decoder = new Decoder(filePath);

			com.voytechs.jnetstream.codec.Packet packet = null;

			while ((packet = decoder.nextPacket()) != null) {

				dataObject.add(packet.getDataHeader().getValue(0));
			}

			LOGGER.info("successfully read the pacap file ");

		} catch (SyntaxError ex) {

			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.media.MediaException(
					"Error while reading media packet from " + filePath
							+ " file");

		} catch (StreamFormatException ex) {

			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.media.MediaException(
					"Error while reading media packet from " + filePath
							+ " file");

		} catch (IOException ex) {

			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.media.MediaException(
					"Error while reading media packet from " + filePath
							+ " file");

		} catch (Exception ex) {

			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.media.MediaException(
					"Error while reading media packet from " + filePath
							+ " file");

		}

		return dataObject;

	}

	/**
	 * This method is used to read the content from the Text file
	 * 
	 * @param path
	 *            name of the file path
	 * @return string object consists of contents of the text file
	 * @throws MediaException
	 */

	public static String readTxtFile(String path) throws MediaException {

		LOGGER.info("called readTxtFile");
		try {

			BufferedReader buf = new BufferedReader(new FileReader(path));

			StringBuilder sb = new StringBuilder();

			String str = null;

			while ((str = buf.readLine()) != null) {
				sb.append(str);
				sb.append(LINE_SEPARATOR);
			}

			return sb.toString();

		} catch (IOException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.media.MediaException(
					"Error while reading media packet from " + path + " file");
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.media.MediaException(
					"Error while reading media packet from " + path + " file");
		}

	}

}
