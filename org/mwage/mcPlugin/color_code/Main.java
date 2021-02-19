package org.mwage.mcPlugin.color_code;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
@SuppressWarnings("deprecation")
public class Main extends JavaPlugin {
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new ColorTextListener(), this);
	}
	public void onDisable() {
	}
}
class ColorTextListener implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		String text = event.getMessage();
		text = ColorCoder.parse(text);
		event.setMessage(text);
	}
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		String[] lines = event.getLines();
		int length = lines.length;
		for(int i = 0; i < length; i++) {
			event.setLine(i, ColorCoder.parse(lines[i]));
		}
	}
	@EventHandler
	public void onPlayerEditBook(PlayerEditBookEvent event) {
		BookMeta meta = event.getNewBookMeta();
		List<String> pages = meta.getPages();
		List<String> newPages = new ArrayList<String>();
		for(String page : pages) {
			String newPage = ColorCoder.parse(page);
			newPages.add(newPage);
		}
		meta.setPages(newPages);
		event.setNewBookMeta(meta);
	}
	@EventHandler
	public void onAnvilRenameItem(PrepareAnvilEvent event) {
		try {
			ItemStack stack = event.getResult();
			ItemMeta meta = stack.getItemMeta();
			String name = meta.getDisplayName();
			name = ColorCoder.parse(name);
			meta.setDisplayName(name);
			stack.setItemMeta(meta);
			event.setResult(stack);
		}
		catch(NullPointerException e) {
		}
	}
	@EventHandler
	public void onPlayerCommandSay(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage();
		if(command.startsWith("/say")) {
			int length = command.length();
			if(length > 4) {
				String message = command.substring(4);
				while(message.startsWith(" ") && message.length() > 1) {
					message = message.substring(1);
				}
				message = ColorCoder.parse(message);
				Player player = event.getPlayer();
				player.chat(message);
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onServerCommandSay(ServerCommandEvent event) {
		String command = event.getCommand();
		if(command.startsWith("say")) {
			command = ColorCoder.parse(command);
			event.setCommand(command);
		}
	}
}
class ColorCoder {
	static final String sectionMark = "¡ì";
	static final String[] styleCodes = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "l", "m", "n", "o", "r"};
	static String parse(String text) {
		text = text.replaceAll(sectionMark, "\\\\");
		for(String styleCode : styleCodes) {
			text = text.replaceAll(sectionMark + styleCode, "\\\\" + styleCode);
		}
		text = text.replaceAll(sectionMark, "");
		text = text.replaceAll("\\\\\\\\", sectionMark + sectionMark);
		for(String styleCode : styleCodes) {
			text = text.replaceAll("\\\\" + styleCode, sectionMark + styleCode);
		}
		text = text.replaceAll("\\\\", "");
		text = text.replaceAll(sectionMark + sectionMark, "\\\\");
		return text;
	}
}