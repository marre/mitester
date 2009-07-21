/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ParseServerScript.java
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
 * Jain SIP stack 				NIST-CONDITIONS-OF-USE 								https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 						The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * JNetStreamStandalone lib     GNU Library or LGPL			     					http://sourceforge.net/projects/jnetstream/
 * 
 */

/* miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications. 
 */
package com.mitester.jaxbparser.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;
import static com.mitester.sipserver.SipServerConstants.ACK_METHOD;
import static com.mitester.sipserver.SipServerConstants.BYE_METHOD;
import static com.mitester.sipserver.SipServerConstants.CANCEL_METHOD;
import static com.mitester.sipserver.SipServerConstants.INFO_METHOD;
import static com.mitester.sipserver.SipServerConstants.INVITE_METHOD;
import static com.mitester.sipserver.SipServerConstants.MESSAGE_METHOD;
import static com.mitester.sipserver.SipServerConstants.NOTIFY_METHOD;
import static com.mitester.sipserver.SipServerConstants.OPTIONS_METHOD;
import static com.mitester.sipserver.SipServerConstants.PRACK_METHOD;
import static com.mitester.sipserver.SipServerConstants.PUBLISH_METHOD;
import static com.mitester.sipserver.SipServerConstants.REFER_METHOD;
import static com.mitester.sipserver.SipServerConstants.REGISTER_METHOD;
import static com.mitester.sipserver.SipServerConstants.SUBSCRIBE_METHOD;
import static com.mitester.sipserver.SipServerConstants.UPDATE_METHOD;
import static com.mitester.utility.UtilityConstants.NORMAL;
import static com.mitester.executor.ExecutorConstants.*;

/**
 * It handles the file operation and JAXB Parsing of the script which is
 * specified in the scripts path of miTester.properties. While parsing scripts,
 * each test script will be parsed separately. If any error occurs, it will
 * throw an error with test case id.
 * 
 */

public class ParseServerScript {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(ParseServerScript.class.getName());

	private static final String FILE_PARSING_SMALLXML = ".xml";

	private static final String FILE_PARSING_CAPITALXML = ".XML";

	private static final String FILE_SEPARATOR = "/";

	private List<TEST> serverScenarios = new ArrayList<TEST>();

	private static Unmarshaller u;

	private static JAXBContext jc;

	private static final String SR = "SR";

	private static final String RR = "RR";

	private static final String BYE = "BYE";

	private static final String APP = "APP";

	private static final String SDES = "SDES";

	private static final String RTP = "RTP";

	private static final String RTCP = "RTCP";

	private static final String MEDIA = "media";

	static {
		try {
			jc = JAXBContext.newInstance("com.mitester.jaxbparser.server");
			LOGGER.info("Unmarshal Created");
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			Schema schema = sf.newSchema(ParseServerScript.class
					.getClassLoader().getResource(
							"com" + FILE_SEPARATOR + "mitester"
									+ FILE_SEPARATOR + "jaxbparser"
									+ FILE_SEPARATOR + "server"
									+ FILE_SEPARATOR + "Server-Script.xsd"));

			u = jc.createUnmarshaller();
			u.setSchema(schema);
			u.setEventHandler(new VaHandeler());

		} catch (Exception ex) {
			// com.mitester.jaxbparser.server catch block
			TestUtility.printMessage(NORMAL,
					"Error at unmarshalling Server Test");
			LOGGER.error("Error at unmarshalling Server Test", ex);
		}
	}

	static class VaHandeler implements ValidationEventHandler {

		public boolean handleEvent(ValidationEvent event) {
			TestUtility.printMessage(NORMAL, event.getMessage());
			LOGGER.error(event.getMessage());

			TestUtility.printMessage(NORMAL, "Line Number	:	"
					+ event.getLocator().getLineNumber());
			LOGGER.error("Line Number	:	" + event.getLocator().getLineNumber());
			TestUtility.printMessage(NORMAL, "Column Number	:	"
					+ event.getLocator().getColumnNumber());
			LOGGER.error("Column Number	:	"
					+ event.getLocator().getColumnNumber());
			return false;
		}
	}

	/**
	 * method called to parse the server script files
	 * 
	 * @param serverScriptPath
	 *            is a name of the server script path
	 * @return is a TreeMap object which includes set of server tests
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws JAXBException
	 * 
	 */

	public List<TEST> FileParsingServer(String serverScriptPath)
			throws IOException, NullPointerException, JAXBException,
			ParserException {

		LOGGER.info("Entered to FileParsingServer");
		File directoryPath = new File(serverScriptPath);

		String ScriptChildren[] = directoryPath.list();

		for (String script : ScriptChildren) {

			String serverScriptPathNew = serverScriptPath.concat(FILE_SEPARATOR
					+ script);

			if (script.equalsIgnoreCase("Content_Files"))
				continue;

			if (script.endsWith(FILE_PARSING_SMALLXML)
					|| script.endsWith(FILE_PARSING_CAPITALXML)) {

				List<com.mitester.jaxbparser.server.TEST> serverScenariosList = parseServerScenarioFile(serverScriptPathNew);

				if (serverScenariosList == null)
					return null;
				else
					serverScenarios.addAll(serverScenariosList);

			} else if (new File(serverScriptPathNew).isDirectory()) {

				serverScenarios = FileParsingServer(serverScriptPathNew);
			}
		}

		return serverScenarios;
	}

