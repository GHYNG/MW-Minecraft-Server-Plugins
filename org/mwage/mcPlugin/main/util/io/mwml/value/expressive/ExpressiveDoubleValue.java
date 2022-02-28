package org.mwage.mcPlugin.main.util.io.mwml.value.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.ExpressiveDoubleParser;
public interface ExpressiveDoubleValue<A> extends ExpressiveNumberValue<Double, A> {
	@Override
	ExpressiveDoubleParser<A> getParser();
}