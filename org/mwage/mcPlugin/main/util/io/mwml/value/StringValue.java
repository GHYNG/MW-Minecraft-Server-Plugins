package org.mwage.mcPlugin.main.util.io.mwml.value;
import org.mwage.mcPlugin.main.util.io.mwml.parser.StringParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualStringValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveStringValue;
public interface StringValue<V extends StringValue<V, P>, P extends StringParser<P, V>> extends ActualStringValue<V, P, String>, ExpressiveStringValue<V, P, String> {
	
}