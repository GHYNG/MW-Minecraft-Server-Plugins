package org.mwage.mcPlugin.main.util.io.config1.value;
import org.mwage.mcPlugin.main.util.clazz.GenericTypeHeader;
import org.mwage.mcPlugin.main.util.clazz.GenericTypesInfoble;
@GenericTypeHeader(superClass = Value.class, typeParamaterName = "E", typeParamater = Object.class)
@GenericTypeHeader(superClass = Value.class, typeParamaterName = "V", typeParamater = Object.class)
@GenericTypeHeader(superClass = GenericTypesInfoble.class, typeParamaterName = "T", typeParamater = Value.class)
public interface Value<E, A> extends GenericTypesInfoble<Value<?, ?>> {
	Class<E> getClassE();
	Class<A> getClassA();
	CollectionValue<?, ?, ?, ?, ?> getOuterValue();
	default CollectionValue<?, ?, ?, ?, ?> getOutestValue() {
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
	E getExpressiveInstance();
	A getActualInstance();
}