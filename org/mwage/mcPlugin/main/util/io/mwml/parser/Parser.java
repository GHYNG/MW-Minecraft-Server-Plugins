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
	default MWMLModule getDefinerModule() {
		return MODULE;
	}
	Signature VALUE_SIGNATURE = new Signature("", "", "value");
	Signature EXPRESSION_SIGNATURE = new Signature("", "", "expression");
	default Signature getValueSignature() {
		return VALUE_SIGNATURE;
	}
	default Signature getExpressionSignature() {
		return EXPRESSION_SIGNATURE;
	}
	default ValueType getValueType() {
		return getDefinerModule().findValueType(getValueSignature());
	}
	default ExpressionType getExpressionType() {
		return getDefinerModule().findExpressionType(getExpressionSignature());
	}
	/*
	 * Notice that the valueSignature and expressionSignature parameters in the 2 methods below is necessary.
	 * Because they are the actual signature found from the file,
	 * instead of the ideal signatures from getValueSignature() and getExpressionSignature()
	 */
	V parseFromActualInstance(MWMLModule invokerModule, Signature valueSignature, Signature expressionSignature, A actualInstance);
	V parseValue(MWMLModule invokerModule, Signature valueSignature, Signature expressionSignature, String expression);
	default V parseValue(MWMLModule invokerModule, String expression) {
		return parseValue(invokerModule, getValueSignature(), getExpressionSignature(), expression);
	}
	boolean canTransform(Value<?, ?> another);
	V tryTransform(Value<?, ?> another);
}