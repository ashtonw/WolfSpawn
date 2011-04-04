
package com.jynxdaddy.wolfspawn;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

/**
 * WolfSpawn
 *
 * @author ashtonw
 */
public class WolfSpawn extends JavaPlugin {
    private final WolfListener wolfListener = new WolfListener(this);
	//private final SamplePlayerListener playerListener = new SamplePlayerListener(this);
    //private final SampleBlockListener blockListener = new SampleBlockListener(this);
    
    public static Logger log = Logger.getLogger("Minecraft");
    public Configuration cfg;

    public void onDisable() {
    	PluginDescriptionFile pdfFile = this.getDescription();
    	log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " disabled!" );
    }

    public void onEnable() {
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.ENTITY_DEATH, wolfListener, Priority.Normal, this);

        PluginDescriptionFile pdfFile = this.getDescription();
        log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
        
        
        File configFile = new File(this.getDataFolder(), "config.yml");
        boolean existed = configFile.exists();
        cfg = this.getConfiguration();
        
        
        
        if (!existed)
        	cfg.setProperty("Test", true);
        cfg.save();
    }
    
    
}
