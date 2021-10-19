package org.mwage.mcPlugin.main.util.io.config1.value;
public interface ErrorIntValue extends IntValue, ErrorValue<Integer, Integer> {}
class ErrorIntValueInstance extends IntValueInstance implements ErrorIntValue {
	protected String errorReason = "";
	protected String originalContent = "";
	ErrorIntValueInstance(String errorReason, String originalContent) {
		super(0);
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
	@Override
	public boolean equals(Object another) {
		if(this == another) {
			return true;
		}
		if(another instanceof ErrorIntValue anotherValue) {
			return errorReason.equals(anotherValue.getErrorReason()) && originalContent.equals(anotherValue.getOriginalContent());
		}
		return false;
	}
}