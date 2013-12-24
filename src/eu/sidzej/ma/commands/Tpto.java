package eu.sidzej.ma.commands;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import eu.sidzej.ma.AuctionPoint;
import eu.sidzej.ma.MajnAuction;

public class Tpto implements CommandInterface {
	private final MajnAuction plugin;
	private final String usage = "[id|name]";
	private final String desc = "Teleports player to auction point.";
	private final String name = "tpto";
	private final String fullUsage = bad + ChatColor.AQUA + "/ma " + name + " " + usage;

	public Tpto(MajnAuction plugin) {
		this.plugin = plugin;
		// TODO language
	}

	@Override
	public void dispatch(CommandSender sender, String[] args) {
		if (args.length == 2) {
			if (sender.hasPermission("majnAuction.tpto")) {
				int id = 0;
				try{
					id = Integer.parseInt(args[1].trim());
				}
				catch (NumberFormatException e){
					//silently discard
				}
				Collection<AuctionPoint> col = plugin.pointList.values();
				Iterator<AuctionPoint> it = col.iterator();
				if(id != 0){
					while(it.hasNext()){
						AuctionPoint tmp = it.next();
						if (tmp.getId() == id){
							sender.getServer().getPlayer(sender.getName()).teleport(tmp.getCenterLocation());
							sender.sendMessage(ChatColor.GREEN + "Teleported to auction point: " + tmp.getName());
							return;
						}
							
					}
				}
				else{
					while(it.hasNext()){
						AuctionPoint tmp = it.next();
						if (tmp.getName().equalsIgnoreCase(args[1])){
							sender.getServer().getPlayer(sender.getName()).teleport(tmp.getCenterLocation());
							sender.sendMessage(ChatColor.GREEN + "Teleported to auction point: " + tmp.getName());
							return;
						}
							
					}
				}
				sender.sendMessage(ChatColor.RED + "Can't find given points id/name");				
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
