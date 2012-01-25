package net.escapecraft.component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.bukkit.event.Event;


/**
 * Represents a bukkit event
 * @author james
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BukkitEvent {
Event.Type type();
Event.Priority priority();
}
