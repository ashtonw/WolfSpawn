/**
 * 
 */
package com.jynxdaddy.wolfspawn;

import java.util.logging.Logger;

import org.bukkit.event.player.PlayerListener;

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

}
