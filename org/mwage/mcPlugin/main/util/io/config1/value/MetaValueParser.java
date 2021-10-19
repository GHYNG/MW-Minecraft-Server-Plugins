package org.mwage.mcPlugin.main.util.io.config1.value;
public abstract class MetaValueParser<V extends MetaValue<?, ?>> {
	protected String typeName;
	public abstract boolean parsable(String content);
	public abstract V parse(String content);
	public MetaValueParser(String typeName) {
		this.typeName = typeName;
	}
	public final String getTypeName() {
		return typeName;
	}
}