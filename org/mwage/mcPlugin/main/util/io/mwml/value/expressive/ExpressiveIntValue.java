package org.mwage.mcPlugin.main.util.io.mwml.value.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.ExpressiveIntParser;
public interface ExpressiveIntValue<A> extends ExpressiveNumberValue<Integer, A> {
	@Override
	ExpressiveIntParser<A> getParser();
	@Override
	default String toExpression() {
		return getExpressiveInstance().toString();
	}
}