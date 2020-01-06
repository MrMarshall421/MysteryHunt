package dev.mrmarshall.mysteryhunt;

import dev.mrmarshall.mysteryhunt.objects.Treasures;
import dev.mrmarshall.mysteryhunt.plugin.DependencyManager;
import dev.mrmarshall.mysteryhunt.plugin.Messages;
import dev.mrmarshall.mysteryhunt.plugin.PluginHandler;
import dev.mrmarshall.mysteryhunt.util.FileManager;
import dev.mrmarshall.mysteryhunt.util.LocationHandler;
import dev.mrmarshall.mysteryhunt.util.SchedulerManager;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class MysteryHunt extends JavaPlugin {

    private static MysteryHunt instance;
    public SchedulerManager schedulerManager;
    public LocationHandler locationHandler;
    public FileManager fileManager;
    public Treasures treasures;
    public HashMap<Location, String> notDestroyable = new HashMap<>();
    public Messages messages;
    public DependencyManager dependencyManager;

    @Override
    public void onEnable() {
        instance = this;
        schedulerManager = new SchedulerManager();
        locationHandler = new LocationHandler();
        fileManager = new FileManager();
        treasures = new Treasures();
        messages = new Messages();
        dependencyManager = new DependencyManager();

        new PluginHandler();
        dependencyManager.loadDependencies();
        schedulerManager.loadTreasures();
    }
	
	@Override
	public void onDisable() {
		treasures.destroyAllTreasures();
		instance = null;
	}
	
	public static MysteryHunt getInstance() {
		return instance;
	}
	
}
