package org.mwage.mcPlugin.vote.normal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mwage.mcPlugin.vote.AbstractCommandProcessor;
import org.mwage.mcPlugin.vote.Main;
import org.mwage.mcPlugin.vote.VoteConfig;
import org.mwage.mcPlugin.vote.Vote_GeneralMethods;
import org.mwage.mcPlugin.vote.normal.VoteNormal.Selection;
public class VoteNormalManageProcessor extends AbstractCommandProcessor implements Vote_GeneralMethods {
	/**
	 * This does nothing yet.
	 */
	public final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	public final VoteNormalSystem voteNormalSystem;
	public VoteNormalManageProcessor(Main plugin) {
		super(plugin);
		voteNormalSystem = plugin.voteNormalSystem;
	}
	public String getCommand() {
		return "vote-normal-manage";
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		VoteConfig.LanguageConfig.VoteNormalManageLanguageConfig vlc = plugin.voteConfig.languageConfig.voteNormalManageLanguageConfig;
		Map<String, String> strConv = new HashMap<String, String>();
		strConv.put("<sender\\-name>", sender.getName());
		strConv.put("<vote\\-name>", "null");
		strConv.put("<selection\\-name>", "null");
		strConv.put("<vpp\\-value\\-old>", "null");
		strConv.put("<vpp\\-value>\\-new", "null");
		strConv.put("<vps\\-value\\-old>", "null");
		strConv.put("<vps\\-value>\\-new", "null");
		strConv.put("<anon\\-value\\-old>", "null");
		strConv.put("<anon\\-value\\-new>", "null");
		strConv.put("<voter\\-name>", "null");
		strConv.put("<vote\\-count>", "null");
		int argLength = args.length;
		if(argLength == 0) {
			sender.sendMessage(convertMessage(vlc.empty_parameter, strConv));
			return false;
		}
		else {
			String arg0 = args[0];
			if(arg0.equalsIgnoreCase("help")) {
				List<String> message = new ArrayList<String>();
				message.add("/vote-normal-manage help");
				message.add("  ask for help.");
				message.add("/vote-nromal-manage start <key vote-name>");
				message.add("  start a new vote");
				message.add("  <vote-name>");
				message.add("    the name of this vote you want to give");
				message.add("/vote-normal-manage setting <vote-name> (optional)<command>");
				message.add("  manage the setting of a ongoing vote");
				message.add("  <vote-name>");
				message.add("    the name of the vote you want to adjust setting");
				message.add("  <command>");
				message.add("    addSelection <key selection-name>");
				message.add("      add a new selection");
				message.add("      <selection-name>");
				message.add("        the name of the selection you want to add");
				message.add("    removeSelection <key selection-name>");
				message.add("      remove a selection");
				message.add("      <selection-name>");
				message.add("        the name of the selection you want to remove");
				message.add("    vpp <int count>");
				message.add("      set the max votes per person count");
				message.add("      <count>");
				message.add("        the max amount of votes one player can cast totally");
				message.add("    vps <int count>");
				message.add("      set the max votes per selection count");
				message.add("      <count>");
				message.add("        the max amount of votes one player can cast to a selection");
				message.add("        -1 if no limit is set");
				message.add("    anon <boolean>");
				message.add("      set if the vote is a secret voting. true if secret");
				message.add("/vote-normal-manage list (optional)<key vote-name>");
				message.add("  list all ongoing votes");
				message.add("  <vote-name>");
				message.add("    list the stats of the given vote");
				message.add("/vote-normal-manage stop <key vote-name>");
				message.add("  stop a vote, and count the votes casted");
				message.add("  <vote-name>");
				message.add("    the vote you want to stop");
				sendMessage(sender, message);
				return true;
			}
			else if(arg0.equalsIgnoreCase("start")) {
				if(argLength == 1) {
					sender.sendMessage(convertMessage(vlc.start_need_vote_name, strConv));
					return true;
				}
				else {
					String arg1 = args[1];
					strConv.put("<vote\\-name>", arg1);
					if(!goodIdentifier(arg1)) {
						sender.sendMessage(convertMessage(vlc.start_bad_identifier, strConv));
						return true;
					}
					else if(voteNormalSystem.ongoingVotes.containsKey(arg1)) {
						sender.sendMessage(convertMessage(vlc.start_already_started, strConv));
						return true;
					}
					else {
						voteNormalSystem.ongoingVotes.put(arg1, new VoteNormal(arg1));
						sender.sendMessage(convertMessage(vlc.start_successful_to_sender, strConv));
						plugin.serverSay(convertMessage(vlc.start_successful_to_public, strConv));
						return true;
					}
				}
			}
			else if(arg0.equalsIgnoreCase("setting")) {
				if(argLength == 1) {
					return false;
				}
				else {
					String arg1 = args[1];
					strConv.put("<vote\\-name>", arg1);
					if(!voteNormalSystem.ongoingVotes.containsKey(arg1)) {
						sender.sendMessage(convertMessage(vlc.setting_vote_not_found, strConv));
						return true;
					}
					else {
						VoteNormal voteNormal = voteNormalSystem.ongoingVotes.get(arg1);
						if(voteNormal == null) {
							sender.sendMessage(convertMessage(vlc.setting_vote_is_null, strConv));
							return true;
						}
						else {
							String voteName = voteNormal.name;
							int vpp = voteNormal.vpp;
							int vps = voteNormal.vps;
							boolean anon = voteNormal.anon;
							strConv.put("<vpp\\-value\\-old>", "" + vpp);
							strConv.put("<vps\\-value\\-old>", "" + vps);
							strConv.put("<anon\\-value\\-old>", "" + anon);
							if(argLength == 2) {
								Set<String> selectionNames = voteNormal.selections.keySet();
								sender.sendMessage("Info for Vote: " + voteName + ":");
								sender.sendMessage("  vpp: " + vpp);
								sender.sendMessage("  vps: " + vps);
								sender.sendMessage("  anon: " + anon);
								sender.sendMessage("  selections: ");
								for(String selectionName : selectionNames) {
									sender.sendMessage("    " + selectionName);
								}
								return true;
							}
							else {
								String arg2 = args[2];
								if(arg2.equalsIgnoreCase("addselection")) {
									if(argLength == 3) {
										sender.sendMessage(convertMessage(vlc.setting_addselection_need_name, strConv));
										return true;
									}
									else {
										String arg3 = args[3];
										strConv.put("<selection\\-name>", arg3);
										if(!goodIdentifier(arg3)) {
											sender.sendMessage(convertMessage(vlc.setting_addselection_bad_identifier, strConv));
											return true;
										}
										else {
											boolean success = voteNormal.createSelection(arg3);
											if(success) {
												sender.sendMessage(convertMessage(vlc.setting_addselection_successful_to_sender, strConv));
												plugin.serverSay(convertMessage(vlc.setting_addselection_successful_to_public, strConv));
												return true;
											}
											else {
												sender.sendMessage(convertMessage(vlc.setting_addselection_unsuccessful_to_sender, strConv));
												return true;
											}
										}
									}
								}
								else if(arg2.equalsIgnoreCase("removeselection")) {
									if(argLength == 3) {
										sender.sendMessage(convertMessage(vlc.setting_removeselection_need_name, strConv));
										return true;
									}
									else {
										String arg3 = args[3];
										strConv.put("<selection\\-name>", arg3);
										if(!voteNormal.selections.containsKey(arg3)) {
											sender.sendMessage(convertMessage(vlc.setting_removeselection_not_found, strConv));
											return true;
										}
										else {
											boolean success = voteNormal.removeSelection(arg3);
											if(success) {
												sender.sendMessage(convertMessage(vlc.setting_removeselection_successful_to_sender, strConv));
												plugin.serverSay(convertMessage(vlc.setting_removeselection_successful_to_public, strConv));
												return true;
											}
											else {
												sender.sendMessage(convertMessage(vlc.setting_removeselection_unsuccessful_to_sender, strConv));
												return true;
											}
										}
									}
								}
								else if(arg2.equalsIgnoreCase("vpp")) {
									if(argLength == 3) {
										sender.sendMessage(convertMessage(vlc.setting_vpp, strConv));
										return true;
									}
									else {
										String arg3 = args[3];
										strConv.put("<vpp\\-value\\-new>", arg3);
										try {
											int vppNew = Integer.parseInt(arg3);
											if(vppNew < 0) {
												sender.sendMessage(convertMessage(vlc.setting_vpp_smaller_than_zero, strConv));
												return true;
											}
											else {
												sender.sendMessage(convertMessage(vlc.setting_vpp_adjusted_to_sender, strConv));
												plugin.serverSay(convertMessage(vlc.setting_vpp_adjusted_to_public, strConv));
												if(vppNew < voteNormal.vpp) {
													Set<? extends OfflinePlayer> players = voteNormal.getPlayers();
													for(OfflinePlayer player : players) {
														if(player instanceof Player onlinePlayer) {
															strConv.put("<voter\\-name>", player.getName());
															if(vppNew < voteNormal.getTotalCastsFromPlayer(player)) {
																voteNormal.clearPlayerCasts(player);
																onlinePlayer.sendMessage(convertMessage(vlc.setting_vpp_clear_player_vote_to_private, strConv));
																if(!voteNormal.anon) {
																	plugin.serverSay(convertMessage(vlc.setting_vpp_clear_player_vote_to_public, strConv));
																}
															}
														}
													}
												}
												voteNormal.vpp = vppNew;
												return true;
											}
										}
										catch(NumberFormatException e) {
											sender.sendMessage(convertMessage(vlc.setting_vpp_wrong_format, strConv));
											return false;
										}
									}
								}
								else if(arg2.equalsIgnoreCase("vps")) {
									if(argLength == 3) {
										sender.sendMessage(convertMessage(vlc.setting_vps, strConv));
										return true;
									}
									else {
										String arg3 = args[3];
										strConv.put("<vps\\-value\\-new>", arg3);
										try {
											int vpsNew = Integer.parseInt(arg3);
											if(vpsNew < -1) {
												sender.sendMessage(convertMessage(vlc.setting_vps_smaller_than_minus_one, strConv));
												return true;
											}
											else {
												sender.sendMessage(convertMessage(vlc.setting_vps_adjusted_to_sender, strConv));
												plugin.serverSay(convertMessage(vlc.setting_vps_adjusted_to_public, strConv));
												if(vpsNew < voteNormal.vps) {
													Set<? extends OfflinePlayer> players = voteNormal.getPlayers();
													for(String selectionName : voteNormal.selections.keySet()) {
														Selection selection = voteNormal.selections.get(selectionName);
														if(selection == null) {
															continue;
														}
														strConv.put("<selection\\-name>", selection.name);
														for(OfflinePlayer offlinePlayer : players) {
															if(offlinePlayer instanceof Player player) {
																strConv.put("<voter\\-name>", offlinePlayer.getName());
																int amountVoted = selection.getCastsFromPlayer(offlinePlayer);
																if(amountVoted > vpsNew && vpsNew >= 0) {
																	selection.setCastsFromPlayer(offlinePlayer, vpsNew);
																	player.sendMessage(convertMessage(vlc.setting_vps_reset_player_vote_to_private, strConv));
																	if(!voteNormal.anon) {
																		plugin.serverSay(convertMessage(vlc.setting_vps_reset_player_vote_to_public, strConv));
																	}
																}
															}
														}
													}
												}
												voteNormal.vps = vpsNew;
												return true;
											}
										}
										catch(NumberFormatException e) {
											sender.sendMessage(convertMessage(vlc.setting_vps_wrong_format, strConv));
											return false;
										}
									}
								}
								else if(arg2.equalsIgnoreCase("anon")) {
									if(argLength == 3) {
										sender.sendMessage(convertMessage(vlc.setting_anon, strConv));
										return true;
									}
									else {
										String arg3 = args[3].toLowerCase();
										strConv.put("<anon\\-value\\-new>", arg3);
										if(arg3.equals("true") || arg3.equals("false")) {
											boolean anonNew = arg3.equals("true") ? true : false;
											if(voteNormal.anon == anonNew) {
												sender.sendMessage(convertMessage(vlc.setting_anon_adjust_no_need, strConv));
												return true;
											}
											else {
												voteNormal.anon = anonNew;
												sender.sendMessage(convertMessage(vlc.setting_anon_adjusted_to_sender, strConv));
												plugin.serverSay(convertMessage(vlc.setting_anon_adjusted_to_public, strConv));
												return true;
											}
										}
										else {
											sender.sendMessage(convertMessage(vlc.setting_anon_wrong_format, strConv));
											return false;
										}
									}
								}
							}
						}
					}
				}
			}
			else if(arg0.equalsIgnoreCase("list")) {
				if(argLength == 1) {
					int numVotes = voteNormalSystem.ongoingVotes.size();
					sender.sendMessage("List of all (" + numVotes + ") ongoing votes: ");
					for(String voteName : voteNormalSystem.ongoingVotes.keySet()) {
						VoteNormal vote = voteNormalSystem.ongoingVotes.get(voteName);
						if(vote == null) {
							continue;
						}
						int numSelections = vote.selections.size();
						int numPlayers = vote.getPlayers().size();
						int numCasts = vote.getTotalCasts();
						sender.sendMessage("  " + voteName + ": " + numPlayers + " players casted " + numCasts + " votes with " + numSelections + " available selections.");
					}
					return true;
				}
				else {
					String arg1 = args[1];
					strConv.put("<vote\\-name>", arg1);
					if(!voteNormalSystem.ongoingVotes.containsKey(arg1)) {
						sender.sendMessage(convertMessage(vlc.list_vote_not_found, strConv));
						return true;
					}
					else {
						VoteNormal voteNormal = voteNormalSystem.ongoingVotes.get(arg1);
						if(voteNormal == null) {
							sender.sendMessage(convertMessage(vlc.setting_vote_is_null, strConv));
							return true;
						}
						else {
							String voteName = voteNormal.name;
							int vpp = voteNormal.vpp;
							int vps = voteNormal.vps;
							boolean anon = voteNormal.anon;
							int voteTotalCasts = voteNormal.getTotalCasts();
							sender.sendMessage("Vote: " + voteName);
							sender.sendMessage("  vpp: " + vpp);
							sender.sendMessage("  vps: " + vps);
							sender.sendMessage("  anon: " + anon);
							sender.sendMessage("  total casts: " + voteTotalCasts);
							sender.sendMessage("  selections: ");
							for(String selectionName : voteNormal.selections.keySet()) {
								Selection selection = voteNormal.selections.get(selectionName);
								if(selection == null) {
									continue;
								}
								int selectionTotalCasts = selection.getTotalCasts();
								sender.sendMessage("    Section: " + selectionName);
								sender.sendMessage("      total casts: " + selectionTotalCasts);
								for(UUID uuid : selection.playerVoteCounts.keySet()) {
									Integer playerCasts = selection.playerVoteCounts.get(uuid);
									if(playerCasts == null) {
										continue;
									}
									OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
									if(player == null) {
										continue;
									}
									String playerName = player.getName();
									sender.sendMessage("        " + playerName + ": " + playerCasts);
								}
							}
							return true;
						}
					}
				}
			}
			else if(arg0.equalsIgnoreCase("stop")) {
				if(argLength == 1) {
					sender.sendMessage(convertMessage(vlc.stop_need_vote_name, strConv));
					return true;
				}
				else {
					String arg1 = args[1];
					strConv.put("<vote\\-name>", arg1);
					if(voteNormalSystem.ongoingVotes.containsKey(arg1)) {
						VoteNormal voteNormal = voteNormalSystem.ongoingVotes.get(arg1);
						if(voteNormal == null) {
							sender.sendMessage(convertMessage(vlc.stop_vote_is_null, strConv));
							return true;
						}
						else {
							voteNormalSystem.ongoingVotes.remove(arg1);
							sender.sendMessage(convertMessage(vlc.stop_successful_to_sender, strConv));
							plugin.serverSay(convertMessage(vlc.stop_successful_to_public, strConv));
							strConv.put("<vote\\-count>", "" + voteNormal.getTotalCasts());
							Bukkit.broadcastMessage(convertMessage(vlc.stop_settle_general, strConv));
							for(String selectionName : voteNormal.selections.keySet()) {
								Selection selection = voteNormal.selections.get(selectionName);
								if(selection == null) {
									continue;
								}
								strConv.put("<selection\\-name>", selectionName);
								strConv.put("<vote\\-count>", "" + selection.getTotalCasts());
								Bukkit.broadcastMessage("  " + convertMessage(vlc.stop_settle_selection, strConv));
								if(!voteNormal.anon) {
									String line = "";
									for(UUID uuid : selection.playerVoteCounts.keySet()) {
										OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
										int cast = selection.getCastsFromPlayer(player);
										strConv.put("<voter\\-name>", player.getName());
										strConv.put("<vote\\-count>", "" + cast);
										line += convertMessage(vlc.stop_settle_player, strConv);
									}
									Bukkit.broadcastMessage("    " + line);
								}
							}
							return true;
						}
					}
					else {
						return true;
					}
				}
			}
			else {
				sender.sendMessage("wrong command!");
				return false;
			}
		}
		sender.sendMessage("something went wrong when calling /vote-normal-manage");
		return false;
	}
}