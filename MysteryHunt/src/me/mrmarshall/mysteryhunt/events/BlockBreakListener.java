package me.mrmarshall.mysteryhunt.events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.mrmarshall.mysteryhunt.MysteryHunt;

public class BlockBreakListener implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		
		for(Location loc : MysteryHunt.getInstance().notDestroyable.keySet()) {
			if(e.getBlock().getLocation().equals(loc) || e.getBlock().getLocation().equals(loc.clone().subtract(0, 1, 0))) {
				e.setCancelled(true);
			}
		}
	}
}
