/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import world.ofunny.bpm.Utils.Logger;
import world.ofunny.bpm.config.Config;

public class Commands implements CommandExecutor {
	
	/*
	 * Members
	 */
	private Logger logger;
	private Config config;
	
	/**
	 * Constructor
	 */
	public Commands() {
		
		/*
		 * Local dependencies
		 */
		logger = Logger.get();
		config = Config.get();
		
	}// end constructor

	/**
	 * On command call.
	 * Actually we just manage the reload command here, if I ever gonna add more than that, 
	 * I gonna rebuild that a little bit. That's actually the reason why there is a switch/case within this method.
	 */
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		// Get the senders name
		String senderName = sender.getName();
		
		// Just a debug log …
		logger.debugLogInfo("'"+senderName+"' performed the 'bedrockplayermanager command!");

        // Check for the correct amount of arguments.
        if (args.length != 1) {
            logger.debugLogWarning("'"+senderName+"' performed the 'bedrockplayermanager' command with the wrong number of arguments!");
            sender.sendMessage("Wrong number of argument!");
            return false;
        }// end if genau 1 Argument

		/*
		 * Execute the called action.
		 */
        switch(args[0]){
            case "reload":
    			if(isAllowed(sender, senderName, "bedrockplayermanager.reload", true)) {	
    				config.reloadConfiguration();
    				sender.sendMessage("The plugin configuartion has been updated with the latest values form the config.yml (please note: changes to the plugins developer settings still require a server restart)!");
    			}// end if player does not have enough permissions
        		break;
            default:
            	logger.debugLogInfo("'"+senderName+"' performed the 'bedrockplayermanager command with an unregistered argument: "+args[0]+"'!");
            	sender.sendMessage("The argument has to be 'reload'!");
                break;
        }//end switch "on" or "off"

        // If the player (or console) uses our command correct, we can return true
        
        return true;
    }// end onCommand

	private boolean isAllowed(CommandSender sender, String senderName, String permissionNote, boolean consoleAllowed) {
		// Checks for players
		if(sender instanceof Player) {
			if(sender.hasPermission(permissionNote)) {
				return true;
			} else {
				logger.debugLogInfo("'"+senderName+"' performed the 'bedrockplayermanager' command with insufficient permissions (bedrockplayermanager.reload)!");
				sender.sendMessage("Insufficient permissions (bedrockplayermanager.reload)!");
				return false;
			}// end if missing permissions
		}// end if player
		
		// Checks for the console
		if(sender instanceof ConsoleCommandSender) {
			if(consoleAllowed) {
				return true;
			} else {
				logger.debugLogInfo("'"+senderName+"' performed the 'bedrockplayermanager' command with insufficient permissions (console as command sender is not allowed)!");
				sender.sendMessage("Insufficient permissions (console as command sender is not allowed)!");
				return false;
			}// end if console allowed
		}// end if console sender
		
		// For all other kinds of senders!
		logger.debugLogInfo("'"+senderName+"' is neither a player nor the console!");
		return false;
	}// is the execution allowed
	
}// end class CommandKit