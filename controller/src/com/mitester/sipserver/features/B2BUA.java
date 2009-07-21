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
package com.mitester.sipserver.features;

import static com.mitester.sipserver.SipServerConstants.INCOMING_MSG;
import static com.mitester.sipserver.SipServerConstants.INCOMING_SIP_MESSAGE;
import static com.mitester.sipserver.SipServerConstants.LINE_SEPARATOR;
import gov.nist.javax.sip.message.SIPMessage;

import java.io.IOException;
import java.net.SocketException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sip.InvalidArgumentException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.URI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.mitester.sipserver.ProcessSIPMessage;
import com.mitester.sipserver.UdpCommn;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;

/**
 * This is class used to run miTester for SIP as UAS ( User Agent Server).
 */
public class B2BUA {
	private static final Logger LOGGER = MiTesterLog.getLogger(B2BUA.class
			.getName());
	private Response response = null;
	private Request request = null;
	private HashMap<String, List<String>> binding = new HashMap<String, List<String>>();
	private List<String> list = null;
	private WWWAuthenticateHeader wwwauth = null;
	private static Boolean authentiaction = false;

	private UdpCommn udpCommn = null;

	private StartServer startServer = null;

	private static MessageFactory ms = null;
	private static HeaderFactory hf = null;

	private SipFactory sipFactory = SipFactory.getInstance();

	public B2BUA(StartServer startServer, UdpCommn udpCommn) {
		this.udpCommn = udpCommn;
		this.startServer = startServer;
	}

	/**
	 * To initialize a necessary factory to construct a SIP MEssages
	 * (MessageFactory,HeaderFactory).
	 */
	public void initializeB2BUA() throws PeerUnavailableException {

		ms = sipFactory.createMessageFactory();
		hf = sipFactory.createHeaderFactory();

	}

	/**
	 * Processing a received SIP Messages according to the SIP METHODS
	 * 
	 * @throws JAXBException
	 * @throws NullPointerException
	 */

