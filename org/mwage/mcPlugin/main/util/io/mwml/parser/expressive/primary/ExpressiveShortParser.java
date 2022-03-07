package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveShortValue;
public interface ExpressiveShortParser<P extends ExpressiveShortParser<P, V, A>, V extends ExpressiveShortValue<V, P, A>, A> extends ExpressiveNumberParser<P, V, Short, A> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "short");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
	@Override
	default Short parseExpressiveInstanceFromExpression(String expression) {
		return Short.parseShort(expression);
	}
}