package net.java.sip.communicator.impl.gui.utils;



	import org.osgi.framework.BundleActivator;
	import org.osgi.framework.BundleContext;

	import net.java.sip.communicator.util.Logger;

	public class GuiUtilsActivator implements BundleActivator
	{
		public GuiUtilsActivator() {
			
		}
		
	    /**
	     * A currently valid bundle context.
	     */
	    
	    private static Logger logger = Logger.getLogger(
	    		GuiUtilsActivator.class.getName());	
		
	    /**
	     * Called when this bundle is started.
	     *
	     * @param bundleContext The execution context of the bundle being started.
	     */
	    public void start(BundleContext bundleContext) throws Exception
	    {
	        try {
	        	
	        	GuiUtilsActivator guiUtilsActivator = new  GuiUtilsActivator();
	        	
	            logger.logEntry();
	            
	            logger.info("GuiUtilsActivator...[  STARTED ]");

	            bundleContext.registerService(GuiUtilsActivator.class.getName(),
	            		guiUtilsActivator, null);

	            logger.info("GuiUtilsActivator ...[REGISTERED]");
	            
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
	        logger.info("GuiUtilsActivator ...[STOPPED]");
	    }

	}