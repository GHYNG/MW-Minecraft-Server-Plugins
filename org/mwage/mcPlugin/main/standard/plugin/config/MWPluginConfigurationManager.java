package org.mwage.mcPlugin.main.standard.plugin.config;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
public class MWPluginConfigurationManager implements Main_GeneralMethods {
	protected final Set<String> keywords = new HashSet<String>();
	protected final MWPlugin plugin;
	protected MemoryConfiguration localConfigGeneral = new MemoryConfiguration();
	protected MemoryConfiguration localConfigGeneralForce = new MemoryConfiguration();
	protected MemoryConfiguration overallConfigGeneral = new MemoryConfiguration();
	protected MemoryConfiguration overallConfigGeneralForce = new MemoryConfiguration();
	protected MemoryConfiguration actualConfig = new MemoryConfiguration();
	{
		keywords.add("this");
	}
	public MWPluginConfigurationManager(MWPlugin plugin) {
		this.plugin = plugin;
	}
	public void reload() {
		readFilesIntoConfig();
	}
	private void readFilesIntoConfig() {
		plugin.generatePluginFolder();
		File folderConfigGeneral = plugin.getConfigsGeneralFolder(true);
		File folderConfigGeneralForce = plugin.getConfigsGeneralForceFolder(true);
		localConfigGeneral = parseConfigFromFile(folderConfigGeneral);
		localConfigGeneralForce = parseConfigFromFile(folderConfigGeneralForce);
		List<MWPlugin> parentPlugins = plugin.getParentMWPlugins();
		int numberParents = parentPlugins.size();
		for(int i = 0; i < numberParents; i++) {
			MWPlugin parentPlugin = parentPlugins.get(i);
			MWPluginConfigurationManager parentConfig = parentPlugin.getMWConfig();
			MemoryConfiguration parentOverallConfigGeneral = parentConfig.overallConfigGeneral;
			combineConfigs(overallConfigGeneral, parentOverallConfigGeneral);
		}
		for(int i = numberParents - 1; i >= 0; i--) {
			MWPlugin parentPlugin = parentPlugins.get(i);
			MWPluginConfigurationManager parentConfig = parentPlugin.getMWConfig();
			MemoryConfiguration parentOverallConfigGeneralForce = parentConfig.overallConfigGeneralForce;
			combineConfigs(overallConfigGeneralForce, parentOverallConfigGeneralForce);
		}
		combineConfigs(actualConfig, overallConfigGeneral);
		combineConfigs(actualConfig, overallConfigGeneralForce);
	}
	protected MemoryConfiguration parseConfigFromFile(File file) {
		/*
		 * Notice: the order of parsing file matters.
		 * The config system uses LIOS method (Last in, only served)
		 * whereas the last read config will be the active config.
		 */
		String name = file.getName();
		/*
		 * Logic 1: while this file is a file (instead of folder)
		 */
		if(file.isFile()) {
			if(name.endsWith("yml")) {
				FileConfiguration config = new YamlConfiguration();
				try {
					config.load(file);
				}
				catch(IOException | InvalidConfigurationException e) {
					e.printStackTrace();
					return null;
				}
				return config;
			}
			else return null;
		}
		/*
		 * Logic 2: while this file is a folder
		 */
		else {
			MemoryConfiguration config = new MemoryConfiguration();
			List<File> subFiles = arrayToList(file.listFiles());
			/*
			 * Loop 1:
			 * look for folders named "<key>.#<orderTag>", but not "this.#<orderTag>"
			 */
			loop1 : for(File subFile : subFiles) {
				if(subFile.isFile()) {
					continue loop1;
				}
				String subName = subFile.getName();
				String[] parts = subName.split(".");
				int numParts = parts.length;
				if(numParts != 2) {
					continue loop1;
				}
				String key = parts[0], orderTag = parts[1];
				if(and(goodIdentifier(key, keywords), orderTag.startsWith("#"), goodIdentifier(orderTag.substring(1)))) {
					MemoryConfiguration subConfig = parseConfigFromFile(subFile);
					configAddSubConfig(config, key, subConfig);
				}
			}
			/*
			 * Loop 2:
			 * look for folders named "<key>", but not "this"
			 */
			loop2 : for(File subFile : subFiles) {
				if(subFile.isFile()) {
					continue loop2;
				}
				String subName = subFile.getName();
				if(goodIdentifier(subName, keywords)) {
					MemoryConfiguration subConfig = parseConfigFromFile(subFile);
					configAddSubConfig(config, subName, subConfig);
				}
			}
			/*
			 * Loop 3:
			 * look for files named "<key>.#<orderTag>.yml", but not "this.#<orderTag>.yml"
			 */
			loop3 : for(File subFile : subFiles) {
				if(!subFile.isFile()) {
					continue loop3;
				}
				String subName = subFile.getName();
				String[] parts = subName.split(".");
				int numParts = parts.length;
				if(numParts != 3) {
					continue loop3;
				}
				String key = parts[0], orderTag = parts[1], extendName = parts[2];
				/*@formatter:off*/
				if(
					and(
						goodIdentifier(key, keywords), 
						orderTag.startsWith("#"),
						goodIdentifier(orderTag.substring(1)),
						extendName.equals("yml")
					)
				)
				/*@formatter:on*/
				{
					MemoryConfiguration subConfig = parseConfigFromFile(subFile);
					configAddSubConfig(config, key, subConfig);
				}
			}
			/*
			 * Loop 4:
			 * look for files named "<key>.yml", but not "this.yml"
			 */
			loop4 : for(File subFile : subFiles) {
				if(!subFile.isFile()) {
					continue loop4;
				}
				String subName = subFile.getName();
				String[] parts = subName.split(".");
				int numParts = parts.length;
				if(numParts != 2) {
					continue loop4;
				}
				String key = parts[0], extendName = parts[1];
				if(and(goodIdentifier(key, keywords), extendName.equals("yml"))) {
					MemoryConfiguration subConfig = parseConfigFromFile(subFile);
					configAddSubConfig(config, key, subConfig);
				}
			}
			/*
			 * Loop 5:
			 * look for folders named "this.#orderTag"
			 */
			loop5 : for(File subFile : subFiles) {
				if(subFile.isFile()) {
					continue loop5;
				}
				String subName = subFile.getName();
				String[] parts = subName.split(".");
				int numParts = parts.length;
				if(numParts != 2) {
					continue loop5;
				}
				String key = parts[0], orderTag = parts[1];
				if(and(key.equals("this"), orderTag.startsWith("#"), goodIdentifier(orderTag.substring(1)))) {
					MemoryConfiguration subConfig = parseConfigFromFile(subFile);
					combineConfigs(config, subConfig);
				}
			}
			/*
			 * Loop 6:
			 * look for folder named "this"
			 */
			loop6 : for(File subFile : subFiles) {
				if(subFile.isFile()) {
					continue loop6;
				}
				String subName = subFile.getName();
				if(subName.equals("this")) {
					MemoryConfiguration subConfig = parseConfigFromFile(subFile);
					combineConfigs(config, subConfig);
				}
			}
			/*
			 * Loop 7:
			 * look for files named "this.#<orderTag>.yml"
			 */
			loop7 : for(File subFile : subFiles) {
				if(!subFile.isFile()) {
					continue loop7;
				}
				String subName = subFile.getName();
				String[] parts = subName.split(".");
				int numParts = parts.length;
				if(numParts != 3) {
					continue loop7;
				}
				String key = parts[0], orderTag = parts[1], extendName = parts[2];
				/*@formatter:off*/
				if(
					and(
						key.equals("this"), 
						orderTag.startsWith("#"), 
						goodIdentifier(orderTag.substring(1)),
						extendName.equals("yml")
					)
				) 
				/*@formatter:on*/
				{
					MemoryConfiguration subConfig = parseConfigFromFile(subFile);
					combineConfigs(config, subConfig);
				}
			}
			/*
			 * Loop 8:
			 * look for file named "this"
			 */
			loop8 : for(File subFile : subFiles) {
				if(!subFile.isFile()) {
					continue loop8;
				}
				String subName = subFile.getName();
				if(subName.equals("this.yml")) {
					MemoryConfiguration subConfig = parseConfigFromFile(subFile);
					combineConfigs(config, subConfig);
				}
			}
			return config;
		}
	}
	private void combineConfigs(MemoryConfiguration base, MemoryConfiguration adder) {
		Map<String, Object> values = adder.getValues(true);
		for(String key : values.keySet()) {
			Object value = values.get(key);
			if(value == null) {
				base.set(key, null);
			}
			else if(value instanceof ConfigurationSection section) {
				base.createSection(key, section.getValues(true));
			}
			else {
				base.set(key, value);
			}
		}
	}
	private void configAddSubConfig(MemoryConfiguration config, String key, MemoryConfiguration subConfig) {
		Map<String, Object> values = subConfig.getValues(true);
		config.createSection(key, values);
	}
}