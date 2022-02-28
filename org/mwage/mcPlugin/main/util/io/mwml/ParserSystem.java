package org.mwage.mcPlugin.main.util.io.mwml;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.parser.Parser;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.ExpressivePrimaryParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressivePrimaryValue;
public class ParserSystem {
	public final Module localModule;
	private final Set<Parser<?, ?, ?>> localParsers = new HashSet<Parser<?, ?, ?>>();
	public ParserSystem(Module localModule) {
		this.localModule = localModule;
	}
	public final Set<Parser<?, ?, ?>> getParsersFromAllParentModules() {
		Set<Parser<?, ?, ?>> parentParsers = new HashSet<Parser<?, ?, ?>>();
		Set<Module> parentModules = localModule.getAllParents();
		for(Module parentModule : parentModules) {
			parentParsers.addAll(parentModule.getParserSystem().localParsers);
		}
		return parentParsers;
	}
	public final Set<Parser<?, ?, ?>> getParsersFromAllChildModules() {
		Set<Parser<?, ?, ?>> childParsers = new HashSet<Parser<?, ?, ?>>();
		Set<Module> childModules = localModule.getAllChildren();
		for(Module childModule : childModules) {
			childParsers.addAll(childModule.getParserSystem().localParsers);
		}
		return childParsers;
	}
	public final Set<Parser<?, ?, ?>> getAllParsers() {
		Set<Parser<?, ?, ?>> parsers = new HashSet<Parser<?, ?, ?>>();
		parsers.addAll(getParsersFromAllParentModules());
		parsers.addAll(localParsers);
		parsers.addAll(getParsersFromAllChildModules());
		return parsers;
	}
	public ExpressivePrimaryValue<?, ?> parseFromPrimaryExpression(Signature typeSignature, String expression) {
		Set<ExpressivePrimaryValue<?, ?>> parsedExpressivePrimaryValues = new HashSet<ExpressivePrimaryValue<?, ?>>();
		for(Parser<?, ?, ?> parser : getAllParsers()) {
			if(parser instanceof ExpressivePrimaryParser<?, ?, ?> expressivePrimaryParser) {
				ValueType valueType = expressivePrimaryParser.getValueType();
				if(valueType.getSignature().equals(typeSignature)) {
					try {
						ExpressivePrimaryValue<?, ?> expressivePrimaryValue = expressivePrimaryParser.parseFromPrimaryExpression(expression);
						if(expressivePrimaryValue != null) {
							parsedExpressivePrimaryValues.add(expressivePrimaryValue);
						}
					}
					catch(Exception e) {}
				}
			}
		}
		if(parsedExpressivePrimaryValues.size() != 0) {
			return null; // TODO unfinished
		}
		return null; // this should return null, because no value is parsed successfully
	}
	public ExpressivePrimaryValue<?, ?> parseFromPrimaryExpression(String actualTypeProjectName, List<String> actualTypePackageNames, String actualTypeName, String expression) {
		for(Parser<?, ?, ?> valueParser : localParsers) {
			if(valueParser instanceof ExpressivePrimaryParser<?, ?, ?> expressivePrimaryValueParser) {
				ValueType actualType = expressivePrimaryValueParser.getValueType();
				ValueType imaginaryActualType = new ValueType(localModule, actualTypeProjectName, actualTypePackageNames, actualTypeName);
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