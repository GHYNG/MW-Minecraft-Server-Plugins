package org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary;
import java.util.Map;
import org.mwage.mcPlugin.main.util.io.mwml.MWMLModule;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualEnumValue;
public interface ActualEnumParser<P extends ActualEnumParser<P, V, E, A>, V extends ActualEnumValue<V, P, E, A>, E, A> extends ActualPrimaryParser<P, V, E, A> {
	Signature VALUE_EXPRESSION = new Signature("", "", "enum");
	@Override
	default Signature getValueSignature() {
		return VALUE_EXPRESSION;
	}
	Map<A, V> getValueLinks();
	@Override
	default V parseValueFromActualInstance(MWMLModule invokerModule, Signature valueSignature, Signature expressionSignature, A actualInstance) {
		V valueFound = getValueLinks().get(actualInstance);
		if(valueFound == null) {
			return null;
		}
		return valueFound.clone(invokerModule, valueSignature, expressionSignature);
	}
}