package eu.sidzej.ma;

import org.bukkit.Location;

public class AuctionPoint {
	
	private Location l;
	private Location center;
	private int id;
	private String name;
	
	public AuctionPoint(int id, Location l, String name){
		this.id = id;
		this.l = l;
		this.name = name;
		
		center = l.clone();
		center.setX(l.getX()+0.5);
		center.setZ(l.getZ()+0.5);
	}
	
	public int getId(){
		return this.id;
	}

	public Location getLocation(){
		return this.l;
	}
	
	public Location getCenterLocation(){
		return center;
	}
	
	public String getName(){
		return this.name;
	}
}
