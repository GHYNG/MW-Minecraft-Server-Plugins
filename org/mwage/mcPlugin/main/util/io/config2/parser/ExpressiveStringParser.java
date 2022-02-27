package org.mwage.mcPlugin.main.util.io.config2.parser;
import java.util.HashMap;
import java.util.Map;
import org.mwage.mcPlugin.main.util.io.config2.StringFormatException;
import org.mwage.mcPlugin.main.util.io.config2.value.ExpressiveStringValue;
import org.mwage.mcPlugin.main.util.methods.LogicUtil;
public interface ExpressiveStringParser<A> extends ExpressivePrimaryParser<String, A, ExpressiveStringValue<A>>, LogicUtil {
	@Override
	default String parseExpressiveInstanceFromPrimaryExpression(String expression) {
		if(not(and(expression.startsWith("\""), expression.endsWith("\"")))) {
			throw new StringFormatException("Cannot parse: " + expression + ", maybe missing quote signs?");
		}
		int length = expression.length();
		if(length < 3) {
			throw new StringFormatException("Unknown error while parsing: " + expression + ", the expression is too short!");
		}
		String cuttedExpression = expression.substring(1, expression.length() - 1);
		String expressiveInstance = ExpressiveStringParserUtil.parseEscapeString(cuttedExpression);
		return expressiveInstance;
	}
}
class ExpressiveStringParserUtil {
	static Map<String, String> replacer = new HashMap<String, String>();
	static {
		replacer.put("t", "\t");
		replacer.put("n", "\n");
		replacer.put("\"", "\"");
		replacer.put("\'", "\'");
	}
	static String parseEscapeString(String originalContent) {
		String content = originalContent + "a";
		String[] sectors = content.split("\\\\\\\\");
		int length = sectors.length;
		if(length == 0) {
			return "";
		}
		for(int i = 0; i < length; i++) {
			String sector = sectors[i];
			for(String key : replacer.keySet()) {
				String rep = replacer.get(key);
				String bef = "\\\\" + key;
				String aft = rep;
				sector = sector.replaceAll(bef, aft);
			}
			if(sector.contains("\\")) {
				return "Error!";
			}
			sectors[i] = sector;
		}
		content = "";
		for(int i = 0; i < length - 1; i++) {
			content += sectors[i] + "\\";
		}
		content += sectors[length - 1];
		return content.substring(0, content.length() - 1);
	}
}