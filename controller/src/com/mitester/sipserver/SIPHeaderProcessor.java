/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: SIPHeaderProcessor.java
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

import gov.nist.javax.sip.message.SIPMessage;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;

import com.mitester.jaxbparser.server.ADDCRCR;
import com.mitester.jaxbparser.server.ADDCRLF;
import com.mitester.jaxbparser.server.ADDLFLF;
import com.mitester.jaxbparser.server.ADDMSG;
import com.mitester.jaxbparser.server.DELCR;
import com.mitester.jaxbparser.server.DELCRLF;
import com.mitester.jaxbparser.server.DELLF;
import com.mitester.jaxbparser.server.DELMSG;
import com.mitester.jaxbparser.server.DUPMSG;
import com.mitester.jaxbparser.server.Fline;
import com.mitester.jaxbparser.server.HEADERS;
import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.ReqLine;
import com.mitester.jaxbparser.server.SDPBODY;
import com.mitester.jaxbparser.server.Sdp;
import com.mitester.jaxbparser.server.StatusLine;
import com.mitester.sipserver.sipheaderhandler.FirstLineHandler;
import com.mitester.sipserver.sipheaderhandler.SIPHeaderHandler;
import com.mitester.sipserver.sipheaderhandler.SIPHeaderRemover;

public class SIPHeaderProcessor {

	public static final String REQUEST_LINE_SEPARATOR = "req-line";
	public static final String STATUS_LINE_SEPARATOR = "status-line";

	public static String processSipHeaders(
	        com.mitester.jaxbparser.server.ACTION action, String Type,
	        String SIPmessage) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException, IOException {

		String msg = SIPmessage;

		if (action.getADDMSG() != null) {
			msg = processAddMessage(action.getADDMSG(), msg, Type);
		}

		// del msg

		if (action.getDELMSG() != null) {
			msg = processDelMessage(action.getDELMSG(), msg, Type);
		}

		// Processing SIP Headers for Duplicate Message

		if (action.getDUPMSG() != null) {
			msg = processDupMessage(action.getDUPMSG(), msg, Type);
		}

		// processing CRCR
		if (action.getADDCRCR() != null) {
			msg = processAddCRCR(action.getADDCRCR(), msg, Type);
		}
		// Processing SIP Headers for Add LFLF Message

		if (action.getADDLFLF() != null) {
			msg = processAddLFLF(action.getADDLFLF(), msg, Type);
		}

		// Processing SIP Headers for Add CRLF Message

		if (action.getADDCRLF() != null) {
			msg = processAddCRLF(action.getADDCRLF(), msg, Type);
		}

		// Processing SIP Headers for Delete CR Message

		if (action.getDELCR() != null) {
			msg = processDelCR(action.getDELCR(), msg, Type);
		}

		// Processing SIP Headers for Delete LF Message

		if (action.getDELLF() != null) {
			msg = processDelLF(action.getDELLF(), msg, Type);
		}

		// Processing SIP Headers for Delete CRLF Message

		if (action.getDELCRLF() != null) {
			msg = processDelCRLF(action.getDELCRLF(), msg, Type);
		}

		List<String> empty = SIPHeaderHandler.getEmptyList();
		if ((empty != null) && (empty.size() != 0)) {
			msg = EmptyValueToHeader.addEmptyValueToHeader(empty, msg);
			SIPHeaderHandler.deiniEmptyList();
		}
		List<com.mitester.jaxbparser.server.Header> invalid = SIPHeaderHandler
		        .getInvalidList();
		if ((invalid != null) && (invalid.size() != 0)) {
			msg = InvalidValueToHeaders.addInvalidValuesToHeader(invalid, msg);
			SIPHeaderHandler.deiniInvalidList();
		}
		List<String> copyHeader = SIPHeaderHandler.getCopyList();
		if ((copyHeader != null) && (copyHeader.size() != 0)) {
			msg = CopyValuetoHeader.addCopyValueToHeader(copyHeader, msg);
			SIPHeaderHandler.deiniCopyList();
		}
		return msg;
	}

