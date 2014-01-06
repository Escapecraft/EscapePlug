package org.tulonsae.triggers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Manages the triggers and all their data.
 *
 * @author Tulonsae
 */
public class TriggersManager {

    private TriggersComponent component = null;
    private IDataProvider db = null;
    private TriggersListener listener = null;

    // list of all event types handled
    private HashSet<String> eventTypes = null;
    // flags records whether any triggers exist for each event
    // listener events don't bother to check for data if false
    // so keep it updated
    private HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
    // TODO - this will change
    private HashMap<String, ArrayList<MessageTrigger>> byEvents = new HashMap<String, ArrayList<MessageTrigger>>();

    /**
     * Constructs this manager.
     *
     * @param component the Triggers component
     */
    public TriggersManager(TriggersComponent component, IDataProvider db) {
        this.component = component;
        this.db = db;

        // initialise event mappings
        eventTypes = db.getEventTypes();
        for (String eventType : eventTypes) {
            flags.put(eventType, false);  // TODO - get real data MUST DO!!!
        }
    }

    /**
     * Registers a listener with this manager.
     *
     * @param listener the listener to register
     * @return a list of flags for each event type
     */
    protected HashMap<String, Boolean> register(TriggersListener listener) {
        this.listener = listener;
        return flags;
    }

    /**
     * Adds a new trigger.
     *
     * @param trigger the trigger to add
     */
    protected void addTrigger(MessageTrigger trigger) {
        // TODO - add target
        // add to internal data structure
        String eventType = trigger.getEventType();
        if (byEvents.containsKey(eventType)) {         // add to list
            // TODO for now, add to end of list, later by priority
            byEvents.get(eventType).add(trigger);
        } else {                                       // create new list
            ArrayList<MessageTrigger> list = new ArrayList<MessageTrigger>();
            list.add(trigger);
            byEvents.put(eventType, list);
            flags.put(eventType, true);
            listener.setFlags(flags);
        }

        // save to persistent data store
        trigger.setId(db.saveTrigger(trigger));
    }

    /**
     * Gets a list of triggers for an event.
     *
     * @param eventType the event type
     * @returns a list of triggers, null if none of the type specified
     */
    protected ArrayList<MessageTrigger> getByEventTriggers(String eventType) {
        if (byEvents.containsKey(eventType)) {
            return byEvents.get(eventType);
        }

        return null;
    }
}
