package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Handles BlockPlaceEvent.
 */
public class AfkBooterListenerBlockPlace implements Listener {

    private AfkBooterComponent afkBooter;

    public AfkBooterListenerBlockPlace(AfkBooterComponent afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(BlockPlaceEvent event) {
        afkBooter.recordActivity(event.getPlayer().getName());
    }
}
