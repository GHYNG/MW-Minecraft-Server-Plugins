package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.complex;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.mwage.mcPlugin.main.util.CircularRegistrationException;
import org.mwage.mcPlugin.main.util.io.mwml.IdentifierFormatException;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.ParserSystem;
import org.mwage.mcPlugin.main.util.io.mwml.parser.Parser;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.complex.ExpressiveTableParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.Value;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveNullValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressivePrimaryValue;
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
StringUtil
// @formatter:on
{
	@Override
	default String toExpression() {
		List<String> lines = new ArrayList<String>();
		lines.add("list:{");
		Map<String, E_V> map = getExpressiveInstance();
		if(map.keySet().size() == 0) {
			return "list:{}";
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
				case BY_VALUE :
					flag = value.equals(innerValue);
					break;
				case BY_REFERENCE :
					flag = value == innerValue;
					break;
			}
			if(flag) {
				return true;
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
	@Override
	default E_V findInnerValueWithKey(String key) {
		return get(key);
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
		map.put(key, value);
		value.appendToOuterValue(this);
		return true;
	}
	default E_V get(String key) {
		if(key == null) {
			throw new NullPointerException("Java method parameter: key is null.");
		}
		Map<String, E_V> map = getExpressiveInstance();
		return map.get(key);
	}
}
