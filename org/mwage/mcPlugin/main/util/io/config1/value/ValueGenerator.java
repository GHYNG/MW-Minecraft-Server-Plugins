package org.mwage.mcPlugin.main.util.io.config1.value;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.mwage.mcPlugin.main.util.Function;
import org.mwage.mcPlugin.main.util.clazz.GenericTypeHeader;
import org.mwage.mcPlugin.main.util.clazz.GenericTypesInfoble;
@GenericTypeHeader(superClass = ValueGenerator.class, typeParamaterName = "E", typeParamater = Object.class)
@GenericTypeHeader(superClass = ValueGenerator.class, typeParamaterName = "A", typeParamater = Object.class)
@GenericTypeHeader(superClass = ValueGenerator.class, typeParamaterName = "V", typeParamater = Value.class)
@GenericTypeHeader(superClass = GenericTypesInfoble.class, typeParamaterName = "T", typeParamater = ValueGenerator.class)
public interface ValueGenerator<E, A, V extends Value<E, A>> extends GenericTypesInfoble<ValueGenerator<E, A, V>> {
	Class<E> getClassE();
	Class<A> getClassA();
	Class<V> getClassV();
	V generateFromInstanceE(CollectionValue<?, ?, ?, ?, ? super V> outerValue, E instanceExpressive);
	V generateFromInstanceA(CollectionValue<?, ?, ?, ?, ? super V> outerValue, A instanceActual);
	String getTypeName();
	Map<String, Function<V, Value<?, ?>>> getMethods();
	Set<String> getExtraKeywords();
	default Set<String> getKeywords() {
		Set<String> keywords = new HashSet<String>();
		keywords.addAll(getExtraKeywords());
		keywords.addAll(getMethods().keySet());
		return keywords;
	}
	default void registerMethod(String methodName, Function<V, Value<?, ?>> method) {
		if(!ValueGeneratorUtil.isGoodIdentifier(methodName)) {
			throw new IllegalIdentifierException("The given method name: \"" + methodName + "\" is not a valid identifier");
		}
		getMethods().put(methodName, method);
	}
	default void registerExtraKeyword(String keyword) {
		if(!ValueGeneratorUtil.isGoodIdentifier(keyword)) {
			throw new IllegalIdentifierException("The given keyword: \"" + keyword + "\" is not a valid identifier");
		}
		getExtraKeywords().add(keyword);
	}
	default void unregisterExtraKeyword(String keyword) {
		getExtraKeywords().remove(keyword);
	}
}
interface ValueGeneratorUtil {
	static boolean isGoodIdentifier(String str) {
		if(str == null) {
			return false;
		}
		if(str.equals("")) {
			return false;
		}
		char[] array = str.toCharArray();
		if(!Character.isJavaIdentifierStart(array[0])) {
			return false;
		}
		for(int i = 1; i < array.length; i++) {
			if(!Character.isJavaIdentifierPart(array[i])) {
				return false;
			}
		}
		return true;
	}
}