	/**
	 * processAddMessage is used to process ADD_MSG Tag in the server scenario
	 * 
	 * @param addMessage
	 * @param msg
	 * @param Type
	 * @return
	 * @throws IndexOutOfBoundsException
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 */
	private static String processAddMessage(ADDMSG addMessage, String msg,
	        String Type) throws IndexOutOfBoundsException, SipException,
	        ParseException, InvalidArgumentException, IOException {

		if (addMessage.getLINE() != null) {

			if (addMessage.getLINE().getFline() != null
			        || addMessage.getLINE().getFline().getName().toString()
			                .equals(REQUEST_LINE_SEPARATOR)) {
				List<ReqLine> reqLine = addMessage.getLINE().getReqLine();

				msg = FirstLineHandler.addRequestLine(msg, reqLine);

			}

			if (addMessage.getLINE().getFline() != null
			        || addMessage.getLINE().getFline().getName().toString()
			                .equals(STATUS_LINE_SEPARATOR)) {
				List<StatusLine> statusLine = addMessage.getLINE()
				        .getStatusLine();

				msg = FirstLineHandler.addStatusLine(msg, statusLine);

			}
		}
		if (addMessage.getHEADERS() != null) {
			HEADERS headers = addMessage.getHEADERS();
			SIPMessage sipmsg = null;
			if (headers != null) {
				List<Header> header = headers.getHeader();

				sipmsg = SIPHeaderHandler.addHeader(header, Type, msg);

			}
			msg = sipmsg.toString();
		}
		if (addMessage.getCONTENT() != null) {
			SIPMessage sipmsg = null;
			sipmsg = AddContentToSIPMessage.setSDPContent(Type, msg, addMessage
			        .getCONTENT());
			msg = sipmsg.toString();
		}
		return msg;
	}

	/**
	 * processDelMessage is used to process teh DEL_MSG Tag in server scenario
	 * 
	 * @param delMessage
	 * @param msg
	 * @param Type
	 * @return
	 * @throws IndexOutOfBoundsException
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 */
	private static String processDelMessage(DELMSG delMessage, String msg,
	        String Type) throws IndexOutOfBoundsException, SipException,
	        ParseException, InvalidArgumentException, IOException {

		if (delMessage.getLINE() != null) {
			msg = FirstLineHandler.removeFirstLine(msg);
		}
		if (delMessage.getHEADERS() != null) {
			HEADERS headers = delMessage.getHEADERS();
			if (headers != null) {
				List<Header> header = headers.getHeader();

				SIPMessage sipmsg = null;
				sipmsg = SIPHeaderRemover.removeHeader(header, msg, Type);
				msg = sipmsg.toString();

			}
		}
		return msg;
	}

	/**
	 * processAddCRCR is used to process ADD_CRCR Tag in server scenario
	 * 
	 * @param addCRCR
	 * @param msg
	 * @param Type
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 */
	private static String processAddCRCR(ADDCRCR addCRCR, String msg,
	        String Type) throws NullPointerException, IllegalArgumentException,
	        SipException, ParseException, InvalidArgumentException, IOException {


		if (addCRCR.getHEADERS() != null) {
			HEADERS headers = addCRCR.getHEADERS();
			if (headers != null) {
				List<Header> header = headers.getHeader();

				for (Header CRCRHeader : header)
					msg = CRLF_Handler.convertCRLFIntoCRCRByCount(CRCRHeader
					        .getName(), msg, Integer.parseInt(CRCRHeader
					        .getCount().toString()));

			}
		}
		if (addCRCR.getCONTENT() != null) {

			if (addCRCR.getCONTENT().getSDPBODY() != null) {
				SDPBODY sdpBody = addCRCR.getCONTENT().getSDPBODY();
				if (sdpBody != null) {
					List<Sdp> sdp = sdpBody.getSdp();

					for (Sdp objSdp : sdp) {
						if (objSdp instanceof Sdp) {
							msg = CRLF_Handler.convertCRLFIntoCRCRByCount(
							        objSdp.getName(), msg, Integer
							                .parseInt(objSdp.getCount()
							                        .toString()));
						}
					}
				}
			}
		}
		if (addCRCR.getLINE() != null) {

			if (addCRCR.getLINE().getFline() != null) {
				msg = CRLF_Handler.convertLineCRLFIntoCRCRByCount(addCRCR
				        .getLINE(), msg);
			}
		}
		return msg;
	}

