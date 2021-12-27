package org.mwage.mcPlugin.vote.ban;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mwage.mcPlugin.vote.Main;
import org.mwage.mcPlugin.vote.VoteConfig;
import org.mwage.mcPlugin.vote.Vote_GeneralMethods;
public class VoteBanProcessor implements Vote_GeneralMethods {
	public final Main plugin;
	public VoteBanPlayerSettings playerSettings;
	public VoteBanProcessor(Main plugin) {
		this.plugin = plugin;
		playerSettings = new VoteBanPlayerSettings(plugin);
		init();
	}
	protected void init() {
		plugin.registerCommandProcessMethod("vote-ban", (plugin, sender, command, label, args) -> {
			VoteConfig.LanguageConfig.VoteBanLanguageConfig vlc = plugin.voteConfig.languageConfig.voteBanLanguageConfig;
			String senderName = sender.getName();
			int argLength = args.length;
			if(argLength != 1) {
				sender.sendMessage(vlc.convertMessage(vlc.wrong_parameter, senderName, "null"));
				return false;
			}
			if(!(sender instanceof Player)) {
				sender.sendMessage(vlc.convertMessage(vlc.only_player_may_run_this_command, senderName, "null"));
				return false;
			}
			Player target = Bukkit.getServer().getPlayer(args[0]);
			String targetName = args[0];
			if(target == null) {
				sender.sendMessage(vlc.convertMessage(vlc.player_not_found, senderName, targetName));
				return true;
			}
			targetName = target.getName();
			Player player = (Player)sender;
			@SuppressWarnings("deprecation")
			VoteBanPlayerSetting setting = playerSettings.get(target);
			if(setting.playersWantMeBanned.contains(player.getUniqueId())) {
				setting.playersWantMeBanned.remove(player.getUniqueId());
				sender.sendMessage(vlc.convertMessage(vlc.player_cancelled_vote, senderName, targetName));
			}
			else {
				setting.playersWantMeBanned.add(player.getUniqueId());
				sender.sendMessage(vlc.convertMessage(vlc.player_voted, senderName, targetName));;
			}
			Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
			int totalSize = onlinePlayers.size();
			int voteSize = setting.playersWantMeBanned.size();
			if(voteSize == 1) {
				plugin.serverSay(vlc.convertMessage(vlc.started, senderName, targetName));
			}
			if(voteSize >= totalSize * plugin.voteConfig.voteBanPercentage) {
				plugin.banPlayer(target, vlc.convertMessage(vlc.you_are_banned, senderName, targetName));
				plugin.serverSay(vlc.convertMessage(vlc.player_banned, senderName, targetName));
				setting.playersWantMeBanned.clear();
			}
			return true;
		});
	}
}