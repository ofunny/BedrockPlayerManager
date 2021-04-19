/* The copyright according to COPYRIGHT.txt applies to this file */

package world.ofunny.bpm.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import world.ofunny.bpm.Player.PlayerManagement;

public class EventListener implements Listener {
	
	/**
	 * Constructor
	 */
	public EventListener() {}// end constructor 
	
	/**
	 * Called when a player joins a server.
	 */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {

    	/*
    	 * Details will be handled within the PlayerManager
    	 */
    	PlayerManagement.get().onPlayerJoin(event);
    	
    }// end onPlayerJoin
    
    
	/**
	 * Called when a player leves a server.
	 */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {

    	/*
		 * Instant execution.
		 */
		PlayerManagement.get().onPlayerQuit(event);
    	
    }// end onPlayerQuitEvent
    
}// end class EventListener
