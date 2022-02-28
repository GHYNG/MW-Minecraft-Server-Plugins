package org.mwage.mcPlugin.main.util.io.mwml.parser;
import org.mwage.mcPlugin.main.util.io.mwml.ExpressionType;
import org.mwage.mcPlugin.main.util.io.mwml.Module;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.ValueType;
import org.mwage.mcPlugin.main.util.io.mwml.value.Value;
public interface Parser<E, A, V extends Value<E, A>> {
	Module getModule();
	default ValueType getValueType() {
		return new ValueType(getModule(), new Signature("", "var"));
	}
	default ExpressionType getExpressionType() {
		return new ExpressionType(getModule(), new Signature("", "exp"));
	}
	V parseFromActualInstance(A actualInstance);
}