/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import world.ofunny.bpm.Floodgate.FloodgateAPI;
import world.ofunny.bpm.Module.CommandModule;
import world.ofunny.bpm.Module.LuckPermsModule;
import world.ofunny.bpm.Module.VaultModule;
import world.ofunny.bpm.Utils.Logger;
import world.ofunny.bpm.config.Config;

public enum PlayerManagement {

	INSTANCE;
	public static PlayerManagement get() {
		return INSTANCE;
	} // end getInstance
	
	/*
	 * Members
	 */
	private Logger logger;
	private Config config;
    private Map<UUID, Boolean> playerBedrockStateCache = new HashMap<UUID, Boolean>();
	private BukkitScheduler bukkitScheduler;
	
	/*
	 * Contructor
	 */
	private PlayerManagement() {
		
		/*
		 * Local dependencies
		 */
		logger = Logger.get();
		config = Config.get();
		bukkitScheduler = Bukkit.getServer().getScheduler();
		
	}// end PlayerManagement

	/**
	 * Called when a player joins the server.
	 */
	public void onPlayerJoin(PlayerJoinEvent event) {

    	/*
		 * Obtain player object and cancel if null.
		 */
		Player player = event.getPlayer();
		if(player == null) {
			logger.logWarning("Player was null, could not proceed.");
			return;
		}// if player

		/*
		 * Save that players Bedrock state to the cache.
		 * (I need this, since Floodgate will always remove the player faster than my plugin – otherwise all 
		 *  players would be treated as Java players on quit since Floodgate no longer holds the state).
		 */
		playerBedrockStateCache.put(player.getUniqueId(), FloodgateAPI.get().isBedrockPlayer(player));
		
		/*
		 * If the permission module has been activated.
		 */
		if(config.isPermissionModuleEnabled()) {
			/*
			 * Perform permission group assignment/removal on join if activated.
			 */
	    	if(config.getJoinPermissionExecutionDelay() < 1L) {
	    		joinCallPermissionModule(player);
	    	} else {
	    		bukkitScheduler.scheduleSyncDelayedTask(config.getPlugin(), new Runnable() { 
	    			public void run() { joinCallPermissionModule(player); }// end run with delay
	    		}, config.getJoinPermissionExecutionDelay());
	    	}// end if executionDelay
		}// end if permissionModule

		/*
		 * if at least one command module is enabled
		 */
		if(config.isJavaJoinCommandModuleEnabled() ||
		   config.isBedrockJoinCommandModuleEnabled() ||
		   config.isAllJoinCommandModuleEnabled()) {
			/*
			 * Perform command execution on join on join if activated.
			 */
	    	if(config.getJoinCommandExecutionDelay() < 1L) {
	    		joinCallCommandModule(player);
	    	} else {
	    		bukkitScheduler.scheduleSyncDelayedTask(config.getPlugin(), new Runnable() {
	    			public void run() { joinCallCommandModule(player); }// end run with delay
	    		}, config.getJoinCommandExecutionDelay());
	    	}// end if executionDelay
		}// if at least one command module is enabled
		
	}// end onPlayerJoin
	
	/**
	 * Perform permission group assignment/removal on join.
	 */
	private void joinCallPermissionModule(Player player) {

			logger.debugLogInfo("Processing join permission group assignment/removal with " + config.getJoinPermissionExecutionDelay() + " ticks delay!");
			switch (config.getPermissionModuleType()) {
				case "vault":
					VaultModule.get().performPermissionGroupChange(player);
				break;
				case "luckperms":
					LuckPermsModule.get().performPermissionGroupChange(player);
				break;
				default:
					logger.logWarning("Only luckperms or vault are valid config options for 'permissions.plugin' – check your config.yml and correct the permission plugin string: aborting now!");
				break;
			}// end switch permissionModule
		
	}// end of joinCallPermissionModule
	
	/**
	 * Perform command execution on join.
	 */
	private void joinCallCommandModule(Player player) {
		
		/*
		 * Setting a debug log entry if debug has been activated.
		 */
		logger.debugLogInfo("Processing join commands with " + config.getJoinCommandExecutionDelay() + " ticks delay!");
		
		/*
		 * If the java join command module has been activated.
		 */
		if(config.isJavaJoinCommandModuleEnabled()) {
			if(!FloodgateAPI.get().isBedrockPlayer(player)) CommandModule.get().performJoinCommandsForJava(player);
		}// end if isJavaJoinCommandModuleEnabled
		
		/*
		 * If the java join command module has been activated.
		 */
		if(config.isBedrockJoinCommandModuleEnabled()) {
			if(FloodgateAPI.get().isBedrockPlayer(player)) CommandModule.get().performJoinCommandsForBedrock(player);
		}// end if isJoinBedrockCommandModuleEnabled
		
		/*
		 * If the "all" join command module has been activated.
		 */
		if(config.isAllJoinCommandModuleEnabled()) {
			CommandModule.get().performJoinCommandsForAll(player);
		}// end if isJoinAllCommandModuleEnabled
		
	}// end of joinCallPermissionModule
	
	/**
	 * Called when a player leaves the server.
	 */
	public void onPlayerQuit(PlayerQuitEvent event) {

    	/*
		 * Obtain player object and cancel if null.
		 */
		Player player = event.getPlayer();
		if(player == null) {
			logger.logWarning("Player was null, could not proceed.");
			return;
		}// if player
		
		/*
		 * Setting a debug log entry if debug has been activated.
		 */
		logger.debugLogInfo("Processing quit event!");

		/*
		 * Get the players Bedrock state from cache and than remove it.
		 * (I need this, since Floodgate will always remove the player faster than my plugin – otherwise all 
		 *  players would be treated as Java players on quit since Floodgate no longer holds the state).
		 */
		Boolean isBedrockPlayer = playerBedrockStateCache.remove(player.getUniqueId());
		
		/*
		 * If the java command module has been activated.
		 */
		if(config.isJavaQuitCommandModuleEnabled()) {
			if(!isBedrockPlayer) CommandModule.get().performQuitCommandsForJava(player);
		}// end if isJavaQuitCommandModuleEnabled
		
		/*
		 * If the java command module has been activated.
		 */
		if(config.isBedrockQuitCommandModuleEnabled()) {
			if(isBedrockPlayer) CommandModule.get().performQuitCommandsForBedrock(player);
		}// end if isQuitBedrockCommandModuleEnabled
		
		/*
		 * If the "all" command module has been activated.
		 */
		if(config.isAllQuitCommandModuleEnabled()) {
			CommandModule.get().performQuitCommandsForAll(player);
		}// end if isQuitAllCommandModuleEnabled
		
	}// end onPlayerQuit

} // end class(enum) PlayerManagement