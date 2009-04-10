package net.java.sip.communicator.impl.gui.main.login;


	import org.osgi.framework.BundleActivator;
	import org.osgi.framework.BundleContext;

	import net.java.sip.communicator.util.Logger;

	public class LoginManagerActivator implements BundleActivator
	{
		public LoginManagerActivator(){
			
		}
		
	    /**
	     * A currently valid bundle context.
	     */
	    
	    private static Logger logger = Logger.getLogger(
	    		LoginManagerActivator.class.getName());	
		
	    /**
	     * Called when this bundle is started.
	     *
	     * @param bundleContext The execution context of the bundle being started.
	     */
	    public void start(BundleContext bundleContext) throws Exception
	    {
	        try {
	        	
	        	LoginManagerActivator loginManagerActivator = new LoginManagerActivator();
	        	
	            logger.logEntry();
	            
	            logger.info("LoginManagerActivator...[  STARTED ]");

	            bundleContext.registerService(LoginManagerActivator.class.getName(),
	            		loginManagerActivator, null);

	            logger.info("LoginManagerActivator ...[REGISTERED]");
	            
	        } finally 
	        {
	            logger.logExit();
	        }
	    }
	    
	    /**
	     * Called when this bundle is started.
	     *
	     * @param bundleContext The execution context of the bundle being stopped.
	     */
	    public void stop(BundleContext bundleContext) throws Exception
	    {
	        logger.info("LoginManagerActivator ...[STOPPED]");
	    }

	}

