/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.Floodgate;

import world.ofunny.bpm.config.Config;

public class FloodgateAPI {
	
	/*
	 * Initialisation.
	 */
	private static FloodgateAPI INSTANCE = null;
	private final Floodgate floodgateAPI;
	
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

		if(Config.get().getFloodgateVersion() == 1) {
			floodgateAPI = new Floodgate_1_0();
		} else {
			floodgateAPI = new Floodgate_2_0();
		}// end if API 1.0 or 2.0
		
	}//end constructor

}// end class FloodgateAPI
