package org.mwage.mcPlugin.main.util.io.mwml.value.expressive;

import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.ExpressiveNumberParser;

public interface ExpressiveNumberValue<E extends Number, A> extends ExpressivePrimaryValue<E, A> {
	@Override
	ExpressiveNumberParser<E, A, ? extends ExpressiveNumberValue<E, A>> getParser();
}