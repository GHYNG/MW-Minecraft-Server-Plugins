package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressiveIntValue;
public interface ExpressiveIntParser<A> extends ExpressiveNumberParser<Integer, A, ExpressiveIntValue<A>> {
	@Override
	default Integer parseExpressiveInstanceFromPrimaryExpression(String expression) {
		return Integer.parseInt(expression);
	}
}