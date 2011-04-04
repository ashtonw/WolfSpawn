/**
 * 
 */
package com.jynxdaddy.wolfspawn;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityListener;

/**
 * @author Ashton
 *
 */
public class WolfListener extends EntityListener {
	
	private WolfSpawn plugin;
	private static Logger log;
	
	public WolfListener(WolfSpawn plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.entity.EntityListener#onEntityDeath(org.bukkit.event.entity.EntityDeathEvent)
	 */
	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		if (isWolf(event)) {
			log.info("onEntityDeath Wolf");
			
			
			Wolf wolf = (Wolf) event.getEntity();
			Player owner; //= wolf.getOwner();
			
			Location spawn = wolf.getWorld().getSpawnLocation();
			LivingEntity newWolf = wolf.getWorld().spawnCreature(spawn, CreatureType.WOLF);
			//newWolf.setOwner(owner);
			
		}
	}
	
	private boolean isWolf(EntityEvent event) {
		return event.getEntity() instanceof Wolf;
	}

}
