/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.Floodgate;

import world.ofunny.bpm.Utils.Logger;
import world.ofunny.bpm.config.Config;

public class FloodgateAPI {
	
	/*
	 * Initialisation.
	 */
	private static FloodgateAPI INSTANCE = null;
	private final  Floodgate floodgateAPI;
	
	/**
	 * Instance provider for this singleton,
	 */
	public static Floodgate get() {
		if (INSTANCE == null) {
			//synchronized block to remove overhead
			synchronized (FloodgateAPI.class) {
				INSTANCE = new FloodgateAPI();
			}// end synchronized
		}// end if instance already exists
		return INSTANCE.floodgateAPI;
	}// end Config
	
	/**
	 * constructor
	 */
	FloodgateAPI() {
		
		Logger logger = Logger.get();
		switch(Config.get().getFloodgateVersion()) {
			case "2":
				logger.debugLogInfo("manually initialising Floodgate API version 2.x (please make sure you intalled the Floodgate 2.x correctly)!");
	        	floodgateAPI = new Floodgate_2_0();
	            break;
			case "1":
				logger.debugLogInfo("manually initialising Floodgate API version 1.x (please make sure you intalled the Floodgate 1.x correctly)!");
            	floodgateAPI = new Floodgate_1_0();
                break;
            default:
            	// performing auto detection …
            	if(isClass("org.geysermc.floodgate.api.FloodgateApi")) {
            		// Floodgate 2.x has been found!
            		logger.debugLogInfo("automatically initialising Floodgate API version 2.x!");
            		floodgateAPI = new Floodgate_2_0();
            	} else if(isClass("org.geysermc.floodgate.FloodgateAPI")) {
            		// Floodgate 1.x has been found!
            		logger.debugLogInfo("automatically initialising Floodgate API version 1.x!");
            		floodgateAPI = new Floodgate_1_0();
            	} else {
            		// Floodgate has not been found!
            		logger.logError("neither Floodgate API version 1.x nor 2.x has been detected – please install the Floodgate plugin correctly!");
            		floodgateAPI = new Floodgate_Dummy();
            	}// end if Floodgate 1, 2 or none
        }// end switch version

	}//end constructor
	
	/**
	 * Tests if a given class exists and returns true or false
	 * @param className
	 * @return
	 */
	public boolean isClass(String className) {
	    try  {
	        Class.forName(className);
	        return true;
	    }  catch (ClassNotFoundException e) {
	        return false;
	    }
	}// end isClass

}// end class FloodgateAPI
