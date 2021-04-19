/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.Floodgate;
import org.bukkit.entity.Player;

import world.ofunny.bpm.Utils.Logger;

class Floodgate_Dummy implements Floodgate {

	/**
	 * Will always return false (everybody will be a Java player).
	 * Used when Floodgate is missing or could not be found.
	 */
	public boolean isBedrockPlayer(Player player) {
		Logger.get().logWarning("Floodgate plugin not found – all players will be treated as Java players! Did you install the Floodgate plugin correctly? Only the modules 'join_commands.all' and 'quit_commands.all' can work without Floodgate – in that case please deactivate all other modules in the config.yml first and than restart the server!");
		return false;
	}// end isBedrockPlayer 
	
}// end class Floodgate_1_0