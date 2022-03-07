package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressiveLongParser;
public interface ExpressiveLongValue<V extends ExpressiveLongValue<V, P, A>, P extends ExpressiveLongParser<P, V, A>, A> extends ExpressiveNumberValue<V, P, Long, A> {}