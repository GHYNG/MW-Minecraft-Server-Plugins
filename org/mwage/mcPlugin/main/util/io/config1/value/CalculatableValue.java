package org.mwage.mcPlugin.main.util.io.config1.value;
import org.mwage.mcPlugin.main.util.clazz.GenericTypeHeader;
@GenericTypeHeader(superClass = CalculatableValue.class, typeParamaterName = "C", typeParamater = Object.class)
public interface CalculatableValue<C, E, A> extends Value<E, A> {
	Class<C> getClassC();
	C getCalculatableInstance();
}