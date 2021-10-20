package org.mwage.mcPlugin.main.util.io.config1.value;
import java.util.Map;
public interface ExpressiveTableValue<A, V extends Value<?, ?>> extends CollectionValue<Map<String, V>, A, V> {
	@Override
	default V getDirectlyInnerValue(String key) {
		Map<String, V> map = getExpressiveInstance();
		return map.get(key);
	}
	default IntValue getInnerIntValue(String key) {
		// TODO this method is wrong, needs to be rewritten
		V value = getInnerValue(key);
		if(getStoredValueClass().isInstance(value)) {
			return (IntValue)value;
		}
		throw new WrongStorageTypeInCollectionValueException("IntValue is not allowed to store in this table.");
	}
}