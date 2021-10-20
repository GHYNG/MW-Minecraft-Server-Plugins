package org.mwage.mcPlugin.main.util.io.config1.value;
public class DoubleValueParser extends MetaValueParser<DoubleValue> {
	public DoubleValueParser() {
		super(StaticData.DOUBLE_TYPE_NAME);
	}
	@Override
	public boolean parsable(String content) {
		try {
			Double.parseDouble(content);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	@Override
	public DoubleValue parse(CollectionValue<?, ?, ?> outerValue, String content) {
		try {
			double v = Double.parseDouble(content);
			return new DoubleValueInstance(outerValue, v);
		}
		catch(Exception e) {
			return new ErrorDoubleValueInstance(outerValue, "Unable to parse double value with any parser: ", content);
		}
	}
}
