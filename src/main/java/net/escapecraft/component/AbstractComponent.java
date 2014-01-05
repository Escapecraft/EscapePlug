package net.escapecraft.component;

import net.escapecraft.escapeplug.EscapePlug;

/**
 * Represents an abstract component of EscapePlug.

 * @author james
 */
public abstract class AbstractComponent {

    protected Log log = null;
    protected String version = null;
    protected String schema = null;

    /**
     * Gets the component log.
     *
     * @returns the log
     */
    public Log getLog() {
        return log;
    }

    /**
     * Sets the component log.
     *
     * @param log component log
     */
    public void setLog(Log log) {
        this.log = log;
    }

    /**
     * Gets the component version.
     *
     * @returns the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the component version.
     *
     * @param version component version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the component's schema version.
     *
     * @returns the schema version
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Sets the component's schema version.
     *
     * @param version schema version
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * Called upon being enabled.
     *
     * @param plugin instance of EscapePlug
     */
    public abstract boolean enable(EscapePlug plugin);

    /**
     * Called during onDisable().
     */
    public abstract void disable();

    /**
     * Called to tell the plugin to re-check it's config.
     */
    public void reloadConfig() {};
}
