package org.mwage.mcPlugin.sign_fix;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin {
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player player) {
			Location location = player.getLocation();
			World world = location.getWorld();
			int max_height = world.getMaxHeight();
			int min_height = world.getMinHeight();
			Chunk chunk = location.getChunk();
			int count = 0;
			List<String> messages = new ArrayList<String>();
			for(int x = 0; x < 16; x++) {
				for(int y = min_height; y < max_height; y++) {
					for(int z = 0; z < 16; z++) {
						Block block = chunk.getBlock(x, y, z);
						BlockState blockState = block.getState();
						BlockData blockData = block.getBlockData().clone();
						if(blockState instanceof Sign sign) {
							List<String> lines = new ArrayList<String>();
							for(String line : sign.getLines()) {
								lines.add(line);
							}
							Material material = block.getType();
							DyeColor color = sign.getColor();
							block.setType(Material.AIR);
							block.setType(material);
							block.setBlockData(blockData);
							BlockState blockState1 = block.getState();
							if(blockState1 instanceof Sign sign1) {
								messages.add("告示牌：" + count);
								for(int i = 0; i < lines.size(); i++) {
									sign1.setLine(i, lines.get(i));
									messages.add("  " + lines.get(i));
								}
								messages.add("----------------------");
								sign1.setColor(color);
								sign1.update();
								count++;
							}
						}
					}
				}
			}
			messages.add("共调整了" + count + "个告示牌");
			String message = "";
			for(int i = 0; i < messages.size() - 1; i++) {
				message += messages.get(i) + "\n" + ChatColor.WHITE;
			}
			message += messages.get(messages.size() - 1);
			player.sendMessage(message);
			return true;
		}
		return false;
	}
}