/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabComplete implements TabCompleter {

	List<String> tabCompleteList = Arrays.asList("reload");
	
	/**
	 * Just supplies a list of all possible command arguments for the tab completion.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return tabCompleteList; 
	}// end onTabComplete

}//end TabComplete
