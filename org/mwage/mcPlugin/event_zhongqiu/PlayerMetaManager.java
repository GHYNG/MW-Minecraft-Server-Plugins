package org.mwage.mcPlugin.event_zhongqiu;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
public class PlayerMetaManager {
	final Map<UUID, PlayerMeta> playerMetas = new HashMap<UUID, PlayerMeta>();
	private final Main plugin;
	public PlayerMetaManager(Main plugin) {
		this.plugin = plugin;
	}
	public PlayerMeta register(Player player) {
		return register(player.getUniqueId());
	}
	public PlayerMeta register(UUID uuid) {
		PlayerMeta meta = playerMetas.get(uuid);
		if(meta == null) {
			meta = new PlayerMeta(plugin, uuid);
			playerMetas.put(uuid, meta);
		}
		return meta;
	}
	public void clear() {
		Set<UUID> keys = playerMetas.keySet();
		for(UUID key : keys) {
			PlayerMeta meta = playerMetas.get(key);
			if(meta != null) {
				try {
					meta.normalWolfTimer.cancel();
				}
				catch(IllegalStateException e) {
				}
				try {
					meta.crazyWolfTimer.cancel();
				}
				catch(IllegalStateException e) {
				}
			}
		}
		playerMetas.clear();
	}
}