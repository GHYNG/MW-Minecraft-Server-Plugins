package org.mwage.mcPlugin.main.standard.plugin;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mwage.mcPlugin.main.util.UtilCollection;
/**
 * ��·��׼����ࡣ
 * û����������£�������·����඼Ӧ��ֱ�Ӽ̳�����ࣨ�������໥�̳У���
 * 
 * @author GHYNG
 */
public abstract class MWPlugin extends JavaPlugin implements UtilCollection {
	private final Map<String, MWPlugin> reconigizedMWPlugins = new HashMap<String, MWPlugin>();
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
}