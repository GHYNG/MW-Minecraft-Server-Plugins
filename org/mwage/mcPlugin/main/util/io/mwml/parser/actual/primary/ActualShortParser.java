package org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualShortValue;
public interface ActualShortParser<P extends ActualShortParser<P, V, E>, V extends ActualShortValue<V, P, E>, E> extends ActualNumberParser<P, V, E, Short> {
	Signature VALUE_EXPRESSION = new Signature("", "", "short");
	@Override
	default Signature getValueSignature() {
		return VALUE_EXPRESSION;
	}
}