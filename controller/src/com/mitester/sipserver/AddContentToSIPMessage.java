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
 * Package 				License 										    Details
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

import gov.nist.javax.sip.message.SIPMessage;

import java.io.BufferedReader;
import java.io.File;
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
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.xml.bind.JAXBException;

import com.mitester.jaxbparser.sdpbody.ParseSDPBody;
import com.mitester.jaxbparser.server.CONTENT;
import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.OTHERSBODY;
import com.mitester.jaxbparser.server.Param;
import com.mitester.jaxbparser.server.SDPBODY;
import com.mitester.jaxbparser.server.Sdp;
import com.mitester.jaxbparser.server.TXTBODY;
import com.mitester.jaxbparser.server.XMLBODY;
import com.mitester.utility.ConfigurationProperties;
import com.mitester.utility.TestUtility;

/**
 * addContentToSIPMessage is used to add content to the sip message
 * 
 */

public class AddContentToSIPMessage {

	private static final String NEWLINE = "\r\n";
	private static final String DOUBLE_HYPHON = "--";
	private static final String CONTENT_TYPE_SEPARATOR = "/";
	private static final String CONTENT_LENGTH = "Content-Length: ";
	private static final String APPLICATION = "application";
	private static final String SDP = "sdp";
	private static final String SEMICOLON = ";";
	private static final String EQUALS = "=";
	private static final String ADD_FILES = "/";
	private static final String SERVER_PARSING_ACTION_ANGLE_SEPARATOR = "/>";
	private static final String SERVER_PARSING_ACTION_END_TEXT_SEPARATOR = "</TEXT>";
	private static final String SERVER_PARSING_ACTION_START_TEXT_SEPARATOR = "<TEXT>";
	private static final String SERVER_PARSING_ACTION_START_INNER_TXT_SEPARATOR = "<txt";
	private static final String serverScriptPath = ConfigurationProperties.CONFIG_INSTANCE
	        .getValue("SERVER_SCRIPT_PATH");

	/**
	 * This method is used to add a content to the SIP Message
	 * 
	 * @param Type
	 * @param sipMessage
	 * @param content
	 * @return SIPMessage
	 * @throws SipException
	 * @throws InvalidArgumentException
	 * @throws ParseException
	 * @throws IOException
	 */
	public static SIPMessage setSDPContent(String Type, String sipMessage,
	        CONTENT content) throws SipException, InvalidArgumentException,
	        ParseException, IOException {

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

		List<Header> sdpheader = null;
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

		List<Header> txtheader = null;
		TXTBODY txtBody = content.getTXTBODY();
		if (txtBody != null) {
			txtheader = txtBody.getHeader();

			if (txtBody.getFile() != null) {

				txtBuf = processTXTFile(txtBody);

			}
		}
		List<Header> xmlheader = null;
		XMLBODY xmlBody = content.getXMLBODY();
		if (xmlBody != null) {
			xmlheader = xmlBody.getHeader();

			if (xmlBody.getFile() != null) {
				xmlBuf = processXMLFile(xmlBody);

			}
		}
		List<Header> othersheader = null;
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
						for (Header sdpHeader : sdpheader) {
							String array[] = sdpHeader.getValue().split(
							        CONTENT_TYPE_SEPARATOR);
							contentType_sdp = headerFactory
							        .createContentTypeHeader(array[0], array[1]);
							contentType_sdp = addParameteToContentTypeHeader(
							        contentType_sdp, sdpHeader);
						}
					}
					if (txtheader != null) {
						for (Header txtHeader : txtheader) {
							String array[] = txtHeader.getValue().split(
							        CONTENT_TYPE_SEPARATOR);
							contentType_txt = headerFactory
							        .createContentTypeHeader(array[0], array[1]);
							contentType_txt = addParameteToContentTypeHeader(
							        contentType_txt, txtHeader);
						}
					}

					if (xmlheader != null) {
						for (Header txtHeader : txtheader) {
							String xmlarray[] = txtHeader.getValue().split(
							        CONTENT_TYPE_SEPARATOR);
							contentType_xml = headerFactory
							        .createContentTypeHeader(xmlarray[0],
							                xmlarray[1]);
							contentType_xml = addParameteToContentTypeHeader(
							        contentType_xml, txtHeader);
						}
					}
					if (othersheader != null) {
						for (Header othersHeader : othersheader) {
							String othersarray[] = othersHeader.getValue()
							        .split(CONTENT_TYPE_SEPARATOR);
							contentType_others = headerFactory
							        .createContentTypeHeader(othersarray[0],
							                othersarray[1]);
							contentType_others = addParameteToContentTypeHeader(
							        contentType_others, othersHeader);
						}
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
					for (Header sdpHeader : sdpheader) {
						String array[] = sdpHeader.getValue().split(
						        CONTENT_TYPE_SEPARATOR);
						contentType_sdp = headerFactory
						        .createContentTypeHeader(array[0], array[1]);
						contentType_sdp = addParameteToContentTypeHeader(
						        contentType_sdp, sdpHeader);
					}
				}
				if (txtheader != null) {
					for (Header txtHeader : txtheader) {
						String array[] = txtHeader.getValue().split(
						        CONTENT_TYPE_SEPARATOR);
						contentType_txt = headerFactory
						        .createContentTypeHeader(array[0], array[1]);
						contentType_txt = addParameteToContentTypeHeader(
						        contentType_txt, txtHeader);
					}
				}

