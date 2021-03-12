package org.mwage.mcPlugin.chat_command;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
public class MWC_PlayerSettings {
	private static Map<UUID, MWC_PlayerSetting> SETTINGS = new HashMap<UUID, MWC_PlayerSetting>();
	public static void readyPlayer(Player player) {
		UUID uuid = player.getUniqueId();
		MWC_PlayerSetting setting = SETTINGS.get(uuid);
		if(setting == null) {
			SETTINGS.put(uuid, new MWC_PlayerSetting(player));
		}
	}
	public static MWC_PlayerSetting get(Player player) {
		return SETTINGS.get(player.getUniqueId());
	}
}