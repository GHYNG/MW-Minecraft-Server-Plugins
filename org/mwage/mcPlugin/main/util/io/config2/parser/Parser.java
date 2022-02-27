package org.mwage.mcPlugin.main.util.io.config2.parser;
import java.util.ArrayList;
import org.mwage.mcPlugin.main.util.io.config2.ActualType;
import org.mwage.mcPlugin.main.util.io.config2.value.Value;
public interface Parser<E, A, V extends Value<E, A>> {
	static ActualType ACTUAL_TYPE = new ActualType("", new ArrayList<String>(), "var");
	default ActualType getActualType() {
		return ACTUAL_TYPE;
	}
	V parseFromActualInstance(A actualInstance);
}