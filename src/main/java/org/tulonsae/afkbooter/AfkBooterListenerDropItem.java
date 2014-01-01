package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Handles PlayerDropItemEvent.
 */
public class AfkBooterListenerDropItem implements Listener {

    private AfkBooterComponent afkBooter;

    public AfkBooterListenerDropItem(AfkBooterComponent afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerDropItemEvent event) {
        if (!event.isCancelled()) {
            afkBooter.recordActivity(event.getPlayer().getName());
        }
    }
}
