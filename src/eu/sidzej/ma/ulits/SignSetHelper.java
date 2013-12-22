package eu.sidzej.ma.ulits;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

public class SignSetHelper {
	
	private Sign b;
	
	public SignSetHelper(BlockState b){
		this.b = (Sign)b;
	}
	
	public String getline(int i){
		return b.getLine(i);
	}
	
	public void setLine(int i, String msg){
		b.setLine(i, msg);
		b.update();
	}
}
