package com.runicsystems.bukkit.AfkBooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Handle BlockPlaceEvent.
 */
public class AfkBooterListenerBlockPlace implements Listener {

    private AfkBooter afkBooter;

    public AfkBooterListenerBlockPlace(AfkBooter afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(BlockPlaceEvent event) {
        afkBooter.recordPlayerActivity(event.getPlayer().getName());
    }
}
