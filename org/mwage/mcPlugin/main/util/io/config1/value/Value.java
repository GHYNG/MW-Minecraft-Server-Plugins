package org.mwage.mcPlugin.main.util.io.config1.value;
import org.mwage.mcPlugin.main.util.clazz.GenericTypeHeader;
import org.mwage.mcPlugin.main.util.clazz.GenericTypesInfo;
import org.mwage.mcPlugin.main.util.clazz.GenericTypesInfoble;
@GenericTypeHeader(superClass = Value.class, typeParamaterName = "E", typeParamater = Object.class)
@GenericTypeHeader(superClass = Value.class, typeParamaterName = "V", typeParamater = Object.class)
@GenericTypeHeader(superClass = GenericTypesInfoble.class, typeParamaterName = "T", typeParamater = Value.class)
public interface Value<E, A> extends GenericTypesInfoble<Value<E, A>> {
	Class<E> getClassE();
	Class<A> getClassA();
	E getExpressiveInstance();
	A getActualInstance();
	default void prepareGenericTypesInfo(GenericTypesInfo<?> genericTypesInfo) throws PrepareWrongGenericTypesInfoException {
		GenericTypesInfo<? extends Value<E, A>> myGenericTypesInfo = getGenericTypesInfo();
		if(!myGenericTypesInfo.equals(genericTypesInfo)) {
			throw new PrepareWrongGenericTypesInfoException("Only allow to prepare this value instance's own GenricTypesInfo instance.");
		}
		genericTypesInfo.put(Value.class, "E", getClassE());
		genericTypesInfo.put(Value.class, "A", getClassA());
	}
	@Override
	default GenericTypesInfo<? extends Value<E, A>> getGenericTypesInfo() {
		GenericTypesInfo<? extends Value<E, A>> info = GenericTypesInfoble.super.getGenericTypesInfo();
		try {
			prepareGenericTypesInfo(info);
		}
		catch(PrepareWrongGenericTypesInfoException e) {
			throw new RuntimeException("Unexcepted exception occured.", e);
		}
		return info;
	}
	CollectionValue<?, ?, ?, ?, ?> getOuterValue();
	default CollectionValue<?, ?, ?, ?, ?> getOutestValue() {
		if(this instanceof CollectionValue<?, ?, ?, ?, ?> container) {
			while(container.getOuterValue() != null) {
				container = container.getOuterValue();
			}
			return container;
		}
		else {
			CollectionValue<?, ?, ?, ?, ?> outerValue = getOuterValue();
			return outerValue == null ? null : outerValue.getOutestValue();
		}
	}
	String getTypeName();
}