package org.mwage.mcPlugin.chat_command;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
public class AutoyoListener implements Listener, Main_GeneralMethods {
	private final Main plugin;
	public AutoyoListener(Main plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player joinPlayer = event.getPlayer();
		Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
		MWC_PlayerSettings settings = plugin.getPlayerSettings();
		for(Player player : players) {
			if(!player.equals(joinPlayer)) {
				MWC_PlayerSetting setting = settings.get(player);
				if(setting.autoyo) {
					player.chat("yooo������������ע�����Ǹ��������Զ��к���");
					// serverSay(line(player.getName(), "Ҳί���������ʺã�"));
				}
			}
		}
	}
}