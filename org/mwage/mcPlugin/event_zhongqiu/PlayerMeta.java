package org.mwage.mcPlugin.event_zhongqiu;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
public class PlayerMeta {
	public final Main plugin;
	public final UUID uuid;
	public final String name;
	Identity identity = Identity.Observer;
	Identity endIdentity = Identity.Observer;
	public int normal_wolf_countdown = 90;
	public int crazy_wolf_countdown = 90;
	public NormalWolfTimer normalWolfTimer = new NormalWolfTimer();
	public CrazyWolfTimer crazyWolfTimer = new CrazyWolfTimer();
	public PlayerMeta(Main plugin, UUID uuid) {
		this.plugin = plugin;
		this.uuid = uuid;
		normal_wolf_countdown = plugin.normal_wolf_countdown;
		crazy_wolf_countdown = plugin.crazy_wolf_countdown;
		Entity entity = Bukkit.getEntity(uuid);
		name = entity.getName();
	}
	public PlayerMeta(Main plugin, Player player) {
		this(plugin, player.getUniqueId());
	}
	class NormalWolfTimer extends BukkitRunnable {
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			// Bukkit.broadcastMessage("" + normal_wolf_countdown);
			Player player = getPlayer();
			if(player == null) {
				identity = Identity.Observer;
				cancel();
				return;
			}
			if(normal_wolf_countdown <= 0) {
				plugin.crazilize(player);
				cancel();
				return;
			}
			String title = ChatColor.RED + "即将狂躁化";
			String subTitle = "距离您狂躁化还有" + normal_wolf_countdown + "秒";
			if(normal_wolf_countdown == 10 || normal_wolf_countdown == 30 || normal_wolf_countdown == 60) {
				player.sendTitle(title, subTitle);
			}
			if(normal_wolf_countdown < 10) {
				String message = "狂躁化倒计时：" + normal_wolf_countdown;
				player.sendMessage(message);
			}
			int dexp = normal_wolf_countdown - player.getLevel();
			player.giveExpLevels(dexp);
			normal_wolf_countdown--;
		}
	}
	class CrazyWolfTimer extends BukkitRunnable {
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			// Bukkit.broadcastMessage("" + crazy_wolf_countdown);
			Player player = getPlayer();
			if(player == null) {
				identity = Identity.Observer;
				cancel();
				return;
			}
			if(crazy_wolf_countdown <= 0) {
				plugin.killPlayer(player, DeathReason.BloodBurn);
				cancel();
				return;
			}
			String title = ChatColor.RED + "即将血液沸腾";
			String subTitle = "距离您血液沸腾还有" + crazy_wolf_countdown + "秒";
			if(crazy_wolf_countdown == 10 || crazy_wolf_countdown == 30 || crazy_wolf_countdown == 60) {
				player.sendTitle(title, subTitle);
			}
			if(crazy_wolf_countdown < 10) {
				String message = "血液沸腾倒计时：" + crazy_wolf_countdown;
				player.sendMessage(message);
			}
			int dexp = crazy_wolf_countdown - player.getLevel();
			player.giveExpLevels(dexp);
			crazy_wolf_countdown--;
		}
	}
	private Player getPlayer() {
		for(Player player : plugin.getGamePlayers()) {
			UUID u = player.getUniqueId();
			if(uuid.equals(u)) {
				return player;
			}
		}
		return null;
	}
}
