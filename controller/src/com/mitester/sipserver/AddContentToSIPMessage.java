/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: AddContentToSIPMessage.java
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
 * Package 						License 										    Details
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

import gov.nist.javax.sip.message.SIPMessage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;
import com.mitester.jaxbparser.sdpbody.ParseSDPBody;
import com.mitester.jaxbparser.server.CONTENT;
import com.mitester.jaxbparser.server.OTHERSBODY;
import com.mitester.jaxbparser.server.Param;
import com.mitester.jaxbparser.server.SDPBODY;
import com.mitester.jaxbparser.server.Sdp;
import com.mitester.jaxbparser.server.TXTBODY;
import com.mitester.jaxbparser.server.XMLBODY;
import com.mitester.sipserver.sipheaderhandler.CustomHeaderHandler;
import com.mitester.sipserver.sipmessagehandler.CANCELRequestHandler;
import com.mitester.utility.ConfigurationProperties;
import com.mitester.utility.MiTesterLog;

/**
 * It used to add content to the sip message
 * 
 */

public class AddContentToSIPMessage {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(CANCELRequestHandler.class.getName());
	private static final String NEWLINE = "\r\n";
	private static final String DOUBLE_HYPHON = "--";
	private static final String CONTENT_TYPE_SEPARATOR = "/";
	private static final String CONTENT_LENGTH = "Content-Length: ";
	private static final String APPLICATION = "application";
	private static final String SDP = "sdp";
	private static final String SEMICOLON = ";";
	private static final String EQUALS = "=";
	private static final String SERVER_PARSING_ACTION_ANGLE_SEPARATOR = "/>";
	private static final String SERVER_PARSING_ACTION_END_TEXT_SEPARATOR = "</TEXT>";
	private static final String SERVER_PARSING_ACTION_START_TEXT_SEPARATOR = "<TEXT>";
	private static final String SERVER_PARSING_ACTION_START_INNER_TXT_SEPARATOR = "<txt";
	private static final String serverScriptPath = ConfigurationProperties.CONFIG_INSTANCE
			.getValue("SCRIPT_PATH_MITESTER");
	private static boolean misspeltCT = false;
	private static Header sdpmisspelt = null;
	private static Header txtmissplet = null;
	private static Header xmlmissplet = null;
	private static Header othersmissplet = null;
	private static final String FILE_SEPARATOR = "/";
	private static final String CONTENT_FILE = "Content_Files";

