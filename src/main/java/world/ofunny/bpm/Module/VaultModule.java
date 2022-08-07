/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.Module;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.permission.Permission;
import world.ofunny.bpm.Floodgate.FloodgateAPI;
import world.ofunny.bpm.Utils.Logger;
import world.ofunny.bpm.config.Config;

public enum VaultModule {

	/*
	 * Neue Singleton-Implementierung mittels ENUM-Type.
	 * Ist zugleich automatisch Thread-sicher und serialisierbar.
	 */
	INSTANCE;
	public static VaultModule get() {
		return INSTANCE;
	} // end getInstance

	/*
	 * Initialisation
	 */
	private	Permission vaultPermissionService = null;
	private final Logger logger;
	private final Config config;
	
	/*
	 * Constructor.
	 */
	VaultModule() {
		
		/*
		 * Local dependencies
		 */
		logger = Logger.get();
		config = Config.get();
		
		/*
		 * Try to Initialise Vault support.
		 */
		if (!setupPermissions()) {
			logger.logError(config.getVaultPluginName() + " plugin not found! Vault must be installed on your server for the permissions module to work properly – otherwise deactivate Vault in the permission module in your config.yml!");
		}// if Vault support
		
		/*
		 * Check if the bedrock group is present in the registrated permission method.
		 */
		if (vaultPermissionService != null) {
			String[] availablePermissionGroups = vaultPermissionService.getGroups();
			if(Arrays.stream(availablePermissionGroups).noneMatch(group -> group.equals(config.getBedrockPermissionGroup()))) {
				logger.logWarning(
								"Could not find the group '" + config.getBedrockPermissionGroup() + "' in your permission plugin: '" + vaultPermissionService.getName() + "'! "+
								"We won't check for groups again until the next restart. "+
								"Make sure that the group '" + config.getBedrockPermissionGroup() + "' exists otherwise the assignment (on player join) will have no effect! "+
								"You can ignore this message if you know that the '" + config.getBedrockPermissionGroup() + "' group exists and is assignable! We only found the following groups:"
								);
				Arrays.stream(availablePermissionGroups).forEach(group -> { 
					logger.logWarning("'" + group + "'");
				});
			} else {
				logger.debugLogInfo("Vault service: found permission group '" + config.getBedrockPermissionGroup()+"' in the registered permission plugin '" + vaultPermissionService.getName() + "'. Ready to proceed!");
			}// end if group not existing
		}// end if vaultPermissionService
		
	}// end VaultModule
	
	/*
	 * Initialise the Vault api if available.
	 */
	private boolean setupPermissions() {
		if (Bukkit.getServer().getPluginManager().getPlugin(config.getVaultPluginName()) == null) return false;
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
    	if (rsp == null) return false;
        vaultPermissionService = rsp.getProvider();
        return vaultPermissionService != null;
    }// end setupPermissions
	
	/*
	 * Get the Vault permission service.
	 */
	public Permission getPermissions() {
        return vaultPermissionService;
    }// end getPermissions
	
	/*
	 * Applies or removes the permission group defined in the plugins config file.
	 */
	public void performPermissionGroupChange(Player player) {
		
		/*
		 * If the Vault service is given ...
		 */
		if (vaultPermissionService == null) {
			logger.logWarning("Vault service not found: aborting now!");
			return;
		}// end if vaultPermissionService
		
		/*
		 * Check if group support is given.
		 */
		if (!vaultPermissionService.isEnabled()) {
			logger.logWarning("The permission method in Vault is not enabled: aborting now!");
			return;
		}// end isEnabled

		/*
		 * Check if group support is given.
		 */
		if (!vaultPermissionService.hasGroupSupport()) {
			logger.logWarning("The permission method '" + vaultPermissionService.getName() + "' registrated in Vault does not support permission groups: aborting now!");
			return;
		}// end hasGroupSupport
		
    	/*
    	 * Get the offline player object for a player, since Vaults service needs an offline player.
    	 */
		OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(player.getUniqueId());
		
    	/*
    	 * If global groups or per world groups are active.
    	 */
    	if (config.isVaultPerWorldPermissions()) {
    		performPerWorldGroupChange(player, offlinePlayer);
    	} else {
    		performGlobalGroupChange(player, offlinePlayer);
    	}// end if vaultPerWorldPermissions
		
	}// end performPermissionGroupChange
	
