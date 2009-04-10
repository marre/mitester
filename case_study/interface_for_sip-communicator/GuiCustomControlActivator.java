package net.java.sip.communicator.impl.gui.customcontrols;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import net.java.sip.communicator.util.Logger;

public class GuiCustomControlActivator implements BundleActivator
{
	public GuiCustomControlActivator() {
		
	}
 
    /**
     * A currently valid bundle context.
     */
    private static Logger logger = Logger.getLogger(
    		GuiCustomControlActivator.class.getName());	
	
    /**
     * Called when this bundle is started.
     *
     * @param bundleContext The execution context of the bundle being started.
     */
    public void start(BundleContext bundleContext) throws Exception
    {
        try {
        	
        	GuiCustomControlActivator guiCustomControlActivator = new GuiCustomControlActivator();
        	            
            logger.logEntry();
            
            logger.info("GuiCustomControlActivator...[  STARTED ]");

            bundleContext.registerService(GuiCustomControlActivator.class.getName(),
            		guiCustomControlActivator, null);

            logger.info("GuiCustomControlActivator ...[REGISTERED]");
            
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
        logger.info("GuiCustomControlActivator ...[STOPPED]");
    }

}

