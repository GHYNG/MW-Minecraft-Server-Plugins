package org.mwage.mcPlugin.note.command;
import org.mwage.mcPlugin.note.Main;
public abstract class AbstractCommandProcessor implements CommandProcessorInterface {
	public final Main plugin;
	public AbstractCommandProcessor(Main plugin) {
		this.plugin = plugin;
		plugin.registerCommandProcessor(this);
	}
}