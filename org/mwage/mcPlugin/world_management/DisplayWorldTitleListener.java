package org.mwage.mcPlugin.world_management;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
public class DisplayWorldTitleListener implements Listener, MWWorldData {
	private final Map<String, String> name_title = new HashMap<String, String>();
	{
		registerWorldTitle(MAIN_WORLD_NAME, MAIN_WORLD_TITLE);
		registerWorldTitle(MAIN_NETHER_WORLD_NAME, MAIN_NETHER_WORLD_TITLE);
		registerWorldTitle(MAIN_END_WORLD_NAME, MAIN_END_WORLD_TITLE);
		registerWorldTitle(RESOURCE_WORLD_NAME, RESOURCE_WORLD_TITLE);
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		String name = getWorldTitle(world.getName());
		name = ChatColor.YELLOW + name;
		player.sendTitle(name, ChatColor.YELLOW + "与世界同步完成， 武运昌隆！");
		if(world.getName().equalsIgnoreCase(RESOURCE_WORLD_NAME)) {
			String line0 = ChatColor.GOLD + "欢迎来到资源世界， 请不要不设防护的垂直挖矿哦。";
			player.sendMessage(line0);
		}
	}
	public void registerWorldTitle(String worldName, String worldTitle) {
		if(worldName == null) {
			return;
		}
		name_title.put(worldName.toLowerCase(), worldTitle);
	}
	public String getWorldTitle(String worldName) {
		String title = name_title.get(worldName.toLowerCase());
		return title == null ? worldName : title;
	}
}