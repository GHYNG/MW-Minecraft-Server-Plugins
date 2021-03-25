package org.mwage.mcPlugin.vote;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.mwage.mcPlugin.main.standard.player.MWPlayerSettings;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
public class VotePlayerSettings extends MWPlayerSettings<VotePlayerSetting> {
	public VotePlayerSettings(MWPlugin plugin) {
		super(plugin);
	}
	@Override
	public VotePlayerSetting generatePlayerSetting(Player arg0) {
		return new VotePlayerSetting(arg0);
	}
	@Override
	public VotePlayerSetting generatePlayerSetting(UUID arg0) {
		return new VotePlayerSetting(arg0);
	}
}
