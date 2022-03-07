package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveFloatValue;
public interface ExpressiveFloatParser<P extends ExpressiveFloatParser<P, V, A>, V extends ExpressiveFloatValue<V, P, A>, A> extends ExpressiveNumberParser<P, V, Float, A> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "float");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
	@Override
	default Float parseExpressiveInstanceFromExpression(String expression) {
		return Float.parseFloat(expression);
	}
}