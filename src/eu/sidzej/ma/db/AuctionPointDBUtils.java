package eu.sidzej.ma.db;

import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Location;

import eu.sidzej.ma.utils.Log;

public class AuctionPointDBUtils {
	
	public static boolean saveAuctionPoint(Location l, String name, int id) {
		TimedConnection c = null;
		Statement s = null;
		try {
			c = Database.getConnection();
			s = c.createStatement();

			if (name.trim().isEmpty())
				name = "Point " + id;

			s.execute("INSERT INTO ma_auction_points (name,x,y,z,world) VALUES (\"" + name + "\",\""
					+ l.getBlockX() + "\",\"" + l.getBlockY() + "\",\"" + l.getBlockZ() + "\",\"" 
					+ l.getWorld().getName() + "\")");
		} catch (SQLException ex) {
			Log.error("Unable to add new auction point.");
			return false;
		} finally {
			try {
				if (s != null)
					s.close();
				c.release();
			} catch (SQLException e) {
				Log.error("Unable to close connection.");
			}
		}
		return true;
	}
	
	public static boolean renameAuctionPoint(int id, String name) {
		TimedConnection c = null;
		Statement s = null;
		try {
			c = Database.getConnection();
			s = c.createStatement();

			if (name.trim().isEmpty())
				name = "Point " + id;

			s.execute("UPDATE ma_auction_points SET name=\"" + name + "\" WHERE id=\"" + id + "\"");
		} catch (SQLException ex) {
			Log.error("Unable to rename auction point.");
			return false;
		} finally {
			try {
				if (s != null)
					s.close();
				c.release();
			} catch (SQLException e) {
				Log.error("Unable to close connection.");
			}
		}
		return true;
	}

}
