package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Handles PlayerChatEvent.
 */
public class AfkBooterListenerChat implements Listener {

    private AfkBooterComponent afkBooter;

    public AfkBooterListenerChat(AfkBooterComponent afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(AsyncPlayerChatEvent event) {
        afkBooter.recordActivity(event.getPlayer().getName());
    }
}
