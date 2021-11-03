package org.mwage.mcPlugin.main.util.io.config1.value;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.mwage.mcPlugin.main.util.clazz.GenericTypeHeader;
import org.mwage.mcPlugin.main.util.clazz.GenericTypesInfo;
/*
 * <S> The storage value type.
 * <SE> The storage value's expressive instance type.
 * <SA> The storage value's actual instance type.
 */
@GenericTypeHeader(superClass = CollectionValue.class, typeParamaterName = "SE", typeParamater = Object.class)
@GenericTypeHeader(superClass = CollectionValue.class, typeParamaterName = "SA", typeParamater = Object.class)
@GenericTypeHeader(superClass = CollectionValue.class, typeParamaterName = "S", typeParamater = Value.class)
public interface CollectionValue<E, A, SE, SA, S extends Value<SE, SA>> extends Value<E, A> {
	Class<S> getClassS();
	Class<SE> getClassSE();
	Class<SA> getClassSA();
	default GenericTypesInfo<S> getStoredValueGenericTypesInfo() {
		Map<GenericTypesInfo.Node, Class<?>> map = new HashMap<GenericTypesInfo.Node, Class<?>>();
		map.put(new GenericTypesInfo.Node(Value.class, "E"), getClassSE());
		map.put(new GenericTypesInfo.Node(Value.class, "A"), getClassSA());
		return new GenericTypesInfo<S>(getClassS(), map);
	}
	Set<S> getDirectlyInnerValues();
	default Set<S> getAllInnerValues1() {
		Set<S> values = new HashSet<S>();
		boolean containsCollectionValues = CollectionValueUtil.collectionValueGenericTypesInfo.isSuperTo(getStoredValueGenericTypesInfo());
		getDirectlyInnerValues().forEach(value -> {
			values.add(value);
			if(containsCollectionValues && value instanceof CollectionValue<?, ?, ?, ?, ?> container) {
				Set<? extends Value<?, ?>> subValues = container.getAllInnerValues1();
			}
		});
		return null; // TODO unfinished
	}
	@SuppressWarnings("unchecked")
	default Set<S> getAllInnerValues() { // TODO needs rewrite
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
	default S getInnerValueWithKeyArray(String... keys) { // TODO needs rewrite
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
	default <V extends S> V getInnerValueWithGenericTypesInfo(String key, GenericTypesInfo<V> genericTypesInfo, V def) {
		S value = getInnerValue(key);
		GenericTypesInfo<?> valueGenericTypesInfo = value.getGenericTypesInfo();
		if(genericTypesInfo.isSuperTo(valueGenericTypesInfo)) {}
		return null; // TODO unfinished
	}
}
@SuppressWarnings({
		"unused", "rawtypes"
})
class CollectionValueUtil {
	static GenericTypesInfo<CollectionValue> collectionValueGenericTypesInfo;
	static {
		CollectionValue : {
			Map<GenericTypesInfo.Node, Class<?>> collectionValueGenericTypes = new HashMap<GenericTypesInfo.Node, Class<?>>();
			collectionValueGenericTypes.put(new GenericTypesInfo.Node(CollectionValue.class, "SE"), Object.class);
			collectionValueGenericTypes.put(new GenericTypesInfo.Node(CollectionValue.class, "SA"), Object.class);
			collectionValueGenericTypes.put(new GenericTypesInfo.Node(CollectionValue.class, "S"), Value.class);
			collectionValueGenericTypesInfo = new GenericTypesInfo<CollectionValue>(CollectionValue.class, collectionValueGenericTypes);
		}
	}
}