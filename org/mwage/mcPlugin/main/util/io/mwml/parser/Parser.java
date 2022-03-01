package org.mwage.mcPlugin.main.util.io.mwml.parser;
import org.mwage.mcPlugin.main.util.io.mwml.ExpressionType;
import org.mwage.mcPlugin.main.util.io.mwml.MWMLModule;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.ValueType;
import org.mwage.mcPlugin.main.util.io.mwml.value.Value;
public interface Parser<E, A, V extends Value<E, A>> {
	default int getPriority() {
		return 0;
	}
	MWMLModule MODULE = MWMLModule.CORE_MODULE;
	default MWMLModule getModule() {
		return MODULE;
	}
	Signature getValueSignature();
	Signature getExpressionSignature();
	default ValueType getValueType() {
		return getModule().findValueType(getValueSignature());
	}
	default ExpressionType getExpressionType() {
		return getModule().findExpressionType(getExpressionSignature());
	}
	/*
	 * Notice that the valueSignature and expressionSignature parameters in the 2 methods below is necessary.
	 * Because they are the actual signature found from the file,
	 * instead of the ideal signatures from getValueSignature() and getExpressionSignature()
	 */
	V parseFromActualInstance(Signature valueSignature, Signature expressionSignature, A actualInstance);
	V parseFromPureExpression(Signature valueSignature, Signature expressionSignature, String expression);
	boolean canTransform(Value<?, ?> another);
	V tryTransform(Value<?, ?> another);
}