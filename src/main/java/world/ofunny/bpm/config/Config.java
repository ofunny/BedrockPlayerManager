/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.config;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.bukkit.configuration.file.FileConfiguration;
import world.ofunny.bpm.BedrockPlayerManager;

/**
 * Thread safe Singelton what provides the plugins configuration.
 * (Actually self explaining).
 *
 * @author ofunny
 */
public class Config extends Data{

	/*
	 * Essential member initialisation.
	 * (Do not remove the following three unless you know what you are doing).
	 */
	private static Config INSTANCE = null;
	private FileConfiguration configuration = null;
	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss");

	/**
	 * Constructor of the config singleton.
	 * (Leave it private, we do not allow any instances aside of this Singleton)!
	 */
	private Config(BedrockPlayerManager bedrockPlayerManager) {
		
		/*
		 *  Save Plugin reference.
		 */
		setPlugin(bedrockPlayerManager);
		
		/*
		 * Loading the configuration of the plugin.
		 */
		loadConfiguration();
		
	}// end config
	
	/**
	 * Initialise the Singleton.
	 * Thread safe instantiation, even if some people don't understand the sense – trust me, synchronized has a sense here for safety reasons!
	 */
	public static void initialise(BedrockPlayerManager bedrockPlayerManager) {
		if (INSTANCE == null) {
			//synchronized block to remove overhead
			synchronized (Config.class) {
				INSTANCE = new Config(bedrockPlayerManager);
			}// end synchronized
		} // end if instance already exists
	}// end Config
	
	/**
	 * Instance provider for this singleton.
	 */
	public static Config get() { return INSTANCE; }// end get

	/**
	 * Set new configuration (internal use only).
	 * @param configuration config of this plugin delivered via Spigots/Papers config API.
	 */
	private void setConfiguration(FileConfiguration configuration) {
		this.configuration = configuration;
	}//end setConfiguration
	
	/**
	 * Check whether the configuration has been loaded successfully or not.
	 */
	public boolean hasConfiguration() {
		return configuration != null;
	}// end hasConfiguration
	
	/**
	 * Loads the config of this plugin via Spigots/Papers config API.
	 * It will only load the config once (usually at plugin activation), so a reload is not possible!
	 *
	 */
	private void loadConfiguration() {
		loadConfiguration(false);
	}// end loadConfiguration
	
	/**
	 * Reloads the config of this plugin via Spigots/Papers config API.
	 * It will reload all values from the plugins config.yml (if it does not exist, it will be generated).
	 * However, changes within the developer section of the config.yml still require a server restart!
	 * (Will only work after the plugi has been set at least once, what actually happens within the constructor anyways)
	 */
	public void reloadConfiguration() {
		loadConfiguration(true);
	}// end reloadConfiguration
	
	/**
	 * Loads the config of this plugin via Spigots/Papers config API.
	 * It will only load the config once (usually at plugin activation), so a reload is not possible!
	 *
	 * @param reload holds a reference of this plugin for the "getPlugin()" method of this class.
	 */
	private void loadConfiguration(boolean reload) {

		// Check if the plugin config folder and config yamnl exists, otherwise create the default plugins config directory.
		if (!getPlugin().getDataFolder().exists()) getPlugin().getDataFolder().mkdir();
		/*
		 * YAML
		 */
		String yamlFileName = "config.yml";
		File file = new File(getPlugin().getDataFolder(), yamlFileName);
		
		// Check if config file is missing or if it is from another plugin version.
		if (!file.exists()) {
			// Just copying the default config from the plugins resources.
			getPlugin().saveDefaultConfig();
		} else {
			
			/*
			 * If a config file other than the current version has been found, we copy it and rename it by adding "old" to its name.
			 * (Sorry I don't have the time to write a migration method what keeps the values including all comments.
			 *  And I really don't like the Spigot-API version what stripes all of them! Maybe a thing for the next update.)
			 */
			if (!Objects.equals(getPlugin().getConfig().getString("version"), getPluginVersion())) {
				// Reanming the old config and copying the default config from the plugins resources.
				file.renameTo(new File(getPlugin().getDataFolder(), dateTimeFormatter.format(LocalDateTime.now()) + "-old." + yamlFileName));
				getPlugin().saveDefaultConfig();
				
				/* 
				 * In this case we have to reload the config to be on the save side!
				 * Since we use getConfig() on a wrong config version (what will cache it) and save the new default afterwards.
				 */
				reload = true;
				
			}// end if config is from another version
			
		}// if file does not exist

		// If we should reload the configuration.
		if (reload) getPlugin().reloadConfig();
		
		// Load config if existing and set the defined options within these classes members.
		setConfiguration(getPlugin().getConfig());
		
		// sets all config options within this class and performs post-processing.
		if (this.configuration != null) setData(this.configuration);// should we set all options

	}//end loadConfiguration

} // end class Config
