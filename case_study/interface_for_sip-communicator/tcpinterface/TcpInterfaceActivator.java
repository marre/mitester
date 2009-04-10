package net.java.sip.communicator.impl.tcpinterface;

import java.util.Hashtable;
import java.util.Map;

import net.java.sip.communicator.service.configuration.ConfigurationService;
import net.java.sip.communicator.service.gui.AccountRegistrationWizard;
import net.java.sip.communicator.service.gui.UIService;
import net.java.sip.communicator.service.protocol.OperationSetBasicTelephony;
import net.java.sip.communicator.service.protocol.ProtocolNames;
import net.java.sip.communicator.service.protocol.ProtocolProviderFactory;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class TcpInterfaceActivator implements BundleActivator {

	public static BundleContext bundleContext = null;
	private static final Map<Object, ProtocolProviderFactory> PROVIDER_FACTORIES_MAP = new Hashtable<Object, ProtocolProviderFactory>();
	private static UIService uiService = null;
	private static AccountRegistrationWizard accRegWizz = null;
	private static ConfigurationService configService = null;

	private TcpInterfaceServiceImpl tcpServiceImpl = null;
	private static OperationSetBasicTelephony callServicewiz = null;

	/**
	 * Called when this bundle is started.
	 * 
	 * @param bundleContext
	 *            The execution context of the bundle being started.
	 */
	public void start(BundleContext bundleContext) throws Exception {
		TcpInterfaceActivator.bundleContext = bundleContext;
		tcpServiceImpl = new TcpInterfaceServiceImpl();
		tcpServiceImpl.tcpStartService();

	}

	/**
	 * Called when this bundle is stopped
	 * 
	 * @param bundleContext
	 *            The execution context of the bundle being stopped.
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		tcpServiceImpl.setExitThread(true);
	}

	/**
	 * @return the <tt>UIService</tt> obtained from the bundle context
	 */
	public static UIService getUIService() {
		if (uiService == null) {
			ServiceReference versionServiceReference = bundleContext
					.getServiceReference(UIService.class.getName());
			uiService = (UIService) bundleContext
					.getService(versionServiceReference);
		}
		return uiService;
	}

	/**
	 * Returns the <tt>ConfigurationService</tt> obtained from the bundle
	 * context.
	 * 
	 * @return the <tt>ConfigurationService</tt> obtained from the bundle
	 *         context
	 */
	public static ConfigurationService getConfigurationService() {
		if (configService == null) {
			ServiceReference configReference = bundleContext
					.getServiceReference(ConfigurationService.class.getName());

			configService = (ConfigurationService) bundleContext
					.getService(configReference);
		}

		return configService;
	}

	/**
	 * Returns the <tt>AccountRegistrationWizard</tt> obtained from the bundle
	 * context.
	 * 
	 * @return the <tt>AccountRegistrationWizard</tt> obtained from the bundle
	 *         context
	 * @throws
	 */
	public static AccountRegistrationWizard getAccountRegistrationWizardService() {
		try {

			ServiceReference serviceReference[] = bundleContext
					.getServiceReferences(AccountRegistrationWizard.class
							.getName(), null);
			for (int i = 0; i < serviceReference.length; i++) {
				accRegWizz = (AccountRegistrationWizard) bundleContext
						.getService(serviceReference[i]);

				if ((accRegWizz != null)
						&& (accRegWizz instanceof net.java.sip.communicator.plugin.sipaccregwizz.SIPAccountRegistrationWizard)) {
					break;
				}

			}

			return accRegWizz;
		} catch (InvalidSyntaxException ex) {
			System.out.println("AccountRegistrationWizard :" + ex);
			return null;

		} catch (NullPointerException ex) {
			System.out.println("AccountRegistrationWizard" + ex);
			return null;

		} catch (Exception ex) {
			System.out.println("AccountRegistrationWizard" + ex);
			return null;

		}

	}

	/**
	 * Returns the <tt>OperationSetBasicTelephony</tt> obtained from the bundle
	 * context.
	 * 
	 * @return the <tt>OperationSetBasicTelephony</tt> obtained from the bundle
	 *         context
	 */
	public static OperationSetBasicTelephony getCreateCallService() {
		try {

			ServiceReference serviceReference[] = bundleContext
					.getServiceReferences(ProtocolProviderService.class
							.getName(), "(" + ProtocolProviderFactory.PROTOCOL
							+ "=" + ProtocolNames.SIP + ")");

			for (int i = 0; i < serviceReference.length; i++) {
				ProtocolProviderService sip = (ProtocolProviderService) bundleContext
						.getService(serviceReference[i]);
				callServicewiz = (OperationSetBasicTelephony) sip
						.getOperationSet(OperationSetBasicTelephony.class);

				if ((callServicewiz != null)
						&& (callServicewiz instanceof net.java.sip.communicator.service.protocol.AbstractOperationSetBasicTelephony)) {
					break;
				}
			}

			return callServicewiz;

		} catch (InvalidSyntaxException ex) {
			System.out.println("AccountRegistrationWizard :" + ex);
			return null;

		} catch (NullPointerException ex) {
			System.out.println("AccountRegistrationWizard" + ex);
			return null;

		} catch (Exception ex) {
			System.out.println("AccountRegistrationWizard" + ex);
			return null;
		}

	}

	/**
	 * Returns the ProtocolProviderFactory Map obtained from the bundle context.
	 * 
	 * @return the ProtocolProviderFactory Map obtained from the bundle context
	 */

	public static Map<Object, ProtocolProviderFactory> getProtocolProviderFactories() {
		ServiceReference[] serRefs = null;
		try {
			serRefs = bundleContext.getServiceReferences(
					ProtocolProviderFactory.class.getName(), null);

		} catch (InvalidSyntaxException ex) {
			System.out.println("AccountRegistrationWizard :" + ex);

		} catch (NullPointerException ex) {
			System.out.println("AccountRegistrationWizard" + ex);

		} catch (Exception ex) {
			System.out.println("AccountRegistrationWizard" + ex);
		}

		for (int i = 0; i < serRefs.length; i++) {
			ProtocolProviderFactory providerFactory = (ProtocolProviderFactory) bundleContext
					.getService(serRefs[i]);

			PROVIDER_FACTORIES_MAP.put(serRefs[i]
					.getProperty(ProtocolProviderFactory.PROTOCOL),
					providerFactory);
		}
		return PROVIDER_FACTORIES_MAP;
	}

	/**
	 * Returns the ProtocolProviderFactory Map obtained from the bundle context.
	 * 
	 * @return the ProtocolProviderFactory Map obtained from the bundle context
	 */

	public static ProtocolProviderFactory getProtocolProviderFactory(
			ProtocolProviderService protocolProvider) {

		ServiceReference[] serRefs = null;

		String osgiFilter = "(" + ProtocolProviderFactory.PROTOCOL + "="
				+ protocolProvider.getProtocolName() + ")";

		try {
			serRefs = TcpInterfaceActivator.bundleContext.getServiceReferences(
					ProtocolProviderFactory.class.getName(), osgiFilter);
		} catch (InvalidSyntaxException ex) {
			System.out.println("AccountRegistrationWizard :" + ex);

		} catch (NullPointerException ex) {
			System.out.println("AccountRegistrationWizard" + ex);

		} catch (Exception ex) {
			System.out.println("AccountRegistrationWizard" + ex);
		}

		return (ProtocolProviderFactory) TcpInterfaceActivator.bundleContext
				.getService(serRefs[0]);
	}
}
