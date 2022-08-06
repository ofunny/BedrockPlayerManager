/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.config;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import world.ofunny.bpm.BedrockPlayerManager;
import world.ofunny.bpm.Utils.Logger;

public class Data {
	
	/**
	 * Dependencies 
	 */
	private final Logger logger;
	
	/**
	 * Constructor
	 */
	public Data() {	
		
		// Dependencies 
		logger = Logger.get(); 
	
		// Inititialisation
		setProperties();
	
	}// end Data
	
	/**
	 * General attributes
	 */
	// PLUGIN REFERNCE
	private BedrockPlayerManager plugin = null;
	
	/**
	 * Project properties
	 */
	private String pluginVersion;
	
	/**
	 * Config attributes.
	 */
	// JOIN COMMAND MODULE
	private boolean			javaJoinCommandModuleEnabled;
	private boolean			bedrockJoinCommandModuleEnabled;
	private boolean			allJoinCommandModuleEnabled;
	private List<String>	javaJoinServerCommands;
	private List<String>	javaJoinPlayerCommands;
	private List<String>	bedrockJoinServerCommands;
	private List<String>	bedrockJoinPlayerCommands;
	private List<String>	allJoinServerCommands;
	private List<String>	allJoinPlayerCommands;
	private long 			joinCommandExecutionDelay;
	
	// QUIT COMMAND MODULE
	private boolean			javaQuitCommandModuleEnabled;
	private boolean			bedrockQuitCommandModuleEnabled;
	private boolean			allQuitCommandModuleEnabled;
	private List<String>	javaQuitServerCommands;
	private List<String>	javaQuitPlayerCommands;
	private List<String>	bedrockQuitServerCommands;
	private List<String>	bedrockQuitPlayerCommands;
	private List<String>	allQuitServerCommands;
	private List<String>	allQuitPlayerCommands;
	
	// PERMISSION MODULE
	private boolean			permissionModuleEnabled;
	private String  		permissionModuleType;
	private String			bedrockPermissionGroup;
	private boolean			vaultPerWorldPermissions;
	private List<String>	vaultPermissionWorlds;
	private long 			joinPermissionExecutionDelay;
	
	// GENERAL SETTINGS
	private boolean			debug;
	//private long 			executionDelay;
	private boolean			placeholderAPIEnabled;
	
	// DEVELOPER SETTINGS
	private String			floodgate_PluginName;
	private String			luckPermsPluginName;
	private String			vaultPluginName;
	private String			placeholderAPIPluginName;
	
	// OTHER STUFF
	private String			dataFolderPath;
	
	/**
	 * Sets the current plugins reference.
	 */
	protected void setPlugin(BedrockPlayerManager bedrockPlayerManager) {
		plugin = bedrockPlayerManager;
	}// end setPlugin
	
	
	/**
	 * Requesting the project.properties file from the plugins resource folder what
	 * contains selected properties directly from the maven pom.xml 
	 * (so we do not save version and other stuff multiple times).
	 */
	private void setProperties() {
		
		// Getting a property object.
		Properties prop = new Properties();
		
		// Trying to load our property file from resources.
		try { 
			prop.load(this.getClass().getResourceAsStream("/project.properties"));
		} catch (IOException e) { 
			logger.logError("Error: 'project.properties' resource file could not be loaded correctly!"); 
		}// end try loading project properties
		
		// Setting the desired properties or a default value if not available.
		pluginVersion = prop.getProperty("version", "error");
		
		// Creating a debug log entry with the found version information.
		logger.debugLogInfo("Setting plugin version from 'project.properties' resource file – found version string: " + pluginVersion);
		
	}// end setProperties
	
