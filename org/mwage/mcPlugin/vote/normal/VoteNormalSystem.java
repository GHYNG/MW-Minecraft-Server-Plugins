package org.mwage.mcPlugin.vote.normal;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mwage.mcPlugin.vote.Main;
import org.mwage.mcPlugin.vote.normal.VoteNormal.Selection;
public class VoteNormalSystem {
	public final Main plugin;
	protected Map<String, VoteNormal> ongoingVotes = new HashMap<String, VoteNormal>();
	public VoteNormalSystem(Main plugin) {
		this.plugin = plugin;
		onEnable();
	}
	/**
	 * This method should not be called from Main.
	 */
	public void onEnable() {
		File folderPlugin = plugin.getDataFolder();
		folderPlugin.mkdirs();
		File folderVoteNormal = new File(folderPlugin, "vote_normal");
		folderVoteNormal.mkdirs();
		File folderVotes = new File(folderVoteNormal, "votes");
		folderVotes.mkdirs();
		File[] filesVote = folderVotes.listFiles();
		for(File fileVoteNormal : filesVote) {
			if(!fileVoteNormal.isFile()) {
				continue;
			}
			String voteNormalFileName = fileVoteNormal.getName();
			if(!voteNormalFileName.endsWith(".vn")) {
				continue;
			}
			int length = voteNormalFileName.length();
			String voteNormalName = voteNormalFileName.substring(0, length - 3);
			FileConfiguration voteNormalConfig = YamlConfiguration.loadConfiguration(fileVoteNormal);
			VoteNormal voteNormal = readVoteNormal(voteNormalConfig);
			ongoingVotes.put(voteNormalName, voteNormal);
		}
	}
	public void onDisable() {
		File folderPlugin = plugin.getDataFolder();
		folderPlugin.mkdirs();
		File folderVoteNormal = new File(folderPlugin, "vote_normal");
		folderVoteNormal.mkdirs();
		File folderVotes = new File(folderVoteNormal, "votes");
		folderVotes.mkdirs();
		File[] filesVote = folderVotes.listFiles();
		for(File fileVoteNormal : filesVote) {
			if(fileVoteNormal.isFile() && fileVoteNormal.getName().endsWith(".vn")) {
				fileVoteNormal.delete();
			}
		}
		for(String voteNormalName : ongoingVotes.keySet()) {
			VoteNormal voteNormal = ongoingVotes.get(voteNormalName);
			if(voteNormal == null) {
				continue;
			}
			File fileVoteNormal = new File(folderVotes, voteNormalName + ".vn");
			YamlConfiguration yamlVoteNormal = new YamlConfiguration();
			writeVoteNormal(voteNormal, yamlVoteNormal);
			try {
				yamlVoteNormal.save(fileVoteNormal);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	protected VoteNormal readVoteNormal(ConfigurationSection configVoteNormal) {
		VoteNormal voteNormal = new VoteNormal(configVoteNormal.getName());
		int vpp = configVoteNormal.getInt("vpp", 0);
		int vps = configVoteNormal.getInt("vps", 0);
		boolean anon = configVoteNormal.getBoolean("anon", true);
		voteNormal.vpp = vpp;
		voteNormal.vps = vps;
		voteNormal.anon = anon;
		ConfigurationSection configSelections = configVoteNormal.getConfigurationSection("selections");
		if(configSelections != null) {
			Set<String> selectionNames = configSelections.getKeys(false);
			for(String selectionName : selectionNames) {
				ConfigurationSection configSelection = configSelections.getConfigurationSection(selectionName);
				if(configSelection == null) {
					continue;
				}
				Selection selection = readSelection(voteNormal, configSelection);
				voteNormal.selections.put(selectionName, selection);
			}
		}
		return voteNormal;
	}
	protected Selection readSelection(VoteNormal voteNormal, ConfigurationSection configSelection) {
		Selection selection = voteNormal.new Selection(configSelection.getName());
		List<?> playerVoteCounts = configSelection.getList("player_vote_counts");
		for(Object o : playerVoteCounts) {
			if(o instanceof Map<?, ?> mapPlayerVoteCount) {
				Object objUUID = mapPlayerVoteCount.get("uuid");
				if(objUUID == null) {
					continue;
				}
				String strUUID = objUUID.toString();
				UUID uuid = UUID.fromString(strUUID);
				Object objCount = mapPlayerVoteCount.get("count");
				String strCount = "" + objCount;
				try {
					int count = Integer.parseInt(strCount);
					if(count < 0) {
						count = 0;
					}
					selection.playerVoteCounts.put(uuid, count);
				}
				catch(NumberFormatException e) {
					continue;
				}
			}
		}
		return selection;
	}
	protected void writeVoteNormal(VoteNormal voteNormal, ConfigurationSection configVoteNormal) {
		int vpp = voteNormal.vpp;
		int vps = voteNormal.vps;
		boolean anon = voteNormal.anon;
		configVoteNormal.set("vpp", vpp);
		configVoteNormal.set("vps", vps);
		configVoteNormal.set("anon", anon);
		ConfigurationSection configSelections = configVoteNormal.createSection("selections");
		for(String selectionName : voteNormal.selections.keySet()) {
			Selection selection = voteNormal.selections.get(selectionName);
			if(selection == null) {
				continue;
			}
			ConfigurationSection configSelection = configSelections.createSection(selectionName);
			writeSelection(selection, configSelection);
		}
	}
	protected void writeSelection(Selection selection, ConfigurationSection configSelection) {
		List<Map<String, Object>> listPlayerVoteCounts = new ArrayList<Map<String, Object>>();
		for(UUID uuid : selection.playerVoteCounts.keySet()) {
			int count = selection.playerVoteCounts.get(uuid) == null ? 0 : selection.playerVoteCounts.get(uuid);
			Map<String, Object> mapPlayerVoteCount = new LinkedHashMap<String, Object>();
			OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
			String playerName = player == null ? "null" : player.getName();
			mapPlayerVoteCount.put("name", playerName);
			mapPlayerVoteCount.put("uuid", uuid.toString());
			mapPlayerVoteCount.put("count", count);
			listPlayerVoteCounts.add(mapPlayerVoteCount);
		}
		configSelection.set("player_vote_counts", listPlayerVoteCounts);
	}
}