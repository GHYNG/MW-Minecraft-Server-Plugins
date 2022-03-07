package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressiveIntParser;
public interface ExpressiveIntValue<V extends ExpressiveIntValue<V, P, A>, P extends ExpressiveIntParser<P, V, A>, A> extends ExpressiveNumberValue<V, P, Integer, A> {}