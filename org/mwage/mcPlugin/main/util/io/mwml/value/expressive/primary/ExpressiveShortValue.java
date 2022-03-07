package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressiveShortParser;
public interface ExpressiveShortValue<V extends ExpressiveShortValue<V, P, A>, P extends ExpressiveShortParser<P, V, A>, A> extends ExpressiveNumberValue<V, P, Short, A> {}