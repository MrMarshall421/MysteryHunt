package dev.mrmarshall.mysteryhunt.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager {

    public File getWorldsFile() {
        return new File("plugins/MysteryHunt/worlds.yml");
    }

    public FileConfiguration getFileConfiguratioon(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
	
}
