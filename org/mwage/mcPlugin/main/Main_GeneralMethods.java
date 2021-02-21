package org.mwage.mcPlugin.main;
import org.bukkit.Bukkit;
import org.bukkit.event.server.ServerCommandEvent;
/*
 * This interface contains methods relate to server generally,
 * like broadcast messages.
 * Also contains other tool methods, like those related to String.
 */
public interface Main_GeneralMethods {
	/*
	 * To connect string parts to a new string.
	 */
	default String line(Object... parts) {
		String line = "";
		for(Object part : parts) {
			line += part;
		}
		return line;
	}
	/*
	 * To connect lines to a page, separated by \n.
	 */
	default String page(Object... lines) {
		int length = lines.length;
		if(length == 0) {
			return "";
		}
		String page = "";
		for(int i = 0; i < length - 1; i++) {
			page += lines[i] + "\n";
		}
		page += lines[length - 1];
		return page;
	}
	/*
	 * Logic NOT.
	 */
	default boolean not(boolean b) {
		return !b;
	}
	/*
	 * Logic AND.
	 */
	default boolean and(boolean... bs) {
		boolean r = true;
		for(boolean b : bs) {
			r &= b;
		}
		return r;
	}
	/*
	 * logic OR.
	 */
	default boolean or(boolean... bs) {
		boolean r = false;
		for(boolean b : bs) {
			r |= b;
		}
		return r;
	}
	/*
	 * Logic NAND.
	 */
	default boolean nand(boolean... bs) {
		return not(and(bs));
	}
	/*
	 * Logic NOR.
	 */
	default boolean nor(boolean... bs) {
		return not(or(bs));
	}
	/*
	 * Make the server say things.
	 */
	default void serverSay(String message) {
		String command = "say " + message;
		ServerCommandEvent event = new ServerCommandEvent(Bukkit.getConsoleSender(), command);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
			Bukkit.broadcastMessage(message);
		}
	}
}