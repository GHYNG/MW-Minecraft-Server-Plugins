package org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualStringValue;
public interface ActualStringParser<P extends ActualStringParser<P, V, E>, V extends ActualStringValue<V, P, E>, E> extends ActualPrimaryParser<P, V, E, String> {
	Signature VALUE_EXPRESSION = new Signature("", "", "string");
	@Override
	default Signature getValueSignature() {
		return VALUE_EXPRESSION;
	}
}