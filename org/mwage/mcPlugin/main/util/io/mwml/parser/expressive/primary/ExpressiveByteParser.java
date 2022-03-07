package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveByteValue;
public interface ExpressiveByteParser<P extends ExpressiveByteParser<P, V, A>, V extends ExpressiveByteValue<V, P, A>, A> extends ExpressiveNumberParser<P, V, Byte, A> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "byte");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
	@Override
	default Byte parseExpressiveInstanceFromExpression(String expression) {
		return Byte.parseByte(expression);
	}
}