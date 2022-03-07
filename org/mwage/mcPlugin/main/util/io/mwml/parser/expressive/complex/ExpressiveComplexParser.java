package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.complex;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.ExpressiveParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.complex.ExpressiveComplexValue;
public interface ExpressiveComplexParser<P extends ExpressiveComplexParser<P, V, E, A>, V extends ExpressiveComplexValue<V, P, E, A>, E, A> extends ExpressiveParser<P, V, E, A> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "complex");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
}