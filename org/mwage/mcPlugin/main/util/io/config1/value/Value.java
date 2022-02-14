package org.mwage.mcPlugin.main.util.io.config1.value;
import java.util.HashMap;
import java.util.Map;
import org.mwage.mcPlugin.main.util.Function;
import org.mwage.mcPlugin.main.util.clazz.GenericTypeHeader;
import org.mwage.mcPlugin.main.util.clazz.GenericTypesInfo;
import org.mwage.mcPlugin.main.util.clazz.GenericTypesInfoble;
@GenericTypeHeader(superClass = Value.class, typeParamaterName = "E", typeParamater = Object.class)
@GenericTypeHeader(superClass = Value.class, typeParamaterName = "A", typeParamater = Object.class)
@GenericTypeHeader(superClass = GenericTypesInfoble.class, typeParamaterName = "T", typeParamater = Value.class)
public interface Value<E, A> extends GenericTypesInfoble<Value<E, A>> {
	Class<E> getClassE();
	Class<A> getClassA();
	E getInstanceE();
	A getInstanceA();
	default void prepareGenericTypesInfo(GenericTypesInfo<?> genericTypesInfo) throws PrepareWrongGenericTypesInfoException {
		if(getClass() != genericTypesInfo.clazz) {
			throw new PrepareWrongGenericTypesInfoException("Only allow to prepare this value instance's own GenricTypesInfo instance.");
		}
		genericTypesInfo.put(Value.class, "E", getClassE());
		genericTypesInfo.put(Value.class, "A", getClassA());
	}
	@Override
	default GenericTypesInfo<? extends Value<E, A>> getGenericTypesInfo() {
		GenericTypesInfo<? extends Value<E, A>> info = GenericTypesInfoble.super.getGenericTypesInfo();
		try {
			prepareGenericTypesInfo(info);
		}
		catch(PrepareWrongGenericTypesInfoException e) {
			throw new RuntimeException("Unexcepted exception occured.", e);
		}
		return info;
	}
	// TODO this needs to be added
	// ValueGenerator<E, A, ? extends Value<E, A>> getGenerator();
	CollectionValue<?, ?, ?, ?, ? extends Value<?, ?>> getOuterValue();
	default CollectionValue<?, ?, ?, ?, ? extends Value<?, ?>> getOutestValue() {
		if(this instanceof CollectionValue<?, ?, ?, ?, ?> container) {
			while(container.getOuterValue() != null) {
				container = container.getOuterValue();
			}
			return container;
		}
		else {
			CollectionValue<?, ?, ?, ?, ?> outerValue = getOuterValue();
			return outerValue == null ? null : outerValue.getOutestValue();
		}
	}
	String getTypeName();
	default Value<?, ?> calculateKey(String key) {
		// ValueGenerator<E, A, ? extends Value<E, A>> valueGenerator = getGenerator();
		// Function<? extends Value<E, A>, Value<?, ?>> method = valueGenerator.getMethods().get(key);
		// Value<?, ?> result = method.invoke(this);
		return null;
	}
	default Value<?, ?> calculateKeys(String... keys) {
		int length = keys.length;
		if(length == 0) {
			return null;
		}
		Value<?, ?> value = calculateKey(keys[0]);
		if(length == 1 || value == null) {
			return value;
		}
		String[] newKeys = new String[length - 1];
		for(int i = 0; i < length - 1; i++) {
			newKeys[i] = keys[i + 1];
		}
		return value.calculateKeys(newKeys);
	}
	default Value<?, ?> calculateLongKey(String key) {
		String[] keys = key.split("\\.");
		return calculateKeys(keys);
	}
}