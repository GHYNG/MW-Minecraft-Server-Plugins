package org.mwage.mcPlugin.chat_command;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
public class AutoyoListener implements Listener, Main_GeneralMethods {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player joinPlayer = event.getPlayer();
		Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
		for(Player player : players) {
			if(!player.equals(joinPlayer)) {
				MWC_PlayerSettings.readyPlayer(player);
				MWC_PlayerSetting setting = MWC_PlayerSettings.get(player);
				if(setting.autoyo) {
					serverSay(line(player.getName(), "也委托我向您问好！"));
				}
			}
		}
	}
}