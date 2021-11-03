package org.mwage.mcPlugin.main.util.io.config1.value;
import org.mwage.mcPlugin.main.util.clazz.GenericTypeHeader;
import org.mwage.mcPlugin.main.util.clazz.GenericTypesInfo;
@GenericTypeHeader(superClass = CalculatableValue.class, typeParamaterName = "C", typeParamater = Object.class)
public interface CalculatableValue<C, E, A> extends Value<E, A> {
	Class<C> getClassC();
	C getCalculatableInstance();
	@Override
	default void prepareGenericTypesInfo(GenericTypesInfo<?> genericTypesInfo) {
		try {
			Value.super.prepareGenericTypesInfo(genericTypesInfo);
		}
		catch(PrepareWrongGenericTypesInfoException e) {
			throw new RuntimeException("Unexcepted exception occured.", e);
		}
		genericTypesInfo.put(CalculatableValue.class, "C", getClassC());
	}
}