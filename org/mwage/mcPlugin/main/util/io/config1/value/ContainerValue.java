package org.mwage.mcPlugin.main.util.io.config1.value;
import java.util.HashSet;
import java.util.Set;
import org.mwage.mcPlugin.main.util.io.config.Value;
public interface ContainerValue<E, A> extends Value<E, A> {
	Set<Value<?, ?>> getDirectlySubValues();
	default Set<Value<?, ?>> getAllSubValues() {
		Set<Value<?, ?>> values = new HashSet<Value<?, ?>>();
		getDirectlySubValues().forEach(value -> {
			values.add(value);
			if(value instanceof ContainerValue<?, ?> container) {
				values.addAll(container.getAllSubValues());
			}
		});
		return values;
	}
	default int directlySize() {
		return getDirectlySubValues().size();
	}
	default int totalSize() {
		return getAllSubValues().size();
	}
}