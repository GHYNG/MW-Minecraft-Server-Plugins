package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressivePrimaryValue;
public interface ExpressivePrimaryParser<E, A, V extends ExpressivePrimaryValue<E, A>> extends ExpressiveParser<E, A, V> {
	default V parseFromPrimaryExpression(String expression) {
		E expressiveInstance = parseExpressiveInstanceFromPrimaryExpression(expression);
		A actualInstance = parseActualInstanceFromExpressiveInstance(expressiveInstance);
		return parseFromActualInstance(actualInstance);
	}
	E parseExpressiveInstanceFromPrimaryExpression(String expression);
	A parseActualInstanceFromExpressiveInstance(E expressiveInstance);
}