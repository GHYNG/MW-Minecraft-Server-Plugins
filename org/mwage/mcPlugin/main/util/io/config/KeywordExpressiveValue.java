package org.mwage.mcPlugin.main.util.io.config;
import java.util.Map;
import java.util.Set;
/**
 * 用关键字解析的值。
 * 
 * @author GHYNG
 * @param <C>
 *            关键字解析产生的计算值。通常和实际对象相同。
 * @param <A>
 *            实际对象。
 */
public interface KeywordExpressiveValue<C, A> extends MetaExpressiveValue<String, A> {
	public Map<String, KeywordExpressiveValue<C, A>> getKeywordTable();
	@Override
	public default KeywordExpressiveValue<C, A> getValue(String expressiveInstance) {
		return parseValueFromString(expressiveInstance);
	}
	@Override
	public default Set<String> getKeywords() {
		return getKeywordTable().keySet();
	}
	/**
	 * @return 写入文件的形式，这个字符串不会带有包围引号。
	 */
	@Override
	public default String toContent() {
		return getExpressiveInstance();
	}
	@Override
	public default String parseExpressiveInstanceFromString(String content) {
		return content;
	}
	@Override
	public default KeywordExpressiveValue<C, A> parseValueFromString(String content) {
		KeywordExpressiveValue<C, A> value = getKeywordTable().get(content);
		Class<?> clazz = getClass();
		if(value != null && clazz.isInstance(value)) {
			return value;
		}
		return new NullKeywordExpressiveValueInstance<C, A>();
	}
	/**
	 * 用于计算的对象。
	 * 
	 * @return 用于计算的对象。
	 */
	public C getCalculationInstance();
}