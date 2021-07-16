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
import org.bukkit.plugin.java.JavaPlugin;
interface MWWorldData {
	public static final String RESOURCE_WORLD_NAME = "mine3";
	public static final String RESOURCE_WORLD_TITLE = "矿界";
	public static final String MAIN_WORLD_NAME = "world";
	public static final String MAIN_WORLD_TITLE = "主世界";
	public static final String MAIN_NETHER_WORLD_NAME = "world_nether";
	public static final String MAIN_NETHER_WORLD_TITLE = "下界";
	public static final String MAIN_END_WORLD_NAME = "world_the_end";
	public static final String MAIN_END_WORLD_TITLE = "末界";
}
public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new SpawnListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new DisplayWorldTitleListener(), this);
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