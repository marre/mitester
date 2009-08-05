miTester for SIP - Release Version 2.1
---------------------------------------
Date: August 4th, 2009

Project Lead/Maintainer (2009-current): Mobax

Original Author: Mobax

Licensing Terms
--------------------
 This program is licensed under the GNU General Public License.

Note:
-----
- controller (holds the complete source and dependent jars to build miTester)

	- lib (contains the dependent jars to build miTester and lo4j property file)
		- jain-sip-api-1.2.jar
		- jain-sip-ri-1.2.90.jar
		- log4j-1.2.15.jar
                - activation.jar
                - jaxb-api-2.0.jar
                - jaxb-impl-2.0.jar
                - jaxb-xjc-2.0.jar
                - jsr173_1.0_api.jar
                - JNetStreamStandalone.jar
                - log4j.xml
		- validateHeader.properties

	- scripts (holds client and server scripts)
		- client (holds Client Script)
			- Sample_Client_Script.xml (sample client script)
		- server (holds Server Script)
			- Sample_Server_Script.xml (sample server script)

	- miTester.properties (property file)

        - media (holds media packets)
		- RTCP_SR.txt (RTCP SR packet)
		- RTP.txt (RTP packet)
		- RTP.pcap (RTP packet)
                - RTP_BYE.txt (RTP BYE packet)
                - RTP_RR.txt (RTP RR packet)
                - RTCP_SDES.txt (RTCP SDES packet)
	
        - src
		- com (holds the source files)

        - validate.xml (holds the header/presence validation details)


Description
--------------

miTester for SIP is an open source test automation framework to automate all SIP call flows.
miTester for SIP is to make testing easy and quick. As the miTester for SIP automation framework executes entirely on XML scripts, both USER and  ADVANCED  modes of testing can be performed on SIP applications. No additional scripting needed for repetitive testing.

Comments
--------------
  We like comments, even (especially) if they are a simple thanks.

Wishes
---------
  We can't possibly know what everyone wants, so we appreciate all feature requests.

Regards,
-miTester for SIP team




