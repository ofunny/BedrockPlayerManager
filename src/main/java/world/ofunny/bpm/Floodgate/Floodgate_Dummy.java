/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.Floodgate;
import org.bukkit.entity.Player;

import world.ofunny.bpm.Utils.Logger;

class Floodgate_Dummy implements Floodgate {

	/**
	 * Will always return false (everybody will be a Java player).
	 * Used when Floodgate is missing in solo mode (usage without Floodgate) or could not be found.
	 */
	public boolean isBedrockPlayer(Player player) {
		return false;
	}// end isBedrockPlayer 
	
}// end class Floodgate_1_0