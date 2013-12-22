package eu.sidzej.ma.ulits;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class SignSetHelper {
	
	private static Sign b;
	
	public SignSetHelper(Block b){
		SignSetHelper.b = (Sign)b;
	}
	
	public String getline(int i){
		return b.getLine(i);
	}
	
	public void setLine(int i, String msg){
		b.setLine(i, msg);
	}
}
