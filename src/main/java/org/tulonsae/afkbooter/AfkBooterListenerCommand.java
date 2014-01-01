package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Handles PlayerCommandPreprocessEvent.
 */
public class AfkBooterListenerCommand implements Listener {

    private AfkBooterComponent afkBooter;

    public AfkBooterListenerCommand(AfkBooterComponent afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerCommandPreprocessEvent event) {
        afkBooter.recordActivity(event.getPlayer().getName());
    }
}
