package dev.mrmarshall.mysteryhunt.plugin;

import dev.mrmarshall.mysteryhunt.MysteryHunt;
import dev.mrmarshall.mysteryhunt.commands.HuntingToolCMD;
import dev.mrmarshall.mysteryhunt.events.BlockBreakListener;
import dev.mrmarshall.mysteryhunt.events.PlayerArmorStandManipulateListener;
import dev.mrmarshall.mysteryhunt.events.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PluginHandler {

	private void loadCommands() {
		HuntingToolCMD cHuntingToolCMD = new HuntingToolCMD();
		MysteryHunt.getInstance().getCommand("huntingtool").setExecutor(cHuntingToolCMD);
	}
	
	private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), MysteryHunt.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), MysteryHunt.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerArmorStandManipulateListener(), MysteryHunt.getInstance());
    }
	
	private void createFiles() {
		MysteryHunt.getInstance().saveDefaultConfig();
		
		File worlds = new File("plugins/MysteryHunt/worlds.yml");
		FileConfiguration worldsCfg = YamlConfiguration.loadConfiguration(worlds);
		
		if(!worlds.exists()) {
			try {
				worlds.createNewFile();
				worldsCfg.set("world.spawn.X", 0);
				worldsCfg.set("world.spawn.Y", 0);
				worldsCfg.set("world.spawn.Z", 0);
				worldsCfg.set("world.max-chests", 10);
				worldsCfg.set("world_the_end.spawn.X", 0);
				worldsCfg.set("world_the_end.spawn.Y", 0);
				worldsCfg.set("world_the_end.spawn.Z", 0);
				worldsCfg.set("world_the_end.max-chests", 5);
				worldsCfg.save(worlds);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public PluginHandler() {
		createFiles();
		loadCommands();
		registerEvents();
	}
}
