package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveBooleanValue;
public interface ExpressiveBooleanParser<P extends ExpressiveBooleanParser<P, V, A>, V extends ExpressiveBooleanValue<V, P, A>, A> extends ExpressiveEnumParser<P, V, A> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "boolean");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
}