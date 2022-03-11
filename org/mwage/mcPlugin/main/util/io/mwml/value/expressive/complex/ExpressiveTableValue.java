package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.complex;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.mwage.mcPlugin.main.util.CircularRegistrationException;
import org.mwage.mcPlugin.main.util.io.mwml.IdentifierFormatException;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.ParserSystem;
import org.mwage.mcPlugin.main.util.io.mwml.parser.Parser;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.complex.ExpressiveTableParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.Value;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualBooleanValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualByteValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualDoubleValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualEnumValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualFloatValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualIntValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualLongValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualNumberValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualShortValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualStringValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveNullValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressivePrimaryValue;
import org.mwage.mcPlugin.main.util.methods.LogicUtil;
import org.mwage.mcPlugin.main.util.methods.StringUtil;
// @formatter:off
public interface ExpressiveTableValue <
	V extends ExpressiveTableValue <
		V, 
		P, 
		E, 
			E_V, 
			E_P, 
				E_VP_E, 
				E_VP_A, 
		A
	>,
	P extends ExpressiveTableParser <
		P, 
		V, 
		E, 
			E_V, 
			E_P, 
				E_VP_E, 
				E_VP_A, 
		A
	>,
	E extends Map <
		String, 
		E_V
	>,
		E_V extends Value <
			E_V, 
			E_P, 
				E_VP_E, 
				E_VP_A
		>,
		E_P extends Parser <
			E_P, 
			E_V, 
				E_VP_E, 
				E_VP_A
		>,
			E_VP_E,
			E_VP_A,
	A
> 
extends 
ExpressiveComplexValue <
	V, 
	P, 
	E, 
	A
