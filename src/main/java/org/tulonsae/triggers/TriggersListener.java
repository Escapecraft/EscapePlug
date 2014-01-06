package org.tulonsae.triggers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listens for and handles events.
 */
public class TriggersListener implements Listener {

    private TriggersComponent component = null;
    private TriggersManager manager = null;

    private HashMap<String, Boolean> flags = null;

    /**
     * Contructs the event listener.
     *
     * @param component the Triggers component
     * @param manager the Triggers manager
     */
    public TriggersListener(TriggersComponent component, TriggersManager manager) {
        this.component = component;
        this.manager = manager;

        // register with the manager and gets the initial flag settings
        // when the flags change, the manager will update this via setFlags
        this.flags = manager.register(this);
    }

    /**
     * Sets the event flags list.
     * <p>This should be only called by the manager.</p>
     */
    void setFlags(HashMap<String, Boolean> flags) {
        this.flags = flags;
    }

    /**
     * Handles PlayerJoinEvent.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerJoinEvent event) {
        // if no triggers for this event, just return
        if (!flags.get(event.getEventName())) {
            return;
        }

        // process all triggers for PlayerJoinEvent, in priority order
        ArrayList <MessageTrigger> list = manager.getByEventTriggers(event.getEventName());
        if (list != null) {
            for (MessageTrigger trigger : list) {
                event.getPlayer().sendMessage(trigger.getMessage());
            }
        }
    }
}
