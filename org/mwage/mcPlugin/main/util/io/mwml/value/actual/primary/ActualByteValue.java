package org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary.ActualByteParser;
public interface ActualByteValue<V extends ActualByteValue<V, P, E>, P extends ActualByteParser<P, V, E>, E> extends ActualNumberValue<V, P, E, Byte> {}