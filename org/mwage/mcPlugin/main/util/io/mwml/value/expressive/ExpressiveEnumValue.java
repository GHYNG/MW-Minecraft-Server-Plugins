package org.mwage.mcPlugin.main.util.io.mwml.value.expressive;

import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.ExpressiveEnumParser;

public interface ExpressiveEnumValue<A> extends ExpressivePrimaryValue<String, A> {
	@Override
	ExpressiveEnumParser<A, ? extends ExpressiveEnumValue<A>> getParser();
	@Override
	default String toExpression() {
		return getExpressiveInstance();
	}
}