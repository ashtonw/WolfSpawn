/**
 * 
 */
package com.jynxdaddy.wolfspawn;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author Ashton
 * 
 */
public class WPlayerListener extends PlayerListener {

	@SuppressWarnings("unused")
	private WolfSpawn plugin;
	@SuppressWarnings("unused")
	private static Logger log;

	public WPlayerListener(WolfSpawn plugin) {
		this.plugin = plugin;
		WPlayerListener.log = WolfSpawn.log;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.player.PlayerListener#onPlayerTeleport(org.bukkit.event.player.PlayerTeleportEvent)
	 */
	@Override
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled()) return;
		
		Location to = event.getTo();
		
		
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.player.PlayerListener#onPlayerRespawn(org.bukkit.event.player.PlayerRespawnEvent)
	 */
	@Override
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		
		Location loc = event.getRespawnLocation();
		
		
		
	}

}
