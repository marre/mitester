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

import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_REQUEST_HEADER_LIST;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_REQUEST_HEADER_LIST_1;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_REQUEST_HEADER_PARAM;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_REQUEST_HEADER_PARAM_1;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_REQUEST_HEADER_PARAM_VALUE;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_REQUEST_HEADER_PARAM_VALUE_1;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_REQUEST_HEADER_VALUE;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_REQUEST_HEADER_VALUE_1;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_RESPONSE_HEADER_LIST;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_RESPONSE_HEADER_LIST_1;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_RESPONSE_HEADER_PARAM;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_RESPONSE_HEADER_PARAM_1;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_RESPONSE_HEADER_PARAM_VALUE;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_RESPONSE_HEADER_PARAM_VALUE_1;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_RESPONSE_HEADER_VALUE;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.CHECK_RESPONSE_HEADER_VALUE_1;
import static com.mitester.sipserver.SIPHeaderValidatorConstants.HEADER_SEPARATOR;
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
import gov.nist.javax.sip.message.SIPMessage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import javax.sip.header.Header;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;

/**
 * This class checks the presence of the headers in the incoming SIP message
 * defined in the server_config properties files
 * 
 * 
 */

public class SIPHeaderValidator {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(SIPHeaderValidator.class.getName());

	private Map<String, String[]> headers = new HashMap<String, String[]>(17);

	private List<SIPMessage> headerList = new ArrayList<SIPMessage>();

	private final Properties configurationFile = new Properties();

	/**
	 * This method used to call the loadServerProperties,
	 * validateServerProperties & populateHeaders methods
	 * 
	 * @return void
	 */
	public boolean loadServerConfigProperties() throws FileNotFoundException,
			IOException {
		if (loadServerProperties()) {
			if (validateServerProperties()) {
				if (populateHeaders())
					return true;
			}
		}
		return false;
	}

	/**
	 * This method used to load the server config properties file
	 * 
	 * @return true if the file is exist
	 * @throws FileNotFoundException
	 */
	private boolean loadServerProperties() throws FileNotFoundException,
			IOException {
		if (TestUtility.isFileExist("lib/server_config.properties")) {
			configurationFile.load(new FileInputStream(
					"lib/server_config.properties"));
			return true;
		} else {
			TestUtility.printMessage(NORMAL,
					"Server_Config file does not exists");
			LOGGER.error("Server_Config file does not exists");
			return false;
		}
	}

	/**
	 * This method used to populateHeaders
	 * 
	 * @return void
	 */
	private boolean populateHeaders() {
		for (Object key : configurationFile.keySet()) {
			headers.put((String) key, configurationFile.getProperty(
					(String) key).split(HEADER_SEPARATOR));
			if (configurationFile.getProperty((String) key).length() == 0) {
				LOGGER.error("Error in reading server_config file");
				TestUtility.printMessage(NORMAL,
						"Error in reading server_config file");
			}
		}

		Set<String> headerKeys = headers.keySet();
		for (String headerKey : headerKeys) {
			if ((headerKey.startsWith(CHECK_REQUEST_HEADER_LIST))
					|| (headerKey.startsWith(CHECK_RESPONSE_HEADER_LIST))) {
				if (!validateHeaderList(headers.get(headerKey)))
					return false;
			} else if ((headerKey.startsWith(CHECK_REQUEST_HEADER_VALUE))
					|| (headerKey.startsWith(CHECK_RESPONSE_HEADER_VALUE))) {
				if (!validateHeaderValueParamList(headers.get(headerKey)))
					return false;
			} else if ((headerKey.startsWith(CHECK_REQUEST_HEADER_PARAM_VALUE))
					|| (headerKey.startsWith(CHECK_RESPONSE_HEADER_PARAM_VALUE))) {
				if (!validateHeaderParamValueList(headers.get(headerKey)))
					return false;
			} else if ((headerKey.startsWith(CHECK_REQUEST_HEADER_PARAM))
					|| (headerKey.startsWith(CHECK_RESPONSE_HEADER_PARAM))) {
				if (!validateHeaderValueParamList(headers.get(headerKey)))
					return false;
			}
		}
		return true;
	}

	/**
	 * This method used to validate the Server Properties whether the file
	 * contains the valid parameters
	 * 
	 * @return true if the file contains the valid parameters
	 */
	private boolean validateServerProperties() {
		if (configurationFile.containsKey(CHECK_REQUEST_HEADER_LIST_1)
				|| configurationFile.containsKey(CHECK_REQUEST_HEADER_VALUE_1)
				|| configurationFile
						.containsKey(CHECK_REQUEST_HEADER_PARAM_VALUE_1)
				|| configurationFile.containsKey(CHECK_REQUEST_HEADER_PARAM_1)
				|| configurationFile.containsKey(CHECK_RESPONSE_HEADER_LIST_1)
				|| configurationFile.containsKey(CHECK_RESPONSE_HEADER_VALUE_1)
				|| configurationFile
						.containsKey(CHECK_RESPONSE_HEADER_PARAM_VALUE_1)
				|| configurationFile.containsKey(CHECK_RESPONSE_HEADER_PARAM_1)
				|| configurationFile.containsKey(CHECK_REQUEST_HEADER_LIST)
				|| configurationFile.containsKey(CHECK_REQUEST_HEADER_VALUE)
				|| configurationFile
						.containsKey(CHECK_REQUEST_HEADER_PARAM_VALUE)
				|| configurationFile.containsKey(CHECK_REQUEST_HEADER_PARAM)
				|| configurationFile.containsKey(CHECK_RESPONSE_HEADER_LIST)
				|| configurationFile.containsKey(CHECK_RESPONSE_HEADER_VALUE)
				|| configurationFile
						.containsKey(CHECK_RESPONSE_HEADER_PARAM_VALUE)
				|| configurationFile.containsKey(CHECK_RESPONSE_HEADER_PARAM)) {
			LOGGER.info("server_config file has read");
			return true;
		} else {
			LOGGER.error("Error in reading server_config file");
			TestUtility.printMessage(NORMAL,
					"Error in reading server_config file");
			return false;
		}
	}

