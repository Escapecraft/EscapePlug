package net.serubin.warp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.tulonsae.mc.util.Log;

public class FlatFile {
    private EscapePlug plugin;

    // File
    private String fileName = "warps.csv";
    private FileWriter fStreamOut = null;
    private FileReader fStreamIn = null;
    File warpsFile = null;

    private int name = 0;
    private int world = 1;
    private int coords = 2;
    private int user = 3;
    private int date = 4;
    // Hashmaps
    private Map<String, WarpData> warps = new HashMap<String, WarpData>();

    private Logger log;
    private WarpComponent warpClass;

    /**
     * Initiates FlatFile.
     * 
     * @param plugin
     *            EscapePlug
     * @param log
     *            Logger
     * @param warpClass
     *            warpComponent
     */
    public FlatFile(EscapePlug plugin, Logger log, WarpComponent warpClass) {
        this.plugin = plugin;
        this.warpClass = warpClass;
        this.log = log;
        warpsFile = new File(plugin.getDataFolder(), fileName);
        if (!warpsFile.exists()) {
            log.info("Creating '" + fileName + "'...");
            try {
                boolean test = false;
                if (test) {
                    warpsFile.createNewFile();
                }
            } catch (IOException e) {
                log.warning("'" + fileName + "' could not be created!");
                e.printStackTrace();
            }
        }
        try {
            fStreamOut = new FileWriter(warpsFile);
            fStreamIn = new FileReader(warpsFile);
        } catch (IOException e) {
            log.warning("[Warps] There was an error enabling Warps");
            e.printStackTrace();
        }

    }

    /**
     * Adds new data warps hashmap, then pushes hashmap to file
     * 
     * @param name
     *            String, warp name
     * @param loc
     *            Location, current player location
     * @param user
     *            String, users name
     * @return true if no errors : false if error
     */
    public boolean addWarp(String name, Location loc, Player user) {
        Timestamp date = null;
        if (!warps.containsKey(name)) {
            warps.put(name, new WarpData(name, loc, getDate(), user.getName()));
            if (!pushData()) {
                warpClass
                        .printPlayer(ChatColor.RED
                                + "There was an error creating this warp. Check console.");
                return false;
            }
            return true;
        } else {
            warpClass.printPlayer(ChatColor.RED + "Warp already exists!");
            return false;
        }
    }

    /**
     * Removes warp from hashmap then pushes hashmap to file
     * 
     * @param name
     *            String, warp name
     * @return true if no errors : false if errors
     */
    public boolean rmWarp(String name) {
        if (warps.containsKey(name)) {
            warps.remove(name);
            if (!pushData()) {
                warpClass
                        .printPlayer(ChatColor.RED
                                + "There was an error removing this warp. Check console.");
                return false;
            }
            return true;
        } else {
            warpClass.printPlayer(ChatColor.RED + "This warp does not exsist!");
            return true;
        }
    }

    /**
     * Gets WarpData for warp
     * 
     * @param name
     *            String of warp
     * @return warp data
     */
    public WarpData getWarp(String name) {
        return warps.get(name);
    }

    /**
     * Prints warp info
     * 
     * @param player
     *            to print to
     * @param warp
     *            to print
     */
    public void printWarp(Player player, String name) {

        WarpData warp = warps.get(name);
        player.sendMessage(ChatColor.YELLOW + "Name: " + ChatColor.GOLD
                + warp.getName());
        player.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.GOLD
                + warp.getLoc().getWorld().toString());
        player.sendMessage(ChatColor.YELLOW + "X: " + ChatColor.GOLD
                + warp.getLoc().getBlockX());
        player.sendMessage(ChatColor.YELLOW + "Y: " + ChatColor.GOLD
                + warp.getLoc().getBlockY());
        player.sendMessage(ChatColor.YELLOW + "Z: " + ChatColor.GOLD
                + warp.getLoc().getBlockZ());
        player.sendMessage(ChatColor.YELLOW + "Created by: " + ChatColor.GOLD
                + warp.getUser());
        player.sendMessage(ChatColor.YELLOW + "On: " + ChatColor.GOLD + ","
                + warp.getDate());

    }

    /**
     * Print all warps to player
     * 
     * @param player
     */
    public void printWarps(Player player) {
        Iterator<Entry<String, WarpData>> it = warps.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, WarpData> next = it.next();
            WarpData nextWarp = next.getValue();
            player.sendMessage(ChatColor.YELLOW + nextWarp.getName());
        }
    }

    /**
     * Loads warp data from file into hashmap
     * 
     * @return true if no errors : false if errors
     */
    public boolean loadData() {
        warpClass.printDebug("Starting to load data...");
        warps.clear();
        try {
            fStreamIn = new FileReader(warpsFile);
            BufferedReader in = new BufferedReader(fStreamIn);

            String line = in.readLine();
            int lineNumber = 0;
            while (line != null) {
                lineNumber++;
                String[] warp = line.split(",");
                String[] coordString = warp[this.coords].split(":");
                double[] coord = new double[coordString.length];
                for (int i = 0; coordString.length < i; i++) {
                    try {
                        coord[i] = Double.parseDouble(coordString[i]);
                    } catch (NumberFormatException ex) {
                        log.warning("[Warps] There was a problem loading warp "
                                + warp[name] + " on line number "
                                + Integer.toString(lineNumber) + "!");
                    }
                }
                if (!warps.containsKey(warp[name])) {
                    warps.put(warp[name],
                            new WarpData(warp[name], new Location(plugin
                                    .getServer().getWorld(warp[world]),
                                    coord[0], coord[1], coord[2]), warp[date],
                                    warp[user]));
                } else {
                    warpClass.printDebug(warp[name] + " already in map!");
                }
                // TODO change date type - talk to tulonsae
                in.close();
            }
        } catch (IOException e) {
            log.warning("[Warps] There was an error loading warp data...");
            e.printStackTrace();
            return false;
        }
        warpClass.printDebug("Finished loading data... no error detected.");
        return true;
    }

    /**
     * Pushes warp data from hashmap to file
     * 
     * @return true if no errors : false if errors
     */
    public boolean pushData() {
        warpClass.printDebug("Pushing data to file...");
        String outWarps = "";
        try {
            fStreamOut = new FileWriter(warpsFile);
            BufferedWriter out = new BufferedWriter(fStreamOut);

            Iterator<Entry<String, WarpData>> it = warps.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, WarpData> next = it.next();
                WarpData nextWarp = next.getValue();
                outWarps = nextWarp.getName() + ","
                        + nextWarp.getLoc().getWorld() + ","
                        + nextWarp.getLoc().getBlockX() + ":"
                        + nextWarp.getLoc().getBlockY() + ":"
                        + nextWarp.getLoc().getBlockZ() + ","
                        + nextWarp.getUser() + "," + nextWarp.getDate();
                out.write(outWarps);
                out.newLine();
            }

            out.close();
            warpClass
                    .printDebug("Finished pushing data... no errors detected.");
            return true;
        } catch (IOException e) {
            log.warning("[Warps] There was an error pushing data: ");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * get current date in "yyyy-MM-dd_HH:mm:ss" format
     * 
     * @return date
     */
    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Date date = new Date();

        return dateFormat.format(date);
    }
}
