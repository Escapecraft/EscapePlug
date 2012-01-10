package net.escapecraft.escapePlug.component;

import org.bukkit.event.Event;


/**
 * Represents a bukkit event
 * @author james
 *
 */
public @interface BukkitEvent {
Event.Type type();
Event.Priority priority();
}
