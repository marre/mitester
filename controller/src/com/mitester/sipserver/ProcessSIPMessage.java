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

import static com.mitester.sipserver.SipServerConstants.CHECK_PRESENCE_OF_HEADER;
import static com.mitester.sipserver.SipServerConstants.SIPVERSION;
import static com.mitester.sipserver.SipServerConstants.VALIDATION;
import static com.mitester.sipserver.SipServerConstants.LINE_SEPARATOR;
import static com.mitester.sipserver.SipServerConstants.YES;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;
import gov.nist.javax.sip.message.SIPMessage;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.sip.SipException;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.mitester.jaxbparser.validation.Header;
import com.mitester.jaxbparser.validation.MANDATORY;
import com.mitester.jaxbparser.validation.Method;
import com.mitester.jaxbparser.validation.OPTIONAL;
import com.mitester.sipserver.headervalidation.CheckHeaderPresence;
import com.mitester.sipserver.headervalidation.InvalidValuesHeaderException;
import com.mitester.sipserver.headervalidation.MandatoryHeaderValidation;
import com.mitester.sipserver.headervalidation.MissingMandatoryHeaderException;
import com.mitester.sipserver.headervalidation.OptionalHeaderValidation;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;

/**
 * This class process the all incoming and outgoing SIP messages
 * 
 * 
 * 
 */
public class ProcessSIPMessage {
	private static final Logger LOGGER = MiTesterLog
			.getLogger(ProcessSIPMessage.class.getName());
	private static List<SIPMessage> sipMessages = new ArrayList<SIPMessage>();
	private static SIPMessage sipMessage = null;
	private static com.mitester.jaxbparser.validation.VALIDATION validation = null;

	/**
	 * returns the SIPMessage
	 * 
	 * @param MethodName
	 *            name of SIP message
	 * @param Method
	 *            name of SIP message
	 * @param Type
	 *            type of SIP message (req or res)
	 * @return
	 */
	public static SIPMessage getSIPMessage(String MethodName, String Method,
			String Type) {
		if (Type.equals(SipServerConstants.SERVER_REQUEST)) {
			if (Method != SipServerConstants.ACK_METHOD) {
				for (int i = 0; i < sipMessages.size(); i++) {
					SIPMessage s = (SIPMessage) (sipMessages.get(i)).clone();
					if (s.getCSeq().getMethod().equals(MethodName)) {
						sipMessage = s;
						break;
					}
				}
			} else {
				for (int i = sipMessages.size() - 1; i > 0; i--) {
					SIPMessage s = (SIPMessage) sipMessages.get(i).clone();
					if (s.getCSeq().getMethod().equals(MethodName)) {
						sipMessage = s;
						break;
					}
				}
			}
		} else {
			for (int i = sipMessages.size() - 1; i >= 0; i--) {
				SIPMessage s = (SIPMessage) sipMessages.get(i).clone();
				if (s.getCSeq().getMethod().equals(MethodName)) {
					sipMessage = s;
					break;
				}
			}
		}
		return sipMessage;
	}

	/**
	 * It process the SIP message
	 * 
	 * @param sipMsgBuf
	 *            SIP message
	 * @param type
	 *            type of SIPMessage(incoming or outgoing)
	 * @return SIPMessage is an object consists of parsed details of SIP message
	 * @throws ParseException
	 * @throws SipException
	 * @throws NullPointerException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws MissingMandatoryHeaderException
	 * @throws InvalidValuesHeaderException
	 */

	public static SIPMessage processSIPMessage(String sipMsgBuf, String type)
			throws ParseException, SipException, NullPointerException,
			JAXBException, IOException, MissingMandatoryHeaderException,
			InvalidValuesHeaderException {

		SIPMessage parsedSipMsg = null;
		parsedSipMsg = SipParser.parseSipMessage(sipMsgBuf);

		if ((sipMessages.size() > 0)
				&& (type.equals(SipServerConstants.INCOMING_MSG))) {

			for (int i = 0; i < sipMessages.size(); i++) {
				SIPMessage getSipMsg = sipMessages.get(i);

				if ((getSipMsg.getFirstLine().equals(parsedSipMsg
						.getFirstLine()))
						&& (getSipMsg.getCSeq().equals(parsedSipMsg.getCSeq()))
						&& (getSipMsg.getCallId().equals(parsedSipMsg
								.getCallId()))

						&& (getSipMsg.getFrom().equals(parsedSipMsg.getFrom()))
						&& (getSipMsg.getTo().equals(parsedSipMsg.getTo()))
						&& (getSipMsg.getTopmostVia().getBranch()
								.equals(parsedSipMsg.getTopmostVia()
										.getBranch()))) {
					TestUtility
							.printMessage("Retransmitted SIP message received, hence dropped"
									+ LINE_SEPARATOR);
					LOGGER
							.info("Retransmitted SIP message received, hence dropped");
					return null;
				}
			}

			/* add received sip message */

			sipMessages.add(parsedSipMsg);

		} else {
			sipMessages.add(parsedSipMsg);
		}

		return parsedSipMsg;
	}

