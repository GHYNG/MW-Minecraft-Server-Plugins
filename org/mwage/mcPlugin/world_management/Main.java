package org.mwage.mcPlugin.world_management;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
interface MWWorldData {
	public static final String RESOURCE_WORLD_NAME = "mine2";
	public static final String MAIN_WORLD_NAME = "world";
}
public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new SpawnListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
	}
}
class MainListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
	}
}
class SpawnListener implements Listener, MWWorldData {
	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event) {
		Location location = event.getLocation();
		World world = location.getWorld();
		Entity entity = event.getEntity();
		CreatureSpawnEvent.SpawnReason spawnReason = event.getSpawnReason();
		if(world.getName().equalsIgnoreCase(RESOURCE_WORLD_NAME) && entity instanceof Monster) {
			if(spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL) {
				event.setCancelled(true);
			}
		}
	}
}
class PlayerListener implements Listener, MWWorldData {
	private Map<Player, Integer> playerMineCount = new HashMap<Player, Integer>();
	private Set<Location> mainOreLocations = new HashSet<Location>();
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		String name = ChatColor.YELLOW + world.getName();
		player.sendTitle(name, ChatColor.YELLOW + "与世界同步完成， 武运昌隆！");
		if(world.getName().equalsIgnoreCase(RESOURCE_WORLD_NAME)) {
			String line0 = ChatColor.GOLD + "欢迎来到资源世界， 请不要不设防护的垂直挖矿哦。";
			player.sendMessage(line0);
		}
	}
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event) {
		Block block = event.getBlock();
		World world = block.getWorld();
		if(world.getName().equalsIgnoreCase(MAIN_WORLD_NAME) && isOre(block)) {
			mainOreLocations.add(block.getLocation());
		}
	}
	@EventHandler
	public void onPlayerMine(BlockBreakEvent event) {
		Block block = event.getBlock();
		World world = block.getWorld();
		if(world.getName().equalsIgnoreCase(MAIN_WORLD_NAME) && isOre(block)) {
			if(mainOreLocations.contains(block.getLocation())) {
				return;
			}
			Player player = event.getPlayer();
			GameMode gm = player.getGameMode();
			if(gm != GameMode.SURVIVAL) {
				return;
			}
			Integer count = playerMineCount.get(player);
			if(count == null) {
				count = 1;
				playerMineCount.put(player, count);
			}
			else {
				count++;
				playerMineCount.put(player, count);
			}
			if(count % 5 == 0) {
				String line0 = ChatColor.GOLD + player.getName() + "， 您正在主世界挖矿！";
				String line1 = ChatColor.GOLD + "自从上次服务器重启后， 您已经在主世界挖了" + count + "个矿方块了！";
				String line2 = ChatColor.GOLD + "请不要在主世界继续挖矿了！";
				String line3 = ChatColor.AQUA + "如果您在破坏自己放下的矿方块， 可以无视这条信息。";
				player.sendMessage(lines(line0, line1, line2, line3));
				warmOpPlayerMine(player);
			}
		}
	}
	public boolean isOre(Block block) {
		Material material = block.getType();
		switch(material) {
			case GOLD_ORE :
			case IRON_ORE :
			case COAL_ORE :
				// case NETHER_GOLD_ORE :
			case DIAMOND_ORE :
			case LAPIS_ORE :
			case REDSTONE_ORE :
			case EMERALD_ORE :
				// case NETHER_QUARTZ_ORE :
				return true;
			default :
				return false;
		}
	}
	public void warmOpPlayerMine(Player player) {
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		for(Player onlinePlayer : onlinePlayers) {
			if(onlinePlayer.isOp()) {
				onlinePlayer.sendMessage(player.getName() + "正在主世界挖矿");
			}
		}
	}
	public static String lines(String... lines) {
		int length = lines.length;
		if(length == 0) {
			return "";
		}
		String content = "";
		for(int i = 0; i < length - 1; i++) {
			content += lines[i] + "\n";
		}
		content += lines[length - 1];
		return content;
	}
}