	/**
	 * method called to parse the server script files
	 * 
	 * @param serverScriptFile
	 *            is the name of the server script file
	 * @return is a TreeMap object which includes set of server tests
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws JAXBException
	 */

	public List<com.mitester.jaxbparser.server.TEST> parseServerScenarioFile(
			String serverScriptFile) throws IOException, NullPointerException,
			JAXBException, ParserException {

		LOGGER.info("Entered into parse Server ScenarioFile");

		try {
			Object testFlow = u
					.unmarshal(new FileInputStream(serverScriptFile));

			LOGGER.info("Unmarshal completed");
			com.mitester.jaxbparser.server.TESTFLOW sno = (com.mitester.jaxbparser.server.TESTFLOW) testFlow;

			if (!recvMethodNameValidation(sno.getTEST())
					|| !sendSyntaxValidation(sno.getTEST())
					|| !checkMediaPacketPath(sno.getTEST()))
				return null;

			return sno.getTEST();
		} catch (JAXBException ex) {
			TestUtility.printMessage(NORMAL, "Parsing Error in Server Scripts");
			LOGGER.error("Parsing Error in Server Scripts	:	" + ex);
			throw ex;
		} catch (com.mitester.jaxbparser.server.ParserException ex) {
			throw ex;
		}
	}

