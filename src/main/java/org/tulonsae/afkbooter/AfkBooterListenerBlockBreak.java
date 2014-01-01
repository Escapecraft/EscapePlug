package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Handles BlockBreakEvent.
 */
public class AfkBooterListenerBlockBreak implements Listener {

    private AfkBooterComponent afkBooter;

    public AfkBooterListenerBlockBreak(AfkBooterComponent afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(BlockBreakEvent event) {
        afkBooter.recordActivity(event.getPlayer().getName());
    }
}
