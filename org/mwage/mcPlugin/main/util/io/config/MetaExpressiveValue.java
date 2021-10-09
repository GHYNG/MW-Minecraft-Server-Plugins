package org.mwage.mcPlugin.main.util.io.config;
/**
 * 元表达值。
 * <p>
 * 元表达值指的是用于表达表达值的表达值，
 * 换句话说，是不可被进一步分解的表达值。
 * 比如，它们可以是更复杂的表达值（比如Table或者List）的成员。
 * 
 * @author GHYNG
 * @param <E>
 *            表达类型。
 * @param <A>
 *            实际类型。
 */
public interface MetaExpressiveValue<E, A> extends Value<E, A> {
	public MetaExpressiveValue<E, A> getValue(E expressiveInstance);
	/**
	 * 根据给定的字符串（如果有引号的话，会包含引号），
	 * 分析产生表达实例（比如Integer）。
	 * 
	 * @param content
	 *            给定的字符串。
	 * @return 表达实例。
	 */
	public E parseExpressiveInstanceFromString(String content);
	/**
	 * 根据给定的字符串（如果有引号的话，会包含引号），
	 * 产生一个新的值。
	 * 
	 * @param content
	 *            给定的字符串。
	 * @return 新的值。
	 */
	public MetaExpressiveValue<E, A> parseValueFromString(String content);
	/**
	 * 这个值被写入文件时的字符串形式。
	 * 相当于{@code toString()}。
	 * 
	 * @return 被写入文件的字符串。
	 */
	public String toContent();
}