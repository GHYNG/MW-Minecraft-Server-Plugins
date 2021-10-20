package org.mwage.mcPlugin.main.util.io.config1.value;
public interface ErrorValue<E, A> extends Value<E, A> {
	public String getOriginalContent();
	public String getErrorReason();
	public default String getMessage() {
		return getErrorReason() + getOriginalContent();
	}
}
/*
 * All normal methods from Value in ErrorValueInstances should return null.
 */
class ErrorValueInstance<E, A> implements ErrorValue<E, A> {
	protected final CollectionValue<?, ?, ?> outerValue;
	protected String typeName = "";
	protected String originalContent = "";
	protected String errorReason = "";
	ErrorValueInstance(CollectionValue<?, ?, ?> outerValue, String typeName, String originalContent, String errorReason) {
		this.outerValue = outerValue;
		this.typeName = typeName;
		this.originalContent = originalContent;
		this.errorReason = errorReason;
	}
	@Override
	public CollectionValue<?, ?, ?> getOuterValue() {
		return outerValue;
	}
	@Override
	public String getTypeName() {
		return typeName;
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
	public E getExpressiveInstance() {
		return null; // return null
	}
	@Override
	public A getActualInstance() {
		return null; // return null
	}
}