/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ParseClientScript.java
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
package com.mitester.jaxbparser.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
 * It handles the file operation and JAXB Parsing of the script which is
 * specified in the scripts path of miTester.properties. While parsing scripts,
 * each test script will be parsed separately. If any error occurs, it will
 * throw an error with test case id.
 */

public class ParseClientScript {

	private static final Logger LOGGER = MiTesterLog
	        .getLogger(ParseClientScript.class.getName());

	private static final String FILE_PARSING_SMALLXML = ".xml";

	private static final String FILE_PARSING_CAPITALXML = ".XML";

	private static final String FILE_SEPARATOR = "/";// System.getProperty("file.separator");

	private List<TEST> clientScenarios = new ArrayList<TEST>();

	private static Unmarshaller u;

	private static JAXBContext jc;

	static {
		try {
			jc = JAXBContext.newInstance("com.mitester.jaxbparser.client");
			LOGGER.info("Unmarshal Created");
			SchemaFactory sf = SchemaFactory
			        .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			Schema schema = sf.newSchema(ParseClientScript.class
			        .getClassLoader().getResource(
			                "com" + FILE_SEPARATOR + "mitester"
			                        + FILE_SEPARATOR + "jaxbparser"
			                        + FILE_SEPARATOR + "client"
			                        + FILE_SEPARATOR + "Client-Script.xsd"));

			u = jc.createUnmarshaller();
			u.setSchema(schema);
			u.setEventHandler(new VaHandeler());

		} catch (Exception ex) {
			// TODO Auto-com.mitester.jaxbparser.client catch block
			TestUtility.printError("Error at unmarshalling client Test", ex);
		}
	}

	static class VaHandeler implements ValidationEventHandler {

		public boolean handleEvent(ValidationEvent event) {
			TestUtility.printMessage("Line Number	:	"
			        + event.getLocator().getLineNumber());
			TestUtility.printMessage("Column Number	:	"
			        + event.getLocator().getColumnNumber());
			return false;
		}
	}

	/**
	 * method called to parse the client script files
	 * 
	 * @param clientScriptFile
	 *            name of the client script file
	 * @return is a TreeMap object which includes set of client tests
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws JAXBException
	 */
	public List<TEST> parseClientScenarioFile(String clientScriptFile)
	        throws IOException, NullPointerException, JAXBException {

		LOGGER.info("Entered into parseClientScenarioFile");

		try {
			Object testFlow = u
			        .unmarshal(new FileInputStream(clientScriptFile));
			LOGGER.info("Unmarshal completed");
			com.mitester.jaxbparser.client.TESTFLOW sno = (TESTFLOW) testFlow;
			return sno.getTEST();
		} catch (JAXBException ex) {
			TestUtility.printError("Parsing Error", ex);
			LOGGER.info("Parsing Error	:	" + ex);
			throw ex;
		}
	}

	/**
	 * method is called to parse all the client test script files
	 * 
	 * @param clientScriptPath
	 *            is the name of the test script path
	 * @return is a TreeMap object which includes set of client tests
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws JAXBException
	 */
	public List<TEST> FileParsingClient(String clientScriptPath)
	        throws IOException, NullPointerException, JAXBException {

		LOGGER.info("Entered to FileParsingClient");

		File directoryPath = new File(clientScriptPath);

		String ScriptChildren[] = directoryPath.list();

		for (String script : ScriptChildren) {

			String clientScriptPathNew = clientScriptPath.concat(FILE_SEPARATOR
			        + script);

			if (script.endsWith(FILE_PARSING_SMALLXML)
			        || script.endsWith(FILE_PARSING_CAPITALXML)) {

				clientScenarios
				        .addAll(parseClientScenarioFile(clientScriptPathNew));

			} else if (new File(clientScriptPathNew).isDirectory()) {
				clientScenarios = FileParsingClient(clientScriptPathNew);

			}
		}
		return clientScenarios;
	}
}
