package org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary.ActualNumberParser;
public interface ActualNumberValue<V extends ActualNumberValue<V, P, E , A>, P extends ActualNumberParser<P, V, E, A>, E, A extends Number> extends ActualPrimaryValue<V, P, E, A> {}