package org.mwage.mcPlugin.main.util.io.mwml;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mwage.mcPlugin.main.util.methods.LogicUtil;
public abstract class NameSpacedConcept<C extends NameSpacedConcept<C>> implements Comparable<NameSpacedConcept<C>>, LogicUtil {
	public static record Signature(String moduleName, List<String> packageNames, String name) {
		public Signature(String moduleName, String name, String... packageNames) {
			this(moduleName, new ArrayList<String>(), name);
			for(String packageName : packageNames) {
				this.packageNames.add(packageName);
			}
		}
	}
	public Signature getSignature() {
		List<String> packageNames = new ArrayList<String>();
		for(String packageName : this.packageNames) {
			packageNames.add(packageName);
		}
		return new Signature(moduleName, packageNames, name);
	}
	public final Module markupLanguageSpace;
	protected final String moduleName;
	protected final List<String> packageNames = new ArrayList<String>();
	protected final String name;
	private Set<NameSpacedConcept<C>> parents = new HashSet<NameSpacedConcept<C>>();
	private Set<NameSpacedConcept<C>> children = new HashSet<NameSpacedConcept<C>>();
	public NameSpacedConcept(Module module, String moduleName, List<String> packageNames, String name) {
		this.markupLanguageSpace = module;
		if(!ParserSystem.isLegalIdentifier(moduleName) && (!moduleName.equals(""))) {
			throw new IdentifierFormatException("Given moduleName: \"" + moduleName + "\" is not legal for an identifier");
		}
		if(!ParserSystem.isLegalIdentifier(name)) {
			throw new IdentifierFormatException("Given name: \"" + name + "\" is not legal for an identifier");
		}
		this.moduleName = moduleName;
		this.name = name;
		for(String packageName : packageNames) {
			if(!ParserSystem.isLegalIdentifier(packageName)) {
				throw new IdentifierFormatException("Given packageName: \"" + name + "\" is not legal for an identifier");
			}
			this.packageNames.add(packageName);
		}
	}
	public NameSpacedConcept(Module markupLanguageSpace, Signature signature) {
		this(markupLanguageSpace, signature.moduleName, signature.packageNames, signature.name);
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
		if(moduleName.equals("") || moduleName.equals(markupLanguageSpace.getName())) {
			if(packageNames.size() == 0) {
				return name;
			}
			return longPackageName + ":";
		}
		return moduleName + ":" + longPackageName + ":" + name;
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
		if(another instanceof ValueType at) {
			return toString().equals(at.toString());
		}
		return false;
	}
	@Override
	public int compareTo(NameSpacedConcept<C> another) {
		if(another == null) {
			throw new NullPointerException("NameSpacedConcept:another is null");
		}
		if(moduleName.equals("")) {
			if(another.moduleName.equals("")) {
				int packageLength = packageNames.size();
				int anotherPackageLength = another.packageNames.size();
				if(packageLength == 0) {
					if(anotherPackageLength == 0) {
						return name.compareTo(another.name);
					}
					else {
						return -1;
					}
				}
				else if(anotherPackageLength == 0) {
					return 1;
				}
				else {
					for(int i = 0; i < packageLength && i < anotherPackageLength; i++) {
						int compare = packageNames.get(i).compareTo(another.packageNames.get(i));
						if(compare != 0) {
							return compare;
						}
						else {
							if(i == packageLength - 1 && i == anotherPackageLength - 1) {
								return 0;
							}
							else {
								if(i == packageLength - 1) {
									return -1;
								}
								if(i == anotherPackageLength - 1) {
									return 1;
								}
								continue;
							}
						}
					}
					return 0;
				}
			}
		}
		else if(another.moduleName.equals("")) {
			return 1;
		}
		return 0;
	}
	public boolean hasParent(NameSpacedConcept<C> another) {
		if(another == null) {
			return false;
		}
		if(this.equals(another)) {
			return true;
		}
		if(parents.contains(another)) {
			return true;
		}
		for(NameSpacedConcept<C> parent : parents) {
			if(parent.hasParent(another)) {
				return true;
			}
		}
		return false;
	}
	public boolean hasChild(NameSpacedConcept<C> another) {
		if(another == null) {
			return false;
		}
		if(this.equals(another)) {
			return true;
		}
		if(children.contains(another)) {
			return true;
		}
		for(NameSpacedConcept<C> child : children) {
			if(child.hasChild(another)) {
				return true;
			}
		}
		return false;
	}
	public boolean registerParent(NameSpacedConcept<C> another) {
		if(another == null) {
			return false;
		}
		if(another.hasParent(this) || this.hasChild(another)) {
			return false;
		}
		if(this.parents.contains(another) && another.children.contains(this)) {
			return false;
		}
		this.parents.add(another);
		another.children.add(this);
		return true;
	}
	public boolean registerChild(NameSpacedConcept<C> another) {
		if(another == null) {
			return false;
		}
		if(another.hasChild(this) || this.hasParent(another)) {
			return false;
		}
		if(this.children.contains(another) && another.parents.contains(this)) {
			return false;
		}
		this.children.add(another);
		another.parents.add(this);
		return true;
	}
}