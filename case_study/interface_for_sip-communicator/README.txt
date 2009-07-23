miTester for SIP - Release Version 2.0
---------------------------------------
Date: July 20th, 2009

Project Lead/Maintainer (2009-current): Mobax

Original Author: Mobax

Licensing Terms
--------------------

This program is licensed under the GNU General Public License.

Notes
--------
- interface_for_sip-communicator (holds packages and files needed for automating the sip-communicator)

	- changestatus package (it must be placed under 'src/net/java/sip/communicator/impl' directory structure of sip-communicator source and it consists of two files which are specified below)
		- changestatus.manifest.mf 
                - ChangeStatusActivator.java

	- tcpinterface package (it must be placed under 'src/net/java/sip/communicator/impl' directory structure of sip-communicator source and it consists of three files which are specified below)
		- tcpinterface.manifest.mf 
                - TcpInterfaceActivator.java
                - TcpInterfaceServiceImpl.java
                - CallEventHandler.java
                - IncomingCallEventHandler.java
                - OutgoingCallEventHandler.java
                - RegisterEventHandler.java

	- GuiCustomControlActivator.java (it must be placed under 'src/net/java/sip/communicator/impl/gui/customcontrols' directory structure of sip-communicator source)

	- guicustomcontrol.manifest.mf (it must be placed under 'src/net/java/sip/communicator/impl/gui/customcontrols' directory structure of sip-communicator source)

	- guilogin.manifest.mf (it must be placed under 'src/net/java/sip/communicator/impl/gui/main/login' directory structure of sip-communicator source)

	- LoginManagerActivator.java (it must be placed under 'src/net/java/sip/communicator/impl/gui/main/login' directory structure of sip-communicator source)

	- guimain.manifest.mf (it must be placed under 'src/net/java/sip/communicator/impl/gui/main' directory structure of sip-communicator source)

	- GuiMainActivator.java (it must be placed under 'src/net/java/sip/communicator/impl/gui/main' directory structure of sip-communicator source)

	- guimaincall.manifest.mf(it must be placed under 'src/net/java/sip/communicator/impl/gui/main/call' directory structure of sip-communicator source)

	- GuiMainCallActivator.java (it must be placed under 'src/net/java/sip/communicator/impl/gui/main/call' directory structure of sip-communicator source)

	- guiutils.manifest.mf (it must be placed under 'src/net/java/sip/communicator/impl/gui/utils' directory structure of sip-communicator source)

	- GuiUtilsActivator.java (it must be placed under 'src/net/java/sip/communicator/impl/gui/utils' directory structure of sip-communicator source)

	- sipaccregwizz.manifest.mf (add 'Export-Package: net.java.sip.communicator.plugin.sipaccregwizz' in the file. file path: 'src/net/java/sip/communicator/plugin/sipaccregwizz')

	- swing.ui.manifest.mf (add 'Export-Package: net.java.sip.communicator.impl.gui.GuiActivator,
 net.java.sip.communicator.impl.gui' in the file. file path: 'src/net/java/sip/communicator/gui')

	- felix.client.run.properties (it must be placed under 'lib' directory structure of sip-communicator source)

	- build.xml (it needs to be replaced in the working directory)

Description
--------------

miTester for SIP is an open source test automation framework to automate all SIP call flows.
miTester for SIP is to make testing easy and quick. As the miTester for SIP automation framework executes entirely on XML scripts, both USER and  ADVANCED  modes of testing can be performed on SIP applications. No additional scripting needed for repetitive testing.
miTester for SIP works on Windows, Linux, MAC and Solaris Platforms.

Comments
--------------
  We like comments, even (especially) if they are a simple thanks.

Wishes
---------
  We can't possibly know what everyone wants, so we appreciate all feature requests.

Regards,
-miTester for SIP team








