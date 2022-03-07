package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressiveNumberParser;
public interface ExpressiveNumberValue<V extends ExpressiveNumberValue<V, P, E, A>, P extends ExpressiveNumberParser<P, V, E, A>, E extends Number, A> extends ExpressivePrimaryValue<V, P, E, A> {
	@Override
	default String toExpression() {
		return getExpressiveInstance().toString();
	}
}