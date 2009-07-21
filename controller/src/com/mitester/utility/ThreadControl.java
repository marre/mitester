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

/* miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications. 
 */

package com.mitester.utility;

import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * 
 * It controls execution of threads of miTester
 * 
 */

public class ThreadControl {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(ThreadControl.class.getName());

	private final Object lock = new Object();

	private boolean isThreadStop = false;

	private int stopCount = 0;

	private ScheduledFuture<?> checkTimer = null;

	private ScheduledExecutorService checkScheduler = Executors
			.newScheduledThreadPool(1);

	private boolean isStopExecution = false;

	private String testStartTime = null;

	public ThreadControl() {
		testStartTime = TestUtility.getTime();
	}

	/**
	 * controls thread execution
	 * 
	 * @throws InterruptedException
	 */
	public void take() throws InterruptedException {

		LOGGER.info("entered into take");

		synchronized (lock) {
			while (isThreadStop) {
				LOGGER.info("thread suspended");
				lock.wait();
			}
			LOGGER.info("thread released");

			if (stopCount > 0)
				isThreadStop = true;
		}
	}

	/**
	 * suspend the thread execution
	 */
	public void suspend() {
		LOGGER.info("entered into suspend");
		stopCount++;
		isThreadStop = true;
	}

	/**
	 * resume the thread execution
	 */
	public void resume() {
		LOGGER.info("entered into resume");
		synchronized (lock) {
			isThreadStop = false;

			if (stopCount > 0)
				stopCount--;

			lock.notifyAll();
		}
	}

	/**
	 * It starts the timer after starting SUT in ADVANCED mode. It will stop
	 * execution of miTester in case it expires
	 * 
	 */

	public void startCheckTimer() {

		final int waitTime;
		LOGGER.info("client check timer started");

		if (CONFIG_INSTANCE.isKeyExists("MAX_WAIT_TIME"))
			waitTime = Integer.parseInt(CONFIG_INSTANCE
					.getValue("MAX_WAIT_TIME"));
		else
			waitTime = 100;

		checkTimer = checkScheduler.schedule(new Runnable() {
			public void run() {
				LOGGER
						.error("TCP connection is not yet established between SUT and miTester");
				TestUtility
						.printMessage("NORMAL",
								"TCP connection is not yet established between SUT and miTester");

				// close the input, output and error streams
				TestUtility.close();

				LOGGER.info("miTester [STOPPED]");

				System.exit(0);

			}
		}, waitTime, TimeUnit.SECONDS);
	}

	/**
	 * it stops the execution of check timer
	 */
	public void stopCheckTimer() {

		if (checkTimer != null) {
			checkTimer.cancel(true);
			checkTimer = null;
			LOGGER.info("checkTimer is stopped");
		}
	}

	/**
	 * return thread stop status
	 * 
	 * @return isThreadStop status
	 */
	public boolean isThreadStop() {
		return isThreadStop;
	}

	/**
	 * set the stop execution status
	 */

	public void setStopExecution(boolean isStopExecution) {
		this.isStopExecution = isStopExecution;
	}

	/**
	 * return the stop execution status
	 */
	public boolean getStopExecution() {
		return isStopExecution;
	}

	/**
	 * stops the miTester Execution
	 */
	public void stopExecution() {

		// print the test result
		TestResult.printResult(testStartTime, TestUtility.getTime());

		LOGGER.info("test execution is stopped");
		TestUtility.printMessage("NORMAL", "test execution is stopped");

		System.exit(0);

	}

}
