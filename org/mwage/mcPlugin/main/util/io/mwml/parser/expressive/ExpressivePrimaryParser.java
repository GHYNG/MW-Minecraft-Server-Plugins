package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressivePrimaryValue;
public interface ExpressivePrimaryParser<E, A, V extends ExpressivePrimaryValue<E, A>> extends ExpressiveParser<E, A, V> {
	E parseExpressiveInstanceFromPrimaryExpression(String expression);
	A parseActualInstanceFromExpressiveInstance(E expressiveInstance);
	@Override
	default V parseFromPureExpression(Signature valueSignature, Signature expressionSignature, String expression) {
		E expressiveInstance = parseExpressiveInstanceFromPrimaryExpression(expression);
		A actualInstance = parseActualInstanceFromExpressiveInstance(expressiveInstance);
		return parseFromActualInstance(valueSignature, expressionSignature, actualInstance);
	}
}