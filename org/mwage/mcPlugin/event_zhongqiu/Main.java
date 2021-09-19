package org.mwage.mcPlugin.event_zhongqiu;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
public class Main extends JavaPlugin {
	public final String EVENT_WORLD_NAME = "Zhongqiu";
	// public final List<Player> gamePlayers = new ArrayList<Player>();
	// public final Map<UUID, PlayerMeta> playerMetas = new HashMap<UUID, PlayerMeta>();
	public PlayerMetaManager playerMetaManager = new PlayerMetaManager(this);
	public double wolf_man_rate = 0.2;
	public int normal_wolf_countdown = 90;
	public int crazy_wolf_countdown = 90;
	public int game_length = 1200;
	public int end_game_length = 150;
	public Location game_start_location;
	public boolean gamePrepared = false;
	public boolean gameStarted = false;
	public boolean endgameStarted = false;
	public int currentGameLength = 0;
	public int currentEndgameLength = 0;
	public GameTimer gameTimer = new GameTimer();
	public EndgameTimer endgameTimer = new EndgameTimer();
	public BossBar gameCounter = Bukkit.createBossBar("船只沉没倒计时", BarColor.RED, BarStyle.SOLID);
	public BossBar endgameCounter = Bukkit.createBossBar("船只起航倒计时", BarColor.GREEN, BarStyle.SOLID);
	{
		gameCounter.setVisible(false);
		endgameCounter.setVisible(false);
	}
	@Override
	public void onEnable() {
		initFiles();
		readConfig();
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerEnterGameListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerFightListener(this), this);
		// Bukkit.getPluginManager().registerEvents(new TestListener(this), this);
	}
	public void initFiles() {
		List<String> fileNames = new ArrayList<String>();
		fileNames.add("config.yml");
		for(String fileName : fileNames) {
			File file = new File(getDataFolder(), fileName);
			if(!file.exists()) {
				saveResource(fileName, false);
			}
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("zhongqiu")) {
			return false;
		}
		int length = args.length;
		if(length == 0) {
			sender.sendMessage("type /zhongqiu help for helps for this plugin!");
			return true;
		}
		else if(length == 1) {
			String arg0 = args[0];
			if(arg0.equalsIgnoreCase("help")) {
				List<Object> message = new ArrayList<Object>();
				message.add("/zhongqiu prepare - 做好游戏前准备（包括调整物品栏、血条、饥饿条、分配角色）");
				message.add("/zhongqiu start - 正式开始游戏（开始游戏计时）");
				message.add("/zhongqiu endgame - 玩家成功修船，进入游戏末尾阶段");
				message.add("/zhongqiu stop - 强制停止游戏");
				message.add("/zhongqiu reload - 重新加载配置文件");
				sendMessage(sender, message);
				return true;
			}
			if(arg0.equals("reload")) {
				readConfig();
				sender.sendMessage("重新加载了配置文件");
				sender.sendMessage("警告：如果您在游戏进行中使用此指令，可能造成未知错误！");
				return true;
			}
			if(arg0.equals("prepare")) {
				if(gameStarted) {
					sender.sendMessage("错误：游戏已经开始。如果需要重置，请先结束当前游戏");
					return true;
				}
				prepareGame();
				sender.sendMessage("游戏已经准备完成，可以开始游戏");
				sender.sendMessage("输入“/zhongqiu start”开始游戏");
				return true;
			}
			if(arg0.equals("start")) {
				if(!gamePrepared) {
					sender.sendMessage("错误：尚未完成游戏准备！");
					sender.sendMessage("输入“/zhongqiu prepare”准备游戏");
					return true;
				}
				if(gameStarted) {
					sender.sendMessage("错误：游戏已经开始！");
					sender.sendMessage("输入“/zhongqiu stop”停止游戏");
					return true;
				}
				startGame();
				sender.sendMessage("游戏已经开始");
				sender.sendMessage("输入“/zhongqiu endgame”进入游戏后期");
				sender.sendMessage("输入“/zhongqiu stop”停止游戏");
				return true;
			}
			if(arg0.equals("endgame")) {
				if(!gamePrepared) {
					sender.sendMessage("错误：尚未完成游戏准备！");
					sender.sendMessage("输入“/zhongqiu prepare”准备游戏");
					return true;
				}
				if(!gameStarted) {
					sender.sendMessage("错误：游戏尚未开始！");
					sender.sendMessage("输入“/zhongqiu start”开始游戏");
					return true;
				}
				if(endgameStarted) {
					sender.sendMessage("错误：游戏已经进入后期！");
					sender.sendMessage("输入“/stop”停止游戏");
					return true;
				}
				endGame();
				sender.sendMessage("游戏已经进入后期");
				sender.sendMessage("输入“/zhongqiu stop”停止游戏");
			}
			if(arg0.equals("stop")) {
				stopGame();
				sender.sendMessage("游戏停止");
			}
		}
		return false;
	}
	public void prepareGame() {
		// to prepare players
		// playerMetas.clear();
		currentGameLength = 0;
		currentEndgameLength = 0;
		playerMetaManager.clear();
		gameTimer = new GameTimer();
		endgameTimer = new EndgameTimer();
		for(Player player : getGamePlayers()) {
			if(player.isOp()) {
				continue;
			}
			player.setGameMode(GameMode.SURVIVAL);
			player.setFoodLevel(20);
			player.setHealth(20);
			Location location = player.getLocation();
			World world = location.getWorld();
			if(world.getName().equalsIgnoreCase(EVENT_WORLD_NAME)) {
				playerMetaManager.register(player);
				player.getInventory().clear();
			}
			double r = Math.random();
			if(r < wolf_man_rate) {
				assignIdentity(player, Identity.NormalWolf);
			}
			else {
				assignIdentity(player, Identity.Villager);
			}
			player.teleport(game_start_location);
			gameCounter.addPlayer(player);
			endgameCounter.addPlayer(player);
			player.giveExpLevels(-player.getLevel());
		}
		gameCounter.setVisible(false);
		endgameCounter.setVisible(false);
		gamePrepared = true;
	}
	@SuppressWarnings("deprecation")
	public void startGame() {
		gameTimer.runTaskTimer(this, 20, 20);
		gameStarted = true;
		String title = ChatColor.GREEN + "游戏正式开始！";
		String subTitle = "修船、生存、不要被杀！";
		getGamePlayers().forEach(p -> {
			p.sendTitle(title, subTitle);
			PlayerMeta meta = playerMetaManager.register(p);
			if(meta.identity == Identity.NormalWolf) {
				meta.normalWolfTimer.runTaskTimer(this, 20, 20);
			}
		});
		gameCounter.setVisible(true);
	}
	@SuppressWarnings("deprecation")
	public void endGame() {
		gameTimer.cancel();
		endgameTimer.runTaskTimer(this, 20, 20);
		endgameStarted = true;
		String title = ChatColor.GREEN + "船只已经修复完成！";
		String subTitle = "船只将在" + end_game_length + "秒后起航！";
		List<Object> villagerMessage = new ArrayList<Object>();
		List<Object> normalWolfMessage = new ArrayList<Object>();
		List<Object> crazyWolfMessage = new ArrayList<Object>();
		villagerMessage.add("太棒了！船只已经修复了！");
		villagerMessage.add("再坚持一会，您将成为胜利者！");
		normalWolfMessage.add("船只已经修复！");
		normalWolfMessage.add("坚持住，不要狂躁化！只要您的身份不暴露，您就会获胜！");
		crazyWolfMessage.add("喜忧参半：船只已经修复。");
		crazyWolfMessage.add("好消息是，您不用担心喂鱼了。");
		crazyWolfMessage.add("坏消息是，普通人已经知道了您的身份，您必须将他们全部灭口才能获得胜利！");
		getGamePlayers().forEach(p -> {
			p.sendTitle(title, subTitle);
			PlayerMeta meta = playerMetaManager.register(p);
			switch(meta.identity) {
				case Villager :
					sendMessage(p, villagerMessage);
					break;
				case NormalWolf :
					sendMessage(p, normalWolfMessage);
					break;
				case CrazyWolf :
					sendMessage(p, crazyWolfMessage);
				default :
					break;
			}
		});
		gameCounter.setVisible(false);
		endgameCounter.setVisible(true);
	}
	@SuppressWarnings("deprecation")
	public void stopGame() {
		try {
			gameTimer.cancel();
		}
		catch(IllegalStateException e) {
		}
		try {
			endgameTimer.cancel();
		}
		catch(IllegalStateException e) {
		}
		playerMetaManager.clear();
		String title = "游戏停止";
		String subTitle = "游戏被指令停止了";
		getGamePlayers().forEach(p -> {
			p.sendTitle(title, subTitle);
		});
		gamePrepared = false;
		gameStarted = false;
		endgameStarted = false;
	}
	@SuppressWarnings("deprecation")
	public void assignIdentity(Player player, Identity identity) {
		PlayerMeta meta = playerMetaManager.register(player);
		meta.identity = identity;
		String title = "";
		String subTitle = "";
		List<String> message = new ArrayList<String>();
		switch(identity) {
			case Villager :
				title = ChatColor.YELLOW + "普通人";
				subTitle = "请团结协力修好船，生存下去";
				message.add("您是一名普通船员。");
				message.add("这艘船已经触礁，将在" + game_length + "秒后沉没！如果想要存活，需要和大家同心协力修好船。");
				message.add("小心！这艘船上可能有狼人！他们对您的生存的威胁同样严重！");
				message.add("生存，不择手段的生存！只有幸存者才有可能是赢家！");
				meta.endIdentity = Identity.Villager;
				break;
			case NormalWolf :
				title = ChatColor.RED + "狼人";
				subTitle = "请修船的同时确保自己的生存";
				message.add("您是一名狼人。");
				message.add("这艘船已经触礁，将在" + game_length + "秒后沉没！如果想要存活，需要和大家同心协力修好船。");
				message.add("同时您的杀戮欲望已经觉醒，您已经开启了" + normal_wolf_countdown + "秒的倒计时！");
				message.add("如果您在倒计时内杀死另一名玩家，则可以暂时压制杀戮欲望（倒计时重置）");
				message.add("如果不能，您则会狂暴化。如果狂暴化，您无法完成修船任务，或者捡起任何物品，而且其它玩家也都看得出您的真面目！");
				message.add("谨慎行事！修船和保持自身的不暴露一样重要！");
				message.add("生存，不择手段的生存！只有幸存者才有可能是赢家！");
				meta.endIdentity = Identity.NormalWolf;
				break;
			case CrazyWolf :
				title = ChatColor.DARK_PURPLE + "狂暴化狼人";
				subTitle = "杀死所有村民！";
				message.add("您狂暴化了！");
				message.add("您的杀戮欲望已经失控，您已经开启了" + crazy_wolf_countdown + "秒的倒计时");
				message.add("如果您在倒计时内杀死一名玩家，则可以暂时存活（倒计时重置）");
				message.add("如果不能，您将会血液沸腾而死！");
				message.add("注意：不能让村民或者离开这片海域，否则您会被人类社会迫害而死！（您的狼人身份已经彻底暴露了）");
				meta.endIdentity = Identity.CrazyWolf;
				break;
			case Observer :
				title = ChatColor.AQUA + "观察者";
				subTitle = "您已经出局，不再可能获得本局游戏的胜利";
				message.add("您已经出局，请遵守游戏规则！");
				message.add("请牢记一点：死人是不能说话的！");
				break;
			default :
				break;
		}
		player.sendTitle(title, subTitle);
		for(String line : message) {
			player.sendMessage(line);
		}
	}
	public void crazilize(Player player) {
		dropAll(player);
		PlayerInventory inv = player.getInventory();
		inv.setHelmet(StaticData.wolf_head);
		PlayerMeta meta = playerMetaManager.register(player);
		meta.crazyWolfTimer.runTaskTimer(this, 20, 20);
		assignIdentity(player, Identity.CrazyWolf);
	}
	public void dropAll(Player player) {
		PlayerInventory inv = player.getInventory();
		inv.setHelmet(StaticData.wolf_head);
		player.dropItem(true);
		inv.setItemInMainHand(inv.getItemInOffHand());
		inv.setItemInOffHand(null);
		player.dropItem(true);
		ItemStack[] items = inv.getStorageContents();
		int length = items.length;
		for(int i = 0; i < length; i++) {
			if(items[i] != null) {
				inv.setItemInMainHand(items[i]);
				player.dropItem(true);
			}
		}
		inv.clear();
	}
	@SuppressWarnings("deprecation")
	public void killPlayer(final Player player, final DeathReason reason) {
		String title = ChatColor.RED + "阵亡";
		String subTitle = "";
		switch(reason) {
			case Murdered :
				subTitle = "您死于他人之手";
				break;
			case BloodBurn :
				subTitle = "您死于血液沸腾";
				break;
			default :
				subTitle = "您死于意外";
		}
		player.sendTitle(title, subTitle);
		PotionEffect blinder = new PotionEffect(PotionEffectType.BLINDNESS, 200, 10);
		player.addPotionEffect(blinder);
		player.setGameMode(GameMode.SPECTATOR);
		class ObserverChanger extends BukkitRunnable {
			@Override
			public void run() {
				assignIdentity(player, Identity.Observer);
			}
		}
		ObserverChanger oc = new ObserverChanger();
		oc.runTaskLater(this, 200);
		PlayerMeta meta = playerMetaManager.register(player);
		meta.identity = Identity.Observer;
		try {
			meta.normalWolfTimer.cancel();
		}
		catch(IllegalStateException e) {
		}
		try {
			meta.crazyWolfTimer.cancel();
		}
		catch(IllegalStateException e) {
		}
		dropAll(player);
	}
	public Set<Player> getGamePlayers() {
		Set<Player> gamePlayers = new HashSet<Player>();
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for(Player player : players) {
			if(player.isOp()) {
				continue;
			}
			Location location = player.getLocation();
			World world = location.getWorld();
			String worldName = world.getName();
			if(worldName.equalsIgnoreCase(EVENT_WORLD_NAME)) {
				gamePlayers.add(player);
			}
		}
		return gamePlayers;
	}
	private void readConfig() {
		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
		wolf_man_rate = fileConfig.getDouble("wolf-man-rate", wolf_man_rate);
		normal_wolf_countdown = fileConfig.getInt("normal-wolf-countdown", normal_wolf_countdown);
		crazy_wolf_countdown = fileConfig.getInt("crazy-wolf-countdown", crazy_wolf_countdown);
		game_length = fileConfig.getInt("game-length", game_length);
		double gslX = fileConfig.getDouble("game-start-location.x", 0);
		double gslY = fileConfig.getDouble("game-start-location.y", 0);
		double gslZ = fileConfig.getDouble("game-start-location.z", 0);
		World gameWorld = Bukkit.getWorld("zhongqiu");
		game_start_location = new Location(gameWorld, gslX, gslY, gslZ);
	}
	private void sendMessage(CommandSender sender, List<Object> message) {
		for(Object line : message) {
			String str = "" + line;
			sender.sendMessage(str);
		}
	}
	class GameTimer extends BukkitRunnable {
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			if(!gamePrepared) {
				return;
			}
			if(!gameStarted) {
				return;
			}
			getGamePlayers().forEach(p -> {
				PlayerMeta meta = playerMetaManager.register(p);
				if(meta.identity == Identity.Observer) {
					p.setGameMode(GameMode.SPECTATOR);
				}
			});
			if(currentGameLength >= game_length) {
				String title = "船只沉没";
				String subTitle = "所有玩家都淹死了";
				List<Object> message = new ArrayList<Object>();
				message.add("船只已经沉没，游戏结束。");
				message.add("无人获胜。");
				getGamePlayers().forEach(p -> {
					sendMessage(p, message);
					p.sendTitle(title, subTitle);
				});
				countEndGame();
				cancel();
				return;
			}
			if(noOneLeft()) {
				String title = ChatColor.RED + "全员阵亡";
				String subTitle = "所有玩家都死了";
				getGamePlayers().forEach(p -> {
					p.sendTitle(title, subTitle);
				});
				countEndGame();
				cancel();
				return;
			}
			/*
			 * normal player means villagers and normal wolves,
			 * who can still fix the shoips.
			 */
			boolean hasNormalPlayer = false;
			for(Player player : getGamePlayers()) {
				PlayerMeta meta = playerMetaManager.register(player);
				Identity identity = meta.identity;
				switch(identity) {
					case Villager :
					case NormalWolf :
						hasNormalPlayer = true;
						break;
					default :
						break;
				}
			}
			if(!hasNormalPlayer) {
				String title = ChatColor.DARK_RED + "无法完成修船任务";
				String subTitle = "剩余的玩家已经不可能完成修船";
				List<Object> message = new ArrayList<Object>();
				message.add("拥有修船能力的玩家已经全员阵亡。");
				message.add("无人获胜。");
				getGamePlayers().forEach(p -> {
					sendMessage(p, message);
					p.sendTitle(title, subTitle);
				});
				countEndGame();
				cancel();
				return;
			}
			int timeRemaining = game_length - currentGameLength;
			String title = ChatColor.DARK_RED + "船只有沉没的危险！";
			String subTitle = "船只将在" + timeRemaining + "秒后沉没！";
			if(timeRemaining == 10 || timeRemaining == 30 || timeRemaining == 60 || timeRemaining == 300) {
				getGamePlayers().forEach(p -> {
					p.sendTitle(title, subTitle);
				});
			}
			double progress = ((double)timeRemaining) / game_length;
			gameCounter.setProgress(progress);
			gameCounter.setTitle("船只沉没倒计时：" + timeRemaining);
			currentGameLength++;
		}
	}
	class EndgameTimer extends BukkitRunnable {
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			if(!gamePrepared) {
				return;
			}
			if(!gameStarted) {
				return;
			}
			if(!endgameStarted) {
				return;
			}
			getGamePlayers().forEach(p -> {
				PlayerMeta meta = playerMetaManager.register(p);
				if(meta.identity == Identity.Observer) {
					p.setGameMode(GameMode.SPECTATOR);
				}
			});
			if(currentEndgameLength >= end_game_length) {
				String title = ChatColor.GREEN + "游戏结束";
				String subTitle = "玩家信息请看聊天栏结算界面";
				getGamePlayers().forEach(p -> {
					p.sendTitle(title, subTitle);
				});
				countEndGame();
				cancel();
				return;
			}
			if(noOneLeft()) {
				String title = ChatColor.RED + "全员阵亡";
				String subTitle = "所有玩家都死了";
				getGamePlayers().forEach(p -> {
					p.sendTitle(title, subTitle);
				});
				countEndGame();
				cancel();
				return;
			}
			int timeRemaining = end_game_length - currentEndgameLength;
			String title = ChatColor.GREEN + "船只即将起航";
			String subTitle = "船只将在" + timeRemaining + "秒后起航！";
			if(timeRemaining == 10 || timeRemaining == 30 || timeRemaining == 60) {
				getGamePlayers().forEach(p -> {
					p.sendTitle(title, subTitle);
				});
			}
			double progress = ((double)timeRemaining) / end_game_length;
			endgameCounter.setProgress(progress);
			endgameCounter.setTitle("船只起航倒计时：" + timeRemaining);
			currentEndgameLength++;
		}
	}
	@SuppressWarnings("deprecation")
	public void countEndGame() {
		if(!gameStarted) {
			return;
		}
		Set<UUID> uuids = playerMetaManager.playerMetas.keySet();
		Set<PlayerMeta> metas = new HashSet<PlayerMeta>();
		for(UUID uuid : uuids) {
			metas.add(playerMetaManager.register(uuid));
		}
		Set<OfflinePlayer> villagers = new HashSet<OfflinePlayer>();
		Set<OfflinePlayer> normalWolves = new HashSet<OfflinePlayer>();
		Set<OfflinePlayer> crazyWolves = new HashSet<OfflinePlayer>();
		Set<OfflinePlayer> livingPlayers = new HashSet<OfflinePlayer>();
		metas.forEach(meta -> {
			OfflinePlayer p = Bukkit.getOfflinePlayer(meta.uuid);
			switch(meta.endIdentity) {
				case Villager :
					villagers.add(p);
					break;
				case NormalWolf :
					normalWolves.add(p);
					break;
				case CrazyWolf :
					crazyWolves.add(p);
					break;
				default :
					break;
			}
			switch(meta.identity) {
				case Observer :
					break;
				default :
					livingPlayers.add(p);
			}
		});
		Bukkit.broadcastMessage("==========结算游戏==========");
		if(!endgameStarted) {
			Bukkit.broadcastMessage("本局游戏无人获胜。");
		}
		else if(livingPlayers.isEmpty()) {
			Bukkit.broadcastMessage("本局游戏无人获胜。");
		}
		else {
			Bukkit.broadcastMessage("以下玩家为获胜玩家：");
			boolean hasLivingVillager = false;
			for(PlayerMeta meta : metas) {
				if(meta.identity == Identity.Villager) {
					hasLivingVillager = true;
					break;
				}
			}
			if(hasLivingVillager) {
				for(PlayerMeta meta : metas) {
					if(meta.identity == Identity.Villager || meta.identity == Identity.NormalWolf) {
						Bukkit.broadcastMessage("  " + meta.name);
					}
				}
			}
			else {
				for(PlayerMeta meta : metas) {
					if(meta.identity == Identity.NormalWolf || meta.identity == Identity.CrazyWolf) {
						Bukkit.broadcastMessage("  " + meta.name);
					}
				}
			}
		}
		Consumer<OfflinePlayer> printPlayerName = new Consumer<OfflinePlayer>() {
			@Override
			public void accept(OfflinePlayer t) {
				Bukkit.broadcastMessage("  " + t.getName());
			}
		};
		Bukkit.broadcastMessage("以下为本局玩家身份：");
		Bukkit.broadcastMessage(ChatColor.YELLOW + "村民：");
		villagers.forEach(printPlayerName);
		Bukkit.broadcastMessage(ChatColor.RED + "狼人：");
		normalWolves.forEach(printPlayerName);
		Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "狂躁化狼人：");
		crazyWolves.forEach(printPlayerName);
		Bukkit.broadcastMessage("====游戏结算完毕，本局结束====");
		playerMetaManager.clear();
		gameCounter.removeAll();
		endgameCounter.removeAll();
		gamePrepared = false;
		gameStarted = false;
		endgameStarted = false;
	}
	private boolean noOneLeft() {
		for(Player player : getGamePlayers()) {
			PlayerMeta meta = playerMetaManager.register(player);
			if(meta.identity != Identity.Observer) {
				return false;
			}
		}
		return true;
	}
}
class PlayerListener implements Listener {
	public final Main plugin;
	PlayerListener(Main plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Location location = player.getLocation();
		World world = location.getWorld();
		String worldName = world.getName();
		if(worldName.equalsIgnoreCase("zhongqiu")) {
			event.setDeathMessage("");
		}
	}
}
enum Identity {
	Villager,
	NormalWolf,
	CrazyWolf,
	Observer
}
enum DeathReason {
	Murdered,
	BloodBurn,
	Other
}