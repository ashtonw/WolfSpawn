package com.jynxdaddy.wolfspawn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import net.minecraft.server.EntityWolf;

import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * WolfSpawn
 * 
 * @author ashtonw
 */
public class WolfSpawn extends JavaPlugin {
	
	private final WolfListener wolfListener = new WolfListener(this);
	@SuppressWarnings("unused")
	private final WPlayerListener playerListener = new WPlayerListener(this);
	
	private final WolfCommand wolfCommand = new WolfCommand(this);

	public static Logger log = Logger.getLogger("Minecraft");
	public Configuration cfg;
	public static PermissionHandler permissions;
	
	private HashSet<String> releaseUsers = new HashSet<String>(10);

	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " version " + pdfFile.getVersion()
				+ " disabled!");
		
		
	}

	public void onEnable() {
		//Config
		readyConfig();
		cfg = this.getConfiguration();
		setupPermissions();
		
		// Register our events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DEATH, wolfListener,	Priority.Monitor, this);
		//pm.registerEvent(Event.Type.PLAYER_TELEPORT, playerListener, Priority.Monitor, this);
		//pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Priority.Monitor, this);
		
		//register commands
		getCommand("releasewolf").setExecutor(wolfCommand);
		getCommand("spawnwolf").setExecutor(wolfCommand);

		PluginDescriptionFile pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " version " + pdfFile.getVersion()
				+ " is enabled!");
	}

	private void setupPermissions() {
		Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");

	      if (WolfSpawn.permissions == null) {
	          if (test != null) {
	              WolfSpawn.permissions = ((Permissions)test).getHandler();
	          } else {
	              log.info("Permission system not detected");
	          }
	      }
	}
	
	public boolean permsOn() {
		return WolfSpawn.permissions != null;
	}
	
	public boolean getPermission(Player player, String permission) {
		if (permissions == null) return true;
		return permissions.has(player, permission);
	}

	public boolean addReleasePlayer(String name) {
		return releaseUsers.add(name);
	}
	
	public boolean isReleasePlayer(String name) {
		return releaseUsers.contains(name);
	}
	
	public boolean removeReleasePlayer(String name) {
		return releaseUsers.remove(name);
	}
	
	/*
	 * Checks that config exists, if it doesn't, extract default from jar
	 * Based on zydeco / PickBoat 
	 */
	private void readyConfig() {
		File configFile = new File(this.getDataFolder(), "config.yml");

		if (!configFile.exists()) {
			try {
				configFile.getParentFile().mkdirs();
				JarFile jar = new JarFile(this.getFile());
				JarEntry entry = jar.getJarEntry("config.yml");
				InputStream is = jar.getInputStream(entry);
				FileOutputStream os = new FileOutputStream(configFile);
				byte[] buf = new byte[(int)entry.getSize()];
				is.read(buf, 0, (int)entry.getSize());
				os.write(buf);
				os.close();
				this.getConfiguration().load();
			} catch (Exception e) {
				log.info("WolfSpawn: could not create configuration file");
				// unload plugin??
			}
		}
	}

	public enum Message {
		RELEASE_TOGGLE_ON,
		RELEASE_TOGGLE_OFF,
		WOLF_RELEASE,
		WOLF_DEATH
	}
	
	public boolean sendMessage(Player player, Message msg) {
		if (player == null) return false;
		if (!cfg.getBoolean("msg-send", true)) return true;
		
		switch (msg) {
		case RELEASE_TOGGLE_ON:
			if (!cfg.getBoolean("msg-release-toggle", true)) break;
			player.sendMessage(cfg.getString("msg-release-toggle-on-text", ""));
			break;
		case RELEASE_TOGGLE_OFF:
			if (!cfg.getBoolean("msg-release-toggle", true)) break;
			player.sendMessage(cfg.getString("msg-release-toggle-off-text", ""));
			break;
		case WOLF_RELEASE:
			if (!cfg.getBoolean("msg-wolf-release", true)) break;
			player.sendMessage(cfg.getString("msg-wolf-release-text", ""));
			break;
		case WOLF_DEATH:
			if (!cfg.getBoolean("msg-death", true)) break;
			player.sendMessage(cfg.getString("msg-death-text", ""));
			break;
		default:
			break;
		}
		return true;
	}
	public void spawnWolf(Player player, World world, String owner) {
		boolean onPlayer = this.getPermission(player, "WolfSpawn.spawnatplayer") && cfg.getBoolean("wolf-spawn-onplayer", true);
		spawnWolf(player, world, owner, onPlayer);
	}
	public void spawnWolf(Player player, World world, String owner, boolean onPlayer) {

		int health = cfg.getInt("wolf-respawn-health", 5);
		health = health > 0 && health <= 20 ? health : 5;
		if (health <= 0 || health > 20) health = 5;
		
		Runnable task = new SpawnWolfTask(player, world, owner, health, onPlayer);
		// seconds ~= 20 * ticks
		getServer().getScheduler().scheduleAsyncDelayedTask(this, task, 20 * cfg.getInt("wolf-respawn-delay", 30));
		
	}
	
	public class SpawnWolfTask implements Runnable {
		
		private String owner;
		private Player player;
		private World world;
		private int health;
		private boolean onPlayer;
		
		public SpawnWolfTask(Player player, World world, String owner, int health, boolean onPlayer) {
			this.player = player;
			this.world = world;
			this.owner = owner;
			this.health = health;
			this.onPlayer = onPlayer;
		}

		public void run() {
			LivingEntity newWolf = world.spawnCreature(onPlayer ? player.getLocation() : player.getWorld().getSpawnLocation(), CreatureType.WOLF);
			
			owner = owner == null ? "" : owner;
			boolean owned = owner != ""; 
			
			EntityWolf newMcwolf = ((CraftWolf)  newWolf).getHandle();
			newMcwolf.a(owner); //setOwner
			newMcwolf.d(owned); // sitting
			newMcwolf.b(owned); // ?
			newMcwolf.health = health;
		}
	}

}
