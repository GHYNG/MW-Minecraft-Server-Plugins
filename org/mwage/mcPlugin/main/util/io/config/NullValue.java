package org.mwage.mcPlugin.main.util.io.config;
import java.util.HashSet;
import java.util.Set;
/**
 * 所有空置类型的父类型。
 * 
 * @author GHYNG
 * @param <E>
 *            表达类型。
 * @param <A>
 *            实际类型。
 */
public interface NullValue<E, A> extends Value<E, A> {
	public static String NAME_NULL = "null";
	public static Set<String> keyWords = new HashSet<String>();
	public static NullValueInitializer initializer = new NullValueInitializer();
	public static class NullValueInitializer {
		static {
			keyWords.add(NAME_NULL);
		}
	}
	@Override
	public default Set<String> getKeywords() {
		return keyWords;
	}
	/**
	 * @return null。
	 */
	@Override
	public default E getExpressiveInstance() {
		return null;
	}
	/**
	 * @return null。
	 */
	@Override
	public default A getActualInstance() {
		return null;
	}
}
class NullValueInstance<E, A> implements NullValue<E, A> {
	NullValueInstance() {
	}
}