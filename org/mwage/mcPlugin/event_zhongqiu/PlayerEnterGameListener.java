package org.mwage.mcPlugin.event_zhongqiu;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
/*
 * The player came in the world after game starts
 */
public class PlayerEnterGameListener implements Listener {
	public final Main plugin;
	PlayerEnterGameListener(Main plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		if(world.getName().equalsIgnoreCase(plugin.EVENT_WORLD_NAME) && plugin.gameStarted) {
			plugin.assignIdentity(player, Identity.Observer);
			player.teleport(plugin.game_start_location);
			player.setGameMode(GameMode.SPECTATOR);
			plugin.gameCounter.addPlayer(player);
			plugin.endgameCounter.addPlayer(player);
		}
		else {
			plugin.gameCounter.removePlayer(player);
			plugin.endgameCounter.removePlayer(player);
		}
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		if(world.getName().equalsIgnoreCase(plugin.EVENT_WORLD_NAME) && plugin.gameStarted) {
			plugin.assignIdentity(player, Identity.Observer);
			player.teleport(plugin.game_start_location);
			player.setGameMode(GameMode.SPECTATOR);
			plugin.gameCounter.addPlayer(player);
			plugin.endgameCounter.addPlayer(player);
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		plugin.gameCounter.removePlayer(player);
		plugin.endgameCounter.removePlayer(player);
	}
}