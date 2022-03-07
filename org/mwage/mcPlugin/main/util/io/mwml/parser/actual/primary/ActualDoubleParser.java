package org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualDoubleValue;
public interface ActualDoubleParser<P extends ActualDoubleParser<P, V, E>, V extends ActualDoubleValue<V, P, E>, E> extends ActualNumberParser<P, V, E, Double> {
	Signature VALUE_EXPRESSION = new Signature("", "", "double");
	@Override
	default Signature getValueSignature() {
		return VALUE_EXPRESSION;
	}
}