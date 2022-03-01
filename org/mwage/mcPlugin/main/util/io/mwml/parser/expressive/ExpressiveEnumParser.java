package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive;
import java.util.Map;
import java.util.Set;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressiveEnumValue;
public interface ExpressiveEnumParser<A, V extends ExpressiveEnumValue<A>> extends ExpressivePrimaryParser<String, A, V> {
	Map<String, A> getActualLinks();
	default Set<String> getKeys() {
		return getActualLinks().keySet();
	}
	default V parseFromKey(Signature typeSignature, Signature expressionSignature, String string) {
		A actualInstance = getActualLinks().get(string);
		if(actualInstance == null) {
			return null;
		}
		V value = parseFromActualInstance(typeSignature, expressionSignature, actualInstance);
		return value;
	}
	@Override
	default V parseFromPureExpression(Signature typeSignature, Signature expressionSignature, String expression) {
		return parseFromKey(typeSignature, expressionSignature, expression);
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