package org.mwage.mcPlugin.main.standard.plugin;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mwage.mcPlugin.main.standard.logger.Logger;
import org.mwage.mcPlugin.main.util.UtilCollection;
/**
 * 奶路标准插件类。
 * 没有特殊情况下，所有奶路插件类都应该直接继承这个类（而不是相互继承）。
 * 
 * @author GHYNG
 */
public abstract class MWPlugin extends JavaPlugin implements UtilCollection {
	private final Map<File, Logger> LOGGERS = new HashMap<File, Logger>();
	/**
	 * 收到确认的奶路插件列表。
	 * 奶路插件之间可以进行互动。
	 * 这个功能仍在建设。
	 */
	private final Map<String, MWPlugin> reconigizedMWPlugins = new HashMap<String, MWPlugin>();
	/**
	 * 通用设置。
	 * <p>
	 * 通用设置指的是不同的奶路插件可以共享的设置。
	 * 在调用设置时，将会优先调用上级奶路插件的设定。
	 */
	protected final Map<String, FileConfiguration> GENERAL_CONFIGURATIONS = new HashMap<String, FileConfiguration>();
	/**
	 * 这个列表中的文件将会被导出到插件文件夹中，
	 * 如果插件文件夹中不存在该文件。
	 * 已经存在的文件不会被覆写。
	 * <p>
	 * 注意：这些文件路径中应当已经包含插件文件夹的路径。
	 */
	protected final List<File> PACKAGE_FILES = new ArrayList<File>();
	/**
	 * 将指定的，插件包中的文件，写入插件文件夹中。
	 * <p>
	 * 事实上，这个方法是将{@link #PACKAGE_FILES}中的文件输出到插件文件夹中。
	 */
	protected void writePackageFiles() {
		File dataFolder = getDataFolder();
		if(!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
		for(File file : PACKAGE_FILES) {
			if(!file.exists()) {
				String fileName = file.getName();
				saveResource(fileName, false);
			}
		}
	}
	/**
	 * 注册需要被写入插件文件夹的文件。
	 * 这个方法需要在{@link #writePackageFiles()}之前调用。
	 * 
	 * @param paths
	 *            文件的路径。
	 */
	protected void registerPackageFiles(Object... paths) {
		String sep = File.separator;
		int length = paths.length;
		if(length < 1) {
			return;
		}
		String completePath = "";
		for(int i = 0; i < length - 1; i++) {
			completePath += paths[i] + sep;
		}
		completePath += paths[length - 1];
		File file = new File(completePath);
		PACKAGE_FILES.add(file);
	}
	/**
	 * 为这个插件注册一个新的事件监听器。
	 * 
	 * @param listener
	 *            事件监听器。
	 */
	public void registerListener(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, this);
	}
	public void reconigizeMWPlugin(String name) {
		PluginManager pluginManager = Bukkit.getPluginManager();
		Plugin plugin = pluginManager.getPlugin(name);
		if(plugin instanceof MWPlugin) {
			MWPlugin mp = (MWPlugin)plugin;
			reconigizedMWPlugins.put(name, mp);
		}
	}
	/**
	 * 获取上级奶路插件。
	 * 这些插件必须是正在激活状态中的。
	 * 这个方法只会返回直接上级插件，
	 * 间接上级则不会返回。
	 * <p>
	 * 这个方法每一次调用时，
	 * 都会遍历服务器正在运行的所有插件，
	 * 因此应当谨慎使用该方法。
	 * 
	 * @return 上级奶路插件列表。
	 */
	public List<MWPlugin> getParentMWPlugins() {
		return null; // XXX unfinished.
	}
	/**
	 * 产生该插件的插件文件夹。
	 * <p>
	 * 当前版本中，这个插件文件夹中应当自动包含如下子文件夹：
	 * <p>
	 * {@code
	 * configs_general
	 * configs_general_force
	 * logs
	 * }
	 */
	public void generatePluginFolder() {
		File folderPlugin = getDataFolder();
		List<String> subFolderNames = new ArrayList<String>();
		subFolderNames.add("configs_general");
		subFolderNames.add("configs_general_force");
		subFolderNames.add("logs");
		for(String subFolderName : subFolderNames) {
			File subFolder = new File(folderPlugin, subFolderName);
			subFolder.mkdirs();
		}
	}
	/**
	 * 返回{@code configs_general}文件夹。
	 * <p>
	 * 每一个奶路插件的插件文件夹下都应该有该文件夹。
	 * 这个文件夹中的属性同时是子插件的默认属性，
	 * 子插件可以重写这些属性。
	 * 
	 * @param mkdirs
	 *            如果{@code true}，且该文件夹尚不存在，则产生一个新文件夹。
	 * @return 文件夹。
	 */
	public File getConfigsGeneralFolder(boolean mkdirs) {
		File folder = new File(getDataFolder(), "configs_general");
		if(mkdirs) {
			folder.mkdirs();
		}
		return folder;
	}
	/**
	 * 返回{@code configs_general_force}文件夹。
	 * <p>
	 * 每一个奶路插件的插件文件夹下都应该有该文件夹。
	 * 这个文件夹中的属性同时也是子插件的强制属性，
	 * 子插件不得更改这些属性。
	 * 
	 * @param mkdirs
	 *            如果{@code true}，且该文件夹尚不存在，则产生一个新文件夹。
	 * @return 文件夹。
	 */
	public File getConfigsGeneralForceFolder(boolean mkdirs) {
		File folder = new File(getDataFolder(), "configs_general_force");
		if(mkdirs) {
			folder.mkdirs();
		}
		return folder;
	}
	/**
	 * 返回{@code logs}文件夹。
	 * <p>
	 * 这个文件夹用于存放log文件。
	 * 
	 * @param mkdirs
	 *            如果{@code true}，且该文件夹尚不存在，则产生一个新文件夹。
	 * @return 文件夹。
	 */
	public File getLogsFolder(boolean mkdirs) {
		File folder = new File(getDataFolder(), "logs");
		if(mkdirs) {
			folder.mkdirs();
		}
		return folder;
	}
	/**
	 * 将目前所有缓存中的记录写进文件。
	 */
	public final void logAll() {
		for(File folder : LOGGERS.keySet()) {
			Logger logger = LOGGERS.get(folder);
			logger.logFiles();
		}
	}
	/**
	 * 注册一个新的记录器。
	 * 
	 * @param paths
	 *            记录器的文件夹的路径。
	 * @return 被注册的记录器。
	 * @throws org.mwage.mcPlugin.main.standard.logger.LoggerRegisterFailedException
	 *             未能成功产生新的记录器。
	 */
	public Logger registerLogger(Object... paths) {
		Logger logger = new Logger(this, paths);
		if(LOGGERS.containsKey(logger.FOLDER)) {
			return LOGGERS.get(logger.FOLDER);
		}
		LOGGERS.put(logger.FOLDER, logger);
		return logger;
	}
}