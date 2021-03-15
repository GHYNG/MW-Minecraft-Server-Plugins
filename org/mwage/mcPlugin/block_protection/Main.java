package org.mwage.mcPlugin.block_protection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new ExplosionListener(), this);
		Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
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
			case EXPLOSION :
				event.setCancelled(true);
				return;
			default :
				return;
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