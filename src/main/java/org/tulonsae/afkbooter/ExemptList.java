package org.tulonsae.afkbooter;

import java.util.concurrent.ConcurrentSkipListSet;

import net.escapecraft.escapePlug.EscapePlug;

/**
 * Exempt players list.
 * This is placed into its own class so that it can be synchronized properly.
 *
 * @author Tulonsae
 */
public class ExemptList {

    private String CONFIG_EXEMPT_PLAYERS = "plugin.afkbooter.exempt-players";

    private AfkBooter afkBooter;
    private EscapePlug plugin;

    private ConcurrentSkipListSet<String> exemptPlayers;

    /**
     * Construct this object.
     * @param afkBooter AfkBooter component object.
     */
    public ExemptList(AfkBooter afkBooter) {
        this.afkBooter = afkBooter;
        this.plugin = afkBooter.getPlugin();

        exemptPlayers = new ConcurrentSkipListSet<String>();

        sendDebugMsg("created ExemptList object.");
    }

    /**
     * Send an info message to the component object.
     * @param message Information message.
     */
    private void sendInfoMsg(String message) {
        afkBooter.writeInfoMsg(message);
    }

    /**
     * Send a debug message to the component object.
     * @param message Debug message.
     */
    private void sendDebugMsg(String message) {
        afkBooter.writeDebugMsg(this.toString() + " " + message);
    }

    /**
     * Add player name safely.
     * @param name Player name to add.
     */
    private synchronized void addName(String name) {
        exemptPlayers.add(name);
    }

    /**
     * Remove player name safely.
     * @param name Player name to remove.
     */
    private synchronized void removeName(String name) {
        exemptPlayers.remove(name);
    }

    /**
     * Save exempt list to config file.
     */
    private void saveConfig() {
        plugin.getConfig().set(CONFIG_EXEMPT_PLAYERS, exemptPlayers.toString());
        plugin.saveConfig();
    }

    /**
     * Add player to the exempt list.
     * @param player Name of the player to add.
     * @param name Name of the command sender.
     */
    public void addPlayer(String player, String name) {
        addName(player);
        sendInfoMsg(name + " added player '" + player + "' to the exempt list.");
        saveConfig();
    }

    /**
     * Remove player from the exempt list.
     * @param player Name of the player to remove.
     * @param name Name of the command sender.
     */
    public void removePlayer(String player, String name) {
        removeName(player);
        sendInfoMsg(name + " removed player '" + player + "' from the exempt list.");
        saveConfig();
    }

    /**
     * Get exempt players list.
     */
    public ConcurrentSkipListSet<String> getPlayers() {
        return exemptPlayers;
    }

    /**
     * Get configuration settings from escapeplug config file.
     */
    private void loadConfig() {

        // get exempt player list
        String exempt = plugin.getConfig().getString(CONFIG_EXEMPT_PLAYERS);
        exempt = exempt.replace('[', ' ');
        exempt = exempt.replace(']', ' ');
        if (exempt != null) {
            String[] exemptSplit = exempt.split(",");
            if (exemptSplit != null) {
                for (String name : exemptSplit) {
                    name = name.trim().toLowerCase();
                    if (name.length() > 0) {
                        exemptPlayers.add(name);
                    }
                }
            }
        }
    }
}
