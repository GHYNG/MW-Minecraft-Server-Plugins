package org.mwage.mcPlugin.main.util.io.config;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public interface NullExpressiveBooleanValue<A> extends NullValue<String, A>, ExpressiveBooleanValue<A> {
	@Override
	default Set<String> getKeywords() {
		Map<String, KeywordExpressiveValue<Boolean, A>> keywordTable = getKeywordTable();
		Set<String> keyWords = new HashSet<String>();
		keyWords.addAll(keywordTable.keySet());
		keyWords.add(NAME_NULL);
		return keyWords;
	}
}
class NullExpressiveBooleanValueInstance<A> implements NullExpressiveBooleanValue<A> {
	Map<String, KeywordExpressiveValue<Boolean, A>> keywordTable = new HashMap<String, KeywordExpressiveValue<Boolean, A>>();
	{
		keywordTable.put(NAME_NULL, this);
	}
	@Override
	public ExpressiveBooleanValue<A> getValueTrue() {
		return this;
	}
	@Override
	public ExpressiveBooleanValue<A> getValueFalse() {
		return this;
	}
	@Override
	public Map<String, KeywordExpressiveValue<Boolean, A>> getKeywordTable() {
		return keywordTable;
	}
	@Override
	public Boolean getCalculationInstance() {
		return null;
	}
}