package org.mwage.mcPlugin.chat_command;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.mwage.mcPlugin.main.standard.player.MWPlayerSettings;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
public class MWC_PlayerSettings extends MWPlayerSettings<MWC_PlayerSetting> {
	public MWC_PlayerSettings(MWPlugin plugin) {
		super(plugin);
	}
	@Override
	public MWC_PlayerSetting generatePlayerSetting(Player player) {
		return new MWC_PlayerSetting(player);
	}
	@Override
	public MWC_PlayerSetting generatePlayerSetting(UUID arg0) {
		return new MWC_PlayerSetting(arg0);
	}
}