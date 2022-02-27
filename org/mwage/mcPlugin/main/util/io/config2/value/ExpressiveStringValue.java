package org.mwage.mcPlugin.main.util.io.config2.value;
import org.mwage.mcPlugin.main.util.io.config2.parser.ExpressiveStringParser;
public interface ExpressiveStringValue<A> extends ExpressivePrimaryValue<String, A> {
	@Override
	ExpressiveStringParser<A> getParser();
	@Override
	default String toExpression() {
		String originalContent = getExpressiveInstance();
		String content = originalContent.replaceAll("\\\\", "\\\\\\\\");
		content = content.replaceAll("\\t", "\\\\t");
		content = content.replaceAll("\\n", "\\\\n");
		content = content.replaceAll("\\\'", "\\\\\'");
		content = content.replaceAll("\\\"", "\\\\\"");
		return "\"" + content + "\"";
	}
}