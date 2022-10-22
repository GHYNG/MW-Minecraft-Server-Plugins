package org.mwage.mcPlugin.note.notes.plugin;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.mwage.mcPlugin.note.Main;
import org.mwage.mcPlugin.note.notes.NoteProcessor;
public class NotePluginProcessor extends NoteProcessor<Plugin, NoteBookPlugin, NoteBookPluginSystem> {
	public NotePluginProcessor(Main plugin) {
		super(plugin, new NoteBookPluginSystem(plugin));
	}
	@Override
	public String getCommand() {
		return "note-plugin";
	}
	@Override
	protected ChatColor getMessageTitleColor() {
		return ChatColor.DARK_PURPLE;
	}
	@Override
	protected String getMessageTitle() {
		return "Note Plugin";
	}
}