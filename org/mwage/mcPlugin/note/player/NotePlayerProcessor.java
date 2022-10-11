package org.mwage.mcPlugin.note.player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mwage.mcPlugin.note.AbstractCommandProcessor;
import org.mwage.mcPlugin.note.Main;
public class NotePlayerProcessor extends AbstractCommandProcessor {
	enum NoteProcessMode {
		READ,
		WRITE,
		REMOVE,
		RECOVER
	}
	protected final NoteBookPlayerSystem noteBookPlayerSystem;
	public NotePlayerProcessor(Main plugin) {
		super(plugin);
		plugin.registerCommandProcessor(this);
		noteBookPlayerSystem = new NoteBookPlayerSystem(plugin);
	}
	public void onDisable() {
		noteBookPlayerSystem.onDisable();
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player senderPlayer) {
			int argLength = args.length;
			if(argLength == 0) {
				String message = """
				Help for /note-player
				  read <player-id>
				    Read notes about a player.
				  write <player-id>
				    Write a new note about a player.
				  remove
				    Remove your previous one (if there is one) note of a player.
				""";
				senderPlayer.sendMessage(message);
				return true;
			}
			else {
				String arg0 = args[0];
				NoteProcessMode type = null;
				{
					if(arg0.equals("read")) {
						type = NoteProcessMode.READ;
					}
					else if(arg0.equals("write")) {
						type = NoteProcessMode.WRITE;
					}
					else if(arg0.equals("remove")) {
						type = NoteProcessMode.REMOVE;
					}
					else if(arg0.equals("recover")) {
						type = NoteProcessMode.RECOVER;
					}
					if(type == null) {
						String message = """
						Wrong command!
						You must pick from /note-player {read/write/remove/recover} <player-id>
						""";
						senderPlayer.sendMessage(message);
						return false;
					}
				}
				if(argLength == 1) {
					String message = "";
					switch(type) {
						case READ :
							message = """
							The command for reading notes of a player is:
							  /note-player read <player-id> <optional start-page-number>
							""";
							senderPlayer.sendMessage(message);
							return true;
						case WRITE :
							message = """
							The command for writing a new note of a player is:
							  /note-player write <player-id>
							""";
							senderPlayer.sendMessage(message);
							return true;
						case REMOVE :
							message = """
							The command for removing your last note of a player is:
							  /note-player remove <player-id>
							""";
							senderPlayer.sendMessage(message);
							return true;
						case RECOVER :
							message = """
							The command for recovering the last note you removed of a player is:
							  /note-player recover <player-id>
							""";
							senderPlayer.sendMessage(message);
							return true;
					}
					return true;
				}
				else {
					String arg1 = args[1];
					OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(arg1);
					if(targetPlayer == null) {
						String message = "Exception: target player not found.";
						senderPlayer.sendMessage(message);
						return true;
					}
					NoteBookPlayer noteBookPlayer = noteBookPlayerSystem.readyNoteBook(targetPlayer, true);
					switch(type) {
						case READ :
							int startPage = 1;
							if(argLength > 2) {
								String arg2 = args[2];
								try {
									startPage = Integer.parseInt(arg2);
								}
								catch(Exception e) {}
							}
							int totalPages = noteBookPlayer.getPages().size();
							if(startPage > totalPages) {
								startPage = totalPages;
							}
							if(startPage < 1) {
								startPage = 1;
							}
							senderPlayer.openBook(noteBookPlayer.getWrittenBookItem(startPage));
							senderPlayer.sendMessage("Executed: to read note: " + noteBookPlayer.getTitle() + " pages (" + startPage + "/" + totalPages + ")");
							playerCommandSuccessful(senderPlayer);
							return true;
						case WRITE :
							PlayerInventory inventory = senderPlayer.getInventory();
							if(inventory.getItemInMainHand().getType() != Material.AIR) {
								senderPlayer.sendMessage("Exception: You must have an empty main hand to write a new note!");
								return true;
							}
							noteBookPlayerSystem.playerStartWriting(senderPlayer, targetPlayer.getUniqueId());
							ItemStack bookItem = noteBookPlayer.getWritableBookItem(senderPlayer);
							inventory.setItemInMainHand(bookItem);
							senderPlayer.sendMessage("Executed: to write note: " + noteBookPlayer.getTitle());
							playerCommandSuccessful(senderPlayer);
							return true;
						case REMOVE :
							senderPlayer.sendMessage("Executed: to remove note: " + noteBookPlayer.getTitle());
							if(noteBookPlayer.removeNote(senderPlayer)) {
								senderPlayer.sendMessage("Executed: 1 of your note removed.");
							}
							else {
								senderPlayer.sendMessage("Executed: you did not have notes in this note book.");
							}
							playerCommandSuccessful(senderPlayer);
							return true;
						case RECOVER :
							senderPlayer.sendMessage("Executed: to recover note: " + noteBookPlayer.getTitle());
							if(noteBookPlayer.recoverNote(senderPlayer)) {
								senderPlayer.sendMessage("Executed: 1 removed note recovered.");
							}
							else {
								senderPlayer.sendMessage("Executed: you did not have notes to recover.");
							}
							playerCommandSuccessful(senderPlayer);
							return true;
					}
				}
			}
		}
		else {
			sender.sendMessage("Exception: Only players are allowed running this command!");
			return true;
		}
		sender.sendMessage("Error: Something went wrong running this command.");
		return true; // XXX TEMP EXIT
	}
	@Override
	public String getCommand() {
		return "note-player";
	}
	private void playerCommandSuccessful(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.8f, 1.2f);;
	}
}