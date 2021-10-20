package org.mwage.mcPlugin.main.util.io.config1.value;
import java.util.HashSet;
import java.util.Set;
public abstract class MetaValueParser<V extends MetaValue<?, ?>> {
	protected Set<String> keywords = new HashSet<String>();
	protected String typeName;
	public MetaValueParser(String typeName) {
		this.typeName = typeName;
	}
	public abstract boolean parsable(String content);
	public abstract V parse(final CollectionValue<?, ?, ?> outerValue, String content);
	public final String getTypeName() {
		return typeName;
	}
}