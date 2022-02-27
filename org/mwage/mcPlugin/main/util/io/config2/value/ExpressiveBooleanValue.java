package org.mwage.mcPlugin.main.util.io.config2.value;
import org.mwage.mcPlugin.main.util.io.config2.parser.ExpressiveBooleanParser;
public interface ExpressiveBooleanValue<A> extends ExpressiveEnumValue<A> {
	@Override
	ExpressiveBooleanParser<A> getParser();
}