package org.mwage.mcPlugin.note;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
public abstract class NoteBook<T> {
	public static class Page implements Comparable<Page> {
		public static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		public static final UUID SERVER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
		public static final String HEAD_LINE = "-------------------";
		public static final Comparator<Page> PAGE_SORTER_TIME_NEW_TO_OLD = (p1, p2) -> {
			return p1.compareTo(p2);
		};
		public final UUID writer;
		public final Date creationDate;
		protected final List<String> lines = new ArrayList<String>();
		public Page(UUID writer, Date creationDate) {
			this.writer = writer;
			this.creationDate = creationDate;
		}
		public Page(CommandSender commandSender, Date creationDate) {
			this.creationDate = creationDate;
			if(commandSender instanceof Player player) {
				this.writer = player.getUniqueId();
				return;
			}
			this.writer = SERVER_UUID;
		}
		@Override
		public String toString() {
			String content = "";
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(writer);
			String ownerName = offlinePlayer == null ? "Server" : offlinePlayer.getName();
			content = ownerName + "\n";
			content += TIME_FORMAT.format(creationDate) + "\n";
			content += HEAD_LINE;
			if(lines.size() == 0) {
				return content;
			}
			for(int i = 0; i < lines.size() - 1; i++) {
				content += lines.get(i) + "\n";
			}
			content += lines.get(lines.size() - 1);
			return content;
		}
		@Override
		public boolean equals(Object o) {
			if(this == o) {
				return true;
			}
			if(o instanceof Page noteBook) {
				return writer.equals(noteBook.writer) && creationDate.equals(noteBook.creationDate) && lines.equals(noteBook.lines);
			}
			return false;
		}
		@Override
		public int compareTo(Page o) {
			return creationDate.compareTo(o.creationDate);
		}
	}
	public final T identifier;
	protected List<Page> pages = new ArrayList<Page>();
	protected Map<UUID, List<Page>> removedPagess = new HashMap<UUID, List<Page>>();
	public NoteBook(T identifier) {
		this.identifier = identifier;
	}
	public ItemStack getNotes() {
		return getNotesWithStartPage(1);
	}
	@SuppressWarnings("deprecation")
	public ItemStack getNotesWithStartPage(int startPage) {
		if(startPage < 1) {
			return getNotesWithStartPage(1);
		}
		ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK);
		if(startPage > pages.size()) {
			return bookItem;
		}
		ItemMeta itemMeta = bookItem.getItemMeta();
		if(itemMeta instanceof BookMeta bookMeta) {
			pages.sort(Page.PAGE_SORTER_TIME_NEW_TO_OLD);
			List<String> strPages = new ArrayList<String>();
			for(int i = startPage; i <= pages.size(); i++) {
				strPages.add(pages.get(i - 1).toString());
			}
			bookMeta.setPages(strPages);
			bookMeta.setAuthor(getAuthor());
			bookMeta.setTitle(getTitle());
			bookItem.setItemMeta(bookMeta);
		}
		return bookItem;
	}
	public void addNote(Player writer, ItemStack itemStack) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		if(itemMeta instanceof BookMeta bookMeta) {
			addNote(writer, bookMeta);
		}
	}
	@SuppressWarnings("deprecation")
	public void addNote(Player writer, BookMeta bookMeta) {
		String page = bookMeta.getPage(1);
		String[] arrLines = page.split("\n");
		if(arrLines.length < 4) return; // this means the note is empty!
		List<String> lines = new ArrayList<String>();
		for(int i = 3; i < arrLines.length; i++) {
			lines.add(arrLines[i]);
		}
		addNote(writer, lines);
	}
	public void addNote(Player writer, List<String> lines) {
		Page page = new Page(writer.getUniqueId(), new Date());
		page.lines.addAll(lines);
	}
	public void removeNote(Player writer) {
		pages.sort(Page.PAGE_SORTER_TIME_NEW_TO_OLD);
		UUID writerUUID = writer.getUniqueId();
		for(Page page : pages) {
			if(writerUUID.equals(page.writer)) {
				pages.remove(page);
				List<Page> removedPages = removedPagess.get(writerUUID);
				if(removedPages == null) {
					removedPages = new ArrayList<Page>();
					removedPagess.put(writerUUID, removedPages);
				}
				removedPages.add(page);
				return;
			}
		}
	}
	public void recoverNote(Player writer) {
		UUID writerUUID = writer.getUniqueId();
		List<Page> removedPages = removedPagess.get(writerUUID);
		if(removedPages == null) {
			return;
		}
		int length = removedPages.size();
		if(length == 0) {
			return;
		}
		Page lastRemovedPage = removedPages.get(length - 1);
		removedPages.remove(lastRemovedPage);
		pages.add(lastRemovedPage);
	}
	@SuppressWarnings("deprecation")
	public ItemStack readyNewNote(Player writer) {
		ItemStack existedNoteBook = getNotes();
		ItemStack newNoteBook = new ItemStack(Material.WRITABLE_BOOK);
		ItemMeta itemMeta = existedNoteBook.getItemMeta();
		if(itemMeta instanceof BookMeta bookMeta) {
			Page emptyPage = new Page(writer, new Date());
			List<String> strPages = new ArrayList<String>();
			strPages.add(emptyPage.toString());
			pages.sort(Page.PAGE_SORTER_TIME_NEW_TO_OLD);
			pages.forEach(p -> {
				strPages.add(p.toString());
			});
			bookMeta.setPages(strPages);
			newNoteBook.setItemMeta(bookMeta);
		}
		return newNoteBook;
	}
	public void playerReadNotes(Player reader, int startPage) {
		reader.openBook(getNotesWithStartPage(1));
	}
	public void playerWriteNotes(Player writer) {
		PlayerInventory inventory = writer.getInventory();
		if(inventory.getItemInMainHand().getType() != Material.AIR) {
			return;
		}
		inventory.setItemInMainHand(readyNewNote(writer));
	}
	public void playerRemoveNotes(Player remover) {
		removeNote(remover);
	}
	public void playerRecoverNotes(Player recover) {
		recoverNote(recover);
	}
	public abstract String getAuthor();
	public abstract String getTitle();
}