package dev.mrmarshall.mysteryhunt.objects;

import dev.mrmarshall.mysteryhunt.MysteryHunt;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.HashMap;

public class Parachute {

	private HashMap<String, ArmorStand> parts = new HashMap<>();
	
	private ArmorStand createArmorStand(Location location, boolean visible, boolean mini) {
		ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
		
		armorStand.setBasePlate(false);
		armorStand.setArms(true);
		armorStand.setVisible(visible);
		armorStand.setInvulnerable(true);
		armorStand.setCanPickupItems(false);
		armorStand.setGravity(false);
		armorStand.setSmall(mini);
		
		return armorStand;
	}
	
	private double convertToRadians(double angle) {
		return Math.toRadians(angle);
	}
	
	public Parachute(String world, double x, double y, double z, String treasure) {
		Location location = new Location(Bukkit.getWorld(world), x, y, z);

		ArmorStand body = createArmorStand(location.clone().subtract(0, 1, 0), false, false);
		body.setHelmet(new ItemStack(Material.CHEST));

		ArmorStand string1 = createArmorStand(location, false, true);
		string1.setHelmet(new ItemStack(Material.END_ROD));
		string1.setHeadPose(new EulerAngle(0, convertToRadians(270.0), 0));

		ArmorStand string2 = createArmorStand(location, false, true);
		string2.setHelmet(new ItemStack(Material.END_ROD));
		string2.setHeadPose(new EulerAngle(0, convertToRadians(90.0), 0));

		ArmorStand string3 = createArmorStand(location.clone().add(0.45, 0.18, 0), false, true);
		string3.setHelmet(new ItemStack(Material.END_ROD));
		string3.setHeadPose(new EulerAngle(0, convertToRadians(270.0), convertToRadians(330.0)));

		ArmorStand string4 = createArmorStand(location.clone().add(-0.45, 0.18, 0), false, true);
		string4.setHelmet(new ItemStack(Material.END_ROD));
		string4.setHeadPose(new EulerAngle(0, convertToRadians(90.0), convertToRadians(30.0)));

		ArmorStand wool1 = createArmorStand(location.clone().add(0, 0.2, 0), false, false);
		wool1.setHelmet(new ItemStack(Material.PURPLE_CARPET));
		wool1.setHeadPose(new EulerAngle(0, 0, 0));

		ArmorStand wool2 = createArmorStand(location.clone().add(0.6, 0.1, 0), false, false);
		wool2.setHelmet(new ItemStack(Material.PURPLE_CARPET));
		wool2.setHeadPose(new EulerAngle(0, 0, convertToRadians(20.0)));

		ArmorStand wool3 = createArmorStand(location.clone().add(-0.6, 0.1, 0), false, false);
		wool3.setHelmet(new ItemStack(Material.PURPLE_CARPET));
		wool3.setHeadPose(new EulerAngle(0, 0, convertToRadians(340.0)));

		parts.put("body", body);
		parts.put("string1", string1);
		parts.put("string2", string2);
		parts.put("string3", string3);
		parts.put("string4", string4);
		parts.put("wool1", wool1);
		parts.put("wool2", wool2);
		parts.put("wool3", wool3);

		location.getChunk().load();
		MysteryHunt.getInstance().locationHandler.getSpawningParachute().add(location);
		fly(treasure, location);
	}

	public void removeParachute(Location spawnLoc) {
		for (String part : parts.keySet()) {
			parts.get(part).remove();
		}

		parts.clear();
		MysteryHunt.getInstance().locationHandler.getSpawningParachute().remove(spawnLoc);
	}

	public void fly(String treasure, Location spawnLoc) {
		new BukkitRunnable() {
			public void run() {
				for (String part : parts.keySet()) {
					parts.get(part).teleport(parts.get(part).getLocation().subtract(0, 0.1, 0));

					if (parts.get(part).getLocation().getBlock().getType() != Material.AIR) {
						Material blockUnderneath = Material.getMaterial(MysteryHunt.getInstance().getConfig().getString("treasures." + treasure + ".block-underneath"));

						parts.get("body").getWorld().getBlockAt(parts.get("body").getLocation().clone().add(0, 2, 0)).setType(Material.CHEST);
						parts.get("body").getWorld().getBlockAt(parts.get("body").getLocation().clone().add(0, 1, 0)).setType(blockUnderneath);
						MysteryHunt.getInstance().treasures.treasures.put(parts.get("body").getWorld().getBlockAt(parts.get("body").getLocation().clone().add(0, 2, 0)).getLocation(), treasure);

						removeParachute(spawnLoc);
						cancel();
						break;
					}
				}
				try {
					Location particleLoc = parts.get("body").getLocation().clone().add(0, 2, 0);
					Particle.DustOptions dustOptions = new Particle.DustOptions(Color.PURPLE, 1);
					particleLoc.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 3, 0.1, 0.1, 0.1, 5, dustOptions);
				} catch (NullPointerException ex) {
				}
			}
		}.runTaskTimer(MysteryHunt.getInstance(), 0l, 1l);
	}
}
