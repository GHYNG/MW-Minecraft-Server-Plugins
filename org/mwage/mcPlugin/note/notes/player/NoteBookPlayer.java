package org.mwage.mcPlugin.note.notes.player;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.mwage.mcPlugin.note.notes.NoteBook;
public class NoteBookPlayer extends NoteBook<UUID> {
	public final String author;
	public final String title;
	public final OfflinePlayer targetPlayer;
	public NoteBookPlayer(UUID identifier) {
		super(identifier);
		targetPlayer = Bukkit.getOfflinePlayer(identifier);
		String author = "null", title = "null";
		if(targetPlayer != null) {
			author = targetPlayer.getName();
			title = "About Player: " + author; // author is NOT the name of writer of the note.
		}
		this.author = author;
		this.title = title;
	}
	@Override
	public String getAuthor() {
		return author;
	}
	@Override
	public String getTitle() {
		return title;
	}
}