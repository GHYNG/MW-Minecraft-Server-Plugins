package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveDoubleValue;
public interface ExpressiveDoubleParser<P extends ExpressiveDoubleParser<P, V, A>, V extends ExpressiveDoubleValue<V, P, A>, A> extends ExpressiveNumberParser<P, V, Double, A> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "double");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
	@Override
	default Double parseExpressiveInstanceFromExpression(String expression) {
		return Double.parseDouble(expression);
	}
}