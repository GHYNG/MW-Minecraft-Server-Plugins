package org.mwage.mcPlugin.main.util.io.config2.value;
import org.mwage.mcPlugin.main.util.io.config2.ActualType;
import org.mwage.mcPlugin.main.util.io.config2.parser.Parser;
public interface Value<E, A> {
	String toExpression();
	Parser<? extends E, ? extends A, ? extends Value<E, A>> getParser();
	default ActualType getActualType() {
		return getParser().getActualType();
	}
	E getExpressiveInstance();
	A getActualInstance();
}