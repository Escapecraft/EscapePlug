package net.escapecraft.component;

import net.escapecraft.escapeplug.EscapePlug;

/**
 * Represents an abstract component of EscapePlug.

 * @author james
 */
public abstract class AbstractComponent {

    /**
     * Called upon being enabled.
     * @param plugin instance of EscapePlug
     */
    public abstract boolean enable(Log log, EscapePlug plugin);

    /**
     * Called during onDisable().
     */
    public abstract void disable();

    /**
     * Called to tell the plugin to re-check it's config.
     */
    public void reloadConfig() {};
}
