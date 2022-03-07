package org.mwage.mcPlugin.main.util.io.mwml.value.actual;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.ActualParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.Value;
public interface ActualValue<V extends ActualValue<V, P, E, A>, P extends ActualParser<P, V, E, A>, E, A> extends Value<V, P, E, A> {}