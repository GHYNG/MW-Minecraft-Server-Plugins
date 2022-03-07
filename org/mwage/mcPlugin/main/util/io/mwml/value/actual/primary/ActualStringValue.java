package org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary.ActualStringParser;
public interface ActualStringValue<V extends ActualStringValue<V, P, E>, P extends ActualStringParser<P, V, E>, E> extends ActualPrimaryValue<V, P, E, String> {}