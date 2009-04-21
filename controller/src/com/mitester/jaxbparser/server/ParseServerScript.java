/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ParseServerScript.java
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
package com.mitester.jaxbparser.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
 * 
 */

public class ParseServerScript {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(ParseServerScript.class.getName());

	private static final String FILE_PARSING_SMALLXML = ".xml";

	private static final String FILE_PARSING_CAPITALXML = ".XML";

	private static final String FILE_SEPARATOR = "/";// System.getProperty("file.separator");

	private List<TEST> serverScenarios = new ArrayList<TEST>();

	private static Unmarshaller u;

	private static JAXBContext jc;

	static {
		try {
			jc = JAXBContext.newInstance("com.mitester.jaxbparser.server");
			LOGGER.info("Unmarshal Created");
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			Schema schema = sf.newSchema(ParseServerScript.class
					.getClassLoader().getResource(
							"com" + FILE_SEPARATOR + "mitester"
									+ FILE_SEPARATOR + "jaxbparser"
									+ FILE_SEPARATOR + "server"
									+ FILE_SEPARATOR + "Server-Script.xsd"));

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
			TestUtility.printMessage("Line Number	:	"
					+ event.getLocator().getLineNumber());
			TestUtility.printMessage("Column Number	:	"
					+ event.getLocator().getColumnNumber());
			return false;
		}
	}

	/**
	 * method called to parse the server script files
	 * 
	 * @param serverScriptPath
	 *            is a name of the server script path
	 * @return is a TreeMap object which includes set of server tests
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws JAXBException
	 * 
	 */

	public List<TEST> FileParsingServer(String serverScriptPath)
			throws FileNotFoundException, IOException, NullPointerException, JAXBException {

		LOGGER.info("Entered to FileParsingServer");
		File directoryPath = new File(serverScriptPath);

		String ScriptChildren[] = directoryPath.list();

		for (String script : ScriptChildren) {

			String serverScriptPathNew = serverScriptPath.concat(FILE_SEPARATOR
					+ script);

			if (script.equalsIgnoreCase("Content_Files"))
				continue;

			if (script.endsWith(FILE_PARSING_SMALLXML)
					|| script.endsWith(FILE_PARSING_CAPITALXML)) {

				serverScenarios
						.addAll(parseServerScenarioFile(serverScriptPathNew));

			} else if (new File(serverScriptPathNew).isDirectory()) {

				serverScenarios = FileParsingServer(serverScriptPathNew);
			}
		}

		return serverScenarios;
	}

	/**
	 * method called to parse the server script files
	 * 
	 * @param serverScriptFile
	 *            is the name of the server script file
	 * @return is a TreeMap object which includes set of server tests
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws JAXBException
	 */

	public List<com.mitester.jaxbparser.server.TEST> parseServerScenarioFile(
			String serverScriptFile) throws IOException, NullPointerException,
			JAXBException {

		LOGGER.info("Entered into parse Server ScenarioFile");

		try {
			Object testFlow = u
					.unmarshal(new FileInputStream(serverScriptFile));
			LOGGER.info("Unmarshal completed");
			com.mitester.jaxbparser.server.TESTFLOW sno = (com.mitester.jaxbparser.server.TESTFLOW) testFlow;
			return sno.getTEST();
		} catch (JAXBException ex) {
			TestUtility.printError("Parsing Error", ex);
			LOGGER.info("Parsing Error	:	" + ex);
			throw ex;
		}
	}
}
