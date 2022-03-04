package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressiveIntValue;
public interface ExpressiveIntParser<A> extends ExpressiveNumberParser<Integer, A, ExpressiveIntValue<A>> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "int");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
	@Override
	default Integer parseExpressiveInstanceFromPrimaryExpression(String expression) {
		return Integer.parseInt(expression);
	}
}