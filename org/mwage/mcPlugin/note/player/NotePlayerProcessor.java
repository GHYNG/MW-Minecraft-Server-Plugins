package org.mwage.mcPlugin.note.player;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
					NoteBookPlayer noteBookPlayer = noteBookPlayerSystem.getNoteBook(targetPlayer, true);
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
							noteBookPlayer.playerReadNotes(senderPlayer, startPage);
							senderPlayer.sendMessage("to read note: " + noteBookPlayer.getTitle());
							return true;
						case WRITE :
							PlayerInventory inventory = senderPlayer.getInventory();
							if(inventory.getItemInMainHand().getType() != Material.AIR) {
								senderPlayer.sendMessage("Exception: You must have an empty main hand to write a new note!");
								return true;
							}
							noteBookPlayerSystem.playerStartEditing(senderPlayer, targetPlayer.getUniqueId());
							noteBookPlayer.playerWriteNotes(senderPlayer);
							senderPlayer.sendMessage("to write note: " + noteBookPlayer.getTitle());
							return true;
						case REMOVE :
							noteBookPlayer.playerRemoveNotes(senderPlayer);
							senderPlayer.sendMessage("to remove note: " + noteBookPlayer.getTitle());
							return true;
						case RECOVER :
							noteBookPlayer.playerRecoverNotes(senderPlayer);
							senderPlayer.sendMessage("to recover note: " + noteBookPlayer.getTitle());
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
	protected void playerReadPlayerNoteBook(Player reader, Player target) {}
}