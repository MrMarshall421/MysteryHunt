package dev.mrmarshall.mysteryhunt.objects;

import dev.mrmarshall.mysteryhunt.MysteryHunt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HuntingTool {
	
	public static ItemStack getHuntingTool() {
        Material material = Material.getMaterial(MysteryHunt.getInstance().getConfig().getString("huntingtool.material"));
        String name = ChatColor.translateAlternateColorCodes('&', MysteryHunt.getInstance().getConfig().getString("huntingtool.name"));
        List<String> lore = new ArrayList<>();

        for (int i = 0; i < MysteryHunt.getInstance().getConfig().getStringList("huntingtool.lore").size(); i++) {
            lore.add(ChatColor.translateAlternateColorCodes('&', MysteryHunt.getInstance().getConfig().getStringList("huntingtool.lore").get(i)));
        }

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
		return item;
	}
}
