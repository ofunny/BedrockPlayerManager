/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.Floodgate;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.FloodgateAPI;

class Floodgate_1_0 implements Floodgate {

	/*
	 * Check if user is coming from Floodgate
	 */
	public boolean isBedrockPlayer(Player player) {
		return FloodgateAPI.isBedrockPlayer(player);
	}// end isBedrockPlayer 
	
}// end class Floodgate_1_0