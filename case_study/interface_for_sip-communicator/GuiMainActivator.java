package net.java.sip.communicator.impl.gui.main;

 
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import net.java.sip.communicator.util.Logger;

public class GuiMainActivator implements BundleActivator
{
	public GuiMainActivator() {
		
	}
	
    /**
     * A currently valid bundle context.
     */
   
    private static Logger logger = Logger.getLogger(
    		GuiMainActivator.class.getName());	
	
    /**
     * Called when this bundle is started.
     *
     * @param bundleContext The execution context of the bundle being started.
     */
    public void start(BundleContext bundleContext) throws Exception
    {
        try {
        	
        	GuiMainActivator guiMainActivator = new GuiMainActivator();
        	            
            logger.logEntry();
            
            logger.info("GuiMainActivator...[  STARTED ]");

            bundleContext.registerService(GuiMainActivator.class.getName(),
            		guiMainActivator, null);

            logger.info("GuiMainActivator ...[REGISTERED]");
            
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
        logger.info("GuiMainActivator ...[STOPPED]");
    }

}
