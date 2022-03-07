package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressiveStringParser;
public interface ExpressiveStringValue<V extends ExpressiveStringValue<V, P, A>, P extends ExpressiveStringParser<P, V, A>, A> extends ExpressivePrimaryValue<V, P, String, A> {
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