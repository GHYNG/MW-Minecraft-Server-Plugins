package org.mwage.mcPlugin.main.util.io.config1.value;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
public interface CollectionValue<E, A, V extends Value<?, ?>> extends Value<E, A> {
	Class<V> getStoredValueClass();
	Set<V> getDirectlyInnerValues();
	@SuppressWarnings("unchecked")
	default Set<V> getAllInnerValues() {
		Set<V> values = new HashSet<V>();
		getDirectlyInnerValues().forEach(value -> {
			values.add(value);
			if(value instanceof CollectionValue<?, ?, ?> container) {
				values.addAll((Collection<? extends V>)container.getAllInnerValues());
			}
		});
		return values;
	}
	default int directlySize() {
		return getDirectlyInnerValues().size();
	}
	default int totalSize() {
		return getAllInnerValues().size();
	}
	V getDirectlyInnerValue(String key);
	@SuppressWarnings("unchecked")
	default V getInnerValueWithKeyArray(String... keys) {
		if(keys == null) {
			return null;
		}
		int length = keys.length;
		if(length == 0) {
			if(getStoredValueClass().isInstance(this)) {
				return (V)this;
			}
			return null;
		}
		String key0 = keys[0];
		if(key0 == null) {
			return null;
		}
		while(key0.contains(" ")) {
			key0 = key0.replaceAll(" ", "");
		}
		Value<?, ?> innerValue = getDirectlyInnerValue(key0);
		if(length == 1) {
			if(getStoredValueClass().isInstance(innerValue)) {
				return (V)innerValue;
			}
			return null;
		}
		if(innerValue instanceof CollectionValue<?, ?, ?> innerContainer) {
			String[] subKeys = new String[length - 1];
			for(int i = 1; i < length; i++) {
				subKeys[i - 1] = keys[i];
			}
			Value<?, ?> innerInnerValue = innerContainer.getInnerValueWithKeyArray(subKeys);
			if(getStoredValueClass().isInstance(innerInnerValue)) {
				return (V)innerInnerValue;
			}
		}
		return null;
	}
	default V getInnerValue(String key) {
		String[] keys = key.split("\\.");
		return getInnerValueWithKeyArray(keys);
	}
}