package com.runicsystems.bukkit.AfkBooter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.escapecraft.escapePlug.EscapePlug;

/**
 * AfkBooter plugin by Neromir, modified by morganm, converted to
 * an EscapePlug component with a major rewrite to use latest
 * Bukkit config, event, and permission systems.
 */
public class AfkBooter {

    private static final String VERSION = "1.1";
    private static final String PERMISSION_EXEMPT = "escapeplug.afkbooter.exempt";

    private EscapePlug plugin;
    private Logger log;
    private AfkBooterTimer threadedTimer;
    private MovementTracker movementTracker;
    private int taskId;

    private long timeoutCheckInterval;
    private long kickTimeout;
    private int playerCountThreshold;

    private boolean isIgnoreVehicleMovement;

    private String playerKickMessage;
    private String broadcastKickMessage;

    // refractor - implement this as type of list
    private boolean isChatEventActivity;
    private boolean isCommandEventActivity;
    private boolean isInventoryEventActivity;
    private boolean isDropItemEventActivity;
    private boolean isBlockPlaceEventActivity;
    private boolean isBlockBreakEventActivity;
    private boolean isInteractEventActivity;
    private boolean isInteractEntityEventActivity;

    private ConcurrentHashMap<String, Long> lastPlayerActivity = new ConcurrentHashMap<String, Long>();
    private ConcurrentSkipListSet<String> exemptPlayers = new ConcurrentSkipListSet<String>();

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

        // kick check thread
        threadedTimer = new AfkBooterTimer(this, timeoutCheckInterval);
        threadedTimer.start();

