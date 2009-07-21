/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: TestExecutor.java
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
package com.mitester.utility;

import static com.mitester.utility.UtilityConstants.CALL_FLOW_MODE;
import static com.mitester.utility.UtilityConstants.LINE_SEPARATOR;
import static com.mitester.utility.UtilityConstants.LOG_MODE;
import static com.mitester.utility.UtilityConstants.NORMAL_MODE;
import static com.mitester.utility.UtilityConstants.PREVIOUS_MODE;
import static com.mitester.utility.UtilityConstants.RESULT;
import static com.mitester.utility.UtilityConstants.RESULT_MODE;
import static com.mitester.utility.UtilityConstants.HELP;
import static com.mitester.utility.UtilityConstants.HELP_MODE;
import static com.mitester.utility.UtilityConstants.PAUSE_MODE;
import static com.mitester.utility.UtilityConstants.NORMAL;
import static com.mitester.utility.UtilityConstants.STOP_MODE;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

import com.mitester.executor.TestExecutor;

/**
 * It reads the console inputs and sets the mode of execution
 * 
 */

public class ConsoleReader {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(ConsoleReader.class.getName());

	private static final BufferedReader SYSTEM_IN = new BufferedReader(
			new InputStreamReader(System.in));

	private TestExecutor testExecutor = null;

	private String priviousMode = NORMAL_MODE;

	private boolean isPaused = false;

	private ThreadControl threadControl = null;

	public ConsoleReader(TestExecutor testExecutor, ThreadControl threadControl) {
		this.testExecutor = testExecutor;
		this.threadControl = threadControl;
	}

	/**
	 * This method is used to read the console messages
	 * 
	 * @return Runnable interface
	 */

	public Runnable readConsoleMessages() {

		LOGGER.info("entered into readConsoleMessages");

		return new Runnable() {

			public void run() {

				try {

					while (!testExecutor.isCompleted()) {

						// read console message
						String mode = readMessage();

						if (mode.equalsIgnoreCase(LOG_MODE)
								|| mode.equalsIgnoreCase(CALL_FLOW_MODE)
								|| mode.equalsIgnoreCase(NORMAL_MODE)
								|| mode.equalsIgnoreCase(RESULT_MODE)
								|| mode.equalsIgnoreCase(HELP_MODE)
								|| mode.equalsIgnoreCase(PAUSE_MODE)
								|| mode.equalsIgnoreCase(STOP_MODE)
								|| mode.equalsIgnoreCase("-h")
								|| mode.equalsIgnoreCase("--help")
								|| mode.equalsIgnoreCase(PREVIOUS_MODE)) {

							if (mode.equalsIgnoreCase(PREVIOUS_MODE)) {

								String tempPrevMode = TestUtility
										.getModeOfDisplay();

								// set mode of display
								TestUtility.setModeOfDisplay(priviousMode);

								TestUtility.callFlow();

								priviousMode = tempPrevMode;

							} else if (mode.equalsIgnoreCase(RESULT_MODE)) {

								if (!TestUtility.getModeOfDisplay()
										.equalsIgnoreCase(HELP_MODE)
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase("-h")
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase("--help")
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase(RESULT_MODE)
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase(PAUSE_MODE)) {
									priviousMode = TestUtility
											.getModeOfDisplay();
								}

								// set mode of display
								TestUtility.setModeOfDisplay(mode);

								// print execution status
								printExecutionStatus();
							} else if (mode.equalsIgnoreCase(HELP_MODE)
									|| mode.equalsIgnoreCase("-h")
									|| mode.equalsIgnoreCase("--help")) {

								if (!TestUtility.getModeOfDisplay()
										.equalsIgnoreCase(HELP_MODE)
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase("-h")
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase("--help")
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase(RESULT_MODE)
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase(PAUSE_MODE)) {
									priviousMode = TestUtility
											.getModeOfDisplay();
								}

								// set mode of display
								TestUtility.setModeOfDisplay(HELP_MODE);

								// print execution status
								printHelpInfo();

							} else if (mode.equalsIgnoreCase(PAUSE_MODE)) {

								if (!TestUtility.getModeOfDisplay()
										.equalsIgnoreCase(HELP_MODE)
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase("-h")
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase("--help")
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase(RESULT_MODE)
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase(PAUSE_MODE)) {
									priviousMode = TestUtility
											.getModeOfDisplay();
								}

								if (isPaused) {
									isPaused = false;
									threadControl.resume();
									TestUtility.printMessage(NORMAL,
											"Test execution RESUMED");
								} else {
									isPaused = true;
									if (CONFIG_INSTANCE.getValue(
											"MITESTER_MODE").equals("ADVANCED")) {
										if (TestResult.getTestID() != null) {
											TestUtility
													.printMessage(
															NORMAL,
															"Test execution will be suspended after the completion of "
																	+ TestResult
																			.getTestID());
										} else {

											TestUtility
													.printMessage(NORMAL,
															"Test execution will be suspended after the completion of current Test");

										}
									}
									// suspend the thread 
									threadControl.suspend();
								}
							} else if (mode.equalsIgnoreCase(STOP_MODE)) {
								threadControl.setStopExecution(true);

							} else {

								if (!TestUtility.getModeOfDisplay()
										.equalsIgnoreCase(HELP_MODE)
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase("-h")
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase("--help")
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase(RESULT_MODE)
										&& !TestUtility.getModeOfDisplay()
												.equalsIgnoreCase(PAUSE_MODE)) {
									priviousMode = TestUtility
											.getModeOfDisplay();
								}

								// set mode of display
								TestUtility.setModeOfDisplay(mode);

								TestUtility.callFlow();

							}

						}

					}

				} catch (Exception ex) {

				} finally {

					try {
						SYSTEM_IN.close();
					} catch (Exception ex) {

					}

				}

			}
		};
	}

