package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveIntValue;
public interface ExpressiveIntParser<P extends ExpressiveIntParser<P, V, A>, V extends ExpressiveIntValue<V, P, A>, A> extends ExpressiveNumberParser<P, V, Integer, A> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "int");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
	@Override
	default Integer parseExpressiveInstanceFromExpression(String expression) {
		return Integer.parseInt(expression);
	}
}