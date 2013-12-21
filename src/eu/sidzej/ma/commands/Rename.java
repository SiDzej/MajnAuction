package eu.sidzej.ma.commands;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import eu.sidzej.ma.AuctionPoint;
import eu.sidzej.ma.MajnAuction;
import eu.sidzej.ma.db.AuctionPointDBUtils;

public class Rename implements CommandInterface {
	private final MajnAuction plugin;
	private final String usage = "[id|name] ([new_name])";
	private final String desc = "Rename point given by id or name. If [newname] not given default value will be set.";
	private final String name = "rename";
	private final String fullUsage = bad + ChatColor.AQUA + "/ma " + name + " " + usage;

	public Rename(MajnAuction plugin) {
		this.plugin = plugin;
		// TODO language
	}

	@Override
	public void dispatch(CommandSender sender, String[] args) {
		if (args.length == 3 || args.length == 2) {
			if (sender.hasPermission("majnAuction.rename")) {
				int id = 0;
				try{
					id = Integer.parseInt(args[1].trim());
				}
				catch (NumberFormatException e){
					//silently discard
				}
				Collection<AuctionPoint> col = plugin.pointList.values();
				Iterator<AuctionPoint> it = col.iterator();
				String newname = (args.length == 3)? args[2]:"";
				if(id != 0){
					while(it.hasNext()){
						AuctionPoint tmp = it.next();
						if (tmp.getId() == id){
							AuctionPointDBUtils.renameAuctionPoint(tmp.getId(), newname);
							sender.getServer().getPlayer(sender.getName()).teleport(tmp.getCenterLocation());
							sender.sendMessage(ChatColor.GREEN + "Auction point " + tmp.getId() + " was renamed to "
									+ tmp.getName());
							return;
						}
							
					}
				}
				else{
					while(it.hasNext()){
						AuctionPoint tmp = it.next();
						if (tmp.getName().equalsIgnoreCase(args[1])){
							AuctionPointDBUtils.renameAuctionPoint(tmp.getId(), newname);
							sender.sendMessage(ChatColor.GREEN + "Auction point " + tmp.getId() + " was renamed to "
									+ tmp.getName());
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
