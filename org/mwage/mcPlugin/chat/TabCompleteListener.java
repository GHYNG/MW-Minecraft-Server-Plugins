package org.mwage.mcPlugin.chat;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
/**
 * This does not work at all after 1.13.
 * 
 * @author GHYNG
 */
@Deprecated
public class TabCompleteListener implements Listener, Main_GeneralMethods {
	@EventHandler
	public void onTabComplete(AsyncTabCompleteEvent event) {
		Bukkit.broadcastMessage("TabCompleteEvent Called");
		if(and(not(event.isCommand()), event.getSender() instanceof Player)) {
			List<String> tabCompletes = event.getCompletions();
			Collection<? extends Player> players = Bukkit.getOnlinePlayers();
			for(Player player : players) {
				tabCompletes.add("@" + player.getName());
				Bukkit.broadcastMessage(player.getName());
			}
			event.setCompletions(tabCompletes);
		}
	}
}