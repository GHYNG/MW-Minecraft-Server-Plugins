package org.mwage.mcPlugin.vote;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServerCommandEvent;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
public class Main extends MWPlugin implements Main_GeneralMethods {
	private VotePlayerSettings playerSettings;
	private VoteConfig voteConfig;
	@Override
	public void onEnable() {
		initFiles();
		playerSettings = new VotePlayerSettings(this);
		voteConfig = new VoteConfig(this);
	}
	public void initFiles() {
		List<String> fileNames = new ArrayList<String>();
		fileNames.add("config.yml");
		fileNames.add("lang_zh_cn.yml");
		fileNames.add("lang.yml");
		for(String fileName : fileNames) {
			File file = new File(getDataFolder(), fileName);
			if(!file.exists()) {
				saveResource(fileName, false);
			}
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("vote-ban")) {
			return false;
		}
		VoteConfig.LanguageConfig.VoteLanguageConfig vlc = voteConfig.languageConfig.voteLanguageConfig;
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
		VotePlayerSetting setting = playerSettings.get(target);
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
			serverSay(vlc.convertMessage(vlc.started, senderName, targetName));
		}
		if(voteSize >= totalSize * voteConfig.voteBanPercentage) {
			banPlayer(target, vlc.convertMessage(vlc.you_are_banned, senderName, targetName));
			serverSay(vlc.convertMessage(vlc.player_banned, senderName, targetName));
			setting.playersWantMeBanned.clear();
		}
		return true;
	}
	public void banPlayer(Player player, String reason) {
		CommandSender sender = Bukkit.getConsoleSender();
		String command = "ban " + player.getName() + " " + reason;
		ServerCommandEvent event = new ServerCommandEvent(Bukkit.getConsoleSender(), command);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
			Bukkit.dispatchCommand(sender, command);
		}
	}
}