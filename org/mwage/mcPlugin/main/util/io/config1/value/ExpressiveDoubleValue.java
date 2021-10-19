package org.mwage.mcPlugin.main.util.io.config1.value;
public interface ExpressiveDoubleValue<A> extends CalculatableDoubleValue<Double, A>, MetaValue<Double, A> {
	@Override
	default Double getCalculatableInstance() {
		return getExpressiveInstance();
	}
}