package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * Handle PlayerInventoryEvent.
 */
public class AfkBooterListenerInventory implements Listener {

    private AfkBooter afkBooter;

    public AfkBooterListenerInventory(AfkBooter afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(InventoryOpenEvent event) {
        afkBooter.getPlayerActivity().recordActivity(event.getPlayer().getName());
    }
}
