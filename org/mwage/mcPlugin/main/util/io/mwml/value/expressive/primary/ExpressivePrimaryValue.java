package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressivePrimaryParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressiveValue;
public interface ExpressivePrimaryValue<V extends ExpressivePrimaryValue<V, P, E, A>, P extends ExpressivePrimaryParser<P, V, E, A>, E, A> extends ExpressiveValue<V, P, E, A> {
}