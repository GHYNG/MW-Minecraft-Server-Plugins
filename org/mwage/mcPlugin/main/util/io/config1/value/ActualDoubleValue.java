package org.mwage.mcPlugin.main.util.io.config1.value;
public interface ActualDoubleValue<E> extends CalculatableDoubleValue<E, Double> {
	@Override
	default Class<Double> getClassA() {
		return Double.class;
	}
	@Override
	default Double getCalculatableInstance() {
		return getActualInstance();
	}
}