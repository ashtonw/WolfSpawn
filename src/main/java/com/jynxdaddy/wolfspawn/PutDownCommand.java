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

}
