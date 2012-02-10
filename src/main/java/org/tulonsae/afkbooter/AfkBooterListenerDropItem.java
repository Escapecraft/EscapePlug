package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Handle PlayerDropItemEvent.
 */
public class AfkBooterListenerDropItem implements Listener {

    private AfkBooter afkBooter;

    public AfkBooterListenerDropItem(AfkBooter afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerDropItemEvent event) {
        if (!event.isCancelled()) {
            afkBooter.getPlayerActivity().recordActivity(event.getPlayer().getName());
        }
    }
}