	/**
	 * Will take all defined option from the config.yml and sets them within this singleton.
	 * Performs possible post processing afterwards.
	 * 
	 * We copy all config option by value in our instance for good reasons (some preperation in case we do async stuff in the future)
	 * (Natives [including strings] get passed by value and getStringList will already return a clone, so no need to do redundant work here).
	 */
	protected void setData(FileConfiguration configuration) {

		/*
		 * Set config params.
		 */
		// JOIN COMMAND MODULE
		javaJoinCommandModuleEnabled 		= configuration.getBoolean("join_commands.java.enabled", false);
		bedrockJoinCommandModuleEnabled 	= configuration.getBoolean("join_commands.bedrock.enabled", false);
		allJoinCommandModuleEnabled			= configuration.getBoolean("join_commands.all.enabled", false);
		javaJoinServerCommands				= configuration.getStringList("join_commands.java.server");
		javaJoinPlayerCommands				= configuration.getStringList("join_commands.java.player");
		bedrockJoinServerCommands			= configuration.getStringList("join_commands.bedrock.server");
		bedrockJoinPlayerCommands			= configuration.getStringList("join_commands.bedrock.player");
		allJoinServerCommands				= configuration.getStringList("join_commands.all.server");
		allJoinPlayerCommands				= configuration.getStringList("join_commands.all.player");
		joinCommandExecutionDelay			= configuration.getLong("join_commands.execution_delay", 1L);
		
		// QUIT COMMAND MODULE
		javaQuitCommandModuleEnabled 		= configuration.getBoolean("quit_commands.java.enabled", false);
		bedrockQuitCommandModuleEnabled 	= configuration.getBoolean("quit_commands.bedrock.enabled", false);
		allQuitCommandModuleEnabled			= configuration.getBoolean("quit_commands.all.enabled", false);
		javaQuitServerCommands				= configuration.getStringList("quit_commands.java.server");
		javaQuitPlayerCommands				= configuration.getStringList("quit_commands.java.player");
		bedrockQuitServerCommands			= configuration.getStringList("quit_commands.bedrock.server");
		bedrockQuitPlayerCommands			= configuration.getStringList("quit_commands.bedrock.player");
		allQuitServerCommands				= configuration.getStringList("quit_commands.all.server");
		allQuitPlayerCommands				= configuration.getStringList("quit_commands.all.player");
		
		// PERMISSION MODULE
		permissionModuleEnabled 			= configuration.getBoolean("permissions.enabled", false);
		permissionModuleType				= configuration.getString("permissions.plugin", "luckperms").toLowerCase();
		bedrockPermissionGroup 				= configuration.getString("permissions.bedrock_group_name", "bedrock_user");
		vaultPerWorldPermissions 			= configuration.getBoolean("permissions.vault.per_world_permissions", false);
		vaultPermissionWorlds				= configuration.getStringList("permissions.vault.worlds");
		joinPermissionExecutionDelay		= configuration.getLong("permissions.execution_delay", 0L);
		
		// GENERAL SETTINGS
		//executionDelay 					= configuration.getLong("settings.execution_delay", 1L);
		placeholderAPIEnabled				= configuration.getBoolean("settings.PlaceholderAPI", true);
		debug 								= configuration.getBoolean("settings.debug", false);
		
		// DEVELOPER SETTINGS
		floodgate_PluginName				= configuration.getString("development.Floodgate_v2_PluginName", "floodgate");
		luckPermsPluginName					= configuration.getString("development.LuckPermsPluginName", "LuckPerms");
		vaultPluginName						= configuration.getString("development.VaultPluginName", "Vault");
		placeholderAPIPluginName			= configuration.getString("development.PlaceholderAPIPluginName", "PlaceholderAPI");
		
		// OTHER STUFF
		dataFolderPath 						= plugin.getDataFolder().getPath()+"/data/";
		
		/*
		 * Performing additional post processing each time the config gets (re-)loaded. 
		 */
			// Set debugging within the Logger according to the plugin config.
			logger.setDebug(debug);
	
			// Activate placeholder support only if the PlaceholderApi plugin has been loaded before.
			if (placeholderAPIEnabled && Bukkit.getPluginManager().getPlugin(placeholderAPIPluginName) == null) {
				placeholderAPIEnabled = false;
				logger.logWarning("PlaceholderAPI plugin not found! Install the missing plugin or set PlaceholderAPI to false in your config.yml!");
			}// end if PlaceholderAPI not installed.
			
			// Set the correct Floodgate name
			
	}// end config
	
