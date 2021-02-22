package org.mwage.mcPlugin.chat_command;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
public class AutoyoListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player joinPlayer = event.getPlayer();
		Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
		for(Player player : players) {
			if(!player.equals(joinPlayer)) {
				MWC_PlayerSettings.readyPlayer(player);
				MWC_PlayerSetting setting = MWC_PlayerSettings.get(player);
				if(setting.autoyo) {
					player.chat("yooo！（服务器娘注，这是玩家开启的自动招呼）");
				}
			}
		}
	}
}