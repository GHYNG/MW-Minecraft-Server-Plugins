package org.mwage.mcPlugin.main.util;

import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;

/**
 * 函数过程的抽象化。
 * 有一个输入参数和一个输出参数。
 * 
 * @author GHYNG
 * @param <I>
 *            输入参数类型。
 * @param <O>
 *            输出参数类型。
 * @since Paper-1.16.5-MW-4
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public interface Function<I, O> {
	/**
	 * 调用这个函数，并且获得返回。
	 * 
	 * @param input
	 *            函数输入。
	 * @return 函数输出。
	 */
	O invoke(I input);
}