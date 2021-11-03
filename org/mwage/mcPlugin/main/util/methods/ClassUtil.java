package org.mwage.mcPlugin.main.util.methods;
import java.util.HashSet;
import java.util.Set;
public interface ClassUtil {
	default boolean aSuperb(Class<?> a, Class<?> b) {
		if(a == null || b == null) {
			return false;
		}
		if(a == b) {
			return true; // A class can be considered as super class to itself.
		}
		Set<Class<?>> bSuperClasses = getAllSuperClasses(b);
		return bSuperClasses.contains(a);
	}
	default boolean aSubb(Class<?> a, Class<?> b) {
		return aSuperb(b, a);
	}
	default Set<Class<?>> getAllSuperClasses(final Class<?> clazz) {
		if(clazz == null) {
			return null;
		}
		Set<Class<?>> superClasses = new HashSet<Class<?>>();
		Class<?> superClass = clazz.getSuperclass();
		while(superClass != null) {
			superClasses.add(superClass);
			superClass = superClass.getSuperclass();
		}
		superClasses.addAll(getAllSuperInterfaces(clazz));
		return superClasses;
	}
	default Set<Class<?>> getAllSuperInterfaces(Class<?> clazz) {
		if(clazz == null) {
			return null;
		}
		Set<Class<?>> allSuperInterfaces = new HashSet<Class<?>>();
		Class<?>[] superInterfaces = clazz.getInterfaces();
		for(Class<?> superInterface : superInterfaces) {
			allSuperInterfaces.add(superInterface);
			allSuperInterfaces.addAll(getAllSuperInterfaces(superInterface));
		}
		return allSuperInterfaces;
	}
}