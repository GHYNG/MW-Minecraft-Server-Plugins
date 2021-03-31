package org.mwage.mcPlugin.chat;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
	default String line(Object... parts) {
		String s = "";
		for(Object part : parts) {
			s += part;
		}
		return s;
	}
	default boolean and(boolean... bs) {
		for(boolean b : bs) {
			if(b == false) {
				return false;
			}
		}
		return true;
	}
	default boolean or(boolean... bs) {
		for(boolean b : bs) {
			if(b == true) {
				return true;
			}
		}
		return false;
	}
}
interface April1Util extends ChatUtils {
	default String getRandomColoredTitledName(Player player) {
		String name = player.getName();
		Random random = new Random();
		int colorindex = random.nextInt(16);
		int charindex = random.nextInt(26);
		ChatColor color = ChatColor.values()[colorindex];
		char c = (char)(65 + charindex);
		String fullfront = line(color, "[", c, "] ", name);
		return fullfront;
	}
	@SuppressWarnings("deprecation")
	default boolean isApril1Day(Date date) {
		return or(and(date.getMonth() == 2, date.getDate() == 31), and(date.getMonth() == 3, date.getDate() == 1));
	}
	default boolean isApril1Day() {
		return isApril1Day(new Date());
	}
}
class ChatListener implements Listener, ChatUtils, April1Util {
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
		if(player.getGameMode() == GameMode.SPECTATOR && (!player.isOp())) {
			fullName = "[观察] " + fullName;
		}
		Date date = new Date();
		if(isApril1Day(date)) {
			fullName = getRandomColoredTitledName(player);
		}
		String message = event.getMessage();
		String completeMessage = fullName + ChatColor.WHITE + ": " + message;
		Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		if(player.getGameMode() == GameMode.SPECTATOR && (!player.isOp())) {
			for(Player onlinePlayer : onlinePlayers) {
				if(onlinePlayer.getGameMode() == GameMode.SPECTATOR || onlinePlayer.isOp()) {
					onlinePlayer.sendMessage(completeMessage);
				}
			}
			event.setCancelled(true);
			return;
		}
		Bukkit.getServer().broadcastMessage(completeMessage);
		event.setCancelled(true);
		playerChatSound(player);
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
				String title = ChatColor.DARK_PURPLE + "服主被提到了";
				if(name.equals(onlineName)) {
					onlinePlayer.sendTitle(title, "提到你的是你自己");
				}
				else {
					onlinePlayer.sendTitle(title, "提到你的是" + fullName);
				}
			}
			else if((message.toLowerCase().contains("@manager") || message.toLowerCase().contains("@operator")) && onlinePlayer.isOp()) {
				playerReferencedSound(onlinePlayer);
				String title = ChatColor.DARK_PURPLE + "管理员被提到了";
				if(name.equals(onlineName)) {
					onlinePlayer.sendTitle(title, "提到你的是你自己");
				}
				else {
					onlinePlayer.sendTitle(title, "提到你的是" + fullName);
				}
			}
			else if(message.toLowerCase().contains("@all")) {
				playerReferencedSound(onlinePlayer);
				String title = ChatColor.DARK_PURPLE + "所有人都被提到了";
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
					// playerChatSound(onlinePlayer);
				}
			}
		}
	}
	public void playerChatSound(Player player) {
		Location location = player.getLocation();
		if(isApril1Day(new Date())) {
			player.playSound(location, Sound.ENTITY_VILLAGER_AMBIENT, SoundCategory.PLAYERS, 0.6f, 1.2f);
			return;
		}
		player.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.6f, 1.2f);
	}
	public void playerReferencedSound(Player player) {
		Location location = player.getLocation();
		if(isApril1Day(new Date())) {
			player.playSound(location, Sound.ENTITY_WITCH_AMBIENT, SoundCategory.PLAYERS, 0.6f, 1.2f);
			return;
		}
		player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.8f, 1.2f);
	}
}