	public void processSIPMessage(SIPMessage sipMessage) throws ParseException,
			SocketException, IOException, SipException,
			InvalidArgumentException, NullPointerException, JAXBException {

		if (sipMessage.getCSeq().getMethod().equals(Request.REGISTER)) {
			request = (Request) sipMessage;
			ExpiresHeader expires = (ExpiresHeader) request
					.getHeader(ExpiresHeader.NAME);
			ExpiresHeader expiresheader = null;
			ContactHeader contact = null;
			// To check Authentication is enabled or not
			if (!authentiaction) {
				response = ms.createResponse(Response.OK, request);
				if (expires != null) {
					response.setHeader(request.getExpires());
				} else {
					contact = (ContactHeader) request
							.getHeader(ContactHeader.NAME);
					expiresheader = hf
							.createExpiresHeader(contact.getExpires());
					response.setHeader(expiresheader);
				}
			} else {
				AuthorizationHeader auth = (AuthorizationHeader) request
						.getHeader(AuthorizationHeader.NAME);
				if (auth != null) {
					response = ms.createResponse(Response.OK, request);
					if (expires != null) {
						response.setHeader(request.getExpires());
					} else {
						contact = (ContactHeader) request
								.getHeader(ContactHeader.NAME);
						expiresheader = hf.createExpiresHeader(contact
								.getExpires());
						response.setHeader(expiresheader);
					}
				} else {
					response = ms
							.createResponse(Response.UNAUTHORIZED, request);
					// Creatation of WWW-Authenticate Header
					wwwauth = hf.createWWWAuthenticateHeader("Digest");
					wwwauth.setAlgorithm("AKAv1-MD5");
					wwwauth.setRealm("sip:register.mitester.com");
					wwwauth.setDomain("mitester.com");
					wwwauth
							.setNonce("VEVjcmlvU2VydmVyUkFORFRFY3Jpb1NlcnZlckFVVE4w");
					response.addHeader(wwwauth);
				}
			}
			ViaHeader via = (ViaHeader) request.getHeader(ViaHeader.NAME);
			FromHeader from = (FromHeader) request.getHeader(FromHeader.NAME);
			URI uri = from.getAddress().getURI();
			String[] name = uri.toString().split("@");
			String address = name[0].replace("sip:", "");
			if (request.getExpires().getExpires() > 0) {
				list = new ArrayList<String>();
				list.add(via.getHost().toString());
				list.add(new Integer(via.getPort()).toString());
				binding.put(address, list);
			} else {
				binding.remove(address);
			}
			// Sending SIP Message
			udpCommn.sendUdpMessage(response.toString(), via.getHost(),
					new Integer(via.getPort()).toString());
			// Received SIP MEssage is INVITE
		} else if (sipMessage.getCSeq().getMethod().equals(Request.INVITE)) {
			// To Check Request or Response
			if (sipMessage.getFirstLine().startsWith(Request.INVITE)) {
				request = (Request) sipMessage;

				ViaHeader via = (ViaHeader) request.getHeader(ViaHeader.NAME);
				String host = via.getHost();
				int port = via.getPort();
				response = ms.createResponse(Response.TRYING, request);
				udpCommn.sendUdpMessage(response.toString(), via.getHost(),
						new Integer(via.getPort()).toString());
				Request r = (Request) request.clone();
				r.removeHeader(ViaHeader.NAME);
				ToHeader to = (ToHeader) r.getHeader(ToHeader.NAME);
				String[] touri = to.getAddress().getURI().toString().split("@");
				String toname = touri[0].replace("sip:", "");
				List<String> bind = binding.get(toname);
				via.setPort(5070);
				r.addHeader(via);
				if (bind != null) {
					udpCommn.sendUdpMessage(r.toString(), bind.get(0), bind
							.get(1));
				}
				else {
					Response res = ms.createResponse(Response.NOT_FOUND,
							request);
					udpCommn.sendUdpMessage(res.toString(), host, new Integer(
							port).toString());
				}

			} else if (sipMessage.getFirstLine().startsWith("SIP/2.0 ")) {
				Response response = (Response) sipMessage;
				if (response.getStatusCode() != 302) {

					FromHeader from = (FromHeader) response
							.getHeader(FromHeader.NAME);
					String[] fromuri = from.getAddress().getURI().toString()
							.split("@");
					String fromname = fromuri[0].replace("sip:", "");

					List<String> bind = binding.get(fromname);
					if (bind != null) {

						udpCommn.sendUdpMessage(response.toString(), bind
								.get(0), bind.get(1).trim());

					}
				}
			}
		} else if (sipMessage.getCSeq().getMethod().equals(Request.CANCEL)) {
			// To Check Request or Response
			if (sipMessage.getFirstLine().startsWith(Request.CANCEL)) {
				request = (Request) sipMessage;

				ViaHeader via = (ViaHeader) request.getHeader(ViaHeader.NAME);
				Request r = (Request) request.clone();
				r.removeHeader(ViaHeader.NAME);
				ToHeader to = (ToHeader) r.getHeader(ToHeader.NAME);
				String[] touri = to.getAddress().getURI().toString().split("@");
				String toname = touri[0].replace("sip:", "");
				List<String> bind = binding.get(toname);
				via.setPort(5070);
				r.addHeader(via);
				if (bind != null) {
					udpCommn.sendUdpMessage(r.toString(), bind.get(0), bind
							.get(1));
				}
			} else if (sipMessage.getFirstLine().startsWith("SIP/2.0 ")) {
				Response response = (Response) sipMessage;

				FromHeader from = (FromHeader) response
						.getHeader(FromHeader.NAME);
				String[] fromuri = from.getAddress().getURI().toString().split(
						"@");
				String fromname = fromuri[0].replace("sip:", "");

				List<String> bind = binding.get(fromname);
				if (bind != null) {

					udpCommn.sendUdpMessage(response.toString(), bind.get(0),
							bind.get(1).trim());

				}
			}

			// Received SIP Message is OPTIONS
		} else if (sipMessage.getCSeq().getMethod().equals(Request.OPTIONS)) {
			request = (Request) sipMessage;
			response = ms.createResponse(Response.OK, request);
			ViaHeader via = (ViaHeader) request.getHeader(ViaHeader.NAME);

			udpCommn.sendUdpMessage(response.toString(), via.getHost(),
					new Integer(via.getPort()).toString());
			// Received SIP Message is ACK
		} else if (sipMessage.getCSeq().getMethod().equals(Request.ACK)) {
			request = (Request) sipMessage;
			ToHeader to = (ToHeader) request.getHeader(ToHeader.NAME);
			String[] touri = to.getAddress().getURI().toString().split("@");
			String toname = touri[0].replace("sip:", "");
			List<String> bind = binding.get(toname);
			if (bind != null) {
				udpCommn.sendUdpMessage(request.toString(), bind.get(0), bind
						.get(1));
			}
			// Received SIP Message is SUBSCRIBE
		} else if (sipMessage.getCSeq().getMethod().equals(Request.SUBSCRIBE)) {
			request = (Request) sipMessage;
			response = ms.createResponse(Response.OK, request);
			response.addHeader(request.getExpires());
			ViaHeader via = (ViaHeader) request.getHeader(ViaHeader.NAME);
			udpCommn.sendUdpMessage(response.toString(), via.getHost(),
					new Integer(via.getPort()).toString());
		} else {
			Request r = (Request) request.clone();
			r.removeHeader(ViaHeader.NAME);
			ToHeader to = (ToHeader) r.getHeader(ToHeader.NAME);
			String[] touri = to.getAddress().getURI().toString().split("@");
			String toname = touri[0].replace("sip:", "");
			List<String> bind = binding.get(toname);
			ViaHeader via = (ViaHeader) request.getHeader(ViaHeader.NAME);
			via.setPort(5070);
			r.addHeader(via);
			if (bind != null)
				udpCommn.sendUdpMessage(r.toString(), bind.get(0), bind.get(1));
		}
		// }
	}

