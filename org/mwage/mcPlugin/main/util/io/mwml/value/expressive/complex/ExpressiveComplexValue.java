package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.complex;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.complex.ExpressiveComplexParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.Value;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressiveValue;
public interface ExpressiveComplexValue<V extends ExpressiveComplexValue<V, P, E, A>, P extends ExpressiveComplexParser<P, V, E, A>, E, A> extends ExpressiveValue<V, P, E, A> {
	enum IterationRange {
		DIRECT_ONLY,
		ALL
	}
	enum EqualsStandard {
		BY_VALUE,
		BY_REFERENCE
	}
	int size(IterationRange iterationRange);
	boolean contains(Value<?, ?, ?, ?> value, IterationRange iterationRange, EqualsStandard iterationStandard);
}