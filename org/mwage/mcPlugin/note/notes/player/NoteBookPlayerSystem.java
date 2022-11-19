package org.mwage.mcPlugin.note.notes.player;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.mwage.mcPlugin.note.Main;
import org.mwage.mcPlugin.note.notes.NoteBookSystem;
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
	public File getFolder() {
		File pluginFolder = plugin.getDataFolder();
		File notesFolder = new File(pluginFolder, "notes");
		File managerFolder = new File(notesFolder, "manager");
		File playerFolder = new File(managerFolder, "players");
		return playerFolder;
	}
	@Override
	public UUID getIdentifierFromFileString(String string) {
		return UUID.fromString(string);
	}
	@Override
	public String getFileStringFromIdentifier(UUID identifier) {
		return identifier.toString();
	}
	@Override
	public String getFileExtendName() {
		return "nb";
	}
	@SuppressWarnings("deprecation")
	@Override
	public UUID getIdentifierFromCommandString(String string) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(string);
		return player.getUniqueId();
	}
	@Override
	public String getCommandStringFromIdentifier(UUID identifier) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(identifier);
		return player.getName();
	}
	@Override
	public String getItemTypeName() {
		return "player-username";
	}
	@Override
	public Set<UUID> getPossibleNoteTargets() {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		Set<UUID> results = new HashSet<UUID>();
		for(Player player : players) {
			results.add(player.getUniqueId());
		}
		return results;
	}
}