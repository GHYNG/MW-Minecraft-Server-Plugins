package org.mwage.mcPlugin.main.util.io.config1.value;
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
	Set<S> getAllDirectlyInnerValues();
	default Set<Value<?, ?>> getAllInnerValues() {
		Set<Value<?, ?>> values = new HashSet<Value<?, ?>>();
		getAllDirectlyInnerValues().forEach(s -> {
			values.add(s);
			if(s instanceof CollectionValue<?, ?, ?, ?, ?> collection) {
				values.addAll(collection.getAllInnerValues());
			}
		});
		return values;
	}
	default int directlySize() {
		return getAllDirectlyInnerValues().size();
	}
	default int totalSize() {
		return getAllInnerValues().size();
	}
	@Override
	default Value<?, ?> calculateKey(String key) {
		Value<?, ?> value = Value.super.calculateKey(key);
		if(value != null) {
			return value;
		}
		return getDirectlyInnerValue(key);
	}
	S getDirectlyInnerValue(String key);
	default Value<?, ?> getInnerValue(String... keys) {
		return calculateKeys(keys);
	}
	default Value<?, ?> getInnerValueWithLongKey(String key) {
		return calculateLongKey(key);
	}
	default boolean directlyContains(S innerValue) {
		return directlyContains(innerValue, true);
	}
	default boolean directlyContains(S lookfor, boolean useEquals) {
		if(lookfor == null) {
			return false;
		}
		Set<S> innerValues = getAllDirectlyInnerValues();
		if(useEquals && innerValues.contains(lookfor)) {
			return true;
		}
		for(S innerValue : innerValues) {
			if(lookfor == innerValue) {
				return true;
			}
		}
		return false;
	}
	default boolean contains(Value<?, ?> lookfor) {
		return contains(lookfor, true);
	}
	default boolean contains(Value<?, ?> lookfor, boolean useEquals) {
		if(lookfor == null) {
			return false;
		}
		Set<Value<?, ?>> innerValues = getAllInnerValues();
		if(useEquals && innerValues.contains(lookfor)) {
			return true;
		}
		for(Value<?, ?> innerValue : innerValues) {
			if(lookfor == innerValue) {
				return true;
			}
		}
		return false;
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