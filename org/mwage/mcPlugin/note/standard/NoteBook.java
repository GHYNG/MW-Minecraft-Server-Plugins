package org.mwage.mcPlugin.note.standard;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
public abstract class NoteBook<I> {
	public static class Page implements Comparable<Page> {
		public static final UUID SERVER_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
		public static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		public static final String HEAD_LINE = "-------------------";
		public static final Comparator<Page> SORTER_TIME_OLD_TO_NEW = (p1, p2) -> {
			return p1.compareTo(p2);
		};
		public static final Comparator<Page> SORTER_TIME_NEW_TO_OLD = SORTER_TIME_OLD_TO_NEW.reversed();
		public final UUID writerUUID;
		public final Date creationDate;
		public final List<String> contentLines = new ArrayList<String>();
		public Page(UUID writerUUID, Date creationDate) {
			this.writerUUID = writerUUID;
			this.creationDate = creationDate;
		}
		public Page(NoteBookSystem.PageConfig pageConfig) {
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
		@Override
		public String toString() {
			String content = TIME_FORMAT.format(creationDate) + "\n";
			OfflinePlayer writer = Bukkit.getOfflinePlayer(writerUUID);
			content += (writer == null ? writerUUID.equals(SERVER_UUID) ? "Server" : "Unknown" : writer.getName()) + "\n";
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
	}
	public final I identifier;
	private final List<Page> pages = new ArrayList<Page>();
	private Map<UUID, List<Page>> removedPagess = new HashMap<UUID, List<Page>>();
	public NoteBook(I identifier) {
		this.identifier = identifier;
	}
	private List<String> getStringPagesTimeNewToOld() {
		getPages().sort(Page.SORTER_TIME_NEW_TO_OLD);
		List<String> strPages = new ArrayList<String>();
		for(Page page : getPages()) {
			strPages.add(page.toString());
		}
		if(strPages.size() == 0) {
			strPages.add("This note is currently empty");
		}
		return strPages;
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
	@SuppressWarnings("deprecation")
	public ItemStack getWrittenBookItem(int startPage) {
		if(startPage > pages.size()) {
			startPage = pages.size();
		}
		if(startPage < 1) {
			startPage = 1;
		}
		int actualStartPage = startPage - 1;
		List<String> strPages = getStringPagesTimeNewToOld();
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
			bookMeta.setAuthor(getAuthor());
			bookMeta.setDisplayName(getTitle());
		}
		bookItem.setItemMeta(itemMeta);
		return bookItem;
	}
	@SuppressWarnings("deprecation")
	public ItemStack getWritableBookItem(OfflinePlayer writer) {
		Page newPage = new Page(writer.getUniqueId(), new Date());
		List<String> strPages = new ArrayList<String>();
		strPages.add(newPage.toString());
		for(String strPage : getStringPagesTimeNewToOld()) {
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
		Page page = new Page(writer.getUniqueId(), new Date());
		page.contentLines.addAll(contentLines);
		getPages().add(page);
	}
	public void addNote(OfflinePlayer writer, String strPage) {
		String[] lines = strPage.split("\n");
		List<String> contentLines = new ArrayList<String>();
		for(int i = 3; i < lines.length; i++) {
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
		getPages().sort(Page.SORTER_TIME_NEW_TO_OLD);
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
		removedPages.sort(Page.SORTER_TIME_NEW_TO_OLD);
		Page removedPage = removedPages.get(0);
		removedPages.remove(0);
		getPages().add(removedPage);
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
}