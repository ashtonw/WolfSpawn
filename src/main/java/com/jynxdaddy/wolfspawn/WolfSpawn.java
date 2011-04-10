package com.jynxdaddy.wolfspawn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.jynxdaddy.BetterConfig.BetterConfig;
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
	public BetterConfig cfg;
	public static PermissionHandler permissions;
	
	private HashSet<String> releaseUsers = new HashSet<String>(10);

	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info("["+ pdfFile.getName() + "] version " + pdfFile.getVersion()
				+ " disabled!");
		
		
	}

	public void onEnable() {
		//Config
		readyConfig();
		PluginDescriptionFile pdfFile = this.getDescription();
		cfg = new BetterConfig(this, this.getFile());
		//rethink this
		if (cfg.getDouble("version") < Double.parseDouble(pdfFile.getVersion()))
				log.info("[WolfSpawn] config.yml out of date, delete and restart");
		setupPermissions();
		
		// Register our events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DEATH, wolfListener,	Priority.Monitor, this);
		//pm.registerEvent(Event.Type.PLAYER_TELEPORT, playerListener, Priority.Monitor, this);
		//pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Priority.Monitor, this);
		
		//register commands
		getCommand("releasewolf").setExecutor(wolfCommand);
		getCommand("spawnwolf").setExecutor(wolfCommand);

		
		log.info("["+ pdfFile.getName() + "] version " + pdfFile.getVersion()
				+ " is enabled!");
		///////////////////////////////////////////
		
	}

	private void setupPermissions() {
		Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");

	      if (WolfSpawn.permissions == null) {
	          if (test != null) {
	              WolfSpawn.permissions = ((Permissions)test).getHandler();
	          } else {
	              log.info("[WolfSpawn] Permission system not detected");
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
		WOLF_DEATH, WOLF_SPAWN
	}
	
	public boolean sendMessage(Player player, Message msg) {
		if (player == null) return false;
		if (!cfg.getBoolean("messages.enabled")) return true;
		
		switch (msg) {
		case RELEASE_TOGGLE_ON:
			if (!cfg.getBoolean("messages.release-toggle-on.enabled")) break;
			sendMsg(player,cfg.getString("messages.release-toggle-on.text"));
			break;
		case RELEASE_TOGGLE_OFF:
			if (!cfg.getBoolean("messages.release-toggle-off.enabled")) break;
			sendMsg(player,cfg.getString("messages.release-toggle-off.text"));
			break;
		case WOLF_RELEASE:
			if (!cfg.getBoolean("messages.release.enabled")) break;
			sendMsg(player,cfg.getString("messages.release.text"));
			break;
		case WOLF_DEATH:
			if (!cfg.getBoolean("messages.death.enabled")) break;
			sendMsg(player,cfg.getString("messages.death.text"));
			break;
		case WOLF_SPAWN:
			if (!cfg.getBoolean("messages.respawn.enabled")) break;
			sendMsg(player,cfg.getString("messages.respawn.text"));
		default:
			break;
		}
		return true;
	}
	
	/**
	 * Send message if not empty string.
	 * 
	 * @param player
	 * @param msg
	 */
	private void sendMsg(Player player, String msg) {
		if (msg != "") player.sendMessage(msg);
	}
	
	public void respawnWolf(Player player, World world, String owner) {
		boolean onPlayer = this.getPermission(player, "WolfSpawn.spawnatplayer") && cfg.getBoolean("wolf.spawn.on-player");
		int delay = cfg.getInt("wolf.spawn.delay");
		delay = delay < 1 ? 1 : delay; 
		spawnWolf(player, world, owner, onPlayer, delay, false);
	}
	
	public void spawnWolf(Player player, World world, String owner, boolean onPlayer) {
		spawnWolf(player, world, owner, onPlayer, 1, false);
	}
	
	public void spawnWolf(Player player, World world, String owner, boolean onPlayer, boolean angry) {
		spawnWolf(player, world, owner, onPlayer, 1, angry);
	}
	
	public void spawnWolf(Player player, World world, String owner, boolean onPlayer, int delay, boolean isAngry) {
		int health = cfg.getInt("wolf.spawn.health");
		health = health > 0 && health <= 20 ? health : 5;
		if (health <= 0 || health > 20) health = 5;
		
		SpawnWolfTask task = new SpawnWolfTask(this, player, world, owner, health, onPlayer);
		task.setAngry(isAngry);
		// seconds ~= 20 * ticks
		log.info("[WolfSpawn] spawning wolf in " + (20 * delay) + " seconds");
		getServer().getScheduler().scheduleAsyncDelayedTask(this, task, 20 * delay);
		
	}

}
