package org.mwage.mcPlugin.main.util.io.config;
import java.util.HashSet;
import java.util.Set;
@MetaParsable
public interface IntValue extends ActualIntValue<Integer>, ExpressiveIntValue<Integer> {
	public static final IntValue VALUE_DEFAULT = new IntValueInstance(0);
	public static final NullIntValue VALUE_NULL = new NullIntValueInstance();
	@Override
	public default IntValue getValue(Integer i) {
		return parseValueFromString("" + i);
	}
	@Override
	public default NullIntValue getValueNull() {
		return VALUE_NULL;
	}
	public default Integer parseExpressiveInstanceFromString(String content) {
		try {
			Integer i = Integer.parseInt(content);
			return i;
		}
		catch(NumberFormatException e) {
			return null;
		}
	}
	public default IntValue parseValueFromString(String content) {
		try {
			Integer i = Integer.parseInt(content);
			return new IntValueInstance(i);
		}
		catch(NumberFormatException e) {
			return getValueNull();
		}
	}
	public default String toContent() {
		Integer actualInstance = getActualInstance();
		return actualInstance == null ? "null" : actualInstance.toString();
	}
}
class IntValueInstance implements IntValue {
	static Set<String> keyWords = new HashSet<String>();
	static {
		keyWords.add("int");
	}
	final Integer instance;
	IntValueInstance(Integer i) {
		if(i == null) {
			throw new NullPointerException("Null in IntValueInstance constuctor.");
		}
		instance = i;
	}
	@Override
	public Set<String> getKeywords() {
		return keyWords;
	}
	@Override
	public Integer getExpressiveInstance() {
		return instance;
	}
	@Override
	public Integer getActualInstance() {
		return instance;
	}
}