package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveLongValue;
public interface ExpressiveLongParser<P extends ExpressiveLongParser<P, V, A>, V extends ExpressiveLongValue<V, P, A>, A> extends ExpressiveNumberParser<P, V, Long, A> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "long");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
	@Override
	default Long parseExpressiveInstanceFromExpression(String expression) {
		return Long.parseLong(expression);
	}
}