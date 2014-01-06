package org.tulonsae.triggers;

/**
 * Represents a trigger that displays an motd message.
 */
public class MotdTrigger extends MessageTrigger {

    public MotdTrigger(String[] target, String message) {
        this.target = target;  // TODO - process this into groups and players
        this.message = message;
        eventType = "PlayerJoinEvent";
        name = "motd";
    }
}
