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
	protected double value = 0;
	DoubleValueInstance(double value) {
		this.value = value;
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
	
	
}