	/**
	 * This method is used to add a content to the SIP Message
	 * 
	 * @param Type represents sip request/response
	 * @param sipMessage is a  sip message constructed by jain sip stack
	 * @param content going to be added with sip message
	 * @return SIPMessage with added content
	 * @throws SipException
	 * @throws InvalidArgumentException
	 * @throws ParseException
	 * @throws IOException
	 */
	public static SIPMessage setSDPContent(String Type, String sipMessage,
			CONTENT content) throws SipException, InvalidArgumentException,
			ParseException, IOException {
		LOGGER.info("Adding Content to the SIP Message is started");
		SIPMessage returnIPMessage = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory headerFactory = factory.createHeaderFactory();
		ContentTypeHeader contentType_sdp = null;
		ContentTypeHeader contentType_txt = null;
		ContentTypeHeader contentType_xml = null;
		ContentTypeHeader contentType_others = null;
		Response response = null;
		Request request = null;

		StringBuilder txtBuf = new StringBuilder();
		StringBuilder xmlBuf = new StringBuilder();
		StringBuilder sdpBuf = new StringBuilder();
		StringBuilder othersBuf = new StringBuilder();

		List<com.mitester.jaxbparser.server.Header> sdpheader = null;
		SDPBODY sdpBody = content.getSDPBODY();

		sdpheader = sdpBody.getHeader();

		List<Sdp> sdp = sdpBody.getSdp();

		for (Sdp objSdp : sdp) {
			if (objSdp instanceof Sdp) {
				sdpBuf.append(objSdp.getName() + EQUALS + objSdp.getValue()
						+ NEWLINE);
			}
		}
		if (sdpBody.getFile() != null) {

			sdpBuf = processSdpFile(sdpBody);
		}

		List<com.mitester.jaxbparser.server.Header> txtheader = null;
		TXTBODY txtBody = content.getTXTBODY();
		if (txtBody != null) {
			txtheader = txtBody.getHeader();

			if (txtBody.getFile() != null) {

				txtBuf = processTXTFile(txtBody);

			}
		}
		List<com.mitester.jaxbparser.server.Header> xmlheader = null;
		XMLBODY xmlBody = content.getXMLBODY();
		if (xmlBody != null) {
			xmlheader = xmlBody.getHeader();

			if (xmlBody.getFile() != null) {
				xmlBuf = processXMLFile(xmlBody);

			}
		}
		List<com.mitester.jaxbparser.server.Header> othersheader = null;
		OTHERSBODY othersBody = content.getOTHERSBODY();
		if (othersBody != null) {
			othersheader = othersBody.getHeader();

			if (othersBody.getFile() != null) {
				othersBuf = processOthersFile(othersBody);
			}
		}

		if (Type.equalsIgnoreCase(SipServerConstants.SERVER_RESPONSE)) {
			SIPMessage sipmsgconvert = SipParser.parseSipMessage(sipMessage);
			response = (Response) sipmsgconvert;
		} else {
			SIPMessage sipmsgconvert = SipParser.parseSipMessage(sipMessage);
			request = (Request) sipmsgconvert;
		}
		if (Type.equals(SipServerConstants.SERVER_RESPONSE)) {

			ContentTypeHeader contentType = (ContentTypeHeader) response
					.getHeader(ContentTypeHeader.NAME);
			if (contentType != null) {
				String ctype = contentType.toString();
				String ctypearray[] = ctype.split(SEMICOLON);
				if (ctypearray.length > 1) {
					int index = ctypearray[1].indexOf(EQUALS);
					String boundaryValue = ctypearray[1].substring(index + 1,
							ctypearray[1].length());
					boundaryValue = boundaryValue.trim();
					if (sdpheader != null) {
						contentType_sdp = createSDPContentTypeHeader(sdpheader,
								contentType_sdp, headerFactory, Type);
					}
					if (txtheader != null) {
						contentType_txt = createTXTContentTypeHeader(txtheader,
								contentType_txt, headerFactory, Type);
					}
					if (xmlheader != null) {
						contentType_xml = createXMLContentTypeHeader(xmlheader,
								contentType_xml, headerFactory, Type);
					}
					if (othersheader != null) {
						contentType_others = createOthersContentTypeHeader(
								othersheader, contentType_others,
								headerFactory, Type);
					}

					StringBuilder stringBuilder = new StringBuilder();

					if (contentType_sdp != null) {
						stringBuilder = addContentHelper(stringBuilder,
								boundaryValue, contentType_sdp, sdpBuf);
					}
					if (contentType_txt != null) {
						stringBuilder = addContentHelper(stringBuilder,
								boundaryValue, contentType_txt, txtBuf);
					}
					if (contentType_xml != null) {
						stringBuilder = addContentHelper(stringBuilder,
								boundaryValue, contentType_xml, xmlBuf);
					}
					if (contentType_others != null) {
						stringBuilder = addContentHelper(stringBuilder,
								boundaryValue, contentType_others, othersBuf);
					}
					stringBuilder.append(DOUBLE_HYPHON + boundaryValue
							+ DOUBLE_HYPHON);
					stringBuilder.append(NEWLINE);
					response.setContent(stringBuilder, contentType);

				} else {
					if (sdpBuf.length() != 0)
						response.setContent(sdpBuf, contentType);
					else if (xmlBuf.length() != 0) {
						String xml = xmlBuf.toString();
						xml = xml.trim();
						response.setContent(xml, contentType);
					} else if (txtBuf.length() != 0)
						response.setContent(txtBuf, contentType);
					else if (othersBuf.length() != 0)
						response.setContent(othersBuf, contentType);

				}

			} else if (contentType == null) {
				if (sdpheader != null) {
					contentType_sdp = createSDPContentTypeHeader(sdpheader,
							contentType_sdp, headerFactory, Type);
				}
				if (txtheader != null) {
					contentType_txt = createTXTContentTypeHeader(txtheader,
							contentType_txt, headerFactory, Type);
				}

				if (xmlheader != null) {
					contentType_xml = createXMLContentTypeHeader(xmlheader,
							contentType_xml, headerFactory, Type);
				}
				if (othersheader != null) {
					contentType_others = createOthersContentTypeHeader(
							othersheader, contentType_others, headerFactory,
							Type);
				}
				if (contentType_sdp != null) {
					response.setContent(sdpBuf.toString(), contentType_sdp);
				} else if (contentType_xml != null) {
					String xml = xmlBuf.toString();
					xml = xml.trim();
					response.setContent(xml.toString(), contentType_xml);
				} else if (contentType_txt != null) {
					response.setContent(txtBuf.toString(), contentType_txt);
				} else if (contentType_others != null) {
					response.setContent(othersBuf.toString(),
							contentType_others);
				} else {
					LOGGER.info("Adding Content without Content-Type Header in SIP Message");
					ContentTypeHeader c = headerFactory
							.createContentTypeHeader(APPLICATION, SDP);

					if (sdpBuf.length() != 0)
						response.setContent(sdpBuf.toString(), c);
					else if (xmlBuf.length() != 0) {
						String xml = xmlBuf.toString();
						xml = xml.trim();
						response.setContent(xml.toString(), c);
					} else if (txtBuf.length() != 0)
						response.setContent(txtBuf.toString(), c);
					else if (othersBuf.length() != 0)
						response.setContent(othersBuf.toString(), c);
					response.removeHeader(ContentTypeHeader.NAME);
				}
			}
		} else if (Type.equals(SipServerConstants.SERVER_REQUEST)) {

			ContentTypeHeader contentType1 = (ContentTypeHeader) request
					.getHeader(ContentTypeHeader.NAME);
			if (contentType1 != null) {
				String ctype = contentType1.toString();
				String ctypearray[] = ctype.split(SEMICOLON);
				if (ctypearray.length > 1) {
					int index = ctypearray[1].indexOf(EQUALS);

					String boundaryValue = ctypearray[1].substring(index + 1,
							ctypearray[1].length());
					boundaryValue = boundaryValue.trim();
					if (sdpheader != null) {
						contentType_sdp = createSDPContentTypeHeader(sdpheader,
								contentType_sdp, headerFactory, Type);
					}
					if (txtheader != null) {
						contentType_txt = createTXTContentTypeHeader(txtheader,
								contentType_txt, headerFactory, Type);
					}

					if (xmlheader != null) {
						contentType_xml = createXMLContentTypeHeader(xmlheader,
								contentType_xml, headerFactory, Type);
					}
					if (othersheader != null) {
						contentType_others = createOthersContentTypeHeader(
								othersheader, contentType_others,
								headerFactory, Type);
					}
					StringBuilder stringBuilder = new StringBuilder();
					if (contentType_sdp != null) {
						stringBuilder = addContentHelper(stringBuilder,
								boundaryValue, contentType_sdp, sdpBuf);
					}
					if (contentType_txt != null) {
						stringBuilder = addContentHelper(stringBuilder,
								boundaryValue, contentType_txt, txtBuf);
					}
					if (contentType_xml != null) {
						stringBuilder = addContentHelper(stringBuilder,
								boundaryValue, contentType_xml, xmlBuf);
					}
					if (contentType_others != null) {
						stringBuilder = addContentHelper(stringBuilder,
								boundaryValue, contentType_others, othersBuf);
					}
					stringBuilder.append(DOUBLE_HYPHON + boundaryValue
							+ DOUBLE_HYPHON);
					stringBuilder.append(NEWLINE);
					request.setContent(stringBuilder, contentType1);
				} else {
					if (sdpBuf.length() != 0)
						request.setContent(sdpBuf, contentType1);
					else if (xmlBuf.length() != 0) {
						String xml = xmlBuf.toString();
						xml = xml.trim();
						request.setContent(xml, contentType1);
					} else if (txtBuf.length() != 0)
						request.setContent(txtBuf, contentType1);
					else if (othersBuf.length() != 0)
						request.setContent(othersBuf, contentType1);
				}
			} else {
				if (sdpheader != null) {
					contentType_sdp = createSDPContentTypeHeader(sdpheader,
							contentType_sdp, headerFactory, Type);
				}
				if (txtheader != null) {
					contentType_txt = createTXTContentTypeHeader(txtheader,
							contentType_txt, headerFactory, Type);
				}

				if (xmlheader != null) {
					contentType_xml = createXMLContentTypeHeader(xmlheader,
							contentType_xml, headerFactory, Type);
				}
				if (othersheader != null) {
					contentType_others = createOthersContentTypeHeader(
							othersheader, contentType_others, headerFactory,
							Type);
				}
				if (contentType_sdp != null) {
					request.setContent(sdpBuf.toString(), contentType_sdp);
				} else if (contentType_xml != null) {
					String xml = xmlBuf.toString();
					xml = xml.trim();
					request.setContent(xml.toString(), contentType_xml);
				} else if (contentType_txt != null) {
					request.setContent(txtBuf.toString(), contentType_txt);
				} else if (contentType_others != null) {
					request
							.setContent(othersBuf.toString(),
									contentType_others);
				} else {
					LOGGER.info("Adding Content without Content-Type Header in SIP Message");
					ContentTypeHeader c = headerFactory
							.createContentTypeHeader(APPLICATION, SDP);

					if (sdpBuf.length() != 0)
						request.setContent(sdpBuf.toString(), c);
					else if (xmlBuf.length() != 0) {
						String xml = xmlBuf.toString();
						xml = xml.trim();
						request.setContent(xml.toString(), c);
					} else if (txtBuf.length() != 0)
						request.setContent(txtBuf.toString(), c);
					else if (othersBuf.length() != 0)
						request.setContent(othersBuf.toString(), c);
					request.removeHeader(ContentTypeHeader.NAME);
				}
			}
		}
		// adding mis-spelt content type header to the sip message
		if (misspeltCT) {
			if (Type.equals(SipServerConstants.SERVER_RESPONSE)) {
				response.removeHeader(ContentTypeHeader.NAME);
				if (sdpmisspelt != null)
					response.addHeader(sdpmisspelt);
				if (txtmissplet != null)
					response.addHeader(txtmissplet);
				if (xmlmissplet != null)
					response.addHeader(xmlmissplet);
				if (othersmissplet != null)
					response.addHeader(othersmissplet);
			} else {
				request.removeHeader(ContentTypeHeader.NAME);
				if (sdpmisspelt != null)
					request.addHeader(sdpmisspelt);
				if (txtmissplet != null)
					request.addHeader(txtmissplet);
				if (xmlmissplet != null)
					request.addHeader(xmlmissplet);
				if (othersmissplet != null)
					request.addHeader(othersmissplet);
			}
		}

		if (Type.equals(SipServerConstants.SERVER_RESPONSE))
			returnIPMessage = (SIPMessage) response;
		else
			returnIPMessage = (SIPMessage) request;
		LOGGER.info("Adding Content to the SIP Message is ended");
		return returnIPMessage;

	}

