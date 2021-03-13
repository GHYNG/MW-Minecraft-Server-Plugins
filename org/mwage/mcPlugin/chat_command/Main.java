package org.mwage.mcPlugin.chat_command;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.mwage.mcPlugin.chat_command.SoundNotifierListener.EventType;
import org.mwage.mcPlugin.chat_command.SoundNotifierListener.SoundType;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
@SuppressWarnings("deprecation")
public class Main extends MWPlugin {
	private MWC_PlayerSettings playerSettings;
	public void onEnable() {
		playerSettings = new MWC_PlayerSettings(this);
		playerSettings.readyPlayers();
		registerListener(new MainListener(this));
		registerListener(new AutoyoListener(this));
		registerListener(new SoundNotifierListener(this));
	}
	public MWC_PlayerSettings getPlayerSettings() {
		return playerSettings;
	}
}
class MainListener implements Listener, Main_GeneralMethods, MWC_GeneralMethods {
	private Main plugin;
	MainListener(Main plugin) {
		this.plugin = plugin;
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
		MWC_PlayerSettings settings = plugin.getPlayerSettings();
		MWC_PlayerSetting setting = settings.get(player);
		List<String> commands = event.getCommandParts();
		int size = commands.size();
		if(size == 0) {
			List<Object> lines = new ArrayList<Object>();
			lines.add("mwc 指令说明");
			lines.add("  opme - 将自己设为op");
			lines.add("  deop - 将自己解除op");
			lines.add("  autoyo <boolean> - 玩家加入的时候您将会自动发出“yooo！”");
			lines.add("  location <command> - 关于一些坐标的操作");
			lines.add("  sn <command> - 当一些指定的时间发生时，我会通知您！");
			String page = pageList(lines);
			echo(player, page);
		}
		else {
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
				if(size == 1) {
					String line0 = "mwc autoyo 指令说明";
					String line1 = "  true - 打开auto yooo";
					String line2 = "  false - 关闭auto yooo";
					String line3 = "  默认为false，目前为" + setting.autoyo;
					String page = page(line0, line1, line2, line3);
					echo(player, page);
				}
				else {
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
			}
			else if(c0.equalsIgnoreCase("location")) {
				if(size == 1) {
					List<Object> page = new ArrayList<Object>();
					page.add("mwc location 指令说明");
					page.add("  add <identifier> - 储存当前的位置，并用标识符标记");
					page.add("  remove <identifier> - 删除指定的位置");
					page.add("  list - 显示储存的所有位置");
					page.add("  clear - 清除储存的所有位置");
					page.add("  goto <identifier> - 前往指定的位置");
					echo(player, pageList(page));
				}
				else {
					String c1 = commands.get(1);
					if(c1.equalsIgnoreCase("add")) {
						if(size == 2) {
							List<Object> page = new ArrayList<Object>();
							page.add("mwc location add 指令说明");
							page.add("  <identifier> - 新增位置的标识符");
							echo(player, pageList(page));
						}
						else {
							String c2 = commands.get(2);
							if(!goodIdentifier(c2)) {
								sechoOwner(player, "这不是合法的标识符哟~");
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
							sechoOwner(player, line("将当前位置设为", c2));
						}
					}
					else if(c1.equalsIgnoreCase("remove")) {
						if(size == 2) {
							String line0 = "mwc location remove 指令说明";
							String line1 = "  <identifier> - 删除的标识符";
							echo(player, page(line0, line1));
						}
						else {
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
					}
					else if(c1.equalsIgnoreCase("list")) {
						Set<String> keys = setting.locations.keySet();
						int numKeys = keys.size();
						if(numKeys == 0) {
							sechoOwner(player, "没有找到任何您储存的位置呢！");
						}
						else {
							sechoOwner(player, page("您储存了以下位置：", setting.listLocations()));
						}
					}
					else if(c1.equalsIgnoreCase("goto")) {
						if(size == 2) {
							String line0 = "mwc location goto 指令说明";
							String line1 = "  <identifier> - 您想要传送去的地点";
							echo(player, page(line0, line1));
						}
						else {
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
					}
					else if(c1.equalsIgnoreCase("clear")) {
						setting.locations.clear();
						mwcGood(player);
						sechoOwner(player, "已经为您删除所有地点！");
					}
					else {
						unknownCommand(player);
					}
				}
			}
			else if(c0.equalsIgnoreCase("sn")) {
				if(size < 3) {
					List<Object> lines = new ArrayList<Object>();
					lines.add("mwc sn 指令说明");
					lines.add("  <event_type> <sound_type> - 选择某种事件的音效");
					lines.add("  event_type:");
					lines.add("    PlayerJoin - 玩家加入");
					lines.add("    PlayerQuit - 玩家退出");
					lines.add("    PlayerDeath - 玩家死亡");
					lines.add("    PlayerChat - 玩家发出聊天信息");
					lines.add("    All - 全部");
					lines.add("  sound_type");
					lines.add("    Piano - 钢琴");
					lines.add("    Null - 关闭/无声音");
					echo(player, pageList(lines));
				}
				else {
					String c1 = commands.get(1).toLowerCase();
					String c2 = commands.get(2).toLowerCase();
					EventType et = EventType.get(c1);
					SoundType st = SoundType.get(c2);
					if(or(st == null, and(et == null, not(c1.equals("all"))))) {
						unknownCommand(player);
						return;
					}
					if(c1.equals("all")) {
						EventType[] ets = EventType.values();
						for(EventType e : ets) {
							setting.sound_notifications.put(e, st);
						}
						mwcGood(player);
						sechoOwner(player, line("已为您关注全部事件，并将声音选择为", st));
						return;
					}
					mwcGood(player);
					sechoOwner(player, line("已为您关注", et, "，并将声音选择为", st));
					setting.sound_notifications.put(et, st);
				}
			}
			else {
				unknownCommand(player);
			}
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