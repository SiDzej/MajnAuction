package eu.sidzej.ma.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import eu.sidzej.ma.MajnAuction;

/**
 * @author _CJ_
 * 
 * thanks to cnaude - PurpleIRC plugin for bukkit
 */
public class Help implements CommandInterface {
	private final MajnAuction plugin;
    private final String usage 	= "[command]";
    private final String desc 	= "Display help on a specific command or list all commands.";
    private final String name	= "help";
	
	public Help(MajnAuction plugin){
		this.plugin = plugin;
		// TODO language
	}

	@Override
    public void dispatch(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            String s = args[1];
            if (plugin.commandHandler.commands.containsKey(s)) {
                sender.sendMessage(helpStringBuilder(
                        plugin.commandHandler.commands.get(s).name(),
                        plugin.commandHandler.commands.get(s).desc(),
                        plugin.commandHandler.commands.get(s).usage()));
                return;
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid sub command: " 
                        + ChatColor.WHITE + s);
                return;
            }
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
        		"&b-----[  &fMajnAuction&b - &f" + plugin.version + "&b ]-----"));
        for (String s : plugin.commandHandler.sortedCommands) {
        	if (s.equals("pass")){
        		sender.sendMessage(helpStringBuilder("pass","Alternative for /irc password.","[password]"));
        				continue;
        	}
            if (plugin.commandHandler.commands.containsKey(s)) {
                sender.sendMessage(helpStringBuilder(
                        plugin.commandHandler.commands.get(s).name(),
                        plugin.commandHandler.commands.get(s).desc(),
                        plugin.commandHandler.commands.get(s).usage()));
            }
        }

    }

    private String helpStringBuilder(String n, String d, String u) {
        return ChatColor.translateAlternateColorCodes('&', "&b/irc "
                + n + " &e" + u + " &f- " + d);
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
