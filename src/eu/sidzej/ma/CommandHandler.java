package eu.sidzej.ma;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;

import eu.sidzej.ma.commands.CommandInterface;
import eu.sidzej.ma.commands.Password;
import eu.sidzej.ma.ulits.Log;

public class CommandHandler implements CommandExecutor{
	
	public HashMap<String, CommandInterface> commands = new HashMap<String, CommandInterface>();
    public ArrayList<String> sortedCommands = new ArrayList<String>();
    private final MajnAuction plugin;

    /**
     *
     * @param plugin
     */
    public CommandHandler(MajnAuction plugin) {

        this.plugin = plugin;

        commands.put("password", new Password(plugin));
        commands.put("pass", new Password(plugin));
        

        for (String s : commands.keySet()) {
            sortedCommands.add(s);
        }
        Collections.sort(sortedCommands, Collator.getInstance());
        Log.debug("Commands enabled: " + Joiner.on(", ").join(sortedCommands));
    }

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length >= 1) {
            String subCmd = args[0].toLowerCase();
            if (commands.containsKey(subCmd)) {
            	if (subCmd.equalsIgnoreCase("pass"))
            		subCmd = "password";
                if (!sender.hasPermission("ma." + subCmd)) {
                    sender.sendMessage("You have no permission to do that.");
                    return true;
                }
                commands.get(subCmd).dispatch(sender, args);
                return true;
            }
        }
        commands.get("help").dispatch(sender, args);
        return true;
	}
}
