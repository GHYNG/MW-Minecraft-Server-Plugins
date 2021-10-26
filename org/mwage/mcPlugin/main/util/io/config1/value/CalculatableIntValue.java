package org.mwage.mcPlugin.main.util.io.config1.value;
import org.mwage.mcPlugin.main.util.clazz.GenericTypeHeader;
@GenericTypeHeader(superClass = CalculatableValue.class, typeParamaterName = "C", typeParamater = Integer.class)
public interface CalculatableIntValue<E, A> extends CalculatableValue<Integer, E, A>, Comparable<CalculatableIntValue<E, A>> {
	@Override
	default Class<Integer> getClassC() {
		return Integer.class;
	}
	@Override
	default int compareTo(CalculatableIntValue<E, A> another) {
		if(another == null) {
			return 0;
		}
		if(another instanceof ErrorValue) {
			return 0;
		}
		int x = getCalculatableInstance();
		int y = another.getCalculatableInstance();
		return x == y ? 0 : x > y ? 1 : -1;
	}
}