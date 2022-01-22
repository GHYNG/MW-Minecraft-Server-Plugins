package org.mwage.mcPlugin.note;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
import org.mwage.mcPlugin.note.player.NotePlayerProcessor;
public class Main extends MWPlugin {
	protected CommandProcessorManager commandProcessorManager;
	protected NotePlayerProcessor notePlayerProcessor;
	@Override
	public void onEnable() {
		commandProcessorManager = new CommandProcessorManager(this);
		notePlayerProcessor = new NotePlayerProcessor(this);
		// registerListener(new TestListener());
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
class TestListener implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerEditBook(PlayerEditBookEvent event) {
		BookMeta bm = event.getNewBookMeta();
		Bukkit.broadcastMessage("Title: " + bm.getTitle());
		Bukkit.broadcastMessage("Author: " + bm.getAuthor());
		Bukkit.broadcastMessage("Display Name: " + bm.getDisplayName());
	}
}