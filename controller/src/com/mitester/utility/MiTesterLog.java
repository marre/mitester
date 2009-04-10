/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: MiTesterLog.java
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

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.io.IOException;

/**
 * This class used to create and format the log handler and also the create
 * LOGGER for the specified class
 * 
 */

public class MiTesterLog {

	private static Logger logger;

	private static FileHandler fileHandler;

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	private static final DecimalFormat TWO_DIGIT_FORMAT = new DecimalFormat(
			"00");

	private static final DecimalFormat THREE_DIGIT_FORMAT = new DecimalFormat(
			"000");

	static {
		try {

			boolean append = true;
			fileHandler = new FileHandler("miTester.log", append);
			fileHandler.setFormatter(new Formatter() {
				public String format(LogRecord rec) {

					StringBuilder buf = new StringBuilder();

					// current time
					Calendar cal = Calendar.getInstance();
					int hour = cal.get(Calendar.HOUR_OF_DAY);
					int minutes = cal.get(Calendar.MINUTE);
					int seconds = cal.get(Calendar.SECOND);
					int millis = cal.get(Calendar.MILLISECOND);

					buf.append(TWO_DIGIT_FORMAT.format(hour)).append(':');
					buf.append(TWO_DIGIT_FORMAT.format(minutes)).append(':');
					buf.append(TWO_DIGIT_FORMAT.format(seconds)).append('.');
					buf.append(THREE_DIGIT_FORMAT.format(millis)).append(' ');
					buf.append(' ');
					buf.append(rec.getLevel().getLocalizedName());
					buf.append("\t");
					buf.append(":");
					buf.append(' ');
					String loggerName = rec.getLoggerName();
					if (loggerName == null)
						loggerName = rec.getSourceClassName();

					if (loggerName.startsWith("com.")) {
						buf.append(loggerName.substring("com.".length()));
					} else
						buf.append(rec.getLoggerName());

					if (rec.getSourceMethodName() != null) {
						buf.append(".");
						buf.append(rec.getSourceMethodName());
						buf.append("() ");
					}
					buf.append(rec.getMessage());
					buf.append(LINE_SEPARATOR);
					return buf.toString();
				}
			});
		} catch (IOException e) {

		}
	}

	public static Logger getLogger(String name) {
		logger = Logger.getLogger(name);
		logger.addHandler(fileHandler);
		logger.setUseParentHandlers(false);
		return logger;
	}

}
