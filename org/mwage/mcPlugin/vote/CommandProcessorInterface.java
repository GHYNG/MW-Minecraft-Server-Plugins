package org.mwage.mcPlugin.vote;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
public interface CommandProcessorInterface {
	boolean onCommand(CommandSender sender, Command command, String label, String[] args);
	String getCommand();
}