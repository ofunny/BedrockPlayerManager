/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.Floodgate;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

class Floodgate_2_0 implements Floodgate {
	
	/*
	 * Members
	 */
	private FloodgateApi floodgateApi;
	
	/**
	 * Constructor
	 */
	public Floodgate_2_0() {
		
		// Just holding the refernce to save the lookup each call.
		floodgateApi = FloodgateApi.getInstance();
		
	}// end Floodgate_2_0
	
	/**
	 * Check if user is coming from Floodgate
	 */
	public boolean isBedrockPlayer(Player player) {
		return floodgateApi.isFloodgatePlayer(player.getUniqueId());
	}// end isBedrockPlayer 
	
}// end class Floodgate_2_0