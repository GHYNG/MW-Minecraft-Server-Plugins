package org.mwage.mcPlugin.note;
public abstract class AbstractCommandProcessor implements CommandProcessorInterface {
	public final Main plugin;
	public AbstractCommandProcessor(Main plugin) {
		this.plugin = plugin;
		plugin.registerCommandProcessor(this);
	}
}