package org.tulonsae.afkbooter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player activity data.
 * This is placed into its own class so that it can be synchronized properly.
 *
 * @author Tulonsae
 */
public class PlayerActivity {

    private AfkBooter afkBooter;

    private ConcurrentHashMap<String, Long> lastActivity;

    /**
     * Construct this object.
     * @param afkBooter AfkBooter component object.
     */
    public PlayerActivity(AfkBooter afkBooter) {
        this.afkBooter = afkBooter;

        lastActivity = new ConcurrentHashMap<String, Long>();

        sendDebugMsg("created PlayerActivity object.");
    }

    /**
     * Send a debug message to the component object.
     * @param message Debug message.
     */
    private void sendDebugMsg(String message) {
        afkBooter.writeDebugMsg(this.toString() + " " + message);
    }

    /**
     * Record player activity.
     * @param playerName Name of player that did something.
     */
    public synchronized void recordActivity(String playerName) {
        sendDebugMsg("record activity for " + playerName + ".");

        lastActivity.put(playerName, System.currentTimeMillis());
    }

    /**
     * Remove player from activity list.
     * @param playerName Name of player to remove from activity list.
     */
    public synchronized void removePlayer(String playerName) {
        sendDebugMsg("remove player " + playerName + "from activity list.");

        lastActivity.remove(playerName);
    }

    /**
     * Get list of names in activity list.
     * @return Set list of names
     */
    public Set<String> getNames() {
        return lastActivity.keySet();
    }

    /**
     * Get last activity time for player.
     * @return Long last activity time in milliseconds for specified player.
     */
    public Long getTime(String name) {
        return lastActivity.get(name);
    }
}
