/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ParseSDPBody.java
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
 * Package 					License 											Details
 *---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Jain SIP stack 			NIST-CONDITIONS-OF-USE 								https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 					The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * 
 */

/* miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications. 
 */
package com.mitester.jaxbparser.sdpbody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
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

/**
 * It handles JAXB Parsing of the sdp body which is specified in the server
 * scripts. It can give the sdp body in the server script itself or separate
 * file. That file name should be mentioned in 'file source' tag in server
 * script. While parsing sdp body, the file will be checked for existence, if
 * not, it will throw an error with test case id.
 * 
 */

public class ParseSDPBody {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(ParseSDPBody.class.getName());

	private static Unmarshaller u;

	private static JAXBContext jc;

	static {
		try {
			jc = JAXBContext.newInstance("com.mitester.jaxbparser.sdpbody");
			LOGGER.info("Unmarshal Created");
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(ParseSDPBody.class.getClassLoader()
					.getResource(
							"com" + File.separator + "mitester"
									+ File.separator + "jaxbparser"
									+ File.separator + "sdpbody"
									+ File.separator + "SDP_Body.xsd"));
			u = jc.createUnmarshaller();
			u.setSchema(schema);
			u.setEventHandler(new VaHandeler());

		} catch (Exception ex) {
			// TODO Auto-com.mitester.jaxbparser.client catch block
			TestUtility.printError("Error at unmarshalling SDP Body ", ex);
		}
	}

	static class VaHandeler implements ValidationEventHandler {

		public boolean handleEvent(ValidationEvent event) {
			TestUtility.printMessage("Column Number"
					+ event.getLocator().getColumnNumber());
			TestUtility.printMessage("Line Number"
					+ event.getLocator().getLineNumber());
			return false;
		}
	}

	/**
	 * method called to parse the sdp body file
	 * 
	 * @param sdpBodyFile
	 *            name of the sdp body file
	 * @return is a TreeMap object which includes set of client tests
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws JAXBException
	 */

	public List<Sdp> ParseSDPBodyFile(String sdpBodyFile) throws IOException,
			NullPointerException, JAXBException {

		LOGGER.info("Entered into SDP Bosy Parsing");

		try {
			Object sdpBody = u.unmarshal(new FileInputStream(sdpBodyFile));
			LOGGER.info("Unmarshal completed");
			com.mitester.jaxbparser.sdpbody.SDPBODY sdp = (SDPBODY) sdpBody;
			return (sdp.getSdp());

		} catch (JAXBException ex) {
			TestUtility.printError("Parsing Error", ex);
			LOGGER.info("Parsing Error	:	" + ex);
			throw ex;
		}
	}
}
