package net.serubin.warp;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.tulonsae.mc.util.Log;

public class FlatFile {
    private File warpsFile = null;
    private FileConfiguration warps = null;
    private EscapePlug plugin = null;

    /**
     * Initiates FlatFile.
     * 
     * @param plugin
     *            EscapePlug
     */
    public FlatFile(EscapePlug plugin, Logger log) {
        this.plugin = plugin;
        warpsFile = new File(plugin.getDataFolder(), "warps.yml");
        if (!warpsFile.exists()) {
            log.info("Creating 'warps.yml'...");
            try {
                warpsFile.createNewFile();
            } catch (IOException e) {
                log.severe("'warps.yml' could not be created!");
                e.printStackTrace();
            }
        }
        warps = new YamlConfiguration();

        loadYamls();

    }

    /**
     * loads yaml files
     */
    private void loadYamls() {
        try {
            warps.load(warpsFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Saves yaml files
     */
    private void saveYamls() {
        try {
            warps.save(warpsFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean addWarp(String name, Location pos, Player user) {
        name = name.toLowerCase();
        warps.set("", name);
        warps.set(name + ".position.x", pos.getBlockX());
        warps.set(name + ".position.y", pos.getBlockY());
        warps.set(name + ".position.z", pos.getBlockZ());
        warps.set(name + ".created.user", user.getName());
        warps.set(name + ".created.date", "date");
        saveYamls();
        return true;
    }
}
