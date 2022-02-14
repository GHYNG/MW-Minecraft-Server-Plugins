package org.mwage.mcPlugin.main.util.io.config1.value;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.mwage.mcPlugin.main.util.clazz.GenericTypeHeader;
import org.mwage.mcPlugin.main.util.clazz.GenericTypesInfo;
@GenericTypeHeader(superClass = Value.class, typeParamaterName = "E", typeParamater = Map.class)
public interface ExpressiveTableValue<A, SE, SA, S extends Value<SE, SA>> extends CollectionValue<Map<String, S>, A, SE, SA, S> {
	@SuppressWarnings("unchecked")
	@Override
	default Class<Map<String, S>> getClassE() {
		return (Class<Map<String, S>>)new HashMap<String, S>().getClass();
	}
	@Override
	default Set<S> getAllDirectlyInnerValues() {
		Map<String, S> map = getInstanceE();
		Set<S> set = new HashSet<S>();
		map.keySet().forEach(key -> {
			set.add(map.get(key));
		});
		return set;
	}
	@Override
	default S getDirectlyInnerValue(String key) {
		Map<String, S> map = getInstanceE();
		return map.get(key);
	}
}
@SuppressWarnings("unused")
class ExpressiveTableValueUtil {
	static GenericTypesInfo<IntValue> intValueGenericTypesInfo;
	static GenericTypesInfo<DoubleValue> doubleValueGenericTypesInfo;
	static {
		IntValue : {
			Map<GenericTypesInfo.Node, Class<?>> intValueGenericTypes = new HashMap<GenericTypesInfo.Node, Class<?>>();
			intValueGenericTypes.put(new GenericTypesInfo.Node(Value.class, "E"), Integer.class);
			intValueGenericTypes.put(new GenericTypesInfo.Node(Value.class, "A"), Integer.class);
			intValueGenericTypes.put(new GenericTypesInfo.Node(CalculatableValue.class, "C"), Integer.class);
			intValueGenericTypesInfo = new GenericTypesInfo<IntValue>(IntValue.class, intValueGenericTypes);
		}
		DoubleValue : {
			Map<GenericTypesInfo.Node, Class<?>> intValueGenericTypes = new HashMap<GenericTypesInfo.Node, Class<?>>();
			intValueGenericTypes.put(new GenericTypesInfo.Node(Value.class, "E"), Double.class);
			intValueGenericTypes.put(new GenericTypesInfo.Node(Value.class, "A"), Double.class);
			intValueGenericTypes.put(new GenericTypesInfo.Node(CalculatableValue.class, "C"), Double.class);
			doubleValueGenericTypesInfo = new GenericTypesInfo<DoubleValue>(DoubleValue.class, intValueGenericTypes);
		}
	}
}