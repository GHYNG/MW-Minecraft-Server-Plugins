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
			secho(player, "�ߣ���û��mwc��Ȩ�ޣ�");
			return;
		}
		MWC_PlayerSettings settings = plugin.getPlayerSettings();
		MWC_PlayerSetting setting = settings.get(player);
		List<String> commands = event.getCommandParts();
		int size = commands.size();
		if(size == 0) {
			List<Object> lines = new ArrayList<Object>();
			lines.add("mwc ָ��˵��");
			lines.add("  opme - ���Լ���Ϊop");
			lines.add("  deop - ���Լ����op");
			lines.add("  autoyo <boolean> - ��Ҽ����ʱ���������Զ�������yooo����");
			lines.add("  location <command> - ����һЩ����Ĳ���");
			lines.add("  sn <command> - ��һЩָ����ʱ�䷢��ʱ���һ�֪ͨ����");
			String page = pageList(lines);
			echo(player, page);
		}
		else {
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
				if(size == 1) {
					String line0 = "mwc autoyo ָ��˵��";
					String line1 = "  true - ��auto yooo";
					String line2 = "  false - �ر�auto yooo";
					String line3 = "  Ĭ��Ϊfalse��ĿǰΪ" + setting.autoyo;
					String page = page(line0, line1, line2, line3);
					echo(player, page);
				}
				else {
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
			}
			else if(c0.equalsIgnoreCase("location")) {
				if(size == 1) {
					List<Object> page = new ArrayList<Object>();
					page.add("mwc location ָ��˵��");
					page.add("  add <identifier> - ���浱ǰ��λ�ã����ñ�ʶ�����");
					page.add("  remove <identifier> - ɾ��ָ����λ��");
					page.add("  list - ��ʾ���������λ��");
					page.add("  clear - ������������λ��");
					page.add("  goto <identifier> - ǰ��ָ����λ��");
					echo(player, pageList(page));
				}
				else {
					String c1 = commands.get(1);
					if(c1.equalsIgnoreCase("add")) {
						if(size == 2) {
							List<Object> page = new ArrayList<Object>();
							page.add("mwc location add ָ��˵��");
							page.add("  <identifier> - ����λ�õı�ʶ��");
							echo(player, pageList(page));
						}
						else {
							String c2 = commands.get(2);
							if(!goodIdentifier(c2)) {
								sechoOwner(player, "�ⲻ�ǺϷ��ı�ʶ��Ӵ~");
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
							sechoOwner(player, line("����ǰλ����Ϊ", c2));
						}
					}
					else if(c1.equalsIgnoreCase("remove")) {
						if(size == 2) {
							String line0 = "mwc location remove ָ��˵��";
							String line1 = "  <identifier> - ɾ���ı�ʶ��";
							echo(player, page(line0, line1));
						}
						else {
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
					}
					else if(c1.equalsIgnoreCase("list")) {
						Set<String> keys = setting.locations.keySet();
						int numKeys = keys.size();
						if(numKeys == 0) {
							sechoOwner(player, "û���ҵ��κ��������λ���أ�");
						}
						else {
							sechoOwner(player, page("������������λ�ã�", setting.listLocations()));
						}
					}
					else if(c1.equalsIgnoreCase("goto")) {
						if(size == 2) {
							String line0 = "mwc location goto ָ��˵��";
							String line1 = "  <identifier> - ����Ҫ����ȥ�ĵص�";
							echo(player, page(line0, line1));
						}
						else {
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
					}
					else if(c1.equalsIgnoreCase("clear")) {
						setting.locations.clear();
						mwcGood(player);
						sechoOwner(player, "�Ѿ�Ϊ��ɾ�����еص㣡");
					}
					else {
						unknownCommand(player);
					}
				}
			}
			else if(c0.equalsIgnoreCase("sn")) {
				if(size < 3) {
					List<Object> lines = new ArrayList<Object>();
					lines.add("mwc sn ָ��˵��");
					lines.add("  <event_type> <sound_type> - ѡ��ĳ���¼�����Ч");
					lines.add("  event_type:");
					lines.add("    PlayerJoin - ��Ҽ���");
					lines.add("    PlayerQuit - ����˳�");
					lines.add("    PlayerDeath - �������");
					lines.add("    PlayerChat - ��ҷ���������Ϣ");
					lines.add("    All - ȫ��");
					lines.add("  sound_type");
					lines.add("    Piano - ����");
					lines.add("    Null - �ر�/������");
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
						sechoOwner(player, line("��Ϊ����עȫ���¼�����������ѡ��Ϊ", st));
						return;
					}
					mwcGood(player);
					sechoOwner(player, line("��Ϊ����ע", et, "����������ѡ��Ϊ", st));
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