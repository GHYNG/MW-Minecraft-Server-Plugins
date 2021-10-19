package org.mwage.mcPlugin.main.util.io.config1.value;
import java.util.ArrayList;
import java.util.List;
public class MetaValueParserManager {
	protected final List<MetaValueParser<?>> parsers = new ArrayList<MetaValueParser<?>>();
	public void registerParser(MetaValueParser<?> parser) {
		parsers.add(parser);
	}
	public void unregisterParser(MetaValueParser<?> parser) {
		while(parsers.contains(parser)) {
			parsers.remove(parser);
		}
	}
	public boolean parsable(String content) {
		for(MetaValueParser<?> parser : parsers) {
			if(parser.parsable(content)) {
				return true;
			}
		}
		return false;
	}
	@SuppressWarnings("rawtypes")
	public MetaValue<?, ?> parse(String typeName, String content) {
		int size = parsers.size();
		for(int i = size - 1; i >= 0; i--) {
			MetaValueParser<?> parser = parsers.get(i);
			if(parser.typeName.equals(typeName) && parser.parsable(content)) {
				MetaValue<?, ?> value = parser.parse(content);
				return value;
			}
		}
		return new ErrorMetaValueInstance(typeName, "Unable to parse value with any parser: ", content);
	}
}