	/**
	 * To set the Authentication Enabled
	 */
	public void setAuth(Boolean auth) {
		authentiaction = auth;
	}

	/**
	 * TO Start B2BUA
	 */
	public Runnable runB2BUA() {
		return new Runnable() {
			public void run() {

				try {

					// initialize the udp data gram socket
					udpCommn.initializeUdpSocket();

					do {

						SIPMessage sipMessage = null;

						// receive SIP message
						String sipMsg = udpCommn.receiveUdpMessage();

						if (!sipMsg.startsWith("SIP/2.0")
								&& !(sipMsg.substring(sipMsg.indexOf(" ",
										sipMsg.indexOf(" ") + 1) + 1, sipMsg
										.length()).startsWith("SIP/2.0"))) {

							LOGGER.info(LINE_SEPARATOR + sipMsg
									+ LINE_SEPARATOR);
							LOGGER
									.info("Received NON SIP message hence dropped");
							continue;
						} else {
							sipMessage = ProcessSIPMessage.processSIPMessage(
									sipMsg, INCOMING_MSG);

							// process SIP message
							if (sipMessage != null) {
								LOGGER.info(LINE_SEPARATOR
										+ INCOMING_SIP_MESSAGE + LINE_SEPARATOR
										+ sipMsg + INCOMING_SIP_MESSAGE);
								processSIPMessage(sipMessage);
							}

						}

					} while (true);
					// Handling Exception
				} catch (SocketException e) {
					TestUtility.printError(
							"Error while running miTester in B2BUA mode", e);
				} catch (IOException e) {
					TestUtility.printError(
							"Error while running miTester in B2BUA mode", e);
				} catch (NullPointerException e) {
					TestUtility.printError(
							"Error while running miTester in B2BUA mode", e);
				} catch (ParseException e) {
					TestUtility.printError(
							"Error while running miTester in B2BUA mode", e);
				} catch (SipException e) {
					TestUtility.printError(
							"Error while running miTester in B2BUA mode", e);
				} catch (InvalidArgumentException e) {
					TestUtility.printError(
							"Error while running miTester in B2BUA mode", e);
				} catch (com.mitester.sipserver.headervalidation.MissingMandatoryHeaderException e) {
					TestUtility.printError(
							"Error while processing SIP message", e);
				} catch (com.mitester.sipserver.headervalidation.InvalidValuesHeaderException e) {
					TestUtility.printError(
							"Error while processing SIP message", e);
				} catch (Exception e) {
					TestUtility.printError(
							"Error while running miTester in B2BUA mode", e);
				}

				finally {

					if (startServer.getb2buaCountDownLatch().getCount() > 0) {
						startServer.getb2buaCountDownLatch().countDown();
					}

				}
			}
		};
	}
}