	/**
	 * This method used to validate the headers and will call the appropriate
	 * methods for validation
	 * 
	 * @return true if the validation completed without error
	 */
	public boolean validateHeaders() {
		Set<String> headerKeys = headers.keySet();
		for (String headerKey : headerKeys) {
			if ((headerKey.startsWith(CHECK_REQUEST_HEADER_LIST))) {
				if (!validateRequestHeaderList(headers.get(headerKey)))
					return false;
			} else if ((headerKey.startsWith(CHECK_REQUEST_HEADER_VALUE))) {
				if (!validateRequestHeaderValue(headers.get(headerKey)))
					return false;
			} else if ((headerKey.startsWith(CHECK_REQUEST_HEADER_PARAM_VALUE))) {
				if (!validateRequestHeaderParamValue(headers.get(headerKey)))
					return false;
			} else if ((headerKey.startsWith(CHECK_REQUEST_HEADER_PARAM))) {
				if (!validateRequestHeaderParam(headers.get(headerKey)))
					return false;
			} else if ((headerKey.startsWith(CHECK_RESPONSE_HEADER_LIST))) {
				if (!validateResponseHeaderList(headers.get(headerKey)))
					return false;
			} else if ((headerKey.startsWith(CHECK_RESPONSE_HEADER_VALUE))) {
				if (!validateResponseHeaderValue(headers.get(headerKey)))
					return false;
			} else if ((headerKey.startsWith(CHECK_RESPONSE_HEADER_PARAM_VALUE))) {
				if (!validateResponseHeaderParamValue(headers.get(headerKey)))
					return false;
			} else if ((headerKey.startsWith(CHECK_RESPONSE_HEADER_PARAM))) {
				if (!validateResponseHeaderParam(headers.get(headerKey)))
					return false;
			}
		}
		return true;
	}

	/**
	 * This method used to validate the Request header list
	 * 
	 * @return true if the validation completed without error
	 */
	private boolean validateRequestHeaderList(String[] list) {
		for (int j = 0; j < list.length; j++) {
			String method = list[0];
			if (j == 0)
				continue;
			for (int i = 0; i < headerList.size(); i++) {
				if (headerList.get(i).getCSeq().getMethod().equals(
						method.trim())) {
					SIPMessage sipChkMsg = null;
					sipChkMsg = headerList.get(i);
					String firstLine = sipChkMsg.getFirstLine();
					if (firstLine.trim().startsWith(method.trim())) {
						Header sipheader = sipChkMsg.getHeader(list[j].trim());
						if (sipheader == null) {
							TestUtility.printMessage(list[j].trim()
									+ " Header not found from " + method.trim()
									+ " Request");
							LOGGER.error(list[j].trim()
									+ " Header not found from " + method.trim()
									+ " Request");
							return false;
						} else {
							TestUtility.printMessage(list[j].trim()
									+ " Header exists in " + method.trim()
									+ " Request");
							LOGGER.info(list[j].trim() + " Header exists in "
									+ method.trim() + " Request");
						}
						break;
					}
				}
			}
		}
		return true;
	}

	/**
	 * This method used to validate the Request header value
	 * 
	 * @return true if the validation completed without error
	 */
	private boolean validateRequestHeaderValue(String[] list) {
		String methodName = null;
		String header = null;
		boolean isMethodRepeats = false;
		for (int j = 0; j < list.length; j++) {
			methodName = list[0];
			if (j == 0)
				continue;
			for (int i = 0; i < headerList.size(); i++) {
				if (headerList.get(i).getCSeq().getMethod().equals(
						methodName.trim())) {
					int indexPos = list[j].indexOf(":");
					header = list[j].substring(0, indexPos);
					SIPMessage sipChkMsg = null;
					sipChkMsg = headerList.get(i);
					String firstLine = sipChkMsg.getFirstLine();
					if (firstLine.trim().startsWith(methodName.trim())) {
						Header sipHeader = sipChkMsg.getHeader(header.trim());
						if (sipHeader != null) {
							String testHeader = sipHeader.toString();
							if (!testHeader.trim().equals(list[j].trim())) {
								for (int k = i; k < headerList.size(); k++) {
									if (headerList.get(k).getCSeq().getMethod()
											.equals(methodName.trim())) {
										isMethodRepeats = true;
										break;
									}
								}
								if (!isMethodRepeats) {
									TestUtility.printMessage(list[j].trim()
											+ " value not found from "
											+ header.trim() + " Header in "
											+ methodName.trim() + " Request");
									LOGGER.error(list[j].trim()
											+ " value not found from "
											+ header.trim() + " Header in "
											+ methodName.trim() + " Request");
									return false;
								}
							} else {
								TestUtility.printMessage("In "
										+ methodName.trim()
										+ " Request, the value "
										+ list[j].trim() + " exists in "
										+ header.trim() + " Header");
								LOGGER.info("In " + methodName.trim()
										+ " Request, the value "
										+ list[j].trim() + " exists in "
										+ header.trim() + " Header");
								break;
							}
						} else {
							TestUtility.printMessage(header.trim()
									+ " Header not found from "
									+ methodName.trim() + " Request");
							LOGGER.error(header.trim()
									+ " Header not found from "
									+ methodName.trim() + " Request");
							return false;
						}
						// break;
					}
				}
			}
		}
		return true;
	}

