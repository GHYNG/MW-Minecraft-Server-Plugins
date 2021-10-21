package org.mwage.mcPlugin.main.util.io.config1.value;
import java.util.HashSet;
import java.util.Set;
interface UtilMethods_Value {
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
	default Set<Class<?>> getAllSuperClasses(Class<?> clazz) {
		if(clazz == null) {
			return null;
		}
		Set<Class<?>> superClasses = new HashSet<Class<?>>();
		Class<?> superClass = clazz;
		superClasses.add(superClass);
		while(superClass != Object.class) {
			superClass = superClass.getSuperclass();
			superClasses.add(superClass);
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