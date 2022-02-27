package org.mwage.mcPlugin.main.util.io.config2;
import java.io.File;
import java.util.List;
public interface MWConfigFileProject {
	String getName();
	List<MWConfigFileProject> getParents();
	List<MWConfigFileProject> getChildren();
	default ParserSystem getParserSystem() {
		return new ParserSystem(this); // TODO unfinished
	}
	File getDataFolder();
}