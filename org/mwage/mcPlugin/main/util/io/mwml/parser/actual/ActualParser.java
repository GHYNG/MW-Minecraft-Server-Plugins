package org.mwage.mcPlugin.main.util.io.mwml.parser.actual;
import org.mwage.mcPlugin.main.util.io.mwml.MWMLModule;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.parser.Parser;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.ActualValue;
public interface ActualParser<P extends ActualParser<P, V, E, A>, V extends ActualValue<V, P, E, A>, E, A> extends Parser<P, V, E, A> {
	Signature VALUE_EXPRESSION = new Signature("", "", "value");
	@Override
	default Signature getValueSignature() {
		return VALUE_EXPRESSION;
	}
	V parseValueFromActualInstance(MWMLModule invokerModule, Signature valueSignature, Signature expressionSignature, A actualInstance);
	A parseActualInstanceFromExpressiveInstance(E expressiveInstance);
}