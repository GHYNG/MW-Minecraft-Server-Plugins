package org.mwage.mcPlugin.note;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
import org.mwage.mcPlugin.note.command.CommandProcessorInterface;
import org.mwage.mcPlugin.note.notes.player.NotePlayerProcessor;
import org.mwage.mcPlugin.note.notes.plugin.NotePluginProcessor;
public class Main extends MWPlugin {
	protected CommandProcessorManager commandProcessorManager;
	protected NotePlayerProcessor notePlayerProcessor;
	protected NotePluginProcessor notePluginProcessor;
	@Override
	public void onEnable() {
		commandProcessorManager = new CommandProcessorManager(this);
		notePlayerProcessor = new NotePlayerProcessor(this);
		notePluginProcessor = new NotePluginProcessor(this);
	}
	@Override
	public void onDisable() {
		notePlayerProcessor.onDisable();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandProcessorInterface processor = commandProcessorManager.processors.get(command.getName());
		if(processor == null) {
			return false;
		}
		return processor.onCommand(sender, command, label, args);
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		CommandProcessorInterface processor = commandProcessorManager.processors.get(command.getName());
		if(processor == null) {
			return null;
		}
		return processor.onTabComplete(sender, command, label, args);
	}
	public void registerCommandProcessor(CommandProcessorInterface method) {
		commandProcessorManager.processors.put(method.getCommand(), method);
	}
}
class CommandProcessorManager {
	public final Main plugin;
	protected Map<String, CommandProcessorInterface> processors = new HashMap<String, CommandProcessorInterface>();
	CommandProcessorManager(Main plugin) {
		this.plugin = plugin;
	}
}