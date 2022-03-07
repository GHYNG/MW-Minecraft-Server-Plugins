package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.complex;
import java.util.Map;
import org.mwage.mcPlugin.main.util.CircularRegistrationException;
import org.mwage.mcPlugin.main.util.io.mwml.IdentifierFormatException;
import org.mwage.mcPlugin.main.util.io.mwml.ParserSystem;
import org.mwage.mcPlugin.main.util.io.mwml.parser.Parser;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.complex.ExpressiveTableParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.Value;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveNullValue;
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
extends ExpressiveComplexValue <
	V, 
	P, 
	E, 
	A
>
// @formatter:on
{
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
