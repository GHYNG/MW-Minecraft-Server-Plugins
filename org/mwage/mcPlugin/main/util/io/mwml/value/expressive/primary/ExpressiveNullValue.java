package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressiveNullParser;
public interface ExpressiveNullValue<V extends ExpressiveNullValue<V, P, A>, P extends ExpressiveNullParser<P, V, A>, A> extends ExpressiveEnumValue<V, P, A> {}