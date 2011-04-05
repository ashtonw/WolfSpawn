/**
 * 
 */
package com.jynxdaddy.wolfspawn;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handler for /putdown command
 * @author Ashton
 *
 */
public class PutDownCommand implements CommandExecutor {

	@SuppressWarnings("unused")
	private WolfSpawn plugin;
	@SuppressWarnings("unused")
	private static Logger log;
	
	public PutDownCommand(WolfSpawn wolfSpawn) {
		this.plugin = wolfSpawn;
		PutDownCommand.log = WolfSpawn.log;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (plugin.addPutDownPlayer(player.getName())) {
            	player.sendMessage("[WolfSpawn] PutDown enabled - your wolves won't respawn");
            }
            else {
            	plugin.removePutDownPlayer(player.getName()); //toggle off
            	player.sendMessage("[WolfSpawn] PutDown disabled - your wolves will respawn again");
            }
            return true;
        } else {
            return false;
        }
	}

}
