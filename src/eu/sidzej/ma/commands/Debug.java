package eu.sidzej.ma.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import eu.sidzej.ma.MajnAuction;
import eu.sidzej.ma.ulits.Config;

public class Debug extends CommandBase{
	private final MajnAuction plugin;
    private final String usage 	= "[on|off]";
    @SuppressWarnings("unused")
	private final String desc 	= "Enable or disable debug mode.";
    private final String name	= "debug";
    private final String fullUsage = bad + ChatColor.AQUA + "/ma " + name + " " + usage;
	
	public Debug(MajnAuction plugin){
		this.plugin = plugin;
		// TODO language
	}

	@Override
	public void dispatch(CommandSender sender, String[] args) {
		if (args.length == 1){
			if (sender.hasPermission("majnAuction.password")){
				sender.sendMessage(ChatColor.GREEN + "Debug mod is " + ((Config.debugEnabled ? "on":"off")));
			}
		}
		else if (args.length == 2) {
			if (sender.hasPermission("majnAuction.password")){
    			if(args[1].equalsIgnoreCase("on")){
    				Config.debugEnabled = true;
    				plugin.getConfig().set("debug", true);
    			}
    			else if(args[1].equalsIgnoreCase("off")){
    				Config.debugEnabled = false;
    				plugin.getConfig().set("debug", false);
    			}
    			else {
    	            sender.sendMessage(fullUsage);
    	        }
    			sender.sendMessage(ChatColor.GREEN + "Debug mod " + ((Config.debugEnabled ? "on":"off")));
    		}
        } else {
            sender.sendMessage(fullUsage);
        }
	}
}
