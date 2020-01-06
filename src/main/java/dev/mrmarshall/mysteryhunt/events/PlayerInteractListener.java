package dev.mrmarshall.mysteryhunt.events;

import dev.mrmarshall.mysteryhunt.MysteryHunt;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerInteractListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if(e.getHand() == EquipmentSlot.HAND) {
			try {
				if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', MysteryHunt.getInstance().getConfig().getString("huntingtool.name")))) {
					e.setCancelled(true);
					
					Location loc = MysteryHunt.getInstance().treasures.findNearestTreasure(p.getLocation());
					if(loc != null) {
                        int distance = (int) Math.round(p.getLocation().distance(loc));
                        p.sendMessage(MysteryHunt.getInstance().messages.prefix + "§b" + MysteryHunt.getInstance().treasures.treasures.get(loc) + " §fis currently §b" + distance + " §fblocks away!");
                    } else {
                        p.sendMessage(MysteryHunt.getInstance().messages.prefix + "§cThere is no treasure nearby!");
                    }
				}
			} catch(NullPointerException ex) {
			}
			
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(e.getClickedBlock().getType() == Material.CHEST) {
					Block chest = e.getClickedBlock();
					Location loc = chest.getLocation();
					String treasure = MysteryHunt.getInstance().treasures.treasures.get(loc);
					String openPermission = MysteryHunt.getInstance().getConfig().getString("treasures." + treasure + ".open-permission");
					
					if(MysteryHunt.getInstance().treasures.treasures.containsKey(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))) {
						if(p.hasPermission(openPermission)) {
                            //> open treasure
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    MysteryHunt.getInstance().treasures.loadRewards(e.getPlayer(), e.getPlayer().getOpenInventory().getTopInventory(), treasure);
                                    MysteryHunt.getInstance().schedulerManager.despawnOpenTreasure(loc);
                                    MysteryHunt.getInstance().treasures.treasures.remove(loc);
                                }
                            }.runTaskLater(MysteryHunt.getInstance(), 5);
                        } else {
                            e.setCancelled(true);
                            p.sendMessage(MysteryHunt.getInstance().messages.prefix + "§cYou are not allowed to open this treasure!");
                        }
					}
				}
			}
		}
	}
}
