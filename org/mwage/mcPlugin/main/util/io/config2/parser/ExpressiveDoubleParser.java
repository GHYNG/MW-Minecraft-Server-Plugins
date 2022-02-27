package org.mwage.mcPlugin.main.util.io.config2.parser;
import org.mwage.mcPlugin.main.util.io.config2.value.ExpressiveDoubleValue;
public interface ExpressiveDoubleParser<A> extends ExpressiveNumberParser<Double, A, ExpressiveDoubleValue<A>> {
	@Override
	default Double parseExpressiveInstanceFromPrimaryExpression(String expression) {
		return Double.parseDouble(expression);
	}
}