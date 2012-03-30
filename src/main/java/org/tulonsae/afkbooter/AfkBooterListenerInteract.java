package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Handle PlayerInteractEvent.
 */
public class AfkBooterListenerInteract implements Listener {

    private AfkBooter afkBooter;

    public AfkBooterListenerInteract(AfkBooter afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerInteractEvent event) {
        if (!event.isCancelled()) {
             afkBooter.getPlayerActivity().recordActivity(event.getPlayer().getName());
        }
    }
}
