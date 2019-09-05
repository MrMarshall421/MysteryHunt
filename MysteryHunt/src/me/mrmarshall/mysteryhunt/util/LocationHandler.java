package me.mrmarshall.mysteryhunt.util;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import dev.espi.protectionstones.PSRegion;
import me.mrmarshall.mysteryhunt.MysteryHunt;

public class LocationHandler {

	private Location getRandomLocation(String world, String treasure) {
		int spawnX = MysteryHunt.getInstance().fileManager.getFileConfiguratioon(MysteryHunt.getInstance().fileManager.getWorldsFile()).getInt(world + ".spawn.X");
		int spawnY = MysteryHunt.getInstance().fileManager.getFileConfiguratioon(MysteryHunt.getInstance().fileManager.getWorldsFile()).getInt(world + ".spawn.Y");
		int spawnZ = MysteryHunt.getInstance().fileManager.getFileConfiguratioon(MysteryHunt.getInstance().fileManager.getWorldsFile()).getInt(world + ".spawn.Z");
		
		double maxDistance = MysteryHunt.getInstance().getConfig().getDouble("treasures." + treasure + ".distances.max-distance");
		double minDistance = MysteryHunt.getInstance().getConfig().getDouble("treasures." + treasure + ".distances.min-distance");
		
		Random random = new Random();
		int randomLocX = (int) (spawnX + (random.nextInt((int) (maxDistance-minDistance)) + minDistance));
		int randomLocZ = (int) (spawnZ + (random.nextInt((int) (maxDistance-minDistance)) + minDistance));
		int randomLocY = spawnY + getHighestBlock(Bukkit.getWorld(world), randomLocX, randomLocZ);
		
		Location loc = new Location(Bukkit.getWorld(world), randomLocX, randomLocY, randomLocZ);
		return loc;
	}
	
	private int getHighestBlock(World world, int x, int z) {
		int highestBlock = 0;
		for(int i = 1; i < 256; i++) {
			Location testLoc = new Location(world, x, i, z);
			
			if(testLoc.getBlock().getType() != Material.AIR) {
				highestBlock = i;
			} else {
				break;
			}
		}
		return highestBlock;
	}
	
	private Location checkLocation(Location loc) {
		Block block = loc.clone().add(0, 1, 0).getBlock();
		Block underneath = loc.getBlock();
		
		if(block.getType() == Material.AIR) {
			if(underneath.getType() != Material.AIR) {
				if(loc.getY() != 0) {
					return loc;
				}
			} else {
				if(loc.getWorld().getName().equals("world_the_end")) {
					loc.add(0, 50, 0);
					loc.clone().subtract(0, 1, 0).getBlock().setType(Material.STONE);
					return loc;
				}
			}
		}
		return null;
	}
	
	public Location findTreasureLocation(String world, String treasure) {
		Location loc = null;
		boolean locationFound = false;
		
		while(locationFound == false) {
			Location randomLoc = getRandomLocation(world, treasure);
			
			if(checkLocation(randomLoc) != null) {
				PSRegion region = PSRegion.fromLocation(randomLoc);
				RegionContainer wgContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
				RegionManager wgRegions = wgContainer.get(BukkitAdapter.adapt(randomLoc.getWorld()));
				ApplicableRegionSet wgSet = wgRegions.getApplicableRegions(BlockVector3.at(randomLoc.getBlockX(), randomLoc.getBlockY(), randomLoc.getBlockZ()));
				
				if(region == null && wgSet.getRegions().isEmpty()) {
					locationFound = true;
					loc = randomLoc;
				} 
			}
		}
		return loc;
	}
}
