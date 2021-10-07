package org.mwage.mcPlugin.main.standard.plugin;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
import org.mwage.mcPlugin.main.standard.logger.Logger;
import org.mwage.mcPlugin.main.standard.plugin.config.MWPluginConfigurationManager;
import org.mwage.mcPlugin.main.util.UtilCollection;
/**
 * 奶路标准插件类。
 * 没有特殊情况下，所有奶路插件类都应该直接继承这个类（而不是相互继承）。
 * 
 * @author GHYNG
 */
@SuppressWarnings("deprecation")
@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
public abstract class MWPlugin extends JavaPlugin implements UtilCollection {
	/**
	 * 获取当前插件的奶路API版本。
	 * 
	 * @return API版本。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public abstract int getAPIVersion();
	/**
	 * 用于记录log文件的各个记录器。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	private final Map<File, Logger> loggers = new HashMap<File, Logger>();
	/**
	 * 收到确认的奶路插件列表。
	 * 奶路插件之间可以进行互动。
	 * 这个功能仍在建设。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	private final Map<String, MWPlugin> reconigizedMWPlugins = new HashMap<String, MWPlugin>();
	/**
	 * 这个插件的所有上级奶路插件。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected final List<MWPlugin> parentPlugins = new ArrayList<MWPlugin>();
	/**
	 * 这个插件的所有下级奶路插件。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected final List<MWPlugin> childPlugins = new ArrayList<MWPlugin>();
	/**
	 * 这个列表中的文件将会被导出到插件文件夹中，
	 * 如果插件文件夹中不存在该文件。
	 * 已经存在的文件不会被覆写。
	 * <p>
	 * 注意：这些文件路径中应当已经包含插件文件夹的路径。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected final List<File> packageFiles = new ArrayList<File>();
	/**
	 * 本奶路插件的配置文件系统。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected final MWPluginConfigurationManager config = new MWPluginConfigurationManager(this);
	/**
	 * 将指定的，插件包中的文件，写入插件文件夹中。
	 * <p>
	 * 事实上，这个方法是将{@link #packageFiles}中的文件输出到插件文件夹中。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected void writePackageFiles() {
		File dataFolder = getDataFolder();
		if(!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
		for(File file : packageFiles) {
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
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
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
		packageFiles.add(file);
	}
	/**
	 * 为这个插件注册一个新的事件监听器。
	 * 相当于{@code Bukkit.getPluginManager().registerEvents(listener, this);}。
	 * 
	 * @param listener
	 *            事件监听器。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 0))
	public void registerListener(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, this);
	}
	/**
	 * 辨认在同一个服务器中运行的所有奶路插件。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public void reconigizeMWPlugins() {
		PluginManager pluginManager = Bukkit.getPluginManager();
		Plugin[] plugins = pluginManager.getPlugins();
		for(Plugin plugin : plugins) {
			if(plugin instanceof MWPlugin mp) {
				String name = mp.getName();
				reconigizedMWPlugins.put(name, mp);
			}
		}
		PluginDescriptionFile pdf = getDescription();
		pdf.getDepend().forEach((name) -> {
			MWPlugin parent = reconigizedMWPlugins.get(name);
			if(parent != null) {
				parentPlugins.add(parent);
				parent.childPlugins.add(this);
			}
		});
		pdf.getSoftDepend().forEach((name) -> {
			MWPlugin parent = reconigizedMWPlugins.get(name);
			if(parent != null) {
				parentPlugins.add(parent);
			}
		});
	}
	/**
	 * 获取上级奶路插件。
	 * 这些插件必须是正在激活状态中的。
	 * 这个方法只会返回直接上级插件，
	 * 间接上级则不会返回。
	 * 这个列表是根据{@code plugin.yml}中指定的顺序排列的。
	 * <p>
	 * 为了保护列表的稳定，
	 * 这个插件返回的是一个插件列表的副本。
	 * 对返回的列表进行改动不会改变实际列表。
	 * 
	 * @return 上级奶路插件列表的复制。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public List<MWPlugin> getParentMWPlugins() {
		return copyList(parentPlugins);
	}
	/**
	 * 获取下级奶路插件。
	 * 这些插件必须是正在激活状态中的。
	 * 这个方法只会返回直接下级插件，
	 * 间接下级则不会返回。
	 * 这个列表是根据{@code plugin.yml}中指定的顺序排列的。
	 * <p>
	 * 为了保护列表的稳定，
	 * 这个插件返回的是一个插件列表的副本。
	 * 对返回的列表进行改动不会改变实际列表。
	 * 
	 * @return 下级奶路插件列表的复制。
	 */
	public List<MWPlugin> getChildMWPlugins() {
		return copyList(childPlugins);
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
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
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
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
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
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
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
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
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
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public final void logAll() {
		for(File folder : loggers.keySet()) {
			Logger logger = loggers.get(folder);
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
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public Logger registerLogger(Object... paths) {
		Logger logger = new Logger(this, paths);
		if(loggers.containsKey(logger.FOLDER)) {
			return loggers.get(logger.FOLDER);
		}
		loggers.put(logger.FOLDER, logger);
		return logger;
	}
	/**
	 * 当插件被激活时的程序。
	 * <p>
	 * 它将产生一个插件文件夹系统，
	 * 辨认其它奶路文件夹，
	 * 然后启动子插件指定的程序（{@link #onMWEnable()}）。
	 * 
	 * @deprecated 子插件应该调用{@link #onMWEnable()}作为替代。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	@Override
	@Deprecated
	public void onEnable() {
		generatePluginFolder();
		reconigizeMWPlugins();
		onMWEnable();
	}
	/**
	 * 奶路插件用于替代{@link #onEnable()}的方法。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public void onMWEnable() {
		// does nothing
	}
	/**
	 * 当插件停止激活时的程序。
	 * <p>
	 * 它将记录所有需要的log。
	 * 
	 * @deprecated 子插件应该调用{@link #onMWDisable()}作为替代。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	@Override
	@Deprecated
	public void onDisable() {
		logAll();
		onMWDisable();
	}
	/**
	 * 奶路插件用于替代{@link #onDisable()}的方法。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public void onMWDisable() {
		// does nothinge
	}
	/**
	 * 将这个插件启动。
	 * 相当于使用{@code setEnabled(true);}。
	 * <p>
	 * 谨慎使用这个功能！
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public void turnOn() {
		setEnabled(true);
	}
	/**
	 * 将这个插件关闭。
	 * 相当于使用{@code setEnabled(false);}。
	 * <p>
	 * 谨慎使用这个功能！
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public void turnOff() {
		setEnabled(false);
	}
	/**
	 * 获得本插件的配置管理类。
	 * 
	 * @return 管理配置的类的对象。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public MWPluginConfigurationManager getMWConfig() {
		return config;
	}
}