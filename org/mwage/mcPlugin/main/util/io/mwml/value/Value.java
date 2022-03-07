package org.mwage.mcPlugin.main.util.io.mwml.value;
import org.mwage.mcPlugin.main.util.CircularRegistrationException;
import org.mwage.mcPlugin.main.util.MWCloneable;
import org.mwage.mcPlugin.main.util.io.mwml.MWMLModule;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.ValueType;
import org.mwage.mcPlugin.main.util.io.mwml.parser.Parser;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.complex.ExpressiveComplexValue;
public interface Value<V extends Value<V, P, E, A>, P extends Parser<P, V, E, A>, E, A> extends MWCloneable<V> {
	String toExpression();
	default MWMLModule getDifinerModule() {
		return getParser().getDefinerModule();
	}
	MWMLModule getInvokerModule();
	P getParser();
	default ValueType getValueType() {
		return getParser().getValueType();
	}
	E getExpressiveInstance();
	A getActualInstance();
	Signature getUsingValueSignature();
	Signature getUsingExpressionSignature();
	ExpressiveComplexValue<?, ?, ?, ?> getOuterValue();
	default ExpressiveComplexValue<?, ?, ?, ?> getOutestValue() {
		ExpressiveComplexValue<?, ?, ?, ?> outerValue = getOuterValue();
		if(outerValue == null) {
			if(this instanceof ExpressiveComplexValue<?, ?, ?, ?> ecv) {
				return ecv;
			}
			else {
				return null;
			}
		}
		else {
			return outerValue.getOutestValue();
		}
	}
	/*
	 * outerValue can be null, which means de-append to the previous outer value
	 * could throw exception
	 */
	boolean appendToOuterValue(ExpressiveComplexValue<?, ?, ?, ?> outerValue) throws CircularRegistrationException;
	@SuppressWarnings("unchecked")
	default V clone() {
		return getParser().copyValue((V)this);
	}
	@SuppressWarnings("unchecked")
	default V clone(MWMLModule invokerModule, Signature valueSignature, Signature expressionSignature) {
		return getParser().copyValue(invokerModule, valueSignature, expressionSignature, (V)this);
	}
}