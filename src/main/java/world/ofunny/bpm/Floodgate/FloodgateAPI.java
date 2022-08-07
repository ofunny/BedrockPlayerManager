/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.Floodgate;

import world.ofunny.bpm.Utils.Logger;

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
		floodgateAPI = new FloodgateHolder();
		Logger logger = Logger.get();
		logger.debugLogInfo("automatically initialising Floodgate API");

	}//end constructor
}// end class FloodgateAPI
