package org.mwage.mcPlugin.main.util.io.config2.value;

import org.mwage.mcPlugin.main.util.io.config2.parser.ExpressivePrimaryParser;

public interface ExpressivePrimaryValue<E, A> extends Value<E, A> {
	ExpressivePrimaryParser<E, A, ? extends ExpressivePrimaryValue<E, A>> getParser();
}