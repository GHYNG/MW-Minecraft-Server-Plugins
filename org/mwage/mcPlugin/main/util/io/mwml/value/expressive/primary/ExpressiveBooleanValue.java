package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressiveBooleanParser;
public interface ExpressiveBooleanValue<V extends ExpressiveBooleanValue<V, P, A>, P extends ExpressiveBooleanParser<P, V, A>, A> extends ExpressiveEnumValue<V, P, A> {}