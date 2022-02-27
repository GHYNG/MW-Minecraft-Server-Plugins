package org.mwage.mcPlugin.main.util.io.config2;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mwage.mcPlugin.main.util.MWCloneable;
public class ActualType implements MWCloneable<ActualType> {
	private final String projectName;
	private final List<String> packageNames = new ArrayList<String>();
	private final String name;
	private final Set<ActualType> parentActualTypes = new HashSet<ActualType>();
	private final Set<ActualType> childActualTypes = new HashSet<ActualType>();
	public ActualType(String projectName, List<String> packageNames, String name) {
		if(!ParserSystem.isLegalIdentifier(projectName) && (!projectName.equals(""))) {
			throw new IdentifierFormatException("Given projectName: \"" + projectName + "\" is not legal for an identifier");
		}
		if(!ParserSystem.isLegalIdentifier(name)) {
			throw new IdentifierFormatException("Given name: \"" + name + "\" is not legal for an identifier");
		}
		this.projectName = projectName;
		this.name = name;
		for(String packageName : packageNames) {
			if(!ParserSystem.isLegalIdentifier(packageName)) {
				throw new IdentifierFormatException("Given packageName: \"" + name + "\" is not legal for an identifier");
			}
			this.packageNames.add(packageName);
		}
	}
	@Override
	public ActualType clone() {
		ActualType another = new ActualType(projectName, packageNames, name);
		another.parentActualTypes.addAll(parentActualTypes);
		another.childActualTypes.addAll(childActualTypes);
		return another;
	}
	@Override
	public String toString() {
		String longPackageName = "";
		int packageNamesSize = packageNames.size();
		if(packageNamesSize != 0) {
			for(int i = 0; i < packageNamesSize - 1; i++) {
				longPackageName += packageNames.get(i) + ".";
			}
			longPackageName += packageNames.get(packageNamesSize - 1);
		}
		return projectName + ":" + longPackageName + ":" + name;
	}
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	@Override
	public boolean equals(Object another) {
		if(this == another) {
			return true;
		}
		if(another == null) {
			return false;
		}
		if(another instanceof ActualType at) {
			return toString().equals(at.toString());
		}
		return false;
	}
	public boolean hasParent(ActualType another) {
		if(another == null) {
			return false;
		}
		if(this.equals(another)) {
			return true;
		}
		for(ActualType parent : parentActualTypes) {
			if(parent.hasParent(another)) {
				return true;
			}
		}
		return false;
	}
	public boolean hasChild(ActualType another) {
		if(another == null) {
			return false;
		}
		if(this.equals(another)) {
			return true;
		}
		for(ActualType child : childActualTypes) {
			if(child.hasChild(another)) {
				return true;
			}
		}
		return false;
	}
	public boolean registerParentActualType(ActualType another) {
		if(another == null) {
			return false;
		}
		if(another.hasParent(this)) {
			return false;
		}
		if(another.childActualTypes.contains(this) && this.parentActualTypes.contains(another)) {
			return false;
		}
		this.parentActualTypes.add(another);
		another.childActualTypes.add(this);
		return true;
	}
	public boolean registerChildActualType(ActualType another) {
		if(another == null) {
			return false;
		}
		if(another.hasChild(this)) {
			return false;
		}
		if(another.parentActualTypes.contains(this) && this.childActualTypes.contains(another)) {
			return false;
		}
		this.childActualTypes.add(another);
		another.parentActualTypes.add(this);
		return true;
	}
}