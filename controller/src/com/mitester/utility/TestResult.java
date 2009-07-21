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
 * Package 						License 											Details
 *---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Jain SIP stack 				NIST-CONDITIONS-OF-USE 								https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 						The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * JNetStreamStandalone lib     GNU Library or LGPL			     					http://sourceforge.net/projects/jnetstream/
 * 
 */

/* miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications. 
 */
package com.mitester.utility;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * This class updates and prints the test results.
 * 
 */

public class TestResult {

	private static final String TEST_PASS = "PASS";

	private static final String LINE_SEP = System.getProperty("line.separator");

	private static final String EXECUTION_START_TIME = "EXECUTION_START_TIME";

	private static final String EXECUTION_END_TIME = "EXECUTION_END_TIME";

	private static final String FIELD_SEPARATOR = ":";

	private static final String TEST_FAIL = "FAIL";

	private static final StringBuilder RESULT_BUFFER = new StringBuilder();

	private static String failReason = null;

	private static boolean isReasonSet = false;

	private static String testID = null;
	
	private TestResult() {

	}

	/**
	 * this method called at the end of every test execution to update the test
	 * result
	 * 
	 * @param testID
	 *            represents test case id
	 * @param testResult
	 *            represents the test result
	 */

	public static void updateResult(String testID, boolean testResult) {

		if (testResult)
			RESULT_BUFFER.append(formatResult(TEST_PASS, testID) + LINE_SEP);
		else
			RESULT_BUFFER.append(formatResult(TEST_FAIL, testID, failReason)
					+ LINE_SEP);

		isReasonSet = false;
	}

	/**
	 * prints the test result
	 * 
	 * @param startTime
	 *            represents start time of test execution
	 * @param endTime
	 *            represents end time of test execution
	 */
	public static void printResult(String startTime, String endTime) {

		String fmtStr = null;

		try {
			BufferedWriter buffferedWriter = new BufferedWriter(new FileWriter(
					"TestResult.txt", true));

			fmtStr = String.format("%4s", FIELD_SEPARATOR);
			
			buffferedWriter.write(LINE_SEP);
			buffferedWriter.write(EXECUTION_START_TIME + fmtStr + " "
					+ startTime);
			buffferedWriter.write(LINE_SEP);
			buffferedWriter
					.write("*********************************************");
			buffferedWriter.write(LINE_SEP);
			buffferedWriter.write(RESULT_BUFFER.toString());

			fmtStr = String.format("%6s", FIELD_SEPARATOR);

			buffferedWriter.write(EXECUTION_END_TIME + fmtStr + " " + endTime);
			buffferedWriter.write(LINE_SEP);
			buffferedWriter.write(LINE_SEP);
			buffferedWriter.flush();
			buffferedWriter.close();

		} catch (Exception ex) {

		}
	}

	/**
	 * it formats the test result
	 * 
	 * @param testID
	 *            represents test case id
	 * @param testResult
	 *            represents the test result
	 */

	public static String formatResult(String... resultMessage) {

		String resultStr = null;

		if (resultMessage[0].equals(TEST_PASS)) {

			String fmtStr = null;

			fmtStr = String.format("%" + 12 + "s", ": ");

			resultStr = "TC ID" + fmtStr + resultMessage[1] + LINE_SEP;

			fmtStr = String.format("%" + 6 + "s", ": ");

			resultStr = resultStr + "TEST RESULT" + fmtStr + TEST_PASS
					+ LINE_SEP;

			resultStr = resultStr
					+ "*********************************************";

		} else {

			String fmtStr = null;

			fmtStr = String.format("%" + 12 + "s", ": ");

			resultStr = "TC ID" + fmtStr + resultMessage[1] + LINE_SEP;

			fmtStr = String.format("%" + 6 + "s", ": ");

			resultStr = resultStr + "TEST RESULT" + fmtStr + TEST_FAIL
					+ LINE_SEP;

			fmtStr = String.format("%" + 5 + "s", ": ");

			resultStr = resultStr + "ERROR STRING" + fmtStr + resultMessage[2]
					+ LINE_SEP;

			resultStr = resultStr
					+ "*********************************************";

		}

		return resultStr;

	}

	/**
	 * it sets the fail reason
	 * 
	 * @param reason
	 *            holds the reason for fail
	 */

	public static void setFailReason(String reason) {

		if (!isReasonSet) {

			failReason = reason;

			if (failReason != null)
				isReasonSet = true;
		}

	}

	/**
	 * It returns the fail reason
	 * 
	 * @return fail reason string
	 */
	public static String getFailReason() {
		return failReason;
	}

	/**
	 * set TestID
	 * 
	 * @param testId
	 *            test case ID
	 */
	public static void setTestID(String testId) {
		testID = testId;

	}

	/**
	 * get TestID
	 * 
	 * @return testID
	 */
	public static String getTestID() {
		return testID;

	}
	
	
}
