package org.mwage.mcPlugin.chat;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;
@SuppressWarnings("deprecation")
public class Main extends JavaPlugin {
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
	}
}
interface ChatUtils {
	default boolean isOwner(Player player) {
		String name = player.getName();
		return(name.equals("GHYNG") || name.equals("MWQJDOR") || name.equals("Antrooper"));
	}
	default Server server() {
		return Bukkit.getServer();
	}
	default String line(String... parts) {
		String s = "";
		for(String part : parts) {
			s += part;
		}
		return s;
	}
}
class ChatListener implements Listener, ChatUtils {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayer(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		String fullName = "";
		if(isOwner(player)) {
			fullName = ChatColor.DARK_RED + "[A] " + name;
		}
		else if(player.isOp()) {
			fullName = ChatColor.GOLD + "[O] " + name;
		}
		else {
			fullName = ChatColor.DARK_GREEN + "[C] " + name;
		}
		String message = event.getMessage();
		String completeMessage = fullName + ChatColor.WHITE + ": " + message;
		Bukkit.getServer().broadcastMessage(completeMessage);
		event.setCancelled(true);
		playerChatSound(player);
		Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		for(Player onlinePlayer : onlinePlayers) {
			String onlineName = onlinePlayer.getName();
			if(message.toLowerCase().contains("@" + onlineName.toLowerCase())) {
				playerReferencedSound(onlinePlayer);
				String title = ChatColor.DARK_PURPLE + "你被在聊天信息中提到了";
				if(name.equals(onlineName)) {
					onlinePlayer.sendTitle(title, "提到你的是你自己");
				}
				else {
					onlinePlayer.sendTitle(title, "提到你的是" + fullName);
				}
			}
			else if(message.toLowerCase().contains("@owner") && isOwner(onlinePlayer)) {
				playerReferencedSound(onlinePlayer);
				String title = ChatColor.DARK_PURPLE + "你被在聊天信息中作为服主被提到了";
				if(name.equals(onlineName)) {
					onlinePlayer.sendTitle(title, "提到你的是你自己");
				}
				else {
					onlinePlayer.sendTitle(title, "提到你的是" + fullName);
				}
			}
			else if((message.toLowerCase().contains("@manager") || message.toLowerCase().contains("@operator")) && player.isOp()) {
				playerReferencedSound(onlinePlayer);
				String title = ChatColor.DARK_PURPLE + "你被在聊天信息中作为管理员被提到了";
				if(name.equals(onlineName)) {
					onlinePlayer.sendTitle(title, "提到你的是你自己");
				}
				else {
					onlinePlayer.sendTitle(title, "提到你的是" + fullName);
				}
			}
			else if(message.toLowerCase().contains("@all")) {
				playerReferencedSound(onlinePlayer);
				String title = ChatColor.DARK_PURPLE + "所有人都被在聊天信息中提到了";
				if(name.equals(onlineName)) {
					onlinePlayer.sendTitle(title, "提到你的是你自己");
				}
				else {
					onlinePlayer.sendTitle(title, "提到你的是" + fullName);
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onServer(ServerCommandEvent event) {
		String command = event.getCommand();
		if(command.startsWith("say") && command.length() > 4) {
			String message = command.substring(4);
			while(message.startsWith(" ")) {
				message = message.substring(1);
			}
			String completeMessage = ChatColor.AQUA + "[S] Server" + ChatColor.WHITE + ": " + message;
			Bukkit.getServer().broadcastMessage(completeMessage);
			event.setCancelled(true);
			Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
			for(Player onlinePlayer : onlinePlayers) {
				String onlineName = onlinePlayer.getName();
				if(message.toLowerCase().contains("@" + onlineName.toLowerCase())) {
					playerReferencedSound(onlinePlayer);
					String title = ChatColor.DARK_PURPLE + "你被在聊天信息中提到了";
					onlinePlayer.sendTitle(title, "提到你的是" + ChatColor.AQUA + "服务器娘");
				}
				else {
					playerChatSound(onlinePlayer);
				}
			}
		}
	}
	public void playerChatSound(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.6f, 1.2f);
	}
	public void playerReferencedSound(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.8f, 1.2f);
	}
}