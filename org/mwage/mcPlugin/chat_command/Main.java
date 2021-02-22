package org.mwage.mcPlugin.chat_command;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
@SuppressWarnings("deprecation")
public class Main extends JavaPlugin {
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new MainListener(), this);
	}
	public void readyAllOnlinePlayers() {
		Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		for(Player onlinePlayer : onlinePlayers) {
			MWC_PlayerSettings.readyPlayer(onlinePlayer);
		}
	}
}
class MainListener implements Listener, Main_GeneralMethods, MWC_GeneralMethods {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		MWC_PlayerSettings.readyPlayer(player);
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		String message = event.getMessage();
		if(!message.startsWith("mwc")) {
			return;
		}
		message = message.replaceAll("  ", " ");
		String[] subCommands = message.split(" ");
		Player player = event.getPlayer();
		MWCEvent mwcevent = new MWCEvent(player, subCommands);
		server().getPluginManager().callEvent(mwcevent);
		if(mwcevent.isCancelled()) {
			return;
		}
		else {
			parseEvent(mwcevent);
		}
	}
	public void parseEvent(MWCEvent event) {
		Player player = event.getPlayer();
		if(!isOwner(player)) {
			mwcNotAllowed(player);
			secho(player, "�ߣ���û��mwc��Ȩ�ޣ�");
			return;
		}
		MWC_PlayerSettings.readyPlayer(player);
		MWC_PlayerSetting setting = MWC_PlayerSettings.get(player);
		List<String> commands = event.getCommandParts();
		int size = commands.size();
		if(size > 0) {
			String c0 = commands.get(0);
			if(c0.equalsIgnoreCase("opme")) {
				if(player.isOp()) {
					mwcGood(player);
					sechoOwner(player, "���Ѿ���OP�ˣ�");
				}
				else {
					player.setOp(true);
					mwcGood(player);
					sechoOwner(player, "���Ѿ��ָ�ΪOP��");
				}
			}
			else if(c0.equalsIgnoreCase("deop")) {
				if(!player.isOp()) {
					mwcGood(player);
					sechoOwner(player, "���Ѿ�����OP�ˣ�");
				}
				else {
					player.setOp(false);
					mwcGood(player);
					sechoOwner(player, "���Ѿ����OP��");
				}
			}
			else if(c0.equalsIgnoreCase("autoyo")) {
				if(size > 1) {
					String c1 = commands.get(1);
					if(c1.equalsIgnoreCase("true")) {
						setting.autoyo = true;
						mwcGood(player);
						sechoOwner(player, "������autoyo��������Ŷ~��Ҫ�Ұ������к�~");
					}
					else if(c1.equalsIgnoreCase("false")) {
						setting.autoyo = false;
						mwcGood(player);
						sechoOwner(player, "���ر���autoyo����Ȼ���к������»����������ȽϺ���~");
					}
					else {
						mwcBad(player);
						sechoOwner(player, "�����ָ������ �òˣ�");
					}
				}
				else {
					String line0 = "mwc autoyo ָ��˵��";
					String line1 = "  true - ��auto yooo";
					String line2 = "  false - �ر�auto yooo";
					String line3 = "  Ĭ��Ϊfalse";
					String page = page(line0, line1, line2, line3);
					echo(player, page);
				}
			}
			else {
				mwcBad(player);
				sechoOwner(player, "�����ָ������ �òˣ�");
			}
		}
		else {
			String line0 = "mwc ָ��˵��";
			String line1 = "  opme - ���Լ���Ϊop";
			String line2 = "  deop - ���Լ����op";
			String line3 = "  autoyo <boolean> - ��Ҽ����ʱ���������Զ�������yooo����";
			String page = page(line0, line1, line2, line3);
			echo(player, page);
		}
	}
	public void echo(Player player, Object message) {
		player.sendMessage("" + message);
	}
	public void sechoOwner(Player player, Object message) {
		serverSay(line("����", player.getName(), "��", message));
	}
	public void secho(Player player, Object message) {
		serverSay(line(player.getName(), "��", message));
	}
	public void mwcGood(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.8f, 1.2f);
	}
	public void mwcBad(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.ENTITY_CAT_BEG_FOR_FOOD, SoundCategory.PLAYERS, 0.8f, 1.2f);
	}
	public void mwcNotAllowed(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 0.8f, 1.2f);
	}
}