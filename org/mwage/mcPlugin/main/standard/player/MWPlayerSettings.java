package org.mwage.mcPlugin.main.standard.player;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
/**
 * 一个集中管理所有玩家设定的类。
 * 当新的玩家加入时，会自动产生这个玩家的设定（如果内存中没有他的设定的话）。
 * 
 * @author GHYNG
 * @param <S>
 *            储存单个玩家设定的类
 */
public abstract class MWPlayerSettings<S extends MWPlayerSetting> {
	/**
	 * The plugin which uses this settings.
	 */
	private final MWPlugin plugin;
	/**
	 * Settings are stored into a hash map.
	 */
	private Map<UUID, S> SETTINGS = new HashMap<UUID, S>();
	public MWPlayerSettings(MWPlugin plugin) {
		this.plugin = plugin;
		this.plugin.registerListener(new SettingListener(this));
		readyPlayers();
	}
	/**
	 * 准备好一个玩家的设定。
	 * 
	 * @param player
	 *            指定的玩家。
	 */
	public void readyPlayer(Player player) {
		UUID uuid = player.getUniqueId();
		S setting = SETTINGS.get(uuid);
		if(setting == null) {
			setting = generatePlayerSetting(player);
			SETTINGS.put(uuid, setting);
		}
	}
	/**
	 * 准备好所有玩家的设定。
	 */
	public void readyPlayers() {
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		for(Player player : onlinePlayers) {
			readyPlayer(player);
		}
	}
	/**
	 * 获取指定玩家的设定。
	 * 
	 * @param player
	 *            指定玩家。
	 * @return 玩家设定。
	 */
	public S get(Player player) {
		readyPlayer(player);
		return SETTINGS.get(player.getUniqueId());
	}
	/**
	 * 获取指定玩家的设定。
	 * 
	 * @param uuid
	 *            玩家的uuid。
	 * @return 玩家设定。
	 */
	public S get(UUID uuid) {
		return SETTINGS.get(uuid);
	}
	/**
	 * 初始化玩家设定。
	 * 
	 * @param player
	 *            指定玩家。
	 * @return 玩家的一个新的设定。
	 */
	public abstract S generatePlayerSetting(Player player);
	/**
	 * 初始化玩家设定。
	 * 
	 * @param uuid
	 *            指定玩家的uuid。
	 * @return 玩家的一个新的设定。
	 */
	public abstract S generatePlayerSetting(UUID uuid);
}
class SettingListener implements Listener {
	final MWPlayerSettings<?> settings;
	SettingListener(MWPlayerSettings<?> settings) {
		this.settings = settings;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		settings.readyPlayer(event.getPlayer());
	}
}