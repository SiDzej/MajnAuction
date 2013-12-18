package eu.sidzej.ma;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import eu.sidzej.ma.db.Database;
import eu.sidzej.ma.listeners.SignListener;
import eu.sidzej.ma.ulits.Log;
import eu.sidzej.ma.ulits.ParticleEffect;

public class MajnAuction extends JavaPlugin {
	
	private File pluginFolder;
    private File langFile;
    private File configFile;
    private CommandHandler commandHandler;
    public ParticleEffect particleEffect;
    public Database db;
    public String host,port,database,pass,user;
    public List<AuctionPoint> pointList;
	
	public void onEnable(){

		
		
		getServer().getPluginManager().registerEvents(new SignListener(this), this);
		pluginFolder = getDataFolder();
        configFile = new File(pluginFolder, "config.yml");
        createConfig();
        getConfig().options().copyDefaults(true);
        saveConfig(); loadConfig();
		
        Log.logDebug("Debug enabled!"); // log only when enabled in config :)
        
        db = new Database(this);
        db.connect(); // connect
        
		commandHandler = new CommandHandler(this);
		getCommand("ma").setExecutor(commandHandler);
		
		
		this.pointList = new ArrayList<AuctionPoint>();
	} 
	
	
	public void onDisable(){
		db.close();
		getLogger().info("MajnAuction disabled.");
	}
	
   public void disable(){
	   Bukkit.getPluginManager().disablePlugin(this);
   }
   
   public void disable(String msg){
	   Log.logError(msg);
	   Bukkit.getPluginManager().disablePlugin(this);
   }

   
   
   // load config from file
   private void loadConfig() {
       //debugEnabled = getConfig().getBoolean("debug");
       /** DB **/
       host = getConfig().getString("mysql.host");
       port = getConfig().getString("mysql.port");
       database = getConfig().getString("mysql.database");
       pass = getConfig().getString("mysql.password");
       user = getConfig().getString("mysql.user");
       
   }
   
   // create config folder and config file
   private void createConfig() {
       if (!pluginFolder.exists()) {
           try {
               pluginFolder.mkdir();
           } catch (Exception e) {
               Log.logError(e.getMessage());
           }
       }

       if (!configFile.exists()) {
           try {
               configFile.createNewFile();
           } catch (IOException e) {
        	   Log.logError(e.getMessage());
           }
       }
   }

}
