package org.mwage.mcPlugin.note.player;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.mwage.mcPlugin.note.standard.NoteBook;
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
			title = "Note about player: " + author; // this might be the reason some books cannot be opened
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