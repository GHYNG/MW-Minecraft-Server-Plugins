package org.mwage.mcPlugin.main.util.clazz;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.mwage.mcPlugin.main.util.methods.ClassUtil;
public class GenericTypesInfo<T> implements ClassUtil {
	public static record Node(Class<?> clazz, String typeParamaterName) {
		@Override
		public boolean equals(Object obj) {
			if(this == obj) {
				return true;
			}
			if(obj instanceof Node another) {
				return clazz == another.clazz && typeParamaterName.equals(another.typeParamaterName);
			}
			return false;
		}
		@Override
		public String toString() {
			return clazz.toString() + "<" + typeParamaterName + ">";
		}
	}
	public final Class<T> clazz;
	protected final Map<Node, Class<?>> genericTypes = new HashMap<Node, Class<?>>();
	public GenericTypesInfo(Class<T> clazz) {
		this.clazz = clazz;
	}
	public GenericTypesInfo(Class<T> clazz, Map<Node, Class<?>> genericTypes) {
		this(clazz);
		genericTypes.keySet().forEach(node -> {
			Class<?> superClass = node.clazz;
			if(aSuperb(superClass, clazz)) {
				this.genericTypes.put(node, genericTypes.get(node));
			}
		});
	}
	public Class<?> get(Class<?> superClass, String typeParamaterName) {
		Node node = new Node(superClass, typeParamaterName);
		return genericTypes.get(node);
	}
	public Class<?> get(Node node) {
		return genericTypes.get(node);
	}
	public void put(Class<?> superClass, String typeParamaterName, Class<?> typeParamater) throws ClassDoesNotInheritException {
		if(!aSuperb(superClass, clazz)) {
			throw new ClassDoesNotInheritException("Cannot register type paramater in super class, because " + clazz + "does not extend " + superClass);
		}
		Node node = new Node(superClass, typeParamaterName);
		Class<?> targetClass = genericTypes.get(node);
		if(targetClass != null && !aSuperb(targetClass, typeParamater)) {
			throw new ClassDoesNotInheritException("Cannot register type paramater in super class, because " + typeParamater + " does not extend existing mapped class: " + targetClass);
		}
		genericTypes.put(node, typeParamater);
	}
	public <TP> void put(Node node, Class<TP> typeParamater) {
		Class<?> superClass = node.clazz;
		String typeParamaterName = node.typeParamaterName;
		put(superClass, typeParamaterName, typeParamater);
	}
	public boolean isSuperTo(GenericTypesInfo<?> another) {
		if(another == null) {
			return false;
		}
		if(equals(another)) {
			return true;
		}
		Class<?> anotherClazz = another.clazz;
		if(!aSuperb(clazz, anotherClazz)) {
			return false;
		}
		int size = genericTypes.size();
		int anotherSize = another.genericTypes.size();
		if(size != anotherSize) {
			return false;
		}
		Set<Node> nodes = genericTypes.keySet();
		Set<Node> anotherNodes = another.genericTypes.keySet();
		for(Node node : nodes) {
			Class<?> genericType = genericTypes.get(node);
			Class<?> anotherGenericType = another.genericTypes.get(node);
			if(genericType == anotherGenericType) {
				continue;
			}
			if(!aSuperb(genericType, anotherGenericType)) {
				return false;
			}
		}
		for(Node anotherNode : anotherNodes) {
			Class<?> genericType = genericTypes.get(anotherNode);
			Class<?> anotherGenericType = another.genericTypes.get(anotherNode);
			if(genericType == anotherGenericType) {
				continue;
			}
			if(!aSuperb(genericType, anotherGenericType)) {
				return false;
			}
		}
		return true;
	}
	public boolean isSubTo(GenericTypesInfo<?> another) {
		if(another == null) {
			return false;
		}
		return another.isSuperTo(this);
	}
	public boolean isInstance(GenericTypesInfoble<?> obj) {
		GenericTypesInfo<?> objInfo = obj.getGenericTypesInfo();
		return isSuperTo(objInfo);
	}
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj instanceof GenericTypesInfo<?> another) {
			return genericTypes.equals(another.genericTypes);
		}
		return false;
	}
	@Override
	public String toString() {
		return clazz.getName() + ": " + genericTypes.toString();
	}
}