	/**
	 * This method used to validate the Request header param
	 * 
	 * @return true if the validation completed without error
	 */
	private boolean validateRequestHeaderParam(String[] list) {
		String methodName = null;
		String header = null;
		String param = null;

		for (int j = 0; j < list.length; j++) {
			methodName = list[0];
			if (j == 0)
				continue;
			for (int i = 0; i < headerList.size(); i++) {
				if (headerList.get(i).getCSeq().getMethod().equals(
						methodName.trim())) {
					int indexPos = list[j].indexOf(":");
					header = list[j].substring(0, indexPos);
					param = list[j].substring(indexPos + 1, list[j].length());
					SIPMessage sipChkMsg = null;
					sipChkMsg = headerList.get(i);
					String firstLine = sipChkMsg.getFirstLine();
					if (firstLine.trim().startsWith(methodName.trim())) {
						Header sipHeader = sipChkMsg.getHeader(header.trim());
						if (sipHeader != null) {
							String testHeader = sipHeader.toString();
							String[] splitParam = testHeader
									.split(param.trim());
							if (splitParam.length < 2) {
								TestUtility.printMessage(param.trim()
										+ " param not found from "
										+ header.trim() + " Header in "
										+ methodName.trim() + " Request");
								LOGGER.error(param.trim()
										+ " param not found from "
										+ header.trim() + " Header in "
										+ methodName.trim() + " Request");
								return false;
							} else {
								TestUtility.printMessage("In "
										+ methodName.trim()
										+ " Request, the param " + param.trim()
										+ " exists in " + header.trim()
										+ " Header");
								LOGGER.info("In " + methodName.trim()
										+ " Request, the param " + param.trim()
										+ " exists in " + header.trim()
										+ " Header");
							}
						} else {
							TestUtility.printMessage(header.trim()
									+ " Header not found from "
									+ methodName.trim() + " Request");
							LOGGER.error(header.trim()
									+ " Header not found from "
									+ methodName.trim() + " Request");
							return false;
						}
						break;
					}
				}
			}
		}
		return true;
	}

	/**
	 * This method used to validate the Request header param value
	 * 
	 * @return true if the validation completed without error
	 */
	private boolean validateRequestHeaderParamValue(String[] list) {
		String methodName = null;
		String header = null;
		String param = null;

		for (int j = 0; j < list.length; j++) {
			methodName = list[0];
			if (j == 0)
				continue;
			for (int i = 0; i < headerList.size(); i++) {
				if (headerList.get(i).getCSeq().getMethod().equals(
						methodName.trim())) {
					int indexPos = list[j].indexOf(":");
					header = list[j].substring(0, indexPos);
					param = list[j].substring(indexPos + 1, list[j].length());
					SIPMessage sipChkMsg = null;
					sipChkMsg = headerList.get(i);
					String firstLine = sipChkMsg.getFirstLine();
					if (firstLine.trim().startsWith(methodName.trim())) {
						Header sipHeader = sipChkMsg.getHeader(header.trim());
						if (sipHeader != null) {
							int indexParamValuePos = param.indexOf("=");
							String paramName = param.substring(0,
									indexParamValuePos);
							String paramValue = param.substring(
									indexParamValuePos + 1, param.length());

							String testHeader = sipHeader.toString();
							String[] splitParam = testHeader.split(paramName
									.trim());
							if (splitParam.length >= 2) {
								String[] testStr = splitParam[1].split("=");
								String[] testStrSpace = testStr[1].split(" ");
								String[] testStrComma = testStr[1].split(",");
								String[] testStrSemiColon = testStr[1]
										.split(";");
								if (testStrSpace.length >= 2) {
									if (!testStrSpace[0]
											.trim()
											.equalsIgnoreCase(paramValue.trim())) {
										TestUtility.printMessage(paramValue
												.trim()
												+ " value not found from "
												+ param.trim()
												+ " of header "
												+ header.trim()
												+ " in "
												+ methodName.trim()
												+ " Request");
										LOGGER.error(paramValue.trim()
												+ " value not found from "
												+ param.trim() + " of header "
												+ header.trim() + " in "
												+ methodName.trim()
												+ " Request");
										return false;
									}
								} else if (testStrComma.length >= 2) {
									String str1 = testStrComma[0].trim();
									if (!str1.equalsIgnoreCase(paramValue
											.trim())) {
										TestUtility.printMessage(paramValue
												.trim()
												+ " value not found from "
												+ param.trim()
												+ " of header "
												+ header.trim()
												+ " in "
												+ methodName.trim()
												+ " Request");
										LOGGER.error(paramValue.trim()
												+ " value not found from "
												+ param.trim() + " of header "
												+ header.trim() + " in "
												+ methodName.trim()
												+ " Request");
										return false;
									}
								} else if (testStrSemiColon.length >= 2) {
									if (!testStrSemiColon[0]
											.trim()
											.equalsIgnoreCase(paramValue.trim())) {
										TestUtility.printMessage(paramValue
												.trim()
												+ " value not found from "
												+ param.trim()
												+ " of header "
												+ header.trim()
												+ " in "
												+ methodName.trim()
												+ " Request");
										LOGGER.error(paramValue.trim()
												+ " value not found from "
												+ param.trim() + " of header "
												+ header.trim() + " in "
												+ methodName.trim()
												+ " Request");
										return false;
									}
								} else if (!testStr[1].trim().equalsIgnoreCase(
										paramValue.trim())) {
									TestUtility.printMessage(paramValue.trim()
											+ " value not found from "
											+ param.trim() + " of header "
											+ header.trim() + " in "
											+ methodName.trim() + " Request");
									LOGGER.error(paramValue.trim()
											+ " value not found from "
											+ param.trim() + " of header "
											+ header.trim() + " in "
											+ methodName.trim() + " Request");
									return false;
								} else {
									TestUtility.printMessage("In "
											+ methodName.trim()
											+ " Request, the value "
											+ paramValue.trim() + " of param "
											+ param.trim() + " exists in "
											+ header.trim() + " Header");
									LOGGER.info("In " + methodName.trim()
											+ " Request, the value "
											+ paramValue.trim() + " of param "
											+ param.trim() + " exists in "
											+ header.trim() + " Header");
								}
							} else {
								TestUtility.printMessage(param.trim()
										+ " param not found from "
										+ header.trim() + " Header in "
										+ methodName.trim() + " Request");
								LOGGER.warn(param.trim()
										+ " param not found from "
										+ header.trim() + " Header in "
										+ methodName.trim() + " Request");
								return false;
							}
						} else {
							TestUtility.printMessage(header.trim()
									+ " Header not found from "
									+ methodName.trim() + " Request");
							LOGGER.warn(header.trim()
									+ " Header not found from "
									+ methodName.trim() + " Request");
							return false;
						}
						break;
					}
				}
			}
		}
		return true;
	}

