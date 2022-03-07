package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.ExpressiveParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressivePrimaryValue;
public interface ExpressivePrimaryParser<P extends ExpressivePrimaryParser<P, V, E, A>, V extends ExpressivePrimaryValue<V, P, E, A>, E, A> extends ExpressiveParser<P, V, E, A> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "primary");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
}