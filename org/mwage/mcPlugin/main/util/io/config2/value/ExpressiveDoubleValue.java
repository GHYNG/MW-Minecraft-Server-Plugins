package org.mwage.mcPlugin.main.util.io.config2.value;
import org.mwage.mcPlugin.main.util.io.config2.parser.ExpressiveDoubleParser;
public interface ExpressiveDoubleValue<A> extends ExpressiveNumberValue<Double, A> {
	@Override
	ExpressiveDoubleParser<A> getParser();
}