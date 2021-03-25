package org.mwage.mcPlugin.vote;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.mwage.mcPlugin.main.standard.player.MWPlayerSetting;
public class VotePlayerSetting extends MWPlayerSetting {
	public final Set<UUID> playersWantMeBanned = new HashSet<UUID>();
	public VotePlayerSetting(Player player) {
		super(player);
	}
	public VotePlayerSetting(UUID uuid) {
		super(uuid);
	}
}
