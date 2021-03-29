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
		name_title.put(MAIN_WORLD_NAME, MAIN_WORLD_TITLE);
		name_title.put(MAIN_NETHER_WORLD_NAME, MAIN_NETHER_WORLD_TITLE);
		name_title.put(MAIN_END_WORLD_NAME, MAIN_END_WORLD_TITLE);
		name_title.put(RESOURCE_WORLD_NAME, RESOURCE_WORLD_TITLE);
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		String name = name_title.get(world.getName());
		name = name == null ? world.getName() : name;
		name = ChatColor.YELLOW + name;
		player.sendTitle(name, ChatColor.YELLOW + "与世界同步完成， 武运昌隆！");
		if(world.getName().equalsIgnoreCase(RESOURCE_WORLD_NAME)) {
			String line0 = ChatColor.GOLD + "欢迎来到资源世界， 请不要不设防护的垂直挖矿哦。";
			player.sendMessage(line0);
		}
	}
}