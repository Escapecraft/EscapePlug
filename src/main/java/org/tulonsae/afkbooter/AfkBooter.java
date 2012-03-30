package org.tulonsae.afkbooter;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.escapecraft.escapePlug.EscapePlug;

/**
 * AfkBooter component of EscapePlug.
 *
 * @author Tulonsae
 * Based off version of AfkBooter plugin, written by Neromir and
 * modified by morganm, but massively rewritten by Tulonsae.
 * Updated to the latest Bukkit api for config, even, and permission
 * systems.
 */
public class AfkBooter {

    private static final String VERSION = "1.1.3";
    private static final String PERMISSION_EXEMPT = "escapeplug.afkbooter.exempt";

    private EscapePlug plugin;
    private Logger log;
    private AfkBooterTimer threadedTimer;
    private MovementTracker movementTracker;
    private int taskId;
    private PlayerActivity playerActivity;
    private ExemptList exemptList;

    private boolean isDebug;

    // refractor - implement this as type of list
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
     * Construct this component
     * @param plugin EscapePlug plugin
     */
    public AfkBooter (EscapePlug plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();

        if (!enable()) {
            log.warning("AfkBooter didn't enable properly.");
        }
    }

    /**
     * Called during onEnable()
     */
    public boolean enable() {

        // load configuration
        loadConfig();

        // player activity object
        playerActivity = new PlayerActivity(this);

        // exempt list object
        exemptList = new ExemptList(this);

        // kick check thread
        threadedTimer = new AfkBooterTimer(this);
        threadedTimer.start();

        // movement check thread
        // initial delay of 1 min, check every 30 sec after that
        // this uses ticks, and there are (ideally0 20 ticks per second
        if (isMoveEventActivity) {
            movementTracker = new MovementTracker(this);
            taskId = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, movementTracker, 1200, 600);
        }

        // refractor, use a list
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
        plugin.getCommand("afkbooter").setExecutor(afkBooterCommand);

        return true;
    }

    /**
     * Called during onDisable()
     */
    public void tidyUp() {

        if (threadedTimer != null) {
            threadedTimer.setAborted(true);
            threadedTimer = null;
        }

        if (isMoveEventActivity) {
            plugin.getServer().getScheduler().cancelTask(taskId);
        }
    }

    /**
     * Write info message
     * @param message Information to write.
     */
    public void writeInfoMsg(String message) {
        log.info("AfkBooter: " + message);
    }

    /**
     * Write debug message
     * @param message Information to write.
     */
    public void writeDebugMsg(String message) {
        if (isDebug) {
            writeInfoMsg("debug: " + message);
        }
    }

    /**
     * Get EscapePlug plugin.
     * @return plugin passed into this component.
     */
    public EscapePlug getPlugin() {
        return plugin;
    }

    /**
     * Get MovementTracker object.
     * @return movement tracker object created by this component.
     */
    public MovementTracker getMovementTracker() {
        return movementTracker;
    }

    /**
     * Get Movement Tracker flag.
     * @return whether Movement Tracker flag is true or false.
     */ 
    public boolean getMovementTrackerFlag() {
        return isMoveEventActivity;
    }

    /**
     * Get PlayerActivity object.
     * @return player activity object created by this component.
     */
    public PlayerActivity getPlayerActivity () {
        return playerActivity;
    }

    /**
     * Get ExemptList object.
     * @return exempt list object created by this component.
     */
    public ExemptList getExemptList() {
        return exemptList;
    }

    /**
     * Get Debug flag.
     * @return whether debug flag is true or false.
     */ 
    public boolean getDebugFlag() {
        return isDebug;
    }

    /**
     * Change Debug mode.
     * @param flag Debug mode to set.
     */
    public void changeDebugMode(String name, boolean flag) {

        isDebug = flag;
        plugin.getConfig().set("plugin.afkbooter.debug", flag);
        plugin.saveConfig();

        writeInfoMsg(name + " changed debug mode to " + flag + ".");
    }

    /**
     * Get configuration settings from escapeplug config file.
     */
    private void loadConfig() {

        plugin.getConfig().set("plugin.afkbooter.version", VERSION);

        // get debug mode
        isDebug = plugin.getConfig().getBoolean("plugin.afkbooter.debug", false);

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
}
