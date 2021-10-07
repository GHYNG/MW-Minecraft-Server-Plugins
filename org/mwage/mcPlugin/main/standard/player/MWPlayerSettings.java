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
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
/**
 * 一个集中管理所有玩家设定的类。
 * 当新的玩家加入时，会自动产生这个玩家的设定（如果内存中没有他的设定的话）。
 * 
 * @author GHYNG
 * @param <S>
 *            储存单个玩家设定的类
 * @deprecated 需要用更好的名称重命名。
 */
@Deprecated
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
public abstract class MWPlayerSettings<S extends MWPlayerSetting> {
	/**
	 * The plugin which uses this settings.
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	private final MWPlugin plugin;
	/**
	 * Settings are stored into a hash map.
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	private Map<UUID, S> SETTINGS = new HashMap<UUID, S>();
	/**
	 * 产生一个新的管理所有在线玩家设定的对象。
	 * 这个对象必须在所属的插件enabled之后建立。
	 * 
	 * @param plugin
	 *            这个设定管理对象所属的插件对象。
	 * @throws CreationBeforeEnabledException
	 *             如果在插件enabled之前创建此对象，报错。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	public MWPlayerSettings(MWPlugin plugin) throws CreationBeforeEnabledException {
		if(!plugin.isEnabled()) {
			throw new CreationBeforeEnabledException("PlayerSettings class must be created after plugin object is enabled.");
		}
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
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
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
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
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
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
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
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
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
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	public abstract S generatePlayerSetting(Player player);
	/**
	 * 初始化玩家设定。
	 * 
	 * @param uuid
	 *            指定玩家的uuid。
	 * @return 玩家的一个新的设定。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	public abstract S generatePlayerSetting(UUID uuid);
}
/**
 * 设定监听器。
 * 已经过时。
 * 该类名未来可能被重复使用。
 * 
 * @author GHYNG
 * @deprecated 不再使用，未来会被替代。
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0, openToSubPlugin = false))
@Deprecated
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