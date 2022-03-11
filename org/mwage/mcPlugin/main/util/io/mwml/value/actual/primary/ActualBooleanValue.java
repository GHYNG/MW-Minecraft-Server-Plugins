package org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary.ActualBooleanParser;
public interface ActualBooleanValue<V extends ActualBooleanValue<V, P, E>, P extends ActualBooleanParser<P, V, E>, E> extends ActualEnumValue<V, P, E, Boolean> {}