package org.mwage.mcPlugin.chat_command;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
public class MWCEvent extends PlayerEvent implements Cancellable {
	private boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();
	private List<String> commandParts = new ArrayList<String>();
	public MWCEvent(Player who, String[] subCommands) {
		super(who);
		for(int i = 1; i < subCommands.length; i++) {
			commandParts.add(subCommands[i]);
		}
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
	public List<String> getCommandParts() {
		return commandParts;
	}
}