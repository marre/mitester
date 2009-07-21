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
package com.mitester.jaxbparser.validation;

import static com.mitester.utility.UtilityConstants.NORMAL;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;

import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;

public class ParseValidationScript {
	private static final Logger LOGGER = MiTesterLog
			.getLogger(ParseValidationScript.class.getName());

	private static final String FILE_SEPARATOR = "/";
	private static Unmarshaller u;

	private static JAXBContext jc;

	static {
		try {
			jc = JAXBContext.newInstance("com.mitester.jaxbparser.validation");
			LOGGER.info("Unmarshal Created");
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			Schema schema = sf.newSchema(ParseValidationScript.class
					.getClassLoader().getResource(
							"com" + FILE_SEPARATOR + "mitester"
									+ FILE_SEPARATOR + "jaxbparser"
									+ FILE_SEPARATOR + "validation"
									+ FILE_SEPARATOR + "Validation.xsd"));

			u = jc.createUnmarshaller();
			u.setSchema(schema);
			u.setEventHandler(new VaHandeler());

		} catch (Exception ex) {

			// TODO Auto-com.mitester.jaxbparser.server catch block
			TestUtility.printError("Error at unmarshalling Server Test", ex);
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
	
	public static VALIDATION parseValidationScript(String inputfile) throws FileNotFoundException, JAXBException {
		try {
			Object testFlow = u
					.unmarshal(new FileInputStream(inputfile));
			LOGGER.info("Unmarshal completed");
			com.mitester.jaxbparser.validation.VALIDATION validation = (com.mitester.jaxbparser.validation.VALIDATION) testFlow;
			return validation;
		} catch (JAXBException ex) {
			TestUtility.printError("Parsing Error", ex);
			LOGGER.info("Parsing Error	:	" + ex);
			throw ex;
		}
	}
}