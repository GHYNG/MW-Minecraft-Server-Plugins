package org.mwage.mcPlugin.main.util.io.config1.value;
public interface Value<E, A> {
	// ContainerValue<?, ?> getOutterValue();
	String getTypeName();
	E getExpressiveInstance();
	A getActualInstance();
}