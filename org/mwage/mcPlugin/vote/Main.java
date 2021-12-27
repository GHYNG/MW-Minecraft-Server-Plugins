package org.mwage.mcPlugin.vote;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServerCommandEvent;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
import org.mwage.mcPlugin.vote.ban.VoteBanProcessor;
import org.mwage.mcPlugin.vote.normal.VoteNormalManageProcessor;
public class Main extends MWPlugin implements Main_GeneralMethods {
	public VoteConfig voteConfig;
	protected CommandProcessor commandProcessor;
	protected VoteBanProcessor voteBanProcessor;
	protected VoteNormalManageProcessor voteNormalManageProcessor;
	@Override
	public void onEnable() {
		initFiles();
		commandProcessor = new CommandProcessor(this);
		voteBanProcessor = new VoteBanProcessor(this);
		voteNormalManageProcessor = new VoteNormalManageProcessor(this);
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
		if(command.getName().equalsIgnoreCase("vote-ban")) {}
		CommandProcessMethod method = commandProcessor.methods.get(command.getName());
		if(method == null) {
			return false;
		}
		return method.onCommand(this, sender, command, label, args);
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
	// @Override // this is not implemented in MW-Plugin-Main yet
	public int getAPIVersion() {
		return -1;
	}
	public void registerCommandProcessMethod(String command, CommandProcessMethod method) {
		commandProcessor.methods.put(command, method);
	}
}
class CommandProcessor {
	public final Main plugin;
	protected Map<String, CommandProcessMethod> methods = new HashMap<String, CommandProcessMethod>();
	CommandProcessor(Main plugin) {
		this.plugin = plugin;
	}
}