package org.mwage.mcPlugin.main.util;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
/**
 * 函数的抽象化。
 * 函数的输入和输出被打包成Record形式，
 * 用于更好的储存多个不同参数。
 * 
 * @author GHYNG
 * @since Paper-1.17.1-MW-1
 * @param <I>
 *            输入参数包。
 * @param <O>
 *            输出参数包。
 */
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public interface PackedFunction<I extends Record, O extends Record> extends Function<I, O> {}