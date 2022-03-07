package org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualNumberValue;
public interface ActualNumberParser<P extends ActualNumberParser<P, V, E, A>, V extends ActualNumberValue<V, P, E, A>, E, A extends Number> extends ActualPrimaryParser<P, V, E, A> {
	Signature VALUE_EXPRESSION = new Signature("", "", "number");
	@Override
	default Signature getValueSignature() {
		return VALUE_EXPRESSION;
	}
}