/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

public class TabComplete implements TabCompleter {

	List<String> tabCompleteList = Collections.singletonList("reload");
	
	/**
	 * Just supplies a list of all possible command arguments for the tab completion.
	 */
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
		return tabCompleteList; 
	}// end onTabComplete

}//end TabComplete
