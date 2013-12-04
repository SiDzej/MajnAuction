package eu.sidzej.ma.listeners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.block.Sign;

import eu.sidzej.ma.MajnAuction;

public class SignListener implements Listener{
	
	private MajnAuction plugin;
	private String[] labels = {"[MajnAuction]", "[MA]", "ma"};
		
	public SignListener (MajnAuction plugin){
		this.plugin = plugin;		
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignPlace(final SignChangeEvent e){
		Player p = e.getPlayer();
		if ( p == null)
			return;
		
		String[] l = e.getLines();
		
		for (int i = 0; i < labels.length+1; i++){
			if (i == labels.length){
				return;
			}
			if (l[0].equalsIgnoreCase(labels[i])){
				break;
			}
		}
		/*// color codes from config
		StringBuilder sb = new StringBuilder(event.getLine(line));
        sb.setCharAt(ic - 1, ChatColor.COLOR_CHAR);
        event.setLine(line, sb.toString());*/
		e.setLine(0, ChatColor.COLOR_CHAR+"4"+plugin.getName());
		
		// no permissions
		if(!p.hasPermission("MajnAuction.placeSign")){
			e.setCancelled(true);
			e.getBlock().breakNaturally();
			e.getPlayer().sendMessage("no permissions");
			return;
		}
		
		
		// TODO jazyk
		p.sendMessage("Auction point created");		
		
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
            // right click only
            if( event.getAction() != Action.RIGHT_CLICK_BLOCK &&
                    event.getAction() != Action.RIGHT_CLICK_AIR) return;
            Block block = event.getClickedBlock();
            // not a sign
            if(block == null) return;
            if(block.getType() != Material.SIGN_POST && block.getType() != Material.WALL_SIGN) return;
            // it's a sign
            
            Location l = block.getLocation();
            
            plugin.particleEffect.run(l);
            
            event.getPlayer().openInventory(Bukkit.getServer().createInventory(null, 70, "MajnAuction"));
            
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
            Block b = event.getBlock();
            Player p = event.getPlayer();
            if(b.getType() != Material.SIGN_POST && b.getType() != Material.WALL_SIGN) return;
            
            Sign thisSign = (Sign) b.getState();           
            
            if(thisSign.getLine(0).equals("["+ChatColor.COLOR_CHAR+"4"+plugin.getName()+ChatColor.COLOR_CHAR+"r]")) {
                if(!p.hasPermission("ma.signRemove")) {
                    event.setCancelled(true);
                    p.sendMessage("nemas prava kamo");
                } else {
                    p.sendMessage("znicil si cedulu kamo.");
                }
            }
            
            
    }
}
