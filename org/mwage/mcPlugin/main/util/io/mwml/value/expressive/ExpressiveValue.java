package org.mwage.mcPlugin.main.util.io.mwml.value.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.ExpressiveParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.Value;
public interface ExpressiveValue<E, A> extends Value<E, A> {
	@Override
	ExpressiveParser<E, A, ? extends ExpressiveValue<E, A>> getParser();
}