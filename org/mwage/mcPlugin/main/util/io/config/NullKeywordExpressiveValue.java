package org.mwage.mcPlugin.main.util.io.config;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public interface NullKeywordExpressiveValue<C, A> extends NullValue<String, A>, KeywordExpressiveValue<C, A> {
	@Override
	public default Set<String> getKeywords() {
		Set<String> keywordsA = NullValue.super.getKeywords();
		Set<String> keywordsB = KeywordExpressiveValue.super.getKeywords();
		Set<String> keywords = new HashSet<String>();
		keywords.addAll(keywordsA);
		keywords.addAll(keywordsB);
		return keywords;
	}
}
class NullKeywordExpressiveValueInstance<C, A> implements NullKeywordExpressiveValue<C, A> {
	@Override
	public Map<String, KeywordExpressiveValue<C, A>> getKeywordTable() {
		Map<String, KeywordExpressiveValue<C, A>> map = new HashMap<String, KeywordExpressiveValue<C, A>>();
		map.put(NullValue.NAME_NULL, new NullKeywordExpressiveValueInstance<C, A>());
		return map;
	}
	@Override
	public C getCalculationInstance() {
		return null;
	}
}
