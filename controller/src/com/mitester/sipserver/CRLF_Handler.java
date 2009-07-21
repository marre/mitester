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

import java.io.IOException;
import java.text.ParseException;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;

import com.mitester.jaxbparser.server.LINE;
import com.mitester.utility.MiTesterLog;

/**
 * Addition and removal of CRLF
 * 
 * 
 * 
 */
public class CRLF_Handler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(CRLF_Handler.class.getName());
	private static final String NEW_LINE = "\r\n";
	private static final String CRCR = "\r\r";
	private static final String LFLF = "\n\n";
	private static final String LF = "\n";
	private static final String CR = "\r";
	private static final String EMPTY = "";
	private static final String EQUAL = "=";

	/**
	 * convertCRLRIntoCRCRByCount is used to convert the CRLF into any number of
	 * CRCR
	 * 
	 * @param name
	 * @param sipmesg
	 * @param count
	 * @return after converting CRLF into CRCR SIP Mesages
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */
	public static String convertCRLFIntoCRCRByCount(String name,
	        String sipmesg, int count) throws SipException, ParseException,
	        InvalidArgumentException, IOException, NullPointerException,
	        java.lang.IllegalArgumentException {
		LOGGER.info("CRCR is added to the header " + name + " with the Count "
		        + count);
		String outputSIPMEssage = null;
		if (name.length() != 1) {
			outputSIPMEssage = _convertCRLFToHeaderHelper(name, sipmesg, count,
			        CRCR);
		} else {
			outputSIPMEssage = _convertCRLFToContentHelper(name, sipmesg,
			        count, CRCR);
		}
		return outputSIPMEssage;
	}

	/**
	 * convertCRLFIntoLFLFByCount is used to convert the CRLF into any number of
	 * LFLF
	 * 
	 * @param name
	 * @param sipmesg
	 * @param count
	 * @return after conveting CRLF into LFLF SIP Message
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */
	public static String convertCRLFIntoLFLFByCount(String name,
	        String sipmesg, int count) throws SipException, ParseException,
	        InvalidArgumentException, IOException, NullPointerException,
	        java.lang.IllegalArgumentException {
		LOGGER.info("LFLF is added to the header " + name + " with the Count "
		        + count);

		String outputSIPMEssage = null;
		if (name.length() != 1) {
			outputSIPMEssage = _convertCRLFToHeaderHelper(name, sipmesg, count,
			        LFLF);
		} else {
			outputSIPMEssage = _convertCRLFToContentHelper(name, sipmesg,
			        count, LFLF);
		}
		return outputSIPMEssage;
	}

	/**
	 * convertCRLFIntoCRLFByCount is used to convert the CRLF into any number of
	 * CRLF
	 * 
	 * @param name
	 * @param sipmesg
	 * @param count
	 * @return after conveting CRLF into CRLF with any no of count SIP Message
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */
	public static String convertCRLFIntoCRLFByCount(String name,
	        String sipmesg, int count) throws SipException, ParseException,
	        InvalidArgumentException, IOException, NullPointerException,
	        java.lang.IllegalArgumentException {
		LOGGER.info("CRLF is added to the header " + name + " with the Count "
		        + count);

		String outputSIPMEssage = null;
		if (name.length() != 1) {
			outputSIPMEssage = _convertCRLFToHeaderHelper(name, sipmesg, count,
			        NEW_LINE);
		} else {
			outputSIPMEssage = _convertCRLFToContentHelper(name, sipmesg,
			        count, NEW_LINE);
		}
		return outputSIPMEssage;
	}

	/**
	 * converCRLFIntoCR is used to convert CRLF into an CR
	 * 
	 * @param name
	 * @param sipmesg
	 * @return after conveting CRLF into CR SIP Message
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */
	public static String convertCRLFIntoCR(String name, String sipmesg)
	        throws SipException, ParseException, InvalidArgumentException,
	        IOException, NullPointerException,
	        java.lang.IllegalArgumentException {
		LOGGER.info("Removed CR in the '" + name +"' header");
		StringBuilder builder = new StringBuilder();
		SIPHeaders headerName = SIPHeaders.getSipHeaderfromString(name
		        .toUpperCase());
		StringTokenizer token = new StringTokenizer(sipmesg, NEW_LINE);

		if (name.length() != 1) {
			while (token.hasMoreElements()) {
				String nextToken = token.nextToken();
				if (headerName.toString() != null) {
					if (nextToken.startsWith(headerName.toString())) {
						builder.append(nextToken).append(LF);
					} else {
						builder.append(nextToken).append(NEW_LINE);
					}
				} else {
					builder.append(nextToken).append(LF);
				}
			}
		} else {

			while (token.hasMoreElements()) {
				String nextToken = token.nextToken();
				if (name.toString() != null) {
					if (nextToken.startsWith(name + EQUAL)) {
						builder.append(nextToken).append(LF);
					} else {
						builder.append(nextToken).append(NEW_LINE);
					}
				} else {
					builder.append(nextToken).append(LF);
				}
			}
		}
		return builder.toString();
	}

	/**
	 * convertCRLFIntoLF is used to convert CRLF into an LF
	 * 
	 * @param name
	 * @param sipmesg
	 * @return after conveting CRLF into LF SIP Message
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */

	public static String convertCRLFIntoLF(String name, String sipmesg)
	        throws SipException, ParseException, InvalidArgumentException,
	        IOException, NullPointerException,
	        java.lang.IllegalArgumentException {
		LOGGER.info("Removed LF in the '" + name +"' header");
		StringBuilder builder = new StringBuilder();
		SIPHeaders headerName = SIPHeaders.getSipHeaderfromString(name
		        .toUpperCase());
		StringTokenizer token = new StringTokenizer(sipmesg, NEW_LINE);

		if (name.length() != 1) {
			while (token.hasMoreElements()) {
				String nextToken = token.nextToken();
				if (headerName.toString() != null) {
					if (nextToken.startsWith(headerName.toString())) {
						builder.append(nextToken).append(CR);
					} else {
						builder.append(nextToken).append(NEW_LINE);
					}
				} else {
					builder.append(nextToken).append(CR);
				}
			}
		} else {

			while (token.hasMoreElements()) {
				String nextToken = token.nextToken();
				if (name.toString() != null) {
					if (nextToken.startsWith(name + EQUAL)) {
						builder.append(nextToken).append(CR);
					} else {
						builder.append(nextToken).append(NEW_LINE);
					}
				} else {
					builder.append(nextToken).append(CR);
				}
			}
		}
		return builder.toString();
	}

	/**
	 * removeCRLF is used to remove CRLF in any header
	 * 
	 * @param name
	 * @param sipmesg
	 * @return after removing CRLF into an SIP Message
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */

	public static String removeCRLF(String name, String sipmesg)
	        throws SipException, ParseException, InvalidArgumentException,
	        IOException, NullPointerException,
	        java.lang.IllegalArgumentException {
		LOGGER.info("Removed CRLF in the '" + name +"' header");
		StringBuilder builder = new StringBuilder();
		SIPHeaders headerName = SIPHeaders.getSipHeaderfromString(name
		        .toUpperCase());
		StringTokenizer token = new StringTokenizer(sipmesg, NEW_LINE);

		if (name.length() != 1) {
			while (token.hasMoreElements()) {
				String nextToken = token.nextToken();
				if (headerName.toString() != null) {
					if (nextToken.startsWith(headerName.toString())) {
						builder.append(nextToken);
					} else {
						builder.append(nextToken).append(NEW_LINE);
					}
				} else {
					builder.append(nextToken);
				}
			}
		} else {

			while (token.hasMoreElements()) {
				String nextToken = token.nextToken();
				if (name.toString() != null) {
					if (nextToken.startsWith(name + EQUAL)) {
						builder.append(nextToken);
					} else {
						builder.append(nextToken).append(NEW_LINE);
					}
				} else {
					builder.append(nextToken);
				}
			}
		}
		return builder.toString();
	}

	/**
	 * converLineCRLFIntoCRCRByCount is used to convert the first
	 * line(Request-Line or Status-Line) CRLF into CRCR
	 * 
	 * @param name
	 * @param sipmesg
	 * @return after converted the first line(Request-Line or Status-Line) CRLF
	 *         into CRCR
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */

	public static String convertLineCRLFIntoCRCRByCount(LINE line, String msg)
	        throws SipException, ParseException, InvalidArgumentException,
	        IOException, NullPointerException,
	        java.lang.IllegalArgumentException {
		LOGGER.info("Adding CRCR to the First Line");
		StringBuilder Fcrcr = new StringBuilder();
		int CRCRCount = Integer.parseInt(line.getFline().getCount().toString());
		String crcrarray[] = msg.split(NEW_LINE);
		for (int cr = 0; cr < crcrarray.length; cr++) {
			Fcrcr.append(crcrarray[cr]);
			if (cr == 0) {
				for (int crcr1 = 0; crcr1 < CRCRCount; crcr1++) {
					Fcrcr.append(CRCR);
				}
			}
			if (cr != 0)
				Fcrcr.append(NEW_LINE);
		}
		return Fcrcr.toString();
	}

	/**
	 * converLineCRLFIntoCRLFByCount is used to convert the first
	 * line(Request-Line or Status-Line) CRLF into CRLF
	 * 
	 * @param name
	 * @param sipmesg
	 * @return after converted the first line(Request-Line or Status-Line) CRLF
	 *         into CRLF
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */

	public static String convertLineCRLFIntoCRLFByCount(LINE line, String msg)
	        throws SipException, ParseException, InvalidArgumentException,
	        IOException, NullPointerException,
	        java.lang.IllegalArgumentException {
		LOGGER.info("Adding CRLF to the First Line");

		StringBuilder Fcrcr = new StringBuilder();
		int CRCRCount = Integer.parseInt(line.getFline().getCount().toString());

		String crcrarray[] = msg.split(NEW_LINE);
		for (int cr = 0; cr < crcrarray.length; cr++) {
			Fcrcr.append(crcrarray[cr]);
			if (cr == 0) {
				for (int crcr1 = 0; crcr1 < CRCRCount; crcr1++) {
					Fcrcr.append(NEW_LINE);
				}
			}
			if (cr != 0)
				Fcrcr.append(NEW_LINE);
		}
		return Fcrcr.toString();
	}

	/**
	 * converLineCRLFIntoLFLFByCount is used to convert the first
	 * line(Request-Line or Status-Line) CRLF into LFLF
	 * 
	 * @param name
	 * @param sipmesg
	 * @return after converted the first line(Request-Line or Status-Line) CRLF
	 *         into LFLF
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */

	public static String convertLineCRLFIntoLFLFByCount(LINE line, String msg)
	        throws SipException, ParseException, InvalidArgumentException,
	        IOException, NullPointerException,
	        java.lang.IllegalArgumentException {
		LOGGER.info("Adding LFLF to the First Line");

		StringBuilder Fcrcr = new StringBuilder();

		int CRCRCount = Integer.parseInt(line.getFline().getCount().toString());
		String crcrarray[] = msg.split(NEW_LINE);
		for (int cr = 0; cr < crcrarray.length; cr++) {
			Fcrcr.append(crcrarray[cr]);
			if (cr == 0) {
				for (int crcr1 = 0; crcr1 < CRCRCount; crcr1++)
					Fcrcr.append(LFLF);
			}
			if (cr != 0)
				Fcrcr.append(NEW_LINE);
		}
		return Fcrcr.toString();
	}

	/**
	 * convertLineCRLFIntoCR is used to convert the first line(Request-Line or
	 * Status-Line) CRLF into CR
	 * 
	 * @param name
	 * @param sipmesg
	 * @return after converted the first line(Request-Line or Status-Line) CRLF
	 *         into CR
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */

	public static String convertLineCRLFIntoCR(LINE line, String msg)
	        throws SipException, ParseException, InvalidArgumentException,
	        IOException, NullPointerException,
	        java.lang.IllegalArgumentException {
		LOGGER.info("Removing LF from the First Line");
		StringBuilder Fcrcr = new StringBuilder();
		String crcrarray[] = msg.split(NEW_LINE);

		for (int cr = 0; cr < crcrarray.length; cr++) {
			Fcrcr.append(crcrarray[cr]);
			if (cr == 0)
				Fcrcr.append(LF);

			if (cr != 0)
				Fcrcr.append(NEW_LINE);
		}
		return Fcrcr.toString();
	}

	/**
	 * convertLineCRLFIntoLF is used to convert the first line(Request-Line or
	 * Status-Line) CRLF into LF
	 * 
	 * @param name
	 * @param sipmesg
	 * @return after converted the first line(Request-Line or Status-Line) CRLF
	 *         into LF
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */

	public static String convertLineCRLFIntoLF(String msg) throws SipException,
	        ParseException, InvalidArgumentException, IOException,
	        NullPointerException, java.lang.IllegalArgumentException {
		LOGGER.info("Removing CR from the First Line");
		StringBuilder Fcrcr = new StringBuilder();
		String crcrarray[] = msg.split(NEW_LINE);

		for (int cr = 0; cr < crcrarray.length; cr++) {
			Fcrcr.append(crcrarray[cr]);
			if (cr == 0)
				Fcrcr.append(CR);

			if (cr != 0)
				Fcrcr.append(NEW_LINE);
		}
		return Fcrcr.toString();
	}

	/**
	 * removeLineCRLF is used to remove the first line(Request-Line or
	 * Status-Line) CRLF
	 * 
	 * @param name
	 * @param sipmesg
	 * @return after removed the first line(Request-Line or Status-Line) CRLF
	 *         SIP Message
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws java.lang.IllegalArgumentException
	 */

	public static String removeLineCRLF(String msg) throws SipException,
	        ParseException, InvalidArgumentException, IOException,
	        NullPointerException, java.lang.IllegalArgumentException {
		LOGGER.info("Removing CRLF from the First Line");
		StringBuilder Fcrcr = new StringBuilder();

		String crcrarray[] = msg.split(NEW_LINE);
		for (int cr = 0; cr < crcrarray.length; cr++) {
			Fcrcr.append(crcrarray[cr]);
			if (cr == 0)
				Fcrcr.append(EMPTY);

			if (cr != 0)
				Fcrcr.append(NEW_LINE);
		}
		return Fcrcr.toString();
	}

	/**
	 * This is helper method to convert the CRLF into CRCR or LFLF or CRLF or CR
	 * or LR or without CRLF
	 * 
	 * @param name
	 * @param sipmesg
	 * @param count
	 * @param modifyCRLF
	 * @return
	 */
	public static String _convertCRLFToHeaderHelper(String name,
	        String sipmesg, int count, String modifyCRLF) {
		StringBuilder builder = new StringBuilder();
		StringTokenizer token = new StringTokenizer(sipmesg, NEW_LINE);
		SIPHeaders headerName = SIPHeaders.getSipHeaderfromString(name
		        .toUpperCase());
		while (token.hasMoreElements()) {
			String nextToken = token.nextToken();
			if (headerName.toString() != null) {
				if (nextToken.startsWith(headerName.toString())) {
					builder.append(nextToken);
					for (int n = 0; n < count; n++)
						builder.append(modifyCRLF);
				} else {
					builder.append(nextToken).append(NEW_LINE);
				}
			} else {
				builder.append(nextToken);
				for (int n = 0; n < count; n++)
					builder.append(modifyCRLF);
			}
		}
		return builder.toString();
	}

	/**
	 * This is helper method to convert CRLF to the contents
	 * 
	 * @param name
	 * @param sipmesg
	 * @param count
	 * @param modifyCRLF
	 * @return
	 */
	public static String _convertCRLFToContentHelper(String name,
	        String sipmesg, int count, String modifyCRLF) {

		StringBuilder builder = new StringBuilder();
		StringTokenizer token = new StringTokenizer(sipmesg, NEW_LINE);
		while (token.hasMoreElements()) {
			String nextToken = token.nextToken();
			if (name.toString() != null) {
				if (nextToken.startsWith(name + EQUAL)) {
					builder.append(nextToken);
					for (int n = 0; n < count; n++)
						builder.append(modifyCRLF);
				} else {
					builder.append(nextToken).append(NEW_LINE);
				}
			} else {
				builder.append(nextToken);
				for (int n = 0; n < count; n++)
					builder.append(modifyCRLF);
			}
		}
		return builder.toString();
	}
}
