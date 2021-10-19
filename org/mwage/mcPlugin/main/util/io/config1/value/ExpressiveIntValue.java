package org.mwage.mcPlugin.main.util.io.config1.value;
public interface ExpressiveIntValue<A> extends CalculatableIntValue<Integer, A>, MetaValue<Integer, A> {
	@Override
	default Integer getCalculatableInstance() {
		return getExpressiveInstance();
	}
}