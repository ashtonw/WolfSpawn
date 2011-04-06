/**
 * 
 */
package com.jynxdaddy.wolfspawn;

import java.util.logging.Logger;

import net.minecraft.server.EntityWolf;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Handler for /putdown command
 * @author Ashton
 *
 */
public class WolfCommand implements CommandExecutor {

	private WolfSpawn plugin;
	@SuppressWarnings("unused")
	private static Logger log;
	
	public WolfCommand(WolfSpawn wolfSpawn) {
		this.plugin = wolfSpawn;
		WolfCommand.log = WolfSpawn.log;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
        if (label.compareTo("spawnwolf") == 0)
        	return spawnWolf(sender, command, label, args);
        
        if (label.compareTo("putdown") == 0)
        	return putDown(sender, command, label, args);
        
		return false;
	}

	private boolean putDown(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
            Player player = (Player) sender;
            
            if (!plugin.getPermission(player, "WolfSpawn.putdown")) return true;
            
            if (plugin.addPutDownPlayer(player.getName())) {
            	plugin.sendMessage(player, WolfSpawn.Message.PUT_DOWN_TOGGLE_ON);
            }
            else {
            	plugin.removePutDownPlayer(player.getName()); //toggle off
            	plugin.sendMessage(player, WolfSpawn.Message.PUT_DOWN_TOGGLE_OFF);
            }
            return true;
        } else {
            return false;
        }
	}

	private boolean spawnWolf(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = null;
		
		if (sender instanceof Player)
			player = (Player) sender;
		
		if (args.length == 1) {
			Player targetPlayer = sender.getServer().getPlayer(args[0]);
			if (targetPlayer != null)
				player = targetPlayer;
			else
				sender.sendMessage("[WolfSpawn] No user '" + args[0] + "' found");
		}
		
		if (sender instanceof ConsoleCommandSender && player == null)
			return false;
		
		boolean wild = (args.length == 2 && args[1].equalsIgnoreCase("wild"));
		//spawn wolf, should reuse code somewhere...
		Location spawn = player.getLocation();
		LivingEntity newWolf = player.getWorld().spawnCreature(spawn,
				CreatureType.WOLF);
		
		int health = plugin.cfg.getInt("wolf-respawn-health", 5);
		health = health > 0 && health <= 20 ? health : 5;
		if (health <= 0 || health > 20) health = 5;
		
		EntityWolf newMcwolf = ((CraftWolf)  newWolf).getHandle();
		newMcwolf.a(wild ? "" : player.getName()); //setOwner
		newMcwolf.d(true); // owned?
		newMcwolf.b(true); // sitting
		newMcwolf.health = health;
		
		return true;
	}

}
