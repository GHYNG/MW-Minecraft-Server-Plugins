package org.mwage.mcPlugin.main.util.io.mwml;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
public interface CoreModule extends Module {
	@Override
	@Deprecated
	default boolean registerParent(Module another) {
		throw new UnsupportedOperationException("Core Module cannot have any parent module! It is root already!");
	}
	@Override
	@Deprecated
	default File getDataFolder() {
		throw new UnsupportedOperationException("Core Module is imaginary, which does not have actual folder structure.");
	}
}
class CoreModuleImplementer implements CoreModule {
	private Set<Module> parents = new HashSet<Module>();
	private Set<Module> children = new HashSet<Module>();
	@Override
	public String getName() {
		return "";
	}
	@Override
	public Set<Module> getParents() {
		return parents;
	}
	@Override
	public Set<Module> getChildren() {
		return children;
	}
	@Override
	public ParserSystem getParserSystem() {
		return null; // TODO unfinished
	}
}
class CoreParserSystem extends ParserSystem {
	public CoreParserSystem(Module localModule) {
		super(localModule);
		// TODO 自动生成的构造函数存根
	}
}