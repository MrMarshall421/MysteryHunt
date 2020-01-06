package dev.mrmarshall.mysteryhunt.events;

import dev.mrmarshall.mysteryhunt.MysteryHunt;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class PlayerArmorStandManipulateListener implements Listener {

    @EventHandler
    public void onArmorStandManipulate(PlayerArmorStandManipulateEvent e) {
        Player p = e.getPlayer();

        for (Location loc : MysteryHunt.getInstance().locationHandler.getSpawningParachute()) {
            loc.setY(p.getLocation().getY());
            int distance = (int) Math.round(p.getLocation().distance(loc));

            if (distance <= 15.0) {
                e.setCancelled(true);
                break;
            }
        }
    }
}
