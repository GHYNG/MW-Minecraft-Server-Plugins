package org.mwage.mcPlugin.main.util.io.config1.value;
public interface ExpressiveDoubleValue<A> extends CalculatableDoubleValue<Double, A>, MetaValue<Double, A> {
	@Override
	default Class<Double> getClassE() {
		return Double.class;
	}
	@Override
	default Double getCalculatableInstance() {
		return getExpressiveInstance();
	}
}