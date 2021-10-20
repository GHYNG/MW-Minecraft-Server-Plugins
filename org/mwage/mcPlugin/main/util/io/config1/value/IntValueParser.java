package org.mwage.mcPlugin.main.util.io.config1.value;
public class IntValueParser extends MetaValueParser<IntValue> {
	public IntValueParser() {
		super(StaticData.INT_TYPE_NAME);
	}
	@Override
	public boolean parsable(String content) {
		try {
			Integer.parseInt(content);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	@Override
	public IntValue parse(final CollectionValue<?, ?, ?> outerValue, String content) {
		try {
			int v = Integer.parseInt(content);
			return new IntValueInstance(outerValue, v);
		}
		catch(Exception e) {
			return new ErrorIntValueInstance(outerValue, "Unable to parse int value with any parser: ", content);
		}
	}
	public IntValue generate(final CollectionValue<?, ?, ?> outerValue, int v) {
		return new IntValueInstance(outerValue, v);
	}
}