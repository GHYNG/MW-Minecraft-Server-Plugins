package org.mwage.mcPlugin.main.standard.player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
/**
 * 玩家数据管理器。
 * <p>
 * 有两种关于玩家的数据：
 * <p>
 * 短期数据：MWShortTermPlayerData，
 * 每一次游戏运行周期结束后就会失去这些数据。
 * 这种数据可用于存放短期内容。
 * <p>
 * 长期数据：MWLongTermPlayerData，
 * 每一次游戏周期开始时，
 * 会从文件中读取数据。
 * 在游戏周期结束时，
 * 会将数据写入文件，
 * 这些数据可用于存放长期内容。
 * 
 * @author GHYNG
 * @param <D>
 *            用于储存玩家的数据的类。
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public abstract class MWPlayerDataManager<D extends MWPlayerData> {
	/**
	 * 调用该类的插件。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected final MWPlugin plugin;
	/**
	 * 数据管理器管理的每一个玩家的具体数据。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected final Map<UUID, D> datas = new HashMap<UUID, D>();
	/**
	 * 创建一个新的玩家数据管理器。
	 * 
	 * @param plugin
	 *            调用该管理器的插件。
	 * @throws CreationBeforeEnabledException
	 *             在插件生效前产生该管理器的实例。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public MWPlayerDataManager(MWPlugin plugin) throws CreationBeforeEnabledException {
		if(!plugin.isEnabled()) {
			throw new CreationBeforeEnabledException("Instance of MWPlayerDataManager must be created after plugin enabled.");
		}
		readyAllPlayersData();
		this.plugin = plugin;
	}
	/**
	 * 准备好一个玩家的数据。
	 * 如果这个数据已经产生且存在了管理器内部，
	 * 则直接返回这个数据。
	 * 如果这个数据未能产生，
	 * 则先产生该数据，然后存入管理器，
	 * 然后返回。
	 * 
	 * @param player
	 *            指定的玩家
	 * @return 准备完成的玩家数据。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public final D readyPlayerData(Player player) {
		UUID uuid = player.getUniqueId();
		D data = datas.get(uuid);
		if(data == null) {
			data = generatePlayerData(player);
			datas.put(uuid, data);
		}
		return data;
	}
	/**
	 * 准备好一个玩家的数据。
	 * 如果这个数据已经产生且存在了管理器内部，
	 * 则直接返回这个数据。
	 * 如果这个数据未能产生，
	 * 则先产生该数据，然后存入管理器，
	 * 然后返回。
	 * 
	 * @param player
	 *            指定的玩家
	 * @return 准备完成的玩家数据。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public final D readyPlayerData(UUID uuid) {
		D data = datas.get(uuid);
		if(data == null) {
			data = generatePlayerData(uuid);
			datas.put(uuid, data);
		}
		return data;
	}
	/**
	 * 准备完成所有玩家的数据。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public final void readyAllPlayersData() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			readyPlayerData(player);
		});
	}
	/**
	 * 产生一个新的玩家数据。
	 * 这个数据不会被自动保持入数据管理器，需要手动放置。
	 * 
	 * @param player
	 *            指定的玩家。
	 * @return 新的玩家数据。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected D generatePlayerData(Player player) {
		return generatePlayerData(player.getUniqueId());
	}
	/**
	 * 产生一个新的玩家的数据。
	 * 这个数据不会被自动保持入数据管理器，需要手动放置。
	 * 
	 * @param uuid
	 *            指定的玩家的UUID。
	 * @return 新的玩家数据。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected abstract D generatePlayerData(UUID uuid);
}
