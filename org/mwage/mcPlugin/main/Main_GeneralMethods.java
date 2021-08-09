package org.mwage.mcPlugin.main;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.server.ServerCommandEvent;
import org.mwage.mcPlugin.main.standard.events.ServerChatEvent;
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
	default String lineList(List<Object> parts) {
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
	default String pageList(List<Object> lines) {
		int length = lines.size();
		if(length == 0) {
			return "";
		}
		String page = "";
		for(int i = 0; i < length - 1; i++) {
			page += lines.get(i) + "\n";
		}
		page += lines.get(length - 1);
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
		CommandSender sender = Bukkit.getConsoleSender();
		String command = "say " + message;
		ServerCommandEvent event = new ServerCommandEvent(Bukkit.getConsoleSender(), command);
		callEvent(event);
		if(!event.isCancelled()) {
			command = event.getCommand();
			if(command.startsWith("say ")) {
				message = command.substring("say ".length()) + "";
				ServerChatEvent event1 = new ServerChatEvent(message);
				callEvent(event1);
				if(!event1.isCancelled()) {
					message = event1.getMessage();
					command = "say " + message;
					Bukkit.dispatchCommand(sender, command);
				}
			}
		}
	}
	default <T> List<T> getNewList() {
		List<T> list = new ArrayList<T>();
		return list;
	}
	default boolean goodIdentifier(String identifier) {
		if(identifier == null || identifier.length() < 1) {
			return false;
		}
		int index = 0;
		if(Character.isJavaIdentifierStart(identifier.charAt(index))) {
			while(++index < identifier.length()) {
				if(!Character.isJavaIdentifierPart(identifier.charAt(index))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	default void callEvent(Event event) {
		Bukkit.getServer().getPluginManager().callEvent(event);
	}
}