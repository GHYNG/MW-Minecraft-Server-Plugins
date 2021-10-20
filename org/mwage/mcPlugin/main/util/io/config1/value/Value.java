package org.mwage.mcPlugin.main.util.io.config1.value;
public interface Value<E, A> {
	CollectionValue<?, ?> getOuterValue();
	default CollectionValue<?, ?> getOutestValue() {
		if(this instanceof CollectionValue<?, ?> container) {
			while(container.getOuterValue() != null) {
				container = container.getOuterValue();
			}
			return container;
		}
		else {
			CollectionValue<?, ?> outerValue = getOuterValue();
			return outerValue == null ? null : outerValue.getOutestValue();
		}
	}
	String getTypeName();
	E getExpressiveInstance();
	A getActualInstance();
}