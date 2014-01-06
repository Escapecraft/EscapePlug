package org.tulonsae.triggers;

/**
 * Represents a trigger that displays a message.
 */
public class MessageTrigger {

    protected int id = 0;
    protected int priority = 0;
    protected boolean active = true;
    protected String name = "";
    protected String message = "";
    protected String target[] = null;
    protected String eventType = null;

    /**
     * Gets the event type.
     *
     * @return the event type, null if none
     */
    protected String getEventType() {
        return eventType;
    }

    /**
     * Gets the active state.
     *
     * @return true if active, false if inactive
     */
    protected boolean getActiveState() {
        return active;
    }

    /**
     * Gets the priority.
     *
     * @return the priority
     */
    protected int getPriority() {
        return priority;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    protected String getName() {
        return name;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    protected String getMessage() {
        return message;
    }

    /**
     * Sets the persistent store id.
     *
     * @param id the trigger's db id
     */
    protected void setId(int id) {
        this.id = id;
    }
}
