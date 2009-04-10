package net.java.sip.communicator.impl.gui.main.call;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import net.java.sip.communicator.util.Logger;

public class GuiMainCallActivator implements BundleActivator
{
	public GuiMainCallActivator() {
		
	}
	
    /**
     * A currently valid bundle context.
     */
   
    private static Logger logger = Logger.getLogger(
    		GuiMainCallActivator.class.getName());	
	
    /**
     * Called when this bundle is started.
     *
     * @param bundleContext The execution context of the bundle being started.
     */
    public void start(BundleContext bundleContext) throws Exception
    {
        try {
        	
        	GuiMainCallActivator guiMainCallActivator = new GuiMainCallActivator();
        	       	           
            logger.logEntry();
            
            logger.info("GuiMainCallActivator...[  STARTED ]");

            bundleContext.registerService(GuiMainCallActivator.class.getName(),
            		guiMainCallActivator, null);

            logger.info("GuiMainCallActivator ...[REGISTERED]");
            
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
        logger.info("GuiMainCallActivator ...[STOPPED]");
    }

}
