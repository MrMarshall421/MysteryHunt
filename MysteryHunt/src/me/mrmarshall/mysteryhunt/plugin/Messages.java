package me.mrmarshall.mysteryhunt.plugin;

import org.bukkit.ChatColor;

import me.mrmarshall.mysteryhunt.MysteryHunt;

public class Messages {

	public String prefix = ChatColor.translateAlternateColorCodes('&', MysteryHunt.getInstance().getConfig().getString("messages.prefix"));
	
}
