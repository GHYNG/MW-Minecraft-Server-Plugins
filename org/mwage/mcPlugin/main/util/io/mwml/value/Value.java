package org.mwage.mcPlugin.main.util.io.mwml.value;
import org.mwage.mcPlugin.main.util.io.mwml.MWMLModule;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.ValueType;
import org.mwage.mcPlugin.main.util.io.mwml.parser.Parser;
public interface Value<E, A> {
	String toExpression();
	MWMLModule getInvokerModule();
	default MWMLModule getDifinerModule() {
		return getParser().getDefinerModule();
	}
	Parser<? extends E, ? extends A, ? extends Value<E, A>> getParser();
	default ValueType getValueType() {
		return getParser().getValueType();
	}
	E getExpressiveInstance();
	A getActualInstance();
	Signature getUsingTypeSignature();
	Signature getUsingExpressionSignaure();
}