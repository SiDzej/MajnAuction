package eu.sidzej.ma;

import org.bukkit.Location;

public class AuctionPoint {
	
	private Location l;
	private int id;
	private String name;
	
	public AuctionPoint(int id, Location l, String name){
		this.id = id;
		this.l = l;
		this.name = name;	
	}
	
	public int getId(){
		return this.id;
	}

	public Location getLocation(){
		return this.l;
	}
	
	public String getName(){
		return this.name;
	}
}
