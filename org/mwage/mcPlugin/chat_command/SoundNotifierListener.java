package org.mwage.mcPlugin.chat_command;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
@SuppressWarnings("deprecation")
public class SoundNotifierListener implements Listener {
	private final Main plugin;
	private Collection<? extends Player> players = Bukkit.getOnlinePlayers();
	public SoundNotifierListener(Main plugin) {
		this.plugin = plugin;
	}
	public enum EventType {
		PLAYER_JOIN("PlayerJoin"),
		PLAYER_QUIT("PlayerQuit"),
		PLAYER_DEATH("PlayerDeath"),
		PLAYER_CHAT("PlayerChat");
		public final String name;
		EventType(String name) {
			this.name = name;
		}
		public static EventType get(String name) {
			name = name.toLowerCase();
			EventType[] types = EventType.values();
			for(EventType type : types) {
				if(type.name.toLowerCase().equals(name)) {
					return type;
				}
			}
			return null;
		}
	}
	public enum SoundType {
		PIANO("Piano"),
		NULL("Null");
		public final String name;
		SoundType(String name) {
			this.name = name;
		}
		public static SoundType get(String name) {
			name = name.toLowerCase();
			SoundType[] types = SoundType.values();
			for(SoundType type : types) {
				if(type.name.toLowerCase().equals(name)) {
					return type;
				}
			}
			return null;
		}
	}
	private void parseEvent(Event event) {
		if(event instanceof PlayerJoinEvent) {
			ringEvent(EventType.PLAYER_JOIN);
		}
		if(event instanceof PlayerQuitEvent) {
			ringEvent(EventType.PLAYER_QUIT);
		}
		if(event instanceof PlayerDeathEvent) {
			ringEvent(EventType.PLAYER_DEATH);
		}
		if(event instanceof PlayerChatEvent) {
			ringEvent(EventType.PLAYER_CHAT);
		}
	}
	@EventHandler
	public void onEvent(PlayerJoinEvent event) {
		parseEvent(event);
	}
	@EventHandler
	public void onEvent(PlayerQuitEvent event) {
		parseEvent(event);
	}
	@EventHandler
	public void onEvent(PlayerDeathEvent event) {
		parseEvent(event);
	}
	@EventHandler
	public void onEvent(PlayerChatEvent event) {
		parseEvent(event);
	}
	public void ringEvent(EventType type) {
		MWC_PlayerSettings settings = plugin.getPlayerSettings();
		for(Player player : players) {
			MWC_PlayerSetting setting = settings.get(player);
			SoundType st = setting.sound_notifications.get(type);
			if(st != null) {
				ringSound(player, st);
			}
		}
	}
	public void ringSound(Player player, SoundType type) {
		if(type == null) {
			return;
		}
		Location location = player.getLocation();
		switch(type) {
			case PIANO :
				player.playSound(location, Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.PLAYERS, 0.8f, 1.2f);
				break;
			case NULL :
				break; // does nothing
			default :
				break;
		}
	}
}