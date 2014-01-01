package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Handles PlayerJoinEvent and PlayerQuitEvent.
 */
public class AfkBooterListener implements Listener {

    private AfkBooterComponent afkBooter;
    private MovementTracker movementTracker;

    public AfkBooterListener(AfkBooterComponent afkBooter) {
        this.afkBooter = afkBooter;
        if (afkBooter.getMovementTrackerFlag()) {
            this.movementTracker = afkBooter.getMovementTracker();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerJoinEvent event) {
        afkBooter.recordActivity(event.getPlayer().getName());
        if (afkBooter.getMovementTrackerFlag()) {
            movementTracker.addPlayer(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerQuit(PlayerQuitEvent event) {
        afkBooter.removePlayerFromActivityList(event.getPlayer().getName());
        if (afkBooter.getMovementTrackerFlag()) {
            movementTracker.removePlayer(event.getPlayer().getName());
        }
    }
}
