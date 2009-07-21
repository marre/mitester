/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: MessageHandlerHelper.java
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
 * Package 						License 										Details
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
package com.mitester.sipserver.sipmessagehandler;

import static com.mitester.sipserver.sipmessagehandler.SIPMessageHandlerConstants.AT;
import gov.nist.javax.sip.message.MessageFactoryImpl;

import java.text.ParseException;
import java.util.List;

import javax.sip.InvalidArgumentException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

import org.apache.log4j.Logger;

import com.mitester.utility.MiTesterLog;



/**
 * This class supports the SIP message construction
 * 
 */

public class MessageHandlerHelper {
	private static final Logger LOGGER = MiTesterLog
	.getLogger(MessageHandlerHelper.class.getName());
	/**
	 * this method is used to create a SIP URI from the string
	 * 
	 * @param userName
	 * @param hostName
	 * @param addressFactory
	 * @return
	 * @throws ParseException
	 */
	public static SipURI createSIPURI(String userName, String hostName,
	        AddressFactory addressFactory) throws ParseException {
		//LOGGER.info("Creating SIP URI");
		SipURI sipURI = addressFactory.createSipURI(userName, hostName);
		
		return sipURI;
	}

	/**
	 * this is used to create a Address
	 * 
	 * @param sipURI
	 * @param addressFactory
	 * @return
	 * @throws ParseException
	 */
	public static Address createAddress(SipURI sipURI,
	        AddressFactory addressFactory) throws ParseException {
		//LOGGER.info("Creating Address from the SIP URI");
		Address address = addressFactory.createAddress(sipURI);
		
		return address;
	}

	/**
	 * this is used to create from header
	 * 
	 * @param address
	 * @param tag
	 * @param headerFactory
	 * @return
	 * @throws ParseException
	 */
	public static FromHeader createFromHeader(Address address, String tag,
	        HeaderFactory headerFactory) throws ParseException {
		//LOGGER.info("Creating From Header");
		FromHeader fromHeader = headerFactory.createFromHeader(address, tag);
		LOGGER.info("Created From Header successfully");
		return fromHeader;
	}

	/**
	 * This is used to create To header
	 * 
	 * @param address
	 * @param tag
	 * @param headerFactory
	 * @return
	 * @throws ParseException
	 */
	public static ToHeader createToHeader(Address address, String tag,
	        HeaderFactory headerFactory) throws ParseException {
		//LOGGER.info("Creating To Header");
		ToHeader toHeader = headerFactory.createToHeader(address, tag);
		LOGGER.info("Created To Header successfully");
		return toHeader;
	}

	/**
	 * this is used to create via header
	 * 
	 * @param address
	 * @param port
	 * @param protocol
	 * @param magicCookies
	 * @param branch
	 * @param headerFactory
	 * @return
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 */
	public static ViaHeader createViaHeader(String address, int port,
	        String protocol, String branch, HeaderFactory headerFactory)
	        throws ParseException, InvalidArgumentException {
		//LOGGER.info("Creating Via Header");
		ViaHeader viaHeader = headerFactory.createViaHeader(address, port,
		        protocol, branch);
		LOGGER.info("Created Via Header successfully");
		return viaHeader;
	}

	/**
	 * This is used to create CSeq Header
	 * 
	 * @param number
	 * @param methodName
	 * @param headerFactory
	 * @return
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 */
	public static CSeqHeader createCSeqHeader(long number, String methodName,
	        HeaderFactory headerFactory) throws ParseException,
	        InvalidArgumentException {
		//LOGGER.info("Creating CSeq Header");
		CSeqHeader cseqHeader = headerFactory.createCSeqHeader(number,
		        methodName);
		LOGGER.info("Created CSeq Header successfully");
		return cseqHeader;
	}

	/**
	 * This is used to create a MaxForwardsHeader
	 * 
	 * @param number
	 * @param headerFactory
	 * @return
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 */
	public static MaxForwardsHeader createMaxForwardsHeader(int number,
	        HeaderFactory headerFactory) throws ParseException,
	        InvalidArgumentException {
		//LOGGER.info("Creating Max-Forwards Header");
		MaxForwardsHeader maxForwardsHeader = headerFactory
		        .createMaxForwardsHeader(number);
		LOGGER.info("Created Max-Forwards Header successfully");
		return maxForwardsHeader;
	}

	/**
	 * this is used to create call id header
	 * 
	 * @param random
	 * @param address
	 * @param headerFactory
	 * @return
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 */
	public static CallIdHeader createCallIdHeader(String random,
	        String address, HeaderFactory headerFactory) throws ParseException,
	        InvalidArgumentException {
		//LOGGER.info("Creating Call-ID Header");
		CallIdHeader callIdHeader = headerFactory.createCallIdHeader(random
		        + AT + address);
		LOGGER.info("Created Call-ID Header successfully");
		return callIdHeader;
	}

	/**
	 * This is used to create expires header
	 * 
	 * @param expires
	 * @param headerFactory
	 * @return
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 */
	public static ExpiresHeader createExpiresHeader(int expires,
	        HeaderFactory headerFactory) throws ParseException,
	        InvalidArgumentException {
		//LOGGER.info("Creating Expires Header");
		ExpiresHeader expiresHeader = headerFactory
		        .createExpiresHeader(expires);
		LOGGER.info("Created Expires Header successfully");
		return expiresHeader;
	}

	public static ContactHeader createContactHeader(Address contactAddress,
	        HeaderFactory headerFactory) throws ParseException,
	        InvalidArgumentException {
		//LOGGER.info("Creating Contact Header");
		ContactHeader contactHeader = headerFactory
		        .createContactHeader(contactAddress);
		LOGGER.info("Created Contact Header successfully");
		return contactHeader;
	}

	/**
	 * This is used to create request
	 * 
	 * @param requesturi
	 * @param methodName
	 * @param callIdHeader
	 * @param cseqHeader
	 * @param fromHeader
	 * @param toHeader
	 * @param viaHeaders
	 * @param maxForwardsHeader
	 * @param messageFactoryImpl
	 * @return
	 * @throws ParseException
	 */
	public static Request createRequest(URI requesturi, String methodName,
	        CallIdHeader callIdHeader, CSeqHeader cseqHeader,
	        FromHeader fromHeader, ToHeader toHeader,
	        List<ViaHeader> viaHeaders, MaxForwardsHeader maxForwardsHeader,
	        MessageFactoryImpl messageFactoryImpl) throws ParseException {
		//LOGGER.info("Creating Creating from the SIP Headers");
		Request request = messageFactoryImpl.createRequest(requesturi,
		        methodName, callIdHeader, cseqHeader, fromHeader, toHeader,
		        viaHeaders, maxForwardsHeader);
		if(request != null)
			LOGGER.info("SIP Request is created successfully");
		return request;
	}
}
