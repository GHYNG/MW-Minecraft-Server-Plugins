package org.mwage.mcPlugin.note.player;
import java.io.File;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.mwage.mcPlugin.note.Main;
import org.mwage.mcPlugin.note.standard.NoteBookSystem;
public class NoteBookPlayerSystem extends NoteBookSystem<UUID, NoteBookPlayer> {
	public NoteBookPlayerSystem(Main plugin) {
		super(plugin);
	}
	public NoteBookPlayer readyNoteBook(OfflinePlayer target, boolean forceCreate) {
		return readyNoteBook(target.getUniqueId(), forceCreate);
	}
	@Override
	public NoteBookPlayer createNewNoteBook(UUID identifier) {
		return new NoteBookPlayer(identifier);
	}
	@Override
	protected File getFolder() {
		File pluginFolder = plugin.getDataFolder();
		File notesFolder = new File(pluginFolder, "notes");
		File managerFolder = new File(notesFolder, "manager");
		File playerFolder = new File(managerFolder, "players");
		return playerFolder;
	}
	@Override
	protected UUID getIdentifierFromString(String string) {
		return UUID.fromString(string);
	}
	@Override
	protected String getStringFromIdentifier(UUID identifier) {
		return identifier.toString();
	}
	@Override
	public String getFileExtendName() {
		return "nb";
	}
}