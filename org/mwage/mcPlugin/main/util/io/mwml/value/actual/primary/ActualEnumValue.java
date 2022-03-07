package org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary.ActualEnumParser;
public interface ActualEnumValue<V extends ActualEnumValue<V, P, E, A>, P extends ActualEnumParser<P, V, E, A>, E, A> extends ActualPrimaryValue<V, P, E, A> {}