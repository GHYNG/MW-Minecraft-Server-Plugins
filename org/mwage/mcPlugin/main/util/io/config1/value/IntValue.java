package org.mwage.mcPlugin.main.util.io.config1.value;
public interface IntValue extends ExpressiveIntValue<Integer>, ActualIntValue<Integer> {
	@Override
	default Integer getInstanceC() {
		Integer c1 = ExpressiveIntValue.super.getInstanceC();
		Integer c2 = ActualIntValue.super.getInstanceC();
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
	public Integer getInstanceE() {
		return value;
	}
	@Override
	public Integer getInstanceA() {
		return value;
	}
	@Override
	public String toContent() {
		return "" + value;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object another) {
		if(this == another) {
			return true;
		}
		if(this instanceof ErrorValue me && another instanceof ErrorValue err) {
			return(getTypeName().equals(err.getTypeName()) && me.getErrorReason().equals(err.getErrorReason()) && me.getOriginalContent().equals(err.getOriginalContent()));
		}
		if(another instanceof IntValue i) {
			int x = getInstanceC();
			int y = i.getInstanceC();
			return x == y;
		}
		return false;
	}
}