	/**
	 * processAddLFLF is used to process ADD_LFLF tag in server scenario
	 * 
	 * @param addLFLF
	 * @param msg
	 * @param Type
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 */
	private static String processAddLFLF(ADDLFLF addLFLF, String msg,
	        String Type) throws NullPointerException, IllegalArgumentException,
	        SipException, ParseException, InvalidArgumentException, IOException {

		if (addLFLF.getHEADERS() != null) {
			HEADERS headers = addLFLF.getHEADERS();
			if (headers != null) {
				List<Header> header = headers.getHeader();

				for (Header LFLFHeader : header)
					msg = CRLF_Handler.convertCRLFIntoLFLFByCount(LFLFHeader
					        .getName(), msg, Integer.parseInt(LFLFHeader
					        .getCount().toString()));

			}
		}
		if (addLFLF.getCONTENT() != null) {

			if (addLFLF.getCONTENT().getSDPBODY() != null) {
				SDPBODY sdpBody = addLFLF.getCONTENT().getSDPBODY();
				if (sdpBody != null) {

					List<Sdp> sdp = sdpBody.getSdp();

					for (Sdp objSdp : sdp) {
						if (objSdp instanceof Sdp) {
							msg = CRLF_Handler.convertCRLFIntoLFLFByCount(
							        objSdp.getName(), msg, Integer
							                .parseInt(objSdp.getCount()
							                        .toString()));
						}
					}
				}
			}
		}
		if (addLFLF.getLINE() != null) {

			if (addLFLF.getLINE().getFline() != null) {
				if (addLFLF.getLINE() != null) {
					msg = CRLF_Handler.convertLineCRLFIntoLFLFByCount(addLFLF
					        .getLINE(), msg);
				}
			}
		}

		return msg;
	}

	/**
	 * processAddCRLF is used to process a ADD_CRLF Tag in the server scenario
	 * 
	 * @param addCRLF
	 * @param msg
	 * @param Type
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 */
	private static String processAddCRLF(ADDCRLF addCRLF, String msg,
	        String Type) throws NullPointerException, IllegalArgumentException,
	        SipException, ParseException, InvalidArgumentException, IOException {


		if (addCRLF.getHEADERS() != null) {
			HEADERS headers = addCRLF.getHEADERS();
			if (headers != null) {
				List<Header> header = headers.getHeader();

				for (Header DUPHeader : header)
					msg = CRLF_Handler.convertCRLFIntoCRLFByCount(DUPHeader
					        .getName(), msg, Integer.parseInt(DUPHeader
					        .getCount().toString()));

			}
		}
		if (addCRLF.getCONTENT() != null) {

			if (addCRLF.getCONTENT().getSDPBODY() != null) {
				SDPBODY sdpBody = addCRLF.getCONTENT().getSDPBODY();
				if (sdpBody != null) {
					List<Sdp> sdp = sdpBody.getSdp();

					for (Sdp objSdp : sdp) {
						if (objSdp instanceof Sdp) {
							msg = CRLF_Handler.convertCRLFIntoCRLFByCount(
							        objSdp.getName(), msg, Integer
							                .parseInt(objSdp.getCount()
							                        .toString()));
						}
					}

				}
			}
		}
		if (addCRLF.getLINE() != null) {

			if (addCRLF.getLINE().getFline() != null) {
				if (addCRLF.getLINE() != null) {
					msg = CRLF_Handler.convertLineCRLFIntoCRLFByCount(addCRLF
					        .getLINE(), msg);
				}
			}
		}
		return msg;
	}

	/**
	 * processDelCR is used to process a DEL_CR Tag in server scenario
	 * 
	 * @param DelCR
	 * @param msg
	 * @param Type
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 */

	private static String processDelCR(DELCR DelCR, String msg, String Type)
	        throws NullPointerException, IllegalArgumentException,
	        SipException, ParseException, InvalidArgumentException, IOException {

		if (DelCR.getHEADERS() != null) {
			HEADERS headers = DelCR.getHEADERS();
			if (headers != null) {
				List<Header> header = headers.getHeader();

				for (Header DELCRHeader : header)
					msg = CRLF_Handler.convertCRLFIntoCR(DELCRHeader.getName(),
					        msg);

			}
		}
		if (DelCR.getCONTENT() != null) {

			if (DelCR.getCONTENT().getSDPBODY() != null) {
				SDPBODY sdpBody = DelCR.getCONTENT().getSDPBODY();
				if (sdpBody != null) {
					List<Sdp> sdp = sdpBody.getSdp();

					for (Sdp objSdp : sdp) {
						if (objSdp instanceof Sdp) {
							msg = CRLF_Handler.convertCRLFIntoCR(objSdp
							        .getName(), msg);
						}

					}
				}
			}
		}
		if (DelCR.getLINE() != null) {

			if (DelCR.getLINE().getFline() != null) {
				if (DelCR.getLINE() != null) {
					msg = CRLF_Handler.convertLineCRLFIntoCR(DelCR.getLINE(),
					        msg);
				}
			}
		}
		return msg;
	}

