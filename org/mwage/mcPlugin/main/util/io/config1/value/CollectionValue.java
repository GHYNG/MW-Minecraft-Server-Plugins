package org.mwage.mcPlugin.main.util.io.config1.value;
import java.util.HashSet;
import java.util.Set;
public interface CollectionValue<E, A> extends Value<E, A> {
	Set<Value<?, ?>> getDirectlyInnerValues();
	default Set<Value<?, ?>> getAllInnerValues() {
		Set<Value<?, ?>> values = new HashSet<Value<?, ?>>();
		getDirectlyInnerValues().forEach(value -> {
			values.add(value);
			if(value instanceof CollectionValue<?, ?> container) {
				values.addAll(container.getAllInnerValues());
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
}