	/**
	 * It's helper method to add a content to the sip message
	 * 
	 * @param stringBuilder consists of content
	 * @param paramValu
	 * @param contentType
	 * @return
	 */
	public static StringBuilder addContentHelper(StringBuilder stringBuilder,
			String paramValu, ContentTypeHeader contentType,
			StringBuilder buffer) {

		stringBuilder.append(DOUBLE_HYPHON + paramValu).append(NEWLINE).append(
				contentType).append(CONTENT_LENGTH + buffer.length()).append(
				NEWLINE + NEWLINE).append(buffer).append(NEWLINE + NEWLINE);

		return stringBuilder;
	}

	public static ContentTypeHeader createSDPContentTypeHeader(
			List<com.mitester.jaxbparser.server.Header> sdpheader,
			ContentTypeHeader contentType_sdp, HeaderFactory headerFactory,
			String type) throws ParseException, IndexOutOfBoundsException,
			SipException, InvalidArgumentException {
		for (com.mitester.jaxbparser.server.Header sdpHeader : sdpheader) {
			if (SIPHeaders.getSipHeaderfromString(sdpHeader.getName()).equals(
					SIPHeaders.CONTENT_TYPE)) {
				String array[] = sdpHeader.getValue().split(
						CONTENT_TYPE_SEPARATOR);
				contentType_sdp = headerFactory.createContentTypeHeader(
						array[0], array[1]);
				contentType_sdp = addParameteToContentTypeHeader(
						contentType_sdp, sdpHeader);
			} else {
				LOGGER
						.info("Given Content-Type header in SDP-BODY content is Mis-splet");
				sdpmisspelt = CustomHeaderHandler.createCustomHeader(sdpHeader);
				String array[] = sdpHeader.getValue().split(
						CONTENT_TYPE_SEPARATOR);
				contentType_sdp = headerFactory.createContentTypeHeader(
						array[0], array[1]);
				contentType_sdp = addParameteToContentTypeHeader(
						contentType_sdp, sdpHeader);
				misspeltCT = true;
			}
		}
		return contentType_sdp;
	}

