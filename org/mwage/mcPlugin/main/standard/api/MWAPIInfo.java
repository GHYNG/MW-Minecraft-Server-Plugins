package org.mwage.mcPlugin.main.standard.api;
import java.lang.annotation.Documented;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
/**
 * 奶路API注解。
 * <p>
 * 这个注解用于标注插件API版本（而不是插件版本）。
 * 每一个插件API版本应该以0开始。
 * <p>
 * 注意：插件API版本应该是本插件范围内共享的，
 * 而不是不同的功能各自使用不同的标记进度。
 * <p>
 * 当插件更新时，插件的版本就会前进，
 * 但是插件API版本却不一定。
 * 只有当插件出现新功能时
 * （确切的说，出现新的可以调用的API内容时），
 * 插件API版本才会前进。
 * <p>
 * API版本应该有3个值：
 * 
 * <pre>
 * {@code
 * -----------------------------------------------------------------------------------------------
 * startsAt	第一次出现该功能时的版本号，或者未来上线时的版本号；	默认为-1，意味着该功能实际上不在使用；
 * currentlyAy	该功能最后一次修改时的版本号；			默认为-1，意味着上线后未作修改；
 * endsAt 	该功能（预期）停止支持时的版本号；			默认为-1，意味着为打算停止支持；
 * -----------------------------------------------------------------------------------------------
 * }
 * </pre>
 * 
 * 这个标记可以有效的提示下级插件，本插件的支持版本。
 * 当下级插件调用上级插件的功能时，
 * 应当指示它所调用的上级插件的API版本号，
 * 以免出现不能兼容的情况。
 * <p>
 * 注意：该注解不应该直接被插件直接调用，
 * 而是应该用于注解每个奶路插件自有的版本注解。
 * 
 * @author GHYNG
 */
@Documented
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
public @interface MWAPIInfo {
	/**
	 * 这个功能出现时的API版本号。
	 */
	public int startsAt() default -1;
	/**
	 * 这个功能最后一次修改时的版本号。
	 */
	public int currentlyAt() default -1;
	/**
	 * 这个功能结束支持时的版本号。
	 */
	public int endsAt() default -1;
	/**
	 * 这个功能是否可以被下级插件调用。
	 * 注意，尽管有些功能是{@code public}的，
	 * 但是如果并不希望这些功能被子插件调用的话，
	 * 仍然应该标记为{@code false}。
	 * <p>
	 * 默认为{@code true}。
	 */
	public boolean openToSubPlugin() default true;
	/**
	 * 最后一次修改时的插件版本。
	 * 当修改版本与API版本同步变动时，不需要标注。
	 */
	public String lastModified() default "with API version";
}