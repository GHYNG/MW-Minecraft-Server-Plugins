package org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.ActualParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualPrimaryValue;
public interface ActualPrimaryParser<P extends ActualPrimaryParser<P, V, E, A>, V extends ActualPrimaryValue<V, P, E, A>, E, A> extends ActualParser<P, V, E, A> {
	Signature VALUE_EXPRESSION = new Signature("", "", "primary");
	@Override
	default Signature getValueSignature() {
		return VALUE_EXPRESSION;
	}
}