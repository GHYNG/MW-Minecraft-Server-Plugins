package org.mwage.mcPlugin.note;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
import org.mwage.mcPlugin.note.player.NotePlayerProcessor;
public class Main extends MWPlugin {
	protected CommandProcessorManager commandProcessorManager;
	protected NotePlayerProcessor notePlayerProcessor;
	@Override
	public void onEnable() {
		commandProcessorManager = new CommandProcessorManager(this);
		notePlayerProcessor = new NotePlayerProcessor(this);
	}
	@Override
	public void onDisable() {
		notePlayerProcessor.onDisable();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandProcessorInterface method = commandProcessorManager.methods.get(command.getName());
		if(method == null) {
			return false;
		}
		return method.onCommand(sender, command, label, args);
	}
	public void registerCommandProcessor(CommandProcessorInterface method) {
		commandProcessorManager.methods.put(method.getCommand(), method);
	}
}
class CommandProcessorManager {
	public final Main plugin;
	protected Map<String, CommandProcessorInterface> methods = new HashMap<String, CommandProcessorInterface>();
	CommandProcessorManager(Main plugin) {
		this.plugin = plugin;
	}
}