package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Handle PlayerJoinEvent and PlayerQuitEvent.
 */
public class AfkBooterListener implements Listener {

    private AfkBooter afkBooter;
    private MovementTracker movementTracker;

    public AfkBooterListener(AfkBooter afkBooter) {
        this.afkBooter = afkBooter;
        if (afkBooter.getMovementTrackerFlag()) {
            this.movementTracker = afkBooter.getMovementTracker();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerJoinEvent event) {
        afkBooter.getPlayerActivity().recordActivity(event.getPlayer().getName());
        if (afkBooter.getMovementTrackerFlag()) {
            movementTracker.addPlayer(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerQuit(PlayerQuitEvent event) {
        afkBooter.getPlayerActivity().removePlayer(event.getPlayer().getName());
        if (afkBooter.getMovementTrackerFlag()) {
            movementTracker.removePlayer(event.getPlayer().getName());
        }
    }
}
