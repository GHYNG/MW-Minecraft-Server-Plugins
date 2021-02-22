package org.mwage.mcPlugin.chat_command;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
public class MWC_PlayerSettings {
	public static Map<Player, MWC_PlayerSetting> SETTINGS = new HashMap<Player, MWC_PlayerSetting>();
	public static void readyPlayer(Player player) {
		MWC_PlayerSetting setting = SETTINGS.get(player);
		if(setting == null) {
			SETTINGS.put(player, new MWC_PlayerSetting(player));
		}
	}
	public static MWC_PlayerSetting get(Player player) {
		return SETTINGS.get(player);
	}
}