>, 
StringUtil, LogicUtil
// @formatter:on
{
	@Override
	default String toExpression() {
		List<String> lines = new ArrayList<String>();
		lines.add("table:{");
		Map<String, E_V> map = getExpressiveInstance();
		if(map.keySet().size() == 0) {
			return "table:{}";
		}
		for(String key : map.keySet()) {
			E_V innerValue = map.get(key);
			if(innerValue == null) {
				continue;
			}
			Signature usingValueSignature = innerValue.getUsingValueSignature();
			Signature usingExpressionSignature = innerValue.getUsingExpressionSignature();
			if(innerValue instanceof ExpressivePrimaryValue) {
				String line = "" + usingValueSignature + " " + key + " = " + usingExpressionSignature + " " + innerValue.toExpression() + ";";
				lines.add(line);
				continue;
			}
			if(innerValue instanceof ExpressiveComplexValue) {
				String innerExpression = innerValue.toExpression();
				List<String> innerLines = separateByLine(innerExpression);
				int length = innerLines.size();
				if(length == 0) {
					String line = "" + usingValueSignature + " " + key + " = " + usingExpressionSignature + " null;";
					lines.add(line);
					continue;
				}
				if(length == 1) {
					String line = "" + usingValueSignature + " " + key + " = " + usingExpressionSignature + " " + innerExpression + ";";
					lines.add(line);
					continue;
				}
				for(int i = 1; i < length - 1; i++) {
					String innerLine = "\t" + innerLines.get(i);
					innerLines.set(i, innerLine);
				}
				String line = "" + usingValueSignature + " " + key + " = " + usingExpressionSignature + " " + page(innerLines) + ";";
				lines.add(line);
			}
		}
		lines.add("}");
		return page(lines);
	}
	@Override
	default int size(IterationRange iterationRange) {
		int size = 0;
		switch(iterationRange) {
			case DIRECT_ONLY :
				size = getExpressiveInstance().size();
				return size;
			case ALL :
				Map<String, E_V> map = getExpressiveInstance();
				size += map.size();
				for(String key : map.keySet()) {
					E_V innerValue = map.get(key);
					if(innerValue == null || innerValue instanceof ExpressiveNullValue) {
						continue;
					}
					if(innerValue instanceof ExpressiveComplexValue<?, ?, ?, ?> ecv) {
						size += ecv.size(iterationRange);
					}
				}
				return size;
		}
		return size;
	}
	@Override
	default boolean contains(Value<?, ?, ?, ?> value, IterationRange iterationRange, EqualsStandard equalsStandard) {
		if(value == null) {
			return false;
		}
		Map<String, E_V> map = getExpressiveInstance();
		for(String key : map.keySet()) {
			E_V innerValue = map.get(key);
			if(innerValue == null) {
				continue;
			}
			boolean flag = false;
			switch(equalsStandard) {
				case BY_OBJECT_EQUALS :
					if(value.equals(innerValue)) return true;
				case BY_REFERENCE :
					if(value == innerValue) return true;
				default :
					break;
			}
			if(iterationRange == IterationRange.ALL && innerValue instanceof ExpressiveComplexValue<?, ?, ?, ?> ecv) {
				flag = ecv.contains(value, iterationRange, equalsStandard);
				if(flag) {
					return true;
				}
			}
		}
		return false;
	}
	default boolean contains(E_V value, EqualsStandard equalsStandard) {
		if(value == null) {
			return false;
		}
		Map<String, E_V> map = getExpressiveInstance();
		for(String key : map.keySet()) {
			E_V innervalue = map.get(key);
			if(innervalue == null) {
				return false;
			}
			switch(equalsStandard) {
				case BY_VALUE_EQUALS :
					if(value.valueEquals(innervalue)) return true;
				case BY_OBJECT_EQUALS :
					if(value.equals(innervalue)) return true;
				case BY_REFERENCE :
					if(value == innervalue) return true;
			}
		}
		return false;
	}
	@Override
	default E_V findInnerValueWithKey(String key) {
		return get(key);
	}
	@Override
	default boolean valueEquals(V another) {
		if(another == null) {
			return false;
		}
		if(or(this.getUsingValueSignature() != another.getUsingValueSignature(), this.getUsingExpressionSignature() != another.getUsingExpressionSignature())) {
			return false;
		}
		if(this == another || this.equals(another)) {
			return true;
		}
		Map<String, E_V> thismap = this.getExpressiveInstance(), anothermap = another.getExpressiveInstance();
		Set<String> thiskeys = thismap.keySet(), anotherkeys = anothermap.keySet();
		if(!thiskeys.equals(anotherkeys)) {
			return false;
		}
		for(String k : thiskeys) {
			E_V thisvalue = thismap.get(k), anothervalue = anothermap.get(k);
			if(and(thisvalue == null, anothervalue == null)) {
				return true;
			}
			if(or(thisvalue == null, anothervalue == null)) {
				return false;
			}
			if(!thisvalue.valueEquals(anothervalue)) {
				return false;
			}
		}
		return true;
	}
	default boolean put(String key, E_V value) throws CircularRegistrationException {
		if(!ParserSystem.isLegalIdentifier(key)) {
			throw new IdentifierFormatException("Wrong format for identifier: " + key);
		}
		if(this == value) {
			throw new CircularRegistrationException("Trying to put one value as its member value again.");
		}
		Map<String, E_V> map = getExpressiveInstance();
		E_V innerValue = map.get(key);
		if(value == innerValue) {
			return false;
		}
		if(value instanceof ExpressiveComplexValue<?, ?, ?, ?> ecv) {
			if(ecv.contains(this, IterationRange.ALL, EqualsStandard.BY_REFERENCE)) {
				throw new CircularRegistrationException("Trying to put a outer value to become a member of its inner value.");
			}
		}
		P parser = getParser();
		Map<String, Function<E_V, Boolean>> goodParameterJudgers = parser.getGoodParameterJudger();
		Function<E_V, Boolean> goodParameterJudger = goodParameterJudgers.get(key);
		if(goodParameterJudger != null) {
			boolean good = goodParameterJudger.apply(value);
			if(!good) {
				return false;
			}
		}
		try {
			value.appendToOuterValue(this);
		}
		catch(RuntimeException e) {
			return false;
		}
		map.put(key, value);
		return true;
	}
	default void putAll(Map<String, E_V> values) {
		for(String key : values.keySet()) {
			E_V value = values.get(key);
			if(value == null) {
				continue;
			}
			E_V input = value.clone();
			try {
				input.appendToOuterValue(this);
			}
			catch(RuntimeException e) {
				continue;
			}
			put(key, input);
		}
	}
	default void putAll(ExpressiveTableValue<V, P, E, E_V, E_P, E_VP_E, E_VP_A, A> anotherMapValue) {
		Map<String, E_V> anotherMap = anotherMapValue.getExpressiveInstance();
		putAll(anotherMap);
	}
	default E_V get(String key) {
		if(key == null) {
			throw new NullPointerException("Java method parameter: key is null.");
		}
		Map<String, E_V> map = getExpressiveInstance();
		E_V value = map.get(key);
		if(value == null) {
			E_V defaultValue = getParser().getDefaultValues().get(key);
			return defaultValue == null ? null : defaultValue.clone();
		}
		return value;
	}
	default E_V get(String key, E_V defaultValue) {
		E_V innervalue = get(key);
		return innervalue == null ? defaultValue == null ? null : defaultValue.clone() : null;
	}
	default ActualBooleanValue<?, ?, ?> getBooleanValue(String key) {
		return getBooleanValue(key, null);
	}
	default ActualBooleanValue<?, ?, ?> getBooleanValue(String key, ActualBooleanValue<?, ?, ?> defaultValue) {
		Value<?, ?, ?, ?> innervalue = get(key);
		if(innervalue == null) {
			return defaultValue == null ? null : defaultValue.clone();
		}
		if(innervalue instanceof ActualBooleanValue<?, ?, ?> iv) {
			return iv;
		}
		return defaultValue == null ? null : defaultValue.clone();
	}
	default ActualByteValue<?, ?, ?> getByteValue(String key) {
		return getByteValue(key, null);
	}
	default ActualByteValue<?, ?, ?> getByteValue(String key, ActualByteValue<?, ?, ?> defaultValue) {
		Value<?, ?, ?, ?> innervalue = get(key);
		if(innervalue == null) {
			return defaultValue == null ? null : defaultValue.clone();
		}
		if(innervalue instanceof ActualByteValue<?, ?, ?> iv) {
			return iv;
		}
		return defaultValue == null ? null : defaultValue.clone();
	}
	default ActualDoubleValue<?, ?, ?> getDoubleValue(String key) {
		return getDoubleValue(key, null);
	}
	default ActualDoubleValue<?, ?, ?> getDoubleValue(String key, ActualDoubleValue<?, ?, ?> defaultValue) {
		Value<?, ?, ?, ?> innervalue = get(key);
		if(innervalue == null) {
			return defaultValue == null ? null : defaultValue.clone();
		}
		if(innervalue instanceof ActualDoubleValue<?, ?, ?> iv) {
			return iv;
		}
		return defaultValue == null ? null : defaultValue.clone();
	}
	default ActualEnumValue<?, ?, ?, ?> getEnumValue(String key) {
		return getEnumValue(key, null);
	}
	default ActualEnumValue<?, ?, ?, ?> getEnumValue(String key, ActualEnumValue<?, ?, ?, ?> defaultValue) {
		Value<?, ?, ?, ?> innervalue = get(key);
		if(innervalue == null) {
			return defaultValue == null ? null : defaultValue.clone();
		}
		if(innervalue instanceof ActualEnumValue<?, ?, ?, ?> iv) {
			return iv;
		}
		return defaultValue == null ? null : defaultValue.clone();
	}
	default ActualFloatValue<?, ?, ?> getFloatValue(String key) {
		return getFloatValue(key, null);
	}
	default ActualFloatValue<?, ?, ?> getFloatValue(String key, ActualFloatValue<?, ?, ?> defaultValue) {
		Value<?, ?, ?, ?> innervalue = get(key);
		if(innervalue == null) {
			return defaultValue == null ? null : defaultValue.clone();
		}
		if(innervalue instanceof ActualFloatValue<?, ?, ?> iv) {
			return iv;
		}
		return defaultValue == null ? null : defaultValue.clone();
	}
	default ActualIntValue<?, ?, ?> getIntValue(String key) {
		return getIntValue(key, null);
	}
	default ActualIntValue<?, ?, ?> getIntValue(String key, ActualIntValue<?, ?, ?> defaultValue) {
		Value<?, ?, ?, ?> innervalue = get(key);
		if(innervalue == null) {
			return defaultValue == null ? null : defaultValue.clone();
		}
		if(innervalue instanceof ActualIntValue<?, ?, ?> iv) {
			return iv;
		}
		return defaultValue == null ? null : defaultValue.clone();
	}
	default ActualLongValue<?, ?, ?> getLongValue(String key) {
		return getLongValue(key, null);
	}
	default ActualLongValue<?, ?, ?> getLongValue(String key, ActualLongValue<?, ?, ?> defaultValue) {
		Value<?, ?, ?, ?> innervalue = get(key);
		if(innervalue == null) {
			return defaultValue == null ? null : defaultValue.clone();
		}
		if(innervalue instanceof ActualLongValue<?, ?, ?> iv) {
			return iv;
		}
		return defaultValue == null ? null : defaultValue.clone();
	}
	default ActualNumberValue<?, ?, ?, ?> getNumberValue(String key) {
		return getFloatValue(key, null);
	}
	default ActualNumberValue<?, ?, ?, ?> getNumberValue(String key, ActualNumberValue<?, ?, ?, ?> defaultValue) {
		Value<?, ?, ?, ?> innervalue = get(key);
		if(innervalue == null) {
			return defaultValue == null ? null : defaultValue.clone();
		}
		if(innervalue instanceof ActualNumberValue<?, ?, ?, ?> iv) {
			return iv;
		}
		return defaultValue == null ? null : defaultValue.clone();
	}
	default ActualShortValue<?, ?, ?> getShortValue(String key) {
		return getShortValue(key, null);
	}
	default ActualShortValue<?, ?, ?> getShortValue(String key, ActualShortValue<?, ?, ?> defaultValue) {
		Value<?, ?, ?, ?> innervalue = get(key);
		if(innervalue == null) {
			return defaultValue == null ? null : defaultValue.clone();
		}
		if(innervalue instanceof ActualShortValue<?, ?, ?> iv) {
			return iv;
		}
		return defaultValue == null ? null : defaultValue.clone();
	}
	default ActualStringValue<?, ?, ?> getStringValue(String key) {
		return getStringValue(key, null);
	}
	default ActualStringValue<?, ?, ?> getStringValue(String key, ActualStringValue<?, ?, ?> defaultValue) {
		Value<?, ?, ?, ?> innervalue = get(key);
		if(innervalue == null) {
			return defaultValue == null ? null : defaultValue.clone();
		}
		if(innervalue instanceof ActualStringValue<?, ?, ?> iv) {
			return iv;
		}
		return defaultValue == null ? null : defaultValue.clone();
	}
}