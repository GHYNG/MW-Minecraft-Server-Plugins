package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary;
import java.util.Map;
import java.util.Set;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveEnumValue;
public interface ExpressiveEnumParser<P extends ExpressiveEnumParser<P, V, A>, V extends ExpressiveEnumValue<V, P, A>, A> extends ExpressivePrimaryParser<P, V, String, A> {
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "enum");
	@Override
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
	Map<String, A> getActualLinks();
	default Set<String> getKeys() {
		return getActualLinks().keySet();
	}
	@Override
	default String parseExpressiveInstanceFromExpression(String expression) {
		return expression;
	}
}