package org.mwage.mcPlugin.main.util.io.config1.value;
import org.mwage.mcPlugin.main.util.clazz.GenericTypeHeader;
@GenericTypeHeader(superClass = Value.class, typeParamaterName = "A", typeParamater = Integer.class)
public interface ActualIntValue<E> extends CalculatableIntValue<E, Integer> {
	@Override
	default Class<Integer> getClassA() {
		return Integer.class;
	}
	@Override
	default Integer getCalculatableInstance() {
		return getActualInstance();
	}
}