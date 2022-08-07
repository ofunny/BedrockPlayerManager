/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.Floodgate;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

class FloodgateHolder implements Floodgate {
	
	/*
	 * Members
	 */
	private final FloodgateApi floodgateApi;
	
	/**
	 * Constructor
	 */
	public FloodgateHolder() {
		
		// Just holding the reference to save the lookup each call.
		floodgateApi = FloodgateApi.getInstance();
		
	}// end Floodgate
	
	/**
	 * Check if user is coming from Floodgate
	 */
	public boolean isBedrockPlayer(Player player) {
		return floodgateApi.isFloodgatePlayer(player.getUniqueId());
	}// end isBedrockPlayer 
	
}// end class Floodgate