package net.serubin.warp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.escapecraft.escapeplug.EscapePlug;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
    private int yaw = 5;
    private int pitch = 6;
    // Hashmaps
    private Map<String, WarpData> warps = new HashMap<String, WarpData>();
    private Map<String, WarpData> disabledWarps = new HashMap<String, WarpData>();

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
     * 
     *            warpComponent
     */
    public FlatFile(EscapePlug plugin, WarpComponent warpClass) {
        this.plugin = plugin;
        this.warpClass = warpClass;
        this.log = plugin.getLogger();

        log.info("Loading in warp data...");
        if (loadData()) {
            log.info("Sucessfully loaded warp data!");
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
        name = name.toLowerCase();
        if (!warps.containsKey(name)) {
            warps.put(name, new WarpData(name, loc, getDate(), user.getName(),
                    loc.getYaw(), loc.getPitch()));
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
            return false;
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
    public void printWarp(CommandSender sender, String name) {

        WarpData warp = warps.get(name);
        sender.sendMessage(ChatColor.YELLOW + "Name: " + ChatColor.GOLD
                + warp.getName());
        sender.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.GOLD
                + warp.getLoc().getWorld().getName());
        sender.sendMessage(ChatColor.YELLOW + "X: " + ChatColor.GOLD
                + warp.getLoc().getBlockX() + ChatColor.YELLOW + " Y: "
                + ChatColor.GOLD + warp.getLoc().getBlockY() + ChatColor.YELLOW
                + " Z: " + ChatColor.GOLD + warp.getLoc().getBlockZ());
        sender.sendMessage(ChatColor.YELLOW + "Created by: " + ChatColor.GOLD
                + warp.getUser());
        sender.sendMessage(ChatColor.YELLOW + "On: " + ChatColor.GOLD
                + warp.getDate());

    }

    /**
     * Print all warps to player
     * 
     * @param player
     */
    public void printWarps(CommandSender sender) {
        Iterator<Entry<String, WarpData>> it = warps.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, WarpData> next = it.next();
            WarpData nextWarp = next.getValue();
            try {
                sender.sendMessage(ChatColor.GOLD + nextWarp.getName()
                        + ChatColor.YELLOW + " ("
                        + nextWarp.getLoc().getWorld().getName() + ", "
                        + nextWarp.getLoc().getBlockX() + ", "
                        + nextWarp.getLoc().getBlockY() + ", "
                        + nextWarp.getLoc().getBlockZ() + ")");
            } catch (NullPointerException NPE) {
                log.warning("There was an issue displaying warp "
                        + nextWarp.getName() + ":");
                NPE.printStackTrace();
            }
        }
    }

    /**
     * Loads warp data from file into hashmap
     * 
     * @return true if no errors : false if errors
     */
    public boolean loadData() {
        // Creates new file
        warpsFile = new File(plugin.getDataFolder(), fileName);
        if (!warpsFile.exists()) {
            log.info("Creating '" + fileName + "'...");
            try {
                warpsFile.createNewFile();
            } catch (IOException e) {
                log.warning("'" + fileName + "' could not be created!");
                e.printStackTrace();
            }
        }
        // Loads data
        // TODO alphabetize
        warpClass.printDebug("Starting to load data...");
        warps.clear();
        try {
            fStreamIn = new FileReader(warpsFile);
            BufferedReader in = new BufferedReader(fStreamIn);

            String line = in.readLine();
            int lineNumber = 0;
            while (line != null) {
                warpClass.printDebug(line);
                String[] warp = line.split(",");
                // Processes coords
                String[] coordString = warp[this.coords].split(":");
                for (String str : coordString) {
                    warpClass.printDebug(str);
                }
                double[] coord = new double[coordString.length];
                for (int i = 0; i < coordString.length; i++) {
                    try {
                        coord[i] = Double.parseDouble(coordString[i]);
                    } catch (NumberFormatException ex) {
                        log.warning("[Warps] There was a problem loading warp "
                                + warp[name] + " on line number "
                                + Integer.toString(lineNumber) + "!");
                    }
                }
                // Yaw-pitch work around
                float[] yapi = new float[2];
                try {
                    yapi[0] = Float.parseFloat(warp[yaw]);
                    yapi[1] = Float.parseFloat(warp[pitch]);
                } catch (NumberFormatException ex) {
                    log.warning("[Warps] There was a problem loading warp "
                            + warp[name] + " on line number "
                            + Integer.toString(lineNumber) + "!");
                }
                if (!warps.containsKey(warp[name])) {
                    if (plugin.getServer().getWorld(warp[world]) != null) {
                        warps.put(warp[name].toLowerCase(), new WarpData(
                                warp[name].toLowerCase(), new Location(plugin
                                        .getServer().getWorld(warp[world]),
                                        coord[0], coord[1], coord[2]),
                                warp[date], warp[user], yapi[0], yapi[1]));
                        lineNumber++;
                    } else {
                        log.warning(warp[name] + " was not loaded, world '"
                                + warp[world] + "' does not exist! ("
                                + warp[world] + ", " + coord[0] + ", "
                                + coord[1] + ", " + coord[2] + ")(Line "
                                + lineNumber + ")");
                    }
                } else {
                    warpClass.printDebug(warp[name] + " already in map!");
                }
                line = in.readLine();
            }
            in.close();
            log.info(Integer.toString(lineNumber) + " warps loaded!");
        } catch (IOException e) {
            log.warning("[Warps] There was an error loading warp data...");
            e.printStackTrace();
            return false;
        }
        warpClass.printDebug("Finished loading data... no error detected.");
        for (Entry<String, WarpData> entry : warps.entrySet()) {
            warpClass.printDebug(entry.getValue().toString());
        }
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
                if (nextWarp != null) {
                    outWarps = nextWarp.getName() + ","
                            + nextWarp.getLoc().getWorld().getName() + ","
                            + nextWarp.getLoc().getX() + ":"
                            + nextWarp.getLoc().getY() + ":"
                            + nextWarp.getLoc().getZ() + ","
                            + nextWarp.getUser() + "," + nextWarp.getDate()
                            + "," + nextWarp.getLoc().getYaw() + ","
                            + nextWarp.getLoc().getPitch() + ",";
                    out.write(outWarps);
                    out.newLine();
                }
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
