package eu.sidzej.ma;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor{
	
	private MajnAuction plugin;
	
	public CommandHandler(MajnAuction plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ma")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
		                "&5-----[  &fMajnAuction&5 - &f" + plugin.getServer().getPluginManager()
		                .getPlugin("MajnAuction").getDescription().getVersion() + "&5 ]-----"));
			} else {
				Player p = (Player) sender;
				p.sendMessage("Not implemented yet.");
			}
			return true;
		}
		return false;
	}
}
