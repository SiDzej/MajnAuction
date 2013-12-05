package eu.sidzej.ma.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import eu.sidzej.ma.MajnAuction;

public interface CommandInterface {	
	final String bad = ChatColor.RED + "Bad usage! " + ChatColor.WHITE + "Try: ";
	void dispatch(CommandSender sender, String[] args);
    String name();
    String desc();
    String usage();  
}
