package org.mwage.mcPlugin.main.util.io.config1.value;
public interface MetaValue<E, A> extends Value<E, A> {
	public String toContent();
}
class MetaValueInstance<E, A> implements ErrorValue<E, A>, MetaValue<E, A> {
	protected String typeName = "";
	protected String originalContent = "";
	protected String errorReason = "";
	MetaValueInstance(String typeName, String originalContent, String errorReason) {
		this.typeName = typeName;
		this.originalContent = originalContent;
		this.errorReason = errorReason;
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
	public String toContent() {
		return originalContent;
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