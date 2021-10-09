package org.mwage.mcPlugin.main.util.io.config;
import java.lang.annotation.Annotation;
import java.util.Set;
/**
 * 配置文件系统中的值。
 * <p>
 * 这些值有不同的类型，
 * 在程序中，用两种类型来表示值的类型：表达类型和实际类型。
 * 表达类型指的是该类型在程序中的储存方法，
 * 实际类型指的是该类型的实际指代对象。
 * <p>
 * 比如，构建一个Value类：
 * <p>
 * {@code public class ColorValue extends Value<Map<String, IntValue>, Color>}
 * <p>
 * 在这个类中，
 * Map是储存类型，而Color是实际类型。
 * <p>
 * 这个类是不可变的，
 * 意味着它的成员不可改变。
 * 通过运算产生的值将是一个新的实例。
 * 
 * @author GHYNG
 * @param <E>
 *            表达类型。
 * @param <A>
 *            实际类型。
 */
public interface Value<E, A> {
	/**
	 * 获取这个值的类型所要求的关键字。
	 * 这些关键字不再被允许用于给键命名。
	 * 有些类型有自己的关键字，有些没有。
	 * 
	 * @return
	 */
	public Set<String> getKeywords();
	/**
	 * 获取表达类型的实例。
	 * 
	 * @return 表达类型实例。
	 */
	public E getExpressiveInstance();
	/**
	 * 获取实际类型的实例。
	 * 
	 * @return 实际类型实例。
	 */
	public A getActualInstance();
	/**
	 * 获取null值实例。
	 * 
	 * @return null值实例。
	 */
	public default NullValue<E, A> getValueNull() {
		return new NullValueInstance<E, A>();
	}
	/**
	 * 判断该值类型是否是可以直接从简单字符串解析的。
	 * 确切的说，
	 * 该值的类型有{@code @MetaParsable}注解。
	 * 
	 * @return 真，可解析；假，否则。
	 */
	default boolean isMetaParsable() {
		Class<?> clazz = getClass();
		Annotation parsable = clazz.getAnnotation(MetaParsable.class);
		return parsable != null;
	}
}