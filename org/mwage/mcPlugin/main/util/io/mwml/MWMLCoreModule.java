package org.mwage.mcPlugin.main.util.io.mwml;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
public interface MWMLCoreModule extends MWMLModule {
	@Override
	@Deprecated
	default boolean registerParent(MWMLModule another) {
		throw new UnsupportedOperationException("Core Module cannot have any parent module! It is root already!");
	}
	@Override
	@Deprecated
	default File getDataFolder() {
		throw new UnsupportedOperationException("Core Module is imaginary, which does not have actual folder structure.");
	}
	@Override
	@Deprecated
	default Logger getLogger() {
		throw new UnsupportedOperationException("Core Module does not have a logger, because it is imaginary.");
	}
}
class MWMLCoreModuleImplementer implements MWMLCoreModule {
	private Set<MWMLModule> parents = new HashSet<MWMLModule>();
	private Set<MWMLModule> children = new HashSet<MWMLModule>();
	private Set<ValueType> localValueTypes = new HashSet<ValueType>();
	private Set<ExpressionType> localExpressionTypes = new HashSet<ExpressionType>();
	@Override
	public String getName() {
		return "";
	}
	@Override
	public Set<MWMLModule> getParents() {
		return parents;
	}
	@Override
	public Set<MWMLModule> getChildren() {
		return children;
	}
	@Override
	public ParserSystem getParserSystem() {
		return null; // TODO unfinished
	}
	@Override
	public Set<ValueType> getLocalValueTypes() {
		return localValueTypes;
	}
	@Override
	public Set<ExpressionType> getLocalExpressionTypes() {
		return localExpressionTypes;
	}
}
class CoreParserSystem extends ParserSystem {
	public CoreParserSystem(MWMLModule localModule) {
		super(localModule);
		// TODO 自动生成的构造函数存根
	}
}