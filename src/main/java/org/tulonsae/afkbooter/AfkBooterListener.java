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
        this.movementTracker = afkBooter.getMovementTracker();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerJoinEvent event) {
        afkBooter.recordPlayerActivity(event.getPlayer().getName());
        movementTracker.addPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerQuit(PlayerQuitEvent event) {
        afkBooter.stopTrackingPlayer(event.getPlayer().getName());
        movementTracker.removePlayer(event.getPlayer().getName());
    }
}
