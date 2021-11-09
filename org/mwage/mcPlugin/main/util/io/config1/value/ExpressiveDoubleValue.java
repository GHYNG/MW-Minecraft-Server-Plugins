package org.mwage.mcPlugin.main.util.io.config1.value;
import org.mwage.mcPlugin.main.util.clazz.GenericTypeHeader;
@GenericTypeHeader(superClass = Value.class, typeParamaterName = "E", typeParamater = Double.class)
public interface ExpressiveDoubleValue<A> extends CalculatableDoubleValue<Double, A>, MetaValue<Double, A> {
	@Override
	default Class<Double> getClassE() {
		return Double.class;
	}
	@Override
	default Double getInstanceC() {
		return getInstanceE();
	}
}