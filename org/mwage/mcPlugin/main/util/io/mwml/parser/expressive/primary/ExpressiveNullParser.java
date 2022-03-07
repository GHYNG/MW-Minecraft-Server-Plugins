package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveNullValue;
public interface ExpressiveNullParser<P extends ExpressiveNullParser<P, V, A>, V extends ExpressiveNullValue<V, P, A>, A> extends ExpressiveEnumParser<P, V, A> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "null");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
}