	/**
	 * return the SIPMessage
	 * 
	 * @return sipMessage
	 */
	public static SIPMessage getSipMessage() {
		return sipMessage;
	}

	/**
	 * remove the unwanted int the SIPMessage List
	 */
	public static void removeSipMessageFromList() {

		int removeIndex = sipMessages.size() - 1;
		sipMessages.remove(removeIndex);
	}

	/**
	 * clean-up the SIPMessage List for every Test execution
	 */
	public static void cleanUpSipMessageList() {

		sipMessages = null;
		SendRequestHandler.setIsOriginator();
		sipMessages = new ArrayList<SIPMessage>();
		AddContentToSIPMessage.setIsMisspelt();
	}

	/**
	 * set the validation
	 */

	public static void setValidation(
			com.mitester.jaxbparser.validation.VALIDATION validationObj) {
		validation = validationObj;

	}

	/**
	 * validate incoming SIP message
	 * 
	 * @param sipMessage
	 *            SIPMessage object holds parsed informations of incoming SIP
	 *            message
	 * @param actionValue
	 *            specifies the name of the expected SIP message
	 * @throws MissingMandatoryHeaderException
	 * @throws InvalidValuesHeaderException
	 * @throws SipServerException
	 */

	public static void validateSIPMessage(SIPMessage sipMessage,
			String actionValue) throws MissingMandatoryHeaderException,
			InvalidValuesHeaderException, SipServerException {

		try {
			if ((CONFIG_INSTANCE.isKeyExists(CHECK_PRESENCE_OF_HEADER))
					&& (CONFIG_INSTANCE.getValue(CHECK_PRESENCE_OF_HEADER)
							.equals(YES))) {

				List<MANDATORY> mandatoryHdrs = validation.getMANDATORY();
				List<OPTIONAL> optionalHdrs = validation.getOPTIONAL();

				String line = sipMessage.getFirstLine();

				boolean isChecked = false;

				if (line.startsWith("SIP/2.0")) {

					for (MANDATORY mandatoryHdr : mandatoryHdrs) {
						List<Method> method = mandatoryHdr.getMethod();

						for (Method mthd : method) {

							String value = mthd.getValue();

							if (mthd.getName().value().equals("res")
									&& value.equals(actionValue)) {

								List<Header> header = mthd.getHeader();
								CheckHeaderPresence.checkHeaderPresence(
										sipMessage, header);
								isChecked = true;
								break;

							}
						}

						if (isChecked)
							break;

					}

					isChecked = false;

					for (OPTIONAL optionalHdr : optionalHdrs) {
						List<Method> method = optionalHdr.getMethod();
						for (Method mthd : method) {
							String value = mthd.getValue();

							if (mthd.getName().value().equals("res")
									&& value.equals(actionValue)) {

								List<Header> header = mthd.getHeader();
								CheckHeaderPresence.checkHeaderPresence(
										sipMessage, header);
								isChecked = true;
								break;

							}
						}

						if (isChecked)
							break;
					}

				} else {

					for (MANDATORY mandatoryHdr : mandatoryHdrs) {
						List<Method> method = mandatoryHdr.getMethod();
						for (Method mthd : method) {

							if (mthd.getName().value().equals("req")
									&& actionValue.equals(mthd.getValue())) {

								List<Header> header = mthd.getHeader();
								CheckHeaderPresence.checkHeaderPresence(
										sipMessage, header);
								isChecked = true;
								break;

							}
						}

						if (isChecked)
							break;
					}

					for (OPTIONAL optionalHdr : optionalHdrs) {
						List<Method> method = optionalHdr.getMethod();
						for (Method mthd : method) {

							if (mthd.getName().value().equals("req")
									&& actionValue.equals(mthd.getValue())) {

								List<Header> header = mthd.getHeader();
								CheckHeaderPresence.checkHeaderPresence(
										sipMessage, header);
								isChecked = true;
								break;

							}
						}

						if (isChecked)
							break;
					}

				}
			}

			if ((CONFIG_INSTANCE.isKeyExists(VALIDATION))
					&& (CONFIG_INSTANCE.getValue(VALIDATION).equals(YES))) {

				List<MANDATORY> mandatoryHdrs = validation.getMANDATORY();
				List<OPTIONAL> optionalHdrs = validation.getOPTIONAL();

				String line = sipMessage.getFirstLine();

				boolean isChecked = false;

				if (line.startsWith("SIP/2.0")) {

					for (MANDATORY mandatoryHdr : mandatoryHdrs) {
						List<Method> method = mandatoryHdr.getMethod();

						for (Method mthd : method) {

							String value = mthd.getValue();
							if (mthd.getName().value().equals("res")
									&& value.equals(actionValue)) {

								List<Header> header = mthd.getHeader();
								MandatoryHeaderValidation.validateMandatoryHdr(
										sipMessage, header);
								isChecked = true;
								break;

							}
						}

						if (isChecked)
							break;
					}

					isChecked = false;

					for (OPTIONAL optionalHdr : optionalHdrs) {
						List<Method> method = optionalHdr.getMethod();
						for (Method mthd : method) {
							
							String value = mthd.getValue();
							if (mthd.getName().value().equals("res")
									&& value.equals(actionValue)) {

								List<Header> header = mthd.getHeader();
								OptionalHeaderValidation
										.validateOptionalHeader(sipMessage,
												header);
								isChecked = true;
								break;

							}
						}

						if (isChecked)
							break;
					}

				} else {

					for (MANDATORY mandatoryHdr : mandatoryHdrs) {
						List<Method> method = mandatoryHdr.getMethod();
						for (Method mthd : method) {

							if (mthd.getName().value().equals("req")
									&& actionValue.equals(mthd.getValue())) {

								List<Header> header = mthd.getHeader();
								MandatoryHeaderValidation.validateMandatoryHdr(
										sipMessage, header);
								isChecked = true;
								break;

							}
						}

						if (isChecked)
							break;
					}

					for (OPTIONAL optionalHdr : optionalHdrs) {
						List<Method> method = optionalHdr.getMethod();
						for (Method mthd : method) {

							if (mthd.getName().value().equals("req")
									&& actionValue.equals(mthd.getValue())) {

								List<Header> header = mthd.getHeader();
								OptionalHeaderValidation
										.validateOptionalHeader(sipMessage,
												header);
								isChecked = true;
								break;

							}
						}

						if (isChecked)
							break;
					}

				}
			}
		} catch (com.mitester.sipserver.headervalidation.MissingMandatoryHeaderException ex) {
			throw ex;
		} catch (com.mitester.sipserver.headervalidation.InvalidValuesHeaderException ex) {
			throw ex;
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.sipserver.SipServerException(
					"Error while validating "
							+ ProcessSIPMessage.getMethodName(sipMessage)
							+ " SIP message");
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new com.mitester.sipserver.SipServerException(
					"Error while validating "
							+ ProcessSIPMessage.getMethodName(sipMessage)
							+ " SIP message");
		}
	}

	/**
	 * It returns method name/response code with method name of the SIP message
	 * 
	 * @param sipMessage
	 *            is an SIPMessage object holds parsed details of SIP message
	 * @return method name/response code with method name of the SIP message
	 */

	public static String getMethodName(SIPMessage sipMessage) {

		String methodName = null;
		String firstLine = sipMessage.getFirstLine();
		if (firstLine.startsWith(SIPVERSION)) {

			int spaceIndx = firstLine.indexOf(" ");
			methodName = firstLine.substring(spaceIndx + 1, firstLine.indexOf(
					" ", spaceIndx + 1))
					+ "_" + sipMessage.getCSeq().getMethod();
		} else
			methodName = sipMessage.getCSeq().getMethod();
		
		return methodName;

	}
}