	/**
	 * This method used to validate the Response header list
	 * 
	 * @return true if the validation completed without error
	 */
	private boolean validateResponseHeaderList(String[] list) {
		String methodName = null;
		String method = null;
		String code = null;
		for (int j = 0; j < list.length; j++) {
			if (j == 0) {
				methodName = list[0];
				continue;
			}
			if (methodName.length() != 0) {
				int indexPos = methodName.lastIndexOf("_");
				method = methodName
						.substring(indexPos + 1, methodName.length());
				code = methodName.substring(0, indexPos);
				for (int i = 0; i < headerList.size(); i++) {
					if (headerList.get(i).getCSeq().getMethod().equals(
							method.trim())) {
						SIPMessage sipChkMsg = null;
						sipChkMsg = headerList.get(i);
						String firstLine = sipChkMsg.getFirstLine();
						String splitValue[] = firstLine.split("SIP/2.0");
						String str = splitValue[1].trim();
						if (str.startsWith(code.trim())) {
							Header sipheader = sipChkMsg.getHeader(list[j]
									.trim());
							if (sipheader == null) {
								TestUtility.printMessage(list[j].trim()
										+ " Header not found from "
										+ methodName.trim() + " Response");
								LOGGER.error(list[j].trim()
										+ " Header not found from "
										+ methodName.trim() + " Response");
								return false;
							} else {
								TestUtility.printMessage(list[j].trim()
										+ " Header exists in " + method.trim()
										+ " Response");
								LOGGER.info(list[j].trim()
										+ " Header exists in " + method.trim()
										+ " Response");
							}
							break;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * This method used to validate the Response header value
	 * 
	 * @return true if the validation completed without error
	 */
	private boolean validateResponseHeaderValue(String[] list) {
		String methodName = null;
		String header = null;
		String method = null;
		String code = null;
		boolean isMethodRepeats = false;
		for (int j = 0; j < list.length; j++) {
			methodName = list[0];
			if (methodName.length() != 0) {
				if (j == 0) {
					int indexPos1 = methodName.lastIndexOf("_");
					method = methodName.substring(indexPos1 + 1, methodName
							.length());
					code = methodName.substring(0, indexPos1);
					continue;
				}
				for (int i = 0; i < headerList.size(); i++) {
					if (headerList.get(i).getCSeq().getMethod().equals(
							method.trim())) {
						SIPMessage sipChkMsg = null;
						sipChkMsg = headerList.get(i);
						String firstLine = sipChkMsg.getFirstLine();
						String splitValue[] = firstLine.split("SIP/2.0");
						String str = splitValue[1].trim();
						if (str.startsWith(code.trim())) {
							int indexPos = list[j].indexOf(":");
							header = list[j].substring(0, indexPos);
							Header sipHeader = sipChkMsg.getHeader(header
									.trim());
							if (sipHeader != null) {
								String testHeader = sipHeader.toString();
								if (!testHeader.trim().equals(list[j].trim())) {
									for (int k = i; k < headerList.size(); k++) {
										if (headerList.get(k).getCSeq()
												.getMethod().equals(
														methodName.trim())) {
											isMethodRepeats = true;
											break;
										}
									}
									if (!isMethodRepeats) {
										TestUtility.printMessage(list[j].trim()
												+ " value not found from "
												+ header.trim() + " Header in "
												+ methodName.trim()
												+ " Response");
										LOGGER.error(list[j].trim()
												+ " value not found from "
												+ header.trim() + " Header in "
												+ methodName.trim()
												+ " Response");
										return false;
									}
								} else {
									TestUtility.printMessage("In "
											+ methodName.trim()
											+ " Response, the value "
											+ list[j].trim() + " exists in "
											+ header.trim() + " Header");
									LOGGER.info("In " + methodName.trim()
											+ " Response, the value "
											+ list[j].trim() + " exists in "
											+ header.trim() + " Header");
								}
							} else {
								TestUtility.printMessage(header.trim()
										+ " Header not found from "
										+ methodName.trim() + " Response");
								LOGGER.error(header.trim()
										+ " Header not found from "
										+ methodName.trim() + " Response");
								return false;
							}
							// break;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * This method used to validate the Response header param
	 * 
	 * @return true if the validation completed without error
	 */
	private boolean validateResponseHeaderParam(String[] list) {
		String methodName = null;
		String header = null;
		String method = null;
		String code = null;
		String param = null;
		for (int j = 0; j < list.length; j++) {
			methodName = list[0];
			if (methodName.length() != 0) {
				if (j == 0) {
					int indexPos1 = methodName.lastIndexOf("_");
					method = methodName.substring(indexPos1 + 1, methodName
							.length());
					code = methodName.substring(0, indexPos1);
					continue;
				}
				for (int i = 0; i < headerList.size(); i++) {
					if (headerList.get(i).getCSeq().getMethod().equals(
							method.trim())) {
						SIPMessage sipChkMsg = null;
						sipChkMsg = headerList.get(i);
						String firstLine = sipChkMsg.getFirstLine();
						String splitValue[] = firstLine.split("SIP/2.0");
						String str = splitValue[1].trim();
						if (str.startsWith(code.trim())) {
							int indexPos = list[j].indexOf(":");
							header = list[j].substring(0, indexPos);
							param = list[j].substring(indexPos + 1, list[j]
									.length());
							Header sipHeader = sipChkMsg.getHeader(header
									.trim());
							if (sipHeader != null) {
								String testHeader = sipHeader.toString();
								String[] splitParam = testHeader.split(param
										.trim());
								if (splitParam.length < 2) {
									TestUtility.printMessage(param.trim()
											+ " param not found from "
											+ header.trim() + " Header in "
											+ methodName.trim() + " Response");
									LOGGER.error(param.trim()
											+ " param not found from "
											+ header.trim() + " Header in "
											+ methodName.trim() + " Response");
									return false;
								} else {
									TestUtility.printMessage("In "
											+ methodName.trim()
											+ " Response, the param "
											+ param.trim() + " exists in "
											+ header.trim() + " Header");
									LOGGER.info("In " + methodName.trim()
											+ " Response, the param "
											+ param.trim() + " exists in "
											+ header.trim() + " Header");
								}
							} else {
								TestUtility.printMessage(header.trim()
										+ " Header not found from "
										+ methodName.trim() + " Response");
								LOGGER.error(header.trim()
										+ " Header not found from "
										+ methodName.trim() + " Response");
								return false;
							}
							break;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * This method used to validate the Response header param value
	 * 
	 * @return true if the validation completed without error
	 */
	private boolean validateResponseHeaderParamValue(String[] list) {
		String methodName = null;
		String header = null;
		String param = null;
		String method = null;
		String code = null;
		for (int j = 0; j < list.length; j++) {
			methodName = list[0];
			if (methodName.length() != 0) {
				if (j == 0) {
					int indexPos1 = methodName.lastIndexOf("_");
					method = methodName.substring(indexPos1 + 1, methodName
							.length());
					code = methodName.substring(0, indexPos1);
					continue;
				}
				for (int i = 0; i < headerList.size(); i++) {
					if (headerList.get(i).getCSeq().getMethod().equals(
							method.trim())) {
						SIPMessage sipChkMsg = null;
						sipChkMsg = headerList.get(i);
						String firstLine = sipChkMsg.getFirstLine();
						String splitValue[] = firstLine.split("SIP/2.0");
						String str = splitValue[1].trim();
						if (str.startsWith(code.trim())) {
							int indexPos = list[j].indexOf(":");
							header = list[j].substring(0, indexPos);
							param = list[j].substring(indexPos + 1, list[j]
									.length());
							Header sipHeader = sipChkMsg.getHeader(header
									.trim());
							if (sipHeader != null) {
								int indexParamValuePos = param.indexOf("=");
								String paramName = param.substring(0,
										indexParamValuePos);
								String paramValue = param.substring(
										indexParamValuePos + 1, param.length());
								String testHeader = sipHeader.toString();
								String[] splitParam = testHeader
										.split(paramName.trim());
								if (splitParam.length >= 2) {
									String[] testStr = splitParam[1].split("=");
									String[] testStrSpace = testStr[1]
											.split(" ");
									String[] testStrComma = testStr[1]
											.split(",");
									String[] testStrSemiColon = testStr[1]
											.split(";");
									if (testStrSpace.length >= 2) {
										if (!testStrSpace[0].trim()
												.equalsIgnoreCase(
														paramValue.trim())) {
											TestUtility.printMessage(paramValue
													.trim()
													+ " value not found from "
													+ param.trim()
													+ " of header "
													+ header.trim()
													+ " in "
													+ methodName.trim()
													+ " Response");
											LOGGER.error(paramValue.trim()
													+ " value not found from "
													+ param.trim()
													+ " of header "
													+ header.trim() + " in "
													+ methodName.trim()
													+ " Response");
											return false;
										}
									} else if (testStrComma.length >= 2) {
										if (!testStrComma[0].trim()
												.equalsIgnoreCase(
														paramValue.trim())) {
											TestUtility.printMessage(paramValue
													.trim()
													+ " value not found from "
													+ param.trim()
													+ " of header "
													+ header.trim()
													+ " in "
													+ methodName.trim()
													+ " Response");
											LOGGER.error(paramValue.trim()
													+ " value not found from "
													+ param.trim()
													+ " of header "
													+ header.trim() + " in "
													+ methodName.trim()
													+ " Response");
											return false;
										}
									} else if (testStrSemiColon.length >= 2) {
										if (!testStrSemiColon[0].trim()
												.equalsIgnoreCase(
														paramValue.trim())) {
											TestUtility.printMessage(paramValue
													.trim()
													+ " value not found from "
													+ param.trim()
													+ " of header "
													+ header.trim()
													+ " in "
													+ methodName.trim()
													+ " Response");
											LOGGER.error(paramValue.trim()
													+ " value not found from "
													+ param.trim()
													+ " of header "
													+ header.trim() + " in "
													+ methodName.trim()
													+ " Response");
											return false;
										}
									} else if (!testStr[1]
											.trim()
											.equalsIgnoreCase(paramValue.trim())) {
										TestUtility.printMessage(paramValue
												.trim()
												+ " value not found from "
												+ param.trim()
												+ " of header "
												+ header.trim()
												+ " in "
												+ methodName.trim()
												+ " Response");
										LOGGER.error(paramValue.trim()
												+ " value not found from "
												+ param.trim() + " of header "
												+ header.trim() + " in "
												+ methodName.trim()
												+ " Response");
										return false;
									} else {
										TestUtility.printMessage("In "
												+ methodName.trim()
												+ " Response, the value "
												+ paramValue.trim()
												+ " of param " + param.trim()
												+ " exists in " + header.trim()
												+ " Header");
										LOGGER.info("In " + methodName.trim()
												+ " Response, the value "
												+ paramValue.trim()
												+ " of param " + param.trim()
												+ " exists in " + header.trim()
												+ " Header");
									}
								} else {
									TestUtility.printMessage(param.trim()
											+ " param not found from "
											+ header.trim() + " Header in "
											+ methodName.trim() + " Response");
									LOGGER.error(param.trim()
											+ " param not found from "
											+ header.trim() + " Header in "
											+ methodName.trim() + " Response");
									return false;
								}
							} else {
								TestUtility.printMessage(header.trim()
										+ " Header not found from "
										+ methodName.trim() + " Response");
								LOGGER.error(header.trim()
										+ " Header not found from "
										+ methodName.trim() + " Response");
								return false;
							}
							break;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * This method used to validate the method name
	 * 
	 * @return true if the validation completed
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

	/**
	 * This method used to add the incoming methods to the server
	 * 
	 * @return void
	 */
	public void setheaderList(SIPMessage sipMsg) {
		headerList.add(sipMsg);
	}

	/**
	 * This method used to cleanup the header list
	 * 
	 * @return void
	 */
	public void cleanUpheaderList() {
		headerList.clear();
		headerList = new ArrayList<SIPMessage>();
	}

	/**
	 * This method used to validate the header list
	 * 
	 * @return true if the validation completed without error
	 */
	public boolean validateHeaderList(String[] list) {
		String methodName = null;
		String method = null;
		if (list.length == 1) {
			TestUtility.printMessage(NORMAL, "Error in server_config file");
			LOGGER.error("Error in server_config file");
			return false;
		}
		for (int j = 0; j < list.length; j++) {
			if (j == 0) {
				methodName = list[0];
				continue;
			}
			if (methodName.length() != 0) {
				int indexPos = methodName.lastIndexOf("_");
				if (indexPos > 0) {
					// Response
					method = methodName.substring(indexPos + 1, methodName
							.length());
					if (!validateMethodName(method.trim())) {
						TestUtility.printMessage(NORMAL,
								"In CHECK_RESPONSE_HEADER_LIST, the given METHOD name "
										+ method.trim()
										+ " is not defined in RFC's");
						LOGGER
								.error("In CHECK_RESPONSE_HEADER_LIST, the given METHOD name "
										+ method.trim()
										+ " is not defined in RFC's");
						return false;
					}
				} else {
					// Request
					if (!validateMethodName(methodName.trim())) {
						TestUtility.printMessage(NORMAL,
								"In CHECK_REQUEST_HEADER_LIST, the given METHOD name "
										+ methodName.trim()
										+ " is not defined in RFC's");
						LOGGER
								.error("In CHECK_REQUEST_HEADER_LIST, the given METHOD name "
										+ methodName.trim()
										+ " is not defined in RFC's");
						return false;
					}
				}
			}
			if (SIPHeaders.getSipHeaderfromString(list[j].trim()) == SIPHeaders.CUSTOM) {
				TestUtility.printMessage(NORMAL,
						"In CHECK_REQUEST_HEADER_LIST, the given Header "
								+ list[j].trim()
								+ " is not defined in RFC's Headers");
				LOGGER.error("In CHECK_REQUEST_HEADER_LIST, the given Header "
						+ list[j].trim() + " is not defined in RFC's Headers");
				return false;
			}
		}
		return true;
	}

	/**
	 * This method used to validate the header value and param value list
	 * 
	 * @return true if the validation completed without error
	 */
	public boolean validateHeaderValueParamList(String[] list) {
		String methodName = null;
		String value = null;
		String method = null;
		String header = null;
		if (list.length == 1) {
			TestUtility.printMessage(NORMAL, "Error in server_config file");
			LOGGER.error("Error in server_config file");
			return false;
		}
		for (int j = 0; j < list.length; j++) {
			methodName = list[0];
			if (j == 0) {
				continue;
			}
			if (methodName.length() != 0) {
				int indexPos = methodName.lastIndexOf("_");
				if (indexPos > 0) {
					// Response
					method = methodName.substring(indexPos + 1, methodName
							.length());
					if (!validateMethodName(method.trim())) {
						TestUtility
								.printMessage(
										NORMAL,
										"In CHECK_RESPONSE_HEADER_VALUE/CHECK_RESPONSE_HEADER_PARAM, the given METHOD name "
												+ method.trim()
												+ " is not defined in RFC's");
						LOGGER
								.error("In CHECK_RESPONSE_HEADER_VALUE/CHECK_RESPONSE_HEADER_PARAM, the given METHOD name "
										+ method.trim()
										+ " is not defined in RFC's");
						return false;
					}
					int valueIndexPos = list[j].indexOf(":");
					if (valueIndexPos > 0) {
						header = list[j].substring(0, valueIndexPos);
						if (SIPHeaders.getSipHeaderfromString(header.trim()) == SIPHeaders.CUSTOM) {
							TestUtility
									.printMessage(
											NORMAL,
											"In CHECK_REQUEST_HEADER_LIST, the given Header "
													+ list[j].trim()
													+ " is not defined in RFC's Headers");
							LOGGER
									.error("In CHECK_REQUEST_HEADER_LIST, the given Header "
											+ list[j].trim()
											+ " is not defined in RFC's Headers");
							return false;
						}
						value = list[j].substring(valueIndexPos + 1, list[j]
								.length());
						if (value.trim().length() == 0) {
							TestUtility
									.printMessage(
											NORMAL,
											"Error in CHECK_RESPONSE_HEADER_VALUE/CHECK_RESPONSE_HEADER_PARAM syntax, Header Value/Param need to be given");
							LOGGER
									.error("Error in CHECK_RESPONSE_HEADER_VALUE/CHECK_RESPONSE_HEADER_PARAM syntax, Header Value/Param need to be given");
							return false;
						}
					} else {
						TestUtility
								.printMessage(NORMAL,
										"Error in CHECK_RESPONSE_HEADER_VALUE/CHECK_RESPONSE_HEADER_PARAM syntax");
						LOGGER
								.error("Error in CHECK_RESPONSE_HEADER_VALUE/CHECK_RESPONSE_HEADER_PARAM syntax");
						return false;
					}
				} else {
					// Request
					if (!validateMethodName(methodName.trim())) {
						TestUtility
								.printMessage(
										NORMAL,
										"In CHECK_REQUEST_HEADER_VALUE/CHECK_REQUEST_HEADER_PARAM, the given METHOD name "
												+ methodName.trim()
												+ " is not defined in RFC's");
						LOGGER
								.warn("In CHECK_REQUEST_HEADER_VALUE/CHECK_REQUEST_HEADER_PARAM, the given METHOD name "
										+ methodName.trim()
										+ " is not defined in RFC's");
						return false;
					}
					int valueIndexPos = list[j].indexOf(":");
					header = list[j].substring(0, valueIndexPos);
					if (SIPHeaders.getSipHeaderfromString(header.trim()) == SIPHeaders.CUSTOM) {
						TestUtility.printMessage(NORMAL,
								"In CHECK_REQUEST_HEADER_LIST, the given Header "
										+ list[j].trim()
										+ " is not defined in RFC's Headers");
						LOGGER
								.warn("In CHECK_REQUEST_HEADER_LIST, the given Header "
										+ list[j].trim()
										+ " is not defined in RFC's Headers");
						return false;
					}
					if (valueIndexPos > 0) {
						value = list[j].substring(valueIndexPos + 1, list[j]
								.length());
						if (value.trim().length() == 0) {
							TestUtility
									.printMessage(
											NORMAL,
											"Error in CHECK_REQUEST_HEADER_VALUE/CHECK_REQUEST_HEADER_PARAM syntax, Header Value/Param need to be given");
							LOGGER
									.error("Error in CHECK_REQUEST_HEADER_VALUE/CHECK_REQUEST_HEADER_PARAM syntax, Header Value/Param need to be given");
							return false;
						}
					} else {
						TestUtility
								.printMessage(
										NORMAL,
										"Error in CHECK_REQUEST_HEADER_VALUE/CHECK_REQUEST_HEADER_PARAM syntax, Header Value/Param need to be given");
						LOGGER
								.warn("Error in CHECK_REQUEST_HEADER_VALUE/CHECK_REQUEST_HEADER_PARAM syntax, Header Value/Param need to be given");
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * This method used to validate the header param list
	 * 
	 * @return true if the validation completed without error
	 */
	public boolean validateHeaderParamValueList(String[] list) {
		String methodName = null;
		String param = null;
		String method = null;
		String header = null;
		if (list.length == 1) {
			TestUtility.printMessage(NORMAL, "Error in server_config file");
			LOGGER.error("Error in server_config file");
			return false;
		}
		for (int j = 0; j < list.length; j++) {
			methodName = list[0];
			if (j == 0) {
				continue;
			}
			if (methodName.length() != 0) {
				int indexPos = methodName.lastIndexOf("_");
				if (indexPos > 0) {
					// Response
					method = methodName.substring(indexPos + 1, methodName
							.length());
					if (!validateMethodName(method.trim())) {
						TestUtility.printMessage(NORMAL,
								"In CHECK_RESPONSE_HEADER_PARAM_VALUE, the given METHOD name "
										+ method.trim()
										+ " is not defined in RFC's");
						LOGGER
								.error("In CHECK_RESPONSE_HEADER_PARAM_VALUE, the given METHOD name "
										+ method.trim()
										+ " is not defined in RFC's");
						return false;
					}
					int valueIndexPos = list[j].indexOf(":");
					if (valueIndexPos > 0) {
						header = list[j].substring(0, valueIndexPos);
						if (SIPHeaders.getSipHeaderfromString(header.trim()) == SIPHeaders.CUSTOM) {
							TestUtility
									.printMessage(
											NORMAL,
											"In CHECK_REQUEST_HEADER_LIST, the given Header "
													+ list[j].trim()
													+ " is not defined in RFC's Headers");
							LOGGER
									.error("In CHECK_REQUEST_HEADER_LIST, the given Header "
											+ list[j].trim()
											+ " is not defined in RFC's Headers");
							return false;
						}
						param = list[j].substring(valueIndexPos + 1, list[j]
								.length());
						if (param.trim().length() == 0) {
							TestUtility
									.printMessage(
											NORMAL,
											"Error in CHECK_RESPONSE_HEADER_PARAM_VALUE syntax, Param name need to be given");
							LOGGER
									.error("Error in CHECK_RESPONSE_HEADER_PARAM_VALUE syntax, Param name need to be given");
							return false;
						} else {
							if (param.indexOf("=") <= 0) {
								TestUtility
										.printMessage(
												NORMAL,
												"Error in CHECK_RESPONSE_HEADER_PARAM_VALUE syntax, Param value need to be given");
								LOGGER
										.error("Error in CHECK_RESPONSE_HEADER_PARAM_VALUE syntax, Param value need to be given");
								return false;
							}
						}
					} else {
						TestUtility
								.printMessage(NORMAL,
										"Error in CHECK_RESPONSE_HEADER_PARAM_VALUE syntax");
						LOGGER
								.error("Error in CHECK_RESPONSE_HEADER_PARAM_VALUE syntax");
						return false;
					}
				} else {
					// Request
					if (!validateMethodName(methodName.trim())) {
						TestUtility.printMessage(NORMAL,
								"In CHECK_REQUEST_HEADER_PARAM_VALUE, the given METHOD name "
										+ methodName.trim()
										+ " is not defined in RFC's");
						LOGGER
								.error("In CHECK_REQUEST_HEADER_PARAM_VALUE, the given METHOD name "
										+ methodName.trim()
										+ " is not defined in RFC's");
						return false;
					}
					int valueIndexPos = list[j].indexOf(":");
					header = list[j].substring(0, indexPos);
					if (SIPHeaders.getSipHeaderfromString(header.trim()) == SIPHeaders.CUSTOM) {
						TestUtility.printMessage(NORMAL,
								"In CHECK_REQUEST_HEADER_LIST, the given Header "
										+ list[j].trim()
										+ " is not defined in RFC's Headers");
						LOGGER
								.error("In CHECK_REQUEST_HEADER_LIST, the given Header "
										+ list[j].trim()
										+ " is not defined in RFC's Headers");
						return false;
					}
					if (valueIndexPos > 0) {
						param = list[j].substring(valueIndexPos + 1, list[j]
								.length());
						if (param.trim().length() == 0) {
							TestUtility
									.printMessage(NORMAL,
											"Error in CHECK_REQUEST_HEADER_PARAM_VALUE syntax, Param name need to be given");
							LOGGER
									.error("Error in CHECK_REQUEST_HEADER_PARAM_VALUE syntax, Param name need to be given");
							return false;
						}
					} else {
						TestUtility
								.printMessage(
										NORMAL,
										"Error in CHECK_REQUEST_HEADER_PARAM_VALUE syntax, Param value need to be given");
						LOGGER
								.error("Error in CHECK_REQUEST_HEADER_PARAM_VALUE syntax, Param value need to be given");
						return false;
					}
				}
			}
		}
		return true;
	}
}