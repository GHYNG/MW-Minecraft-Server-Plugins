package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.MWMLModule;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressivePrimaryValue;
public interface ExpressivePrimaryParser<E, A, V extends ExpressivePrimaryValue<E, A>> extends ExpressiveParser<E, A, V> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "primary");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
	E parseExpressiveInstanceFromPrimaryExpression(String expression);
	A parseActualInstanceFromExpressiveInstance(E expressiveInstance);
	@Override
	default V parseValue(MWMLModule invokerModule, Signature valueSignature, Signature expressionSignature, String expression) {
		E expressiveInstance = parseExpressiveInstanceFromPrimaryExpression(expression);
		A actualInstance = parseActualInstanceFromExpressiveInstance(expressiveInstance);
		return parseFromActualInstance(invokerModule, valueSignature, expressionSignature, actualInstance);
	}
}