package eu.sidzej.ma.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public abstract class CommandBase {
	private String usage;
	private String desc;
	private String name;
	
	final String bad = ChatColor.RED + "Bad usage! " + ChatColor.WHITE + "Try: ";
	
	public void dispatch(CommandSender sender, String[] args){}
	
	public String name() {
		return name;
	}
	
	public String desc() {
		return desc;
	}

	public String usage() {
		return usage;
	} 
}
