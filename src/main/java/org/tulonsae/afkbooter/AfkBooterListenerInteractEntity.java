package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Handle PlayerInteractEntityEvent.
 */
public class AfkBooterListenerInteractEntity implements Listener {

    private AfkBooter afkBooter;

    public AfkBooterListenerInteractEntity(AfkBooter afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerInteractEntityEvent event) {
        if (!event.isCancelled()) {
             afkBooter.getPlayerActivity().recordActivity(event.getPlayer().getName());
        }
    }
}
