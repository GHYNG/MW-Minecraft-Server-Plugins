package org.mwage.mcPlugin.note.notes.plugin;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.mwage.mcPlugin.note.Main;
import org.mwage.mcPlugin.note.notes.NoteBookSystem;
public class NoteBookPluginSystem extends NoteBookSystem<Plugin, NoteBookPlugin> {
	public NoteBookPluginSystem(Main plugin) {
		super(plugin);
	}
	@Override
	protected NoteBookPlugin createNewNoteBook(Plugin identifier) {
		return new NoteBookPlugin(identifier);
	}
	@Override
	public File getFolder() {
		File pluginFolder = plugin.getDataFolder();
		File notesFolder = new File(pluginFolder, "notes");
		File managerFolder = new File(notesFolder, "manager");
		File playerFolder = new File(managerFolder, "plugins");
		return playerFolder;
	}
	@Override
	public String getFileExtendName() {
		return "nb";
	}
	@Override
	public Plugin getIdentifierFromFileString(String string) {
		PluginManager pluginManager = Bukkit.getPluginManager();
		Plugin[] plugins = pluginManager.getPlugins();
		for(Plugin plugin : plugins) {
			if(plugin.getName().equals(string)) {
				return plugin;
			}
		}
		return null;
	}
	@Override
	public String getFileStringFromIdentifier(Plugin identifier) {
		return identifier.getName();
	}
	@Override
	public Plugin getIdentifierFromCommandString(String string) {
		return getIdentifierFromFileString(string);
	}
	@Override
	public String getCommandStringFromIdentifier(Plugin identifier) {
		return getFileStringFromIdentifier(identifier);
	}
	@Override
	public String getItemTypeName() {
		return "plugin-name";
	}
	@Override
	public Set<Plugin> getPossibleNoteTargets() {
		Set<Plugin> results = new HashSet<Plugin>();
		PluginManager pluginManager = Bukkit.getPluginManager();
		Plugin[] plugins = pluginManager.getPlugins();
		for(Plugin plugin : plugins) {
			results.add(plugin);
		}
		return results;
	}
}