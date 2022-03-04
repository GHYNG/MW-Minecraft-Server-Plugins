package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressiveBooleanValue;
public interface ExpressiveBooleanParser<A> extends ExpressiveEnumParser<A, ExpressiveBooleanValue<A>> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "boolean");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
}