package org.mwage.mcPlugin.note.notes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mwage.mcPlugin.note.Main;
import org.mwage.mcPlugin.note.command.OrderedParameterCommand;
import org.mwage.mcPlugin.note.command.OrderedParameterCommandProcessor;
public abstract class NoteProcessor<I, NB extends NoteBook<I>, NS extends NoteBookSystem<I, NB>> extends OrderedParameterCommandProcessor {
	private final NS noteBookSystem;
	private final OPCNote<I, NB, NS> rootCommand;
	public NoteProcessor(Main plugin, NS noteBookSystem) {
		super(plugin);
		this.noteBookSystem = noteBookSystem;
		this.rootCommand = new OPCNote<I, NB, NS>(getCommand(), this.noteBookSystem);
	}
	public void onDisable() {
		noteBookSystem.onDisable();
	}
	@Override
	public boolean isSenderLegal(CommandSender sender) {
		return sender instanceof Player;
	}
	@Override
	public OrderedParameterCommand getOrderedParameterCommand() {
		return this.rootCommand;
	}
}
class OPCNote<I, NB extends NoteBook<I>, NS extends NoteBookSystem<I, NB>> extends OrderedParameterCommand {
	public final String lineSep = "\n"; // System.getProperty("line.separator");
	private final NS noteBookSystem;
	private final OPCRead branchRead;
	private final OPCWrite branchWrite;
	private final OPCRemove branchRemove;
	private final OPCRecover branchRecover;
	private final OPCReadAll branchReadAll;
	public OPCNote(String command, NS noteBookSystem) {
		super(null, command);
		this.noteBookSystem = noteBookSystem;
		branchRead = new OPCRead(this);
		branchWrite = new OPCWrite(this);
		branchRemove = new OPCRemove(this);
		branchRecover = new OPCRecover(this);
		branchReadAll = new OPCReadAll(this);
	}
	public String getItemTypeName() {
		return noteBookSystem.getItemTypeName();
	}
	@Override
	public boolean isCommandLegal(CommandSender sender, String command) {
		return this.command.equals(command);
	}
	@Override
	public boolean isComplete() {
		return false;
	}
	@Override
	public String getLocalLocalEchoHelp(CommandSender sender, String command) {
		return "This command is used to keep note of a given topic.";
	}
	@Override
	public boolean localPreRun(CommandSender sender, String command) {
		return true; // does nothing
	}
	@Override
	public boolean localPostRun(CommandSender sender, String command) {
		if(sender instanceof Player senderPlayer) {
			Location location = senderPlayer.getLocation();
			senderPlayer.playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.8f, 1.2f);;
		}
		return true; // does nothing
	}
	@Override
	public void localUpdate(CommandSender sender, String command) {
		READ : {
			Map<I, NB> noteBooks = noteBookSystem.getNoteBooks();
			if(noteBooks.isEmpty()) {
				deactivateSubCommand(branchRead);
				deactivateSubCommand(branchReadAll);
			}
			else {
				activateSubCommand(branchRead);
				activateSubCommand(branchReadAll);
			}
			break READ; // just to remove warnings
		}
		WRITE : {
			activateSubCommand(branchWrite);
			break WRITE;
		}
		REMOVE : {
			if(sender instanceof Player senderPlayer && noteBookSystem.canRemoveNoteOf(senderPlayer)) {
				activateSubCommand(branchRemove);
			}
			else {
				deactivateSubCommand(branchRemove);
			}
			break REMOVE;
		}
		RECOVER : {
			if(sender instanceof Player senderPlayer && noteBookSystem.canRecoverNoteOf(senderPlayer)) {
				activateSubCommand(branchRecover);
			}
			else {
				deactivateSubCommand(branchRecover);
			}
			break RECOVER;
		}
	}
	class OPCRead extends OrderedParameterCommand {
		private NB targetNoteBook;
		private int startPage = 1;
		private final OPCItem branchItem;
		public OPCRead(OrderedParameterCommand parent) {
			super(parent, "read");
			branchItem = new OPCItem(this);
			activateSubCommand(branchItem);
		}
		@Override
		public boolean isCommandLegal(CommandSender sender, String command) {
			return this.command.equals(command);
		}
		@Override
		public boolean isComplete() {
			return false;
		}
		@Override
		public String getLocalLocalEchoHelp(CommandSender sender, String command) {
			return "Read the notes of this topic.";
		}
		@Override
		public boolean localPreRun(CommandSender sender, String command) {
			return true; // does nothing
		}
		@Override
		public boolean localPostRun(CommandSender sender, String command) {
			if(sender instanceof Player senderPlayer) {
				if(targetNoteBook == null) {
					return false;
				}
				int totalPages = targetNoteBook.getPages().size();
				senderPlayer.openBook(targetNoteBook.getWrittenBookItem(startPage));
				senderPlayer.sendMessage("Executed: Reading notes: " + targetNoteBook.getTitle() + " pages (" + startPage + "/" + totalPages + ")");
				return true;
			}
			return false;
		}
		@Override
		public void localUpdate(CommandSender sender, String command) {
			targetNoteBook = null;
			startPage = 1;
		}
		class OPCItem extends OrderedParameterCommand {
			private int maxPage = 0;
			private final OPCStartPage branchStartPage;
			public OPCItem(OrderedParameterCommand parent) {
				super(parent, "<" + getItemTypeName() + ">");
				branchStartPage = new OPCStartPage(this);
				activateSubCommand(branchStartPage);
			}
			@Override
			public List<String> getLocalTabComplete(CommandSender sender, String command) {
				List<String> results = new ArrayList<String>();
				noteBookSystem.getCommandStrings().forEach(s -> {
					results.add(s);
				});
				return results;
			}
			@Override
			public boolean isCommandLegal(CommandSender sender, String command) {
				return getLocalTabComplete(sender, command).contains(command);
			}
			@Override
			public boolean isComplete() {
				return true;
			}
			@Override
			public String getLocalLocalEchoHelp(CommandSender sender, String command) {
				return "Read the notes of the named item. Use the actual item name instead.";
			}
			@Override
			public boolean localPreRun(CommandSender sender, String command) {
				return targetNoteBook != null;
			}
			@Override
			public boolean localPostRun(CommandSender sender, String command) {
				return true;
			}
			@Override
			public void localUpdate(CommandSender sender, String command) {
				I identifier = noteBookSystem.getIdentifierFromCommandString(command);
				if(identifier != null) {
					targetNoteBook = noteBookSystem.readyNoteBook(identifier, true);
				}
				if(targetNoteBook != null) {
					maxPage = targetNoteBook.getPages().size();
				}
				else {
					maxPage = 1;
				}
			} // does nothing
			class OPCStartPage extends OrderedParameterCommand {
				public OPCStartPage(OrderedParameterCommand parent) {
					super(parent, "<optional--starting-page-number>");
				}
				@Override
				public List<String> getLocalTabComplete(CommandSender sender, String command) {
					List<String> results = new ArrayList<String>();
					try {
						int startPage = Integer.parseInt(command);
						if(startPage < 1 && startPage > maxPage) {
							throw new Exception(); // goto catch section
						}
						results.add("" + startPage);
						int possibleStartPage = startPage;
						for(int i = 0; i <= 9; i++) {
							possibleStartPage = Integer.parseInt("" + startPage + i);
							if(possibleStartPage <= maxPage) {
								results.add("" + possibleStartPage);
							}
							else {
								break;
							}
						}
					}
					catch(Exception e) {
						results.add("1");
						results.add("" + maxPage);
					}
					return results;
				}
				@Override
				public boolean isCommandLegal(CommandSender sender, String command) {
					try {
						int startPage = Integer.parseInt(command);
						return startPage >= 1 && startPage <= maxPage;
					}
					catch(Exception e) {
						return false;
					}
				}
				@Override
				public boolean isComplete() {
					return true;
				}
				@Override
				public String getLocalLocalEchoHelp(CommandSender sender, String command) {
					String echo = "An integer between 1 and the max number of pages.";
					echo += lineSep + "Default: 1";
					if(maxPage > 1) {
						echo += lineSep + "Max: " + maxPage;
					}
					else {
						echo += lineSep + "Max: Total page number of the given item.";
					}
					return echo;
				}
				@Override
				public boolean localPreRun(CommandSender sender, String command) {
					if(targetNoteBook == null) {
						return false;
					}
					try {
						int startPage = Integer.parseInt(command);
						if(startPage > maxPage) {
							startPage = maxPage;
						}
						if(startPage < 1) {
							startPage = 1;
						}
						OPCRead.this.startPage = startPage;
						return true;
					}
					catch(Exception e) {
						return false;
					}
				}
				@Override
				public boolean localPostRun(CommandSender sender, String command) {
					return true; // does nothing
				}
				@Override
				public void localUpdate(CommandSender sender, String command) {} // does nothing
			}
		}
	}
	class OPCWrite extends OrderedParameterCommand {
		private I targetItem;
		private final OPCNotedItem branchNotedItem;
		private final OPCNewItem branchNewItem;
		public OPCWrite(OrderedParameterCommand parent) {
			super(parent, "write");
			branchNotedItem = new OPCNotedItem(this);
			branchNewItem = new OPCNewItem(this);
		}
		@Override
		public boolean isCommandLegal(CommandSender sender, String command) {
			return this.command.equals(command);
		}
		@Override
		public boolean isComplete() {
			return false;
		}
		@Override
		public String getLocalLocalEchoHelp(CommandSender sender, String command) {
			return "Write notes of this topic.";
		}
		@Override
		public boolean localPreRun(CommandSender sender, String command) {
			return true;
		}
		@Override
		public boolean localPostRun(CommandSender sender, String command) {
			if(sender instanceof Player senderPlayer) {
				PlayerInventory inventory = senderPlayer.getInventory();
				if(inventory.getItemInMainHand().getType() != Material.AIR) {
					senderPlayer.sendMessage("Exception: You must have an empty main hand to write a new note!");
					return true;
				}
				if(targetItem == null) {
					senderPlayer.sendMessage("Error: Target item not found.");
					return true;
				}
				NB noteBook = noteBookSystem.readyNoteBook(targetItem, true);
				if(noteBook == null) {
					senderPlayer.sendMessage("Error: Target item is not in the datum base. Maybe something went wrong with file system?");
				}
				noteBookSystem.playerStartWriting(senderPlayer, targetItem);
				ItemStack bookItem = noteBook.getWritableBookItem(senderPlayer);
				inventory.setItemInMainHand(bookItem);
				senderPlayer.sendMessage("Executed: Writing note: " + noteBook.getTitle());
				return true;
			}
			else return false;
		}
		@Override
		public void localUpdate(CommandSender sender, String command) {
			targetItem = null;
			if(noteBookSystem.getNoteBooks().size() == 0) {
				deactivateSubCommand(branchNotedItem);
			}
			else {
				activateSubCommand(branchNotedItem);
			}
			if(branchNewItem.getLocalTabComplete(sender, null).size() == 0) {
				deactivateSubCommand(branchNewItem);
			}
			else {
				activateSubCommand(branchNewItem);
			}
		}
		abstract class OPCItem extends OrderedParameterCommand {
			public OPCItem(OrderedParameterCommand parent, String command) {
				super(parent, command);
			}
			@Override
			public boolean isComplete() {
				return true;
			}
			@Override
			public boolean localPreRun(CommandSender sender, String command) {
				targetItem = noteBookSystem.getIdentifierFromCommandString(command);
				return targetItem != null;
			}
			@Override
			public boolean localPostRun(CommandSender sender, String command) {
				return true; // does nothing
			}
			@Override
			public void localUpdate(CommandSender sender, String command) {
				targetItem = noteBookSystem.getIdentifierFromCommandString(command);
			}
		}
		class OPCNotedItem extends OPCItem {
			public OPCNotedItem(OrderedParameterCommand parent) {
				super(parent, "<existing-note-" + getItemTypeName() + ">");
			}
			@Override
			public List<String> getLocalTabComplete(CommandSender sender, String command) {
				List<String> results = new ArrayList<String>();
				noteBookSystem.getCommandStrings().forEach(s -> {
					results.add(s);
				});
				return results;
			}
			@Override
			public boolean isCommandLegal(CommandSender sender, String command) {
				return getLocalTabComplete(sender, command).contains(command);
			}
			@Override
			public String getLocalLocalEchoHelp(CommandSender sender, String command) {
				return "Write a note for the named item.";
			}
		}
		class OPCNewItem extends OPCItem {
			public OPCNewItem(OrderedParameterCommand parent) {
				super(parent, "<new-note-" + getItemTypeName() + ">");
			}
			@Override
			public List<String> getLocalTabComplete(CommandSender sender, String command) {
				List<String> possibleStrings = noteBookSystem.getPossibleNoteTargetCommandStrings();
				List<String> existingStrings = noteBookSystem.getExistingNoteTargetCommandStrings();
				List<String> results = new ArrayList<String>();
				for(String possibleString : possibleStrings) {
					if(!existingStrings.contains(possibleString)) {
						results.add(possibleString);
					}
				}
				return results;
			}
			@Override
			public boolean isCommandLegal(CommandSender sender, String command) {
				return getLocalTabComplete(sender, command).contains(command);
			}
			@Override
			public String getLocalLocalEchoHelp(CommandSender sender, String command) {
				return "Write a note for the named item.";
			}
		}
	}
	class OPCRemove extends OrderedParameterCommand {
		private NB targetNoteBook;
		private final OPCItem branchItem;
		public OPCRemove(OrderedParameterCommand parent) {
			super(parent, "remove");
			branchItem = new OPCItem(this);
			activateSubCommand(branchItem);
		}
		@Override
		public boolean isCommandLegal(CommandSender sender, String command) {
			return this.command.equals(command);
		}
		@Override
		public boolean isComplete() {
			return false;
		}
		@Override
		public String getLocalLocalEchoHelp(CommandSender sender, String command) {
			return "Remove a note you previously wrote for an item.";
		}
		@Override
		public boolean localPreRun(CommandSender sender, String command) {
			return true; // does nothing
		}
		@Override
		public boolean localPostRun(CommandSender sender, String command) {
			if(sender instanceof Player senderPlayer && targetNoteBook != null) {
				boolean successful = targetNoteBook.removeNote(senderPlayer);
				if(successful) {
					sender.sendMessage("Executed: One note you previously wrote for: " + targetNoteBook.getTitle() + " is removed.");
				}
				else {
					sender.sendMessage("Error: Something went wrong while removing the note for: " + targetNoteBook.getTitle() + ".");
				}
			}
			else {
				sender.sendMessage("Error: Something went wrong while removing the note. Target is null?");
			}
			return true;
		}
		@Override
		public void localUpdate(CommandSender sender, String command) {
			targetNoteBook = null;
		}
		public class OPCItem extends OrderedParameterCommand {
			public OPCItem(OrderedParameterCommand parent) {
				super(parent, "<" + getItemTypeName() + ">");
			}
			@Override
			public List<String> getLocalTabComplete(CommandSender sender, String command) {
				List<String> results = new ArrayList<String>();
				Set<I> identifiers = noteBookSystem.getExistingNoteTargets();
				for(I identifier : identifiers) {
					NB noteBook = noteBookSystem.readyNoteBook(identifier, true);
					if(noteBook != null && sender instanceof Player senderPlayer && noteBook.canRemoveNoteOf(senderPlayer)) {
						String result = noteBookSystem.getCommandStringFromIdentifier(identifier);
						if(result != null) {
							results.add(result);
						}
					}
				}
				return results;
			}
			@Override
			public boolean isCommandLegal(CommandSender sender, String command) {
				return getLocalTabComplete(sender, command).contains(command);
			}
			@Override
			public boolean isComplete() {
				return true;
			}
			@Override
			public String getLocalLocalEchoHelp(CommandSender sender, String command) {
				return "Remove one note of this named item.";
			}
			@Override
			public boolean localPreRun(CommandSender sender, String command) {
				return targetNoteBook != null;
			}
			@Override
			public boolean localPostRun(CommandSender sender, String command) {
				return true;
			}
			@Override
			public void localUpdate(CommandSender sender, String command) {
				I identifier = noteBookSystem.getIdentifierFromCommandString(command);
				if(identifier != null) {
					targetNoteBook = noteBookSystem.readyNoteBook(identifier, true);
				}
			}
		}
	}
	class OPCRecover extends OrderedParameterCommand {
		private NB targetNoteBook = null;
		private OPCItem branchItem;
		public OPCRecover(OrderedParameterCommand parent) {
			super(parent, "recover");
			branchItem = new OPCItem(this);
			activateSubCommand(branchItem);
		}
		@Override
		public boolean isCommandLegal(CommandSender sender, String command) {
			return this.command.equals(command);
		}
		@Override
		public boolean isComplete() {
			return false;
		}
		@Override
		public String getLocalLocalEchoHelp(CommandSender sender, String command) {
			return "Recover a note you previously removed.";
		}
		@Override
		public boolean localPreRun(CommandSender sender, String command) {
			return true; // does nothing
		}
		@Override
		public boolean localPostRun(CommandSender sender, String command) {
			if(sender instanceof Player senderPlayer && targetNoteBook != null) {
				boolean successful = targetNoteBook.recoverNote(senderPlayer);
				if(successful) {
					sender.sendMessage("Executed: One note you previously removed for: " + targetNoteBook.getTitle() + " is recovered.");
				}
				else {
					sender.sendMessage("Error: Something went wrong while recovering the note for: " + targetNoteBook.getTitle() + ".");
				}
			}
			else {
				sender.sendMessage("Error: Something went wrong while recovering the note. Target is null?");
			}
			return true;
		}
		@Override
		public void localUpdate(CommandSender sender, String command) {
			targetNoteBook = null;
		}
		class OPCItem extends OrderedParameterCommand {
			public OPCItem(OrderedParameterCommand parent) {
				super(parent, "<" + getItemTypeName() + ">");
			}
			@Override
			public List<String> getLocalTabComplete(CommandSender sender, String command) {
				List<String> results = new ArrayList<String>();
				Set<I> identifiers = noteBookSystem.getExistingNoteTargets();
				for(I identifier : identifiers) {
					NB noteBook = noteBookSystem.readyNoteBook(identifier, true);
					if(noteBook != null && sender instanceof Player senderPlayer && noteBook.canRecoverNoteOf(senderPlayer)) {
						String result = noteBookSystem.getCommandStringFromIdentifier(identifier);
						if(result != null) {
							results.add(result);
						}
					}
				}
				return results;
			}
			@Override
			public boolean isCommandLegal(CommandSender sender, String command) {
				return getLocalTabComplete(sender, command).contains(command);
			}
			@Override
			public boolean isComplete() {
				return true;
			}
			@Override
			public String getLocalLocalEchoHelp(CommandSender sender, String command) {
				return "Recover one note you previously removed from the named item.";
			}
			@Override
			public boolean localPreRun(CommandSender sender, String command) {
				return targetNoteBook != null;
			}
			@Override
			public boolean localPostRun(CommandSender sender, String command) {
				return true; // does nothing
			}
			@Override
			public void localUpdate(CommandSender sender, String command) {
				I identifier = noteBookSystem.getIdentifierFromCommandString(command);
				if(identifier != null) {
					targetNoteBook = noteBookSystem.readyNoteBook(identifier, true);
				}
			}
		}
	}
	class OPCReadAll extends OrderedParameterCommand {
		private int startPage = 1;
		private int totalPages = 1;
		private OPCStartPage branchStartPage;
		public OPCReadAll(OrderedParameterCommand parent) {
			super(parent, "readAll");
			branchStartPage = new OPCStartPage(this);
			activateSubCommand(branchStartPage);
		}
		@Override
		public boolean isCommandLegal(CommandSender sender, String command) {
			return this.command.equals(command);
		}
		@Override
		public boolean isComplete() {
			return true;
		}
		@Override
		public String getLocalLocalEchoHelp(CommandSender sender, String command) {
			return "Read all notes of this topic.";
		}
		@Override
		public boolean localPreRun(CommandSender sender, String command) {
			return true;
		}
		@Override
		public boolean localPostRun(CommandSender sender, String command) {
			if(sender instanceof Player senderPlayer) {
				senderPlayer.openBook(noteBookSystem.getWrittenBookItem(startPage));
				senderPlayer.sendMessage("Executed: Reading all notes: " + parent.command + " pages (" + startPage + "/" + totalPages + ")");
				return true;
			}
			return false;
		}
		@Override
		public void localUpdate(CommandSender sender, String command) {
			totalPages = 0;
			Map<I, NB> noteBooks = noteBookSystem.getNoteBooks();
			for(I identifier : noteBooks.keySet()) {
				NB noteBook = noteBooks.get(identifier);
				if(noteBook == null) {
					continue;
				}
				totalPages += noteBook.getPages().size();
			}
		}
		class OPCStartPage extends OrderedParameterCommand {
			public OPCStartPage(OrderedParameterCommand parent) {
				super(parent, "<optional--starting-page-number>");
			}
			@Override
			public List<String> getLocalTabComplete(CommandSender sender, String command) {
				List<String> results = new ArrayList<String>();
				try {
					int startPage = Integer.parseInt(command);
					if(startPage < 1 && startPage > totalPages) {
						throw new Exception(); // goto catch section
					}
					results.add("" + startPage);
					int possibleStartPage = startPage;
					for(int i = 0; i <= 9; i++) {
						possibleStartPage = Integer.parseInt("" + startPage + i);
						if(possibleStartPage <= totalPages) {
							results.add("" + possibleStartPage);
						}
						else {
							break;
						}
					}
				}
				catch(Exception e) {
					results.add("1");
					results.add("" + totalPages);
				}
				return results;
			}
			@Override
			public boolean isCommandLegal(CommandSender sender, String command) {
				try {
					int startPage = Integer.parseInt(command);
					return startPage >= 1 && startPage <= totalPages;
				}
				catch(Exception e) {
					return false;
				}
			}
			@Override
			public boolean isComplete() {
				return true;
			}
			@Override
			public String getLocalLocalEchoHelp(CommandSender sender, String command) {
				String echo = "An integer between 1 and the max number of pages.";
				echo += lineSep + "Default: 1";
				if(totalPages > 1) {
					echo += lineSep + "Max: " + totalPages;
				}
				else {
					echo += lineSep + "Max: Total page number of the given item.";
				}
				return echo;
			}
			@Override
			public boolean localPreRun(CommandSender sender, String command) {
				try {
					int startPage = Integer.parseInt(command);
					if(startPage > totalPages) {
						startPage = totalPages;
					}
					if(startPage < 1) {
						startPage = 1;
					}
					OPCReadAll.this.startPage = startPage;
					return true;
				}
				catch(Exception e) {
					return false;
				}
			}
			@Override
			public boolean localPostRun(CommandSender sender, String command) {
				return true; // does nothing
			}
			@Override
			public void localUpdate(CommandSender sender, String command) {} // does nothing
		}
	}
}