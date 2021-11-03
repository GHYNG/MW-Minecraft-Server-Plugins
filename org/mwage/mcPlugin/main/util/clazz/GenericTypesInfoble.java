package org.mwage.mcPlugin.main.util.clazz;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import org.mwage.mcPlugin.main.util.methods.ClassUtil;
@GenericTypeHeader(superClass = GenericTypesInfoble.class, typeParamaterName = "T", typeParamater = GenericTypesInfoble.class)
public interface GenericTypesInfoble<T extends GenericTypesInfoble<T>> {
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	default GenericTypesInfo<? extends T> getGenericTypesInfo() {
		Map<GenericTypesInfo.Node, Class<?>> genericTypes = new HashMap<GenericTypesInfo.Node, Class<?>>();
		Class<?> myClass = getClass();
		GenericTypesInfobleUtil.getGenericTypesOfClass(myClass, genericTypes);
		GenericTypesInfo<? extends T> info = new GenericTypesInfo(myClass, genericTypes);
		return info;
	}
	default Class<?> getGenericType(GenericTypesInfo.Node node) {
		return getGenericTypesInfo().get(node);
	}
	default Class<?> getGenericType(Class<?> superClass, String typeParamaterName) {
		return getGenericTypesInfo().get(superClass, typeParamaterName);
	}
}
class GenericTypesInfobleUtil implements ClassUtil {
	static ClassUtil classUtil = new ClassUtil() {};
	static Map<GenericTypesInfo.Node, Class<?>> getGenericTypesOfClass(Class<?> clazz, final Map<GenericTypesInfo.Node, Class<?>> genericTypes) {
		Annotation[] annotations = clazz.getAnnotations();
		for(Annotation annotation : annotations) {
			if(annotation instanceof GenericTypeHeaders headers) {
				loopSuperClasses : for(GenericTypeHeader header : headers.value()) {
					Class<?> superClass = header.superClass();
					String typeParamaterName = header.typeParamaterName();
					Class<?> genericType = header.typeParamater();
					GenericTypesInfo.Node node = new GenericTypesInfo.Node(superClass, typeParamaterName);
					Class<?> existingGenericType = genericTypes.get(node);
					if(existingGenericType == null) {
						genericTypes.put(node, genericType);
						continue loopSuperClasses;
					}
					if(classUtil.aSuperb(existingGenericType, genericType)) {
						genericTypes.put(node, genericType);
						continue loopSuperClasses;
					}
					if(classUtil.aSuperb(genericType, existingGenericType)) {
						continue;
					}
					throw new GenericTypeCannotInheriteException("Generic type conflict in " + node + " with: " + genericType + " and " + existingGenericType);
				}
			}
		}
		classUtil.getAllSuperClasses(clazz).forEach(superClass -> {
			getGenericTypesOfClass(superClass, genericTypes);
		});
		return genericTypes;
	}
}