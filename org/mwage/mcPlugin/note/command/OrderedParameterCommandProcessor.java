package org.mwage.mcPlugin.note.command;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mwage.mcPlugin.note.Main;
public abstract class OrderedParameterCommandProcessor extends AbstractCommandProcessor {
	public OrderedParameterCommandProcessor(Main plugin) {
		super(plugin);
	}
	public abstract OrderedParameterCommand getOrderedParameterCommand();
	public abstract boolean isSenderLegal(CommandSender sender);
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		getOrderedParameterCommand().update(sender, command.getName(), args);
		if(isSenderLegal(sender)) {
			boolean result = getOrderedParameterCommand().run(sender, command.getName(), args);
			if(result) {
				return true;
			}
			String echoHelp = getOrderedParameterCommand().getEchoHelp(sender, command.getName(), args);
			if(echoHelp == null || echoHelp.equals("")) {
				return false;
			}
			sender.sendMessage(getMessageTitleColor().toString() + "[" + getMessageTitle() + "]:");
			sender.sendMessage(echoHelp);
			return false;
		}
		else {
			return false;
		}
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		getOrderedParameterCommand().update(sender, command.getName(), args);
		return getOrderedParameterCommand().getTabComplete(sender, command.getName(), args);
	}
	protected abstract ChatColor getMessageTitleColor();
	protected abstract String getMessageTitle();
}