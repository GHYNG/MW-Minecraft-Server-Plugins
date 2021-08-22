package org.mwage.mcPlugin.main.standard.plugin.config;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
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
public class MWPluginConfiguration implements Main_GeneralMethods {
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
	public MWPluginConfiguration(MWPlugin plugin) {
		this.plugin = plugin;
	}
	public void reload() {
		plugin.generatePluginFolder();
		File folderConfigGeneral = plugin.getConfigsGeneralFolder(true);
		File folderConfigGeneralForce = plugin.getConfigsGeneralForceFolder(true);
		localConfigGeneral = parseConfigFromFile(folderConfigGeneral);
		localConfigGeneralForce = parseConfigFromFile(folderConfigGeneralForce);
		List<MWPlugin> parentPlugins = plugin.getParentMWPlugins();
		int numberParents = parentPlugins.size();
		for(int i = 0; i < numberParents; i++) {
			MWPlugin parentPlugin = parentPlugins.get(i);
			MWPluginConfiguration parentConfig = parentPlugin.getMWConfig();
			MemoryConfiguration parentOverallConfigGeneral = parentConfig.overallConfigGeneral;
			combineConfigs(overallConfigGeneral, parentOverallConfigGeneral);
		}
		for(int i = numberParents - 1; i >= 0; i--) {
			MWPlugin parentPlugin = parentPlugins.get(i);
			MWPluginConfiguration parentConfig = parentPlugin.getMWConfig();
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
				if(and(goodIdentifier(key, keywords), goodIdentifier(orderTag, keywords))) {
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
						goodIdentifier(orderTag, keywords), 
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
			return config;
			/*
			 * Loop 5
			 * look for folders named "this.#orderTag"
			 */
			
		}
	}
	protected MemoryConfiguration parseConfig(File file) {
		String name = file.getName();
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
		else {
			FileConfiguration localGeneralConfig = new YamlConfiguration();
			List<File> subFiles = arrayToList(file.listFiles());
			Collections.sort(subFiles);
			/*
			 * Loop 1: look for "this.yml" files in folder "file"
			 */
			loop1 : for(File subFile : subFiles) {
				String subFileName = subFile.getName();
				if(subFileName.equals("this.yml")) {
					configParseWithFile(localGeneralConfig, subFile);
					break loop1;
				}
			}
			/*
			 * Loop 2: look for "this.*.yml" files in folder "file"
			 * "*" must be good java identifier (but reserved names ok)
			 */
			loop2 : for(File subFile : subFiles) {
				String subFileName = subFile.getName();
				/*@formatter:off*/
				if (
					and (
						not (
							subFileName.equals("this.yml")
						), 
						subFileName.startsWith("this."), 
						subFileName.endsWith(".yml")
					)
				)
				/*@formatter:on*/
				{
					String[] parts = subFileName.split(".");
					for(String part : parts) {
						if(!goodIdentifier(part)) {
							continue loop2;
						}
					}
					configParseWithFile(localGeneralConfig, subFile);
				}
			}
			/*
			 * Loop 3: look for all "*.yml" files in folder "file"
			 * "*" must be good java identifier (but reserved names ok)
			 */
			loop3 : for(File subFile : subFiles) {
				String subFileName = subFile.getName();
				if(!subFileName.endsWith(".yml")) {
					continue loop3;
				}
				String key = subFileName.substring(0, subFileName.length() - 4);
				if(!goodIdentifier(key)) {
					continue loop3;
				}
				else if(key.equals("this")) {
					continue loop3;
				}
				FileConfiguration subConfig = new YamlConfiguration();
				try {
					subConfig.load(subFile);
				}
				catch(IOException | InvalidConfigurationException e) {
					e.printStackTrace();
					continue loop3;
				}
				localGeneralConfig.createSection(key, subConfig.getValues(true));
			}
		}
		return null;
	}
	private void configParseWithFile(MemoryConfiguration config, File file) {
		MemoryConfiguration thisConfig = parseConfig(file);
		if(thisConfig == null) {
			return;
		}
		combineConfigs(config, thisConfig);
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