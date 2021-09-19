package org.mwage.mcPlugin.event_zhongqiu;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
public interface StaticData {
	public static ItemStack wolf_head = new ItemStack(Material.DRAGON_HEAD, 1);
	public static StaticDataHelper staticDataHelper = new StaticDataHelper();
	@SuppressWarnings("deprecation")
	static class StaticDataHelper {
		static {
			ItemMeta meta = StaticData.wolf_head.getItemMeta();
			meta.addEnchant(Enchantment.BINDING_CURSE, 1, false);
			meta.addEnchant(Enchantment.VANISHING_CURSE, 1, false);
			meta.setDisplayName(ChatColor.RED + "狼人之头");
			List<String> lore = new ArrayList<String>();
			lore.add("你狂躁化了！");
			lore.add("现在所有看到你的人都知道你是狼人了！");
			meta.setLore(lore);
			StaticData.wolf_head.setItemMeta(meta);
		}
	}
}