package eu.sidzej.ma.db;

import java.sql.Connection;

import org.bukkit.Bukkit;

import eu.sidzej.ma.MajnAuction;

public class Database {
	
	private MajnAuction plugin;
	private MySQL MySQL;
	private Connection c;
	
	public Database(MajnAuction plugin){
		this.plugin = plugin;
		
		MySQL = new MySQL(plugin, plugin.host, plugin.port,
				plugin.database, plugin.user, plugin.pass);
		c = null;
	}
	
	
	
	public void connect(){
		c = MySQL.openConnection();
		if(c != null){
			plugin.logInfo("Connected to database.");
			return;
		}
		plugin.logError("Can't work without database.");
		Bukkit.getPluginManager().disablePlugin(plugin);
	}
	
	public void close(){
		MySQL.closeConnection();
	}

}
