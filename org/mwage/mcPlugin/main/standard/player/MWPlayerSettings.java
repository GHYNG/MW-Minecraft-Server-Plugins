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
 * һ�����й�����������趨���ࡣ
 * ���µ���Ҽ���ʱ�����Զ����������ҵ��趨������ڴ���û�������趨�Ļ�����
 * 
 * @author GHYNG
 * @param <S>
 *            ���浥������趨����
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
	 * ׼����һ����ҵ��趨��
	 * 
	 * @param player
	 *            ָ������ҡ�
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
	 * ׼����������ҵ��趨��
	 */
	public void readyPlayers() {
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		for(Player player : onlinePlayers) {
			readyPlayer(player);
		}
	}
	/**
	 * ��ȡָ����ҵ��趨��
	 * 
	 * @param player
	 *            ָ����ҡ�
	 * @return ����趨��
	 */
	public S get(Player player) {
		readyPlayer(player);
		return SETTINGS.get(player.getUniqueId());
	}
	/**
	 * ��ȡָ����ҵ��趨��
	 * 
	 * @param uuid
	 *            ��ҵ�uuid��
	 * @return ����趨��
	 */
	public S get(UUID uuid) {
		return SETTINGS.get(uuid);
	}
	/**
	 * ��ʼ������趨��
	 * 
	 * @param player
	 *            ָ����ҡ�
	 * @return ��ҵ�һ���µ��趨��
	 */
	public abstract S generatePlayerSetting(Player player);
	/**
	 * ��ʼ������趨��
	 * 
	 * @param uuid
	 *            ָ����ҵ�uuid��
	 * @return ��ҵ�һ���µ��趨��
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