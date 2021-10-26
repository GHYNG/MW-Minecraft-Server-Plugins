package org.mwage.mcPlugin.main.util.clazz;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.mwage.mcPlugin.main.util.methods.ClassUtil;
public class GenericTypesInfo<T> implements ClassUtil {
	public static record Node(Class<?> clazz, String genericTypeParamaterName) {
		@Override
		public boolean equals(Object obj) {
			if(this == obj) {
				return true;
			}
			if(obj instanceof Node another) {
				return clazz == another.clazz && genericTypeParamaterName.equals(another.genericTypeParamaterName);
			}
			return false;
		}
		@Override
		public String toString() {
			return clazz.toString() + "<" + genericTypeParamaterName + ">";
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
			System.out.println(superClass.getName());
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