	/*
	 * If the registrated permission plugin supports global groups.
	 */
	public void performGlobalGroupChange(final Player player, final OfflinePlayer offlinePlayer) {
		
		/*
		 * Nothing more to do, we can directly perfom the change
		 */
		changeGroup(player, offlinePlayer, null);
		
	}// end performGlobalGroupChange
	
	/*
	 * If the registrated permission plugin supports per world groups only.
	 */
	public void performPerWorldGroupChange(final Player player, final OfflinePlayer offlinePlayer) {
		
		
		 //Utils.get().debugLog(Level.INFO, "++++"+vaultPermissionWorlds.toString());
		
		/*
		 * If no groups where defined, we perform it for all worlds on the server.
		 */
		if (config.getVaultPermissionWorlds().size() > 0) {
			
			/*
			 * Perform the group change only for defined worlds
			 */
			config.getVaultPermissionWorlds().forEach((String worldName) -> {
				World world = Bukkit.getServer().getWorld(worldName);
				if (world != null) {
					changeGroup(player, offlinePlayer, world.getName());
				} else {
					logger.logWarning("Vault service (" + vaultPermissionService.getName() + "): there is no active world called '"+worldName+"' on your server. Skipping group change for '"+worldName+"'!");
				}// end if world exists
			});
			
			
		} else {
			
			/*
			 * Perform the group change for each world on the server.
			 */
			Bukkit.getServer().getWorlds().forEach((World world) -> {
				changeGroup(player, offlinePlayer, world.getName());
			});
			
		}// end if vaultPermissionWorlds
	}// end performPerWorldGroupChange
	
	
	public void changeGroup(Player player, OfflinePlayer offlinePlayer, String worldName) {
		
		/*
		 * Inits
		 */
		String debugWorldName = worldName;
		if (debugWorldName == null) debugWorldName = "global";
		
		/*
		 * Assign or remove permission group depending on the client.
		 */
		if (FloodgateAPI.get().isBedrockPlayer(player)) {

        	/*
        	 * Add group to the user.
        	 */
			logger.debugLogInfo("["+debugWorldName+"] Vault service (" + vaultPermissionService.getName() + "): adding permission group '"+config.getBedrockPermissionGroup()+"' for player " + offlinePlayer.getName());
			try {
				if(!vaultPermissionService.playerInGroup(worldName, offlinePlayer, config.getBedrockPermissionGroup()))
					vaultPermissionService.playerAddGroup(worldName, offlinePlayer, config.getBedrockPermissionGroup());
			} catch (Exception e) {
				logger.logWarning("["+debugWorldName+"] Vault service (" + vaultPermissionService.getName() + "): could not add the permission group '" + config.getBedrockPermissionGroup() + "' for player " + offlinePlayer.getName() + ". Does the group exist? Error: " + e.getMessage());
			}// end try/catch
            
		} else {
			
			/*
			 *  Remove group from the user.
			 */
			logger.debugLogInfo("["+debugWorldName+"] Vault service (" + vaultPermissionService.getName() + "): removing permission group '" + config.getBedrockPermissionGroup()+"' for player " + offlinePlayer.getName());
			try {
				if (vaultPermissionService.playerInGroup(worldName, offlinePlayer, config.getBedrockPermissionGroup()))
					vaultPermissionService.playerRemoveGroup(worldName, offlinePlayer, config.getBedrockPermissionGroup());
			} catch (Exception e) {
				logger.logWarning("["+debugWorldName+"] Vault service (" + vaultPermissionService.getName() + "): could not remove the permission group '" + config.getBedrockPermissionGroup() + "' for player " + offlinePlayer.getName()+". Does the group exist? Error: " + e.getMessage());
			}// end try/catch
			
		}// end if Bedrock noob	
	}// end performGlobalGroupChange

} // end class(enum) VaultModule