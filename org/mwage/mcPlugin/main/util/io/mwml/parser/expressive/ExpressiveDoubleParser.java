package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressiveDoubleValue;
public interface ExpressiveDoubleParser<A> extends ExpressiveNumberParser<Double, A, ExpressiveDoubleValue<A>> {
	@Override
	default Double parseExpressiveInstanceFromPrimaryExpression(String expression) {
		return Double.parseDouble(expression);
	}
}