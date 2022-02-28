package org.mwage.mcPlugin.main.util.io.mwml.value.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.ExpressivePrimaryParser;
public interface ExpressivePrimaryValue<E, A> extends ExpressiveValue<E, A> {
	ExpressivePrimaryParser<E, A, ? extends ExpressivePrimaryValue<E, A>> getParser();
}