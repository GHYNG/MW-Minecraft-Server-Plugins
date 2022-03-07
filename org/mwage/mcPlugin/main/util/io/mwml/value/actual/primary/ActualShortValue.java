package org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary.ActualShortParser;
public interface ActualShortValue<V extends ActualShortValue<V, P, E>, P extends ActualShortParser<P, V, E>, E> extends ActualNumberValue<V, P, E, Short> {}