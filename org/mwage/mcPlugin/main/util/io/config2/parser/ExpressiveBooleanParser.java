package org.mwage.mcPlugin.main.util.io.config2.parser;
import java.util.HashMap;
import java.util.Map;
import org.mwage.mcPlugin.main.util.io.config2.value.ExpressiveBooleanValue;
public interface ExpressiveBooleanParser<A> extends ExpressiveEnumParser<A, ExpressiveBooleanValue<A>> {}
// below are test codes
class ExpressiveBooleanParserClass implements ExpressiveBooleanParser<Boolean> {
	private Map<String, Boolean> actualLinks = new HashMap<String, Boolean>();
	{
		actualLinks.put("true", true);
		actualLinks.put("false", false);
	}
	@Override
	public Map<String, Boolean> getActualLinks() {
		return actualLinks;
	}
	@Override
	public ExpressiveBooleanValue<Boolean> parseFromActualInstance(Boolean actualInstance) {
		return new ExpressiveBooleanValueClass<Boolean>(this, actualInstance.toString(), actualInstance);
	}
}
class ExpressiveBooleanValueClass<A> implements ExpressiveBooleanValue<A> {
	private final ExpressiveBooleanParser<A> parser;
	private final String expressiveInstance;
	private final A actualInstance;
	ExpressiveBooleanValueClass(ExpressiveBooleanParser<A> parser, String expressiveInstance, A actualInstance) {
		this.parser = parser;
		this.expressiveInstance = expressiveInstance;
		this.actualInstance = actualInstance;
	}
	@Override
	public String getExpressiveInstance() {
		return expressiveInstance;
	}
	@Override
	public A getActualInstance() {
		return actualInstance;
	}
	@Override
	public ExpressiveBooleanParser<A> getParser() {
		return parser;
	}
}