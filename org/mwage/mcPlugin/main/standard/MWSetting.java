package org.mwage.mcPlugin.main.standard;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
/**
 * 用于存放某一个事物（比如玩家、世界）的设定信息的类。
 * 注意，这些设定信息是暂时的，将在下一次reload时重置。
 * <p>
 * 每一个设定信息类的对象包含了一个身份对象（id）。
 * 比如说，玩家（Player）类的身份是UUID对象，
 * 而世界（World）的身份是字符串对象。
 * 
 * @author GHYNG
 * @param <I>
 *            身份对象的类型。
 * @deprecated 未完成建设，请勿使用。
 */
@Deprecated
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0, openToSubPlugin = false))
public abstract class MWSetting<I> {
	/**
	 * 身份对象。
	 */
	public final I ID_OBJECT;
	/**
	 * 构建一个新的，所有值是默认的设定信息对象。
	 * 
	 * @param ID_Object
	 *            身份对象。
	 */
	public MWSetting(I ID_Object) {
		this.ID_OBJECT = ID_Object;
	}
}