        // movement check thread
        // initial delay of 10 sec, check every 5 sec after that
        movementTracker = new MovementTracker(this);
        taskId = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, movementTracker, 200, 100);

        // refractor, use a list
        // register join/quit events, always; register the others if configured
        plugin.getServer().getPluginManager().registerEvents(new AfkBooterListener(this), plugin);
        if (isChatEventActivity) {
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerChat(this), plugin);
        }
        if (isCommandEventActivity) {
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerCommand(this), plugin);
        }
        if (isInventoryEventActivity) {
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerInventory(this), plugin);
        }
        if (isDropItemEventActivity) {
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerDropItem(this), plugin);
        }
        if (isBlockPlaceEventActivity) {
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerBlockPlace(this), plugin);
        }
        if (isBlockBreakEventActivity) {
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerBlockBreak(this), plugin);
        }
        if (isInteractEventActivity) {
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerInteract(this), plugin);
        }
        if (isInteractEntityEventActivity) {
             plugin.getServer().getPluginManager().registerEvents(new AfkBooterListenerInteractEntity(this), plugin);
        }

        // register command
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

        plugin.getServer().getScheduler().cancelTask(taskId);
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
     * Get Vehicle Movement flag.
     */ 
    public boolean getVehicleFlag() {
        return isIgnoreVehicleMovement;
    }

    /**
     * Record player activity.
     * @param playerName Name of player that did something.
     */
    public synchronized void recordPlayerActivity(String playerName) {
        lastPlayerActivity.put(playerName, System.currentTimeMillis());
    } 

    /**
     * Remove player from activity list.
     */
    public synchronized void stopTrackingPlayer(String playerName) {
        lastPlayerActivity.remove(playerName);
    } 

    /**
     * Add player to the exempt list.
     * @param player Name of the player to add.
     * @param name Name of the command sender.
     */
    public synchronized void addExemptPlayer(String player, String name) {
        exemptPlayers.add(player);
        log.info("AfkBooter: " + name + " added player '" + player + "' to the exempt list.");

        plugin.getConfig().set("plugin.afkbooter.exempt-players", exemptPlayers.toString());

        plugin.saveConfig();
    }

    /**
     * Remove player from the exempt list.
     * @param player Name of the player to remove.
     * @param name Name of the command sender.
     */
    public synchronized void removeExemptPlayer(String player, String name) {
        exemptPlayers.remove(player);
        log.info("AfkBooter: " + name + " removed player '" + player + "' from the exempt list.");

        plugin.getConfig().set("plugin.afkbooter.exempt-players", exemptPlayers.toString());

        plugin.saveConfig();
    }

    /**
     * Get exempt players list.
     */
    public ConcurrentSkipListSet<String> getExemptPlayers() {
        return exemptPlayers;
    }

    /**
     * Kick AFK players.
     */
    public void kickAfkPlayers() {

        // no one to kick
        if (lastPlayerActivity.size() < 1) {
            return;
        }

        // don't kick if under the player count threshold
        if (plugin.getServer().getOnlinePlayers().length < playerCountThreshold) {
            return;
        }

        // set the activity window to the length of kickTimeout (milliseconds),
        // players whose last activity is before the window will be kicked
        // unless exempt
        long activityWindow = System.currentTimeMillis() - kickTimeout;

        // go thru the last activity list
        for (String name : lastPlayerActivity.keySet()) {
            Player player = plugin.getServer().getPlayer(name);
            if (player == null) {
                continue;
            }
            if (player.hasPermission(PERMISSION_EXEMPT) || (exemptPlayers.contains(name.toLowerCase()))) {
                continue;
            }
            long lastActivity = lastPlayerActivity.get(name);
            // java doc says the get returns a null if no mapped items,
            // however java compiler says a long can't be a null
            if (lastActivity == 0) {
                continue;
            }
            if (lastActivity < activityWindow) {
                kickIdlePlayer(player);
            }
        }
    }

    /**
     * Kick an idle player.
     */
    private synchronized void kickIdlePlayer(Player player) {
        if (player.isOnline()) {
            log.info("AfkBooter kicking idle player '" + player.getName() + "'");
            player.kickPlayer(playerKickMessage);
            // don't broadcast kick message if empty or null
            if ((broadcastKickMessage != null) && (!broadcastKickMessage.isEmpty())) {
                plugin.getServer().broadcastMessage(ChatColor.YELLOW + player.getName() + " " + broadcastKickMessage);
            }
        }
    }

    /**
     * Get configuration settings from escapeplug config file.
     */
    private void loadConfig() {

        plugin.getConfig().set("plugin.afkbooter.version", VERSION);

        // config specifies seconds, but we use milliseconds
        timeoutCheckInterval = plugin.getConfig().getLong("plugin.afkbooter.timeout-check-interval", 60) * 1000;
        kickTimeout = plugin.getConfig().getLong("plugin.afkbooter.kick-timeout", 600) * 1000;

        // if 0, players will always be kicked
        playerCountThreshold = plugin.getConfig().getInt("plugin.afkbooter.player-count-threshold", 0);

        // which events count as activities
        // note that movement always counts so it's not an option
        isChatEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-player-chat", true);
        isCommandEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-player-command-preprocess", true);
        isInventoryEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-inventory-open", true);
        isDropItemEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-player-drop-item", true);
        isBlockPlaceEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-block-place", true);
        isBlockBreakEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-block-break", true);
        isInteractEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-player-interact", true);
        isInteractEntityEventActivity = plugin.getConfig().getBoolean("plugin.afkbooter.event-player-interact-entity", true);

        // vehicle flag
        isIgnoreVehicleMovement = plugin.getConfig().getBoolean("plugin.afkbooter.ignore-vehicle-movement", true);

        // kick messages
        playerKickMessage = plugin.getConfig().getString("plugin.afkbooter.kick-message", "Kicked for idling.");
        broadcastKickMessage = plugin.getConfig().getString("plugin.afkbooter.kick-broadcast");

        // get exempt player list
        String exempt = plugin.getConfig().getString("plugin.afkbooter.exempt-players");
        exempt = exempt.replace('[', ' ');
        exempt = exempt.replace(']', ' ');
        if (exempt != null) {
            String[] exemptSplit = exempt.split(",");
            if (exemptSplit != null) {
                for (String name : exemptSplit) {
                    name = name.trim().toLowerCase();
                    if (name.length() > 0) {
                        exemptPlayers.add(name);
                    }
                }
            }
        }

        // save any changes, esp the afkbooter version
        plugin.saveConfig();
    }
}
