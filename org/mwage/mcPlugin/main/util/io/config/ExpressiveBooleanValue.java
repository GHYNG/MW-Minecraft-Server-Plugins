package org.mwage.mcPlugin.main.util.io.config;
/**
 * 布尔表达值。
 * 
 * @author GHYNG
 * @param <A>
 *            实际对象。
 */
public interface ExpressiveBooleanValue<A> extends KeywordExpressiveValue<Boolean, A> {
	public default ExpressiveBooleanValue<A> getValue(Boolean b) {
		if(b == null) {
			return getValueNull();
		}
		if(b) {
			return getValueTrue();
		}
		return getValueFalse();
	}
	public ExpressiveBooleanValue<A> getValueTrue();
	public ExpressiveBooleanValue<A> getValueFalse();
	@Override
	public default NullExpressiveBooleanValue<A> getValueNull() {
		return new NullExpressiveBooleanValueInstance<A>();
	}
	public default ExpressiveBooleanValue<A> and(ExpressiveBooleanValue<A> another) {
		if(this instanceof NullValue<?, ?> || another instanceof NullValue<?, ?> || another == null) {
			return getValueNull();
		}
		boolean a = getCalculationInstance();
		boolean b = another.getCalculationInstance();
		if(a && b) {
			return getValueTrue();
		}
		return getValueFalse();
	}
	public default ExpressiveBooleanValue<A> or(ExpressiveBooleanValue<A> another) {
		if(this instanceof NullValue<?, ?> || another instanceof NullValue<?, ?> || another == null) {
			return getValueNull();
		}
		boolean a = getCalculationInstance();
		boolean b = another.getCalculationInstance();
		if(a || b) {
			return getValueTrue();
		}
		return getValueFalse();
	}
	public default ExpressiveBooleanValue<A> not() {
		if(this instanceof NullValue<?, ?>) {
			return getValueNull();
		}
		if(getCalculationInstance()) {
			return getValueFalse();
		}
		return getValueTrue();
	}
}