package org.mwage.mcPlugin.main.util.io.mwml;
import java.util.List;
public class ExpressionType extends NameSpacedConcept<ExpressionType> {
	public ExpressionType(MWMLModule markupLanguageSpace, String spaceName, List<String> packageNames, String name) {
		super(spaceName, packageNames, name);
	}
	public ExpressionType(MWMLModule markupLanguageSpace, Signature signature) {
		super(signature);
	}
}