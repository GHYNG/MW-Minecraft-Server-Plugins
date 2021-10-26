package org.mwage.mcPlugin.main.util.io.config1.value;
import org.mwage.mcPlugin.main.util.clazz.GenericTypeHeader;
@GenericTypeHeader(superClass = CalculatableValue.class, typeParamaterName = "C", typeParamater = Double.class)
public interface CalculatableDoubleValue<E, A> extends CalculatableValue<Double, E, A>, Comparable<CalculatableDoubleValue<E, A>> {
	@Override
	default Class<Double> getClassC() {
		return Double.class;
	}
	@Override
	default int compareTo(CalculatableDoubleValue<E, A> another) {
		if(another == null) {
			return 0;
		}
		if(another instanceof ErrorValue) {
			return 0;
		}
		double x = getCalculatableInstance();
		double y = another.getCalculatableInstance();
		return x == y ? 0 : x > y ? 1 : -1;
	}
}