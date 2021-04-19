/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import world.ofunny.bpm.Events.EventListener;
import world.ofunny.bpm.Utils.Logger;
import world.ofunny.bpm.commands.CommandManager;
import world.ofunny.bpm.config.Config;

public class BedrockPlayerManager extends JavaPlugin {
	
	/*
	 * On activation of our plugin.
	 */
    @Override
    public void onEnable() {

        /*
         * Holds a reference of the PluginManager.
         */
        PluginManager pluginManager = this.getServer().getPluginManager();

		/*
		 * Local dependencies
		 */
		Logger logger = Logger.get();
        
        /*
         * Load the plugins configuration from the plugin.yml.
         */
		Config.initialise(this);
        Config config = Config.get();

        /*
         * Check if the configuration did load successfully.
         */
        if(!config.hasConfiguration()) {
        	logger.logError("Configuration object is null – aborting now!");
            pluginManager.disablePlugin(this);
            return;
        }// end if configuration

		/*
		 * Check if the floodgade plugin exists,
		 * only exception is the modul commands for all players what can work without it!
		 */
		if(
			(config.isJavaJoinCommandModuleEnabled() || config.isBedrockJoinCommandModuleEnabled() || config.isPermissionModuleEnabled()) &&
			getServer().getPluginManager().getPlugin(config.getFloodgatePluginName()) == null) {
			logger.logError(config.getFloodgatePluginName()+" plugin not found! Did you install Floodgate and did you set the correct version (development.floodgate_version) in the config.yml? Only the modules 'join_commands.all' and 'quit_commands.all' can work without Floodgate – in that case please deactivate all other modules in the config.yml first and than restart the server!");
			return;
		}// if Luckperms installes
		
	    /*
	     * Register EventListener.
	     */
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		
        /*
         * Lets register all our commands now.
         */
        CommandManager.get().initialise();

    }// end onEnable

}//end class BedrockPermissions
