package org.mwage.mcPlugin.main.util.io.config1.value;
public interface ErrorDoubleValue extends DoubleValue, ErrorMetaValue<Double, Double> {}
class ErrorDoubleValueInstance extends DoubleValueInstance implements ErrorDoubleValue {
	protected String errorReason;
	protected String originalContent;
	ErrorDoubleValueInstance(CollectionValue<?, ?, ?> outerValue, String errorReason, String originalContent) {
		super(outerValue, 0);
		this.errorReason = errorReason;
		this.originalContent = originalContent;
	}
	@Override
	public String getOriginalContent() {
		return originalContent;
	}
	@Override
	public String getErrorReason() {
		return errorReason;
	}
}