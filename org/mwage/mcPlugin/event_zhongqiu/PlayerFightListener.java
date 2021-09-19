package org.mwage.mcPlugin.event_zhongqiu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
public class PlayerFightListener implements Listener {
	public final Main plugin;
	public PlayerFightListener(Main plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if(plugin.getGamePlayers().contains(player)) {
			event.setCancelled(true);
			Player killer = player.getKiller();
			if(killer == null) {
				plugin.killPlayer(player, DeathReason.Other);
			}
			else {
				plugin.killPlayer(player, DeathReason.Murdered);
				PlayerMeta meta = plugin.playerMetaManager.register(killer);
				if(meta.identity == Identity.NormalWolf) {
					meta.normal_wolf_countdown += plugin.normal_wolf_countdown;
					String title = ChatColor.RED + "杀戮欲望满足";
					String subTitle = "您获得了额外的" + plugin.normal_wolf_countdown + "秒";
					killer.sendTitle(title, subTitle);
				}
				if(meta.identity == Identity.CrazyWolf) {
					meta.crazy_wolf_countdown += plugin.crazy_wolf_countdown;
					String title = ChatColor.RED + "杀戮欲望满足";
					String subTitle = "您获得了额外的" + plugin.crazy_wolf_countdown + "秒";
					killer.sendTitle(title, subTitle);
				}
			}
		}
	}
	@EventHandler
	public void onPlayerPickUpItem(EntityPickupItemEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Player player) {
			if(plugin.getGamePlayers().contains(player)) {
				PlayerMeta meta = plugin.playerMetaManager.register(player);
				Identity identity = meta.identity;
				if(identity == Identity.CrazyWolf || identity == Identity.Observer) {
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
		Entity shou = event.getEntity();
		Entity gong = event.getDamager();
		if(shou instanceof Player pshou && gong instanceof Player pgong) {
			if(!plugin.gameStarted) {
				if(!pgong.isOp()) {
					event.setCancelled(true);
					return;
				}
			}
			else {
				if(pgong.isOp()) {
					return;
				}
				if(plugin.getGamePlayers().contains(pshou) && plugin.getGamePlayers().contains(pgong)) {
					PlayerMeta shoumeta = plugin.playerMetaManager.register(pshou);
					PlayerMeta gongmeta = plugin.playerMetaManager.register(pgong);
					if(shoumeta.identity == Identity.Observer || gongmeta.identity == Identity.Observer) {
						event.setCancelled(true);
						return;
					}
					if(shoumeta.identity == Identity.CrazyWolf) {
						event.setDamage(6);
						return;
					}
					if(gongmeta.identity == Identity.Villager || gongmeta.identity == Identity.NormalWolf) {
						event.setDamage(8);
						return;
					}
					if(gongmeta.identity == Identity.CrazyWolf) {
						event.setDamage(11);
					}
				}
			}
		}
	}
}