package eu.sidzej.ma.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.sidzej.ma.MajnAuction;

public class Password implements CommandInterface{
	private final MajnAuction plugin;
    private final String usage 	= "[password]";
    private final String desc 	= "Set or reset password for web-auction access.";
    private final String name	= "password";
    private final String fullUsage = bad + ChatColor.AQUA + "/ma " + name + " " + usage;
	
	public Password(MajnAuction plugin){
		this.plugin = plugin;
		// TODO language
	}

	@Override
	public void dispatch(CommandSender sender, String[] args) {
		if (args.length == 2) {
			if (!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "Console can't do that! :)");
				return;
			}
			if (sender.hasPermission("majnAuction.password")){
    			plugin.db.setPassword(sender.getName(), args[1]);
    			sender.sendMessage(ChatColor.DARK_PURPLE + "Your password has been set.");
    		}
        } else {
            sender.sendMessage(fullUsage);
        }
	}
	
	@Override
    public String name() {
        return name;
    }

    @Override
    public String desc() {
        return desc;
    }

    @Override
    public String usage() {
        return usage;
    }
}