	/**
	 * Get the Plugin instance for schedulers and similar stuff.
	 * @return returns the plugins instance.
	 */
	public BedrockPlayerManager getPlugin() 			{ return plugin; }// end of plugin
	
	// PROJECT PROPERTIES
	public String getPluginVersion() 					{ return pluginVersion; }	
	
	// JOIN COMMAND MODULE
	public boolean isJavaJoinCommandModuleEnabled()		{ return javaJoinCommandModuleEnabled; }
	public boolean isBedrockJoinCommandModuleEnabled() 	{ return bedrockJoinCommandModuleEnabled; }
	public boolean isAllJoinCommandModuleEnabled() 		{ return allJoinCommandModuleEnabled; }
	public List<String> getJavaJoinServerCommands() 	{ return javaJoinServerCommands; }
	public List<String> getJavaJoinPlayerCommands() 	{ return javaJoinPlayerCommands; }
	public List<String> getBedrockJoinServerCommands() 	{ return bedrockJoinServerCommands; }
	public List<String> getBedrockJoinPlayerCommands() 	{ return bedrockJoinPlayerCommands; }
	public List<String> getAllJoinServerCommands() 		{ return allJoinServerCommands; }
	public List<String> getAllJoinPlayerCommands() 		{ return allJoinPlayerCommands; }
	public long getJoinCommandExecutionDelay()			{ return joinCommandExecutionDelay;	}
	
	// QUIT COMMAND MODULE
	public boolean isJavaQuitCommandModuleEnabled()		{ return javaQuitCommandModuleEnabled; }
	public boolean isBedrockQuitCommandModuleEnabled() 	{ return bedrockQuitCommandModuleEnabled; }
	public boolean isAllQuitCommandModuleEnabled() 		{ return allQuitCommandModuleEnabled; }
	public List<String> getJavaQuitServerCommands() 	{ return javaQuitServerCommands; }
	public List<String> getJavaQuitPlayerCommands() 	{ return javaQuitPlayerCommands; }
	public List<String> getBedrockQuitServerCommands() 	{ return bedrockQuitServerCommands; }
	public List<String> getBedrockQuitPlayerCommands() 	{ return bedrockQuitPlayerCommands; }
	public List<String> getAllQuitServerCommands() 		{ return allQuitServerCommands; }
	public List<String> getAllQuitPlayerCommands() 		{ return allQuitPlayerCommands; }
	
	// PERMISSION MODULE
	public boolean isPermissionModuleEnabled() 			{ return permissionModuleEnabled; }
	public String getPermissionModuleType() 			{ return permissionModuleType;	}
	public String getBedrockPermissionGroup() 			{ return bedrockPermissionGroup; }
	public boolean isVaultPerWorldPermissions() 		{ return vaultPerWorldPermissions; }
	public List<String> getVaultPermissionWorlds() 		{ return vaultPermissionWorlds;	}
	public long getJoinPermissionExecutionDelay()		{ return joinPermissionExecutionDelay; }

	// GENERAL SETTINGS
	//public long getExecutionDelay() 					{ return executionDelay; }
	public boolean isPlaceholderAPIEnabled() 			{ return placeholderAPIEnabled; }
	public boolean isDebug() 							{ return debug; }
	
	// DEVELOPER SETTINGS
	public String getFloodgate_PluginName()			{ return floodgate_PluginName; }
	public String getLuckPermsPluginName() 				{ return luckPermsPluginName; }
	public String getVaultPluginName() 					{ return vaultPluginName; }
	public String getPlaceholderAPIPluginName() 		{ return placeholderAPIPluginName; }
	
	// OTHER STUFF
	public String getDataFolderPath() 					{ return dataFolderPath; }

}// end Data
