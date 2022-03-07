package org.mwage.mcPlugin.main.util;
import java.lang.annotation.Target;
/**
 * 不可被直接实现的接口。
 * <p>
 * 被注解的结构不能被直接实现，
 * 但是可以被子接口继承。
 * 子接口不受限制。
 * 
 * @author GHYNG
 */
@Target(value = {
		java.lang.annotation.ElementType.TYPE
})
public @interface Unimplementable {}