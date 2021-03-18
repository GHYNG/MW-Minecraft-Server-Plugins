package org.mwage.mcPlugin.main.standard.player;
import java.util.UUID;
import org.bukkit.entity.Player;
/**
 * 用于单个储存玩家信息的类。
 * <p>
 * 注意：这个类中储存的设定信息是临时的，
 * 会随着服务器的重启或reload丢失。
 * 如果需要长期的储存信息，
 * 应当寻求其他的方案。
 * <p>
 * 这是一个抽象类，意味着子插件的设置存储类应该继承此类。
 * 
 * @author GHYNG
 */
public abstract class MWPlayerSetting {
	/**
	 * 玩家的UUID。
	 */
	public final UUID uuid;
	/**
	 * 根据指定的UUID，创建一个新的对象。
	 * 
	 * @param uuid
	 *            指定的UUID。
	 */
	public MWPlayerSetting(UUID uuid) {
		this.uuid = uuid;
	}
	/**
	 * 根据指定的Player对象，创建一个新的对象。
	 * 
	 * @param player
	 *            指定的Player对象。
	 */
	public MWPlayerSetting(Player player) {
		this(player.getUniqueId());
	}
}