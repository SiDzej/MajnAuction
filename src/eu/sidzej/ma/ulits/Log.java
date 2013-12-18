package eu.sidzej.ma.ulits;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

	static final Logger log = Logger.getLogger("Minecraft");
	private static String log_prefix = "[MajnAuction]";

	public static void logInfo(String message) {
		log.log(Level.INFO, String.format("%s %s", log_prefix, message));
	}

	public static void logError(String message) {
		log.log(Level.SEVERE, String.format("%s %s", log_prefix, message));
	}

	public static void logDebug(String message) {
		if (true) { // TODO config
			log.log(Level.INFO, String.format("%s [DEBUG] %s", log_prefix, message));
		}
	}

}
