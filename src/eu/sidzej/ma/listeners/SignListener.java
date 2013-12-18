package eu.sidzej.ma.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Sign;

import eu.sidzej.ma.MajnAuction;
import eu.sidzej.ma.ulits.Log;
import eu.sidzej.ma.ulits.ParticleEffect;

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
		Log.info("cedule");
		Block b = e.getBlock();
		
		if (b.getType() != Material.WALL_SIGN) 
			return;
		Sign s = (Sign) b.getState().getData();
		Block attachedBlock = b.getRelative(s.getAttachedFace());
		
		Log.info("na bloku");
		if (attachedBlock.getType().compareTo(Material.ENDER_CHEST) != 0)
			return;	
		
		Log.info("na enderbedne");
		String line = e.getLine(0);
		for (int i = 0; i < labels.length+1; i++){
			if (i == labels.length){
				return;
			}
			if (line.equalsIgnoreCase(labels[i])){
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
	
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
            // right click only
            if( event.getAction() != Action.RIGHT_CLICK_BLOCK &&
                    event.getAction() != Action.RIGHT_CLICK_AIR) return;
            Block block = event.getClickedBlock();
            // not a sign
            if(block == null) return;
            if(block.getType() != Material.ENDER_CHEST && block.getType() != Material.WALL_SIGN) return;
            if(event.getPlayer().isSneaking() && event.getPlayer().getItemInHand() != null 
            		&& event.getPlayer().getItemInHand().getType().equals(Material.AIR))
            	return;
            
            if(block.getType() == Material.WALL_SIGN){
            	block = block.getRelative(((Sign)block.getState().getData()).getAttachedFace());
            }
            
            // it's a sign
            // 
            Location l = block.getLocation();
            
            ParticleEffect.sendToLocation(ParticleEffect.INSTANT_SPELL, l, 1.0F, 1.0F, 1.0F, 0, 20);
            
            event.getPlayer().openInventory(Bukkit.getServer().createInventory(null, 54, "MajnAuction"));
            event.setCancelled(true);
            event.getPlayer().playSound(l, Sound.WITHER_DEATH, 10, 1);
            
         
            
            
	}
	
	/*
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
            
            
    }*/
}
