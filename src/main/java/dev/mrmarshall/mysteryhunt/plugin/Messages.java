package dev.mrmarshall.mysteryhunt.plugin;

import dev.mrmarshall.mysteryhunt.MysteryHunt;
import org.bukkit.ChatColor;

public class Messages {

	public String prefix = ChatColor.translateAlternateColorCodes('&', MysteryHunt.getInstance().getConfig().getString("messages.prefix"));
	
}
