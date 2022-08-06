/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.Module;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.messaging.MessagingService;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import world.ofunny.bpm.Floodgate.FloodgateAPI;
import world.ofunny.bpm.Utils.Logger;
import world.ofunny.bpm.config.Config;

public enum LuckPermsModule {

	/*
	 * Neue Singleton-Implementierung mittels ENUM-Type.
	 * Ist zugleich automatisch Thread-sicher und serialisierbar.
	 */
	INSTANCE;
	public static LuckPermsModule get() {
		return INSTANCE;
	} // end getInstance

	/*
	 * Initialisation
	 */
	private boolean moduleInitialisationError = false;
	private LuckPerms luckPermsApi = null;
	private Logger logger;
	private Config config;
	
	/*
	 * Contructor
	 */
	private LuckPermsModule() {
		
		/*
		 * Local dependencies
		 */
		logger = Logger.get();
		config = Config.get();
		
		/*
		 * Luckperms plugin check.
		 */
		if (Bukkit.getServer().getPluginManager().getPlugin(config.getLuckPermsPluginName()) == null) {
			moduleInitialisationError = true;
			logger.logError(config.getLuckPermsPluginName() + " plugin not found! LukePerms must be installed on your server for the permissions module to work properly – otherwise deactivate Luckperms in the permission module in your config.yml!");
		} else {
			moduleInitialisationError = false;
		}// if Luckperms installed
		
		/*
		 * Get LuckPerms Api-provider reference.
		 */
		if (!moduleInitialisationError) luckPermsApi = LuckPermsProvider.get();
		
	}// end LuckPermsModule
	
	/*
	 * Applies or removes the permission group defined in the plugins config file.
	 */
	public void performPermissionGroupChange(Player player) {
		
		/*
		 * Check if active or deactivated because of an error at initialisation.
		 */
		if (moduleInitialisationError) {
			logger.logWarning("LuckPerms-Module initialisation failed, can not proceed!");
			return;
		}// end if luckPermsApi
		
		/*
		 * If LuckPermsApi is given ...
		 */
		if (luckPermsApi == null) {
			logger.logWarning("LuckPerms-Api not found: aborting now!");
			return;
		}// end if luckPermsApi

		/*
		 * Obtain group object for the Bedrock group.
		 */
        Group group = luckPermsApi.getGroupManager().getGroup(config.getBedrockPermissionGroup());

        /*
         * If this group does not exist, cancel with error,
         * then this must first be created in LuckPerms.
         */
        if (group == null) {
        	logger.logWarning("Group " + config.getBedrockPermissionGroup() + " does not exist in LukePerms, create the permission group first: aborting now!");
            return;
        }// end if group
		
        /*
         * Perform permission change.
         */
        performAsyncGroupChange(player, player.getUniqueId(), group, config.getBedrockPermissionGroup());
		 
	}// end PostLoginEvent
	
	/*
	 * Perform permission change in any case async, no matter what Luckperms might change in its API.
	 * Also, Luckperms claims to be completely async save, a recommends to run Luckperms Api calls async only.
	 */
	private void performAsyncGroupChange(final Player player, final UUID playerUUID, final Group group, final String bedrockPermissionGroup) {

		/*
		 * Run task async
		 */
		//end run
		Bukkit.getScheduler().runTaskAsynchronously(config.getPlugin(), () -> {

			/*
			 * Inits
			 */
			String playerName = player.getName();

			/*
			 *  Load, modify & save the user in LuckPerms.
			 */
			CompletableFuture<Void> userModificationFuture = luckPermsApi.getUserManager().modifyUser(playerUUID, (User user) -> {

				// Create a node to add to the player.
				Node node = InheritanceNode.builder(group).build();

				if(FloodgateAPI.get().isBedrockPlayer(player)) {

					/*
					 * Add the node to the user.
					 */
					logger.debugLogInfo("LuckPerms: adding permission group '"+bedrockPermissionGroup+"' for player " + playerName);
					user.data().add(node);

				} else {

					/*
					 *  Remove the node from the user.
					 */
					logger.debugLogInfo("LuckPerms: removing permission group '"+bedrockPermissionGroup+"' for player " + playerName);
					user.data().remove(node);

				}// end if Bedrock noob

			});

			/*
			 *  Since we're already on an async thread, it doesn't matter how long we have to wait for the elusive Log to show up.
			 *  The #join method will block - and wait until the Log has been supplied, and then return it.
			 *  If for whatever reason the process to obtain a ActionLog threw an exception,
			 *  this method will rethrow an the same exception wrapped in a CompletionException
			 */
			try {
				/*
				 * Let's try to join it.
				 */
				userModificationFuture.join();

			} catch (CompletionException ex) {
				logger.logError("Command execution caused an error: " + ex.getMessage());
			}// end try dispatchCommand

			/*
			 * Notify other servers about the permission change.
			 */
			logger.debugLogInfo("Starting messaging for " + playerUUID.toString());
			User LuckPermsUser = luckPermsApi.getUserManager().getUser(playerUUID);
			if (LuckPermsUser != null) {

				/*
				 * Since Luckperms messagin service is optional,
				 * we have to check if it is present first otherwise we risk an exception.
				 */
				Optional<MessagingService> luckPermMessagingService = luckPermsApi.getMessagingService();

				/*
				 * If present, perfom messaging.
				 */
				if (luckPermMessagingService.isPresent()) {

					luckPermMessagingService.get().pushUserUpdate(LuckPermsUser);
					logger.debugLogInfo("LuckPerms: informed other servers about the permission change.");

				} else {

					logger.debugLogInfo("LuckPerms: messaging service was deactivated in the Luckperms configuration, aborting messaging!");

				}// end if present

			} else {
				logger.logWarning("Could not push message since LuckPermsUser is null");
			}// end if LuckPermsUser

		});
	}// end performAsyncGroupChange
	
} // end class(enum) LuckPermsModule
