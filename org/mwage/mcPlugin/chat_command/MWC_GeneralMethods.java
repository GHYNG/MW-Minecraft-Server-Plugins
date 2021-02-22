package org.mwage.mcPlugin.chat_command;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
public interface MWC_GeneralMethods {
	/*
	 * See if given player is owner of milkyway server.
	 */
	default boolean isOwner(Player player) {
		String name = player.getName();
		return(name.equals("GHYNG") || name.equals("MWQJDOR") || name.equals("Antrooper"));
	}
	/*
	 * Get server, this method will be moved to main plugin.
	 */
	default Server server() {
		return Bukkit.getServer();
	}
}