package org.mwage.mcPlugin.vote.ban;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.mwage.mcPlugin.main.standard.player.MWPlayerSettings;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
@SuppressWarnings("deprecation")
public class VoteBanPlayerSettings extends MWPlayerSettings<VoteBanPlayerSetting> {
	public VoteBanPlayerSettings(MWPlugin plugin) {
		super(plugin);
	}
	@Override
	public VoteBanPlayerSetting generatePlayerSetting(Player arg0) {
		return new VoteBanPlayerSetting(arg0);
	}
	@Override
	public VoteBanPlayerSetting generatePlayerSetting(UUID arg0) {
		return new VoteBanPlayerSetting(arg0);
	}
}
