package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Handles PlayerInteractEntityEvent.
 */
public class AfkBooterListenerInteractEntity implements Listener {

    private AfkBooterComponent afkBooter;

    public AfkBooterListenerInteractEntity(AfkBooterComponent afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerInteractEntityEvent event) {
        if (!event.isCancelled()) {
             afkBooter.recordActivity(event.getPlayer().getName());
        }
    }
}
