package org.tulonsae.afkbooter;

import java.util.logging.Logger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.component.Log;
import net.escapecraft.escapeplug.EscapePlug;

/**
 * AfkBooter component of EscapePlug.
 * <p />
 * Note: The Bukkit Dev team has decided that scheduled events have to be done
 * in the main server thread so these tasks can not be run in separate threads
 * to reduce load and improve performance.
 *
 * @author Tulonsae
 * Based off version of AfkBooter plugin, written by Neromir and modified by
 * morganm, but massively rewritten by Tulonsae.  Updated to the latest Bukkit
 * api (1.5.1) for config, event, scheduling, and permission systems.
 */
@ComponentDescriptor(name="AFKBooter",slug="afkbooter",version="1.1.4")
public class AfkBooter extends AbstractComponent {

    private static final String VERSION = "1.1.4";

    private String CONFIG_EXEMPT_PLAYERS = "plugin.afkbooter.exempt-players";

    private EscapePlug plugin;
    private Logger log;

    // schedulers
    private MovementTracker movementTracker;
    private KickChecker kickChecker;
    private int taskIdMovementTracker;
    private int taskIdKickChecker;
    private long timeoutCheckInterval;

    // lists
    private Map<String, Long> lastActivity = null;
    private Set<String> exemptList = null;

    // debug flag
    private boolean isDebug;

    // flags for activity types to check
    private boolean isMoveEventActivity;
    private boolean isChatEventActivity;
    private boolean isCommandEventActivity;
    private boolean isInventoryEventActivity;
    private boolean isDropItemEventActivity;
    private boolean isBlockPlaceEventActivity;
    private boolean isBlockBreakEventActivity;
    private boolean isInteractEventActivity;
    private boolean isInteractEntityEventActivity;

    /**
     * Called during onEnable()
     */
    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
        
        // load configuration
        loadConfig();

        // create exempt list
        exemptList = new HashSet<String>();
        writeDebugMsg("created exempt list");

        // create last activity list
        lastActivity = new HashMap<String, Long>();
        writeDebugMsg("created last activity list");

