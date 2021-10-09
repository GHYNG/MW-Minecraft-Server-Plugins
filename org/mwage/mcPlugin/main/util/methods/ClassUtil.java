package org.mwage.mcPlugin.main.util.methods;
public interface ClassUtil {
	public default boolean canCastTo(Object a, Object b) {
		Class<?> classA = a.getClass();
		Class<?> classB = b.getClass();
		return classB.isInstance(a);
	}
}