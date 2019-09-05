package me.mrmarshall.mysteryhunt.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.clip.placeholderapi.PlaceholderAPI;
import me.mrmarshall.mysteryhunt.MysteryHunt;

public class Treasures {

	public HashMap<Location, String> treasures = new HashMap<Location, String>();
	private ArrayList<Integer> usedRewards;
	
	@SuppressWarnings("deprecation")
	public void loadRewards(Player p, Inventory inv, String treasure) {
		usedRewards = new ArrayList<Integer>();
		
		String rewardsAmountString = MysteryHunt.getInstance().getConfig().getString("treasures." + treasure + ".rewards-amount");
		int rewardsAmount = 0;
		
		if(rewardsAmountString.contains("-")) {
			String[] rewardsAmountStringArray = rewardsAmountString.split("-");
			int rewardsAmountMin = Integer.parseInt(rewardsAmountStringArray[0]);
			int rewardsAmountMax = Integer.parseInt(rewardsAmountStringArray[1]);
			
			Random random = new Random();
			rewardsAmount = random.nextInt(rewardsAmountMax - rewardsAmountMin) + rewardsAmountMin;
		} else {
			rewardsAmount = Integer.parseInt(rewardsAmountString);
		}
		
		int availableRewardsCount = getAvailableRewardsCount(treasure);
		
		for(int i = 0; i < rewardsAmount; i++) {
			int selectedReward = -1;
			
			while(selectedReward == -1) {
				selectedReward = getRandomSelection(availableRewardsCount, treasure);
			}
			
			String type = MysteryHunt.getInstance().getConfig().getString("treasures." + treasure + ".rewards." + selectedReward + ".type");
			String executeAmountString = MysteryHunt.getInstance().getConfig().getString("treasures." + treasure + ".rewards." + selectedReward + ".amount");
			int executeAmount = 0;
			
			if(executeAmountString.contains("-")) {
				String[] executeAmountStringArray = executeAmountString.split("-");
				int executeAmountMin = Integer.parseInt(executeAmountStringArray[0]);
				int executeAmountMax = Integer.parseInt(executeAmountStringArray[1]);
				Random rand = new Random();
				executeAmount = rand.nextInt(executeAmountMax - executeAmountMin) + executeAmountMin;
			} else {
				executeAmount = Integer.parseInt(executeAmountString);
			}
			
			if(type.equalsIgnoreCase("command")) {
				String command = ChatColor.translateAlternateColorCodes('&', MysteryHunt.getInstance().getConfig().getString("treasures." + treasure + ".rewards." + selectedReward + ".value")).replaceAll("%player%", p.getName()).toString().replaceAll("%p_x%", p.getLocation().getX() + "").replaceAll("%p_y%", p.getLocation().getY() + "").replaceAll("%p_z%", p.getLocation().getZ() + "");
				
				if(MysteryHunt.getInstance().placeholderAPI) {
					command = PlaceholderAPI.setPlaceholders(p, command);
				}

				for(int i1 = 0; i1 < executeAmount; i1++) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
				}
			} else if(type.equalsIgnoreCase("item")) {
				Material material = Material.getMaterial(MysteryHunt.getInstance().getConfig().getString("treasures." + treasure + ".rewards." + selectedReward + ".value"));
				String name = ChatColor.translateAlternateColorCodes('&', MysteryHunt.getInstance().getConfig().getString("treasures." + treasure + ".rewards." + selectedReward + ".name"));
				List<String> lore = new ArrayList<String>();

				for(String loreEntry : MysteryHunt.getInstance().getConfig().getStringList("treasures." + treasure + ".rewards." + selectedReward + ".lore")) {
					lore.add(ChatColor.translateAlternateColorCodes('&', loreEntry));
				}
				
				ItemStack item = new ItemStack(material, executeAmount);
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.setDisplayName(name);
				itemMeta.setLore(lore);
				
				for(String enchantsEntry : MysteryHunt.getInstance().getConfig().getStringList("treasures." + treasure + ".rewards." + selectedReward + ".enchantments")) {
					String[] enchantmentsArray = enchantsEntry.split(" ");
					String enchantment = enchantmentsArray[0];
					int level = Integer.parseInt(enchantmentsArray[1]);
					itemMeta.addEnchant(Enchantment.getByName(enchantment), level, false);
				}
				
				item.setItemMeta(itemMeta);
				
				int randomSlot = 0;
				while(inv.getItem(randomSlot) != null) {
					Random ran = new Random();
					randomSlot = ran.nextInt(inv.getSize());
				}
				
				inv.setItem(randomSlot, item);
			}
		}
	}
	
	public int getAvailableRewardsCount(String treasure) {
		for(int i = 0; i < 999; i++) {
			if(MysteryHunt.getInstance().getConfig().get("treasures." + treasure + ".rewards." + i) == null) {
				return i;
			}
		}
		return 0;
	}
	
	public int getRandomSelection(int rewardsAmount, String treasure) {
		Random random = new Random();
		int selectedReward = random.nextInt(rewardsAmount);
		
		if(!MysteryHunt.getInstance().getConfig().getBoolean("treasures." + treasure + ".same-rewards")) {
			if(!usedRewards.contains(selectedReward)) {
				if(MysteryHunt.getInstance().getConfig().get("treasures." + treasure + ".rewards." + selectedReward) != null) {
					usedRewards.add(selectedReward);
					return selectedReward;
				} 
			}
		} else {
			if(MysteryHunt.getInstance().getConfig().get("treasures." + treasure + ".rewards." + selectedReward) != null) {
				return selectedReward;
			} 
		}
		return -1;
	}
	
	public Location findNearestTreasure(Location playerLocation) {
		Location nearestTreasureLocation = null;
		
		for(Location loc : MysteryHunt.getInstance().treasures.treasures.keySet()) {
			if(loc.getWorld().getName() == playerLocation.getWorld().getName()) {
				if(nearestTreasureLocation != null) {
					if(playerLocation.distance(loc) < playerLocation.distance(nearestTreasureLocation)) {
						nearestTreasureLocation = loc;
					}
				} else {
					nearestTreasureLocation = loc;
				}
			}
		}
		
		return nearestTreasureLocation;
	}
	
	public void destroyAllTreasures() {
		for(Location loc : MysteryHunt.getInstance().treasures.treasures.keySet()) {
			loc.getBlock().setType(Material.AIR);
			loc.clone().subtract(0, 1, 0).getBlock().setType(Material.AIR);
		}
	}
}
