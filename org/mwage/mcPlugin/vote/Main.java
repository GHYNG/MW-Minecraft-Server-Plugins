package org.mwage.mcPlugin.vote;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServerCommandEvent;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
public class Main extends MWPlugin implements Main_GeneralMethods {
	private VotePlayerSettings playerSettings;
	@Override
	public void onEnable() {
		playerSettings = new VotePlayerSettings(this);
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("vote-ban")) {
			return false;
		}
		int argLength = args.length;
		if(argLength != 1) {
			sender.sendMessage("参数数量错误：唯一参数为玩家id。");
			return false;
		}
		if(!(sender instanceof Player)) {
			sender.sendMessage("只有玩家可以使用这条命令！");
			return false;
		}
		Player target = Bukkit.getServer().getPlayer(args[0]);
		if(target == null) {
			sender.sendMessage("玩家" + args[0] + "目前不在线。");
			return false;
		}
		String targetName = target.getName();
		Player player = (Player)sender;
		VotePlayerSetting setting = playerSettings.get(target);
		if(setting.playersWantMeBanned.contains(player.getUniqueId())) {
			setting.playersWantMeBanned.remove(player.getUniqueId());
			sender.sendMessage("您已经取消对玩家" + targetName + "的投票封禁，如果想要投票封禁，请再次输入本指令。");
		}
		else {
			setting.playersWantMeBanned.add(player.getUniqueId());
			sender.sendMessage("您已经为封禁玩家" + targetName + "投出一票，如果想要取消投票，请再次输入本指令。");
		}
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		int totalSize = onlinePlayers.size();
		int voteSize = setting.playersWantMeBanned.size();
		if(voteSize == 1) {
			serverSay(player.getName() + "提意投票封禁" + targetName + "，如果您也同意，请输入 /vote-ban " + targetName + " 来投票。citizen及以上玩家可以投票。投票成功需要至少一半玩家总人数投票支持。");
		}
		if(voteSize * 2 >= totalSize) {
			banPlayer(target, "您已经被服务器玩家投票封禁。如果您认为这是一次错误的封禁，请加群（541100688）联系管理员。");
			serverSay(targetName + "已经被投票封禁。");
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