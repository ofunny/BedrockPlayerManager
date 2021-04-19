/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.commands;

import org.bukkit.command.PluginCommand;
import world.ofunny.bpm.BedrockPlayerManager;
import world.ofunny.bpm.config.Config;

public enum CommandManager {

    INSTANCE;
    public static CommandManager get() {
		return INSTANCE;
	} // end getInstance*/

	/*
	 * Members
	 */
	//private Logger logger;
	private Config config;
    private boolean initialised = false;
    
    private CommandManager() {
		
		/*
		 * Local dependencies
		 */
		//logger = Logger.get();
		config = Config.get();
		
    }// end CmdInitialisation


    /**
     * Initialise all of the plugins commands once after startup.
     */
    public void initialise() {
    	
        /*
         * Initialising the command list is only allowed once!
         */
        if(initialised) return;

        /*
         * Lets register all our commands now.
         */
        BedrockPlayerManager bedrockPlayerManager = config.getPlugin();
        PluginCommand bedrockplayermanager = bedrockPlayerManager.getCommand("bedrockplayermanager");
        
        /*
         * List of all commands (executor and tab completion).
         */
        bedrockplayermanager.setExecutor(new Commands());
        bedrockplayermanager.setTabCompleter(new TabComplete());

        /*
         * Mark command list as initialised.
         */
        initialised = true;

    }// end initialise

}// end CmdInitialisation