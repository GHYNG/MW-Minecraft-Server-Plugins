package org.mwage.mcPlugin.main.api;
import java.lang.annotation.Documented;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
/**
 * 主插件API注解。
 * <p>
 * 应当用这个注解各个主插件的内容。
 * 各子插件不应该用此注解来注解它们的功能。
 * 子插件可以使用此注解来表明子插件的依赖性。
 * 
 * @author GHYNG
 */
@Documented
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public @interface MWAPIInfo_Main {
	/**
	 * 标准化奶路API信息。
	 */
	public MWAPIInfo api() default @MWAPIInfo();
}