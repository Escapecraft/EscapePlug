package org.tulonsae.afkbooter;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.escapecraft.escapePlug.EscapePlug;

/**
 * AfkBooter component thread that checks for idle players to kick.
 *
 * @author Tulonsae
 * Original author neromir.
 */
public class AfkBooterTimer extends Thread
{
    private static final String PERMISSION_EXEMPT = "escapeplug.afkbooter.exempt";

    private AfkBooter afkBooter;
    private EscapePlug plugin;
    private PlayerActivity activity;
    private boolean aborted;

    // config settings
    private long timeoutCheckInterval;
    private long kickTimeout;
    private int playerCountThreshold;
    private String playerKickMessage;
    private String broadcastKickMessage;

    /**
     * Construct this object.
     * @param afkBooter the AfkBooter component object.
     */
    public AfkBooterTimer(AfkBooter afkBooter) {
        this.afkBooter = afkBooter;
        this.plugin = afkBooter.getPlugin();
        this.activity = afkBooter.getPlayerActivity();
        this.aborted = false;

        // load configuration
        loadConfig();

        afkBooter.writeDebugMsg("created AfkBooterTimer object.");
    }

    @Override
    public void run() {
        while(!aborted) {
            try {
                kickAfkPlayers();
                Thread.sleep(timeoutCheckInterval);
            } catch(InterruptedException e) {
                afkBooter.writeInfoMsg("AfkBooterTimer thread interrupted while sleeping.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Let this thread know that it should not run.
     * @param aborted whether to abort the run or not.
     */
    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    /**
     * Kick AFK players.
     */
    public void kickAfkPlayers() {

        afkBooter.writeDebugMsg("running kickAfkPlayers()."); 

        // don't kick if under the player count threshold
        if (plugin.getServer().getOnlinePlayers().length < playerCountThreshold) {
            return;
        }

        // set the activity window to the length of kickTimeout (milliseconds),
        // players whose last activity is before the window will be kicked
        // unless exempt
        long activityWindow = System.currentTimeMillis() - kickTimeout;

        // go thru the last activity list
        for (String name : activity.getNames()) {
            Player player = plugin.getServer().getPlayer(name);
            if (player == null) {
                continue;
            }
            if (player.hasPermission(PERMISSION_EXEMPT) || (afkBooter.getExemptList().getPlayers().contains(name.toLowerCase()))) {
                continue;
            }
            Long activityTime = activity.getTime(name);
            if (activityTime == null) {
                continue;
            }
            long lastActivity = activityTime.longValue();
            if (lastActivity < activityWindow) {
                kickIdlePlayer(player);
            }
        }
    }

    /**
     * Kick an idle player.
     */
    private void kickIdlePlayer(Player player) {
        if (player.isOnline()) {
            afkBooter.writeInfoMsg("kicking idle player '" + player.getName() + "'.");
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

        // config specifies seconds, but we use milliseconds
        timeoutCheckInterval = plugin.getConfig().getLong("plugin.afkbooter.timeout-check-interval", 60) * 1000;
        kickTimeout = plugin.getConfig().getLong("plugin.afkbooter.kick-timeout", 600) * 1000;

        // if 0, players will always be kicked
        playerCountThreshold = plugin.getConfig().getInt("plugin.afkbooter.player-count-threshold", 0);

        // kick messages
        playerKickMessage = plugin.getConfig().getString("plugin.afkbooter.kick-message", "Kicked for idling.");
        broadcastKickMessage = plugin.getConfig().getString("plugin.afkbooter.kick-broadcast");

        // save any changes
        plugin.saveConfig();
    }
}
