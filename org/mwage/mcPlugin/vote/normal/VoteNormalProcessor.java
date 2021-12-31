package org.mwage.mcPlugin.vote.normal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mwage.mcPlugin.vote.AbstractCommandProcessor;
import org.mwage.mcPlugin.vote.Main;
import org.mwage.mcPlugin.vote.VoteConfig;
import org.mwage.mcPlugin.vote.Vote_GeneralMethods;
import org.mwage.mcPlugin.vote.normal.VoteNormal.Selection;
public class VoteNormalProcessor extends AbstractCommandProcessor implements Vote_GeneralMethods {
	public final VoteNormalSystem voteNormalSystem;
	public VoteNormalProcessor(Main plugin) {
		super(plugin);
		voteNormalSystem = plugin.voteNormalSystem;
	}
	public String getCommand() {
		return "vote-normal";
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		VoteConfig.LanguageConfig.VoteNormalLanguageConfig vlc = plugin.voteConfig.languageConfig.voteNormalLanguageConfig;
		Map<String, String> strConv = new HashMap<String, String>();
		int maxCasts = Math.abs(plugin.voteConfig.voteNormalMaxCasts);
		strConv.put("<max\\-casts\\-value", "" + maxCasts);
		strConv.put("<vote\\-name>", "null");
		strConv.put("<selection\\-name>", "null");
		strConv.put("<voter\\-name>", sender.getName());
		strConv.put("<vote\\-count>", "null");
		strConv.put("<total\\-voted\\-count>", "null");
		strConv.put("<selection\\-voted\\-count>", "null");
		strConv.put("<vpp\\-value>", "null");
		strConv.put("<vps\\-value>", "null");
		strConv.put("<anon\\-value>", "null");
		if(sender instanceof Player player) {
			String voterName = player.getName();
			strConv.put("<voter\\-name>", voterName);
			int argLength = args.length;
			if(argLength == 0) {
				sender.sendMessage(convertMessage(vlc.help, strConv));
				return true;
			}
			else {
				String arg0 = args[0];
				strConv.put("<vote\\-name>", arg0);
				if(!voteNormalSystem.ongoingVotes.containsKey(arg0)) {
					sender.sendMessage(convertMessage(vlc.vote_not_found, strConv));
					return true;
				}
				else {
					VoteNormal voteNormal = voteNormalSystem.ongoingVotes.get(arg0);
					if(voteNormal == null) {
						sender.sendMessage(convertMessage(vlc.vote_not_found, strConv));
						return true;
					}
					else {
						int totalVotedCount = voteNormal.getTotalCastsFromPlayer(player);
						int vpp = voteNormal.vpp;
						int vps = voteNormal.vps < 0 ? vpp : voteNormal.vps;
						boolean anon = voteNormal.anon;
						strConv.put("<total\\-voted\\-count>", "" + totalVotedCount);
						strConv.put("<vpp\\-value>", "" + vpp);
						strConv.put("<vps\\-value>", "" + vps);
						strConv.put("<anon\\-value>", "" + anon);
						if(argLength == 1) {
							sender.sendMessage(convertMessage(vlc.vote_help, strConv));
							String lineSelections = "  ";
							for(String selectionName : voteNormal.selections.keySet()) {
								strConv.put("<selection\\-name>", selectionName);
								lineSelections += convertMessage(vlc.vote_help_selections, strConv);
							}
							sender.sendMessage(lineSelections);
							return true;
						}
						else {
							String arg1 = args[1];
							String selectionName = arg1;
							strConv.put("<selection\\-name>", selectionName);
							if(!voteNormal.selections.containsKey(selectionName)) {
								sender.sendMessage(convertMessage(vlc.selection_not_found, strConv));
								return true;
							}
							else {
								Selection selection = voteNormal.selections.get(selectionName);
								if(selection == null) {
									sender.sendMessage(convertMessage(vlc.selection_is_null, strConv));
									return true;
								}
								else {
									int selectionVotedCount = selection.getCastsFromPlayer(player);
									strConv.put("<selection\\-voted\\-count>", "" + selectionVotedCount);
									if(argLength == 2) {
										sender.sendMessage(convertMessage(vlc.selection_help, strConv));
										if(!voteNormal.anon) {
											String votesLine = "  ";
											for(UUID uuid : selection.playerVoteCounts.keySet()) {
												Player voter = Bukkit.getPlayer(uuid);
												int voteCount = selection.getCastsFromPlayer(voter);
												strConv.put("<voter\\-name>", voter.getName());
												strConv.put("<vote\\-count>", "" + voteCount);
												votesLine += convertMessage(vlc.selection_help_voters, strConv);
											}
											sender.sendMessage(votesLine);
										}
										return true;
									}
									else {
										String arg2 = args[2];
										strConv.put("<vote\\-count>", arg2);
										try {
											int voteCount = Integer.parseInt(arg2);
											if(voteCount > maxCasts) {
												voteCount = maxCasts;
											}
											else if(voteCount < -maxCasts) {
												voteCount = -maxCasts;
											}
											strConv.put("<vote\\-count>", "" + voteCount);
											if(totalVotedCount + voteCount > vpp) {
												voteCount = vpp - totalVotedCount;
												strConv.put("<vote\\-count>", "" + voteCount);
												sender.sendMessage(convertMessage(vlc.above_vpp, strConv));
											}
											if(selectionVotedCount + voteCount > vps) {
												voteCount = vps - selectionVotedCount;
												strConv.put("<vote\\-count>", "" + voteCount);
												sender.sendMessage(convertMessage(vlc.above_vps, strConv));
											}
											if(selectionVotedCount + voteCount < 0) {
												voteCount = -selectionVotedCount;
												strConv.put("<vote\\-count>", "" + voteCount);
												sender.sendMessage(convertMessage(vlc.below_0, strConv));
											}
											boolean successful = selection.castVote(player, voteCount);
											if(successful) {
												sender.sendMessage(convertMessage(vlc.vote_successful_to_sender, strConv));
												if(!voteNormal.anon) {
													plugin.serverSay(convertMessage(vlc.vote_successful_to_public, strConv));
												}
												return true;
											}
											else {
												sender.sendMessage(convertMessage(vlc.vote_unsuccessful_to_sender, strConv));
												return true;
											}
										}
										catch(NumberFormatException e) {
											sender.sendMessage(convertMessage(vlc.wrong_format, strConv));
											return false;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		else {
			sender.sendMessage(convertMessage(vlc.sender_must_be_player, strConv));
			return false;
		}
		// sender.sendMessage("something went wrong when calling /vote-normal-manage");
		// return false;
	}
}