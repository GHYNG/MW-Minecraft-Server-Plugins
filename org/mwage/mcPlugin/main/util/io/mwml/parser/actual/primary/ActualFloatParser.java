package org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualFloatValue;
public interface ActualFloatParser<P extends ActualFloatParser<P, V, E>, V extends ActualFloatValue<V, P, E>, E> extends ActualNumberParser<P, V, E, Float> {
	Signature VALUE_EXPRESSION = new Signature("", "", "float");
	@Override
	default Signature getValueSignature() {
		return VALUE_EXPRESSION;
	}
}