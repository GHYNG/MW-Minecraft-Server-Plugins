package org.mwage.mcPlugin.main.util.io.config1.value;
public interface ExpressiveIntValue<A> extends CalculatableIntValue<Integer, A>, MetaValue<Integer, A> {
	@Override
	default Class<Integer> getClassE() {
		return Integer.class;
	}
	@Override
	default Integer getCalculatableInstance() {
		return getExpressiveInstance();
	}
}