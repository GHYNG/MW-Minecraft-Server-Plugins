package org.mwage.mcPlugin.note.notes;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.mwage.mcPlugin.note.Main;
import org.mwage.mcPlugin.note.notes.NoteBook.Page;
public abstract class NoteBookSystem<I, NB extends NoteBook<I>> implements Listener {
	public static class PageConfig extends HashMap<String, Object> {
		private static final long serialVersionUID = 1L;
		public PageConfig(NoteBook.Page page) {
			put("writerUUID", page.writerUUID.toString());
			put("creationDate", page.creationDate);
			put("contentLines", page.contentLines);
		}
		@SuppressWarnings({
				"unchecked", "rawtypes"
		})
		protected PageConfig(Map map) {
			putAll(map);
		}
	}
	public final Main plugin;
	private final Map<UUID, Set<I>> playerWritingss = new HashMap<UUID, Set<I>>();
	private final Map<I, NB> noteBooks = new HashMap<I, NB>();
	public NoteBookSystem(Main plugin) {
		this.plugin = plugin;
		onEnable();
	}
	protected void onEnable() {
		readNoteBooksFiles();
		plugin.registerListener(this);
	}
	private void readNoteBooksFiles() {
		File folder = getFolder();
		folder.mkdirs();
		File[] files = folder.listFiles(f -> {
			if(!f.exists()) {
				return false;
			}
			if(f.isFile()) {
				String name = f.getName();
				if(name.endsWith("." + getFileExtendName())) {
					return true;
				}
			}
			return false;
		});
		for(File file : files) {
			String fileName = file.getName();
			int fileNameLength = fileName.length();
			String istr = fileName.substring(0, fileNameLength - getFileExtendName().length() - 1);
			I identifier = getIdentifierFromFileString(istr);
			if(identifier == null) {
				continue;
			}
			FileConfiguration config = new YamlConfiguration();
			NB noteBook = readyNoteBook(identifier, true);
			try {
				config.load(file);
				List<?> opages = config.getList("pages");
				for(Object opage : opages) {
					if(opage instanceof Map<?, ?> mpage) {
						PageConfig pageConfig = new PageConfig(mpage);
						NoteBook.Page page = new NoteBook.Page(noteBook, pageConfig);
						noteBook.addPage(page);
					}
				}
			}
			catch(IOException | InvalidConfigurationException e) {
				e.printStackTrace();
				continue;
			}
		}
	}
	public void onDisable() {
		writeNoteBooksFiles();
	}
	private void writeNoteBooksFiles() {
		File folder = getFolder();
		folder.mkdirs();
		File[] files = folder.listFiles(f -> {
			if(!f.exists()) {
				return false;
			}
			if(f.isFile()) {
				String name = f.getName();
				if(name.endsWith("." + getFileExtendName())) {
					return true;
				}
			}
			return false;
		});
		for(File file : files) {
			file.delete();
		}
		Set<I> identifiers = noteBooks.keySet();
		for(I identifier : identifiers) {
			File file = new File(folder, getFileStringFromIdentifier(identifier) + "." + getFileExtendName());
			try {
				file.createNewFile();
				FileConfiguration fileConfig = new YamlConfiguration();
				NB noteBook = readyNoteBook(identifier, true);
				List<Page> pages = noteBook.getPages();
				List<PageConfig> pageConfigs = new ArrayList<PageConfig>();
				for(Page page : pages) {
					pageConfigs.add(new PageConfig(page));
				}
				fileConfig.set("pages", pageConfigs);
				fileConfig.save(file);
			}
			catch(IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerSignBook(PlayerEditBookEvent event) {
		if(!event.isSigning()) {
			return;
		}
		if(event.isCancelled()) {
			return;
		}
		Player writer = event.getPlayer();
		BookMeta bookMeta = event.getNewBookMeta();
		I identifier = playerMayWriteNote(writer, bookMeta);
		if(identifier != null) {
			removeNoteBookInHand(writer);
			NB noteBook = readyNoteBook(identifier, true);
			String strPage = "" + bookMeta.getPage(1);
			noteBook.addNote(writer, strPage);
			playerStopWriting(writer, identifier);
			event.setCancelled(true);
		}
	}
	public void playerStartWriting(OfflinePlayer writer, I identifier) {
		UUID writeruuid = writer.getUniqueId();
		Set<I> playerWritings = playerWritingss.get(identifier);
		if(playerWritings == null) {
			playerWritings = new HashSet<I>();
			playerWritingss.put(writeruuid, playerWritings);
		}
		playerWritings.add(identifier);
		readyNoteBook(identifier, true);
	}
	public void playerStopWriting(OfflinePlayer writer, I identifier) {
		UUID writeruuid = writer.getUniqueId();
		Set<I> playerWritings = playerWritingss.get(identifier);
		if(playerWritings == null) {
			playerWritings = new HashSet<I>();
			playerWritingss.put(writeruuid, playerWritings);
		}
		playerWritings.remove(identifier);
	}
	protected abstract NB createNewNoteBook(I identifier);
	public NB readyNoteBook(I identifier, boolean forceCreate) {
		NB noteBook = noteBooks.get(identifier);
		if(noteBook == null && forceCreate) {
			noteBook = createNewNoteBook(identifier);
			noteBooks.put(identifier, noteBook);
		}
		return noteBook;
	}
	@SuppressWarnings("deprecation")
	private I playerMayWriteNote(OfflinePlayer writer, BookMeta bookMeta) {
		String title = bookMeta.getTitle() == null ? "null" : bookMeta.getTitle();
		String displayName = bookMeta.getDisplayName() == null ? "null" : bookMeta.getDisplayName();
		UUID writeruuid = writer.getUniqueId();
		Set<I> playerWritings = playerWritingss.get(writeruuid);
		if(playerWritings == null) {
			return null;
		}
		for(I identifier : playerWritings) {
			NB noteBook = noteBooks.get(identifier);
			if(noteBook == null) {
				return null;
			}
			if(title.equals(noteBook.getTitle()) || displayName.equals(noteBook.getTitle())) {
				return identifier;
			}
		}
		return null;
	}
	private void removeNoteBookInHand(Player writer) {
		PlayerInventory inventory = writer.getInventory();
		ItemStack mainHand = inventory.getItemInMainHand();
		if(mainHand != null && mainHand.getType() == Material.WRITABLE_BOOK) {
			int handindex = inventory.getHeldItemSlot();
			mainHand.setAmount(0);
			inventory.setItem(handindex, mainHand);
		}
		else {
			inventory.setItemInOffHand(null);
		}
	}
	public Set<String> getCommandStrings() {
		Set<I> keys = noteBooks.keySet();
		Set<String> commandKeys = new HashSet<String>();
		for(I key : keys) {
			String commandString = getCommandStringFromIdentifier(key);
			if(commandString != null) {
				commandKeys.add(commandString);
			}
		}
		return commandKeys;
	}
	public Map<I, NB> getNoteBooks() {
		return this.noteBooks;
	}
	public abstract File getFolder();
	public abstract String getFileExtendName();
	public abstract I getIdentifierFromFileString(String string);
	public abstract String getFileStringFromIdentifier(I identifier);
	public abstract I getIdentifierFromCommandString(String string);
	public abstract String getCommandStringFromIdentifier(I identifier);
	public abstract String getItemTypeName();
	public Set<I> getExistingNoteTargets() {
		return noteBooks.keySet();
	}
	public abstract Set<I> getPossibleNoteTargets();
	public List<String> getExistingNoteTargetCommandStrings() {
		List<String> commandStrings = new ArrayList<String>();
		for(I identifier : noteBooks.keySet()) {
			String commandString = getCommandStringFromIdentifier(identifier);
			if(commandString != null) {
				commandStrings.add(commandString);
			}
		}
		return commandStrings;
	}
	public List<String> getPossibleNoteTargetCommandStrings() {
		List<String> commandStrings = new ArrayList<String>();
		for(I identifier : getPossibleNoteTargets()) {
			String commandString = getCommandStringFromIdentifier(identifier);
			if(commandString != null) {
				commandStrings.add(commandString);
			}
		}
		return commandStrings;
	}
	public boolean canRemoveNoteOf(OfflinePlayer author) {
		for(I identifier : noteBooks.keySet()) {
			NB noteBook = noteBooks.get(identifier);
			if(noteBook != null && noteBook.canRemoveNoteOf(author)) {
				return true;
			}
		}
		return false;
	}
	public boolean canRecoverNoteOf(OfflinePlayer author) {
		for(I identifier : noteBooks.keySet()) {
			NB noteBook = noteBooks.get(identifier);
			if(noteBook != null && noteBook.canRecoverNoteOf(author)) {
				return true;
			}
		}
		return false;
	}
	@SuppressWarnings("deprecation")
	public static ItemStack getWrittenBookItem(List<NoteBook.Page> pages, String author, String title, int startPage) {
		if(startPage > pages.size()) {
			startPage = pages.size();
		}
		if(startPage < 1) {
			startPage = 1;
		}
		int actualStartPage = startPage - 1;
		List<String> strPages = NoteBook.Page.getStringPagesTimeNewToOld(pages);
		List<String> actualStrPages = new ArrayList<String>();
		for(int i = actualStartPage; i < strPages.size(); i++) {
			actualStrPages.add(strPages.get(i));
		}
		ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK);
		ItemMeta itemMeta = bookItem.getItemMeta();
		if(itemMeta instanceof BookMeta bookMeta) {
			bookMeta.setPages(actualStrPages);
			/*
			 * FIXED: if the title for a book item is too long,
			 * the item will not work correctly.
			 * Plus the actual title for the book is not needed anyway,
			 * since the text displayed to players will be displayName
			 */
			bookMeta.setTitle("book title");
			bookMeta.setAuthor(author);
			bookMeta.setDisplayName(title);
		}
		bookItem.setItemMeta(itemMeta);
		return bookItem;
	}
	public ItemStack getWrittenBookItem(int startPage) {
		List<NoteBook.Page> pages = new ArrayList<NoteBook.Page>();
		for(I identifier : noteBooks.keySet()) {
			NB noteBook = noteBooks.get(identifier);
			if(noteBook == null) {
				continue;
			}
			pages.addAll(noteBook.getPages());
		}
		return getWrittenBookItem(pages, "Server", "Note Books", startPage);
	}
}