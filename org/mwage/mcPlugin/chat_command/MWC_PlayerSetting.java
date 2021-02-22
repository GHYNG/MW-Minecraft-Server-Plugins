package org.mwage.mcPlugin.chat_command;
import org.bukkit.entity.Player;
public class MWC_PlayerSetting {
	public final Player player;
	public boolean autoyo = false;
	public MWC_PlayerSetting(Player player) {
		this.player = player;
	}
}