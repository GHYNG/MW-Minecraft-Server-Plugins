package org.mwage.mcPlugin.note;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
public abstract class NoteBookSystem<I, N extends NoteBook<I>> implements Listener {
	public final Main plugin;
	protected Map<UUID, Set<I>> playerEditingss = new HashMap<UUID, Set<I>>(); // this is not a typo
	protected Map<I, N> noteBooks = new HashMap<I, N>();
	public NoteBookSystem(Main plugin) {
		this.plugin = plugin;
		plugin.registerListener(this);
	}
	public N getNoteBook(I identifier) {
		return noteBooks.get(identifier);
	}
	/**
	 * This is triggered when the note writer finished his note and prepared to send the note into system.
	 * 
	 * @param event
	 *            The event when writer edit the note book.
	 */
	@EventHandler
	public void onPlayerSignBook(PlayerEditBookEvent event) {
		// Bukkit.broadcastMessage("isSigning() = " + event.isSigning()); true
		if(!event.isSigning()) {
			return;
		}
		Player writer = event.getPlayer();
		UUID uuid = writer.getUniqueId();
		Set<I> playerEditings = playerEditingss.get(uuid);
		if(playerEditings == null) return;
		BookMeta eventBookMeta = event.getNewBookMeta();
		String eventBookName = eventBookMeta.getTitle();
		if(eventBookName == null) {
			return;
		}
		for(I playerEditing : playerEditings) {
			N noteBook = noteBooks.get(playerEditing);
			if(noteBook == null) {
				noteBook = createNewNoteBook(playerEditing);
				noteBooks.put(playerEditing, noteBook);
			}
			if(eventBookName.equals(noteBook.getTitle())) {
				noteBook.addNote(writer, eventBookMeta);
				event.setCancelled(true);
				PlayerInventory inventory = writer.getInventory();
				if(inventory.getItemInMainHand().getType() == Material.WRITABLE_BOOK) {
					inventory.setItemInMainHand(new ItemStack(Material.AIR));
				}
				else {
					inventory.setItemInOffHand(new ItemStack(Material.AIR));
				}
				return;
			}
		}
	}
	public void playerReadNote(Player player, I identifier, int startPage) {
		N noteBook = noteBooks.get(identifier);
		if(noteBook == null) return;
		ItemStack bookItem = noteBook.getNotesWithStartPage(startPage);
		player.openBook(bookItem);
	}
	public void playerStartEditing(Player player, I identifier) {
		Set<I> playerEditings = playerEditingss.get(player.getUniqueId());
		if(playerEditings == null) {
			playerEditings = new HashSet<I>();
			playerEditingss.put(player.getUniqueId(), playerEditings);
		}
		playerEditings.add(identifier);
	}
	public abstract I getIdentifierFromString(String str);
	public abstract N createNewNoteBook(I identifier);
}