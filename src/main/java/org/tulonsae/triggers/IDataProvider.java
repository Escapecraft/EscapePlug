package org.tulonsae.triggers;

import java.sql.SQLException;
import java.util.HashSet;

/**
 * Data provider interface for Triggers.
 */
public interface IDataProvider {

    // The event types supported.
    public HashSet getEventTypes();
}
