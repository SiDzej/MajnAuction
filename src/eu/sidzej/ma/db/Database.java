package eu.sidzej.ma.db;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.bukkit.Location;

import com.google.common.io.CharStreams;

import eu.sidzej.ma.MajnAuction;

public class Database {
	
	private MajnAuction plugin;
	private MySQL MySQL;
	private Connection c;
	private List<String> table_names = Arrays.asList("ma_players","ma_data_values",
			"ma_auction_points");
	
	public Database(MajnAuction plugin){
		this.plugin = plugin;
		
		MySQL = new MySQL(plugin, plugin.host, plugin.port,
				plugin.database, plugin.user, plugin.pass);
		c = null;
		
	}
	
	
	
	public void saveAuctionPoint(Location l,String name){
		if(name.trim().isEmpty())
			name = "Point " + (plugin.pointList.size() + 1);
		else
			name = "Point " + 1;
		MySQL.updateSQL("INSERT INTO ma_auction_points (name,x,y,z) VALUES ('" + name +
				"','" + l.getBlockX() + "'," + l.getBlockY() + "'," + l.getBlockZ() + "')");
		
		
	}
	
	public boolean setPassword(String p, String s){
		String password = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes("UTF-8"));
			byte[] digest = md.digest();
			password = DatatypeConverter.printHexBinary(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {   
		    e.printStackTrace();
		}
		
		ResultSet tmp = MySQL.querySQL("SELECT id FROM ma_players WHERE nick=\""+p+"\"");
			
		try {
			if (tmp.next()){
				MySQL.updateSQL("UPDATE ma_players SET password=\""+password+"\" WHERE nick=\""+p+"\"");
				plugin.logDebug("Chaning "+p+" password");
			}
			else{
				MySQL.updateSQL("INSERT INTO ma_players (nick,password) VALUES (\""+p+"\",\""+password+"\")");
				plugin.logDebug("Creating "+p+" account");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	private void createMajnAuctionDB(){
		plugin.logInfo("Creating MajnAuctionDB structure...");
        Statement s;
		try {
			s = c.createStatement();
			if (plugin.getResource("CREATE.sql") == null){
				plugin.disable("No CREATE.sql file found! Critical error.");
				return;
			}
			String[] queries = CharStreams.toString(new InputStreamReader(plugin.getResource("CREATE.sql"))).split(";");
	        for (String query : queries) {
	            plugin.logInfo("Creating DB table");
	            s.execute(query);
	        }
		} catch (SQLException | IOException e) {
			plugin.logError(e.getMessage());
			plugin.disable("Can't create database.");
		}
	}
	
	public void connect(){
		c = MySQL.openConnection();
		if(c == null){
			plugin.logError("Can't work without database.");
			plugin.disable();
			
			return;
		}
		plugin.logInfo("Connected to database.");
		
		// check if database is complete
		Iterator<String> iterator = table_names.iterator();
		while(iterator.hasNext())
			if(!tableExists(iterator.next())){
				createMajnAuctionDB();
				break;
			}
	}
	
	public void close(){
		MySQL.closeConnection();
	}
	
	private boolean tableExists(String table) {
		ResultSet rs = MySQL.querySQL("SHOW TABLES LIKE '" + table + "';");
		if (rs != null) return true;
		return false;
		}
}
