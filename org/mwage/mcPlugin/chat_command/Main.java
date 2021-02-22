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
	public void onEnable() {readyAllOnlinePlayers();
		Bukkit.getPluginManager().registerEvents(new MainListener(), this);
		Bukkit.getPluginManager().registerEvents(new AutoyoListener(), this);
		
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
			secho(player, "�ߣ���û��mwc��Ȩ�ޣ�");
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
					sechoOwner(player, "���Ѿ���OP�ˣ�");
				}
				else {
					player.setOp(true);
					mwcGood(player);
					sechoOwner(player, "���Ѿ��ָ�ΪOP��");
				}
			}
			else if(c0.equalsIgnoreCase("deop")) {
				if(!player.isOp()) {
					mwcGood(player);
					sechoOwner(player, "���Ѿ�����OP�ˣ�");
				}
				else {
					player.setOp(false);
					mwcGood(player);
					sechoOwner(player, "���Ѿ����OP��");
				}
			}
			else if(c0.equalsIgnoreCase("autoyo")) {
				if(size > 1) {
					String c1 = commands.get(1);
					if(c1.equalsIgnoreCase("true")) {
						setting.autoyo = true;
						mwcGood(player);
						sechoOwner(player, "������autoyo��������Ŷ~��Ҫ�Ұ������к�~");
					}
					else if(c1.equalsIgnoreCase("false")) {
						setting.autoyo = false;
						mwcGood(player);
						sechoOwner(player, "���ر���autoyo����Ȼ���к������»����������ȽϺ���~");
					}
					else {
						unknownCommand(player);
					}
				}
				else {
					String line0 = "mwc autoyo ָ��˵��";
					String line1 = "  true - ��auto yooo";
					String line2 = "  false - �ر�auto yooo";
					String line3 = "  Ĭ��Ϊfalse";
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
								sechoOwner(player, "�ڲ��ǺϷ��ı�ʶ��Ӵ~");
								return;
							}
							Set<String> identifiers = setting.locations.keySet();
							for(String identifier : identifiers) {
								if(c2.equals(identifier)) {
									sechoOwner(player, "�����ʶ���Ѿ���������");
									return;
								}
							}
							setting.locations.put(c2, player.getLocation());
							mwcGood(player);
							sechoOwner(player, line("����ǰλ����Ϊ", c1));
						}
						else {
							List<Object> page = new ArrayList<Object>();
							page.add("mwc location add ָ��˵��");
							page.add("  <identifier> - ����λ�õı�ʶ��");
							echo(player, pageList(page));
						}
					}
					else if(c1.equalsIgnoreCase("remove")) {
						if(size > 2) {
							String c2 = commands.get(2);
							if(!setting.locations.containsKey(c2)) {
								sechoOwner(player, line(c2, "�����Ѵ����λ�á�"));
							}
							else {
								setting.locations.remove(c2);
								mwcGood(player);
								sechoOwner(player, line(c2, "�Ѿ�ɾ����"));
							}
						}
						else {
							String line0 = "mwc location remove ָ��˵��";
							String line1 = "  <identifier> - ɾ���ı�ʶ��";
							echo(player, page(line0, line1));
						}
					}
					else if(c1.equalsIgnoreCase("list")) {
						Set<String> keys = setting.locations.keySet();
						int numKeys = keys.size();
						if(numKeys == 0) {
							sechoOwner(player, "û���ҵ��κ��������λ���أ�");
						}
						else {
							String strKeys = "";
							for(String key : keys) {
								strKeys += key + "��";
							}
							sechoOwner(player, line("�������ˣ�", strKeys, "��Щλ�ã�"));
						}
					}
					else if(c1.equalsIgnoreCase("goto")) {
						if(size > 2) {
							String c2 = commands.get(2);
							if(setting.locations.containsKey(c2)) {
								Location location = setting.locations.get(c2);
								if(location == null) {
									mwcBad(player);
									sechoOwner(player, "�����ˣ�ָ���ص㾹Ȼ��null��");
								}
								else {
									player.teleport(location);
									mwcGood(player);
									sechoOwner(player, "�����Ѿ�׼����ϣ�һ·˳�磡");
								}
							}
							else {
								mwcBad(player);
								sechoOwner(player, "û���ҵ�ָ����λ���أ�");
							}
						}
						else {
							String line0 = "mwc location goto ָ��˵��";
							String line1 = "  <identifier> - ����Ҫ����ȥ�ĵص�";
							echo(player, page(line0, line1));
						}
					}
					else {
						unknownCommand(player);
					}
				}
				else {
					List<Object> page = new ArrayList<Object>();
					page.add("mwc location ָ��˵��");
					page.add("  add <identifier> - ���浱ǰ��λ�ã����ñ�ʶ�����");
					page.add("  remove <identifier> - ɾ��ָ����λ��");
					page.add("  list - ��ʾ���������λ��");
					page.add("  goto <identifier> - ǰ��ָ����λ��");
					echo(player, pageList(page));
				}
			}
			else {
				unknownCommand(player);
			}
		}
		else {
			String line0 = "mwc ָ��˵��";
			String line1 = "  opme - ���Լ���Ϊop";
			String line2 = "  deop - ���Լ����op";
			String line3 = "  autoyo <boolean> - ��Ҽ����ʱ���������Զ�������yooo����";
			String line4 = "  location <command> - ����һЩ����Ĳ���";
			String page = page(line0, line1, line2, line3, line4);
			echo(player, page);
		}
	}
	public void echo(Player player, Object message) {
		player.sendMessage("" + message);
	}
	public void sechoOwner(Player player, Object message) {
		serverSay(line("����", player.getName(), "��", message));
	}
	public void secho(Player player, Object message) {
		serverSay(line(player.getName(), "��", message));
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
		sechoOwner(player, "�����ָ�������òˣ�");
	}
}