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
	public IntValue parse(String content) {
		try {
			int v = Integer.parseInt(content);
			return new IntValueInstance(v);
		}
		catch(Exception e) {
			return new ErrorIntValueInstance("Unable to parse int value with any parser: ", content);
		}
	}
	public IntValue generate(int v) {
		return new IntValueInstance(v);
	}
}