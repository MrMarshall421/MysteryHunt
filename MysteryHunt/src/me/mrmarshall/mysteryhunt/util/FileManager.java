package me.mrmarshall.mysteryhunt.util;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager {

	public File getWorldsFile() {
		return new File("plugins/MysteryHunt/worlds.yml");
	}
	
	public FileConfiguration getFileConfiguratioon(File file) {
		return YamlConfiguration.loadConfiguration(file);
	}
	
}
