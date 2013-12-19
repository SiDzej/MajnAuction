package eu.sidzej.ma;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import eu.sidzej.ma.db.ConnectionManager;
import eu.sidzej.ma.db.Database;
import eu.sidzej.ma.listeners.SignListener;
import eu.sidzej.ma.ulits.Config;
import eu.sidzej.ma.ulits.Log;
import eu.sidzej.ma.ulits.ParticleEffect;

public class MajnAuction extends JavaPlugin {

	private File pluginFolder;
	private File langFile;
	private File configFile;
	private CommandHandler commandHandler;
	public ParticleEffect particleEffect;
	public Database db;

	public Config config;
	public String version;
	public static String name;

	public List<AuctionPoint> pointList;

	public void onEnable() {

		// PluginManager pm = getServer().getPluginManager();

		name = this.getDescription().getName();
		version = this.getDescription().getVersion();

		Log.info("Starting " + name + " " + version + "...");

		config = new Config(this);
		
		// Required - Vault,
		checkDependencies(getServer().getPluginManager());
		
		try {
			db = new Database(this);
		} catch (ClassNotFoundException e) {
			Log.error("Can't join database. com.mysql.jdbc.Driver not found.");
			this.disable();
		}
		
		/*
		 * for lang getServer().getPluginManager().registerEvents(new SignListener(this), this);
		 * pluginFolder = getDataFolder(); configFile = new File(pluginFolder, "config.yml");
		 * createConfig(); getConfig().options().copyDefaults(true); saveConfig(); loadConfig();
		 */

		Log.debug("Debug enabled!"); // log only when enabled in config :)


		getServer().getPluginManager().registerEvents(new SignListener(this), this);

		commandHandler = new CommandHandler(this);
		getCommand("ma").setExecutor(commandHandler);

		this.pointList = new ArrayList<AuctionPoint>();
	}

	public void onDisable() {
		db.close();
		getLogger().info("MajnAuction disabled.");
	}

	public void disable() {
		Bukkit.getPluginManager().disablePlugin(this);
	}

	public void disable(String msg) {
		Log.error(msg);
		Bukkit.getPluginManager().disablePlugin(this);
	}

	/**
	 * Checks if required plugins are loaded
	 * 
	 * @param pm
	 *            PluginManager
	 */
	private void checkDependencies(PluginManager pm) {
		if (!pm.isPluginEnabled("Vault")) {
			Log.error("Vault is required for this plugin.");
			this.disable("Vault is required!");
		}

	}

}
