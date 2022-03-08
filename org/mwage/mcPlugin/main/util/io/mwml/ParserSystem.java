package org.mwage.mcPlugin.main.util.io.mwml;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.mwage.mcPlugin.main.util.CircularRegistrationException;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.parser.Parser;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary.ActualPrimaryParser;
import org.mwage.mcPlugin.main.util.io.mwml.parser.actual.primary.ActualStringParser;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressivePrimaryParser;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressiveStringParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.Value;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualPrimaryValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualStringValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.complex.ExpressiveComplexValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressivePrimaryValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveStringValue;
import org.mwage.mcPlugin.main.util.methods.LogicUtil;
import org.mwage.mcPlugin.main.util.methods.StringUtil;
/*
 * This class is not completed yet. Improvements needed.
 * Some of the methods are wrong. Rework needed.
 */
public class ParserSystem implements LogicUtil, StringUtil {
	public final MWMLModule localModule;
	private final Set<Parser<?, ?, ?, ?>> localParsers = new HashSet<Parser<?, ?, ?, ?>>();
	public ParserSystem(MWMLModule localModule) {
		this.localModule = localModule;
	}
	public final Set<Parser<?, ?, ?, ?>> getParsersFromAllParentModules() {
		Set<Parser<?, ?, ?, ?>> parentParsers = new HashSet<Parser<?, ?, ?, ?>>();
		Set<MWMLModule> parentModules = localModule.getAllParents();
		for(MWMLModule parentModule : parentModules) {
			parentParsers.addAll(parentModule.getParserSystem().localParsers);
		}
		return parentParsers;
	}
	public final Set<Parser<?, ?, ?, ?>> getParsersFromAllChildModules() {
		Set<Parser<?, ?, ?, ?>> childParsers = new HashSet<Parser<?, ?, ?, ?>>();
		Set<MWMLModule> childModules = localModule.getAllChildren();
		for(MWMLModule childModule : childModules) {
			childParsers.addAll(childModule.getParserSystem().localParsers);
		}
		return childParsers;
	}
	public final Set<Parser<?, ?, ?, ?>> getAllParsers() {
		Set<Parser<?, ?, ?, ?>> parsers = new HashSet<Parser<?, ?, ?, ?>>();
		parsers.addAll(getParsersFromAllParentModules());
		parsers.addAll(localParsers);
		parsers.addAll(getParsersFromAllChildModules());
		return parsers;
	}
	protected Set<Value<?, ?, ?, ?>> parsePossibleValuesFromPureExpression(File file, String location, Signature valueSignature, Signature expressionSignature, String expression) {
		Set<Value<?, ?, ?, ?>> parsedValues = new HashSet<Value<?, ?, ?, ?>>();
		for(Parser<?, ?, ?, ?> parser : getAllParsers()) {
			ValueType valueType = parser.getValueType();
			ExpressionType expressionType = parser.getExpressionType();
			if(valueType.hasParent(valueSignature) && expressionType.hasParent(expressionSignature)) {
				try {
					Value<?, ?, ?, ?> parsedValue = parser.parseValue(localModule, valueSignature, expressionSignature, expression);
					if(parsedValue != null) {
						parsedValues.add(parsedValue);
					}
				}
				catch(Exception e) {
					List<String> message = new ArrayList<String>();
					String f = file.isFile() ? "file" : "folder";
					message.add("Exception occured while parsing " + f + " at location: " + location);
					message.add("This expression cannot be parsed successfully with given value and expression signatures: ");
					message.add("Value Signature: " + valueSignature);
					message.add("Expression Signature: " + expressionSignature);
					try {
						// localModule.getLogger().warning("The parser with value type: " + valueSignature + " and expression type: " + expressionSignature);
						Logger logger = localModule.getLogger();
						logger.warning(page(message));
					}
					catch(UnsupportedOperationException e1) {
						System.out.println("Error happened: Core MWML Module is trying to directly parse some values, and this should not happen!");
						System.out.println("Or maybe something else happened with a module's logger system.");
						System.out.println(page(message));
					}
				}
			}
		}
		return parsedValues;
	}
	/*
	 * This method could still be improved
	 */
	protected Value<?, ?, ?, ?> selectValue(Signature valueSignature, Signature expressionSignature, Set<Value<?, ?, ?, ?>> values) {
		// TODO the order should be rearranged
		Value<?, ?, ?, ?> selectedValue = null;
		Parser<?, ?, ?, ?> selectedParser = null;
		MWMLModule selectedModule = null;
		int selectedPriority = Integer.MIN_VALUE;
		for(Value<?, ?, ?, ?> value : values) {
			if(value.getParser().getValueType().getSignature().equals(valueSignature) && value.getParser().getExpressionType().getSignature().equals(expressionSignature)) {
				return value;
			}
			if(selectedValue == null) {
				selectedValue = value;
				selectedParser = selectedValue.getParser();
				selectedModule = selectedParser.getDefinerModule();
				selectedPriority = selectedParser.getPriority();
				continue;
			}
			Parser<?, ?, ?, ?> parser = value.getParser();
			int priority = parser.getPriority();
			MWMLModule module = parser.getDefinerModule();
			if(and(selectedParser.getDefinerModule() != localModule, module == localModule)) {
				selectedValue = value;
				selectedParser = selectedValue.getParser();
				selectedModule = selectedParser.getDefinerModule();
				selectedPriority = selectedParser.getPriority();
				continue;
			}
			if(or(selectedModule != localModule, and(module == localModule, selectedModule == localModule))) {
				if(priority > selectedPriority) {
					selectedValue = value;
					selectedParser = selectedValue.getParser();
					selectedModule = selectedParser.getDefinerModule();
					selectedPriority = selectedParser.getPriority();
					continue;
				}
				if(priority == selectedPriority) {
					if(module.getTier() < selectedModule.getTier()) {
						selectedValue = value;
						selectedParser = selectedValue.getParser();
						selectedModule = selectedParser.getDefinerModule();
						selectedPriority = selectedParser.getPriority();
						continue;
					}
					if(module.getTier() == selectedModule.getTier()) {
						if(parser.getValueType().getTier() * parser.getExpressionType().getTier() < selectedParser.getValueType().getTier() * selectedParser.getExpressionType().getTier()) {
							selectedValue = value;
							selectedParser = selectedValue.getParser();
							selectedModule = selectedParser.getDefinerModule();
							selectedPriority = selectedParser.getPriority();
							continue;
						}
					}
				}
			}
		}
		return selectedValue;
	}
	public Value<?, ?, ?, ?> parseFromPureExpression(File file, String location, Signature valueSignature, Signature expressionSignature, String expression) {
		Set<Value<?, ?, ?, ?>> values = parsePossibleValuesFromPureExpression(file, location, valueSignature, expressionSignature, expression);
		return selectValue(valueSignature, expressionSignature, values);
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