package net.java.sip.communicator.impl.changestatus;

import java.util.Iterator;
import java.util.List;

import net.java.sip.communicator.service.configuration.ConfigurationService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ChangeStatusActivator implements BundleActivator {

	private BundleContext bundleContext = null;
	private ConfigurationService configService = null;

	/**
	 * Called when this bundle is started.
	 * 
	 * @param bundleContext
	 *            The execution context of the bundle being started.
	 */
	public void start(BundleContext bundleContext) throws Exception {
		
		this.bundleContext = bundleContext;

		System.out.println("changeStatusActivator started");
		ConfigurationService configService = this.getConfigurationServiceobj();
		String prefix = "net.java.sip.communicator.impl.gui.accounts";
		List<String> accounts = configService.getPropertyNamesByPrefix(prefix,
				true);
		Iterator<String> accountsIter = accounts.iterator();
		System.out.println("size " + accounts.size());
		if (accounts.size() > 0) {
			while (accountsIter.hasNext()) {
				String accountRootPropName = (String) accountsIter.next();
				String accountUID = configService
						.getString(accountRootPropName);
				configService.setProperty(accountRootPropName
						+ ".lastAccountStatus", (Object) "Offline");
				System.out.println(accountUID + " set to offline");
			}
		}
	}

	/**
	 * Called when this bundle is stopped
	 * 
	 * @param bundleContext
	 *            The execution context of the bundle being stopped.
	 */

	public void stop(BundleContext arg0) throws Exception {
		System.out.println("changeStatusActivator stopped");

	}

	/**
	 * Returns the <tt>ConfigurationService</tt> obtained from the bundle
	 * context.
	 * 
	 * @return the <tt>ConfigurationService</tt> obtained from the bundle
	 *         context
	 */
	private ConfigurationService getConfigurationServiceobj() {

		if (configService == null) {
			ServiceReference configReference = bundleContext
					.getServiceReference(ConfigurationService.class.getName());

			configService = (ConfigurationService) bundleContext
					.getService(configReference);
		}
		return configService;
	}

}
