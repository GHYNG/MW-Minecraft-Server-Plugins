package org.mwage.mcPlugin.main.standard.logger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mwage.mcPlugin.main.standard.plugin.MWPlugin;
/**
 * 记录器。
 * <p>
 * 每一个记录器对应一个logs文件夹的子文件夹。
 * 在该文件夹中，文件以日期（{@code yyyy-MM-dd.log}）命名，
 * 因此每一天都会有一个新的.log文件。
 * <p>
 * 记录器会先将每一行记录存在缓存中，
 * 然后在需要的时候统一写入文件里。
 * 
 * @author GHYNG
 */
public class Logger {
	/**
	 * 这个记录器所属的插件。
	 */
	public final MWPlugin PLUGIN;
	/**
	 * 这个记录器负责的文件夹。
	 */
	public final File FOLDER;
	/**
	 * 记录的具体行的列表。
	 */
	private List<LogLine<?>> logLines = new ArrayList<LogLine<?>>();
	/**
	 * 根据给定的路径作为储存文件夹，
	 * 产生一个新的记录器。
	 * 
	 * @param plugin
	 *            调用该功能的插件。
	 * @param paths
	 *            指定的路径。
	 *            这个路径将在{@code [plugin-folder]\logs}中。
	 */
	public Logger(MWPlugin plugin, Object... paths) {
		String sep = File.separator;
		int length = paths.length;
		if(length < 1) {
			throw new IllegalArgumentException("Wrong number of paths. (number of paths should be >= 1)");
		}
		PLUGIN = plugin;
		plugin.generatePluginFolder();
		String completePath = "";
		for(int i = 0; i < length - 1; i++) {
			completePath += paths[i] + sep;
		}
		completePath += paths[length - 1];
		FOLDER = new File(plugin.getLogsFolder(true), completePath);
		FOLDER.mkdirs();
		if(!FOLDER.exists()) {
			throw new LoggerRegisterFailedException("Cannot create logger's needed folder");
		}
	}
	/**
	 * 添加一行新的记录。
	 * 
	 * @param logLine
	 *            新的记录。
	 */
	public void log(LogLine<?> logLine) {
		logLines.add(logLine);
	}
	/**
	 * 添加一行新的记录。
	 * 
	 * @param object
	 *            新的记录。
	 */
	public void logObject(Object object) {
		logLines.add(new LogLine<>(object));
	}
	/**
	 * 将缓存中的记录写入文件，然后清空缓存。
	 */
	public void logFiles() {
		PLUGIN.generatePluginFolder();
		Collections.sort(logLines);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, List<LogLine<?>>> files = new HashMap<String, List<LogLine<?>>>();
		for(LogLine<?> logLine : logLines) {
			String fileName = formatter.format(logLine.DATE) + ".log";
			if(!files.containsKey(fileName)) {
				files.put(fileName, new ArrayList<LogLine<?>>());
			}
			files.get(fileName).add(logLine);
		}
		for(String fileName : files.keySet()) {
			File file = new File(PLUGIN.getLogsFolder(true), fileName);
			if(!file.exists()) {
				try {
					file.createNewFile();
					BufferedWriter bw = new BufferedWriter(new FileWriter(file));
					List<LogLine<?>> lines = files.get(fileName);
					for(LogLine<?> line : lines) {
						bw.write(line.toString());
						bw.newLine();
						logLines.remove(line);
					}
					bw.close();
				}
				catch(IOException e) {
				}
			}
		}
		logLines.clear();
	}
}