package org.mwage.mcPlugin.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin implements Main_GeneralMethods {
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new MainListener(), this);
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
		serverSay(line("��ӭ��", name, "��"));
	}
	@SuppressWarnings("deprecation")
	public void sendWelcomeTitle(Player player) {
		String title = ChatColor.AQUA + "��ӭ����Mwage��·����أ�";
		String subTitle = ChatColor.DARK_PURPLE + "Ը�Ǻ�����ͬ�ڣ�" + player.getName();
		player.sendTitle(title, subTitle);
	}
	public void sendWelcomeMessage(Player player) {
		String line0 = ChatColor.AQUA + "��ӭ������·��������";
		String line1 = ChatColor.AQUA + "�����ر����ѣ��벻Ҫ���������ڿ�Ŷ~";
		String line2 = ChatColor.AQUA + "������ڿ󣬿���ͨ��/warp mineȥ��磨��Դ�磩�ڿ�Ŷ��";
		player.sendMessage(page(line0, line1, line2));
	}
	public void sendWelcomeSound(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.8f, 1.2f);
	}
}