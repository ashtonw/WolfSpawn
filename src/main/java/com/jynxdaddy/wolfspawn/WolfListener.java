/**
 * 
 */
package com.jynxdaddy.wolfspawn;

import java.util.logging.Logger;

import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;

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
	 * @see org.bukkit.event.entity.EntityListener#onCreatureSpawn(org.bukkit.event.entity.CreatureSpawnEvent)
	 */
	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (isWolf(event)) {
			log.info("onCreatureSpawn Wolf");
		}
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.entity.EntityListener#onEntityDeath(org.bukkit.event.entity.EntityDeathEvent)
	 */
	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		if (isWolf(event)) {
			log.info("onEntityDeath Wolf");
		}
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.entity.EntityListener#onEntityTarget(org.bukkit.event.entity.EntityTargetEvent)
	 */
	@Override
	public void onEntityTarget(EntityTargetEvent event) {
		if (isWolf(event)) {
			log.info("onEntityTarget Wolf");
		}
	}
	
	private boolean isWolf(EntityEvent event) {
		return event.getEntity() instanceof Wolf;
	}

}
