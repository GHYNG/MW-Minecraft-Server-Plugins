package org.mwage.mcPlugin.main.util.io.mwml.value.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.ExpressiveParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.Value;
public interface ExpressiveValue<V extends ExpressiveValue<V, P, E, A>, P extends ExpressiveParser<P, V, E, A>, E, A> extends Value<V, P, E, A> {}