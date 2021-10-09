package org.mwage.mcPlugin.main.util.io.config;
import java.util.Map;
public interface ExpressiveTableValue<A> extends Value<Map<String, Value<?, ?>>, A> {
	@SuppressWarnings("rawtypes")
	public default Value<?, ?> get(String key) {
		return get(key, new NullValueInstance());
	}
	public default Value<?, ?> get(String key, Value<?, ?> defaultValue) {
		Map<String, Value<?, ?>> map = getExpressiveInstance();
		if(map == null) {
			return defaultValue;
		}
		if(key == null) {
			return defaultValue;
		}
		if(key.length() == 0) {
			return this;
		}
		String[] sectors = key.split(".");
		int length = sectors.length;
		if(length == 0) {
			Value<?, ?> value = map.get(key);
			if(value == null) {
				return defaultValue;
			}
			return value;
		}
		String key0 = sectors[0];
		Value<?, ?> value = map.get(key0);
		if(value == null) {
			return defaultValue;
		}
		else if(value instanceof ExpressiveTableValue<?> tableValue) {
			String subKey = sectors[1];
			for(int i = 2; i < length; i++) {
				if(sectors[i].length() != 0) {
					subKey += sectors[i];
				}
			}
			return tableValue.get(subKey, defaultValue);
		}
		return defaultValue;
	}
	public default IntValue getInt(String key) {
		// return getInt(key, new NullIntValueInstance());
		return null; // TODO unfinished
	}
	public default IntValue getInt(String key, IntValue defaultValue) {
		Value<?, ?> value = get(key);
		if(value instanceof IntValue intValue) {
			return intValue;
		}
		return defaultValue;
	}
	@Deprecated
	@SuppressWarnings("unchecked")
	public default <V extends Value<?, ?>> V getValueOfType(String key, V defaultValue) {
		Value<?, ?> value = get(key);
		Class<?> wantedType = defaultValue.getClass();
		if(wantedType.isInstance(value)) {
			return (V)value;
		}
		return defaultValue;
	}
}