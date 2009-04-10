/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: PChargingVectorHeaderHandler.java
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
package com.mitester.sipserver.sipheaderhandler;

import static com.mitester.sipserver.sipheaderhandler.SIPHeaderConstant.P_CHARGING_VECTOR;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.header.ims.PChargingVectorHeader;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.HeaderFactory;

import com.mitester.jaxbparser.server.Header;
import com.mitester.jaxbparser.server.Param;
import com.mitester.sipserver.SIPHeaders;
import com.mitester.utility.MiTesterLog;

/**
 * PChargingVectorHeaderHandler is used to add and remove the P-ChargingVector
 * header
 * 
 * RFC 3455 says,
 * 
 * 4.6 The P-Charging-Vector header
 * 
 * 3GPP has defined a distributed architecture that results in multiple network
 * entities becoming involved in providing access and services. Operators need
 * the ability and flexibility to charge for the access and services as they see
 * fit. This requires coordination among the network entities (e.g., SIP
 * proxies), which includes correlating charging records generated from
 * different entities that are related to the same session. The correlation
 * information includes, but it is not limited to, a globally unique charging
 * identifier that makes easy the billing effort. A charging vector is defined
 * as a collection of charging information. The charging vector may be filled in
 * during the establishment of a dialog or standalone transaction outside a
 * dialog. The information inside the charging vector may be filled in by
 * multiple network entities (including SIP proxies) and retrieved by multiple
 * network entities. There are three types of correlation information to be
 * transferred: the IMS Charging Identity (ICID) value, the address of the SIP
 * proxy that creates the ICID value, and the Inter Operator Identifiers (IOI).
 * ICID is a charging value that identifies a dialog or a transaction outside a
 * dialog. It is used to correlate charging records. ICID MUST be a globally
 * unique value. One way to achieve globally uniqueness is to generate the ICID
 * using two components: a locally unique value and the host name or IP address
 * of the SIP proxy that generated the locally unique value. The IOI identifies
 * both the originating and terminating networks involved in a SIP dialog or
 * transaction outside a dialog. There may an IOI generated from each side of
 * the dialog to identify the network associated with each side. There is also
 * expected to be access network charging information, which consists of network
 * specific identifiers for the access level (e.g., UMTS radio access network or
 * IEEE 802.11b). The details of the information for each type of network are
 * not described in this memo. We define the SIP private header
 * P-Charging-Vector. A proxy MAY include this header, if not already present,
 * in either the initial request or response for a dialog, or in the request and
 * response of a standalone transaction outside a dialog. Only one instance of
 * the header MUST be present in a particular request or response. The
 * mechanisms by which a SIP proxy collects the values to populate in the
 * P-Charging-Vector are outside the scope of this document.
 * 
 * 
 * 
 */

public class PChargingVectorHeaderHandler {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(PChargingVectorHeaderHandler.class.getName());

	/**
	 * createPChargingVectorHeader is used to create P-ChargingVector header
	 * 
	 * @param headerNew
	 * @param type
	 * @param sipMessage
	 * @return
	 * @throws SipException
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public static PChargingVectorHeader createPChargingVectorHeader(
	        Header headerNew) throws SipException, ParseException,
	        InvalidArgumentException, IndexOutOfBoundsException {

		LOGGER.info("Creating "
		        + SIPHeaders.getSipHeaderfromString(
		                headerNew.getName().toUpperCase()).toString()
		        + " Header");

		PChargingVectorHeader pChargingVecotr = null;
		SipFactory factory = SipFactory.getInstance();
		HeaderFactory hf = factory.createHeaderFactory();
		HeaderFactoryImpl hfimpl = (HeaderFactoryImpl) factory
		        .createHeaderFactory();
		((HeaderFactoryImpl) hf).setPrettyEncoding(true);
		hfimpl.setPrettyEncoding(true);

		String hvalue = null;
		hvalue = headerNew.getValue();

		if (hvalue == null)
			hvalue = P_CHARGING_VECTOR;
		pChargingVecotr = hfimpl.createChargingVectorHeader(hvalue);
		List<Param> param = headerNew.getParam();

		for (Param objParam : param) {
			String paramname = objParam.getName();
			String value = objParam.getValue();
			pChargingVecotr.setParameter(paramname, value);
		}

		return pChargingVecotr;
	}

}
