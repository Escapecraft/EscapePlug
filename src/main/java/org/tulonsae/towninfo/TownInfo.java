package org.tulonsae.towninfo;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.escapecraft.escapePlug.EscapePlug;

/**
 * TownInfo component of EscapePlug.
 *
 * @author Tulonsae
 */
public class TownInfo {

    private static final String VERSION = "0.1";

    private EscapePlug plugin;
    private Logger log;

    /**
     * Construct this component
     * @param plugin EscapePlug plugin
     */
    public TownInfo (EscapePlug plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();

        if (!enable()) {
            log.warning("TownInfo didn't enable properly.");
        }
    }

    /**
     * Called during onEnable()
     */
    public boolean enable() {

        // load configuration
        loadConfig();

        return true;
    }

    /**
     * Called during onDisable()
     */
    public void tidyUp() {
    }

    /**
     * Get EscapePlug plugin.
     * @return plugin passed into this component.
     */
    public EscapePlug getPlugin() {
        return plugin;
    }

    /**
     * Get configuration settings from escapeplug config file.
     */
    private void loadConfig() {

        plugin.getConfig().set("plugin.towninfo.version", VERSION);

        // save any changes, esp the version
        plugin.saveConfig();
    }
}
