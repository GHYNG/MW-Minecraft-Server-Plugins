package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.parser.Parser;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressiveValue;
public interface ExpressiveParser<E, A, V extends ExpressiveValue<E, A>> extends Parser<E, A, V> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "expression");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
}