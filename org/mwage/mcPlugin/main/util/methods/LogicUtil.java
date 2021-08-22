package org.mwage.mcPlugin.main.util.methods;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
/**
 * 逻辑相关工具方法。
 * 
 * @author GHYNG
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public interface LogicUtil {
	/**
	 * 逻辑非。
	 * 
	 * @param b
	 *            运算前布尔值。
	 * @return 运算后布尔值。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	default boolean not(boolean b) {
		return !b;
	}
	/**
	 * 逻辑与。
	 * 
	 * @param bs
	 *            布尔值参数。
	 * @return 运算结果。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	default boolean and(boolean... bs) {
		boolean r = true;
		for(boolean b : bs) {
			r &= b;
		}
		return r;
	}
	/**
	 * 逻辑或。
	 * 
	 * @param bs
	 *            布尔值参数。
	 * @return 运算结果。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	default boolean or(boolean... bs) {
		boolean r = false;
		for(boolean b : bs) {
			r |= b;
		}
		return r;
	}
	/**
	 * 逻辑与非。相当于{@code not(and(bs))}。
	 * 
	 * @param bs
	 *            布尔参数。
	 * @return 运算结果。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	default boolean nand(boolean... bs) {
		return not(and(bs));
	}
	/**
	 * 逻辑或非。相当于{@code not(or(bs))}。
	 * 
	 * @param bs
	 *            布尔参数。
	 * @return 运算结果。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	default boolean nor(boolean... bs) {
		return not(or(bs));
	}
}