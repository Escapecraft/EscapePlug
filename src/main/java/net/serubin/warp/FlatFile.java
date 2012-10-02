package net.serubin.warp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.tulonsae.mc.util.Log;

public class FlatFile {
    private EscapePlug plugin;

    // File
    private String fileName = "warps.csv";
    private FileWriter fStream = null;

    // Hashmaps
    private Map<String, WarpData> warps = new HashMap<String, WarpData>();

    /**
     * Initiates FlatFile.
     * 
     * @param plugin
     *            EscapePlug
     */
    public FlatFile(EscapePlug plugin, Logger log) {
        this.plugin = plugin;
        File warpsFile = new File(plugin.getDataFolder(), fileName);
        if (!warpsFile.exists()) {
            log.info("Creating '" + fileName + "'...");
            try {
                warpsFile.createNewFile();
            } catch (IOException e) {
                log.severe("'" + fileName + "' could not be created!");
                e.printStackTrace();
            }
        }
        loadData();
    }

    private void loadData() {
        try {
            fStream = new FileWriter(fileName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean addWarp(String name, Location loc, Player user) {
        Timestamp date = null;
        if (!warps.containsKey(name)) {
            warps.put(name, new WarpData(name, loc, date, user.getName()));
            // TODO add to file and reload

        } else {
            // TODO already exsists error
        }
        return false;
    }

    public boolean rmWarp(String name) {
        if (warps.containsKey(name)) {
            warps.remove(name);
        } else {
            // TODO not exsistant error
        }
        return false;
    }

    public boolean writeFile() {
        for (int i = 0; warps.size() < i; i++) {
            // TODO make string, write to file thingy

        }
        return true;
    }

    public static Timestamp getDateTime() {
        java.sql.Timestamp date;
        java.util.Date today = new java.util.Date();
        date = new java.sql.Timestamp(today.getTime());
        return date;
    }
}
