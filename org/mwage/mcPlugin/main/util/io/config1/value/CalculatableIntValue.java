package org.mwage.mcPlugin.main.util.io.config1.value;
public interface CalculatableIntValue<E, A> extends CalculatableValue<Integer, E, A>, Comparable<CalculatableIntValue<E, A>> {
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