	public static ContentTypeHeader createTXTContentTypeHeader(
			List<com.mitester.jaxbparser.server.Header> txtheader,
			ContentTypeHeader contentType_txt, HeaderFactory headerFactory,
			String type) throws ParseException, IndexOutOfBoundsException,
			SipException, InvalidArgumentException {
		for (com.mitester.jaxbparser.server.Header txtHeader : txtheader) {
			if (SIPHeaders.getSipHeaderfromString(txtHeader.getName()).equals(
					SIPHeaders.CONTENT_TYPE)) {
				String array[] = txtHeader.getValue().split(
						CONTENT_TYPE_SEPARATOR);
				contentType_txt = headerFactory.createContentTypeHeader(
						array[0], array[1]);
				contentType_txt = addParameteToContentTypeHeader(
						contentType_txt, txtHeader);
			} else {
				LOGGER
						.info("Given Content-Type header in TEXT-BODY tag is Mis-splet");
				txtmissplet = CustomHeaderHandler.createCustomHeader(txtHeader);
				String array[] = txtHeader.getValue().split(
						CONTENT_TYPE_SEPARATOR);
				contentType_txt = headerFactory.createContentTypeHeader(
						array[0], array[1]);
				contentType_txt = addParameteToContentTypeHeader(
						contentType_txt, txtHeader);
				misspeltCT = true;
			}
		}
		return contentType_txt;
	}

