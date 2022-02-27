package org.mwage.mcPlugin.main.util.io.config2.parser;

import org.mwage.mcPlugin.main.util.io.config2.value.ExpressivePrimaryValue;

public interface ExpressivePrimaryParser<E, A, V extends ExpressivePrimaryValue<E, A>> extends Parser<E, A, V> {
	default V parseFromPrimaryExpression(String expression) {
		E expressiveInstance = parseExpressiveInstanceFromPrimaryExpression(expression);
		A actualInstance = parseActualInstanceFromExpressiveInstance(expressiveInstance);
		return parseFromActualInstance(actualInstance);
	}
	E parseExpressiveInstanceFromPrimaryExpression(String expression);
	A parseActualInstanceFromExpressiveInstance(E expressiveInstance);
}