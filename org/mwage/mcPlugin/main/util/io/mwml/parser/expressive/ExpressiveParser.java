package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.MWMLModule;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.parser.Parser;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressiveValue;
public interface ExpressiveParser<P extends ExpressiveParser<P, V, E, A>, V extends ExpressiveValue<V, P, E, A>, E, A> extends Parser<P, V, E, A> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "expression");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
	V parseValueFromExpressiveInstance(MWMLModule invokerModule, Signature valueSignature, Signature expressionSignature, E expressiveInstance);
	E parseExpressiveInstanceFromExpression(String expression);
	@Override
	default V parseValue(MWMLModule invokerModule, Signature valueSignature, Signature expressionSignature, String expression) {
		return parseValueFromExpressiveInstance(invokerModule, valueSignature, expressionSignature, parseExpressiveInstanceFromExpression(expression));
	}
}