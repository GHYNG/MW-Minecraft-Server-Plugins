package org.mwage.mcPlugin.mw_book_frame;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new MainListener(), this);
	}
}
class MainListener implements Listener {
	@EventHandler
	public void onClickItemFrame(PlayerInteractEntityEvent event) {
		Entity entity = event.getRightClicked();
		if(!(entity instanceof ItemFrame)) {
			return;
		}
		Player player = event.getPlayer();
		if(player.isSneaking()) {
			return;
		}
		ItemFrame frame = (ItemFrame)entity;
		ItemStack item = frame.getItem();
		Material material = item.getType();
		switch(material) {
			case WRITABLE_BOOK :
				ItemStack displayBook = new ItemStack(Material.WRITTEN_BOOK, 1);
				ItemStack actualBook = item;
				BookMeta actualMeta = (BookMeta)actualBook.getItemMeta();
				BookMeta displayMeta = (BookMeta)displayBook.getItemMeta();
				List<String> pages = actualMeta.getPages();
				displayMeta.setPages(pages);
				displayMeta.setAuthor("no author yet");
				displayMeta.setTitle(actualMeta.getDisplayName());
				displayBook.setItemMeta(displayMeta);
				player.openBook(displayBook);
				break;
			case WRITTEN_BOOK :
				displayBook = item;
				player.openBook(displayBook);
				break;
			default :
				return;
		}
		event.setCancelled(true);
	}
}