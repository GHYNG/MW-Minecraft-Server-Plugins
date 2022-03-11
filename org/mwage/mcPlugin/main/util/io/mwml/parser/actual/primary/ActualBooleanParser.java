package org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualBooleanValue;
public interface ActualBooleanParser<P extends ActualBooleanParser<P, V, E>, V extends ActualBooleanValue<V, P, E>, E> extends ActualEnumParser<P, V, E, Boolean> {
	Signature VALUE_EXPRESSION = new Signature("", "", "boolean");
	@Override
	default Signature getValueSignature() {
		return VALUE_EXPRESSION;
	}
}