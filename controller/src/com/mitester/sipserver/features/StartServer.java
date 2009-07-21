/*
 * Project: mitesterforsip
 * Author: Mobax
 * Filename: UdpCommn.java
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
 * Jain SIP stack 				NIST-CONDITIONS-OF-USE 						        https://jain-sip.dev.java.net/source/browse/jain-sip/licenses/
 * Log4J 						The Apache Software License, Version 2.0 			http://logging.apache.org/log4j/1.2/license.html
 * JNetStreamStandalone lib     GNU Library or LGPL			     					http://sourceforge.net/projects/jnetstream/
 * 
 */

/*
 * miTester is a test automation framework developed for testing SIP applications. 
 * Developers and testers can simulate any kind of test cases and test applications.
 *  
 */
package com.mitester.sipserver.features;

import static com.mitester.sipserver.SipServerConstants.AUTHENTICATION;
import static com.mitester.sipserver.SipServerConstants.B2BUA_MODE;
import static com.mitester.sipserver.SipServerConstants.YES;
import static com.mitester.sipserver.SipServerConstants.PROXY_MODE;
import static com.mitester.sipserver.SipServerConstants.SERVER_MODE;
import static com.mitester.utility.ConfigurationProperties.CONFIG_INSTANCE;
import static com.mitester.utility.UtilityConstants.NORMAL;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

import javax.sip.PeerUnavailableException;

import com.mitester.sipserver.UdpCommn;
import com.mitester.utility.MiTesterLog;
import com.mitester.utility.TestUtility;

/**
 * A method used to start a miTester for SIP in Back To Back User Agent and
 * Proxy mode
 */
public class StartServer {

	private static final Logger LOGGER = MiTesterLog
			.getLogger(StartServer.class.getName());

	private ExecutorService serverExecutors = Executors.newCachedThreadPool();

	private CountDownLatch b2buaLatch = null;

	private CountDownLatch proxyLatch = null;

	/**
	 * To Start a B2BUA and Proxy
	 */
	public void startServer() {

		try {

			boolean isAuthEnabled = false;

			if ((CONFIG_INSTANCE.isKeyExists(AUTHENTICATION))
					&& (CONFIG_INSTANCE.getValue(AUTHENTICATION)
							.equals(YES))) {
				isAuthEnabled = true;
			}

			if (CONFIG_INSTANCE.getValue(SERVER_MODE).equalsIgnoreCase(B2BUA_MODE)) {

				TestUtility.printMessage(NORMAL,
						"miTester runnning in B2BUA mode");

				LOGGER.info("miTester runnning in B2BUA mode");

				b2buaLatch = new CountDownLatch(1);

				UdpCommn udpCommn = new UdpCommn(B2BUA_MODE);

				B2BUA b2bua = new B2BUA(this, udpCommn);

				// initialize B2BUA
				b2bua.initializeB2BUA();

				b2bua.setAuth(isAuthEnabled);

				// start B2BUA
				serverExecutors.execute(b2bua.runB2BUA());

				// wait for thread to finish
				b2buaLatch.await();

			} else if (CONFIG_INSTANCE.getValue(SERVER_MODE).equalsIgnoreCase(PROXY_MODE)) {

				TestUtility.printMessage(NORMAL,
						"miTester runnning in Proxy mode");

				LOGGER.info("miTester runnning in Proxy mode");

				proxyLatch = new CountDownLatch(1);

				UdpCommn udpCommn = new UdpCommn(PROXY_MODE);

				Proxy proxy = new Proxy(this, udpCommn);

				// initialize Proxy
				proxy.initializeProxy();

				proxy.setAuth(isAuthEnabled);

				// start proxy
				serverExecutors.execute(proxy.runProxy());

				// wait for thread to finish
				proxyLatch.await();

			}
		} catch (PeerUnavailableException ex) {
			TestUtility.printError(ex.getMessage(), ex);

		} catch (Exception ex) {
			TestUtility.printError(ex.getMessage(), ex);
		}

	}

	/**
	 * it returns b2bua CountDownLatch
	 * 
	 * @return
	 */

	public CountDownLatch getb2buaCountDownLatch() {

		return b2buaLatch;
	}

	/**
	 * it returns proxy CountDownLatch
	 * 
	 * @return CountDownLatch
	 */

	public CountDownLatch getproxyCountDownLatch() {

		return proxyLatch;
	}

}