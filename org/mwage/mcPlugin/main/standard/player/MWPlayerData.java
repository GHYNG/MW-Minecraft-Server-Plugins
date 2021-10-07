package org.mwage.mcPlugin.main.standard.player;
import java.util.UUID;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
/**
 * 玩家数据。
 * 
 * @author GHYNG
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public abstract class MWPlayerData {
	/**
	 * 玩家的UUID。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public final UUID uuid;
	/**
	 * 产生一个新的玩家储存数据对象。
	 * 
	 * @param uuid
	 *            指定玩家的UUID。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public MWPlayerData(UUID uuid) {
		this.uuid = uuid;
	}
}