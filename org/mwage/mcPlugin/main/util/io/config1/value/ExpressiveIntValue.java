package org.mwage.mcPlugin.main.util.io.config1.value;

import org.mwage.mcPlugin.main.util.clazz.GenericTypeHeader;

@GenericTypeHeader(superClass = Value.class, typeParamaterName = "E", typeParamater = Integer.class)
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