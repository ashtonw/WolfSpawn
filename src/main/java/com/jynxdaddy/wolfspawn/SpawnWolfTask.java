package com.jynxdaddy.wolfspawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

/**
 * ssync Wolf spawning with delay in seconds
 * 
 * @author Ashton
 *
 */
class SpawnWolfTask implements Runnable {
	/**
	 * 
	 */
	private final WolfSpawn plugin;
	private final String owner;
	private final Player player;
	private final World world;
	private final int health;
	private boolean onPlayer;
	private boolean isAngry;
	
	public SpawnWolfTask(final WolfSpawn wolfSpawn, final Player player, final World world, final String owner, final int health, final boolean onPlayer) {
		this.plugin = wolfSpawn;
		this.player = player;
		this.world = world;
		this.owner = owner;
		this.health = health;
		this.onPlayer = onPlayer;
		this.isAngry = false;
	}

	public void run() {
		if (!player.isOnline()) return; //disconnected
		Location location = onPlayer ? player.getLocation() : player.getWorld().getSpawnLocation();
		UpdatedWolf wolf = new UpdatedWolf((Wolf) world.spawnCreature(location, CreatureType.WOLF));
		String ownerName = owner == null ? "" : owner;
		wolf.setAngry(isAngry);
		wolf.setOwner(ownerName);
		
		plugin.sendMessage(player, WolfSpawn.Message.WOLF_SPAWN);
	}
	
	public boolean isOnPlayer() {
		return onPlayer;
	}

	public void setOnPlayer(boolean onPlayer) {
		this.onPlayer = onPlayer;
	}

	public boolean isAngry() {
		return isAngry;
	}

	public void setAngry(boolean isAngry) {
		this.isAngry = isAngry;
	}
}