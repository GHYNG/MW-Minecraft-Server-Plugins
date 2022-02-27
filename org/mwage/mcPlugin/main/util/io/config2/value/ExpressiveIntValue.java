package org.mwage.mcPlugin.main.util.io.config2.value;
import org.mwage.mcPlugin.main.util.io.config2.parser.ExpressiveIntParser;
public interface ExpressiveIntValue<A> extends ExpressiveNumberValue<Integer, A> {
	@Override
	ExpressiveIntParser<A> getParser();
	@Override
	default String toExpression() {
		return getExpressiveInstance().toString();
	}
}