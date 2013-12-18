package eu.sidzej.ma.db;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class TimedConnection implements Connection {
	private long time = 0;
	private boolean inuse = false;

	long getLastUse() {
		return time;
	}

	boolean inUse() {
		return inuse;
	}

	boolean isValid() {
		try {
			return this.isValid(1);
		} catch (final SQLException ex) {
			return false;
		}
	}

	synchronized boolean lease() {
		if (inuse)
			return false;
		inuse = true;
		time = System.currentTimeMillis();
		return true;
	}
	
	void release (){
		inuse = false;
        try {
                if (!this.getAutoCommit())
                        this.setAutoCommit(true);
        } catch (final SQLException ex) {
                try {
					close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
                ConnectionManager.removeConn(this);
        }
	}
}