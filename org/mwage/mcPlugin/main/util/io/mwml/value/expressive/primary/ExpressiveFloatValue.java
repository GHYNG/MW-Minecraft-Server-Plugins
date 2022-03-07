package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressiveFloatParser;
public interface ExpressiveFloatValue<V extends ExpressiveFloatValue<V, P, A>, P extends ExpressiveFloatParser<P, V, A>, A> extends ExpressiveNumberValue<V, P, Float, A> {}