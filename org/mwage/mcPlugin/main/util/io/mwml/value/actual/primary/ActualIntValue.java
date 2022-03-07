package org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary.ActualIntParser;
public interface ActualIntValue<V extends ActualIntValue<V, P, E>, P extends ActualIntParser<P, V, E>, E> extends ActualNumberValue<V, P, E, Integer> {}