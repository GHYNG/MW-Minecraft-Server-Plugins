package org.mwage.mcPlugin.main.util.io.mwml;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
public interface Module {
	String getName();
	Set<Module> getParents();
	Set<Module> getChildren();
	ParserSystem getParserSystem();
	File getDataFolder();
	default boolean hasParent(Module another) {
		if(another == null) {
			return false;
		}
		if(this == another || this.equals(another)) {
			return true;
		}
		if(getParents().contains(another)) {
			return true;
		}
		for(Module parent : getParents()) {
			if(parent.hasParent(another)) {
				return true;
			}
		}
		return false;
	}
	default boolean hasChild(Module another) {
		if(another == null) {
			return false;
		}
		if(this == another || this.equals(another)) {
			return true;
		}
		if(getChildren().contains(another)) {
			return true;
		}
		for(Module child : getChildren()) {
			if(child.hasChild(another)) {
				return true;
			}
		}
		return false;
	}
	default boolean registerParent(Module another) {
		if(another == null) {
			return false;
		}
		if(this == another || this.equals(another)) {
			return false;
		}
		if(another.hasParent(this) || this.hasChild(another)) {
			return false;
		}
		if(this.getParents().contains(another) && another.getChildren().contains(this)) {
			return false;
		}
		this.getParents().add(another);
		another.getChildren().add(this);
		return true;
	}
	default boolean registerChild(Module another) {
		if(another == null) {
			return false;
		}
		if(this == another || this.equals(another)) {
			return false;
		}
		if(another.hasChild(this) || this.hasParent(another)) {
			return false;
		}
		if(this.getChildren().contains(another) && another.getParents().contains(this)) {
			return false;
		}
		this.getChildren().add(another);
		another.getParents().add(this);
		return true;
	}
	default Set<Module> getAllParents() {
		Set<Module> allParents = new HashSet<Module>();
		for(Module parent : getParents()) {
			allParents.addAll(parent.getAllParents());
		}
		return allParents;
	}
	default Set<Module> getAllChildren() {
		Set<Module> allChildren = new HashSet<Module>();
		for(Module child : getChildren()) {
			allChildren.addAll(child.getAllChildren());
		}
		return allChildren;
	}
}