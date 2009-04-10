/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: ClientStarter.java
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * This class consists of java native methods which are used to start and stop
 * the SUT.
 * 
 */

public class ClientStarter {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(ClientStarter.class.getName());

	// Native APIs 
	public native String getProcessDetails();

	public native int killProcess(String processBefStart,
			String processAfStart, String processName);

	public native int startApplication(String testAppPath);

	private static final String OS_NAME = System.getProperty("os.name");

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	// load the killApp library 
	static {

		try {
			if (OS_NAME.startsWith("Windows")) {
				String dllPath = new java.io.File(".").getCanonicalPath()
						+ "\\native\\windows\\miTester.dll";
				System.load(dllPath);
			}
		} catch (IOException ex) {
			TestUtility.printError("Error at loading miTester.dll", ex);
		} catch (Exception ex) {
			TestUtility.printError("Error at loading miTester.dll", ex);
		}

	}

	public ClientStarter() {
	}

	/**
	 * This method is used to start the SUT
	 * 
	 * @param testApplicationPath
	 *            is a String value specifies the path of the executable
	 * @return is a boolean value represents true if the SUT started
	 *         successfully
	 */
	public boolean startClient(String testApplicationPath)
			throws NullPointerException, IllegalArgumentException,
			InterruptedException, IOException {
		boolean isClientStart = false;
		if (OS_NAME.startsWith("Linux")) {
			FileWriter fiw = null;
			BufferedWriter put = null;
			int shFileindex = testApplicationPath.lastIndexOf("/");
			String workspacePath = new java.io.File(".").getCanonicalPath();
			String shPath = workspacePath + "/" + "testApp.sh";
			fiw = new FileWriter(shPath);
			put = new BufferedWriter(fiw);
			put.write("cd " + testApplicationPath.substring(0, shFileindex));
			put.write(LINE_SEPARATOR);
			put.write("sh ./"
					+ testApplicationPath.substring(shFileindex + 1,
							testApplicationPath.length()));
			put.write(LINE_SEPARATOR);
			put.close();
			fiw.close();
			String cmd = "bash " + shPath;
			Runtime.getRuntime().exec(cmd);
			isClientStart = true;

		} else if (OS_NAME.startsWith("Windows")) {
			int ret = this.startApplication(testApplicationPath);
			if (ret == 0) {
				isClientStart = false;
			} else {
				isClientStart = true;
			}
		}

		return isClientStart;
	}

	/**
	 * This method is used to stop the SUT
	 * 
	 * @param testApplicationPath
	 *            is a String value specifies the path of the executable
	 * @param processDetails
	 *            is a String object contains list of processes were running
	 *            before start the SUT
	 * @return is a boolean value represents true if the SUT started
	 *         successfully
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public boolean stopClient(String testApplicationPath,
			String processBefStart, String processAfStart)
			throws NullPointerException, IllegalArgumentException,
			InterruptedException, IOException {
		boolean isClientStop = false;
		if (OS_NAME.startsWith("Linux")) {
			String shPath = new java.io.File(".").getCanonicalPath() + "/"
					+ "testApp.sh";

			if (TestUtility.isFileExist(shPath)) {
				new File(shPath).delete();

			}
			String processes[] = processAfStart.split(";");
			LOGGER.info("no of processes " + processes.length);
			for (int i = 0; i < processes.length; i++) {
				String procPath = "/proc/" + processes[i];
				if ((TestUtility.isFileExist(procPath)))
				{
					Runtime.getRuntime().exec("kill -9 " + processes[i]);
				}
			}
			isClientStop = true;

		} else if (OS_NAME.startsWith("Windows")) {
			int batFileindex = testApplicationPath.lastIndexOf("/");
			String exeName = testApplicationPath.substring(batFileindex + 1,
					testApplicationPath.length());
			int ret = this
					.killProcess(processBefStart, processAfStart, exeName);
			if (ret == 0) {
				isClientStop = false;
			} else {
				isClientStop = true;
			}
		}
		return isClientStop;
	}

	/**
	 * This method return the Win32 Processes currently running on windows
	 * 
	 * @return is a String object represents list of processes
	 */
	public String getProcessInfo() {
		String processDetails = null;

		if (OS_NAME.startsWith("Linux")) {

			StringBuilder processIds = new StringBuilder();

			File directoryPath = new File("/proc");

			String processList[] = directoryPath.list();

			for (int i = 0; i < processList.length; i++) {
				processIds.append(processList[i]);
				processIds.append(";");
			}

			processDetails = processIds.toString();

		} else if (OS_NAME.startsWith("Windows")) {
			return this.getProcessDetails();
		}
		return processDetails;
	}

}
