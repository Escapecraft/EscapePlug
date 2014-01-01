package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * Handles PlayerInventoryEvent.
 */
public class AfkBooterListenerInventory implements Listener {

    private AfkBooterComponent afkBooter;

    public AfkBooterListenerInventory(AfkBooterComponent afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(InventoryOpenEvent event) {
        afkBooter.recordActivity(event.getPlayer().getName());
    }
}
