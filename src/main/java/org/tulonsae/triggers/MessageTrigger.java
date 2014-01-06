package org.tulonsae.triggers;

/**
 * Represents a trigger that displays a message.
 */
public class MessageTrigger {

    protected String name = null;
    protected String message = "";
    protected String target[] = null;
    protected String eventType = null;
    protected int priority = 0;

    /**
     * Gets the event type.
     *
     * @return the event type, null if none
     */
    protected String getEventType() {
        return eventType;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    protected String getMessage() {
        return message;
    }
}