        // schedule movement tracker
        //   initial delay of 60 sec, check every 30 sec after that
        //   this uses ticks, and there are (ideally) 20 ticks per second
        if (isMoveEventActivity) {
            movementTracker = new MovementTracker(this);
            taskIdMovementTracker = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, movementTracker, 1200, 600);
            if (taskIdMovementTracker == -1) {
                isMoveEventActivity = false;
                writeWarnMsg("movement tracker could not be scheduled by bukkit, turning off movement event activity");
            } else {
                writeDebugMsg("started MovementTracker scheduling, id=" + taskIdMovementTracker);
            }
        }

        // schedule kick checker thread
        //   initial delay of 90 sec, check periodically after that
        //   this uses ticks, and there are (ideally) 20 ticks per second
        kickChecker = new KickChecker(this);
        taskIdKickChecker = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, kickChecker, 1800L, (timeoutCheckInterval * 20L));
        if (taskIdKickChecker == -1) {
            writeWarnMsg("kick checker could not be scheduled by bukkit, players will NOT be kicked!");
        } else {
            writeDebugMsg("started KickChecker scheduling, id=" + taskIdKickChecker);
        }

        // register join/quit events, always; register the others if configured
        plugin.getServer().getPluginManager().registerEvents(new AfkBooterListener(this), plugin);
        if (isChatEventActivity) {
             writeDebugMsg("registered AfkListenerChat");
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerChat(this), plugin);
        }
        if (isCommandEventActivity) {
             writeDebugMsg("registered AfkListenerCommand");
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerCommand(this), plugin);
        }
        if (isInventoryEventActivity) {
             writeDebugMsg("registered AfkListenerInventory");
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerInventory(this), plugin);
        }
        if (isDropItemEventActivity) {
             writeDebugMsg("registered AfkListenerDropItem");
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerDropItem(this), plugin);
        }
        if (isBlockPlaceEventActivity) {
             writeDebugMsg("registered AfkListenerBlockPlace");
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerBlockPlace(this), plugin);
        }
        if (isBlockBreakEventActivity) {
             writeDebugMsg("registered AfkListenerBlockBreak");
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerBlockBreak(this), plugin);
        }
        if (isInteractEventActivity) {
             writeDebugMsg("registered AfkListenerInteract");
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerInteract(this), plugin);
        }
        if (isInteractEntityEventActivity) {
             writeDebugMsg("registered AfkListenerInteractEntity");
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerInteractEntity(this), plugin);
        }

        // register command
        writeDebugMsg("registered AfkBooterCommand");
        AfkBooterCommand afkBooterCommand = new AfkBooterCommand(this);
        plugin.getComponentManager().registerCommands(afkBooterCommand);

        return true;
    }

    /**
     * Called during onDisable()
     */
    @Override
    public void disable() {

        if (taskIdKickChecker != -1) {
            plugin.getServer().getScheduler().cancelTask(taskIdKickChecker);
        }

        if (isMoveEventActivity) {
            plugin.getServer().getScheduler().cancelTask(taskIdMovementTracker);
        }
    }

    /**
     * Writes an info message.
     *
     * @param message information to write
     */
    public void writeInfoMsg(String message) {
        log.info("AfkBooter: " + message);
    }

    /**
     * Writes a warning message.
     *
     * @param message warning to write
     */
    public void writeWarnMsg(String message) {
        log.warning("AfkBooter: " + message);
    }

    /**
     * Writes a debug message.
     *
     * @param message debug information to write
     */
    public void writeDebugMsg(String message) {
        if (isDebug) {
            writeInfoMsg("debug: " + message);
        }
    }

    /**
     * Gets the EscapePlug plugin.
     *
     * @return plugin passed into this component
     */
    public EscapePlug getPlugin() {
        return plugin;
    }

    /**
     * Get MovementTracker object.
     * @return movement tracker object created by this component
     */
    public MovementTracker getMovementTracker() {
        return movementTracker;
    }

    /**
     * Gets movement tracker flag.
     *
     * @return whether movement tracker flag is true or false
     */ 
    public boolean getMovementTrackerFlag() {
        return isMoveEventActivity;
    }

    /**
     * Gets the debug flag.
     *
     * @return whether debug flag is true or false
     */ 
    public boolean getDebugFlag() {
        return isDebug;
    }

    /**
     * Changes the debug mode.
     *
     * @param flag debug mode to set
     */
    public void changeDebugMode(String name, boolean flag) {

        isDebug = flag;
        plugin.getConfig().set("plugin.afkbooter.debug", flag);
        plugin.saveConfig();

        writeInfoMsg(name + " changed debug mode to " + flag + ".");
    }

    /**
     * Gets configuration settings from the EscapePlug config file.
     */
    private void loadConfig() {

        plugin.getConfig().set("plugin.afkbooter.version", VERSION);

        // get debug mode
        isDebug = plugin.getConfig().getBoolean("plugin.afkbooter.debug", false);

        // how often to run KickChecker
        timeoutCheckInterval = plugin.getConfig().getLong("plugin.afkbooter.timeout-check-interval", 60);

        // which events count as activities
        isMoveEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-player-move", false);
        isChatEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-player-chat", true);
        isCommandEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-player-command-preprocess", true);
        isInventoryEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-inventory-open", true);
        isDropItemEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-player-drop-item", true);
        isBlockPlaceEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-block-place", true);
        isBlockBreakEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-block-break", true);
        isInteractEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-player-interact", true);
        isInteractEntityEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-player-interact-entity", true);

        // save any changes, esp the afkbooter version
        plugin.saveConfig();
    }

    /**
     * Saves exempt list to config file.
     */
    private void saveExemptList() {
        plugin.getConfig().set(CONFIG_EXEMPT_PLAYERS, exemptList.toString());
        plugin.saveConfig();
    }

    /**
     * Adds player to the exempt list.
     *
     * @param player name of the player to add
     * @param name name of the command sender
     */
    public void addPlayer(String player, String name) {
        exemptList.add(player);
        writeInfoMsg("added player '" + player + "' to the exempt list.");
        saveExemptList();
    }

    /**
     * Removes player from the exempt list.
     *
     * @param player name of the player to remove
     * @param name name of the command sender
     */
    public void removePlayerFromExemptList(String player, String name) {
        exemptList.remove(player);
        writeInfoMsg("removed player '" + player + "' from the exempt list.");  
        saveExemptList();
    }

    /**
     * Gets exempt players list.
     */
    public Set<String> getExemptList() {
        return exemptList;
    }

    /**
     * Records a player's activity.
     *
     * @param playerName name of the player that performed a tracked activity
     */
    public void recordActivity(String playerName) {
        writeDebugMsg("record activity for " + playerName);

        lastActivity.put(playerName, System.currentTimeMillis());
    }

    /**
     * Removes a player from the activity list.
     *
     * @param playerName name of the player to remove from the activity list
     */
    public void removePlayerFromActivityList(String playerName) {
        writeDebugMsg("remove player " + playerName + "from activity list");

        lastActivity.remove(playerName);
    }

    /**
     * Gets the list of names in the activity list.
     * @return set of names
     */
    public Set<String> getNames() {
        return lastActivity.keySet();
    }

    /**
     * Gets the last activity time for a player.
     * @return last activity time in milliseconds for a specified player
     */
    public Long getTime(String name) {
        return lastActivity.get(name);
    }

}
