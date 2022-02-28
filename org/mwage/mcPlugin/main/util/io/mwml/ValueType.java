package org.mwage.mcPlugin.main.util.io.mwml;
import java.util.List;
public class ValueType extends NameSpacedConcept<ValueType> {
	public ValueType(Module module, String spaceName, List<String> packageNames, String name) {
		super(module, spaceName, packageNames, name);
	}
	public ValueType(Module module, Signature signature) {
		super(module, signature);
	}
}