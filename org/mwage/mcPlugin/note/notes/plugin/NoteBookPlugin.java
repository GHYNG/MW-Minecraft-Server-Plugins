package org.mwage.mcPlugin.note.notes.plugin;
import java.util.List;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.mwage.mcPlugin.note.notes.NoteBook;
public class NoteBookPlugin extends NoteBook<Plugin> {
	private final String title;
	private final String author;
	private final PluginDescriptionFile pluginDescription;
	public NoteBookPlugin(Plugin identifier) {
		super(identifier);
		title = "About Plugin " + identifier.getName();
		pluginDescription = identifier.getDescription();
		List<String> authors = pluginDescription.getAuthors();
		author = authors == null ? "Server" : authors.size() == 0 ? "Server" : authors.get(0);
	}
	@Override
	public String getTitle() {
		return title;
	}
	@Override
	public String getAuthor() {
		return author;
	}
}