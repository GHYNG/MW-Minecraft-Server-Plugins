package org.mwage.mcPlugin.main.util.io.config2.parser;
import org.mwage.mcPlugin.main.util.io.config2.value.ExpressiveIntValue;
public interface ExpressiveIntParser<A> extends ExpressiveNumberParser<Integer, A, ExpressiveIntValue<A>> {
	@Override
	default Integer parseExpressiveInstanceFromPrimaryExpression(String expression) {
		return Integer.parseInt(expression);
	}
}
// below are test codes. they should be removed
class ExpressiveIntValueParserClass implements ExpressiveIntParser<Integer> {
	@Override
	public Integer parseActualInstanceFromExpressiveInstance(Integer expressiveInstance) {
		return expressiveInstance;
	}
	@Override
	public ExpressiveIntValue<Integer> parseFromActualInstance(Integer actualInstance) {
		return new ExpressiveIntValueClass<Integer>(this, actualInstance, actualInstance);
	}
}
class ExpressiveIntValueClass<A> implements ExpressiveIntValue<A> {
	private final int expressiveInstance;
	private final A actualInstance;
	private final ExpressiveIntParser<A> parser;
	public ExpressiveIntValueClass(ExpressiveIntParser<A> parser, int expressiveInstance, A actualInstance) {
		this.parser = parser;
		this.expressiveInstance = expressiveInstance;
		this.actualInstance = actualInstance;
	}
	@Override
	public ExpressiveIntParser<A> getParser() {
		return parser;
	}
	@Override
	public Integer getExpressiveInstance() {
		return expressiveInstance;
	}
	@Override
	public A getActualInstance() {
		return actualInstance;
	}
}