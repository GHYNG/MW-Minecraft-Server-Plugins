package org.mwage.mcPlugin.main.util.io.config1.value;
public interface ErrorMetaValue<E, A> extends MetaValue<E, A>, ErrorValue<E, A> {
	@Override
	public default String toContent() {
		return getOriginalContent();
	}
}
class ErrorMetaValueInstance<E, A> implements ErrorMetaValue<E, A>, MetaValue<E, A> {
	protected final CollectionValue<?, ?, ?> outerValue;
	protected String typeName = "";
	protected String originalContent = "";
	protected String errorReason = "";
	ErrorMetaValueInstance(CollectionValue<?, ?, ?> outerValue, String typeName, String originalContent, String errorReason) {
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