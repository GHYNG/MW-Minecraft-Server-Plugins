package org.mwage.mcPlugin.chat_command;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
public class MWC_PlayerSetting implements Main_GeneralMethods {
	public final Player player;
	public boolean autoyo = false;
	public final Map<String, Location> locations = new HashMap<String, Location>();
	public MWC_PlayerSetting(Player player) {
		this.player = player;
	}
	/*
	 * Get information about stored locations.
	 */
	public String listLocations() {
		Set<String> keys = locations.keySet();
		List<Object> lines = new ArrayList<Object>();
		for(String key : keys) {
			Location location = locations.get(key);
			if(location == null) {
				continue;
			}
			else {
				String worldName = location.getWorld().getName();
				int x = (int)location.getX(), y = (int)location.getY(), z = (int)location.getZ();
				String line = line("    ", key, " - ", worldName, ": (", x, ", ", y, ", ", z, ")");
				lines.add(line);
			}
		}
		return pageList(lines);
	}
}