	/**
	 * validates the RECV method name
	 * 
	 * @param test
	 * @return boolean
	 */
	public boolean recvMethodNameValidation(List<TEST> test)
			throws ParserException {

		LOGGER.info("Entered into recv Method Name Validation");

		for (TEST obj : test) {

			List<Object> actionwait = obj.getACTIONOrWAIT();

			for (Object objAction : actionwait) {

				if (objAction instanceof ACTION) {

					ACTION action = (ACTION) objAction;

					if (action.getRECV() != null) {

						String methodName = action.getValue().toString();

						if (action.getRECV().toString().equalsIgnoreCase(
								REQUEST_MSG)) {
							if (!validateMethodName(methodName.trim())) {
								LOGGER.warn("In " + obj.getTESTID()
										+ ", the RECV method " + methodName
										+ " is an unknown method");
								TestUtility.printMessage("Warning : In "
										+ obj.getTESTID()
										+ ", the RECV method " + methodName
										+ " is an unknown method");
							}
						} else if (action.getRECV().toString()
								.equalsIgnoreCase(RESPONSE_MSG)) {
							try {
								int indexPos = methodName.lastIndexOf("_");
								if (indexPos > 0) {
									String method = methodName.substring(
											indexPos + 1, methodName.length());
									if (!validateMethodName(method.trim())) {
										LOGGER.warn("In " + obj.getTESTID()
												+ ", the RECV method "
												+ methodName + ", the "
												+ method
												+ " is an unknown method");
										TestUtility
												.printMessage("Warning : In "
														+ obj.getTESTID()
														+ ", the RECV method "
														+ methodName
														+ ", the "
														+ method
														+ " is an unknown method");
									}

									String code = methodName.substring(0,
											indexPos);
									int rCode = Integer.parseInt(code);
									if (rCode < 100 || rCode > 699) {
										LOGGER
												.warn("In "
														+ obj.getTESTID()
														+ ", RECV "
														+ methodName
														+ " response, the response Code "
														+ rCode + " is invalid");
										TestUtility
												.printMessage("Warning : In "
														+ obj.getTESTID()
														+ ", RECV "
														+ methodName
														+ " response, the response Code "
														+ rCode + " is invalid");
									}
								} else {
									throw new com.mitester.jaxbparser.server.ParserException(
											"In "
													+ obj.getTESTID()
													+ ", the RECV method "
													+ methodName
													+ " is invalid response syntax");
								}

							} catch (com.mitester.jaxbparser.server.ParserException ex) {
								throw ex;

							} catch (NumberFormatException ex) {
								LOGGER.error(ex.getMessage(), ex);
								throw new com.mitester.jaxbparser.server.ParserException(
										"In "
												+ obj.getTESTID()
												+ ", error while parsing the response code from "
												+ methodName);
							} catch (Exception ex) {
								LOGGER.error(ex.getMessage(), ex);
								throw new com.mitester.jaxbparser.server.ParserException(
										"In " + obj.getTESTID()
												+ ", error while parsing the  "
												+ methodName + " response");
							}
						} else if (action.getRECV().toString()
								.equalsIgnoreCase(MEDIA)) {
							int indexPos = methodName.lastIndexOf("_");
							if (indexPos > 0) {
								String method = methodName.substring(0,
										indexPos);
								if (!method.equals(RTCP)) {
									throw new com.mitester.jaxbparser.server.ParserException(
											"In " + obj.getTESTID()
													+ ", the RECV method "
													+ methodName
													+ " is invalid media type");
								}

								String mediaSuffix = methodName.substring(
										indexPos + 1, methodName.length());
								if (!mediaSuffix.equals(SR)
										&& !mediaSuffix.equals(RR)
										&& !mediaSuffix.equals(BYE)
										&& !mediaSuffix.equals(APP)
										&& !mediaSuffix.equals(SDES)) {
									throw new com.mitester.jaxbparser.server.ParserException(
											"In " + obj.getTESTID()
													+ ", the RECV method "
													+ methodName
													+ " is invalid media type");
								}
							} else {
								if (!methodName.equals(RTP)) {
									throw new com.mitester.jaxbparser.server.ParserException(
											"In " + obj.getTESTID()
													+ ", the RECV method "
													+ methodName
													+ " is invalid media type");
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Validates the SEND message syntax
	 * 
	 * @param test
	 * @return boolean
	 * @throws ParserException
	 */
	public boolean sendSyntaxValidation(List<TEST> test) throws ParserException {

		LOGGER.info("Entered into send Syntax Validation");

		for (TEST obj : test) {
			List<Object> actionwait = obj.getACTIONOrWAIT();

			for (Object objAction : actionwait) {

				if (objAction instanceof ACTION) {

					ACTION action = (ACTION) objAction;

					if (action.getSEND() != null) {

						if (action.getSEND().toString().equalsIgnoreCase(
								RESPONSE_MSG)) {

							String regExpression = "(([0-9]+?)([_]{1})([a-zA-Z0-9]+)+?)";

							String methodName = action.getValue().toString();

							if (!Pattern.matches(regExpression, methodName)) {
								throw new com.mitester.jaxbparser.server.ParserException(
										"In " + obj.getTESTID()
												+ ", the SEND method "
												+ methodName
												+ " is invalid response syntax");
							}
						} else if (action.getSEND().toString()
								.equalsIgnoreCase(MEDIA)) {
							String methodName = action.getValue().toString();
							if (!methodName.trim().equals(RTP)
									&& !methodName.trim().equals(RTCP)
									&& !methodName.trim().equals("RTCP_SR")
									&& !methodName.trim().equals("RTCP_RR")
									&& !methodName.trim().equals("RTCP_SDES")
									&& !methodName.trim().equals("RTCP_APP")
									&& !methodName.trim().equals("RTCP_BYE")) {

								throw new com.mitester.jaxbparser.server.ParserException(
										"In "
												+ obj.getTESTID()
												+ ", the SEND method "
												+ methodName
												+ " is invalid media packet type");
							}

						}

					}
				}
			}
		}
		return true;
	}

	/**
	 * check media packet path
	 * 
	 * @param test
	 * @return boolean
	 * @throws ParserException
	 */
	public boolean checkMediaPacketPath(List<TEST> test) throws ParserException {
		LOGGER.info("Entered into media validation");

		try {

			for (TEST obj : test) {

				List<Object> actionwait = obj.getACTIONOrWAIT();

				for (Object objAction : actionwait) {

					ACTION action = (ACTION) objAction;

					if (objAction instanceof ACTION) {

						SendEnumType send = action.getSEND();

						if (send != null
								&& send.toString().equalsIgnoreCase(MEDIA)) {

							String mediaFilePath = action.getMEDIA().getFile()
									.getSource();
							if (!TestUtility.isFileExist(mediaFilePath)) {
								throw new com.mitester.jaxbparser.server.ParserException(
										"In " + obj.getTESTID()
												+ ", file source path '"
												+ mediaFilePath
												+ "' doesn't exist");

							}

							if (!mediaFilePath.endsWith("txt")
									&& !mediaFilePath.endsWith("pcap")) {
								throw new com.mitester.jaxbparser.server.ParserException(
										"In " + obj.getTESTID()
												+ ", file '"
												+ mediaFilePath
												+ "' miTester doesn't support this file extension");
							}

						}

					}

				}

			}
		} catch (com.mitester.jaxbparser.server.ParserException ex) {
			throw ex;
		} catch (IOException ex) {
			throw new com.mitester.jaxbparser.server.ParserException(
					"error while checking media source path");

		} catch (Exception ex) {

			throw new com.mitester.jaxbparser.server.ParserException(
					"error while checking media source path");

		}

		return true;

	}

	/**
	 * validates the method name
	 * 
	 * @param methodName
	 * @return boolean
	 */

	private boolean validateMethodName(String methodName) {
		return (methodName.equals(INVITE_METHOD)
				|| methodName.equals(ACK_METHOD)
				|| methodName.equals(OPTIONS_METHOD)
				|| methodName.equals(CANCEL_METHOD)
				|| methodName.equals(BYE_METHOD)
				|| methodName.equals(PRACK_METHOD)
				|| methodName.equals(PUBLISH_METHOD)
				|| methodName.equals(INFO_METHOD)
				|| methodName.equals(SUBSCRIBE_METHOD)
				|| methodName.equals(NOTIFY_METHOD)
				|| methodName.equals(PUBLISH_METHOD)
				|| methodName.equals(REFER_METHOD)
				|| methodName.equals(REGISTER_METHOD)
				|| methodName.equals(MESSAGE_METHOD) || methodName
				.equals(UPDATE_METHOD));
	}
}