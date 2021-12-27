package org.mwage.mcPlugin.vote.ban;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.mwage.mcPlugin.main.standard.player.MWPlayerSetting;
@SuppressWarnings("deprecation")
public class VoteBanPlayerSetting extends MWPlayerSetting {
	public final Set<UUID> playersWantMeBanned = new HashSet<UUID>();
	public VoteBanPlayerSetting(Player player) {
		super(player);
	}
	public VoteBanPlayerSetting(UUID uuid) {
		super(uuid);
	}
}
