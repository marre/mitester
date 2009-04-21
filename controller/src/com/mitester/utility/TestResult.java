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

	private static final String EXECUTION_START_TIME = "EXECUTION_START_TIME";

	private static final String EXECUTION_END_TIME = "EXECUTION_END_TIME";

	private static final String FIELD_SEPARATOR = ":";

	private static final String TEST_FAIL = "FAIL";

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	private static final StringBuilder RESULT_BUFFER = new StringBuilder();

	private static int TEST_ID_LENGTH = 35;

	private TestResult() {

	}

	/**
	 * this method called end of every test execution to update the test result
	 * 
	 * @param testID
	 *            String object represents test case id
	 * @param testResult
	 *            boolean represents the test result
	 */

	public static void updateResult(String testID, boolean testResult) {

		String resultStr = null;
		int len = 0;
		int testIdLen = testID.length();

		if (TEST_ID_LENGTH > testIdLen) {

			len = TEST_ID_LENGTH - testIdLen;
		} else {

			TEST_ID_LENGTH = testIdLen + 10;

			len = TEST_ID_LENGTH - testIdLen;
		}

		if (testResult) {

			resultStr = String.format("%" + len + "s", TEST_PASS);

		} else {

			resultStr = String.format("%" + len + "s", TEST_FAIL);

		}

		RESULT_BUFFER.append(testID + resultStr
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

		String fmtStr = null;

		TestUtility
				.printMessage(LINE_SEPARATOR
						+ "************************************************************************");
		TestUtility.printMessage("Consolidated Test Results");
		TestUtility
				.printMessage("************************************************************************");
		TestUtility.printMessage(RESULT_BUFFER.toString());

		try {
			BufferedWriter buffferedWriter = new BufferedWriter(new FileWriter(
					"TestResult.txt", true));

			fmtStr = String.format("%4s", FIELD_SEPARATOR);

			buffferedWriter.write(EXECUTION_START_TIME + fmtStr + " "
					+ startTime);
			buffferedWriter.write(LINE_SEPARATOR);
			buffferedWriter.write(RESULT_BUFFER.toString());

			fmtStr = String.format("%6s", FIELD_SEPARATOR);

			buffferedWriter.write(EXECUTION_END_TIME + fmtStr + " " + endTime);
			buffferedWriter.write(LINE_SEPARATOR);
			buffferedWriter.flush();
			buffferedWriter.close();

		} catch (Exception ex) {

		}
	}
}