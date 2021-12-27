package org.mwage.mcPlugin.vote;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
public interface CommandProcessMethod {
	boolean onCommand(Main plugin, CommandSender sender, Command command, String label, String[] args);
}