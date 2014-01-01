package org.tulonsae.afkbooter;

import net.escapecraft.escapeplug.EscapePlug;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Schedules a Bukkit process to periodically check for idle players to kick.
 *
 * @author Tulonsae
 * Original author neromir.
 */
public class KickChecker implements Runnable {

    private static final String PERMISSION_EXEMPT = "escapeplug.afkbooter.exempt";

    private AfkBooterComponent afkBooter;
    private EscapePlug plugin;

    // config settings
    private long kickTimeout;
    private int playerCountThreshold;
    private String playerKickMessage;
    private String broadcastKickMessage;

    /**
     * Constructs this object.
     *
     * @param afkBooter the AfkBooter component object
     */
    public KickChecker(AfkBooterComponent afkBooter) {
        this.afkBooter = afkBooter;
        this.plugin = afkBooter.getPlugin();

        // load configuration
        loadConfig();

        afkBooter.writeDebugMsg("created AfkBooterTimer object");
    }

    /**
     * Kicks idle players.
     * <p />
     * The method for the Bukkit scheduler to run.
     */
    @Override
    public void run() {
        afkBooter.writeDebugMsg("begin KickChecker.run()..."); 

        // don't kick if under the player count threshold
        //   this is an option in the config
        if (plugin.getServer().getOnlinePlayers().length < playerCountThreshold) {
            return;
        }

        // set the activity window to the length of kickTimeout (milliseconds),
        // players whose last activity is before the window will be kicked
        // unless exempt
        long activityWindow = System.currentTimeMillis() - kickTimeout;
        afkBooter.writeDebugMsg("activityWindow=" + activityWindow);

        // go thru the last activity list
        for (String name : afkBooter.getNames()) {
            Player player = plugin.getServer().getPlayer(name);
            if (player == null) {
                continue;
            }
            if (player.hasPermission(PERMISSION_EXEMPT) || (afkBooter.getExemptList().contains(name.toLowerCase()))) {
                continue;
            }
            Long activityTime = afkBooter.getTime(name);
            if (activityTime == null) {
                continue;
            }
            long lastActivity = activityTime.longValue();
            if (lastActivity < activityWindow) {
                kickIdlePlayer(player);
            }
        }

        afkBooter.writeDebugMsg("...end KickChecker.run()"); 
    }

    /**
     * Kicks an idle player.
     *
     * @param player player to kick
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
     * Gets configuration settings from escapeplug config file.
     */
    private void loadConfig() {

        // config specifies seconds, but we use milliseconds
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
