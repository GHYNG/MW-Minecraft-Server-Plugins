package org.mwage.mcPlugin.main.util.io.config2;
import java.util.ArrayList;
import java.util.List;
import org.mwage.mcPlugin.main.util.io.config2.parser.ExpressivePrimaryParser;
import org.mwage.mcPlugin.main.util.io.config2.parser.Parser;
import org.mwage.mcPlugin.main.util.io.config2.value.ExpressivePrimaryValue;
public class ParserSystem {
	public final MWConfigFileProject configFileProject;
	public ParserSystem(MWConfigFileProject configFileProject) {
		this.configFileProject = configFileProject;
	}
	private final List<Parser<?, ?, ?>> valueParsers = new ArrayList<Parser<?, ?, ?>>();
	public ExpressivePrimaryValue<?, ?> parseFromPrimaryExpression(String actualTypeProjectName, List<String> actualTypePackageNames, String actualTypeName, String expression) {
		for(Parser<?, ?, ?> valueParser : valueParsers) {
			if(valueParser instanceof ExpressivePrimaryParser<?, ?, ?> expressivePrimaryValueParser) {
				ActualType actualType = expressivePrimaryValueParser.getActualType();
				ActualType imaginaryActualType = new ActualType(actualTypeProjectName, actualTypePackageNames, actualTypeName);
				if(actualType.equals(imaginaryActualType)) {
					try {
						ExpressivePrimaryValue<?, ?> expressivePrimaryValue = expressivePrimaryValueParser.parseFromPrimaryExpression(expression);
						if(expressivePrimaryValue != null) {
							return expressivePrimaryValue; // See end of this method
						}
					}
					catch(RuntimeException e) {}
				}
			}
		}
		return null; // If any kind of Error Value Type is implemented, then null should not be used
	}
	/*
	 * This method only tests if the given identifier is legal grammaly.
	 * While return true, it may not necessarily be good to go to be named to a given object.
	 * For example, when strings like "boolean", "null", "true" are given, they are legal identifiers, but they cannot be named to other things.
	 */
	public static boolean isLegalIdentifier(String identifier) {
		char[] chars = identifier.toCharArray();
		int length = chars.length;
		if(length == 0) {
			return false; // an empty string is not legal for identifier
		}
		char c0 = chars[0];
		if(!Character.isLetter(c0)) {
			return false; // the first character of any identifier must be a letter
		}
		for(int i = 1; i < length; i++) {
			char c = chars[i];
			if(c == '_') {
				continue;
			}
			if(Character.isLetterOrDigit(c)) {
				continue;
			}
			return false;
		}
		return true;
	}
}