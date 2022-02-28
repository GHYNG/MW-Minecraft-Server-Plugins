package org.mwage.mcPlugin.main.util.io.mwml;
import java.util.List;
public class ExpressionType extends NameSpacedConcept<ExpressionType> {
	public ExpressionType(Module markupLanguageSpace, String spaceName, List<String> packageNames, String name) {
		super(markupLanguageSpace, spaceName, packageNames, name);
	}
	public ExpressionType(Module markupLanguageSpace, Signature signature) {
		super(markupLanguageSpace, signature);
	}
}