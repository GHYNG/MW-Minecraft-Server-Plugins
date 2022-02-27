package org.mwage.mcPlugin.main.util.io.config2.parser;
import java.util.Map;
import java.util.Set;
import org.mwage.mcPlugin.main.util.io.config2.value.ExpressiveEnumValue;
public interface ExpressiveEnumParser<A, V extends ExpressiveEnumValue<A>> extends ExpressivePrimaryParser<String, A, V> {
	Map<String, A> getActualLinks();
	default Set<String> getKeys() {
		return getActualLinks().keySet();
	}
	default V parseFromKey(String string) {
		A actualInstance = getActualLinks().get(string);
		if(actualInstance == null) {
			return null;
		}
		V value = parseFromActualInstance(actualInstance);
		return value;
	}
	@Override
	default V parseFromPrimaryExpression(String expression) {
		return parseFromKey(expression);
	}
	@Override
	default String parseExpressiveInstanceFromPrimaryExpression(String expression) {
		return expression;
	}
	@Override
	default A parseActualInstanceFromExpressiveInstance(String expressiveInstance) {
		return getActualLinks().get(expressiveInstance);
	}
}