package org.mwage.mcPlugin.main.util.io.config1.value;
import java.util.HashMap;
import java.util.Map;
public interface ExpressiveTableValue<A, SE, SA, S extends Value<SE, SA>> extends CollectionValue<Map<String, S>, A, SE, SA, S> {
	@SuppressWarnings("unchecked")
	@Override
	default Class<Map<String, S>> getClassE() {
		return (Class<Map<String, S>>)new HashMap<String, S>().getClass();
	}
	@Override
	default S getDirectlyInnerValue(String key) {
		Map<String, S> map = getExpressiveInstance();
		return map.get(key);
	}
	default IntValue getInnerIntValue(String key) {
		// TODO this method is wrong, needs to be rewritten
		S value = getInnerValue(key);
		if(getClassS().isInstance(value)) {
			return (IntValue)value;
		}
		throw new WrongStorageTypeInCollectionValueException("IntValue is not allowed to store in this table.");
	}
}