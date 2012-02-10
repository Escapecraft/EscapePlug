package org.tulonsae.afkbooter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

/**
 * Handle PlayerChatEvent.
 */
public class AfkBooterListenerChat implements Listener {

    private AfkBooter afkBooter;

    public AfkBooterListenerChat(AfkBooter afkBooter) {
        this.afkBooter = afkBooter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerChatEvent event) {
        afkBooter.getPlayerActivity().recordActivity(event.getPlayer().getName());
    }
}
