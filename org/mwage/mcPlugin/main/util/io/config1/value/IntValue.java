package org.mwage.mcPlugin.main.util.io.config1.value;
public interface IntValue extends ExpressiveIntValue<Integer>, ActualIntValue<Integer> {
	@Override
	default Integer getCalculatableInstance() {
		Integer c1 = ExpressiveIntValue.super.getCalculatableInstance();
		Integer c2 = ActualIntValue.super.getCalculatableInstance();
		return c1 == c2 ? c1 : null;
	}
}
class IntValueInstance implements IntValue {
	protected final CollectionValue<?, ?, ?, ?, ?> outerValue;
	protected int value = 0;
	IntValueInstance(CollectionValue<?, ?, ?, ?, ?> outerValue, int value) {
		this.outerValue = outerValue;
		this.value = value;
	}
	@Override
	public CollectionValue<?, ?, ?, ?, ?> getOuterValue() {
		return outerValue;
	}
	@Override
	public String getTypeName() {
		return StaticData.INT_TYPE_NAME;
	}
	@Override
	public Integer getExpressiveInstance() {
		return value;
	}
	@Override
	public Integer getActualInstance() {
		return value;
	}
	@Override
	public String toContent() {
		return "" + value;
	}
	@Override
	public boolean equals(Object another) {
		if(this == another) {
			return true;
		}
		if(this instanceof ErrorValue<?, ?> me && another instanceof ErrorValue<?, ?> err) {
			return(getTypeName().equals(err.getTypeName()) && me.getErrorReason().equals(err.getErrorReason()) && me.getOriginalContent().equals(err.getOriginalContent()));
		}
		if(another instanceof IntValue i) {
			int x = getCalculatableInstance();
			int y = i.getCalculatableInstance();
			return x == y;
		}
		return false;
	}
}