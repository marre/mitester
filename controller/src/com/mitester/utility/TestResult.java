/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: TestResult.java
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
package com.mitester.utility;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * This class updates and print the test results.
 * 
 */

public class TestResult {

	private static final String TEST_PASS = "PASS";

	private static final String TEST_FAIL = "FAIL";

	private static final StringBuilder RESULT_BUFFER = new StringBuilder();

	/**
	 * this method called end of every test execution to update the test result
	 * 
	 * @param testID
	 *            is String object represents test case id
	 * @param testResult
	 *            is a boolean represents the test result
	 */
	
	private TestResult() {
		
	}
	
	

	public static void updateResult(String testID, boolean testResult) {

		if (testResult)
			RESULT_BUFFER.append(testID + "   " + TEST_PASS
					+ System.getProperty("line.separator"));
		else
			RESULT_BUFFER.append(testID + "   " + TEST_FAIL
					+ System.getProperty("line.separator"));
	}

	/**
	 * prints the test result
	 * 
	 * @param startTime
	 *            is a String object represents start time of test execution
	 * @param endTime
	 *            is a String object represents end time of test execution
	 */
	public static void printResult(String startTime, String endTime) {

		TestUtility
				.printMessage("\n"
						+ "************************************************************************");
		TestUtility.printMessage("Consolidated Test Results");
		TestUtility
				.printMessage("************************************************************************");
		TestUtility.printMessage(RESULT_BUFFER.toString());

		try {
			BufferedWriter buffferedWriter = new BufferedWriter(new FileWriter(
					"TestResult.txt", true));
			buffferedWriter.write("Excution Start Time\t: " + startTime);
			buffferedWriter.write("\r\n");
			buffferedWriter.write(RESULT_BUFFER.toString());
			buffferedWriter.write("Excution End Time\t\t: " + endTime);
			buffferedWriter.write("\r\n");
			buffferedWriter.flush();
			buffferedWriter.close();

		} catch (Exception ex) {

		}
	}
}