
package com.jynxdaddy.wolfspawn;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * WolfSpawn
 *
 * @author ashtonw
 */
public class WolfSpawn extends JavaPlugin {
    //private final SamplePlayerListener playerListener = new SamplePlayerListener(this);
    //private final SampleBlockListener blockListener = new SampleBlockListener(this);
    
    public Logger log = Logger.getLogger("Minecraft");

    public void onDisable() {
    	PluginDescriptionFile pdfFile = this.getDescription();
    	log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " disabled!" );
    }

    public void onEnable() {
        // TODO: Place any custom enable code here including the registration of any events

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        //pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);

        PluginDescriptionFile pdfFile = this.getDescription();
        log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    
}
