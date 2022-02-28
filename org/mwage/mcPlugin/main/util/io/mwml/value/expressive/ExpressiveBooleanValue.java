package org.mwage.mcPlugin.main.util.io.mwml.value.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.ExpressiveBooleanParser;
public interface ExpressiveBooleanValue<A> extends ExpressiveEnumValue<A> {
	@Override
	ExpressiveBooleanParser<A> getParser();
}