	public static ContentTypeHeader createXMLContentTypeHeader(
			List<com.mitester.jaxbparser.server.Header> xmlheader,
			ContentTypeHeader contentType_xml, HeaderFactory headerFactory,
			String type) throws ParseException, IndexOutOfBoundsException,
			SipException, InvalidArgumentException {
		for (com.mitester.jaxbparser.server.Header xmlHeader : xmlheader) {
			if (SIPHeaders.getSipHeaderfromString(xmlHeader.getName()).equals(
					SIPHeaders.CONTENT_TYPE)) {
				String array[] = xmlHeader.getValue().split(
						CONTENT_TYPE_SEPARATOR);
				contentType_xml = headerFactory.createContentTypeHeader(
						array[0], array[1]);
				contentType_xml = addParameteToContentTypeHeader(
						contentType_xml, xmlHeader);
			} else {
				LOGGER
						.info("Given Content-Type header in XML-BODY tag is Mis-splet");
				xmlmissplet = CustomHeaderHandler.createCustomHeader(xmlHeader);
				String array[] = xmlHeader.getValue().split(
						CONTENT_TYPE_SEPARATOR);
				contentType_xml = headerFactory.createContentTypeHeader(
						array[0], array[1]);
				contentType_xml = addParameteToContentTypeHeader(
						contentType_xml, xmlHeader);
				misspeltCT = true;
			}
		}
		return contentType_xml;
	}

