package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressiveDoubleParser;
public interface ExpressiveDoubleValue<V extends ExpressiveDoubleValue<V, P, A>, P extends ExpressiveDoubleParser<P, V, A>, A> extends ExpressiveNumberValue<V, P, Double, A> {}