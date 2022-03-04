package org.mwage.mcPlugin.main.util.io.mwml;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mwage.mcPlugin.main.util.MWCloneable;
import org.mwage.mcPlugin.main.util.methods.LogicUtil;
import org.mwage.mcPlugin.main.util.methods.StringUtil;
public abstract class NameSpacedConcept<C extends NameSpacedConcept<C>> implements Comparable<NameSpacedConcept<C>>, LogicUtil, StringUtil {
	public static record Signature(NamePath moduleNames, NamePath packageNames, String name) implements MWCloneable<Signature>, Comparable<Signature>, LogicUtil {
		public Signature {
			if(name == null) {
				throw new NullPointerException("Parameter: name cannot be null.");
			}
			boolean moduleNull = false, packageNull = false;
			if(moduleNames == null) {
				moduleNames = new NamePath("");
				moduleNull = true;
			}
			if(packageNames == null) {
				packageNames = new NamePath("");
				packageNull = true;
			}
			if(!moduleNull) {
				moduleNames = moduleNames.clone();
			}
			if(!packageNull) {
				packageNames = packageNames.clone();
			}
		}
		public Signature(List<String> moduleNames, List<String> packageNames, String name) {
			this(new NamePath(moduleNames), new NamePath(moduleNames), name);
		}
		public Signature(String moduleNames, String packageNames, String name) {
			this(new ArrayList<String>(), new ArrayList<String>(), name);
			if(moduleNames != null && (!moduleNames.equals(""))) {
				String[] moduleNameParts = moduleNames.split("\\.");
				for(String moduleNamePart : moduleNameParts) {
					this.moduleNames.path.add(moduleNamePart);
				}
			}
			if(packageNames != null && (!packageNames.equals(""))) {
				String[] packageNameParts = packageNames.split("\\.");
				for(String packageNamePart : packageNameParts) {
					this.packageNames.path.add(packageNamePart);
				}
			}
		}
		public static Signature parseSignature(String completeName) throws NullPointerException, IllegalArgumentException, RuntimeException {
			if(completeName == null) {
				throw new NullPointerException("Complete name is null.");
			}
			if(completeName.length() == 0) {
				throw new IllegalArgumentException("Complete name is empty.");
			}
			String[] parts = completeName.split("\\#");
			int length = parts.length;
			if(length == 0) {
				throw new IllegalArgumentException("Cannot parse complete name: \"" + completeName + "\"");
			}
			if(length == 1) {
				return new Signature("", "", completeName);
			}
			if(length == 2) {
				String packagePart = parts[0];
				String namePart = parts[1];
				return new Signature("", packagePart, namePart);
			}
			if(length == 3) {
				String modulePart = parts[0];
				String packagePart = parts[1];
				String namePart = parts[2];
				return new Signature(modulePart, packagePart, namePart);
			}
			throw new IllegalArgumentException("Unknown exception occured while parsing signature with string: \"" + completeName + "\", maybe wrong syntax?");
		}
		@Override
		public Signature clone() {
			return new Signature(moduleNames, packageNames, name);
		}
		@Override
		public int compareTo(Signature another) {
			if(another == null) {
				throw new NullPointerException("Null parameter.");
			}
			int compare = this.moduleNames.compareTo(another.moduleNames);
			if(compare != 0) {
				return compare;
			}
			compare = this.packageNames.compareTo(another.packageNames);
			if(compare != 0) {
				return compare;
			}
			return this.name.compareTo(another.name);
		}
		@Override
		public String toString() {
			if(moduleNames.isEmpty()) {
				if(packageNames.isEmpty()) {
					return name;
				}
				else {
					return "" + packageNames + ":" + name;
				}
			}
			else {
				return "" + moduleNames + ":" + packageNames + ":" + name;
			}
		}
	}
	public static record NamePath(List<String> path) implements MWCloneable<NamePath>, Comparable<NamePath> {
		public NamePath {
			if(path == null) {
				throw new NullPointerException("Null parameter.");
			}
			List<String> apath = new ArrayList<String>();
			for(String name : path) {
				apath.add(name);
			}
			path = apath;
		}
		public NamePath(String... pathNames) {
			this(new ArrayList<String>());
			for(String pathName : pathNames) {
				if(!ParserSystem.isLegalIdentifier(pathName)) {
					throw new IdentifierFormatException("Part of path names cannot be parsed: \"" + pathName + "\"");
				}
				path.add(pathName);
			}
		}
		public NamePath(String longName) {
			this(new ArrayList<String>());
			if(longName == null || longName.equals("")) {
				return;
			}
			String[] names = longName.split("\\.");
			for(String name : names) {
				if(!ParserSystem.isLegalIdentifier(name)) {
					throw new IdentifierFormatException("Part of path names cannot be parsed: \"" + name + "\"");
				}
				path.add(name);
			}
		}
		@Override
		public NamePath clone() {
			return new NamePath(path);
		}
		@Override
		public int compareTo(NamePath another) {
			if(another == null) {
				throw new NullPointerException("Null Parameter.");
			}
			int asize = this.path.size(), bsize = another.path.size();
			if(asize == 0 && bsize == 0) {
				return 0;
			}
			if(this.toString().equals("") && another.toString().equals("")) {
				return 0;
			}
			if(asize == 0 || this.toString().equals("")) {
				return -1;
			}
			if(bsize == 0 || another.toString().equals("")) {
				return 1;
			}
			for(int i = 0; i < asize && i < bsize; i++) {
				String aname = this.path.get(i), bname = another.path.get(i);
				int compare = aname.compareTo(bname);
				if(compare != 0) {
					return compare;
				}
				if(i == asize - 1 && i == bsize - 1) {
					return 0;
				}
				if(i == asize - 1) {
					return -1;
				}
				if(i == bsize - 1) {
					return 1;
				}
			}
			return 0;
		}
		@Override
		public String toString() {
			int size = path.size();
			if(size == 0) {
				return "";
			}
			String result = "";
			for(int i = 0; i < size - 1; i++) {
				result += path.get(i) + ".";
			}
			result += path.get(size - 1);
			return result;
		}
		public boolean isEmpty() {
			if(path == null) {
				return true;
			}
			if(path.size() == 0) {
				return true;
			}
			if(toString().equals("")) {
				return true;
			}
			return false;
		}
	}
	public Signature getSignature() {
		return signature.clone();
	}
	private String description;
	private final Signature signature;
	private Set<NameSpacedConcept<C>> parents = new HashSet<NameSpacedConcept<C>>();
	private Set<NameSpacedConcept<C>> children = new HashSet<NameSpacedConcept<C>>();
	public NameSpacedConcept(Signature signature) {
		this.signature = signature;
		initDefaultDescrption();
	}
	public NameSpacedConcept(NamePath moduleNames, NamePath packageNames, String name) {
		this(new Signature(moduleNames, packageNames, name));
	}
	public NameSpacedConcept(List<String> moduleNames, List<String> packageNames, String name) {
		this(new Signature(moduleNames, packageNames, name));
	}
	private void initDefaultDescrption() {
		List<String> lines = new ArrayList<String>();
		lines.add("There is no description for this type by author, below is a default description generated:");
		lines.add("Module: `" + signature.moduleNames + "`");
		lines.add("Package: `" + signature.packageNames + "`");
		lines.add("Type: `" + signature.name + "`");
		lines.add("End default description.");
		description = page(lines);
	}
	@Override
	public String toString() {
		return signature.toString();
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
		if(another instanceof NameSpacedConcept<?> nc) {
			return this.getSignature().equals(nc.getSignature());
		}
		return false;
	}
	@Override
	public int compareTo(NameSpacedConcept<C> another) {
		if(another == null) {
			throw new NullPointerException("NameSpacedConcept:another is null");
		}
		return this.signature.compareTo(another.signature);
	}
	public int getTier() {
		int tier = 0;
		for(NameSpacedConcept<C> parent : parents) {
			tier += parent.getTier() + 1;
		}
		return tier;
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
	public boolean hasParent(Signature anotherSignature) {
		if(anotherSignature == null) {
			return false;
		}
		if(this.getSignature().equals(anotherSignature)) {
			return true;
		}
		for(NameSpacedConcept<C> parent : parents) {
			if(parent.getSignature().equals(anotherSignature)) {
				return true;
			}
			if(parent.hasParent(anotherSignature)) {
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
	public boolean hasChild(Signature anotherSignature) {
		if(anotherSignature == null) {
			return false;
		}
		if(this.getSignature().equals(anotherSignature)) {
			return true;
		}
		for(NameSpacedConcept<C> child : children) {
			if(child.getSignature().equals(anotherSignature)) {
				return true;
			}
			if(child.hasChild(anotherSignature)) {
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}