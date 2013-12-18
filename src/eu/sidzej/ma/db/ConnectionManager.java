package eu.sidzej.ma.db;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

import eu.sidzej.ma.MajnAuction;

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

	private MajnAuction plugin;

	public ConnectionManager(MajnAuction plugin, String hostname, String port, String database,
			String username, String password) throws ClassNotFoundException {
		this.plugin = plugin;
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.user = username;
		this.password = password;

		Class.forName("com.mysql.jdbc.Driver");
		plugin.logInfo("Attempting to connecting to database at: " + hostname);
		poolsize = 10; // TODO config
		connections = new Vector<TimedConnection>(poolsize);
		reaper = new ConnectionKiller();
		reaper.start();
	}

	@Override
	public synchronized void close() {
		plugin.logDebug("Closing all MySQL connections");
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

	private void KillConnections() {
		final long stale = System.currentTimeMillis() - timeToLive;
        final Enumeration<TimedConnection> conns = connections.elements();
        int i = 1;
        while (conns.hasMoreElements()) {
                final TimedConnection conn = conns.nextElement();

                if (conn.inUse() && stale > conn.getLastUse() && !conn.isValid()) {
                        connections.remove(conn);
                }

                if (i > poolsize) {
                        connections.remove(conn);
                        try {
							conn.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                }
                i++;
        }		
	}
	
	public static void removeConn(TimedConnection c) {
		connections.remove(c);
		
	}

	/**
	 * Connection timed-killer
	 */
	private class ConnectionKiller extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(300000);
				} catch (final InterruptedException e) {
				}
				KillConnections();
			}
		}

		
	}
}
