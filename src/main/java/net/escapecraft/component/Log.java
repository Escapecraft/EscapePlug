package net.escapecraft.component;

import java.util.logging.Logger;

/**
 * Logger utility class for EscapePlug.
 *
 * @author Tulonsae
 */
public class Log {

    private String prefix;

    /**
     * Bukkit server logger
     */
    private static final Logger log = Logger.getLogger("Minecraft");

    /**
     * Log instance for main plugin
     */
    public Log(String pluginName) {
        this.prefix = "[" + pluginName + "]";
    }

    /**
     * Log instance for a plugin component
     */
    public Log(String pluginName, String componentName) {
        this.prefix = "[" + pluginName + ":" + componentName + "]";
    }

    /**
     * Sends an info level log to the console.
     */
    public void info(String msg) {
        log.info(prefix + " " + msg);
    }

    /**
     * Sends a warning level log to the console.
     */
    public void warning(String msg) {
        log.warning(prefix + " " + msg);
    }

    /**
     * Sends a severe level log to the console.
     */
    public void severe(String msg) {
        log.severe(prefix + " " + msg);
    }
}
