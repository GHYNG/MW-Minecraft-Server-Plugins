package org.mwage.mcPlugin.main.util.io.mwml.value;
import org.mwage.mcPlugin.main.util.io.mwml.ValueType;
import org.mwage.mcPlugin.main.util.io.mwml.parser.Parser;
public interface Value<E, A> {
	String toExpression();
	Parser<? extends E, ? extends A, ? extends Value<E, A>> getParser();
	default ValueType getActualType() {
		return getParser().getValueType();
	}
	E getExpressiveInstance();
	A getActualInstance();
}