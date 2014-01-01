package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Handles PlayerInteractEvent.
 */
public class AfkBooterListenerInteract implements Listener {

    private AfkBooterComponent afkBooter;

    public AfkBooterListenerInteract(AfkBooterComponent afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerInteractEvent event) {
        if (!event.isCancelled()) {
             afkBooter.recordActivity(event.getPlayer().getName());
        }
    }
}
