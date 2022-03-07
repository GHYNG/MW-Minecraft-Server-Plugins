package org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualByteValue;
public interface ActualByteParser<P extends ActualByteParser<P, V, E>, V extends ActualByteValue<V, P, E>, E> extends ActualNumberParser<P, V, E, Byte> {
	Signature VALUE_EXPRESSION = new Signature("", "", "byte");
	@Override
	default Signature getValueSignature() {
		return VALUE_EXPRESSION;
	}
}