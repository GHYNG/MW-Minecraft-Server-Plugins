package org.mwage.mcPlugin.main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
public class Main extends MWPlugin implements Main_GeneralMethods {
	@Override
	public void onEnable() {
		registerListener(new MainListener());
	}
}
class MainListener implements Listener, Main_GeneralMethods {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		sendWelcomeTitle(player);
		sendWelcomeMessage(player);
		sendWelcomeSound(player);
		String name = player.getName();
		serverSay(line("欢迎，", name, "！"));
	}
	@SuppressWarnings("deprecation")
	public void sendWelcomeTitle(Player player) {
		String title = ChatColor.AQUA + "欢迎来到Mwage奶路星起地！";
		String subTitle = ChatColor.DARK_PURPLE + "愿星河与你同在，" + player.getName();
		player.sendTitle(title, subTitle);
	}
	public void sendWelcomeMessage(Player player) {
		String line0 = ChatColor.AQUA + "欢迎来到奶路服务器！";
		String line1 = ChatColor.AQUA + "腐竹特别提醒：请不要在主世界挖矿哦~";
		String line2 = ChatColor.AQUA + "如果想挖矿，可以通过/warp mine去矿界（资源界）挖矿哦！";
		player.sendMessage(page(line0, line1, line2));
	}
	public void sendWelcomeSound(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.8f, 1.2f);
	}
}