	/**
	 * This method is used to read the inputs from the console window
	 * 
	 * @return console message
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private String readMessage() throws IOException, InterruptedException {
		return SYSTEM_IN.readLine();
	}

	/**
	 * This method displays the execution status
	 */

	private void printExecutionStatus() {

		LOGGER.info("called printExecutionStatus");

		TestUtility.printMessage(RESULT, String.format("%s",
				"\t\t\t TEST EXECUTION STATUS"));

		TestUtility.printMessage(RESULT, LINE_SEPARATOR);

		TestUtility.printMessage(RESULT, String.format("%s%13s%5d ",
				"\tTotal number of test cases ", "=", testExecutor
						.getTotalTestCount()));

		TestUtility.printMessage(RESULT, String.format("%s%10s%5d ",
				"\tNumber of test cases executed ", "=", testExecutor
						.getExecutedTestCount()));

		TestUtility.printMessage(RESULT, String.format("%s%12s%5d ",
				"\tNumber of test cases passed ", "=", testExecutor
						.getPassCount()));

		TestUtility
				.printMessage(RESULT, String.format("%s%12s%5d ",
						"\tNumber of test cases failed ", "=", (testExecutor
								.getExecutedTestCount() - testExecutor
								.getPassCount())));

		TestUtility.printMessage(RESULT, String.format("%s%4s%5d ",
				"\tNumber of test cases to be executed ", "=", (testExecutor
						.getTotalTestCount() - testExecutor
						.getExecutedTestCount())));

		TestUtility.printMessage(RESULT, LINE_SEPARATOR);

		TestUtility
				.printMessage(
						RESULT,
						String
								.format(
										"%s",
										"\tpress 'n' with ENTER key to go to Normal window or press 'q' with ENTER key to previous window"));

	}

	/**
	 * print the help information
	 */
	public static void printHelpInfo() {

		// LOGGER.info("called printHelpInfo");

		TestUtility.printMessage(HELP,
				"\tc - Press 'c' + 'ENTER' to traverse to the CALL FLOW window "
						+ LINE_SEPARATOR);

		TestUtility.printMessage(HELP,
				"\tl - Press 'l' + 'ENTER' to traverse to the LOG window "
						+ LINE_SEPARATOR);

		TestUtility.printMessage(HELP,
				"\tn - Press 'n' + 'ENTER' to traverse to the NORMAL window "
						+ LINE_SEPARATOR);

		TestUtility.printMessage(HELP,
				"\tr - Press 'r' + 'ENTER' to traverse to the RESULT window "
						+ LINE_SEPARATOR);

		TestUtility.printMessage(HELP,
				"\tq - Press 'q' + 'ENTER' to traverse to the previous window "
						+ LINE_SEPARATOR);

		TestUtility.printMessage(HELP,
				"\tp - Press 'p' + 'ENTER' to suspend/resume the miTester execution "
						+ LINE_SEPARATOR);

		TestUtility.printMessage(HELP,
				"\tt - Press 't' + 'ENTER' to stop the miTester execution "
						+ LINE_SEPARATOR);

		TestUtility.printMessage(HELP,
				"\th - Press 'h' + 'ENTER' to traverse to the HELP window "
						+ LINE_SEPARATOR);
	}

}
