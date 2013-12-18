package eu.sidzej.ma.db;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import eu.sidzej.ma.ulits.Log;

/**
 * @author _CJ_
 * 
 *         Manage connection pool over database
 * 
 */
public class ConnectionManager implements Closeable {

	private static int poolsize = 10;
	private static long timeToLive = 300000;
	private static Vector<TimedConnection> connections;
	private final ConnectionKiller reaper; // closing dead connections

	private final String user;
	private final String database;
	private final String password;
	private final String port;
	private final String hostname;

	/**
	 * Self-explaining throws exception when DB can't be used.
	 * 
	 * @throws ClassNotFoundException
	 */
	public ConnectionManager(String hostname, String port, String database, String username,
			String password) throws ClassNotFoundException {
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.user = username;
		this.password = password;

		Class.forName("com.mysql.jdbc.Driver");
		Log.info("Attempting to connecting to database at: " + hostname);
		poolsize = 10; // TODO config
		connections = new Vector<TimedConnection>(poolsize);
		reaper = new ConnectionKiller();
		reaper.start();
	}

	/**
	 * Returns a connection from the pool
	 * 
	 * @return returns a TimedConnection
	 * @throws SQLException
	 */
	public synchronized TimedConnection getConnection() throws SQLException {
		TimedConnection conn;
		Iterator<TimedConnection> it = connections.iterator();
		while (it.hasNext()) {
			conn = it.next();
			if (conn.lease()) {
				if (conn.isValid())
					return conn;
				Log.debug("Removing dead MySQL connection");
				connections.remove(conn);
				conn.close();
			}
		}
		Log.debug("No available MySQL connections, attempting to create new one");
		conn = (TimedConnection) DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":"
				+ this.port + "/" + this.database, this.user, this.password);
		conn.lease();
		if (!conn.isValid()) {
			conn.close();
		}
		connections.add(conn);
		return conn;
	}

	// Could not create new connection
	/**
	 * @see java.io.Closeable#close()
	 * 
	 *      close all opened connections in all threads
	 */
	@Override
	public synchronized void close() {
		Log.debug("Closing all MySQL connections");
		final Enumeration<TimedConnection> conns = connections.elements();
		while (conns.hasMoreElements()) {
			final Connection conn = conns.nextElement();
			connections.remove(conn);
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * remove connection from pool which is already closed
	 */
	public static void removeConn(TimedConnection c) {
		connections.remove(c);
	}

	/**
	 * Kill all broken and stucked connection called repeately after some time
	 */
	private void KillConnections() {
		Log.debug("Killing bad connections");
		final long stale = System.currentTimeMillis() - timeToLive;
		final Enumeration<TimedConnection> conns = connections.elements();
		int i = 1;
		int count = 0;
		while (conns.hasMoreElements()) {
			final TimedConnection conn = conns.nextElement();

			if (conn.inUse() && stale > conn.getLastUse() && !conn.isValid()) {
				connections.remove(conn);
				count++;
			}

			if (i > poolsize) {
				connections.remove(conn);
				try {
					conn.close();
				} catch (SQLException e) {
					Log.error("Error while killing connection!");
					e.printStackTrace();
				}
				count++;
			}
			i++;
		}
		Log.debug("Connection terminated count: " + count);
	}

	/**
	 * Connection timed-killer repeatly calls KillConnections to clear pool
	 */
	private class ConnectionKiller extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(300000);
				} catch (final InterruptedException e) {
					Log.error("Error in connectionKiller sleeping thread");
				}
				KillConnections();
			}
		}

	}
}