				if (xmlheader != null) {
					for (Header txtHeader : txtheader) {
						String xmlarray[] = txtHeader.getValue().split(
						        CONTENT_TYPE_SEPARATOR);
						contentType_xml = headerFactory
						        .createContentTypeHeader(xmlarray[0],
						                xmlarray[1]);
						contentType_xml = addParameteToContentTypeHeader(
						        contentType_xml, txtHeader);
					}
				}
				if (othersheader != null) {
					for (Header othersHeader : othersheader) {
						String othersarray[] = othersHeader.getValue().split(
						        CONTENT_TYPE_SEPARATOR);
						contentType_others = headerFactory
						        .createContentTypeHeader(othersarray[0],
						                othersarray[1]);
						contentType_others = addParameteToContentTypeHeader(
						        contentType_others, othersHeader);
					}
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
						for (Header sdpHeader : sdpheader) {
							String array[] = sdpHeader.getValue().split(
							        CONTENT_TYPE_SEPARATOR);
							contentType_sdp = headerFactory
							        .createContentTypeHeader(array[0], array[1]);
							contentType_sdp = addParameteToContentTypeHeader(
							        contentType_sdp, sdpHeader);
						}
					}
					if (txtheader != null) {
						for (Header txtHeader : txtheader) {
							String array[] = txtHeader.getValue().split(
							        CONTENT_TYPE_SEPARATOR);
							contentType_txt = headerFactory
							        .createContentTypeHeader(array[0], array[1]);
							contentType_txt = addParameteToContentTypeHeader(
							        contentType_txt, txtHeader);
						}
					}

					if (xmlheader != null) {
						for (Header xmlHeader : xmlheader) {
							String xmlarray[] = xmlHeader.getValue().split(
							        CONTENT_TYPE_SEPARATOR);
							contentType_xml = headerFactory
							        .createContentTypeHeader(xmlarray[0],
							                xmlarray[1]);
							contentType_xml = addParameteToContentTypeHeader(
							        contentType_xml, xmlHeader);
						}
					}
					if (othersheader != null) {
						for (Header othersHeader : othersheader) {
							String othersarray[] = othersHeader.getValue()
							        .split(CONTENT_TYPE_SEPARATOR);
							contentType_others = headerFactory
							        .createContentTypeHeader(othersarray[0],
							                othersarray[1]);
							contentType_others = addParameteToContentTypeHeader(
							        contentType_others, othersHeader);
						}
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
					for (Header sdpHeader : sdpheader) {
						String array[] = sdpHeader.getValue().split(
						        CONTENT_TYPE_SEPARATOR);
						contentType_sdp = headerFactory
						        .createContentTypeHeader(array[0], array[1]);
						contentType_sdp = addParameteToContentTypeHeader(
						        contentType_sdp, sdpHeader);
					}
				}
				if (txtheader != null) {
					for (Header txtHeader : txtheader) {
						String array[] = txtHeader.getValue().split(
						        CONTENT_TYPE_SEPARATOR);
						contentType_txt = headerFactory
						        .createContentTypeHeader(array[0], array[1]);
						contentType_txt = addParameteToContentTypeHeader(
						        contentType_txt, txtHeader);
					}
				}

				if (xmlheader != null) {
					for (Header txtHeader : xmlheader) {
						String xmlarray[] = txtHeader.getValue().split(
						        CONTENT_TYPE_SEPARATOR);
						contentType_xml = headerFactory
						        .createContentTypeHeader(xmlarray[0],
						                xmlarray[1]);
						contentType_xml = addParameteToContentTypeHeader(
						        contentType_xml, txtHeader);
					}
				}
				if (othersheader != null) {
					for (Header othersHeader : othersheader) {
						String othersarray[] = othersHeader.getValue().split(
						        CONTENT_TYPE_SEPARATOR);
						contentType_others = headerFactory
						        .createContentTypeHeader(othersarray[0],
						                othersarray[1]);
						contentType_others = addParameteToContentTypeHeader(
						        contentType_others, othersHeader);
					}
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
		if (Type.equals(SipServerConstants.SERVER_RESPONSE))
			returnIPMessage = (SIPMessage) response;
		else
			returnIPMessage = (SIPMessage) request;

		return returnIPMessage;

	}

