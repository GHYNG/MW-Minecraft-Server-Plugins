package org.mwage.mcPlugin.note.notes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
public abstract class NoteBook<I> {
	public static class Page implements Comparable<Page> {
		public static final int HEADER_LINES = 4; // this is related to toString().
		public static final UUID SERVER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
		public static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		public static final String HEAD_LINE = "-------------------";
		public static final Comparator<Page> SORTER_CREATION_TIME_OLD_TO_NEW = (p1, p2) -> {
			return p1.compareTo(p2);
		};
		public static final Comparator<Page> SORTER_CREATION_TIME_NEW_TO_OLD = SORTER_CREATION_TIME_OLD_TO_NEW.reversed();
		public static final Comparator<Page> SORTER_DELETION_TIME_OLD_TO_NEW = (p1, p2) -> {
			Date d1 = p1.deletionDate == null ? new Date() : p1.deletionDate;
			Date d2 = p2.deletionDate == null ? new Date() : p2.deletionDate;
			return d1.compareTo(d2);
		};
		public static final Comparator<Page> SORTER_DELETION_TIME_NEW_TO_OLD = SORTER_DELETION_TIME_OLD_TO_NEW.reversed();
		public final NoteBook<?> parent;
		public final UUID writerUUID;
		public final Date creationDate;
		public Date deletionDate = null;
		public final List<String> contentLines = new ArrayList<String>();
		public Page(NoteBook<?> parent, UUID writerUUID, Date creationDate) {
			this.parent = parent;
			this.writerUUID = writerUUID;
			this.creationDate = creationDate;
		}
		public Page(NoteBook<?> parent, NoteBookSystem.PageConfig pageConfig) {
			this.parent = parent;
			Object owriterUUID = pageConfig.get("writerUUID");
			UUID writerUUID = UUID.fromString(owriterUUID.toString());
			this.writerUUID = writerUUID == null ? SERVER_UUID : writerUUID;
			Object ocreationDate = pageConfig.get("creationDate");
			if(ocreationDate instanceof Date date) {
				creationDate = date;
			}
			else {
				creationDate = new Date();
			}
			Object ocontentLines = pageConfig.get("contentLines");
			if(ocontentLines instanceof List<?> list) {
				list.forEach(o -> {
					contentLines.add("" + o);
				});
			}
		}
		@Override
		public int compareTo(Page o) {
			return creationDate.compareTo(o.creationDate);
		}
		@Override
		public boolean equals(Object o) {
			if(this == o) {
				return true;
			}
			if(o instanceof Page op) {
				return toString().equals(op.toString());
			}
			return false;
		}
		// This is related to HEADER_LINES
		@Override
		public String toString() {
			String content = TIME_FORMAT.format(creationDate) + "\n";
			OfflinePlayer writer = Bukkit.getOfflinePlayer(writerUUID);
			content += "Author: " + (writer == null ? writerUUID.equals(SERVER_UUID) ? "Server" : "Unknown" : writer.getName()) + "\n";
			content += parent.getTitle() + "\n";
			content += HEAD_LINE + "\n";
			int length = contentLines.size();
			if(length > 0) {
				for(int i = 0; i < length - 1; i++) {
					content += contentLines.get(i) + "\n";
				}
				content += contentLines.get(length - 1);
			}
			return content;
		}
		@Override
		public int hashCode() {
			return toString().hashCode();
		}
		public static List<String> getStringPagesTimeNewToOld(List<Page> pages) {
			pages.sort(Page.SORTER_CREATION_TIME_NEW_TO_OLD);
			List<String> strPages = new ArrayList<String>();
			for(Page page : pages) {
				strPages.add(page.toString());
			}
			if(strPages.size() == 0) {
				strPages.add("This note is currently empty");
			}
			return strPages;
		}
	}
	public final I identifier;
	private final List<Page> pages = new ArrayList<Page>();
	private Map<UUID, List<Page>> removedPagess = new HashMap<UUID, List<Page>>();
	public NoteBook(I identifier) {
		this.identifier = identifier;
	}
	/**
	 * 按照指定的页码返回一个新的笔记本实例。
	 * 这个笔记本实例应当是一次性的，不可以进入物品栏，只是显示在屏幕上供玩家浏览。
	 * 注意笔记本的页数是从1开始的，而不是按照习惯的0。
	 * 
	 * @param startPage
	 *            开始页数。
	 * @return 一个笔记本实例。
	 */
	public ItemStack getWrittenBookItem(int startPage) {
		return NoteBookSystem.getWrittenBookItem(getPages(), getAuthor(), getTitle(), startPage);
	}
	@SuppressWarnings("deprecation")
	public ItemStack getWritableBookItem(OfflinePlayer writer) {
		Page newPage = new Page(this, writer.getUniqueId(), new Date());
		List<String> strPages = new ArrayList<String>();
		strPages.add(newPage.toString());
		for(String strPage : Page.getStringPagesTimeNewToOld(getPages())) {
			strPages.add(strPage);
		}
		ItemStack bookItem = new ItemStack(Material.WRITABLE_BOOK);
		ItemMeta itemMeta = bookItem.getItemMeta();
		if(itemMeta instanceof BookMeta bookMeta) {
			bookMeta.setPages(strPages);
			bookMeta.setDisplayName(getTitle());
		}
		bookItem.setItemMeta(itemMeta);
		return bookItem;
	}
	public void addNote(OfflinePlayer writer, List<String> contentLines) {
		Page page = new Page(this, writer.getUniqueId(), new Date());
		page.contentLines.addAll(contentLines);
		getPages().add(page);
	}
	public void addNote(OfflinePlayer writer, String strPage) {
		String[] lines = strPage.split("\n");
		List<String> contentLines = new ArrayList<String>();
		for(int i = Page.HEADER_LINES; i < lines.length; i++) {
			contentLines.add(lines[i]);
		}
		addNote(writer, contentLines);
	}
	@SuppressWarnings("deprecation")
	public void addNote(OfflinePlayer writer, ItemStack bookItem) {
		if(bookItem.getItemMeta() instanceof BookMeta bookMeta) {
			List<String> strPages = bookMeta.getPages();
			if(strPages.size() > 0) {
				String p1 = strPages.get(0);
				addNote(writer, p1);
			}
		}
	}
	public boolean removeNote(OfflinePlayer writer) {
		UUID writeruuid = writer.getUniqueId();
		getPages().sort(Page.SORTER_CREATION_TIME_NEW_TO_OLD);
		int index = -1;
		for(int i = 0; i < getPages().size(); i++) {
			Page page = getPages().get(i);
			if(writeruuid.equals(page.writerUUID)) {
				index = i;
				break;
			}
		}
		if(index != -1) {
			Page removedPage = getPages().get(index);
			getPages().remove(index);
			List<Page> removedPages = removedPagess.get(writeruuid);
			if(removedPages == null) {
				removedPages = new ArrayList<Page>();
				removedPagess.put(writeruuid, removedPages);
			}
			removedPages.add(removedPage);
			removedPage.deletionDate = new Date();
			return true;
		}
		return false;
	}
	public boolean recoverNote(OfflinePlayer writer) {
		UUID writeruuid = writer.getUniqueId();
		List<Page> removedPages = removedPagess.get(writeruuid);
		if(removedPages == null) {
			return false;
		}
		if(removedPages.size() == 0) {
			return false;
		}
		removedPages.sort(Page.SORTER_DELETION_TIME_NEW_TO_OLD);
		Page removedPage = removedPages.get(0);
		removedPages.remove(0);
		getPages().add(removedPage);
		removedPage.deletionDate = null;
		return true;
	}
	public abstract String getTitle();
	public abstract String getAuthor();
	public void addPage(Page page) {
		getPages().add(page);
	}
	public List<Page> getPages() {
		return pages;
	}
	public boolean canRemoveNoteOf(OfflinePlayer author) {
		for(Page page : pages) {
			if(author.getUniqueId().equals(page.writerUUID)) {
				return true;
			}
		}
		return false;
	}
	public boolean canRecoverNoteOf(OfflinePlayer player) {
		UUID uuid = player.getUniqueId();
		Set<UUID> removers = removedPagess.keySet();
		if(!removers.contains(uuid)) {
			return false;
		}
		List<Page> removedPages = removedPagess.get(uuid);
		if(removedPages == null) {
			return false;
		}
		return removedPages.size() != 0;
	}
}