package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveNumberValue;
public interface ExpressiveNumberParser<P extends ExpressiveNumberParser<P, V, E, A>, V extends ExpressiveNumberValue<V, P, E, A>, E extends Number, A> extends ExpressivePrimaryParser<P, V, E, A> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "number");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
}