package me.mrmarshall.mysteryhunt;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrmarshall.mysteryhunt.objects.Treasures;
import me.mrmarshall.mysteryhunt.plugin.Messages;
import me.mrmarshall.mysteryhunt.plugin.PluginHandler;
import me.mrmarshall.mysteryhunt.util.FileManager;
import me.mrmarshall.mysteryhunt.util.LocationHandler;
import me.mrmarshall.mysteryhunt.util.SchedulerManager;

public class MysteryHunt extends JavaPlugin {

	//> global classes
	private static MysteryHunt instance;
	public boolean placeholderAPI;
	public SchedulerManager schedulerManager;
	public LocationHandler locationHandler;
	public FileManager fileManager;
	public Treasures treasures;
	public HashMap<Location, String> notDestroyable = new HashMap<Location, String>();
	public Messages messages;
	
	@Override
	public void onEnable() {
		instance = this;
		schedulerManager = new SchedulerManager();
		locationHandler = new LocationHandler();
		fileManager = new FileManager();
		treasures = new Treasures();
		messages = new Messages();
		
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			placeholderAPI = true;
			System.out.println(messages.prefix + "Successfully hooked into PlaceholderAPI");
		} else {
			placeholderAPI = false;
		}
		
		new PluginHandler();
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
