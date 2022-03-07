package org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary.ActualLongParser;
public interface ActualLongValue<V extends ActualLongValue<V, P, E>, P extends ActualLongParser<P, V, E>, E> extends ActualNumberValue<V, P, E, Long> {}