package org.mwage.mcPlugin.main.util.io.config1.value;
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