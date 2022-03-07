package org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary.ActualFloatParser;
public interface ActualFloatValue<V extends ActualFloatValue<V, P, E>, P extends ActualFloatParser<P, V, E>, E> extends ActualNumberValue<V, P, E, Float> {}