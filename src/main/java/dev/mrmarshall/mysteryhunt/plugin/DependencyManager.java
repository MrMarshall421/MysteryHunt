package dev.mrmarshall.mysteryhunt.plugin;

import dev.mrmarshall.mysteryhunt.MysteryHunt;
import org.bukkit.Bukkit;

public class DependencyManager {

    private boolean placeholderApi;
    private boolean protectionStones;
    private boolean worldGuard;
    private boolean worldEdit;

    public DependencyManager() {
        protectionStones = false;
        placeholderApi = false;
        worldGuard = false;
        worldEdit = false;
    }

    public void loadDependencies() {
        //> PlaceholderAPI
        if (checkDependency("me.clip.placeholderapi.PlaceholderAPI")) {
            placeholderApi = true;
            Bukkit.getConsoleSender().sendMessage(MysteryHunt.getInstance().messages.prefix + "§aSuccessfully hooked into PlaceholderAPI!");
        }

        //> ProtectionStones
        if (checkDependency("dev.espi.protectionstones.PSRegion")) {
            protectionStones = true;
            Bukkit.getConsoleSender().sendMessage(MysteryHunt.getInstance().messages.prefix + "§aSuccessfully hooked into ProtectionStones!");
        }

        //> WorldGuard
        if (checkDependency("com.sk89q.worldguard.WorldGuard")) {
            worldGuard = true;
            Bukkit.getConsoleSender().sendMessage(MysteryHunt.getInstance().messages.prefix + "§aSuccessfully hooked into WorldGuard!");
        }

        //> WorldEdit
        if (checkDependency("com.sk89q.worldedit.bukkit.BukkitAdapter")) {
            worldEdit = true;
            Bukkit.getConsoleSender().sendMessage(MysteryHunt.getInstance().messages.prefix + "§aSuccessfully hooked into WorldEdit!");
        }
    }

    private boolean checkDependency(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            return false;
        }

        return true;
    }

    public boolean getPlaceholderApi() {
        return placeholderApi;
    }

    public boolean getProtectionStones() {
        return protectionStones;
    }

    public boolean getWorldGuard() {
        return worldGuard;
    }

    public boolean getWorldEdit() {
        return worldEdit;
    }
}