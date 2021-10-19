package org.mwage.mcPlugin.main.util.io.config;
public interface NullIntValue extends NullValue<Integer, Integer>, IntValue {
}
class NullIntValueInstance extends IntValueInstance implements NullIntValue {
	NullIntValueInstance(Integer i) {
		super(i);
	}
}