package org.mwage.mcPlugin.main.util.io.mwml;
import java.util.List;
public class ValueType extends NameSpacedConcept<ValueType> {
	public ValueType(String moduleName, List<String> packageNames, String name) {
		super(moduleName, packageNames, name);
	}
	public ValueType(MWMLModule module, Signature signature) {
		super(signature);
	}
}