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
	public final int voteNormalMaxCasts;
	public VoteConfig(Main plugin) {
		this.plugin = plugin;
		configFile = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
		languageConfig = new LanguageConfig();
		voteBanPercentage = configFile.getDouble("vote-ban-percentage", 0.5);
		voteNormalMaxCasts = configFile.getInt("vote-normal-max-casts", 25565);
	}
	public class LanguageConfig {
		private String lang = configFile.getString("language", "lang.yml");
		private FileConfiguration languageConfigFile = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), lang));
		public final VoteBanLanguageConfig voteBanLanguageConfig = new VoteBanLanguageConfig();
		public final VoteNormalManageLanguageConfig voteNormalManageLanguageConfig = new VoteNormalManageLanguageConfig();
		public final VoteNormalLanguageConfig voteNormalLanguageConfig = new VoteNormalLanguageConfig();
		public class VoteBanLanguageConfig {
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
		public class VoteNormalManageLanguageConfig {
			public final String empty_parameter = getStringOfKey(languageConfigFile, "VoteNormalManage-empty-parameter");
			public final String start_need_vote_name = getStringOfKey(languageConfigFile, "VoteNormalManage-start-need-vote-name");
			public final String start_bad_identifier = getStringOfKey(languageConfigFile, "VoteNormalManage-start-bad-identifier");
			public final String start_already_started = getStringOfKey(languageConfigFile, "VoteNormalManage-start-already-started");
			public final String start_successful_to_sender = getStringOfKey(languageConfigFile, "VoteNormalManage-start-successful-to-sender");
			public final String start_successful_to_public = getStringOfKey(languageConfigFile, "VoteNormalManage-start-successful-to-public");
			public final String setting_vote_not_found = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vote-not-found");
			public final String setting_vote_is_null = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vote-is-null");
			public final String setting_addselection_need_name = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-addselection-need-name");
			public final String setting_addselection_bad_identifier = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-addselection-bad-identifier");
			public final String setting_addselection_successful_to_sender = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-addselection-successful-to-sender");
			public final String setting_addselection_successful_to_public = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-addselection-successful-to-public");
			public final String setting_addselection_unsuccessful_to_sender = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-addselection-unsuccessful-to-sender");
			public final String setting_removeselection_need_name = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-removeselection-need-name");
			public final String setting_removeselection_not_found = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-removeselection-not-found");
			public final String setting_removeselection_successful_to_sender = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-removeselection-successful-to-sender");
			public final String setting_removeselection_successful_to_public = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-removeselection-successful-to-public");
			public final String setting_removeselection_unsuccessful_to_sender = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-removeselection-unsuccessful-to-sender");
			public final String setting_vpp = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vpp");
			public final String setting_vpp_smaller_than_zero = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vpp-smaller-than-zero");
			public final String setting_vpp_adjusted_to_sender = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vpp-adjusted-to-sender");
			public final String setting_vpp_adjusted_to_public = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vpp-adjusted-to-public");
			public final String setting_vpp_clear_player_vote_to_private = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vpp-clear-player-vote-to-private");
			public final String setting_vpp_clear_player_vote_to_public = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vpp-clear-player-vote-to-public");
			public final String setting_vpp_wrong_format = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vpp-wrong-format");
			public final String setting_vps = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vps");
			public final String setting_vps_smaller_than_minus_one = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vps-smaller-than-minus-one");
			public final String setting_vps_adjusted_to_sender = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vps-adjusted-to-sender");
			public final String setting_vps_adjusted_to_public = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vps-adjusted-to-public");
			public final String setting_vps_reset_player_vote_to_private = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vps-reset-player-vote-to-private");
			public final String setting_vps_reset_player_vote_to_public = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vps-reset-player-vote-to-public");
			public final String setting_vps_wrong_format = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-vps-wrong-format");
			public final String setting_anon = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-anon");
			public final String setting_anon_adjust_no_need = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-anon-adjust-no-need");
			public final String setting_anon_adjusted_to_sender = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-anon-adjusted-to-sender");
			public final String setting_anon_adjusted_to_public = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-anon-adjusted-to-public");
			public final String setting_anon_wrong_format = getStringOfKey(languageConfigFile, "VoteNormalManage-setting-anon-wrong-format");
			public final String list_vote_not_found = getStringOfKey(languageConfigFile, "VoteNormalManage-list-vote-not-found");
			public final String list_vote_is_null = getStringOfKey(languageConfigFile, "VoteNormalManage-list-vote-is-null");
			public final String stop_need_vote_name = getStringOfKey(languageConfigFile, "VoteNormalManage-stop-need-vote-name");
			public final String stop_vote_is_null = getStringOfKey(languageConfigFile, "VoteNormalManage-stop-vote-is-null");
			public final String stop_successful_to_sender = getStringOfKey(languageConfigFile, "VoteNormalManage-stop-successful-to-sender");
			public final String stop_successful_to_public = getStringOfKey(languageConfigFile, "VoteNormalManage-stop-successful-to-public");
			public final String stop_settle_general = getStringOfKey(languageConfigFile, "VoteNormalManage-stop-settle-general");
			public final String stop_settle_selection = getStringOfKey(languageConfigFile, "VoteNormalManage-stop-settle-selection");
			public final String stop_settle_player = getStringOfKey(languageConfigFile, "VoteNormalManage-stop-settle-player");
		}
		public class VoteNormalLanguageConfig {
			public final String sender_must_be_player = getStringOfKey(languageConfigFile, "VoteNormal-sender-must-be-player");
			public final String help = getStringOfKey(languageConfigFile, "VoteNormal-help");
			public final String vote_not_found = getStringOfKey(languageConfigFile, "VoteNormal-vote-not-found");
			public final String vote_is_null = getStringOfKey(languageConfigFile, "VoteNormal-vote-is-null");
			public final String vote_help = getStringOfKey(languageConfigFile, "VoteNormal-vote-help");
			public final String vote_help_selections = getStringOfKey(languageConfigFile, "VoteNormal-vote-help-selections");
			public final String selection_not_found = getStringOfKey(languageConfigFile, "VoteNormal-selection-not-found");
			public final String selection_is_null = getStringOfKey(languageConfigFile, "VoteNormal-selection-is-null");
			public final String selection_help = getStringOfKey(languageConfigFile, "VoteNormal-selection-help");
			public final String selection_help_voters = getStringOfKey(languageConfigFile, "VoteNormal-selection-help-voters");
			public final String above_vpp = getStringOfKey(languageConfigFile, "VoteNormal-above-vpp");
			public final String above_vps = getStringOfKey(languageConfigFile, "VoteNormal-above-vps");
			public final String below_0 = getStringOfKey(languageConfigFile, "VoteNormal-below-0");
			public final String vote_successful_to_sender = getStringOfKey(languageConfigFile, "VoteNormal-vote-successful-to-sender");
			public final String vote_unsuccessful_to_sender = getStringOfKey(languageConfigFile, "VoteNormal-vote-unsuccessful-to-sender");
			public final String vote_successful_to_public = getStringOfKey(languageConfigFile, "VoteNormal-vote-successful-to-public");
			public final String wrong_format = getStringOfKey(languageConfigFile, "VoteNormal-wrong-format");
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