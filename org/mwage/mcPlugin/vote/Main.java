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
import org.mwage.mcPlugin.vote.normal.VoteNormalProcessor;
import org.mwage.mcPlugin.vote.normal.VoteNormalSystem;
public class Main extends MWPlugin implements Main_GeneralMethods {
	public VoteConfig voteConfig;
	public VoteNormalSystem voteNormalSystem;
	protected CommandProcessorManager commandProcessorManager;
	protected VoteBanProcessor voteBanProcessor;
	protected VoteNormalManageProcessor voteNormalManageProcessor;
	protected VoteNormalProcessor voteNormalProcessor;
	@Override
	public void onEnable() {
		initFiles();
		voteNormalSystem = new VoteNormalSystem(this);
		commandProcessorManager = new CommandProcessorManager(this);
		voteBanProcessor = new VoteBanProcessor(this);
		voteNormalManageProcessor = new VoteNormalManageProcessor(this);
		voteNormalProcessor = new VoteNormalProcessor(this);
		voteConfig = new VoteConfig(this);
	}
	@Override
	public void onDisable() {
		voteNormalSystem.onDisable();
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
		CommandProcessorInterface method = commandProcessorManager.methods.get(command.getName());
		if(method == null) {
			return false;
		}
		return method.onCommand(sender, command, label, args);
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
	public void registerCommandProcessor(CommandProcessorInterface method) {
		commandProcessorManager.methods.put(method.getCommand(), method);
	}
}
class CommandProcessorManager {
	public final Main plugin;
	protected Map<String, CommandProcessorInterface> methods = new HashMap<String, CommandProcessorInterface>();
	CommandProcessorManager(Main plugin) {
		this.plugin = plugin;
	}
}