	/**
	 * this is helper method to add a content to the sip message
	 * 
	 * @param stringBuilder
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

	/**
	 * adding parameter to content type header
	 * 
	 * @param contentType
	 * @param sdpHeader
	 * @return
	 * @throws ParseException
	 */
	public static ContentTypeHeader addParameteToContentTypeHeader(
	        ContentTypeHeader contentType, Header header) throws ParseException {
		List<Param> param = header.getParam();

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
	 */
	private static StringBuilder processSdpFile(SDPBODY sdpBody) {
		String filePath;
		StringBuilder sdpBuf = new StringBuilder();
		String fileName = sdpBody.getFile().getSource();

		ParseSDPBody parseSdpBody = new ParseSDPBody();
		try {
			filePath = FileParsingContent(serverScriptPath, fileName);
			List<com.mitester.jaxbparser.sdpbody.Sdp> newSdp = parseSdpBody
			        .ParseSDPBodyFile(filePath);
			for (com.mitester.jaxbparser.sdpbody.Sdp objNewSDP : newSdp) {
				if (objNewSDP instanceof Object) {
					sdpBuf.append(objNewSDP.getName() + EQUALS
					        + objNewSDP.getValue() + NEWLINE);
				}

			}
		} catch (NullPointerException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return sdpBuf;
	}

	private static StringBuilder processTXTFile(TXTBODY txtBody) {
		StringBuilder txtBuf = new StringBuilder();
		com.mitester.jaxbparser.server.File file = txtBody.getFile();
		String fileName = file.getSource();
		try {
			String newFileName = FileParsingContent(serverScriptPath, fileName);
			FileReader readFile = new FileReader(newFileName);
			BufferedReader readBuf = new BufferedReader(readFile);

			String lineRead = null;

			while ((lineRead = readBuf.readLine()) != null) {
				lineRead = lineRead.trim();
				if (lineRead
				        .startsWith(SERVER_PARSING_ACTION_START_TEXT_SEPARATOR))
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
		} catch (Exception e) {

		}
		return txtBuf;
	}

	private static StringBuilder processXMLFile(XMLBODY xmlBody) {
		StringBuilder xmlBuf = new StringBuilder();
		String xml = null;
		try {
			com.mitester.jaxbparser.server.File file = xmlBody.getFile();
			String fileName = file.getSource();
			String newFileName = FileParsingContent(serverScriptPath, fileName);
			InputStream br = new FileInputStream(newFileName);
			int size = br.available();
			byte b[] = new byte[size + 1];
			br.read(b);
			xml = new String(b);
			xmlBuf.append(xml);
		} catch (Exception e) {

		}
		return xmlBuf;
	}

	/**
	 * searching the content files
	 * 
	 * @param serverScriptPath
	 * @param contentFileName
	 * @return String
	 * @throws ParseException
	 */
	public static String FileParsingContent(String serverScriptPath,
	        String contentFileName) throws IOException, NullPointerException {

		File directoryPath = new File(serverScriptPath);

		String testString = null;

		String contentNewPath = null;

		String ScriptChildren[] = directoryPath.list();

		if (ScriptChildren == null) {
			TestUtility.printMessage("There is no file exist");
		}

		for (String script : ScriptChildren) {
			String newContentPath = serverScriptPath.concat(ADD_FILES + script);

			if (script.equalsIgnoreCase("Content_Files")) {
				testString = newContentPath + ADD_FILES + contentFileName;
				File newFile = new File(testString);
				if (newFile.isFile())
					return testString;
				else
					testString = null;
			} else if (new File(newContentPath).isDirectory()) {
				contentNewPath = FileParsingContent(newContentPath,
				        contentFileName);
				if (contentNewPath.endsWith(contentFileName))
					break;
				ScriptChildren = directoryPath.list();
			}
		}
		return contentNewPath;
	}

	private static StringBuilder processOthersFile(OTHERSBODY othersBody) {
		StringBuilder othersBuf = new StringBuilder();
		String s;
		com.mitester.jaxbparser.server.File file = othersBody.getFile();
		String fileName = file.getSource();
		try {
			String newFileName = FileParsingContent(serverScriptPath, fileName);
			InputStream br = new FileInputStream(newFileName);
			int size = br.available();
			byte b[] = new byte[size + 1];
			br.read(b);
			s = new String(b);
			othersBuf.append(s);
		} catch (Exception e) {

		}
		return othersBuf;
	}
}
