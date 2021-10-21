package org.mwage.mcPlugin.main.util.io.config1.value;
public interface DoubleValue extends ExpressiveDoubleValue<Double>, ActualDoubleValue<Double> {
	@Override
	default Double getCalculatableInstance() {
		Double c1 = ExpressiveDoubleValue.super.getCalculatableInstance();
		Double c2 = ActualDoubleValue.super.getCalculatableInstance();
		return c1 == c2 ? c1 : null;
	}
}
class DoubleValueInstance implements DoubleValue {
	protected final CollectionValue<?, ?, ?, ?, ?> outerValue;
	protected double value = 0;
	DoubleValueInstance(CollectionValue<?, ?, ?, ?, ?> outerValue, double value) {
		this.outerValue = outerValue;
		this.value = value;
	}
	@Override
	public CollectionValue<?, ?, ?, ?, ?> getOuterValue() {
		return outerValue;
	}
	@Override
	public String getTypeName() {
		return StaticData.DOUBLE_TYPE_NAME;
	}
	@Override
	public Double getExpressiveInstance() {
		return value;
	}
	@Override
	public Double getActualInstance() {
		return value;
	}
	@Override
	public String toContent() {
		return "" + value;
	}
	@Override
	public String toString() {
		return toContent();
	}
	@Override
	public boolean equals(Object another) {
		if(this == another) {
			return true;
		}
		if(this instanceof ErrorValue<?, ?> me && another instanceof ErrorValue<?, ?> err) {
			return(getTypeName().equals(err.getTypeName()) && me.getErrorReason().equals(err.getErrorReason()) && me.getOriginalContent().equals(err.getOriginalContent()));
		}
		if(another instanceof DoubleValue d) {
			double x = getCalculatableInstance();
			double y = d.getCalculatableInstance();
			return x == y;
		}
		return false;
	}
}