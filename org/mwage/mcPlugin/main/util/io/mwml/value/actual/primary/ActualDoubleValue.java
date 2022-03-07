package org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary.ActualDoubleParser;
public interface ActualDoubleValue<V extends ActualDoubleValue<V, P, E>, P extends ActualDoubleParser<P, V, E>, E> extends ActualNumberValue<V, P, E, Double> {}