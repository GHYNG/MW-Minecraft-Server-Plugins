package org.mwage.mcPlugin.note.player;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.mwage.mcPlugin.note.Main;
import org.mwage.mcPlugin.note.NoteBookSystem;
public class NoteBookPlayerSystem extends NoteBookSystem<UUID, NoteBookPlayer> {
	public NoteBookPlayerSystem(Main plugin) {
		super(plugin);
	}
	public NoteBookPlayer getNoteBook(OfflinePlayer target) {
		return getNoteBook(target, false);
	}
	public NoteBookPlayer getNoteBook(OfflinePlayer target, boolean forceCreate) {
		NoteBookPlayer noteBook = getNoteBook(target.getUniqueId());
		if(noteBook == null && forceCreate) {
			noteBook = new NoteBookPlayer(target.getUniqueId());
		}
		return noteBook;
	}
	@Override
	public UUID getIdentifierFromString(String str) {
		return UUID.fromString(str);
	}
	@Override
	public NoteBookPlayer createNewNoteBook(UUID identifier) {
		return new NoteBookPlayer(identifier);
	}
}