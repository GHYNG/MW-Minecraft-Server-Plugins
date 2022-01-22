package org.mwage.mcPlugin.note.standard;
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
import org.mwage.mcPlugin.note.Main;
import org.mwage.mcPlugin.note.standard.NoteBook.Page;
public abstract class NoteBookSystem<I, N extends NoteBook<I>> implements Listener {
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
	private final Map<I, N> noteBooks = new HashMap<I, N>();
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
			I identifier = getIdentifierFromString(istr);
			if(identifier == null) {
				continue;
			}
			FileConfiguration config = new YamlConfiguration();
			N noteBook = readyNoteBook(identifier, true);
			try {
				config.load(file);
				List<?> opages = config.getList("pages");
				for(Object opage : opages) {
					if(opage instanceof Map<?, ?> mpage) {
						PageConfig pageConfig = new PageConfig(mpage);
						NoteBook.Page page = new NoteBook.Page(pageConfig);
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
			File file = new File(folder, getStringFromIdentifier(identifier) + "." + getFileExtendName());
			try {
				file.createNewFile();
				FileConfiguration fileConfig = new YamlConfiguration();
				N noteBook = readyNoteBook(identifier, true);
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
			N noteBook = readyNoteBook(identifier, true);
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
	protected abstract N createNewNoteBook(I identifier);
	public N readyNoteBook(I identifier, boolean forceCreate) {
		N noteBook = noteBooks.get(identifier);
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
			N noteBook = noteBooks.get(identifier);
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
			inventory.setItem(handindex, null);
		}
		else {
			inventory.setItemInOffHand(null);
		}
	}
	protected abstract File getFolder();
	protected abstract I getIdentifierFromString(String string);
	protected abstract String getStringFromIdentifier(I identifier);
	public abstract String getFileExtendName();
}