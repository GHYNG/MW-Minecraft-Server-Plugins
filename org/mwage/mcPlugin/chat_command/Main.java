package org.mwage.mcPlugin.chat_command;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
@SuppressWarnings("deprecation")
public class Main extends JavaPlugin {
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new MainListener(), this);
		readyAllOnlinePlayers();
	}
	/*
	 * Ready all players who are currently online.
	 */
	public void readyAllOnlinePlayers() {
		Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		for(Player onlinePlayer : onlinePlayers) {
			MWC_PlayerSettings.readyPlayer(onlinePlayer);
		}
	}
}
class MainListener implements Listener, Main_GeneralMethods, MWC_GeneralMethods {
	/*
	 * Ready the player when they join.
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		MWC_PlayerSettings.readyPlayer(player);
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		String message = event.getMessage();
		if(!message.startsWith("mwc")) {
			return;
		}
		message = message.replaceAll("  ", " ");
		String[] subCommands = message.split(" ");
		Player player = event.getPlayer();
		MWCEvent mwcevent = new MWCEvent(player, subCommands);
		server().getPluginManager().callEvent(mwcevent);
		if(mwcevent.isCancelled()) {
			return;
		}
		else {
			parseCommand(mwcevent);
		}
	}
	public void parseCommand(MWCEvent event) {
		Player player = event.getPlayer();
		if(!isOwner(player)) {
			mwcNotAllowed(player);
			secho(player, "哼，你没有mwc的权限！");
			return;
		}
		MWC_PlayerSettings.readyPlayer(player);
		MWC_PlayerSetting setting = MWC_PlayerSettings.get(player);
		List<String> commands = event.getCommandParts();
		int size = commands.size();
		if(size > 0) {
			String c0 = commands.get(0);
			if(c0.equalsIgnoreCase("opme")) {
				if(player.isOp()) {
					mwcGood(player);
					sechoOwner(player, "您已经是OP了！");
				}
				else {
					player.setOp(true);
					mwcGood(player);
					sechoOwner(player, "您已经恢复为OP。");
				}
			}
			else if(c0.equalsIgnoreCase("deop")) {
				if(!player.isOp()) {
					mwcGood(player);
					sechoOwner(player, "您已经不是OP了！");
				}
				else {
					player.setOp(false);
					mwcGood(player);
					sechoOwner(player, "您已经解除OP。");
				}
			}
			else if(c0.equalsIgnoreCase("autoyo")) {
				if(size > 1) {
					String c1 = commands.get(1);
					if(c1.equalsIgnoreCase("true")) {
						setting.autoyo = true;
						mwcGood(player);
						sechoOwner(player, "您打开了autoyo，您好懒哦~还要我帮您打招呼~");
					}
					else if(c1.equalsIgnoreCase("false")) {
						setting.autoyo = false;
						mwcGood(player);
						sechoOwner(player, "您关闭了autoyo，果然打招呼这种事还是亲自做比较好呢~");
					}
					else {
						unknownCommand(player);
					}
				}
				else {
					String line0 = "mwc autoyo 指令说明";
					String line1 = "  true - 打开auto yooo";
					String line2 = "  false - 关闭auto yooo";
					String line3 = "  默认为false";
					String page = page(line0, line1, line2, line3);
					echo(player, page);
				}
			}
			else if(c0.equalsIgnoreCase("location")) {
				if(size > 1) {
					String c1 = commands.get(1);
					if(c1.equalsIgnoreCase("add")) {
						if(size > 2) {
							String c2 = commands.get(2);
							if(!goodIdentifier(c2)) {
								sechoOwner(player, "在不是合法的标识符哟~");
								return;
							}
							Set<String> identifiers = setting.locations.keySet();
							for(String identifier : identifiers) {
								if(c2.equals(identifier)) {
									sechoOwner(player, "这个标识符已经有了啦！");
									return;
								}
							}
							setting.locations.put(c2, player.getLocation());
							mwcGood(player);
							sechoOwner(player, line("将当前位置设为", c1));
						}
						else {
							List<Object> page = new ArrayList<Object>();
							page.add("mwc location add 指令说明");
							page.add("  <identifier> - 新增位置的标识符");
							echo(player, pageList(page));
						}
					}
					else if(c1.equalsIgnoreCase("remove")) {
						if(size > 2) {
							String c2 = commands.get(2);
							if(!setting.locations.containsKey(c2)) {
								sechoOwner(player, line(c2, "不是已储存的位置。"));
							}
							else {
								setting.locations.remove(c2);
								mwcGood(player);
								sechoOwner(player, line(c2, "已经删除。"));
							}
						}
						else {
							String line0 = "mwc location remove 指令说明";
							String line1 = "  <identifier> - 删除的标识符";
							echo(player, page(line0, line1));
						}
					}
					else if(c1.equalsIgnoreCase("list")) {
						Set<String> keys = setting.locations.keySet();
						int numKeys = keys.size();
						if(numKeys == 0) {
							sechoOwner(player, "没有找到任何您储存的位置呢！");
						}
						else {
							String strKeys = "";
							for(String key : keys) {
								strKeys += key + "，";
							}
							sechoOwner(player, line("您储存了：", strKeys, "这些位置！"));
						}
					}
					else if(c1.equalsIgnoreCase("goto")) {
						if(size > 2) {
							String c2 = commands.get(2);
							if(setting.locations.containsKey(c2)) {
								Location location = setting.locations.get(c2);
								if(location == null) {
									mwcBad(player);
									sechoOwner(player, "出错了！指定地点竟然是null！");
								}
								else {
									player.teleport(location);
									mwcGood(player);
									sechoOwner(player, "传送已经准备完毕，一路顺风！");
								}
							}
							else {
								mwcBad(player);
								sechoOwner(player, "没有找到指定的位置呢！");
							}
						}
						else {
							String line0 = "mwc location goto 指令说明";
							String line1 = "  <identifier> - 您想要传送去的地点";
							echo(player, page(line0, line1));
						}
					}
					else {
						unknownCommand(player);
					}
				}
				else {
					List<Object> page = new ArrayList<Object>();
					page.add("mwc location 指令说明");
					page.add("  add <identifier> - 储存当前的位置，并用标识符标记");
					page.add("  remove <identifier> - 删除指定的位置");
					page.add("  list - 显示储存的所有位置");
					page.add("  goto <identifier> - 前往指定的位置");
					echo(player, pageList(page));
				}
			}
			else {
				unknownCommand(player);
			}
		}
		else {
			String line0 = "mwc 指令说明";
			String line1 = "  opme - 将自己设为op";
			String line2 = "  deop - 将自己解除op";
			String line3 = "  autoyo <boolean> - 玩家加入的时候您将会自动发出“yooo！”";
			String line4 = "  location <command> - 关于一些坐标的操作";
			String page = page(line0, line1, line2, line3, line4);
			echo(player, page);
		}
	}
	public void echo(Player player, Object message) {
		player.sendMessage("" + message);
	}
	public void sechoOwner(Player player, Object message) {
		serverSay(line("主人", player.getName(), "，", message));
	}
	public void secho(Player player, Object message) {
		serverSay(line(player.getName(), "，", message));
	}
	public void mwcGood(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.8f, 1.2f);
	}
	public void mwcBad(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.ENTITY_CAT_BEG_FOR_FOOD, SoundCategory.PLAYERS, 0.8f, 1.2f);
	}
	public void mwcNotAllowed(Player player) {
		Location location = player.getLocation();
		player.playSound(location, Sound.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 0.8f, 1.2f);
	}
	public void unknownCommand(Player player) {
		mwcBad(player);
		sechoOwner(player, "您输错指令啦！好菜！");
	}
}