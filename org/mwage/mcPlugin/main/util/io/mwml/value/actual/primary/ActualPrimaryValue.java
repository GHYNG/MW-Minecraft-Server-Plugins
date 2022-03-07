package org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary.ActualPrimaryParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.ActualValue;
public interface ActualPrimaryValue<V extends ActualPrimaryValue<V, P, E, A>, P extends ActualPrimaryParser<P, V, E, A>, E, A> extends ActualValue<V, P, E, A>{
	
}