	public static ContentTypeHeader createOthersContentTypeHeader(
			List<com.mitester.jaxbparser.server.Header> othersheader2,
			ContentTypeHeader contentType_others, HeaderFactory headerFactory,
			String type) throws ParseException, IndexOutOfBoundsException,
			SipException, InvalidArgumentException {
		for (com.mitester.jaxbparser.server.Header othersHeader : othersheader2) {
			if (SIPHeaders.getSipHeaderfromString(othersHeader.getName())
					.equals(SIPHeaders.CONTENT_TYPE)) {
				String array[] = othersHeader.getValue().split(
						CONTENT_TYPE_SEPARATOR);
				contentType_others = headerFactory.createContentTypeHeader(
						array[0], array[1]);
				contentType_others = addParameteToContentTypeHeader(
						contentType_others, othersHeader);
			} else {
				LOGGER
						.info("Given Content-Type header in OTHERS-BODY tag is Mis-splet");
				othersmissplet = CustomHeaderHandler
						.createCustomHeader(othersHeader);
				String array[] = othersHeader.getValue().split(
						CONTENT_TYPE_SEPARATOR);
				contentType_others = headerFactory.createContentTypeHeader(
						array[0], array[1]);
				contentType_others = addParameteToContentTypeHeader(
						contentType_others, othersHeader);
				misspeltCT = true;
			}
		}
		return contentType_others;
	}

	/**
	 * adding parameter to content type header
	 * 
	 * @param contentType is a content type header object
	 * @param sdpHeader consists of the content-type headers going to be added with sip message
	 * @return the Content-Type Header
	 * @throws ParseException
	 */
	public static ContentTypeHeader addParameteToContentTypeHeader(
			ContentTypeHeader contentType,
			com.mitester.jaxbparser.server.Header sdpHeader2)
			throws ParseException {
		LOGGER.info("Adding parameter to the Content-Type header");
		List<Param> param = sdpHeader2.getParam();

		for (Param parameter : param) {
			String pname = parameter.getName();
			String pvalue = parameter.getValue();
			contentType.setParameter(pname, pvalue);
		}

		return contentType;
	}

	/**
	 * processFileSdP is used to process sdp body inside a file
	 * 
	 * @param sdpBody
	 * @return String buffer has a sdp content
	 * @throws IOException
	 * @throws NullPointerException
	 * 
	 */
	private static StringBuilder processSdpFile(SDPBODY sdpBody)
			throws NullPointerException, IOException {
		LOGGER.info("Processing SDP File is started");
		String filePath;
		StringBuilder sdpBuf = new StringBuilder();
		String fileName = sdpBody.getFile().getSource();

		ParseSDPBody parseSdpBody = new ParseSDPBody();

		filePath = serverScriptPath + FILE_SEPARATOR + CONTENT_FILE + FILE_SEPARATOR + fileName;
		List<com.mitester.jaxbparser.sdpbody.Sdp> newSdp;
		try {
			newSdp = parseSdpBody.ParseSDPBodyFile(filePath);

			for (com.mitester.jaxbparser.sdpbody.Sdp objNewSDP : newSdp) {
				if (objNewSDP instanceof Object) {
					sdpBuf.append(objNewSDP.getName() + EQUALS
							+ objNewSDP.getValue() + NEWLINE);
				}

			}
		} catch (JAXBException e) {

		}
		LOGGER.info("Processing SDP File is ended");
		return sdpBuf;
	}

