package org.mwage.mcPlugin.note.notes.player;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.mwage.mcPlugin.note.Main;
import org.mwage.mcPlugin.note.notes.NoteProcessor;
public class NotePlayerProcessor extends NoteProcessor<UUID, NoteBookPlayer, NoteBookPlayerSystem> {
	public NotePlayerProcessor(Main plugin) {
		super(plugin, new NoteBookPlayerSystem(plugin));
	}
	@Override
	public String getCommand() {
		return "note-player";
	}
	@Override
	protected ChatColor getMessageTitleColor() {
		return ChatColor.YELLOW;
	}
	@Override
	protected String getMessageTitle() {
		return "Note Player";
	}
}