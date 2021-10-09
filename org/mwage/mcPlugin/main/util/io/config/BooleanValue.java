package org.mwage.mcPlugin.main.util.io.config;
import java.util.HashMap;
import java.util.Map;
@MetaParsable
public interface BooleanValue extends ExpressiveBooleanValue<Boolean>, ActualBooleanValue<String> {
	public static String NAME_TRUE = "true";
	public static String NAME_FALSE = "false";
	public static BooleanValue VALUE_TRUE = new BooleanValueInstance(true);
	public static BooleanValue VALUE_FALSE = new BooleanValueInstance(false);
	public static BooleanValue VALUE_DEFAULT = VALUE_FALSE;
	@Override
	public default BooleanValue getValueTrue() {
		return VALUE_TRUE;
	}
	@Override
	public default BooleanValue getValueFalse() {
		return VALUE_FALSE;
	}
	public default BooleanValue getValueDefault() {
		return VALUE_DEFAULT;
	}
}
class BooleanValueInstance implements BooleanValue {
	final Boolean instance;
	final String key;
	static final Map<String, KeywordExpressiveValue<Boolean, Boolean>> keywordTable = new HashMap<String, KeywordExpressiveValue<Boolean, Boolean>>();
	static {
		keywordTable.put(NAME_TRUE, VALUE_TRUE);
		keywordTable.put(NAME_FALSE, VALUE_FALSE);
	}
	BooleanValueInstance(Boolean instance) {
		if(instance == null) {
			throw new NullPointerException("Null in BooleanValueInstance constuctor.");
		}
		if(instance) {
			key = NAME_TRUE;
		}
		else {
			key = NAME_FALSE;
		}
		this.instance = instance;
	}
	@Override
	public Map<String, KeywordExpressiveValue<Boolean, Boolean>> getKeywordTable() {
		return keywordTable;
	}
	@Override
	public Boolean getCalculationInstance() {
		return instance;
	}
	@Override
	public String getExpressiveInstance() {
		return key;
	}
	@Override
	public Boolean getActualInstance() {
		return instance;
	}
}