/**
 * 
 */
package com.jynxdaddy.wolfspawn;

import java.util.logging.Logger;

import net.minecraft.server.EntityWolf;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftWolf;
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
	@SuppressWarnings("unused")
	private static Logger log;

	public WolfListener(WolfSpawn plugin) {
		this.plugin = plugin;
		WolfListener.log = WolfSpawn.log;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bukkit.event.entity.EntityListener#onEntityDeath(org.bukkit.event
	 * .entity.EntityDeathEvent)
	 */
	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		if (isWolf(event)) {
			Wolf wolf = (Wolf) event.getEntity();
			
			//Workaround
			EntityWolf mcwolf = ((CraftWolf)  wolf).getHandle();
			String owner = mcwolf.v();
			
			Player player = plugin.getServer().getPlayer(owner);
			if (!plugin.getPermission(player, "WolfSpawn.respawn")) return; //NPE
			
			if (plugin.isPutDownPlayer(owner)) {
				plugin.sendMessage(player, WolfSpawn.Message.WOLF_PUT_DOWN);
			}
			else if (owner != null && owner.length() > 0) {
				Location spawn = wolf.getWorld().getSpawnLocation();
				LivingEntity newWolf = wolf.getWorld().spawnCreature(spawn,
						CreatureType.WOLF);
				
				int health = plugin.cfg.getInt("wolf-respawn-health", 5);
				health = health > 0 && health <= 20 ? health : 5;
				if (health <= 0 || health > 20) health = 5;
				
				EntityWolf newMcwolf = ((CraftWolf)  newWolf).getHandle();
				newMcwolf.a(owner); //setOwner
				newMcwolf.d(true); // owned?
				newMcwolf.b(true); // sitting
				newMcwolf.health = health;
				
				plugin.sendMessage(player, WolfSpawn.Message.WOLF_DEATH);
			}
		}
	}

	private boolean isWolf(EntityEvent event) {
		return event.getEntity() instanceof Wolf;
	}

}
