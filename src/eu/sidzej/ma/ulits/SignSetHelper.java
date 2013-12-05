package eu.sidzej.ma.ulits;

import org.bukkit.event.block.SignChangeEvent;

public class SignSetHelper {
	
	private SignChangeEvent e;
	
	public SignSetHelper(SignChangeEvent e){
		this.e = e;
	}
	
	public String getline(int i){
		return e.getLine(i);
	}
	
	public void setLine(int i, String msg){
		e.setLine(i, msg);
	}
}
