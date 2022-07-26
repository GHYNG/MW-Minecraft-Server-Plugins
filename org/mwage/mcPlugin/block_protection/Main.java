package org.mwage.mcPlugin.block_protection;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new ExplosionListener(), this);
		Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
	}
}
class ExplosionListener implements Listener {
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent event) {
		List<Block> blocks = event.blockList();
		event.setYield(0);
		blocks.clear();
	}
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		List<Block> blocks = event.blockList();
		event.setYield(0);
		blocks.clear();
	}
	@EventHandler
	public void onHangingBreak(HangingBreakEvent event) {
		switch(event.getCause()) {
			case DEFAULT :
			case ENTITY :
				return;
			default :
				event.setCancelled(true);
		}
	}
}
class EntityListener implements Listener {
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		Entity entity = event.getEntity();
		if(entity == null) {
			return;
		}
		if(entity instanceof Player) {
			return;
		}
		if(entity instanceof AbstractVillager || entity instanceof Snowman) {
			return;
		}
		if(entity instanceof FallingBlock) {
			return;
		}
		if(entity instanceof Animals) {
			return;
		}
		event.setCancelled(true);
	}
}
class PlayerListener implements Listener {
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event) {
		Block block = event.getBlock();
		Location location = block.getLocation().toCenterLocation();
		Collection<Entity> entities = location.getNearbyEntities(0.5, 0.5, 0.5);
		entities.forEach((entity) -> {
			if(entity instanceof ItemFrame) {
				Player player = event.getPlayer();
				player.sendMessage("展示框所在的位置上无法放置方块！");
				event.setCancelled(true);
				return;
			}
		});
	}
}