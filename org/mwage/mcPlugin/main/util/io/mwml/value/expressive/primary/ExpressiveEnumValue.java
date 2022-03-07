package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressiveEnumParser;
public interface ExpressiveEnumValue<V extends ExpressiveEnumValue<V, P, A>, P extends ExpressiveEnumParser<P, V, A>, A> extends ExpressivePrimaryValue<V, P, String, A> {
	@Override
	default String toExpression() {
		return getExpressiveInstance();
	}
}