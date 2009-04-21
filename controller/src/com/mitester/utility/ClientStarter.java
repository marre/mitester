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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * This class consists of java native methods which are used to start and stop
 * the SUT.
 * 
 */

public class ClientStarter {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(ClientStarter.class.getName());

	private static final String OS_NAME = System.getProperty("os.name");

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	private static final String FILE_SEPARATOR = System
			.getProperty("file.separator");
	private static final String SEMI_COLON_SEPARATOR = ";";

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
		if ((OS_NAME.startsWith("Linux")) || (OS_NAME.startsWith("SunOS"))
				|| (OS_NAME.startsWith("Solaris"))
				|| (OS_NAME.startsWith("Mac"))) {
			FileWriter fiw = null;
			BufferedWriter put = null;
			int shFileindex = testApplicationPath.lastIndexOf(FILE_SEPARATOR);
			String workspacePath = new java.io.File(".").getCanonicalPath();
			String shPath = workspacePath + FILE_SEPARATOR + "testApp.sh";
			fiw = new FileWriter(shPath);
			put = new BufferedWriter(fiw);
			put.write("cd " + testApplicationPath.substring(0, shFileindex));
			put.write(LINE_SEPARATOR);
			put.write("sh ."
					+ FILE_SEPARATOR
					+ testApplicationPath.substring(shFileindex + 1,
							testApplicationPath.length()));
			put.write(LINE_SEPARATOR);
			put.close();
			fiw.close();
			String cmd[] = { "bash", shPath };
			Runtime.getRuntime().exec(cmd);
			isClientStart = true;

		} else if (OS_NAME.startsWith("Windows")) {
			FileWriter fiw = null;
			BufferedWriter put = null;
			String ClientPath = testApplicationPath;
			int batFileindex1 = ClientPath.lastIndexOf("/");
			int batFileindex2 = ClientPath.lastIndexOf("\\");
			String batchPath = null;
			if (batFileindex1 >= 0) {
				batchPath = ClientPath.substring(0, batFileindex1) + "/"
						+ "runExe.bat";
			} else if (batFileindex2 >= 0) {
				batchPath = ClientPath.substring(0, batFileindex2) + "/"
						+ "runExe.bat";
			}

			fiw = new FileWriter(batchPath);
			put = new BufferedWriter(fiw);
			if (batFileindex1 >= 0) {
				put.write(ClientPath.substring(batFileindex1 + 1, ClientPath
						.length()));
			} else if (batFileindex2 >= 0) {
				put.write(ClientPath.substring(batFileindex2 + 1, ClientPath
						.length()));
			}

			put.write(LINE_SEPARATOR);
			put.close();
			fiw.close();

			if (batFileindex1 >= 0) {
				String cmd1[] = { "cmd", "/c", "start", "/B", "/D",
						ClientPath.substring(0, batFileindex1 + 1),
						"runExe.bat" };
				Runtime.getRuntime().exec(cmd1).waitFor();
			} else if (batFileindex2 >= 0) {
				String cmd2[] = { "cmd", "/c", "start", "/B", "/D",
						ClientPath.substring(0, batFileindex2 + 1),
						"runExe.bat" };
				Runtime.getRuntime().exec(cmd2).waitFor();
			}
			isClientStart = true;
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
		if ((OS_NAME.startsWith("Linux")) || (OS_NAME.startsWith("SunOS"))
				|| (OS_NAME.startsWith("Solaris"))
				|| (OS_NAME.startsWith("Mac"))) {
			String shPath = new java.io.File(".").getCanonicalPath()
					+ FILE_SEPARATOR + "testApp.sh";

			if (TestUtility.isFileExist(shPath)) {
				new File(shPath).delete();

			}
			String processes[] = processAfStart.split(SEMI_COLON_SEPARATOR);
			LOGGER.info("no of processes " + processes.length);
			for (int i = 0; i < processes.length; i++) {
				Runtime.getRuntime().exec("kill -9 " + processes[i]);
			}
			isClientStop = true;

		} else if (OS_NAME.startsWith("Windows")) {
			String processes[] = processAfStart.split(SEMI_COLON_SEPARATOR);
			LOGGER.info("no of processes " + processes.length);
			for (int i = 0; i < processes.length; i++) {
				Runtime.getRuntime().exec("tskill " + processes[i]);
			}
			isClientStop = true;
		}
		return isClientStop;
	}

	/**
	 * This method return the Win32 Processes currently running on windows
	 * 
	 * @return is a String object represents list of processes
	 * @throws IOException
	 */
	public String getProcessInfo() throws IOException {
		String processDetails = null;

		if ((OS_NAME.startsWith("SunOS")) || (OS_NAME.startsWith("Solaris"))) {

			StringBuilder processIds = new StringBuilder();

			File directoryPath = new File("/proc");

			String processList[] = directoryPath.list();

			for (int i = 0; i < processList.length; i++) {
				processIds.append(processList[i]);
				processIds.append(SEMI_COLON_SEPARATOR);
			}

			processDetails = processIds.toString();
		}

		else if ((OS_NAME.startsWith("Linux")) || (OS_NAME.startsWith("Mac"))) {

			StringBuilder tempbuf = new StringBuilder();

			StringBuilder processIds = new StringBuilder();

			Process p = Runtime.getRuntime().exec("ps x");

			int c = -1;

			InputStream inputStream = p.getInputStream();

			while ((c = inputStream.read()) != -1) {

				tempbuf.append((char) c);
			}

			inputStream.close();

			String temp[] = tempbuf.toString().split(LINE_SEPARATOR);

			for (int i = 0; i < temp.length; i++) {

				String line = temp[i];

				line = line.trim();

				if (line.startsWith("PID")) {
					continue;
				} else {
					processIds.append(line.substring(0, line.indexOf(" ")));
					processIds.append(SEMI_COLON_SEPARATOR);

				}
			}

			processDetails = processIds.toString();

		} else if (OS_NAME.startsWith("Windows")) {

			String line;

			StringBuilder processIds = new StringBuilder();

			Process p = Runtime.getRuntime().exec(
					System.getenv("windir") + "\\system32\\" + "tasklist.exe");

			BufferedReader input = new BufferedReader(new InputStreamReader(p
					.getInputStream()));

			while ((line = input.readLine()) != null) {
				int iConsole = line.trim().indexOf(" Console");
				if (iConsole >= 0) {
					String[] consoleSplit = line.trim().split(" Console");
					if (consoleSplit.length != 0) {
						String str1 = consoleSplit[0];
						int position = str1.lastIndexOf("  ");
						String pids = (String) str1.substring(position, str1
								.length());
						processIds.append(pids.trim() + SEMI_COLON_SEPARATOR);
					}
				}
			}
			input.close();
			processDetails = processIds.toString();
		}
		return processDetails;
	}

}
