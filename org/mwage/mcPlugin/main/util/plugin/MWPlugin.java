package org.mwage.mcPlugin.main.util.plugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
/*
 * Just to add a few shortcut methods.
 */
public abstract class MWPlugin extends JavaPlugin {
	public void registerListener(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, this);
	}
}