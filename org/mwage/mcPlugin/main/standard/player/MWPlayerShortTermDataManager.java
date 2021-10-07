package org.mwage.mcPlugin.main.standard.player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
/**
 * 玩家短期数据管理器。
 * 所有数据将在游戏停止后失去。
 * <p>
 * 在该管理器实例产生后，
 * 管理器将自动监听玩家加入游戏事件，
 * 每一个加入游戏的玩家将会自动准备好数据。
 * 
 * @author GHYNG
 * @param <D>
 *            储存玩家短期数据的类。
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public abstract class MWPlayerShortTermDataManager<D extends MWPlayerShortTermData> extends MWPlayerDataManager<D> {
	/**
	 * 该管理器用于监听玩家事件的监听器。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected PlayerListener playerListener;
	/**
	 * 产生一个新的玩家短期数据管理器实例。
	 * 产生该实例后，
	 * 将会自动开始监听玩家事件。
	 * 
	 * @param plugin
	 *            调用该管理器的插件。
	 * @throws CreationBeforeEnabledException
	 *             在插件启动前就产生该实例。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public MWPlayerShortTermDataManager(MWPlugin plugin) throws CreationBeforeEnabledException {
		super(plugin);
		playerListener = new PlayerListener();
		plugin.registerListener(playerListener);
	}
	/**
	 * 停用该数据管理器。
	 * 这个过程是不可逆的。
	 * 监听器也将被注销。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public void disable() {
		playerListener.using = false;
		PlayerJoinEvent.getHandlerList().unregister(playerListener);
	}
	/**
	 * 玩家监听器。
	 * 用于监听玩家加入游戏事件。
	 * 每当玩家加入，
	 * 自动准备该玩家的数据。
	 * 
	 * @author GHYNG
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected class PlayerListener implements Listener {
		/**
		 * 该监听器是否正在使用。
		 */
		@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
		protected boolean using = true;
		/**
		 * 当玩家加入游戏时的操作
		 * （准备数据）。
		 * 
		 * @param event
		 *            玩家加入游戏事件。
		 */
		@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
		@EventHandler
		public void onPlayerJoin(PlayerJoinEvent event) {
			if(using) {
				readyPlayerData(event.getPlayer());
			}
		}
	}
}