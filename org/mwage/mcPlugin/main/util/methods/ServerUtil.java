package org.mwage.mcPlugin.main.util.methods;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.server.ServerCommandEvent;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public interface ServerUtil {
	/**
	 * 使服务器发出聊天信息。
	 * 
	 * @param message
	 *            聊天信息。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0, currentlyAt = 1))
	default void serverSay(Object message) {
		serverCommand("say " + message);
	}
	/**
	 * 使服务器通过{@code /me}指令发出信息。
	 * 
	 * @param message
	 *            信息。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	default void serverMe(Object message) {
		serverCommand("me " + message);
	}
	/**
	 * 使服务器向某个玩家私聊一段信息。
	 * 
	 * @param player
	 *            私聊对象。
	 * @param message
	 *            信息。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	default void serverTell(Player player, Object message) {
		String name = player.getName();
		String command = "tell " + name + " " + message;
		serverCommand(command);
	}
	/**
	 * 使服务器终端发出指令。
	 * 这个方法将会自行调用{@link org.bukkit.event.server.ServerCommandEvent}事件。
	 * 
	 * @param command
	 *            需要发出的指令。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	default void serverCommand(String command) {
		CommandSender sender = Bukkit.getConsoleSender();
		ServerCommandEvent event = new ServerCommandEvent(sender, command);
		callEvent(event);
		if(event.isCancelled()) {
			return;
		}
		sender = event.getSender();
		command = event.getCommand();
		Bukkit.dispatchCommand(sender, command);
	}
	/**
	 * 使服务器调用事件。
	 * 相当于{@code Bukkit.getServer().getPluginManager().callEvent(event);}。
	 * 
	 * @param event
	 *            调用的事件。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	default void callEvent(Event event) {
		Bukkit.getServer().getPluginManager().callEvent(event);
	}
}