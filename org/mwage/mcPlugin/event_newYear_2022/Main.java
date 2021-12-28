package org.mwage.mcPlugin.event_newYear_2022;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
	}
}
class BlockListener implements Listener {
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if(player.isOp()) {
			return;
		}
		Block block = event.getBlockPlaced();
		Material material = block.getType();
		switch(material) {
			case BARRIER :
				event.setCancelled(true);
				return;
			default :
				break;
		}
	}
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(player.isOp()) {
			return;
		}
		Block block = event.getBlock();
		Material material = block.getType();
		switch(material) {
			case BARRIER :
				event.setCancelled(true);
				return;
			default :
				break;
		}
	}
}