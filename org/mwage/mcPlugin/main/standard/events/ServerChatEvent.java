package org.mwage.mcPlugin.main.standard.events;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;
/**
 * 当服务器使用{@code say}指令的时候，
 * 或者主插件调用{@link org.mwage.mcPlugin.main.Main_GeneralMethods#serverSay(String)}的时候，
 * 就会发出这个事件。
 * <p>
 * 这个事件在服务器实际发出聊天内容之前被调用。
 * 
 * @author GHYNG
 */
public class ServerChatEvent extends ServerEvent implements Cancellable {
	private boolean cancelled = false;
	private String message = "";
	private static final HandlerList handlers = new HandlerList();
	public ServerChatEvent(String message) {
		this.message = message;
	}
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	/**
	 * 设置这个事件将要发出的消息。
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * 获得这个事件将要发出的消息。
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}
}
