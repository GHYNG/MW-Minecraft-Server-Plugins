package org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualIntValue;
public interface ActualIntParser<P extends ActualIntParser<P, V, E>, V extends ActualIntValue<V, P, E>, E> extends ActualNumberParser<P, V, E, Integer> {
	Signature VALUE_EXPRESSION = new Signature("", "", "int");
	@Override
	default Signature getValueSignature() {
		return VALUE_EXPRESSION;
	}
}