	private static StringBuilder processTXTFile(TXTBODY txtBody)
			throws NullPointerException, IOException {
		LOGGER.info("Processing TEXT File is started");
		StringBuilder txtBuf = new StringBuilder();
		com.mitester.jaxbparser.server.File file = txtBody.getFile();
		String fileName = file.getSource();
		String newFileName = serverScriptPath + FILE_SEPARATOR + CONTENT_FILE + FILE_SEPARATOR + fileName;		 
		FileReader readFile = new FileReader(newFileName);
		BufferedReader readBuf = new BufferedReader(readFile);

		String lineRead = null;

		while ((lineRead = readBuf.readLine()) != null) {
			lineRead = lineRead.trim();
			if (lineRead.startsWith(SERVER_PARSING_ACTION_START_TEXT_SEPARATOR))
				continue;
			else if (lineRead
					.startsWith(SERVER_PARSING_ACTION_END_TEXT_SEPARATOR))
				break;
			else if (lineRead
					.startsWith(SERVER_PARSING_ACTION_START_INNER_TXT_SEPARATOR)) {
				String splitArray[] = lineRead.split(EQUALS);
				for (int k = 0; k < splitArray.length; k++) {
					if (splitArray[k]
							.startsWith(SERVER_PARSING_ACTION_START_INNER_TXT_SEPARATOR)) {
						continue;
					} else {
						String contents[] = splitArray[k]
								.split(SERVER_PARSING_ACTION_ANGLE_SEPARATOR);

						for (int l = 0; l < contents.length; l++) {
							String[] textContent = contents[l].split("'");
							txtBuf.append(textContent[1]);
						}
						if (k != splitArray.length - 1)
							txtBuf.append(EQUALS);
					}
				}
				txtBuf.append(NEWLINE);
			}
		}

		LOGGER.info("Processing TEXT File is ended");
		return txtBuf;
	}

	private static StringBuilder processXMLFile(XMLBODY xmlBody)
			throws NullPointerException, IOException {
		LOGGER.info("Processing XML File is started");
		StringBuilder xmlBuf = new StringBuilder();
		String xml = null;

		com.mitester.jaxbparser.server.File file = xmlBody.getFile();
		String fileName = file.getSource();
		String newFileName = serverScriptPath + FILE_SEPARATOR + CONTENT_FILE + FILE_SEPARATOR + fileName;
		InputStream br = new FileInputStream(newFileName);
		int size = br.available();
		byte b[] = new byte[size + 1];
		br.read(b);
		xml = new String(b);
		xmlBuf.append(xml);

		LOGGER.info("Processing XML File is ended");
		return xmlBuf;
	}

	private static StringBuilder processOthersFile(OTHERSBODY othersBody)
			throws NullPointerException, IOException {
		LOGGER.info("Processing OTHERS File is started");
		StringBuilder othersBuf = new StringBuilder();
		String s;
		com.mitester.jaxbparser.server.File file = othersBody.getFile();
		String fileName = file.getSource();

		String newFileName = serverScriptPath + FILE_SEPARATOR + CONTENT_FILE + FILE_SEPARATOR + fileName;
		InputStream br = new FileInputStream(newFileName);
		int size = br.available();
		byte b[] = new byte[size + 1];
		br.read(b);
		s = new String(b);
		othersBuf.append(s);

		LOGGER.info("Processing OTHERS File is ended");
		return othersBuf;
	}

	public static void setIsMisspelt() {
		misspeltCT = false;
	}
}