package org.mwage.mcPlugin.main.standard.player;
import java.util.UUID;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
/**
 * 玩家短期数据。
 * 
 * @author GHYNG
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public abstract class MWPlayerShortTermData extends MWPlayerData {
	/**
	 * 构造一个新的玩家短期数据储存对象。
	 * 
	 * @param uuid
	 *            玩家的UUID。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public MWPlayerShortTermData(UUID uuid) {
		super(uuid);
	}
}
