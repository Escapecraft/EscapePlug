package org.tulonsae.afkbooter;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.escapecraft.escapePlug.EscapePlug;

/**
 * Schedules a Bukkit process to periodically determine if players have moved.
 * Uses this method instead of the PlayerMoveEvent.  If player is using a
 * vehicle when checked and ignore-vehicle-movement config setting is true,
 * the move is not counted as activity.
 *
 * @author Tulonsae
 * Original author morganm.  Idea is the same, but algorithm rewritten by
 * Tulonsae.
 */
public class MovementTracker implements Runnable {

    private final AfkBooter afkBooter;
    private EscapePlug plugin;

    // config settings
    private boolean isIgnoreVehicleMovement;

    private ConcurrentHashMap<String, Location> lastLoc = new ConcurrentHashMap<String, Location>();
    
    public MovementTracker(AfkBooter afkBooter) {
        this.afkBooter = afkBooter;
        this.plugin = afkBooter.getPlugin();

        // load configuration
        loadConfig();

        // initialize with current online players
        Player[] players = plugin.getServer().getOnlinePlayers();
        for (Player player : players) {
            addPlayer(player);
        }

        afkBooter.writeDebugMsg("created MovementTracker object.");
    }
    
    public void removePlayer(String playerName) {
        lastLoc.remove(playerName);
    }
    
    public void addPlayer(Player player) {
        Location curLoc = player.getLocation();
        String name = player.getName();

        if ((curLoc != null) && (name != null)) {
            lastLoc.put(name, curLoc);
        }
    }
    
    public void run() {
        for (String name : lastLoc.keySet()) {
            Player player = plugin.getServer().getPlayer(name);
            if (player == null) {
                removePlayer(name);
            } else {
                // workaround for bukkit issue
                String prevWorld = lastLoc.get(name).getWorld().getName();
                String curWorld = player.getLocation().getWorld().getName();
                if (prevWorld.equals(curWorld)) {
                    if (player.getLocation().distance(lastLoc.get(name)) < 1) {
                        continue;
                    }
                }
                // if distance between player's current location and stored
                // location is less than 1, this move is not active; if active
                // and ignore vehicle movement is set and player in vehicle,
                // then update location, but don't count this move as active
                addPlayer(player);
                if (!isIgnoreVehicleMovement || !player.isInsideVehicle()) {
                    afkBooter.getPlayerActivity().recordActivity(name);
                }
            }
        }
    }

    /**
     * Get configuration settings from escapeplug config file.
     */
    private void loadConfig() {

        // vehicle flag
        isIgnoreVehicleMovement = plugin.getConfig().getBoolean("plugin.afkbooter.ignore-vehicle-movement", true);

        // save any changes
        plugin.saveConfig();
    }
}
