package eu.sidzej.ma.ulits;

import org.bukkit.configuration.Configuration;

import eu.sidzej.ma.MajnAuction;

public class Config {

	public static String host, port, database, pass, user; // db
	public static boolean debugEnabled;

	private static Configuration config;
	private MajnAuction plugin;

	public Config(MajnAuction plugin) {
		this.plugin = plugin;
		
		config = plugin.getConfig().getRoot();
		config.options().copyDefaults(true);
		config.set("version", plugin.version);
		plugin.saveConfig();

		debugEnabled = config.getBoolean("debug");
		/** DB **/
		host = config.getString("mysql.host");
		port = config.getString("mysql.port");
		database = config.getString("mysql.database");
		pass = config.getString("mysql.password");
		user = config.getString("mysql.user");
	}
}
