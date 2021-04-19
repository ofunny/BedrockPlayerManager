/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.Module;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import world.ofunny.bpm.Utils.Logger;
import world.ofunny.bpm.config.Config;

public enum CommandModule {
	
	/*
	 * Neue Singleton-Implementierung mittels ENUM-Type.
	 * Ist zugleich automatisch Thread-sicher und serialisierbar.
	 */
	INSTANCE;
	public static CommandModule get() {
		return INSTANCE;
	} // end getInstance

	/*
	 * Members
	 */
	private Logger logger;
	private Config config;
	
	/*
	 * Contructor.
	 */
	private CommandModule() {
		
		/*
		 * Local dependencies
		 */
		logger = Logger.get();
		config = Config.get();
		
	}// end PlayerManagement

	/**
	 * Performs the join commands (as server/player) for Java clients.
	 * @param player
	 */
	public void performJoinCommandsForJava(Player player) {
		
		logger.debugLogInfo("Performing server join commands for Java.");
		perfomCommands(player, config.getJavaJoinServerCommands(), true);
		
		logger.debugLogInfo("Performing player join commands for Java.");
		perfomCommands(player, config.getJavaJoinPlayerCommands(), false);
		
	}// end performJoinCommandsForJava
	
	/**
	 * Performs the join commands (as server/player) for Bedrock clients.
	 * @param player
	 */
	public void performJoinCommandsForBedrock(Player player) {
		
		logger.debugLogInfo("Performing server join commands for Bedrock.");
		perfomCommands(player, config.getBedrockJoinServerCommands(), true);
		
		logger.debugLogInfo("Performing player join commands for Bedrock.");
		perfomCommands(player, config.getBedrockJoinPlayerCommands(), false);
		
	}// end performJoinCommandsForBedrock
	
	/**
	 * Performs the join commands (as server/player) for all clients.
	 * @param player
	 */
	public void performJoinCommandsForAll(Player player) {
		
		logger.debugLogInfo("Performing server join commands for all.");
		perfomCommands(player, config.getAllJoinServerCommands(), true);
		
		logger.debugLogInfo("Performing player join commands for all.");
		perfomCommands(player, config.getAllJoinPlayerCommands(), false);
		
	}// end performJoinCommandsForAll

	/**
	 * Performs the leave (quit) commands (as server/player) for Java clients.
	 * @param player
	 */
	public void performQuitCommandsForJava(Player player) {
		
		logger.debugLogInfo("Performing server quit commands for Java.");
		perfomCommands(player, config.getJavaQuitServerCommands(), true);
		
		logger.debugLogInfo("Performing player quit commands for Java.");
		perfomCommands(player, config.getJavaQuitPlayerCommands(), false);
		
	}// end performQuitCommandsForJava
	
	/**
	 * Performs the leave (quit) commands (as server/player) for Bedrock clients.
	 * @param player
	 */
	public void performQuitCommandsForBedrock(Player player) {
		
		logger.debugLogInfo("Performing server quit commands for Bedrock.");
		perfomCommands(player, config.getBedrockQuitServerCommands(), true);
		
		logger.debugLogInfo("Performing player quit commands for Bedrock.");
		perfomCommands(player, config.getBedrockQuitPlayerCommands(), false);
		
	}// end performQuitCommandsForBedrock
	
	/**
	 * Performs the leave (quit) commands (as server/player) for all clients.
	 * @param player
	 */
	public void performQuitCommandsForAll(Player player) {
		
		logger.debugLogInfo("Performing server quit commands for all.");
		perfomCommands(player, config.getAllQuitServerCommands(), true);
		
		logger.debugLogInfo("Performing player quit commands for all.");
		perfomCommands(player, config.getAllQuitPlayerCommands(), false);
		
	}// end performQuitCommandsForAll
	
	/**
	 * Performs the given command and also handles the placeholder replacement.
	 * 
	 * @param player
	 * @param commandList
	 * @param serverCommand
	 */
	private void perfomCommands(Player player, final List<String> commandList, final boolean serverCommand) {
		
		/*
		 * Initialize
		 */
		final String playerName = player.getName();
		final String playerUUID = player.getUniqueId().toString();
		
		/*
		 * Server or payers command?
		 */
		final CommandSender commandSender;
		if(serverCommand) {
			// Server command.
			commandSender = Bukkit.getServer().getConsoleSender();
		} else {
			// Player command.
			commandSender = player;
		}// end if serverCommand
		
		
		/*
		 * Execute commands.
		 * We don't need to do further checks on the commandList since config.getStringList() will
		 * return an empty list in any case of malformed or missing yaml entry.
		 */
		if(commandSender != null) {
			
			/*
			 * For each command
			 */
			commandList.forEach((String command) -> {
				
				
				/*
				 * Only process not empty command strings.
				 */
				if(command.length() > 0) {
					/*
					 * Replace {player} placeholder.
					 * 
					 * If there will be more placeholder in the future, we may switch to the StrSubstitutor (import org.apache.commons.lang.text.StrSubstitutor;)
					 */
					command = command.replace("{player}", 	playerName);
					command = command.replace("{uuid}", 	playerUUID);
					
					/*
					 * Placeholder-API support test
					 */
					if(config.isPlaceholderAPIEnabled()) {
						command = PlaceholderAPI.setPlaceholders(player, command);
					}// Placeholder API
					
					/*
					 * Finally execute the command :) 
					 */
					logger.debugLogInfo("Executing: "+command);
					try {
						Bukkit.dispatchCommand(commandSender, command);
					} catch (CommandException ex) {
						logger.logError("Command execution caused an error: " + ex.getMessage());
					}// end try dispatchCommand

				}// end if command 
				
			});
			
		}// end if commandSender
		
	}// end perfomCommands
	
} // end class(enum) CommandModule
