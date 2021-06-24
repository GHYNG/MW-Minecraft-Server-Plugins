package org.mwage.mcPlugin.vote;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
public class VoteConfig {
	public final Main plugin;
	private FileConfiguration configFile;
	public final LanguageConfig languageConfig;
	public final double voteBanPercentage;
	public VoteConfig(Main plugin) {
		this.plugin = plugin;
		configFile = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
		languageConfig = new LanguageConfig();
		voteBanPercentage = configFile.getDouble("vote-ban-percentage", 0.5);
	}
	public class LanguageConfig {
		private String lang = configFile.getString("language", "lang");
		private FileConfiguration languageConfigFile = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), lang));
		public final VoteLanguageConfig voteLanguageConfig = new VoteLanguageConfig();
		public class VoteLanguageConfig {
			public final String wrong_parameter = getStringOfKey(languageConfigFile, "VoteBan-wrong-parameter");
			public final String only_player_may_run_this_command = getStringOfKey(languageConfigFile, "VoteBan-only-player-may-run-this-command");
			public final String player_not_found = getStringOfKey(languageConfigFile, "VoteBan-player-not-found");
			public final String player_cancelled_vote = getStringOfKey(languageConfigFile, "VoteBan-player-cancelled-vote");
			public final String player_voted = getStringOfKey(languageConfigFile, "VoteBan-player-voted");
			public final String started = getStringOfKey(languageConfigFile, "VoteBan-started");
			public final String you_are_banned = getStringOfKey(languageConfigFile, "VoteBan-you-are-banned");
			public final String player_banned = getStringOfKey(languageConfigFile, "VoteBan-player-banned");
			public String convertMessage(String originalMessage, Player sender, Player target) {
				String senderName = sender.getName();
				String targetName = target.getName();
				return convertMessage(originalMessage, senderName, targetName);
			}
			public String convertMessage(String originalMessage, String sender, String target) {
				String message = originalMessage.replaceAll("<sender\\-player\\-name>", sender);
				message = message.replaceAll("<target\\-player\\-name>", target);
				return message;
			}
		}
	}
	/*
	 * This disallows null return.
	 * If key not found, return key.
	 */
	private String getStringOfKey(FileConfiguration configFile, String key) {
		return configFile.getString(key, key);
	}
}