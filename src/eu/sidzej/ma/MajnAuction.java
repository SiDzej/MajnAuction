package eu.sidzej.ma;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.sidzej.ma.listeners.SignListener;
import eu.sidzej.ma.ulits.ParticleEffect;

public class MajnAuction extends JavaPlugin {
	
	public String log_prefix;
	static final Logger log = Logger.getLogger("Minecraft");
	private File pluginFolder;
    private File langFile;
    private File configFile;
    private CommandHandler commandHandler;
    public ParticleEffect particleEffect;
	
	public void onEnable(){
		getServer().getPluginManager().registerEvents(new SignListener(this), this);
		this.log_prefix = ChatColor.translateAlternateColorCodes('&',
                "&5-----[  &fPurpleIRC&5 - &f" + getServer().getPluginManager()
                .getPlugin("PurpleIRC").getDescription().getVersion() + "&5 ]-----");
		
		logInfo("MajnAuction enabled.");
		
		commandHandler = new CommandHandler(this);
		getCommand("ma").setExecutor(commandHandler);
		
		particleEffect = new ParticleEffect(this); // create class for effect
	} 
	
	
	public void onDisable(){
		getLogger().info("MajnAuction disabled.");
	}
	
	
	
	/**
    *
    * @param message
    */
   public void logInfo(String message) {
       log.log(Level.INFO, String.format("%s %s", log_prefix, message));
   }

   /**
    *
    * @param message
    */
   public void logError(String message) {
       log.log(Level.SEVERE, String.format("%s %s", log_prefix, message));
   }

   /**
    *
    * @param message
    *
   public void logDebug(String message) {
       if (debugEnabled) {
           log.log(Level.INFO, String.format("%s [DEBUG] %s", log_prefix, message));
       }
   }*/

}
