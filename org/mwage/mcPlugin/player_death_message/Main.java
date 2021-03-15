package org.mwage.mcPlugin.player_death_message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
	}
}
class PlayerDeathListener implements Listener, Main_GeneralMethods {
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		String name = player.getName();
		Location location = player.getLocation();
		String worldName = location.getWorld().getName();
		int x = (int)location.getX(), y = (int)location.getY(), z = (int)location.getZ();
		serverSay(line(name, "死于了", worldName, "世界，坐标为 (", x, ", ", y, ", ", z, ")"));
		// serverSay(line(name, "死了：",worldName, ": (", x, ", ", y, ", ", z, ")"));
	}
}