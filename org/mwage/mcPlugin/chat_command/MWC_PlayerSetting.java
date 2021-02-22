package org.mwage.mcPlugin.chat_command;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.entity.Player;
public class MWC_PlayerSetting {
	public final Player player;
	public boolean autoyo = false;
	Map<String, Location> locations = new HashMap<String, Location>();
	public MWC_PlayerSetting(Player player) {
		this.player = player;
	}
}