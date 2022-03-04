package org.mwage.mcPlugin.main.util.io.mwml;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
public interface MWMLModule {
	static MWMLCoreModule CORE_MODULE = new MWMLCoreModuleImplementer();
	String getName();
	Set<MWMLModule> getParents();
	Set<MWMLModule> getChildren();
	Set<ValueType> getLocalValueTypes();
	Set<ExpressionType> getLocalExpressionTypes();
	ParserSystem getParserSystem();
	File getDataFolder();
	Logger getLogger();
	default int getTier() {
		int tier = 0;
		for(MWMLModule parent : getParents()) {
			tier += parent.getTier() + 1;
		}
		return tier;
	}
	default boolean hasParent(MWMLModule another) {
		if(another == null) {
			return false;
		}
		if(this == another || this.equals(another)) {
			return true;
		}
		if(getParents().contains(another)) {
			return true;
		}
		for(MWMLModule parent : getParents()) {
			if(parent.hasParent(another)) {
				return true;
			}
		}
		return false;
	}
	default boolean hasChild(MWMLModule another) {
		if(another == null) {
			return false;
		}
		if(this == another || this.equals(another)) {
			return true;
		}
		if(getChildren().contains(another)) {
			return true;
		}
		for(MWMLModule child : getChildren()) {
			if(child.hasChild(another)) {
				return true;
			}
		}
		return false;
	}
	default boolean registerParent(MWMLModule another) {
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
	default boolean registerChild(MWMLModule another) {
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
	default Set<MWMLModule> getAllParents() {
		Set<MWMLModule> allParents = new HashSet<MWMLModule>();
		for(MWMLModule parent : getParents()) {
			allParents.addAll(parent.getAllParents());
		}
		return allParents;
	}
	default Set<MWMLModule> getAllChildren() {
		Set<MWMLModule> allChildren = new HashSet<MWMLModule>();
		for(MWMLModule child : getChildren()) {
			allChildren.addAll(child.getAllChildren());
		}
		return allChildren;
	}
	default Set<MWMLModule> getAllModules() {
		Set<MWMLModule> modules = new HashSet<MWMLModule>();
		modules.add(this);
		modules.addAll(getAllParents());
		modules.addAll(getAllChildren());
		return modules;
	}
	default MWMLModule findModule(String name) {
		if(name == null) {
			return null;
		}
		for(MWMLModule module : getAllModules()) {
			if(name.equals(module.getName())) {
				return module;
			}
		}
		return null;
	}
	default Set<ValueType> getAllValueTypes() {
		Set<ValueType> allValueTypes = new HashSet<ValueType>();
		allValueTypes.addAll(getLocalValueTypes());
		for(MWMLModule parent : getAllParents()) {
			allValueTypes.addAll(parent.getLocalValueTypes());
		}
		for(MWMLModule child : getAllChildren()) {
			allValueTypes.addAll(child.getLocalValueTypes());
		}
		return allValueTypes;
	}
	default Set<ExpressionType> getAllExpressionTypes() {
		Set<ExpressionType> allExpressionType = new HashSet<ExpressionType>();
		allExpressionType.addAll(getLocalExpressionTypes());
		for(MWMLModule parent : getAllParents()) {
			allExpressionType.addAll(parent.getLocalExpressionTypes());
		}
		for(MWMLModule child : getAllChildren()) {
			allExpressionType.addAll(child.getLocalExpressionTypes());
		}
		return allExpressionType;
	}
	default boolean registerValueType(ValueType valueType) {
		if(valueType == null) {
			return false;
		}
		if(getLocalValueTypes().contains(valueType)) {
			return false;
		}
		if(!valueType.getSignature().moduleNames().toString().equals(getName())) {
			return false;
		}
		return getLocalValueTypes().add(valueType);
	}
	default boolean registerExpressionType(ExpressionType expressionType) {
		if(expressionType == null) {
			return false;
		}
		if(getLocalExpressionTypes().contains(expressionType)) {
			return false;
		}
		if(!expressionType.getSignature().moduleNames().toString().equals(getName())) {
			return false;
		}
		return getLocalExpressionTypes().add(expressionType);
	}
	default ValueType findValueType(Signature signature) {
		if(signature == null) {
			return null;
		}
		for(ValueType valueType : getAllValueTypes()) {
			if(valueType.getSignature().equals(signature)) {
				return valueType;
			}
		}
		return null;
	}
	default ExpressionType findExpressionType(Signature signature) {
		if(signature == null) {
			return null;
		}
		for(ExpressionType expressionType : getAllExpressionTypes()) {
			if(expressionType.getSignature().equals(signature)) {
				return expressionType;
			}
		}
		return null;
	}
}