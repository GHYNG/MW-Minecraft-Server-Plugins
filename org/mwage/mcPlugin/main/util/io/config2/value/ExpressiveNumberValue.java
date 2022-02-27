package org.mwage.mcPlugin.main.util.io.config2.value;

import org.mwage.mcPlugin.main.util.io.config2.parser.ExpressiveNumberParser;

public interface ExpressiveNumberValue<E extends Number, A> extends ExpressivePrimaryValue<E, A> {
	@Override
	ExpressiveNumberParser<E, A, ? extends ExpressiveNumberValue<E, A>> getParser();
}