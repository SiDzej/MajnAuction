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
import eu.sidzej.ma.ulits.Config;
import eu.sidzej.ma.ulits.Log;

public class Database {

	private MajnAuction plugin;
	private static ConnectionManager cm;

	private List<String> table_names = Arrays.asList("ma_players", "ma_data_values",
			"ma_auction_points");

	public Database(MajnAuction plugin) throws ClassNotFoundException {
		this.plugin = plugin;

		cm = new ConnectionManager(Config.host, Config.port, Config.database, Config.user,
				Config.pass);
		
		Iterator<String> iterator = table_names.iterator();
		while (iterator.hasNext())
			if (!tableExists(iterator.next())) {
				createMajnAuctionDB();
				break;
			}
	}

	public boolean saveAuctionPoint(Location l, String name, int id) {
		TimedConnection c = null;
		Statement s = null;
		try {
			c = cm.getConnection();
			s = c.createStatement();

			if (name.trim().isEmpty())
				name = "Point " + (id + 1);

			s.executeQuery("INSERT INTO ma_auction_points (name,x,y,z) VALUES ('" + name + "','"
					+ l.getBlockX() + "'," + l.getBlockY() + "'," + l.getBlockZ() + "')");
		} catch (SQLException ex) {
			Log.error("Unable to add new auction point.");
			return false;
		} finally {
			try {
				if (s != null)
					s.close();
				c.close();
			} catch (SQLException e) {
				Log.error("Unable to close connection.");
			}

		}
		return true;
	}

	public boolean setPassword(String p, String pass) {
		TimedConnection c = null;
		Statement s = null;
		String password = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(pass.getBytes("UTF-8"));
			byte[] digest = md.digest();
			password = DatatypeConverter.printHexBinary(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			c = cm.getConnection();
			s = c.createStatement();

			ResultSet tmp = s.executeQuery("SELECT id FROM ma_players WHERE nick=\"" + p + "\"");
			if (tmp.next()) {
				s.executeQuery("UPDATE ma_players SET password=\"" + password + "\" WHERE nick=\""
						+ p + "\"");
				Log.debug("Changing " + p + " password");
			} else {
				s.executeQuery("INSERT INTO ma_players (nick,password) VALUES (\"" + p + "\",\""
						+ password + "\")");
				Log.debug("Creating " + p + " account");
			}
		} catch (SQLException e) {
			Log.error("Unable to set password.");
			return false;
		} finally {
			try {
				if (s != null)
					s.close();
				c.close();
			} catch (SQLException e) {
				Log.error("Unable to close connection.");
			}
		}

		return true;
	}
	
	public void close() {
		cm.close();
	}
	
	/// Private part

	private boolean createMajnAuctionDB() {
		Log.info("Creating MajnAuctionDB structure...");
		TimedConnection c = null;
		Statement s = null;
		try {
			c = cm.getConnection();
			s = c.createStatement();
			if (plugin.getResource("CREATE.sql") == null) {
				plugin.disable("No CREATE.sql file found! Critical error.");
				return false;
			}
			String[] queries = CharStreams.toString(
					new InputStreamReader(plugin.getResource("CREATE.sql"))).split(";");
			for (String query : queries) {
				Log.info("Creating DB table");
				s.execute(query);
			}
		} catch (SQLException | IOException e) {
			Log.error(e.getMessage());
			plugin.disable("Can't create database.");
			return false;
		} finally {
			try {
				if (s != null)
					s.close();
				c.close();
			} catch (SQLException e) {
				Log.error("Unable to close connection.");
			}
		}
		return true;
	}

	
	private boolean tableExists(String table) {
		TimedConnection c = null;
		Statement s = null;
		try {
			c = cm.getConnection();
			s = c.createStatement();
			
			ResultSet res = c.getMetaData().getTables(null, null, table, null);
			if (res.next())
				return true;
			else 
				return false;
		} catch (SQLException e) {
			Log.error(e.getMessage());
			plugin.disable("Can't reach database.");
			return false;
		} finally {
			try {
				if (s != null)
					s.close();
				c.close();
			} catch (SQLException e) {
				Log.error("Unable to close connection.");
			}
		}
		
	}
}
