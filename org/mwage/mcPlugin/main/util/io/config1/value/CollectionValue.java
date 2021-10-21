package org.mwage.mcPlugin.main.util.io.config1.value;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
public interface CollectionValue<E, A, SE, SA, S extends Value<SE, SA>> extends Value<E, A> {
	Class<S> getClassS();
	Class<SE> getClassSE();
	Class<SA> getClassSA();
	Set<S> getDirectlyInnerValues();
	@SuppressWarnings("unchecked")
	default Set<S> getAllInnerValues() {
		Set<S> values = new HashSet<S>();
		getDirectlyInnerValues().forEach(value -> {
			values.add(value);
			if(value instanceof CollectionValue<?, ?, ?, ?, ?> container) {
				values.addAll((Collection<? extends S>)container.getAllInnerValues());
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
	S getDirectlyInnerValue(String key);
	@SuppressWarnings("unchecked")
	default S getInnerValueWithKeyArray(String... keys) {
		if(keys == null) {
			return null;
		}
		int length = keys.length;
		if(length == 0) {
			if(getClassS().isInstance(this)) {
				return (S)this;
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
			if(getClassS().isInstance(innerValue)) {
				return (S)innerValue;
			}
			return null;
		}
		if(innerValue instanceof CollectionValue<?, ?, ?, ?, ?> innerContainer) {
			String[] subKeys = new String[length - 1];
			for(int i = 1; i < length; i++) {
				subKeys[i - 1] = keys[i];
			}
			Value<?, ?> innerInnerValue = innerContainer.getInnerValueWithKeyArray(subKeys);
			if(getClassS().isInstance(innerInnerValue)) {
				return (S)innerInnerValue;
			}
		}
		return null;
	}
	default S getInnerValue(String key) {
		String[] keys = key.split("\\.");
		return getInnerValueWithKeyArray(keys);
	}
}