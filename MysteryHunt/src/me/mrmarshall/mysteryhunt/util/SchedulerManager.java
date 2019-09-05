package me.mrmarshall.mysteryhunt.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.mrmarshall.mysteryhunt.MysteryHunt;
import me.mrmarshall.mysteryhunt.objects.Parachute;

public class SchedulerManager {

	public void loadTreasures() {
		ConfigurationSection treasuresSection = MysteryHunt.getInstance().getConfig().getConfigurationSection("treasures");
		for(String treasure : treasuresSection.getKeys(false)) {
			startTreasureSpawning(treasure);
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	public void startTreasureSpawning(String treasure) {
		int minPlayers = MysteryHunt.getInstance().getConfig().getInt("treasures." + treasure + ".min-players");
		int spawnDelay = MysteryHunt.getInstance().getConfig().getInt("treasures." + treasure + ".spawn-delay");
		List<String> worldsList = (List<String>) MysteryHunt.getInstance().getConfig().get("treasures." + treasure + ".spawn-worlds");
		boolean fallFromSky = MysteryHunt.getInstance().getConfig().getBoolean("treasures." + treasure + ".fall-from-sky");
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MysteryHunt.getInstance(), new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < worldsList.size(); i++) {
					int playersInWorld = 0;
					for(Player all : Bukkit.getOnlinePlayers()) {
						if(all.getWorld().getName().equals(worldsList.get(i))) {
							playersInWorld = playersInWorld + 1;
						}
					}
					
					if(playersInWorld >= minPlayers) {
						String broadcast = ChatColor.translateAlternateColorCodes('&', MysteryHunt.getInstance().getConfig().getString("treasures." + treasure + ".broadcast")).replaceAll("%world%", worldsList.get(i).toString());
						int maxChests = MysteryHunt.getInstance().fileManager.getFileConfiguratioon(MysteryHunt.getInstance().fileManager.getWorldsFile()).getInt(worldsList.get(i) + ".max-chests");
						
						if(MysteryHunt.getInstance().placeholderAPI) {
							broadcast = PlaceholderAPI.setPlaceholders(null, broadcast);
						}
						
						int currentChests = 0;
						for(Location loc : MysteryHunt.getInstance().treasures.treasures.keySet()) {
							if(loc.getWorld().getName().equals(worldsList.get(i))) {
								currentChests++;
							}
						}
						
						if(currentChests != maxChests) {
							Location loc = MysteryHunt.getInstance().locationHandler.findTreasureLocation(worldsList.get(i), treasure).add(0, 2, 0);
							
							if(fallFromSky) {
								new Parachute(worldsList.get(i), loc.getX(), (loc.getY()+50), loc.getZ(), treasure);
							} else {
								Material blockUnderneath = Material.getMaterial(MysteryHunt.getInstance().getConfig().getString("treasures." + treasure + ".block-underneath"));
								
								Bukkit.getWorld(worldsList.get(i)).getBlockAt(loc).setType(Material.CHEST);
								Bukkit.getWorld(worldsList.get(i)).getBlockAt(loc.clone().subtract(0, 1, 0)).setType(blockUnderneath);
								
								MysteryHunt.getInstance().treasures.treasures.put(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), treasure);
							}
							
							MysteryHunt.getInstance().notDestroyable.put(loc, treasure);
							despawnNaturalTreasure(loc, treasure);
							Bukkit.broadcastMessage(MysteryHunt.getInstance().messages.prefix + broadcast);
						}
					}
				}
			}
		}, 20*spawnDelay, 20*spawnDelay);
	}
	
	public void despawnOpenTreasure(Location loc, String treasure) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(MysteryHunt.getInstance(), new Runnable() {
			@Override
			public void run() {
				loc.getBlock().setType(Material.AIR);
				loc.clone().subtract(0, 1, 0).getBlock().setType(Material.AIR);
				loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, 5);
				MysteryHunt.getInstance().notDestroyable.remove(loc);
			}
		}, 20*300);
	}
	
	public void despawnNaturalTreasure(Location loc, String treasure) {
		int despawnDelay = MysteryHunt.getInstance().getConfig().getInt("treasures." + treasure + ".despawn-delay");
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MysteryHunt.getInstance(), new Runnable() {
			@Override
			public void run() {
				if(MysteryHunt.getInstance().treasures.treasures.containsKey(loc)) {
					loc.getBlock().setType(Material.AIR);
					loc.clone().subtract(0, 1, 0).getBlock().setType(Material.AIR);
					loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, 5);
					MysteryHunt.getInstance().treasures.treasures.remove(loc);
					MysteryHunt.getInstance().notDestroyable.remove(loc);
				}
			}
		}, 20*despawnDelay);
	}
}
