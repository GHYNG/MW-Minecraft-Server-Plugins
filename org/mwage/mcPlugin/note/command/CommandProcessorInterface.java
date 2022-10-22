package org.mwage.mcPlugin.note.command;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
public interface CommandProcessorInterface {
	String getCommand();
	boolean onCommand(CommandSender sender, Command command, String label, String[] args);
	List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args);
}