	/**
	 * processDelLF is used to process DEL_LF Tag in server scenario
	 * 
	 * @param DelLF
	 * @param msg
	 * @param Type
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 */
	private static String processDelLF(DELLF DelLF, String msg, String Type)
	        throws NullPointerException, IllegalArgumentException,
	        SipException, ParseException, InvalidArgumentException, IOException {

		if (DelLF.getHEADERS() != null) {
			HEADERS headers = DelLF.getHEADERS();
			if (headers != null) {
				List<Header> header = headers.getHeader();

				for (Header DELLFHeader : header)
					msg = CRLF_Handler.convertCRLFIntoLF(DELLFHeader.getName(),
					        msg);

			}
		}
		if (DelLF.getCONTENT() != null) {

			if (DelLF.getCONTENT().getSDPBODY() != null) {
				SDPBODY sdpBody = DelLF.getCONTENT().getSDPBODY();
				if (sdpBody != null) {
					List<Sdp> sdp = sdpBody.getSdp();

					for (Sdp objSdp : sdp) {
						if (objSdp instanceof Sdp) {
							msg = CRLF_Handler.convertCRLFIntoLF(objSdp
							        .getName(), msg);
						}

					}
				}
			}
		}
		if (DelLF.getLINE() != null) {

			if (DelLF.getLINE().getFline() != null) {
				Fline fLine = DelLF.getLINE().getFline();
				if (fLine != null) {
					msg = CRLF_Handler.convertLineCRLFIntoLF(msg);
				}
			}
		}
		return msg;
	}

	/**
	 * processDelCRLF is used to process DEL_CRLF TAG in the serve scenario
	 * 
	 * @param DelCRLF
	 * @param msg
	 * @param Type
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 */
	private static String processDelCRLF(DELCRLF DelCRLF, String msg,
	        String Type) throws NullPointerException, IllegalArgumentException,
	        SipException, ParseException, InvalidArgumentException, IOException {

		if (DelCRLF.getHEADERS() != null) {
			HEADERS headers = DelCRLF.getHEADERS();
			if (headers != null) {
				List<Header> header = headers.getHeader();

				for (Header DELCRLFHeader : header)
					msg = CRLF_Handler.removeCRLF(DELCRLFHeader.getName(), msg);

			}
		}
		if (DelCRLF.getCONTENT() != null) {

			if (DelCRLF.getCONTENT().getSDPBODY() != null) {
				SDPBODY sdpBody = DelCRLF.getCONTENT().getSDPBODY();
				if (sdpBody != null) {
					List<Sdp> sdp = sdpBody.getSdp();

					for (Sdp objSdp : sdp) {
						if (objSdp instanceof Sdp) {
							msg = CRLF_Handler
							        .removeCRLF(objSdp.getName(), msg);
						}
					}

				}
			}
		}
		if (DelCRLF.getLINE() != null) {

			if (DelCRLF.getLINE().getFline() != null) {
				Fline fLine = DelCRLF.getLINE().getFline();
				if (fLine != null) {
					msg = CRLF_Handler.removeLineCRLF(msg);
				}
			}
		}
		return msg;
	}

	/**
	 * processDupMessage is used to process DUP_MSG Tag in server scenario
	 * 
	 * @param DupMsg
	 * @param msg
	 * @param Type
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 */
	private static String processDupMessage(DUPMSG DupMsg, String msg,
	        String Type) throws NullPointerException, IllegalArgumentException,
	        SipException, ParseException, InvalidArgumentException, IOException {
		if (DupMsg.getLINE() != null) {

			if (DupMsg.getLINE().getFline() != null) {
				msg = DuplicateHeaderHandler.addDuplicateLine(DupMsg.getLINE()
				        .getFline(), msg);
			}
		}
		if (DupMsg.getHEADERS() != null) {
			HEADERS headers = DupMsg.getHEADERS();
			if (headers != null) {
				List<Header> header = headers.getHeader();

				for (Header objHeader : header) {
					msg = DuplicateHeaderHandler.addDuplicateSIPHeader(
					        objHeader, msg);
				}

			}
		}
		if (DupMsg.getCONTENT() != null) {

			if (DupMsg.getCONTENT().getSDPBODY() != null) {
				SDPBODY sdpBody = DupMsg.getCONTENT().getSDPBODY();
				if (sdpBody != null) {

					List<Sdp> sdp = sdpBody.getSdp();

					for (Sdp objSdp : sdp) {
						if (objSdp instanceof Sdp) {
							msg = DuplicateHeaderHandler.addDuplicateContent(
							        objSdp.getName(), msg, Integer
							                .parseInt(objSdp.getCount()
							                        .toString()));
						}
					}

				}
			}
		}
		return msg;
	}
}
