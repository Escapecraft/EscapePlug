package org.tulonsae.afkbooter;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.escapecraft.escapeplug.EscapePlug;

/**
 * Schedules a Bukkit process to periodically determine if players have moved.
 * <p />
 * Uses this method instead of the PlayerMoveEvent in order to reduce load
 * by not listening to every move event.
 * <p />
 * If the player is using a vehicle when checked and the ignore-vehicle-movement
 * config setting is true, then the move is not counted as activity.
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

    private HashMap<String, Location> lastLoc = new HashMap<String, Location>();
    
    /**
     * Constructs this object.
     *
     * @param afkBooter the AfkBooter component object
     */
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

        afkBooter.writeDebugMsg("created MovementTracker object");
    }
    
    /**
     * Removes a player from the last location list.
     *
     * @param playerName name of player to remove
     */
    public void removePlayer(String playerName) {
        lastLoc.remove(playerName);
    }
    
    /**
     * Adds a player and their location to the last location list.
     *
     * @param player player to add
     */
    public void addPlayer(Player player) {
        Location curLoc = player.getLocation();
        String name = player.getName();

        if ((curLoc != null) && (name != null)) {
            lastLoc.put(name, curLoc);
        }
    }
    
    /**
     * Gets the last location of each player.
     * <p />
     * The method for the Bukkit scheduler to run.
     */ 
    public void run() {
        afkBooter.writeDebugMsg("begin MovementTracker.run()..."); 

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
                    afkBooter.recordActivity(name);
                }
            }
        }

        afkBooter.writeDebugMsg("...end MovementTracker.run()"); 
    }

    /**
     * Gets configuration settings from escapeplug config file.
     */
    private void loadConfig() {

        // vehicle flag
        isIgnoreVehicleMovement = plugin.getConfig().getBoolean("plugin.afkbooter.ignore-vehicle-movement", true);

        // save any changes
        plugin.saveConfig();
    }

}
