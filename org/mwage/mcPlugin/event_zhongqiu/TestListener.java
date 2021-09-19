package org.mwage.mcPlugin.event_zhongqiu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
@SuppressWarnings("deprecation")
public class TestListener implements Listener {
	public final Main plugin;
	TestListener(Main plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		if(message.equals("狼头")) {
			PlayerInventory inv = player.getInventory();
			inv.setHelmet(StaticData.wolf_head);
		}
		if(message.equals("谋杀")) {
			plugin.killPlayer(player, DeathReason.Murdered);
		}
		if(message.equals("spawn")) {
			boolean teleported = player.teleport(plugin.game_start_location);
			Bukkit.broadcastMessage("teleported = " + teleported);
		}
		if(message.equals("location")) {
			Bukkit.broadcastMessage(plugin.game_start_location.toString());
		}
		if(message.equals("item")) {
			Bukkit.broadcastMessage(player.getInventory().getItemInMainHand().toString());
		}
		if(message.equals("dropall")) {
			PlayerInventory inv = player.getInventory();
			inv.setHelmet(StaticData.wolf_head);
			player.dropItem(true);
			inv.setItemInMainHand(inv.getItemInOffHand());
			inv.setItemInOffHand(null);
			player.dropItem(true);
			ItemStack[] items = inv.getStorageContents();
			int length = items.length;
			for(int i = 0; i < length; i++) {
				if(items[i] != null) {
					inv.setItemInMainHand(items[i]);
					player.dropItem(true);
				}
			}
			inv.clear();
		}
	}
}