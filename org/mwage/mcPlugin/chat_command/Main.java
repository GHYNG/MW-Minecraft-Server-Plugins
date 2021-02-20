package org.mwage.mcPlugin.chat_command;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
@SuppressWarnings("deprecation")
public class Main extends JavaPlugin {
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new ChatCommandListener(), this);
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
	default String page(String... lines) {
		int length = lines.length;
		if(length == 0) {
			return "";
		}
		String page = "";
		for(int i = 0; i < length - 1; i++) {
			page += lines[i] + "\n";
		}
		page += lines[length - 1];
		return page;
	}
}
class ChatCommandListener implements Listener, ChatUtils {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		String message = event.getMessage();
		if(!message.startsWith("mwc")) {
			return;
		}
		message = message.replaceAll("  ", " ");
		String[] subCommands = message.split(" ");
		Player player = event.getPlayer();
		MWCEvent mwcevent = new MWCEvent(player, subCommands);
		server().getPluginManager().callEvent(mwcevent);
		if(mwcevent.isCancelled()) {
			return;
		}
		else {
			parseEvent(mwcevent);
		}
	}
	public void parseEvent(MWCEvent event) {
		Player player = event.getPlayer();
		if(!isOwner(player)) {
			mwcNotAllowed(player);
			echo(player, "哼， 你没有mwc的权限！");
			return;
		}
		List<String> commands = event.getCommandParts();
		int size = commands.size();
		if(size == 0) {
			String line0 = "MWC指令说明";
			String line1 = "  opme - 将自己设为op";
			String line2 = "  deop - 将自己解除op";
			String page = page(line0, line1, line2);
			echo(player, page);
		}
		else if(size >= 1) {
			String c0 = commands.get(0);
			if(c0.equalsIgnoreCase("opme")) {
				if(player.isOp()) {
					mwcGood(player);
					echo(player, "主人， 您已经是OP了！");
				}
				else {
					player.setOp(true);
					mwcGood(player);
					echo(player, "主人， 您已经恢复为OP。");
				}
			}
			else if(c0.equalsIgnoreCase("deop")) {
				if(!player.isOp()) {
					mwcGood(player);
					echo(player, "主人， 您已经不是是OP了！");
				}
				else {
					player.setOp(false);
					mwcGood(player);
					echo(player, "主人， 您已经解除OP。");
				}
			}
			else {
				mwcBad(player);
				echo(player, "主人， 您输错指令啦！ 好菜！");
			}
		}
	}
	public void echo(Player player, String message) {
		player.sendMessage(message);
	}
	public void mwcGood(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.8f, 1.2f);
	}
	public void mwcBad(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.ENTITY_CAT_BEG_FOR_FOOD, SoundCategory.PLAYERS, 0.8f, 1.2f);
	}
	public void mwcNotAllowed(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 0.8f, 1.2f);
	}
}