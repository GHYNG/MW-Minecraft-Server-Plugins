package org.mwage.mcPlugin.main.util.io.config2.value;

import org.mwage.mcPlugin.main.util.io.config2.parser.ExpressiveEnumParser;

public interface ExpressiveEnumValue<A> extends ExpressivePrimaryValue<String, A> {
	@Override
	ExpressiveEnumParser<A, ? extends ExpressiveEnumValue<A>> getParser();
	@Override
	default String toExpression() {
		return getExpressiveInstance();
	}
}