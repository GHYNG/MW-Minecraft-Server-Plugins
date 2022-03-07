package org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualLongValue;
public interface ActualLongParser<P extends ActualLongParser<P, V, E>, V extends ActualLongValue<V, P, E>, E> extends ActualNumberParser<P, V, E, Long> {
	Signature VALUE_EXPRESSION = new Signature("", "", "long");
	@Override
	default Signature getValueSignature() {
		return VALUE_EXPRESSION;
	}
}